package org.cggh.tools.dataMerger.data.exports;

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
import org.cggh.tools.dataMerger.data.merges.MergesModel;
import org.cggh.tools.dataMerger.data.resolutions.ResolutionsModel;
import org.cggh.tools.dataMerger.data.users.UserModel;

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
					
					// TODO
					//////////////////
					
			    	  
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
						exportModel = this.retrieveExportAsExportModelThroughRecreatingExportDatatableUsingExportModel(exportModel, connection);
						
						
						// Get all the unique keys and add to export table.
						exportModel = this.retrieveExportAsExportModelThroughInsertingKeysIntoExportDatatableUsingExportModel(exportModel, connection);
						
						
						// Fill the export datatable with data from source 1 
						// Fill the export datatable with data from source 2
						exportModel = this.retrieveExportAsExportModelThroughUpdatingExportDatatableWithDatatableDataUsingExportModel(exportModel, connection);
					
						
						// Amend the export datatable with resolutions
						exportModel = this.retrieveExportAsExportModelThroughUpdatingExportDatatableWithResolvedDataUsingExportModel(exportModel, connection);
						
						// Remove all rows from the export datatable that have been marked for removal by the resolutions
						exportModel = this.retrieveExportAsExportModelThroughRemovingMarkedRowsFromExportDatatableUsingExportModel(exportModel, connection);
						
						
						/////////////////////////////////////
						//TODO: Export the export datatable into a file.
						//exportModel = this.retrieveExportAsFileUsingExportModel(exportModel, connection);
						
						//TODO: Export the join into a file. Alter table to export_repository_filepath, join_respository_filepath, resolutions_repository_filepath
						
						//TODO: Export the resolutions into a file.
						//////////////////////////////////////
					
					
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


	public ExportModel retrieveExportAsExportModelThroughUpdatingExportDatatableWithResolvedDataUsingExportModel(
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
	        	
	        	String exportedDatatableToJoinedKeytableMappingsForWhereClauseSQL = "";
	        	String exportedDatatableToDatatable1MappingsForWhereClauseSQL = "";
	        	String exportedDatatableToDatatable2MappingsForWhereClauseSQL = "";
	        	
	        	if (keyJoinsAsCachedRowSet.next()) {
	        		
	        		keyJoinsAsCachedRowSet.beforeFirst();
	        		
	        		while (keyJoinsAsCachedRowSet.next()) {
	        			
	        			exportedDatatableToJoinedKeytableMappingsForWhereClauseSQL += "AND `" + exportModel.getExportedDatatableModel().getName() + "`.`" + keyJoinsAsCachedRowSet.getString("column_name") + "` = `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "`.`key_column_" + keyJoinsAsCachedRowSet.getInt("column_number") + "` ";
	        	
	        			exportedDatatableToDatatable1MappingsForWhereClauseSQL = "AND `" + exportModel.getExportedDatatableModel().getName() + "`.`" + keyJoinsAsCachedRowSet.getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable1Model().getName()+ "`.`" + keyJoinsAsCachedRowSet.getString("datatable_1_column_name") + "` ";
	        			
	        			exportedDatatableToDatatable2MappingsForWhereClauseSQL = "AND `" + exportModel.getExportedDatatableModel().getName() + "`.`" + keyJoinsAsCachedRowSet.getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable2Model().getName()+ "`.`" + keyJoinsAsCachedRowSet.getString("datatable_2_column_name") + "` ";
	        			
	        		}
	        		
	        	}
	        	
	        	resolutionsAsCachedRowSet.beforeFirst();
	        	
		        while (resolutionsAsCachedRowSet.next()) {
		        
		        	//TODO: code
		        	
		        	Integer solutionByColumnId = resolutionsAsCachedRowSet.getInt("solution_by_column_id");
		        	Integer solutionByRowId = resolutionsAsCachedRowSet.getInt("solution_by_row_id");
		        	Integer solutionByCellId = resolutionsAsCachedRowSet.getInt("solution_by_cell_id");
		        	
		        	//
		        	String updateExportedDatatableSQL = "";

		        		
		        		if ((solutionByColumnId != null && solutionByColumnId == 1) || (solutionByRowId != null && solutionByRowId == 1) || (solutionByCellId != null && solutionByCellId == 1)) {
		        			
		        			// Prefer source 1
		        			updateExportedDatatableSQL = 
		        				"UPDATE `" + exportModel.getExportedDatatableModel().getName() + "`, `" + exportModel.getMergeModel().getDatatable1Model().getName() + "`, `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "` " +
		        					"SET `" + exportModel.getExportedDatatableModel().getName() + "`.`" + joinColumnNamesByColumnNumberAsHashMap.get(resolutionsAsCachedRowSet.getInt("column_number")) +"` = `" + exportModel.getMergeModel().getDatatable1Model().getName() + "`.`" + datatable1ColumnNamesByColumnNumberAsHashMap.get(resolutionsAsCachedRowSet.getInt("column_number")) + "` " +
		        					"WHERE `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "`.id = '" + resolutionsAsCachedRowSet.getInt("joined_keytable_id") + "' " + exportedDatatableToJoinedKeytableMappingsForWhereClauseSQL + exportedDatatableToDatatable1MappingsForWhereClauseSQL + 
		        				";";
		        			
		        			
		        		}
		        		else if ((solutionByColumnId != null && solutionByColumnId == 2) || (solutionByRowId != null && solutionByRowId == 2) || (solutionByCellId != null && solutionByCellId == 2)) {
		        			
		        			// Prefer source 2
		        			updateExportedDatatableSQL = 
		        				"UPDATE `" + exportModel.getExportedDatatableModel().getName() + "`, `" + exportModel.getMergeModel().getDatatable2Model().getName() + "`, `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "` " +
		        					"SET `" + exportModel.getExportedDatatableModel().getName() + "`.`" + joinColumnNamesByColumnNumberAsHashMap.get(resolutionsAsCachedRowSet.getInt("column_number")) +"` = `" + exportModel.getMergeModel().getDatatable2Model().getName() + "`.`" + datatable2ColumnNamesByColumnNumberAsHashMap.get(resolutionsAsCachedRowSet.getInt("column_number")) + "` " +
		        					"WHERE `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "`.id = '" + resolutionsAsCachedRowSet.getInt("joined_keytable_id") + "' " + exportedDatatableToJoinedKeytableMappingsForWhereClauseSQL + exportedDatatableToDatatable2MappingsForWhereClauseSQL +
		        				";";
		        			
		        		}
		        		else if ((solutionByColumnId != null && solutionByColumnId == 3) || (solutionByRowId != null && solutionByRowId == 3) || (solutionByCellId != null && solutionByCellId == 3)) {
		        			
		        			// Use NULL
		        			updateExportedDatatableSQL = 
		        				"UPDATE `" + exportModel.getExportedDatatableModel().getName() + "`, `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "` " +
		        					"SET `" + exportModel.getExportedDatatableModel().getName() + "`.`" + joinColumnNamesByColumnNumberAsHashMap.get(resolutionsAsCachedRowSet.getInt("column_number")) +"` = NULL " +
		        					"WHERE `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "`.id = '" + resolutionsAsCachedRowSet.getInt("joined_keytable_id") + "' " + exportedDatatableToJoinedKeytableMappingsForWhereClauseSQL +
		        				";";
		        			
		        		}
		        		else if ((solutionByColumnId != null && solutionByColumnId == 4) || (solutionByRowId != null && solutionByRowId == 4) || (solutionByCellId != null && solutionByCellId == 4)) {
		        			
		        			// Use Constant
		        			updateExportedDatatableSQL = 
		        				"UPDATE `" + exportModel.getExportedDatatableModel().getName() + "`, `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "` " +
		        					"SET `" + exportModel.getExportedDatatableModel().getName() + "`.`" + joinColumnNamesByColumnNumberAsHashMap.get(resolutionsAsCachedRowSet.getInt("column_number")) +"` = '" + resolutionsAsCachedRowSet.getString("constant") + "' " +
		        					"WHERE `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "`.id = '" + resolutionsAsCachedRowSet.getInt("joined_keytable_id") + "' " + exportedDatatableToJoinedKeytableMappingsForWhereClauseSQL +
		        				";";
		        		}
		        		//solution 5 (remove row) is dealt with separately, afterwards.
	
		        	if (updateExportedDatatableSQL != "") {
		        		
		        		this.logger.info("updateExportedDatatableSQL: " + updateExportedDatatableSQL);
		        		
						PreparedStatement preparedStatement = connection.prepareStatement(updateExportedDatatableSQL);
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


	public ExportModel retrieveExportAsExportModelThroughRemovingMarkedRowsFromExportDatatableUsingExportModel(
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
	  				String deleteRowFromExportDatatableSQL = 
						"DELETE FROM `" + exportModel.getExportedDatatableModel().getName() + "` WHERE " + deleteRowWhereConditionsSQL + 
						";";
					
					this.logger.info("deleteRowFromExportDatatableSQL: " + deleteRowFromExportDatatableSQL);
					
					PreparedStatement preparedStatement2 = connection.prepareStatement(deleteRowFromExportDatatableSQL);
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



	public ExportModel retrieveExportAsExportModelThroughUpdatingExportDatatableWithDatatableDataUsingExportModel(
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
						datatable1NonCrossDatatableExportColumnMappingsForUpdateSetSQL += "`" + exportModel.getExportedDatatableModel().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable1Model().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("datatable_1_column_name") + "` ";
					}
					else if (exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("datatable_1_column_name") == null && exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("constant_1") != null) {
						if (datatable1NonCrossDatatableExportColumnMappingsForUpdateSetSQL != "") {
							datatable1NonCrossDatatableExportColumnMappingsForUpdateSetSQL += ", ";
						}
						datatable1NonCrossDatatableExportColumnMappingsForUpdateSetSQL += "`" + exportModel.getExportedDatatableModel().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable1Model().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("constant_1") + "` ";
					}
					// Value will stay null by default
					
					if (exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("datatable_2_column_name") != null) {
						if (datatable2NonCrossDatatableExportColumnMappingsForUpdateSetSQL != "") {
							datatable2NonCrossDatatableExportColumnMappingsForUpdateSetSQL += ", ";
						}
						datatable2NonCrossDatatableExportColumnMappingsForUpdateSetSQL += "`" + exportModel.getExportedDatatableModel().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable2Model().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("datatable_2_column_name") + "` ";
					}
					else if (exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("datatable_2_column_name") == null && exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("constant_2") != null) {
						if (datatable2NonCrossDatatableExportColumnMappingsForUpdateSetSQL != "") {
							datatable2NonCrossDatatableExportColumnMappingsForUpdateSetSQL += ", ";
						}
						datatable2NonCrossDatatableExportColumnMappingsForUpdateSetSQL += "`" + exportModel.getExportedDatatableModel().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable2Model().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getJoinsAsCachedRowSet().getString("constant_2") + "` ";
					}
				
				}
				
			}
			
	
			String datatable1KeyExportColumnMappingsForUpdateWhereSQL = "";
			String datatable2KeyExportColumnMappingsForUpdateWhereSQL = "";		
			
			exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().first();
			
			datatable1KeyExportColumnMappingsForUpdateWhereSQL += "`" + exportModel.getExportedDatatableModel().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable1Model().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("datatable_1_column_name") + "` ";
			datatable2KeyExportColumnMappingsForUpdateWhereSQL += "`" + exportModel.getExportedDatatableModel().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable2Model().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("datatable_2_column_name") + "` ";
			
			while (exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().next()) {

				
				datatable1KeyExportColumnMappingsForUpdateWhereSQL += "AND `" + exportModel.getExportedDatatableModel().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable1Model().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("datatable_1_column_name") + "` ";
				datatable2KeyExportColumnMappingsForUpdateWhereSQL += "AND `" + exportModel.getExportedDatatableModel().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable2Model().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("datatable_2_column_name") + "` ";
								
				
			}		
			
			if (datatable1NonCrossDatatableExportColumnMappingsForUpdateSetSQL != "") {
				String updateExportDatatableWithDatatable1Data = 
					"UPDATE `" + exportModel.getExportedDatatableModel().getName() + "`, `" + exportModel.getMergeModel().getDatatable1Model().getName() + "` " + 
					"SET " + datatable1NonCrossDatatableExportColumnMappingsForUpdateSetSQL +
					"WHERE " + datatable1KeyExportColumnMappingsForUpdateWhereSQL + 
					";";
				
				this.logger.info("updateExportDatatableWithDatatable1Data: " + updateExportDatatableWithDatatable1Data);
				
				PreparedStatement preparedStatement = connection.prepareStatement(updateExportDatatableWithDatatable1Data);
				preparedStatement.executeUpdate();
				preparedStatement.close();
			}
			
			if (datatable2NonCrossDatatableExportColumnMappingsForUpdateSetSQL != "") {
			
				String updateExportDatatableWithDatatable2Data = 
					"UPDATE `" + exportModel.getExportedDatatableModel().getName() + "`, `" + exportModel.getMergeModel().getDatatable2Model().getName() + "` " + 
					"SET " + datatable2NonCrossDatatableExportColumnMappingsForUpdateSetSQL +
					"WHERE " + datatable2KeyExportColumnMappingsForUpdateWhereSQL + 
					";";
				
				this.logger.info("updateExportDatatableWithDatatable2Data: " + updateExportDatatableWithDatatable2Data);
				
				PreparedStatement preparedStatement2 = connection.prepareStatement(updateExportDatatableWithDatatable2Data);
				preparedStatement2.executeUpdate();
				preparedStatement2.close(); 
			
			}
			
			
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		
		return exportModel;
	}


	public ExportModel retrieveExportAsExportModelThroughInsertingKeysIntoExportDatatableUsingExportModel(
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
							"INSERT INTO `" + exportModel.getExportedDatatableModel().getName() + "` (" + keyColumnNamesForInsertSQL + ") " +
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


	private ExportModel retrieveExportAsExportModelThroughRecreatingExportDatatableUsingExportModel(
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
		
		exportModel.getExportedDatatableModel().setName("exported_datatable_" + exportModel.getId());
		
		ExportsModel exportsModel = new ExportsModel();
		exportsModel.updateExportExportedDatatableNameUsingExportModel(exportModel, connection);
		
		
		try {
			//Drop the export_datatable
			PreparedStatement preparedStatement = connection.prepareStatement("DROP TABLE IF EXISTS `" + exportModel.getExportedDatatableModel().getName() + "`;");
			preparedStatement.executeUpdate();
			preparedStatement.close();  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			//Create the export_datatable
			PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE `" + exportModel.getExportedDatatableModel().getName() + "` (" + columnDefinitionsForCreateSQL + ") ENGINE=InnoDB;");
			preparedStatement.executeUpdate();
			preparedStatement.close();  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		return exportModel;
	}


	public void updateExportExportedDatatableNameUsingExportModel(
			ExportModel exportModel, Connection connection) {
		
		try {
			//Update the export table
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `export` SET exported_datatable_name = ? WHERE id = ?;");
			
			preparedStatement.setString(1, exportModel.getExportedDatatableModel().getName());
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

}
