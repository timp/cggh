package org.cggh.tools.dataMerger.data.exports;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.DataModel;
import org.cggh.tools.dataMerger.data.joinedDatatables.JoinedDatatablesModel;
import org.cggh.tools.dataMerger.data.joins.JoinsModel;
import org.cggh.tools.dataMerger.data.mergedDatatables.MergedDatatablesModel;
import org.cggh.tools.dataMerger.data.merges.MergesModel;
import org.cggh.tools.dataMerger.data.resolutions.ResolutionsModel;
import org.cggh.tools.dataMerger.data.users.UserModel;
import org.cggh.tools.dataMerger.functions.exports.ExportsFunctionsModel;
import org.cggh.tools.dataMerger.functions.merges.MergesFunctionsModel;

public class ExportsModel {

	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.exports");
	private DataModel dataModel;
	private UserModel userModel;
	
	public ExportsModel() {

		this.setDataModel(new DataModel());
		this.setUserModel(new UserModel());			
	
		
	}


	public ExportModel retrieveExportAsExportModelThroughCreatingExportUsingExportModel(
			ExportModel exportModel) {

		try {
			
			Connection connection = this.getDataModel().getNewConnection();
			
			if (!connection.isClosed()) {
		
			
			      try {		
					
			    	  
						// Get the merge model for the export.
						MergesModel mergesModel = new MergesModel();
						mergesModel.setDataModel(this.getDataModel());
						exportModel.setMergeModel(mergesModel.retrieveMergeAsMergeModelByMergeId(exportModel.getMergeModel().getId()));
						
					
						//Insert the export record
						PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO export (upload_1_id, upload_2_id, created_by_user_id, created_datetime) VALUES (?, ?, ?, NOW());");
						preparedStatement.setInt(1, exportModel.getMergeModel().getUpload1Model().getId());
						preparedStatement.setInt(2, exportModel.getMergeModel().getUpload2Model().getId());
						preparedStatement.setInt(3, this.getUserModel().getId());
						preparedStatement.executeUpdate();
						preparedStatement.close();  
			    	  
						// Get the export Id (the last insert id)
						exportModel.setId(this.getDataModel().retrieveLastInsertIdAsIntegerUsingConnection(connection));
						
						
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
						
						
						// Export the join into a file.
						//TODO: Should this be part of the files or scripts app?
						this.createJoinsAsFileUsingExportModel(exportModel, connection);
						
						//TODO: Export the resolutions into a file.
						this.createResolutionsAsFileUsingExportModel(exportModel, connection);
					
					
				      //End of export algorithm
				      
		
			        }
			        catch(SQLException sqlException){
				    	sqlException.printStackTrace();
			        } 			

			        
				connection.close();
				
			} else {
				
				System.out.println("connection.isClosed");
			}
				
		} 
		catch (Exception e) {
			System.out.println("Exception from createMerge.");
			e.printStackTrace();
		}
  	
		return exportModel;
	}


	public void createMergedDatatableAsFileUsingExportModel(
			ExportModel exportModel, Connection connection) {
		
		
		File exportDirectory = new File(this.getDataModel().getServletContext().getInitParameter("exportsFileRepositoryBasePath") + exportModel.getId().toString());
		
		this.logger.info("exportDirectory created: " + exportDirectory.mkdirs());
		
		MergedDatatablesModel mergedDatatablesModel = new MergedDatatablesModel();
		
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
		
		try {
			
			String fileName = "merged_datatable_" + exportModel.getId() + ".csv";
			
			String createMergedDatatableAsFileSQL =
				"(SELECT " + mergedDatatableColumnNamesForSelectSQL + ") " + 
				"UNION " +
				"(SELECT * FROM `" + exportModel.getMergedDatatableModel().getName() + "` INTO OUTFILE '" + exportDirectory.toString().replace("\\", "\\\\") +  
				 "\\\\" + fileName + "' " +
						"FIELDS ESCAPED BY '\\\\' OPTIONALLY ENCLOSED BY '\"' TERMINATED BY ',' " +
						"LINES TERMINATED BY '\\n' " +
				")" +
				";";		
			
			this.logger.info("createMergedDatatableAsFileSQL: " + createMergedDatatableAsFileSQL);
			
			PreparedStatement preparedStatement = connection.prepareStatement(createMergedDatatableAsFileSQL);
			preparedStatement.executeQuery();
			preparedStatement.close();		

			
			//FIXME: Check whether it was truly successful
			exportModel.getMergedDatatableModel().setExportRepositoryFilepath(exportDirectory.toString() + "\\" + fileName);
			exportModel.getMergedDatatableModel().setExportSuccessful(true);
			
			this.updateExportMergedDatatableExportRepositoryFilepathUsingExportModel(exportModel, connection);
			this.updateExportMergedDatatableExportSuccessfulUsingExportModel(exportModel, connection);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateExportMergedDatatableExportRepositoryFilepathUsingExportModel(
			ExportModel exportModel, Connection connection) {
		
		try {
			//Update the export table
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `export` SET merged_datatable_export_repository_filepath = ? WHERE id = ?;");
			
			preparedStatement.setString(1, exportModel.getMergedDatatableModel().getExportRepositoryFilepath());
			preparedStatement.setInt(2, exportModel.getId());
			
			preparedStatement.executeUpdate();
			preparedStatement.close();  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}		

	public void updateExportMergedDatatableExportSuccessfulUsingExportModel(
			ExportModel exportModel, Connection connection) {
		try {
			//Update the export table
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `export` SET merged_datatable_export_successful = ? WHERE id = ?;");
			
			preparedStatement.setBoolean(1, exportModel.getMergedDatatableModel().getExportSuccessful());
			preparedStatement.setInt(2, exportModel.getId());
			
			preparedStatement.executeUpdate();
			preparedStatement.close();  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}





	public void createJoinsAsFileUsingExportModel(ExportModel exportModel,
			Connection connection) {


		File exportDirectory = new File(this.getDataModel().getServletContext().getInitParameter("exportsFileRepositoryBasePath") + exportModel.getId().toString());
		
		this.logger.info("exportDirectory created: " + exportDirectory.mkdirs());

		
		try {
			
			String fileName = "join_" + exportModel.getId() + ".csv";
			
			String createJoinsAsFileSQL =
				"(SELECT 'Export ID', 'Column Number', 'Key', 'Source 1 Column Name', 'Source 2 Column Name', 'Source 1 Constant', 'Source 2 Constant', 'Merged Column Name') " + 
				"UNION " +
				"(SELECT " + exportModel.getId() + ", `join`.column_number, IF(`join`.`key`,'Yes','No'), `join`.datatable_1_column_name, `join`.datatable_2_column_name, `join`.constant_1, `join`.constant_2, `join`.column_name " +
				"FROM `join` " +
				"JOIN `merge` ON `merge`.id = `join`.merge_id " +
				"WHERE merge_id = " + exportModel.getMergeModel().getId() + " " +
				"INTO OUTFILE '" + exportDirectory.toString().replace("\\", "\\\\") +  
				 "\\\\" + fileName + "' " +
						"FIELDS ESCAPED BY '\\\\' OPTIONALLY ENCLOSED BY '\"' TERMINATED BY ',' " +
						"LINES TERMINATED BY '\\n' " +
				")" +
				";";		
			
			this.logger.info("createJoinsAsFileSQL: " + createJoinsAsFileSQL);
			
			PreparedStatement preparedStatement = connection.prepareStatement(createJoinsAsFileSQL);
			preparedStatement.executeQuery();
			preparedStatement.close();		

			
			//FIXME: Check whether it was truly successful
			exportModel.getMergeModel().getJoinsModel().setExportRepositoryFilepath(exportDirectory.toString() + "\\" + fileName);
			exportModel.getMergeModel().getJoinsModel().setExportSuccessful(true);
			
			this.updateExportJoinsExportRepositoryFilepathUsingExportModel(exportModel, connection);
			this.updateExportJoinsExportSuccessfulUsingExportModel(exportModel, connection);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void updateExportJoinsExportRepositoryFilepathUsingExportModel(
			ExportModel exportModel, Connection connection) {
		
		try {
			//Update the export table
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `export` SET joins_export_repository_filepath = ? WHERE id = ?;");
			
			preparedStatement.setString(1, exportModel.getMergeModel().getJoinsModel().getExportRepositoryFilepath());
			preparedStatement.setInt(2, exportModel.getId());
			
			preparedStatement.executeUpdate();
			preparedStatement.close();  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

		
	
	
	public void updateExportJoinsExportSuccessfulUsingExportModel(
			ExportModel exportModel, Connection connection) {
		try {
			//Update the export table
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `export` SET joins_export_successful = ? WHERE id = ?;");
			
			preparedStatement.setBoolean(1, exportModel.getMergeModel().getJoinsModel().getExportSuccessful());
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

		File exportDirectory = new File(this.getDataModel().getServletContext().getInitParameter("exportsFileRepositoryBasePath") + exportModel.getId().toString());
		
		this.logger.info("exportDirectory created: " + exportDirectory.mkdirs());

		
		try {
			
			String fileName = "resolutions_" + exportModel.getId() + ".csv";
			
			String keyColumnNamesForSelectSQL = "";
			String joinedKeytableKeyColumnNameAliasesForSelectSQL = "";
			
			CachedRowSet keyJoinsAsCachedRowSet = exportModel.getMergeModel().getJoinsModel().retrieveKeyJoinsAsCachedRowSetByMergeId(exportModel.getMergeModel().getId(), connection);
			
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
				"INTO OUTFILE '" + exportDirectory.toString().replace("\\", "\\\\") +  
				 "\\\\" + fileName + "' " +
						"FIELDS ESCAPED BY '\\\\' OPTIONALLY ENCLOSED BY '\"' TERMINATED BY ',' " +
						"LINES TERMINATED BY '\\n' " +
				")" +
				";";		
			
			this.logger.info("createResolutionsAsFileSQL: " + createResolutionsAsFileSQL);
			
			PreparedStatement preparedStatement = connection.prepareStatement(createResolutionsAsFileSQL);
			preparedStatement.executeQuery();
			preparedStatement.close();		

			
			//FIXME: Check whether it was truly successful
			exportModel.getMergeModel().getResolutionsModel().setExportRepositoryFilepath(exportDirectory.toString() + "\\" + fileName);
			exportModel.getMergeModel().getResolutionsModel().setExportSuccessful(true);
			
			this.updateExportResolutionsExportRepositoryFilepathUsingExportModel(exportModel, connection);
			this.updateExportResolutionsExportSuccessfulUsingExportModel(exportModel, connection);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	public void updateExportResolutionsExportSuccessfulUsingExportModel(
			ExportModel exportModel, Connection connection) {
		
		try {
			//Update the export table
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `export` SET resolutions_export_successful = ? WHERE id = ?;");
			
			preparedStatement.setBoolean(1, exportModel.getMergeModel().getResolutionsModel().getExportSuccessful());
			preparedStatement.setInt(2, exportModel.getId());
			
			preparedStatement.executeUpdate();
			preparedStatement.close();  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void updateExportResolutionsExportRepositoryFilepathUsingExportModel(
			ExportModel exportModel, Connection connection) {
		
		try {
			//Update the export table
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `export` SET resolutions_export_repository_filepath = ? WHERE id = ?;");
			
			preparedStatement.setString(1, exportModel.getMergeModel().getResolutionsModel().getExportRepositoryFilepath());
			preparedStatement.setInt(2, exportModel.getId());
			
			preparedStatement.executeUpdate();
			preparedStatement.close();  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}


	public ExportModel retrieveExportAsExportModelThroughUpdatingMergedDatatableWithResolvedDataUsingExportModel(
			ExportModel exportModel, Connection connection) {


		ResolutionsModel resolutionsModel = new ResolutionsModel();
		
		CachedRowSet resolutionsAsCachedRowSet = resolutionsModel.retrieveResolutionsAsCachedRowSetUsingMergeId(exportModel.getMergeModel().getId(), connection);
		
		try {
			resolutionsAsCachedRowSet.beforeFirst();

	        
	        if (resolutionsAsCachedRowSet.next()) {
	
	        	JoinsModel joinsModel = new JoinsModel();
	        	
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
		        		
		        		this.logger.info("updateMergedDatatableSQL: " + updateMergedDatatableSQL);
		        		
						PreparedStatement preparedStatement = connection.prepareStatement(updateMergedDatatableSQL);
						preparedStatement.executeUpdate();
						preparedStatement.close();
		        	}
		        	
		        }
		        
	        } else {
	        	//There may be no resolutions
	        	this.logger.info("Did not retrieve any resolutions as a cached row set using merge Id: " + exportModel.getMergeModel().getId());
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
			
			this.logger.info("SelectRowKeysForRemovalSQL: " + SelectRowKeysForRemovalSQL);
			
			PreparedStatement preparedStatement = connection.prepareStatement(SelectRowKeysForRemovalSQL);
			preparedStatement.executeQuery();
			
			// returns: id, key_column_2, key_column_21
			
	          ResultSet resultSet = preparedStatement.getResultSet();

	          
	          if (resultSet.next()) {

	      		//FIXME: detach joins CRUD from joins Set
	      		JoinsModel joinsModel = new JoinsModel();
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
					
					this.logger.info("deleteRowFromMergedDatatableSQL: " + deleteRowFromMergedDatatableSQL);
					
					PreparedStatement preparedStatement2 = connection.prepareStatement(deleteRowFromMergedDatatableSQL);
					preparedStatement2.executeUpdate();
					preparedStatement2.close();
	        		  
	        	  }

	          } else {
	        	// There may be no rows for exclusion.
	        	  this.logger.info("Did not retrieve any rows for removal.");
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
						datatable1NonCrossDatatableExportColumnMappingsForUpdateSetSQL += "`" + exportModel.getMergedDatatableModel().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable1Model().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("constant_1") + "` ";
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
						datatable2NonCrossDatatableExportColumnMappingsForUpdateSetSQL += "`" + exportModel.getMergedDatatableModel().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable2Model().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("constant_2") + "` ";
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
				
				this.logger.info("updateMergedDatatableWithDatatable1Data: " + updateMergedDatatableWithDatatable1Data);
				
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
				
				this.logger.info("updateMergedDatatableWithDatatable2Data: " + updateMergedDatatableWithDatatable2Data);
				
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
				
				//this.logger.info("Got key joins data as cached row set.");
				
				
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
		JoinsModel joinsModel = new JoinsModel();
		joinsModel.setDataModel(this.getDataModel());
	
		HashMap<Integer, String> joinColumnNamesByColumnNumberAsHashMap = joinsModel.retrieveJoinColumnNamesByColumnNumberAsHashMapUsingMergeModel(exportModel.getMergeModel());
		
		//TODO: Make a (pseudo-temporary) table based on these columns.
		
		//TODO:
		
		String columnDefinitionsForCreateSQL = "";
		
		for (Integer i = 1; i <= joinColumnNamesByColumnNumberAsHashMap.size(); i++) {
			
			//this.logger.info("Column Number: " + i + ", Column Name: " + joinColumnNamesByColumnNumberAsHashMap.get(i));
			
			if (i != 1) {
				columnDefinitionsForCreateSQL += ", ";
			}
			columnDefinitionsForCreateSQL += "`" + joinColumnNamesByColumnNumberAsHashMap.get(i) +  "` VARCHAR(255) NULL";
		}
		
		//this.logger.info("columnDefinitionsForCreateSQL: " + columnDefinitionsForCreateSQL);
		
		exportModel.getMergedDatatableModel().setName("merged_datatable_" + exportModel.getId());
		
		ExportsModel exportsModel = new ExportsModel();
		exportsModel.updateExportMergedDatatableNameUsingExportModel(exportModel, connection);
		
		
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


	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
	}


	public DataModel getDataModel() {
		return this.dataModel;
	}


	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}


	public UserModel getUserModel() {
		return this.userModel;
	}

	
	public String retrieveExportsAsDecoratedXHTMLTableUsingExportsModel (ExportsModel exportsModel) {
		
		String exportsAsDecoratedXHTMLTable = "";
		
		
		  CachedRowSet exportsAsCachedRowSet = this.retrieveExportsAsCachedRowSetUsingUserId(exportsModel.getUserModel().getId());

		  if (exportsAsCachedRowSet != null) {

			  	ExportsFunctionsModel exportsFunctionsModel = new ExportsFunctionsModel();
			  	exportsFunctionsModel.setExportsAsCachedRowSet(exportsAsCachedRowSet);
			  	exportsFunctionsModel.setExportsAsDecoratedXHTMLTableUsingExportsAsCachedRowSet();
			  	exportsAsDecoratedXHTMLTable = exportsFunctionsModel.getExportsAsDecoratedXHTMLTable();
			    
		  } else {
			  
			  //TODO: Error handling
			  this.logger.warning("Error: exportsAsCachedRowSet is null");
			  exportsAsDecoratedXHTMLTable = "<p>Error: exportsAsCachedRowSet is null</p>";
			  
		  }
		
		return exportsAsDecoratedXHTMLTable;
	}


	public CachedRowSet retrieveExportsAsCachedRowSetUsingUserId(Integer userId) {
		
		CachedRowSet exportsAsCachedRowSet = null;
		
		UserModel userModel = new UserModel();
		userModel.setId(userId);
		
		   String CACHED_ROW_SET_IMPL_CLASS = "com.sun.rowset.CachedRowSetImpl";
		   
			try {
				
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				
				Connection connection = this.getDataModel().getNewConnection();
				 
				if (!connection.isClosed()) {
				
					 //FIXME: Apparently a bug in CachedRowSet using getX('columnAlias') aka columnLabel, which actually only works with getX('columnName'), so using getX('columnIndex').
					 
					
				      try{
				          PreparedStatement preparedStatement = connection.prepareStatement(
				        		  "SELECT export.id, upload_1.id, upload_1.original_filename, upload_2.id, upload_2.original_filename, export.created_datetime " +
				        		  "FROM export " +
				        		  "JOIN upload AS upload_1 ON upload_1.id = export.upload_1_id " +
				        		  "JOIN upload AS upload_2 ON upload_2.id = export.upload_2_id " +
				        		  "WHERE export.created_by_user_id = ?" +
				        		  ";");
				          preparedStatement.setInt(1, userModel.getId());
				          preparedStatement.executeQuery();
				          Class<?> cachedRowSetImplClass = Class.forName(CACHED_ROW_SET_IMPL_CLASS);
				          exportsAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
				          exportsAsCachedRowSet.populate(preparedStatement.getResultSet());
				          preparedStatement.close();
	
				        }
				        catch(SQLException sqlException){
				        	System.out.println("<p>" + sqlException + "</p>");
					    	sqlException.printStackTrace();
				        } 	
				
					connection.close();
					
				} else {
					
					System.out.println("connection.isClosed");
				}
					
			} 
			catch (Exception e) {
				this.logger.severe(e.getMessage().toString());
				e.printStackTrace();
			}
	
	
	
	     return exportsAsCachedRowSet;
	     
	}
	
	
}
