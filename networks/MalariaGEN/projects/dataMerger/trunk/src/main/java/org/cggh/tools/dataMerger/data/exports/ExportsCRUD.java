package org.cggh.tools.dataMerger.data.exports;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.code.settings.SettingsCRUD;
import org.cggh.tools.dataMerger.code.settings.SettingsModel;
import org.cggh.tools.dataMerger.data.databases.DatabaseModel;
import org.cggh.tools.dataMerger.data.databases.DatabasesCRUD;
import org.cggh.tools.dataMerger.data.files.FileOriginModel;
import org.cggh.tools.dataMerger.data.files.FileOriginsCRUD;
import org.cggh.tools.dataMerger.data.files.FilesCRUD;
import org.cggh.tools.dataMerger.data.joins.JoinsCRUD;
import org.cggh.tools.dataMerger.data.mergedDatatables.MergedDatatablesCRUD;
import org.cggh.tools.dataMerger.data.merges.MergesCRUD;
import org.cggh.tools.dataMerger.data.resolutions.ResolutionsCRUD;
import org.cggh.tools.dataMerger.data.resolutions.ResolutionsModel;
import org.cggh.tools.dataMerger.data.users.UserModel;
import org.cggh.tools.dataMerger.files.filebases.FilebaseModel;
import org.cggh.tools.dataMerger.functions.data.exports.ExportsFunctions;

public class ExportsCRUD implements java.io.Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2386709583926211005L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.exports");
	
	private DatabaseModel databaseModel;
	private FilebaseModel filebaseModel;
	private String exportsFilebaseDirectoryPathRelativeToFilebaseServerPath = "exports";
	
	public ExportsCRUD() {

		this.setDatabaseModel(new DatabaseModel());
		this.setFilebaseModel(new FilebaseModel());
	
		
	}


	public ExportModel retrieveExportAsExportModelThroughCreatingExportUsingExportModel(
			ExportModel exportModel) {

		try {
			
			Connection connection = this.getDatabaseModel().getNewConnection();
			
			if (connection != null) {
		
			
			      try {		
					
			    	  
						// Get the merge model for the export.
						MergesCRUD mergesModel = new MergesCRUD();
						mergesModel.setDatabaseModel(this.getDatabaseModel());
						exportModel.setMergeModel(mergesModel.retrieveMergeAsMergeModelByMergeId(exportModel.getMergeModel().getId()));
						
					
						//Insert the export record
						PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO export (source_file_1_id, source_file_2_id, source_file_1_filepath, source_file_2_filepath, created_by_user_id, created_datetime) VALUES (?, ?, ?, ?, ?, NOW());");
						preparedStatement.setInt(1, exportModel.getMergeModel().getFile1Model().getId());
						preparedStatement.setInt(2, exportModel.getMergeModel().getFile2Model().getId());
						preparedStatement.setString(3, exportModel.getMergeModel().getFile1Model().getFilepath());
						preparedStatement.setString(4, exportModel.getMergeModel().getFile2Model().getFilepath());
						preparedStatement.setInt(5, exportModel.getCreatedByUserModel().getId());
						preparedStatement.executeUpdate();
						preparedStatement.close();  
			    	  
						// Get the export Id (the last insert id)
						DatabasesCRUD databasesCRUD = new DatabasesCRUD();
						exportModel.setId(databasesCRUD.retrieveLastInsertIdAsIntegerUsingConnection(connection));
						
						
						// Create the export datatable
						exportModel = this.retrieveExportAsExportModelThroughRecreatingMergedDatatableUsingExportModel(exportModel, connection);
						
						
						// Get all the unique keys and add to export table.
						exportModel = this.retrieveExportAsExportModelThroughInsertingKeysIntoMergedDatatableUsingExportModel(exportModel, connection);
						
						
						// Fill the export datatable with data from source 1 
						// Fill the export datatable with data from source 2
						exportModel = this.retrieveExportAsExportModelThroughUpdatingMergedDatatableWithDatatableDataUsingExportModel(exportModel, connection);
					
						
						// Amend the export datatable with resolutions
						exportModel = this.retrieveExportAsExportModelThroughUpdatingMergedDatatableWithResolvedDataUsingExportModel(exportModel, connection);
						
						// Remove all rows from the export datatable that have been marked for removal by the resolutions
						exportModel = this.retrieveExportAsExportModelThroughRemovingMarkedRowsFromMergedDatatableUsingExportModel(exportModel, connection);
						
						
						// Export the export datatable into a file.
						this.createMergedDatatableAsFileUsingExportModel(exportModel, connection);
						
						
						// Export the joins into a file.
						this.createJoinsAsFileUsingExportModel(exportModel, connection);
						
						
						// Export the Resolutions into a file
						this.createResolutionsAsFileUsingExportModel(exportModel, connection);
						
						// Export the Settings into a file
						this.createSettingsAsFileUsingExportId(exportModel.getId(), connection);
					
					
				      //End of export algorithm
				      
		
			        }
			        catch(SQLException sqlException){
				    	sqlException.printStackTrace();
			        }  finally {
						
						try {
							connection.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						
					}			

				
			} else {
				
				//Connection is null (should have handled an exception and redirected before now. 
				this.logger.severe("connection is null");
				
			}
				
		} 
		catch (Exception e) {
			//System.out.println("Exception from createMerge.");
			e.printStackTrace();
		}
  	
		return exportModel;
	}



	//TODO: should probably refactor a lot of this file stuff into the files component.
	public void createMergedDatatableAsFileUsingExportModel(
			ExportModel exportModel, Connection connection) {

		
		File exportDirectory = new File(this.getFilebaseModel().getServerPath() + this.getExportsFilebaseDirectoryPathRelativeToFilebaseServerPath() + this.getFilebaseModel().getFilepathSeparator() + exportModel.getId().toString());
		
		//this.logger.info("exportDirectory created: " + exportDirectory.mkdirs());
		
		exportDirectory.mkdirs();
		
		//TODO: Make this writable for MySQL
		//exportDirectory.setWritable(true); //This would only make it writable for tomcat
		String pathSeparatorForSQL = "\\\\";
		String pathSeparatorForRepositoryFilepath = "\\";
		if(this.getFilebaseModel().getOperatingSystem().equals("nix")){
			pathSeparatorForSQL = "/";
			pathSeparatorForRepositoryFilepath = "/";
			try {
				Runtime.getRuntime().exec("chmod g+w " + exportDirectory.getAbsolutePath());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		MergedDatatablesCRUD mergedDatatablesModel = new MergedDatatablesCRUD();
		
		exportModel.getMergedDatatableModel().setDataAsCachedRowSet(mergedDatatablesModel.retrieveDataAsCachedRowSetByMergedDatatableName(exportModel.getMergedDatatableModel().getName(), connection));
		

		
		String mergedDatatableColumnNamesForSelectSQL = "";
		
		try {
			for (int i = 1; i <= exportModel.getMergedDatatableModel().getDataAsCachedRowSet().getMetaData().getColumnCount(); i++) {
				
				if (i != 1) {
					
					mergedDatatableColumnNamesForSelectSQL += ", ";
				}
				mergedDatatableColumnNamesForSelectSQL += "'" + exportModel.getMergedDatatableModel().getDataAsCachedRowSet().getMetaData().getColumnName(i) + "'";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		SettingsCRUD settingsCRUD = new SettingsCRUD();
		
		SettingsModel settingsModel = settingsCRUD.retrieveSettingsAsSettingsModel(connection);
		
		String mergedDatatableColumnNamesWithIfNullConditionsForSelectSQL = "";
		
		//TODO: Sanitize stringToExportInsteadOfNull
		
		try {
			for (int i = 1; i <= exportModel.getMergedDatatableModel().getDataAsCachedRowSet().getMetaData().getColumnCount(); i++) {
				
				if (i != 1) {
					
					mergedDatatableColumnNamesWithIfNullConditionsForSelectSQL += ", ";
				}
				mergedDatatableColumnNamesWithIfNullConditionsForSelectSQL += "IFNULL(`" + exportModel.getMergedDatatableModel().getDataAsCachedRowSet().getMetaData().getColumnName(i) + "`, '" + settingsModel.getSettingsAsHashMap().get("stringToExportInsteadOfNull") + "')";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			
			String filename = exportModel.getMergedFileAsFileModel().getFilename();
			
			String createMergedDatatableAsFileSQL =
				"(SELECT " + mergedDatatableColumnNamesForSelectSQL + ") " + 
				"UNION " +
				"(SELECT " + mergedDatatableColumnNamesWithIfNullConditionsForSelectSQL + " FROM `" + exportModel.getMergedDatatableModel().getName() + "` INTO OUTFILE '" + exportDirectory.getAbsolutePath().replace("\\", "\\\\") +  
				pathSeparatorForSQL + filename + "' " +
						"FIELDS ESCAPED BY '\\\\' OPTIONALLY ENCLOSED BY '\"' TERMINATED BY ',' " +
						"LINES TERMINATED BY '\\n' " +
				")" +
				";";		
			
			//this.logger.info("createMergedDatatableAsFileSQL: " + createMergedDatatableAsFileSQL);
			
			PreparedStatement preparedStatement = connection.prepareStatement(createMergedDatatableAsFileSQL);
			preparedStatement.executeQuery();
			preparedStatement.close();		

		
			exportModel.getMergedFileAsFileModel().setFilepath(exportDirectory.toString() + pathSeparatorForRepositoryFilepath + filename);
			
			
			FilesCRUD filesCRUD = new FilesCRUD();
			
			FileOriginsCRUD fileOriginsCRUD = new FileOriginsCRUD();
			FileOriginModel fileOriginModel = new FileOriginModel();
			fileOriginModel.setId(fileOriginsCRUD.retrieveFileOriginsAsOriginToIdHashMap(connection).get("export"));
			fileOriginModel.setOrigin("export");
			exportModel.getMergedFileAsFileModel().setFileOriginModel(fileOriginModel);
			exportModel.getMergedFileAsFileModel().setCreatedByUserModel(exportModel.getCreatedByUserModel());

			filesCRUD.createFileUsingFileModel(exportModel.getMergedFileAsFileModel(), connection);
			
			DatabasesCRUD databasesCRUD = new DatabasesCRUD();
			exportModel.getMergedFileAsFileModel().setId(databasesCRUD.retrieveLastInsertIdAsIntegerUsingConnection(connection));
			exportModel.getMergedFileAsFileModel().setFilepath(exportModel.getMergedFileAsFileModel().getFilepath());
			
			File mergedFile = new File(exportModel.getMergedFileAsFileModel().getFilepath());
            
            if (mergedFile.exists()) {
            	
            	exportModel.getMergedFileAsFileModel().setFileSizeInBytes(mergedFile.length());
            	filesCRUD.updateFileFilepathAndFileSizeInBytesUsingFileModel(exportModel.getMergedFileAsFileModel(), connection);
            	this.updateExportMergedFileUsingExportModel(exportModel, connection);
            		
            }
            
            
			
			//mergedFileAsFileModel.getFileSizeInBytes();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			this.logger.severe(e.getMessage());
			e.printStackTrace();
		}
	}

	public void updateExportMergedFileUsingExportModel(
			ExportModel exportModel, Connection connection) {
		
		try {
			//Update the export table
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `export` SET merged_file_id = ?, merged_file_filepath = ? WHERE id = ?;");
			
			preparedStatement.setInt(1, exportModel.getMergedFileAsFileModel().getId());
			preparedStatement.setString(2, exportModel.getMergedFileAsFileModel().getFilepath());
			preparedStatement.setInt(3, exportModel.getId());
			
			preparedStatement.executeUpdate();
			preparedStatement.close();  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}		




	public void createJoinsAsFileUsingExportModel(ExportModel exportModel,
			Connection connection) {

		File exportDirectory = new File(this.getFilebaseModel().getServerPath() + this.getExportsFilebaseDirectoryPathRelativeToFilebaseServerPath() + this.getFilebaseModel().getFilepathSeparator() + exportModel.getId().toString());
		
		//this.logger.info("exportDirectory created: " + exportDirectory.mkdirs());
		
		exportDirectory.mkdirs();
		
		//TODO: Make this writable for MySQL
		//exportDirectory.setWritable(true); //This would only make it writable for tomcat
		String pathSeparatorForSQL = "\\\\";
		if(this.getFilebaseModel().getOperatingSystem().equals("nix")){
			pathSeparatorForSQL = "/";
			try {
				Runtime.getRuntime().exec("chmod g+w " + exportDirectory.getAbsolutePath());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		try {
			
			String fileName = "join_" + exportModel.getId() + ".csv";
			
			String createJoinsAsFileSQL =
				"(SELECT 'Export ID', 'Column Number', 'Key', 'Source 1 Column Name', 'Source 2 Column Name', 'Source 1 Constant', 'Source 2 Constant', 'Merged Column Name') " + 
				"UNION " +
				"(SELECT " + exportModel.getId() + ", `join`.column_number, IF(`join`.`key`,'Yes','No'), `join`.datatable_1_column_name, `join`.datatable_2_column_name, `join`.constant_1, `join`.constant_2, `join`.column_name " +
				"FROM `join` " +
				"JOIN `merge` ON `merge`.id = `join`.merge_id " +
				"WHERE merge_id = " + exportModel.getMergeModel().getId() + " " +
				"INTO OUTFILE '" + exportDirectory.getAbsolutePath().replace("\\", "\\\\") +  
				pathSeparatorForSQL + fileName + "' " +
						"FIELDS ESCAPED BY '\\\\' OPTIONALLY ENCLOSED BY '\"' TERMINATED BY ',' " +
						"LINES TERMINATED BY '\\n' " +
				")" +
				";";		
			
			//this.logger.info("createJoinsAsFileSQL: " + createJoinsAsFileSQL);
			
			PreparedStatement preparedStatement = connection.prepareStatement(createJoinsAsFileSQL);
			preparedStatement.executeQuery();
			preparedStatement.close();		

			exportModel.getMergeModel().getJoinsModel().setExportRepositoryFilepath(exportDirectory.toString() + this.getFilebaseModel().getFilepathSeparator() + fileName);
			
			this.updateExportJoinsRecordFilepathUsingExportModel(exportModel, connection);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void updateExportJoinsRecordFilepathUsingExportModel(
			ExportModel exportModel, Connection connection) {
		
		try {
			//Update the export table
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `export` SET joins_record_filepath = ? WHERE id = ?;");
			
			preparedStatement.setString(1, exportModel.getMergeModel().getJoinsModel().getExportRepositoryFilepath());
			preparedStatement.setInt(2, exportModel.getId());
			
			preparedStatement.executeUpdate();
			preparedStatement.close();  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

		


	
	


	public void createResolutionsAsFileUsingExportModel(
			ExportModel exportModel, Connection connection) {

		File exportDirectory = new File(this.getFilebaseModel().getServerPath() + this.getExportsFilebaseDirectoryPathRelativeToFilebaseServerPath() + this.getFilebaseModel().getFilepathSeparator() + exportModel.getId().toString());
		
		//this.logger.info("exportDirectory created: " + exportDirectory.mkdirs());
		
		exportDirectory.mkdirs();
		
		//TODO: Make this writable for MySQL
		//exportDirectory.setWritable(true); //This would only make it writable for tomcat
		String pathSeparatorForSQL = "\\\\";
		if(this.getFilebaseModel().getOperatingSystem().equals("nix")){
			pathSeparatorForSQL = "/";
			try {
				Runtime.getRuntime().exec("chmod g+w " + exportDirectory.getAbsolutePath());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		try {
			
			String fileName = "resolutions_" + exportModel.getId() + ".csv";
			
			String keyColumnNamesForSelectSQL = "";
			String joinedKeytableKeyColumnNameAliasesForSelectSQL = "";
			
			JoinsCRUD joinsCRUD = new JoinsCRUD();
			
			CachedRowSet keyJoinsAsCachedRowSet = joinsCRUD.retrieveKeyJoinsAsCachedRowSetByMergeId(exportModel.getMergeModel().getId(), connection);
			
			if (keyJoinsAsCachedRowSet.next()) {
				
				keyJoinsAsCachedRowSet.beforeFirst();
				
				while (keyJoinsAsCachedRowSet.next()) {
					keyColumnNamesForSelectSQL += ", '" + keyJoinsAsCachedRowSet.getString("column_name") + "' ";
					
					
					joinedKeytableKeyColumnNameAliasesForSelectSQL += ", `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "`.`key_column_" + keyJoinsAsCachedRowSet.getString("column_number") + "` AS '" + keyJoinsAsCachedRowSet.getString("column_name") + "' ";
				}
				
			}
			
			String createResolutionsAsFileSQL =
				"(SELECT 'Export ID' " + keyColumnNamesForSelectSQL + ", 'Column Number', 'Solution Description', 'Constant') " + 
				"UNION " +
				"(SELECT " + exportModel.getId() + joinedKeytableKeyColumnNameAliasesForSelectSQL + ", column_number, IF(solution_by_column.description IS NULL, IF(solution_by_row.description IS NULL, IF(solution_by_cell.description IS NULL, NULL, solution_by_cell.description), solution_by_row.description), solution_by_column.description), constant " + 
				"FROM `resolution` " + 
				"JOIN `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "` ON `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "`.id = resolution.joined_keytable_id " + 
				"LEFT JOIN solution_by_column ON solution_by_column.id = resolution.solution_by_column_id " + 
				"LEFT JOIN solution_by_row ON solution_by_row.id = resolution.solution_by_row_id " + 
				"LEFT JOIN solution_by_cell ON solution_by_cell.id = resolution.solution_by_cell_id " + 
				"WHERE merge_id = " + exportModel.getMergeModel().getId() + " " +
				"INTO OUTFILE '" + exportDirectory.getAbsolutePath().replace("\\", "\\\\") +  
				pathSeparatorForSQL + fileName + "' " +
						"FIELDS ESCAPED BY '\\\\' OPTIONALLY ENCLOSED BY '\"' TERMINATED BY ',' " +
						"LINES TERMINATED BY '\\n' " +
				")" +
				";";		
			
			//this.logger.info("createResolutionsAsFileSQL: " + createResolutionsAsFileSQL);
			
			PreparedStatement preparedStatement = connection.prepareStatement(createResolutionsAsFileSQL);
			preparedStatement.executeQuery();
			preparedStatement.close();		


			exportModel.getMergeModel().setResolutionsModel(new ResolutionsModel());
			exportModel.getMergeModel().getResolutionsModel().setExportRecordFilepath(exportDirectory.toString() + this.getFilebaseModel().getFilepathSeparator() + fileName);

			this.updateExportResolutionsRecordFilepathUsingExportModel(exportModel, connection);

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



	public void updateExportResolutionsRecordFilepathUsingExportModel(
			ExportModel exportModel, Connection connection) {

		try {
			//Update the export table
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `export` SET resolutions_record_filepath = ? WHERE id = ?;");
			
			preparedStatement.setString(1, exportModel.getMergeModel().getResolutionsModel().getExportRecordFilepath());
			preparedStatement.setInt(2, exportModel.getId());
			
			preparedStatement.executeUpdate();
			preparedStatement.close();  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	public void createSettingsAsFileUsingExportId(
			Integer exportId, Connection connection) {

		File exportDirectory = new File(this.getFilebaseModel().getServerPath() + this.getExportsFilebaseDirectoryPathRelativeToFilebaseServerPath() + this.getFilebaseModel().getFilepathSeparator() + exportId.toString());
		
		//this.logger.info("exportDirectory created: " + exportDirectory.mkdirs());
		
		exportDirectory.mkdirs();

		//TODO: This is not needed (the folder has already been created), although I concede it keeps it modular
		
		//TODO: Make this writable for MySQL
		//exportDirectory.setWritable(true); //This would only make it writable for tomcat
		String pathSeparatorForSQL = "\\\\";
		if(this.getFilebaseModel().getOperatingSystem().equals("nix")){
			pathSeparatorForSQL = "/";
			try {
				Runtime.getRuntime().exec("chmod g+w " + exportDirectory.getAbsolutePath());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		try {
			
			String fileName = "settings_" + exportId + ".csv";
			

			String createSettingsAsFileSQL =
				"SELECT name, value FROM setting " + 
				"INTO OUTFILE '" + exportDirectory.getAbsolutePath().replace("\\", "\\\\") +  
				pathSeparatorForSQL + fileName + "' " +
						"FIELDS ESCAPED BY '\\\\' OPTIONALLY ENCLOSED BY '\"' TERMINATED BY ',' " +
						"LINES TERMINATED BY '\\n' " +
				";";		
			
			//
			//logger.info(createSettingsAsFileSQL);
			
			PreparedStatement preparedStatement = connection.prepareStatement(createSettingsAsFileSQL);
			preparedStatement.executeQuery();
			preparedStatement.close();		


			String settingsRecordFilepath = exportDirectory.toString() + this.getFilebaseModel().getFilepathSeparator() + fileName;

			this.updateExportSettingsRecordFilepathUsingExportIdAndSettingsRecordFilepath(exportId, settingsRecordFilepath, connection);

			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}



	public void updateExportSettingsRecordFilepathUsingExportIdAndSettingsRecordFilepath(
			Integer exportId, String settingsRecordFilepath, Connection connection) {

		try {
			//Update the export table
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `export` SET settings_record_filepath = ? WHERE id = ?;");
			
			preparedStatement.setString(1, settingsRecordFilepath);
			preparedStatement.setInt(2, exportId);
			
			preparedStatement.executeUpdate();
			preparedStatement.close();  
		} catch (SQLException e) {
			
			e.printStackTrace();
		}

	}

	public ExportModel retrieveExportAsExportModelThroughUpdatingMergedDatatableWithResolvedDataUsingExportModel(
			ExportModel exportModel, Connection connection) {


		ResolutionsCRUD resolutionsModel = new ResolutionsCRUD();
		
		CachedRowSet resolutionsAsCachedRowSet = resolutionsModel.retrieveResolutionsAsCachedRowSetUsingMergeId(exportModel.getMergeModel().getId(), connection);
		
		try {
			resolutionsAsCachedRowSet.beforeFirst();

	        
	        if (resolutionsAsCachedRowSet.next()) {
	
	        	JoinsCRUD joinsModel = new JoinsCRUD();
	        	
	        	HashMap<Integer, String> joinColumnNamesByColumnNumberAsHashMap = joinsModel.retrieveJoinColumnNamesByColumnNumberAsHashMapUsingMergeModel(exportModel.getMergeModel(), connection);
	        	
	        	HashMap<Integer, String> datatable1ColumnNamesByColumnNumberAsHashMap = joinsModel.retrieveDatatable1ColumnNamesByColumnNumberAsHashMapUsingMergeId(exportModel.getMergeModel().getId(), connection);
	        	
	        	HashMap<Integer, String> datatable2ColumnNamesByColumnNumberAsHashMap = joinsModel.retrieveDatatable2ColumnNamesByColumnNumberAsHashMapUsingMergeId(exportModel.getMergeModel().getId(), connection);
	        	
	        	CachedRowSet keyJoinsAsCachedRowSet = joinsModel.retrieveKeyJoinsAsCachedRowSetByMergeId(exportModel.getMergeModel().getId(), connection);
	        	
	        	String mergedDatatableToJoinedKeytableMappingsForWhereClauseSQL = "";
	        	String mergedDatatableToDatatable1MappingsForWhereClauseSQL = "";
	        	String mergedDatatableToDatatable2MappingsForWhereClauseSQL = "";
	        	
	        	if (keyJoinsAsCachedRowSet.next()) {
	        		
	        		keyJoinsAsCachedRowSet.beforeFirst();
	        		
	        		while (keyJoinsAsCachedRowSet.next()) {
	        			
	        			mergedDatatableToJoinedKeytableMappingsForWhereClauseSQL += "AND `" + exportModel.getMergedDatatableModel().getName() + "`.`" + keyJoinsAsCachedRowSet.getString("column_name") + "` = `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "`.`key_column_" + keyJoinsAsCachedRowSet.getInt("column_number") + "` ";
	        	
	        			mergedDatatableToDatatable1MappingsForWhereClauseSQL += "AND `" + exportModel.getMergedDatatableModel().getName() + "`.`" + keyJoinsAsCachedRowSet.getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable1Model().getName()+ "`.`" + keyJoinsAsCachedRowSet.getString("datatable_1_column_name") + "` ";
	        			
	        			mergedDatatableToDatatable2MappingsForWhereClauseSQL += "AND `" + exportModel.getMergedDatatableModel().getName() + "`.`" + keyJoinsAsCachedRowSet.getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable2Model().getName()+ "`.`" + keyJoinsAsCachedRowSet.getString("datatable_2_column_name") + "` ";
	        			
	        		}
	        		
	        	}
	        	
	        	resolutionsAsCachedRowSet.beforeFirst();
	        	
		        while (resolutionsAsCachedRowSet.next()) {
		        
		        	//TODO: code
		        	
		        	Integer solutionByColumnId = resolutionsAsCachedRowSet.getInt("solution_by_column_id");
		        	Integer solutionByRowId = resolutionsAsCachedRowSet.getInt("solution_by_row_id");
		        	Integer solutionByCellId = resolutionsAsCachedRowSet.getInt("solution_by_cell_id");
		        	
		        	//
		        	String updateMergedDatatableSQL = "";

		        		
		        		if ((solutionByColumnId != null && solutionByColumnId == 1) || (solutionByRowId != null && solutionByRowId == 1) || (solutionByCellId != null && solutionByCellId == 1)) {
		        			
		        			// Prefer source 1
		        			updateMergedDatatableSQL = 
		        				"UPDATE `" + exportModel.getMergedDatatableModel().getName() + "`, `" + exportModel.getMergeModel().getDatatable1Model().getName() + "`, `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "` " +
		        					"SET `" + exportModel.getMergedDatatableModel().getName() + "`.`" + joinColumnNamesByColumnNumberAsHashMap.get(resolutionsAsCachedRowSet.getInt("column_number")) +"` = `" + exportModel.getMergeModel().getDatatable1Model().getName() + "`.`" + datatable1ColumnNamesByColumnNumberAsHashMap.get(resolutionsAsCachedRowSet.getInt("column_number")) + "` " +
		        					"WHERE `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "`.id = '" + resolutionsAsCachedRowSet.getInt("joined_keytable_id") + "' " + mergedDatatableToJoinedKeytableMappingsForWhereClauseSQL + mergedDatatableToDatatable1MappingsForWhereClauseSQL + 
		        				";";
		        			
		        			
		        		}
		        		else if ((solutionByColumnId != null && solutionByColumnId == 2) || (solutionByRowId != null && solutionByRowId == 2) || (solutionByCellId != null && solutionByCellId == 2)) {
		        			
		        			// Prefer source 2
		        			updateMergedDatatableSQL = 
		        				"UPDATE `" + exportModel.getMergedDatatableModel().getName() + "`, `" + exportModel.getMergeModel().getDatatable2Model().getName() + "`, `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "` " +
		        					"SET `" + exportModel.getMergedDatatableModel().getName() + "`.`" + joinColumnNamesByColumnNumberAsHashMap.get(resolutionsAsCachedRowSet.getInt("column_number")) +"` = `" + exportModel.getMergeModel().getDatatable2Model().getName() + "`.`" + datatable2ColumnNamesByColumnNumberAsHashMap.get(resolutionsAsCachedRowSet.getInt("column_number")) + "` " +
		        					"WHERE `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "`.id = '" + resolutionsAsCachedRowSet.getInt("joined_keytable_id") + "' " + mergedDatatableToJoinedKeytableMappingsForWhereClauseSQL + mergedDatatableToDatatable2MappingsForWhereClauseSQL +
		        				";";
		        			
		        		}
		        		else if ((solutionByColumnId != null && solutionByColumnId == 3) || (solutionByRowId != null && solutionByRowId == 3) || (solutionByCellId != null && solutionByCellId == 3)) {
		        			
		        			// Use NULL
		        			updateMergedDatatableSQL = 
		        				"UPDATE `" + exportModel.getMergedDatatableModel().getName() + "`, `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "` " +
		        					"SET `" + exportModel.getMergedDatatableModel().getName() + "`.`" + joinColumnNamesByColumnNumberAsHashMap.get(resolutionsAsCachedRowSet.getInt("column_number")) +"` = NULL " +
		        					"WHERE `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "`.id = '" + resolutionsAsCachedRowSet.getInt("joined_keytable_id") + "' " + mergedDatatableToJoinedKeytableMappingsForWhereClauseSQL +
		        				";";
		        			
		        		}
		        		else if ((solutionByColumnId != null && solutionByColumnId == 4) || (solutionByRowId != null && solutionByRowId == 4) || (solutionByCellId != null && solutionByCellId == 4)) {
		        			
		        			// Use Constant
		        			updateMergedDatatableSQL = 
		        				"UPDATE `" + exportModel.getMergedDatatableModel().getName() + "`, `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "` " +
		        					"SET `" + exportModel.getMergedDatatableModel().getName() + "`.`" + joinColumnNamesByColumnNumberAsHashMap.get(resolutionsAsCachedRowSet.getInt("column_number")) +"` = '" + resolutionsAsCachedRowSet.getString("constant") + "' " +
		        					"WHERE `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "`.id = '" + resolutionsAsCachedRowSet.getInt("joined_keytable_id") + "' " + mergedDatatableToJoinedKeytableMappingsForWhereClauseSQL +
		        				";";
		        		}
		        		//solution 5 (remove row) is dealt with separately, afterwards.
	
		        	if (updateMergedDatatableSQL != "") {
		        		
		        		//this.logger.info("updateMergedDatatableSQL: " + updateMergedDatatableSQL);
		        		
						PreparedStatement preparedStatement = connection.prepareStatement(updateMergedDatatableSQL);
						preparedStatement.executeUpdate();
						preparedStatement.close();
		        	}
		        	
		        }
		        
	        } else {
	        	//There may be no resolutions
	        	//this.logger.info("Did not retrieve any resolutions as a cached row set using merge Id: " + exportModel.getMergeModel().getId());
	        }
	        
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return exportModel;
	}


	public ExportModel retrieveExportAsExportModelThroughRemovingMarkedRowsFromMergedDatatableUsingExportModel(
			ExportModel exportModel, Connection connection) {
		
		


		try {		
			
			
			
			String SelectRowKeysForRemovalSQL = 
				"SELECT `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "`.* FROM resolution " +
				"JOIN `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "` ON `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "`.id = resolution.joined_keytable_id " +
				"WHERE merge_id = 1 AND solution_by_row_id = 5;" +
				";";
			
			//this.logger.info("SelectRowKeysForRemovalSQL: " + SelectRowKeysForRemovalSQL);
			
			PreparedStatement preparedStatement = connection.prepareStatement(SelectRowKeysForRemovalSQL);
			preparedStatement.executeQuery();
			
			// returns: id, key_column_2, key_column_21
			
	          ResultSet resultSet = preparedStatement.getResultSet();

	          
	          if (resultSet.next()) {

	      		//FIXME: detach joins CRUD from joins Set
	      		JoinsCRUD joinsModel = new JoinsCRUD();
	      		HashMap<Integer, String> joinColumnNamesByColumnNumberAsHashMap = joinsModel.retrieveJoinColumnNamesByColumnNumberAsHashMapUsingMergeModel(exportModel.getMergeModel(), connection);
	      		
	      		Pattern joinedKeytableKeyColumnNamePattern = Pattern.compile("^key_column_(\\d+)$");
	      		
	      		
	      		
	      		
	        	  resultSet.beforeFirst();

	        	  while (resultSet.next()) {
	        		  
	        		  
	        		  String deleteRowWhereConditionsSQL = "";
	        		  
	        		  for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
	  	      			
	        			  Matcher joinedKeytableKeyColumnNamePatternMatcher = joinedKeytableKeyColumnNamePattern.matcher(resultSet.getMetaData().getColumnName(i));
		        		  
	        			  if (joinedKeytableKeyColumnNamePatternMatcher.find()) {
	    	        		  
			        		  Integer columnNumber = Integer.parseInt(joinedKeytableKeyColumnNamePatternMatcher.group(1));
			        		  
			        		  String exportDatatableColumnName =  joinColumnNamesByColumnNumberAsHashMap.get(columnNumber);
			        		  
			        		  if (deleteRowWhereConditionsSQL != "") {
			        			  deleteRowWhereConditionsSQL += "AND ";
			        		  }
			        		  deleteRowWhereConditionsSQL += "`" + exportDatatableColumnName + "` = '" + resultSet.getString(resultSet.getMetaData().getColumnName(i)) + "' ";
			        		  
			        		  
		        		  }
	  	      			
	  	      		  }
	        		  
	        		  // Remove from export datatable
	  				String deleteRowFromMergedDatatableSQL = 
						"DELETE FROM `" + exportModel.getMergedDatatableModel().getName() + "` WHERE " + deleteRowWhereConditionsSQL + 
						";";
					
					//this.logger.info("deleteRowFromMergedDatatableSQL: " + deleteRowFromMergedDatatableSQL);
					
					PreparedStatement preparedStatement2 = connection.prepareStatement(deleteRowFromMergedDatatableSQL);
					preparedStatement2.executeUpdate();
					preparedStatement2.close();
	        		  
	        	  }

	          } else {
	        	// There may be no rows for exclusion.
	        	  //this.logger.info("Did not retrieve any rows for removal.");
	          }

	          resultSet.close();
			
			preparedStatement.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return exportModel;
	}



	public ExportModel retrieveExportAsExportModelThroughUpdatingMergedDatatableWithDatatableDataUsingExportModel(
			ExportModel exportModel, Connection connection) {
		
		// This populates the export datatable, first with data from source 1, then overwrites with data from source 2. (Conflicting cells will be dealt with separately afterwards.)
		
		try {		
		
			//Populate the export datatable with datatable data from source 1
			
			String datatable1NonCrossDatatableExportColumnMappingsForUpdateSetSQL = "";
			String datatable2NonCrossDatatableExportColumnMappingsForUpdateSetSQL = "";
			
			//FIXME: detach joins CRUD from joins Set
			//JoinsModel joinsModel = new JoinsModel();
			
			//TODO: check with if (.next()) ...  
			
			//TODO: Could getNonKeyJoinsAsCachedRowSet
			
			exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().beforeFirst();

			while (exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().next()) {
		
				if (exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getBoolean("key") != true) {
				
					if (exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("datatable_1_column_name") != null) {
						if (datatable1NonCrossDatatableExportColumnMappingsForUpdateSetSQL != "") {
							datatable1NonCrossDatatableExportColumnMappingsForUpdateSetSQL += ", ";
						}
						datatable1NonCrossDatatableExportColumnMappingsForUpdateSetSQL += "`" + exportModel.getMergedDatatableModel().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable1Model().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("datatable_1_column_name") + "` ";
					}
					else if (exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("datatable_1_column_name") == null && exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("constant_1") != null) {
						if (datatable1NonCrossDatatableExportColumnMappingsForUpdateSetSQL != "") {
							datatable1NonCrossDatatableExportColumnMappingsForUpdateSetSQL += ", ";
						}
						datatable1NonCrossDatatableExportColumnMappingsForUpdateSetSQL += "`" + exportModel.getMergedDatatableModel().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("column_name") + "` = '" + exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("constant_1") + "' ";
					}
					// Value will stay null by default
					
					if (exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("datatable_2_column_name") != null) {
						if (datatable2NonCrossDatatableExportColumnMappingsForUpdateSetSQL != "") {
							datatable2NonCrossDatatableExportColumnMappingsForUpdateSetSQL += ", ";
						}
						datatable2NonCrossDatatableExportColumnMappingsForUpdateSetSQL += "`" + exportModel.getMergedDatatableModel().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable2Model().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("datatable_2_column_name") + "` ";
					}
					else if (exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("datatable_2_column_name") == null && exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("constant_2") != null) {
						if (datatable2NonCrossDatatableExportColumnMappingsForUpdateSetSQL != "") {
							datatable2NonCrossDatatableExportColumnMappingsForUpdateSetSQL += ", ";
						}
						datatable2NonCrossDatatableExportColumnMappingsForUpdateSetSQL += "`" + exportModel.getMergedDatatableModel().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("column_name") + "` = '" + exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("constant_2") + "' ";
					}
				
				}
				
			}
			
	
			String datatable1KeyExportColumnMappingsForUpdateWhereSQL = "";
			String datatable2KeyExportColumnMappingsForUpdateWhereSQL = "";		
			
			exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().first();
			
			datatable1KeyExportColumnMappingsForUpdateWhereSQL += "`" + exportModel.getMergedDatatableModel().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable1Model().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("datatable_1_column_name") + "` ";
			datatable2KeyExportColumnMappingsForUpdateWhereSQL += "`" + exportModel.getMergedDatatableModel().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable2Model().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("datatable_2_column_name") + "` ";
			
			while (exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().next()) {

				
				datatable1KeyExportColumnMappingsForUpdateWhereSQL += "AND `" + exportModel.getMergedDatatableModel().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable1Model().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("datatable_1_column_name") + "` ";
				datatable2KeyExportColumnMappingsForUpdateWhereSQL += "AND `" + exportModel.getMergedDatatableModel().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable2Model().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("datatable_2_column_name") + "` ";
								
				
			}		
			
			if (datatable1NonCrossDatatableExportColumnMappingsForUpdateSetSQL != "") {
				String updateMergedDatatableWithDatatable1Data = 
					"UPDATE `" + exportModel.getMergedDatatableModel().getName() + "`, `" + exportModel.getMergeModel().getDatatable1Model().getName() + "` " + 
					"SET " + datatable1NonCrossDatatableExportColumnMappingsForUpdateSetSQL +
					"WHERE " + datatable1KeyExportColumnMappingsForUpdateWhereSQL + 
					";";
				
				//this.logger.info("updateMergedDatatableWithDatatable1Data: " + updateMergedDatatableWithDatatable1Data);
				
				PreparedStatement preparedStatement = connection.prepareStatement(updateMergedDatatableWithDatatable1Data);
				preparedStatement.executeUpdate();
				preparedStatement.close();
			}
			
			if (datatable2NonCrossDatatableExportColumnMappingsForUpdateSetSQL != "") {
			
				String updateMergedDatatableWithDatatable2Data = 
					"UPDATE `" + exportModel.getMergedDatatableModel().getName() + "`, `" + exportModel.getMergeModel().getDatatable2Model().getName() + "` " + 
					"SET " + datatable2NonCrossDatatableExportColumnMappingsForUpdateSetSQL +
					"WHERE " + datatable2KeyExportColumnMappingsForUpdateWhereSQL + 
					";";
				
				//this.logger.info("updateMergedDatatableWithDatatable2Data: " + updateMergedDatatableWithDatatable2Data);
				
				PreparedStatement preparedStatement2 = connection.prepareStatement(updateMergedDatatableWithDatatable2Data);
				preparedStatement2.executeUpdate();
				preparedStatement2.close(); 
			
			}
			
			
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		
		return exportModel;
	}


	public ExportModel retrieveExportAsExportModelThroughInsertingKeysIntoMergedDatatableUsingExportModel(
			ExportModel exportModel, Connection connection) {



		try {

			exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().beforeFirst();

			if (exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().next()) {
				
				////this.logger.info("Got key joins data as cached row set.");
				
				
				String keyColumnNamesForInsertSQL = "";
				String datatable1KeyColumnAliasesForSelectSQL = "";
				String datatable2KeyColumnAliasesForSelectSQL = "";

				
				exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().first();
				
				keyColumnNamesForInsertSQL += "`" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("column_name") + "`";
				
				datatable1KeyColumnAliasesForSelectSQL += "`" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("datatable_1_column_name") + "` AS `" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("column_name") + "`";
				
				datatable2KeyColumnAliasesForSelectSQL += "`" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("datatable_2_column_name") + "` AS `" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("column_name") + "`";
				
				while (exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().next()) {
				
					keyColumnNamesForInsertSQL += ", `" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("column_name") + "`";
					
					datatable1KeyColumnAliasesForSelectSQL += ", `" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("datatable_1_column_name") + "` AS `" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("column_name") + "`";
					
					datatable2KeyColumnAliasesForSelectSQL += ", `" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("datatable_2_column_name") + "` AS `" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("column_name") + "`";
					
				}
				
				
				try {
					
					//Populate the export datatable with all the keys
					PreparedStatement preparedStatement = connection.prepareStatement(
							"INSERT INTO `" + exportModel.getMergedDatatableModel().getName() + "` (" + keyColumnNamesForInsertSQL + ") " +
								"(SELECT " + datatable1KeyColumnAliasesForSelectSQL + " FROM " + exportModel.getMergeModel().getDatatable1Model().getName() + ") " +
								"UNION (SELECT " + datatable2KeyColumnAliasesForSelectSQL + " FROM " + exportModel.getMergeModel().getDatatable2Model().getName() + ") " +
								"ORDER BY " + keyColumnNamesForInsertSQL + 
							";");
					preparedStatement.executeUpdate();
					preparedStatement.close();  
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				
				
			} else {
				this.logger.severe("Did not get key joins data as cached row set.");
			}
		
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	
		
		
		return exportModel;
	}


	private ExportModel retrieveExportAsExportModelThroughRecreatingMergedDatatableUsingExportModel(
			ExportModel exportModel, Connection connection) {

		
		
		// Get the column names as a String List
		
		//FIXME: Detach JoinsCRUD from JoinColumnsModel
		JoinsCRUD joinsModel = new JoinsCRUD();
		joinsModel.setDatabaseModel(this.getDatabaseModel());
	
		HashMap<Integer, String> joinColumnNamesByColumnNumberAsHashMap = joinsModel.retrieveJoinColumnNamesByColumnNumberAsHashMapUsingMergeModel(exportModel.getMergeModel());
		
		//TODO: Make a (pseudo-temporary) table based on these columns.
		
		//TODO:
		
		String columnDefinitionsForCreateSQL = "";
		
		for (Integer i = 1; i <= joinColumnNamesByColumnNumberAsHashMap.size(); i++) {
			
			////this.logger.info("Column Number: " + i + ", Column Name: " + joinColumnNamesByColumnNumberAsHashMap.get(i));
			
			if (i != 1) {
				columnDefinitionsForCreateSQL += ", ";
			}
			columnDefinitionsForCreateSQL += "`" + joinColumnNamesByColumnNumberAsHashMap.get(i) +  "` VARCHAR(36) NULL";
		}
		
		////this.logger.info("columnDefinitionsForCreateSQL: " + columnDefinitionsForCreateSQL);
		
		exportModel.getMergedDatatableModel().setName("merged_datatable_" + exportModel.getMergeModel().getId());
		
		//FIXME: This is deprecated
		//this.updateExportMergedDatatableNameUsingExportModel(exportModel, connection);
		
		
		try {
			//Drop the export_datatable
			PreparedStatement preparedStatement = connection.prepareStatement("DROP TABLE IF EXISTS `" + exportModel.getMergedDatatableModel().getName() + "`;");
			preparedStatement.executeUpdate();
			preparedStatement.close();  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			//Create the export_datatable
			PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE `" + exportModel.getMergedDatatableModel().getName() + "` (" + columnDefinitionsForCreateSQL + ") ENGINE=InnoDB;");
			preparedStatement.executeUpdate();
			preparedStatement.close();  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		return exportModel;
	}


	public void updateExportMergedDatatableNameUsingExportModel(
			ExportModel exportModel, Connection connection) {
		
		try {
			//Update the export table
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `export` SET merged_datatable_name = ? WHERE id = ?;");
			
			preparedStatement.setString(1, exportModel.getMergedDatatableModel().getName());
			preparedStatement.setInt(2, exportModel.getId());
			
			preparedStatement.executeUpdate();
			preparedStatement.close();  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void setDatabaseModel(DatabaseModel databaseModel) {
		this.databaseModel = databaseModel;
	}


	public DatabaseModel getDatabaseModel() {
		return this.databaseModel;
	}

	
	public String retrieveExportsAsDecoratedXHTMLTableUsingUserId (Integer userId) {
		
		String exportsAsDecoratedXHTMLTable = "";
		
		//
		//logger.info("got userId: " + userId);
		
		  CachedRowSet exportsAsCachedRowSet = this.retrieveExportsAsCachedRowSetUsingUserId(userId);

		  if (exportsAsCachedRowSet != null) {

			  	ExportsFunctions exportsFunctions = new ExportsFunctions();
			  	exportsFunctions.setExportsAsCachedRowSet(exportsAsCachedRowSet);
			  	exportsFunctions.setExportsAsDecoratedXHTMLTableUsingExportsAsCachedRowSet();
			  	exportsAsDecoratedXHTMLTable = exportsFunctions.getExportsAsDecoratedXHTMLTable();
			    
		  } else {
			  
			  //TODO: Error handling
			  this.logger.warning("exportsAsCachedRowSet is null");
			  exportsAsDecoratedXHTMLTable = "<p>Error: exportsAsCachedRowSet is null</p>";
			  
		  }
		
		return exportsAsDecoratedXHTMLTable;
	}


	public CachedRowSet retrieveExportsAsCachedRowSetUsingUserId(Integer userId) {
		
		//
		//logger.info("got userId: " + userId);
		
		CachedRowSet exportsAsCachedRowSet = null;
		
		UserModel userModel = new UserModel();
		userModel.setId(userId);
		
		   String CACHED_ROW_SET_IMPL_CLASS = "com.sun.rowset.CachedRowSetImpl";
		   
		   try {	

				Connection connection = this.getDatabaseModel().getNewConnection();
				 
				if (connection != null) {
					
					 //FIXME: Apparently a bug in CachedRowSet using getX('columnAlias') aka columnLabel, which actually only works with getX('columnName'), so using getX('columnIndex').
					 
					
					//NOTE: This won't return results that don't have a record for each source file and the merged file.
					
				      try{
				          PreparedStatement preparedStatement = connection.prepareStatement(
				        		  "SELECT source_file_1.filename, source_file_2.filename, merged_file.filename, export.id, source_file_1_id, source_file_2_id, merged_file_id, export.created_datetime " +
				        		  "FROM export " +
				        		  "JOIN file AS source_file_1 ON source_file_1.id = export.source_file_1_id " +
				        		  "JOIN file AS source_file_2 ON source_file_2.id = export.source_file_2_id " +
				        		  "JOIN file AS merged_file ON merged_file.id = export.merged_file_id " +
				        		  "WHERE export.created_by_user_id = ?" +
				        		  ";");
				          preparedStatement.setInt(1, userModel.getId());
				          preparedStatement.executeQuery();
				          Class<?> cachedRowSetImplClass = Class.forName(CACHED_ROW_SET_IMPL_CLASS);
				          exportsAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
				          exportsAsCachedRowSet.populate(preparedStatement.getResultSet());
				          preparedStatement.close();
	
				        } 
				      	catch (SQLException sqlException){
				        	//System.out.println("<p>" + sqlException + "</p>");
					    	sqlException.printStackTrace();
						} finally {
				        	try {
								connection.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
				        }
				
					
					
				} else {
					
					//System.out.println("connection.isClosed");
				}
		
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
	
	
	     return exportsAsCachedRowSet;
	     
	}
	



	public void setExportsFilebaseDirectoryPathRelativeToFilebaseServerPath(
			String exportsFilebaseDirectoryPathRelativeToFilebaseServerPath) {
		this.exportsFilebaseDirectoryPathRelativeToFilebaseServerPath = exportsFilebaseDirectoryPathRelativeToFilebaseServerPath;
	}


	public String getExportsFilebaseDirectoryPathRelativeToFilebaseServerPath() {
		return exportsFilebaseDirectoryPathRelativeToFilebaseServerPath;
	}


	public void setFilebaseModel(FilebaseModel filebaseModel) {
		this.filebaseModel = filebaseModel;
	}


	public FilebaseModel getFilebaseModel() {
		return filebaseModel;
	}
	
}
