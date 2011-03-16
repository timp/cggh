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
import org.cggh.tools.dataMerger.data.joins.JoinsModel;
import org.cggh.tools.dataMerger.data.merges.MergesModel;
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
						exportModel = this.retrieveExportAsExportModelThroughPopulatingExportDatatableWithKeysUsingExportModel(exportModel, connection);
						
						// Add all the non-cross-datatable join data to the export datatable, according to key
						exportModel = this.retrieveExportAsExportModelThroughPopulatingExportDatatableWithNonCrossDatatableValuesUsingExportModel(exportModel, connection);
						
						
						// Remove all rows from the export datatable that have been marked for removal by the resolutions
						exportModel = this.retrieveExportAsExportModelThroughRemovingMarkedRowsFromExportDatatableUsingExportModel(exportModel, connection);
						
						
						//TODO:
						// Create a tmp cross-datatable join with real keys (+ both sources in cols)
						// Loop through
						// Look up resolution
						// Update export table
						////////////
						//
						////////////
						
						exportModel = this.retrieveExportAsExportModelThroughPopulatingExportDatatableWithResolvedCrossDatatableValuesUsingExportModel(exportModel, connection);
					
					
					
					
					
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
						"DELETE FROM `" + exportModel.getDatatableName() + "` WHERE " + deleteRowWhereConditionsSQL + 
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


	public ExportModel retrieveExportAsExportModelThroughPopulatingExportDatatableWithResolvedCrossDatatableValuesUsingExportModel(
			ExportModel exportModel, Connection connection) {
		
		try {		
			
			//TODO: This is mostly duplicated from MergeScriptsModel
			
			//FIXME: Move to ExportScriptsModel
			
			JoinsModel joinsModel = new JoinsModel();
			
	        HashMap<Integer, String> datatable1ColumnNamesByColumnNumberAsHashMap = joinsModel.retrieveDatatable1ColumnNamesByColumnNumberAsHashMapUsingMergeId(exportModel.getMergeModel().getId(), connection); 
	        HashMap<Integer, String> datatable2ColumnNamesByColumnNumberAsHashMap = joinsModel.retrieveDatatable2ColumnNamesByColumnNumberAsHashMapUsingMergeId(exportModel.getMergeModel().getId(), connection);
	        
	        String datatable1JoinSQL = "";
	        String datatable2JoinSQL = "";

	        String columnDefinitionsUsingKeyColumnNamesSQL = "";
	        String joinedKeytableColumnAliasesAsCSVForSelectFromJoinSQL = "";
	        
	        CachedRowSet keyJoinsAsCachedRowSet = joinsModel.retrieveKeyJoinsAsCachedRowsetByMergeId(exportModel.getMergeModel().getId(), connection);
	        
	        keyJoinsAsCachedRowSet.beforeFirst();
	        
	        if (keyJoinsAsCachedRowSet.next()) {
	        	
	        	columnDefinitionsUsingKeyColumnNamesSQL += "`" + keyJoinsAsCachedRowSet.getString("column_name") + "` VARCHAR(255) ";
	        	
	        	datatable1JoinSQL += "`" + exportModel.getMergeModel().getDatatable1Model().getName() + "`.`" + datatable1ColumnNamesByColumnNumberAsHashMap.get(keyJoinsAsCachedRowSet.getInt("column_number")) + "` = `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "`.`key_column_" + keyJoinsAsCachedRowSet.getInt("column_number") + "` ";
	        	datatable2JoinSQL += "`" + exportModel.getMergeModel().getDatatable2Model().getName() + "`.`" + datatable2ColumnNamesByColumnNumberAsHashMap.get(keyJoinsAsCachedRowSet.getInt("column_number")) + "` = `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "`.`key_column_" + keyJoinsAsCachedRowSet.getInt("column_number") + "` AND `" + exportModel.getMergeModel().getDatatable2Model().getName() + "`.`" + datatable2ColumnNamesByColumnNumberAsHashMap.get(keyJoinsAsCachedRowSet.getInt("column_number")) + "` = `" + exportModel.getMergeModel().getDatatable1Model().getName() + "`.`" + datatable1ColumnNamesByColumnNumberAsHashMap.get(keyJoinsAsCachedRowSet.getInt("column_number")) + "` ";
	        	
	        	joinedKeytableColumnAliasesAsCSVForSelectFromJoinSQL += "`"  + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "`.`key_column_" + keyJoinsAsCachedRowSet.getInt("column_number") + "` AS `" + keyJoinsAsCachedRowSet.getString("column_name") + "` ";
	        	
		        while (keyJoinsAsCachedRowSet.next()) {
		        
		        	columnDefinitionsUsingKeyColumnNamesSQL += ", `" + keyJoinsAsCachedRowSet.getString("column_name") + "` VARCHAR(255) ";
		        	
		        	datatable1JoinSQL += "AND `" + exportModel.getMergeModel().getDatatable1Model().getName() + "`.`" + datatable1ColumnNamesByColumnNumberAsHashMap.get(keyJoinsAsCachedRowSet.getInt("column_number")) + "` = `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "`.`key_column_" + keyJoinsAsCachedRowSet.getInt("column_number") + "` ";
		        	datatable2JoinSQL += "AND `" + exportModel.getMergeModel().getDatatable2Model().getName() + "`.`" + datatable2ColumnNamesByColumnNumberAsHashMap.get(keyJoinsAsCachedRowSet.getInt("column_number")) + "` = `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "`.`key_column_" + keyJoinsAsCachedRowSet.getInt("column_number") + "` AND `" + exportModel.getMergeModel().getDatatable2Model().getName() + "`.`" + datatable2ColumnNamesByColumnNumberAsHashMap.get(keyJoinsAsCachedRowSet.getInt("column_number")) + "` = `" + exportModel.getMergeModel().getDatatable1Model().getName() + "`.`" + datatable1ColumnNamesByColumnNumberAsHashMap.get(keyJoinsAsCachedRowSet.getInt("column_number")) + "` ";
		        	
		        	joinedKeytableColumnAliasesAsCSVForSelectFromJoinSQL += ", `"  + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "`.`key_column_" + keyJoinsAsCachedRowSet.getInt("column_number") + "` AS `" + keyJoinsAsCachedRowSet.getString("column_name") + "` ";
		        }
		        
	        } else {
	        	this.logger.severe("Did not retrieve any key joins as a cached row set using merge Id: " + exportModel.getMergeModel().getId());
	        }
	        
	        

	        String nonKeyCrossDatatableColumnAliasesAsCSVForSelectFromJoinSQL = "";	        
	        String columnDefinitionsUsingNonKeyCrossDatatableColumnAndSourceNumbersSQL = "";

	        CachedRowSet nonKeyCrossDatatableJoinsAsCachedRowSet = joinsModel.retrieveNonKeyCrossDatatableJoinsAsCachedRowsetByMergeId(exportModel.getMergeModel().getId(), connection);

	        nonKeyCrossDatatableJoinsAsCachedRowSet.beforeFirst();
	        
	        if (nonKeyCrossDatatableJoinsAsCachedRowSet.next()) {
	        	
	        	columnDefinitionsUsingNonKeyCrossDatatableColumnAndSourceNumbersSQL = columnDefinitionsUsingNonKeyCrossDatatableColumnAndSourceNumbersSQL.concat("`column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("column_number") + "_source_1` VARCHAR(255), `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("column_number") + "_source_2` VARCHAR(255) ");
	        	
	        	nonKeyCrossDatatableColumnAliasesAsCSVForSelectFromJoinSQL = nonKeyCrossDatatableColumnAliasesAsCSVForSelectFromJoinSQL.concat("`" + exportModel.getMergeModel().getDatatable1Model().getName() + "`.`" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("datatable_1_column_name") + "` AS `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_1`, `" + exportModel.getMergeModel().getDatatable2Model().getName() + "`.`" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("datatable_2_column_name") + "` AS `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_2` ");

		        while (nonKeyCrossDatatableJoinsAsCachedRowSet.next()) {
		        
		        	columnDefinitionsUsingNonKeyCrossDatatableColumnAndSourceNumbersSQL += ", `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("column_number") + "_source_1` VARCHAR(255), `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("column_number") + "_source_2` VARCHAR(255) ";

		        	nonKeyCrossDatatableColumnAliasesAsCSVForSelectFromJoinSQL += ", `" + exportModel.getMergeModel().getDatatable1Model().getName() + "`.`" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("datatable_1_column_name") + "` AS `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_1`, `" + exportModel.getMergeModel().getDatatable2Model().getName() + "`.`" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("datatable_2_column_name") + "` AS `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_2` ";

		        }
		        
	        } else {
	        	this.logger.severe("Did not retrieve any non-key cross-datatable joins as a cached row set using merge Id: " + exportModel.getMergeModel().getId());
	        }	
			
			
			//TODO: Want to use a temporary table, but that will cause a SQLException "Can't reopen table" when insertConflictsByCellSQL.
			
			String dropTemporaryJoinedDatatableSQL = "DROP TABLE IF EXISTS `tmp_joined_datatable_" + exportModel.getMergeModel().getId() + "`;";

			this.logger.info(dropTemporaryJoinedDatatableSQL);
			
			PreparedStatement preparedStatement2 = connection.prepareStatement(dropTemporaryJoinedDatatableSQL);
			preparedStatement2.executeUpdate();
			preparedStatement2.close();				
		
		
			String createAndPopulateTemporaryJoinedDatatableSQL = "CREATE TABLE `tmp_joined_datatable_" + exportModel.getMergeModel().getId() + "` (joined_keytable_id BIGINT(255), " + columnDefinitionsUsingKeyColumnNamesSQL + ", " + columnDefinitionsUsingNonKeyCrossDatatableColumnAndSourceNumbersSQL + ", PRIMARY KEY (joined_keytable_id)) ENGINE=InnoDB " +
												        			"SELECT `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "`.id AS joined_keytable_id, " + joinedKeytableColumnAliasesAsCSVForSelectFromJoinSQL + ", " + nonKeyCrossDatatableColumnAliasesAsCSVForSelectFromJoinSQL + 
												        			"FROM `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "` " + 
												        			"JOIN `" + exportModel.getMergeModel().getDatatable1Model().getName() + "` ON " + datatable1JoinSQL + 
												        			"JOIN `" + exportModel.getMergeModel().getDatatable2Model().getName() + "` ON " + datatable2JoinSQL + 
												        			"ORDER BY `" + exportModel.getMergeModel().getJoinedKeytableModel().getName() + "`.id" +
												        			";";

			this.logger.info(createAndPopulateTemporaryJoinedDatatableSQL);
		
	        PreparedStatement preparedStatement3 = connection.prepareStatement(createAndPopulateTemporaryJoinedDatatableSQL);
	        preparedStatement3.executeUpdate();
	        preparedStatement3.close();
	        
	        
	        /////////////////////////////////////////
	        //TODO: Cycle through the results (just select rather than making a table) and detect conflicts.
	        // Upon detection, find solution and update the export datatable. 
	        // Upon non-conflict detection, update the export datatable.
	        /////////////////////////////////////////
	        
	        
	        
	        
	        
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
							
		
		return exportModel;
	}


	public ExportModel retrieveExportAsExportModelThroughPopulatingExportDatatableWithNonCrossDatatableValuesUsingExportModel(
			ExportModel exportModel, Connection connection) {
		
		try {		
		
			//Populate the export datatable with the non-cross-datatable data from source 1
			
			String datatable1NonCrossDatatableExportColumnMappingsForUpdateSetSQL = "";
			String datatable2NonCrossDatatableExportColumnMappingsForUpdateSetSQL = "";
			
			//FIXME: detach joins CRUD from joins Set
			JoinsModel joinsModel = new JoinsModel();
			exportModel.getMergeModel().getJoinsModel().setNonCrossDatatableJoinsAsCachedRowSet(joinsModel.retrieveNonCrossDatatableJoinsAsCachedRowsetByMergeId(exportModel.getMergeModel().getId(), connection));
			
			//TODO: check with if (.next()) ...  
			
			exportModel.getMergeModel().getJoinsModel().getNonCrossDatatableJoinsAsCachedRowSet().beforeFirst();

			while (exportModel.getMergeModel().getJoinsModel().getNonCrossDatatableJoinsAsCachedRowSet().next()) {
		
				
				if (exportModel.getMergeModel().getJoinsModel().getNonCrossDatatableJoinsAsCachedRowSet().getString("datatable_1_column_name") != null) {
					if (datatable1NonCrossDatatableExportColumnMappingsForUpdateSetSQL != "") {
						datatable1NonCrossDatatableExportColumnMappingsForUpdateSetSQL += ", ";
					}
					datatable1NonCrossDatatableExportColumnMappingsForUpdateSetSQL += "`" + exportModel.getDatatableName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getNonCrossDatatableJoinsAsCachedRowSet().getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable1Model().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getNonCrossDatatableJoinsAsCachedRowSet().getString("datatable_1_column_name") + "` ";
				}
				else if (exportModel.getMergeModel().getJoinsModel().getNonCrossDatatableJoinsAsCachedRowSet().getString("datatable_1_column_name") == null && exportModel.getMergeModel().getJoinsModel().getNonCrossDatatableJoinsAsCachedRowSet().getString("constant_1") != null) {
					if (datatable1NonCrossDatatableExportColumnMappingsForUpdateSetSQL != "") {
						datatable1NonCrossDatatableExportColumnMappingsForUpdateSetSQL += ", ";
					}
					datatable1NonCrossDatatableExportColumnMappingsForUpdateSetSQL += "`" + exportModel.getDatatableName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getNonCrossDatatableJoinsAsCachedRowSet().getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable1Model().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getNonCrossDatatableJoinsAsCachedRowSet().getString("constant_1") + "` ";
				}
				// Value will stay null by default
				
				if (exportModel.getMergeModel().getJoinsModel().getNonCrossDatatableJoinsAsCachedRowSet().getString("datatable_2_column_name") != null) {
					if (datatable2NonCrossDatatableExportColumnMappingsForUpdateSetSQL != "") {
						datatable2NonCrossDatatableExportColumnMappingsForUpdateSetSQL += ", ";
					}
					datatable2NonCrossDatatableExportColumnMappingsForUpdateSetSQL += "`" + exportModel.getDatatableName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getNonCrossDatatableJoinsAsCachedRowSet().getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable2Model().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getNonCrossDatatableJoinsAsCachedRowSet().getString("datatable_2_column_name") + "` ";
				}
				else if (exportModel.getMergeModel().getJoinsModel().getNonCrossDatatableJoinsAsCachedRowSet().getString("datatable_2_column_name") == null && exportModel.getMergeModel().getJoinsModel().getNonCrossDatatableJoinsAsCachedRowSet().getString("constant_2") != null) {
					if (datatable2NonCrossDatatableExportColumnMappingsForUpdateSetSQL != "") {
						datatable2NonCrossDatatableExportColumnMappingsForUpdateSetSQL += ", ";
					}
					datatable2NonCrossDatatableExportColumnMappingsForUpdateSetSQL += "`" + exportModel.getDatatableName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getNonCrossDatatableJoinsAsCachedRowSet().getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable2Model().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getNonCrossDatatableJoinsAsCachedRowSet().getString("constant_2") + "` ";
				}
				
			}
			
	
			String datatable1KeyExportColumnMappingsForUpdateWhereSQL = "";
			String datatable2KeyExportColumnMappingsForUpdateWhereSQL = "";		
			
			exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().first();
			
			datatable1KeyExportColumnMappingsForUpdateWhereSQL += "`" + exportModel.getDatatableName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable1Model().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("datatable_1_column_name") + "` ";
			datatable2KeyExportColumnMappingsForUpdateWhereSQL += "`" + exportModel.getDatatableName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable2Model().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("datatable_2_column_name") + "` ";
			
			while (exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().next()) {

				
				datatable1KeyExportColumnMappingsForUpdateWhereSQL += "AND `" + exportModel.getDatatableName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable1Model().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("datatable_1_column_name") + "` ";
				datatable2KeyExportColumnMappingsForUpdateWhereSQL += "AND `" + exportModel.getDatatableName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("column_name") + "` = `" + exportModel.getMergeModel().getDatatable2Model().getName() + "`.`" + exportModel.getMergeModel().getJoinsModel().getKeyJoinsAsCachedRowSet().getString("datatable_2_column_name") + "` ";
								
				
			}		
			
			if (datatable1NonCrossDatatableExportColumnMappingsForUpdateSetSQL != "") {
				String updateExportDatatableWithDatatable1Data = 
					"UPDATE `" + exportModel.getDatatableName() + "`, `" + exportModel.getMergeModel().getDatatable1Model().getName() + "` " + 
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
					"UPDATE `" + exportModel.getDatatableName() + "`, `" + exportModel.getMergeModel().getDatatable2Model().getName() + "` " + 
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


	public ExportModel retrieveExportAsExportModelThroughPopulatingExportDatatableWithKeysUsingExportModel(
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
							"INSERT INTO `" + exportModel.getDatatableName() + "` (" + keyColumnNamesForInsertSQL + ") " +
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
		
		try {
			//Drop the export_datatable
			PreparedStatement preparedStatement = connection.prepareStatement("DROP TABLE IF EXISTS `export_datatable_" + exportModel.getId() + "`;");
			preparedStatement.executeUpdate();
			preparedStatement.close();  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			//Create the export_datatable
			PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE `export_datatable_" + exportModel.getId() + "` (" + columnDefinitionsForCreateSQL + ") ENGINE=InnoDB;");
			preparedStatement.executeUpdate();
			preparedStatement.close();  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		//Record the export datatable name.
		exportModel.setDatatableName("export_datatable_" + exportModel.getId());
		
		
		
		return exportModel;
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
