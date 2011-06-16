package org.cggh.tools.dataMerger.scripts.data.merges;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.joins.JoinModel;
import org.cggh.tools.dataMerger.data.joins.JoinsCRUD;
import org.cggh.tools.dataMerger.data.merges.MergeModel;
import org.cggh.tools.dataMerger.data.merges.MergesCRUD;



public class MergeScripts implements java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3828652891254659545L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.scripts.data.merges");


	public MergeScripts() {
		
	}



	
	public MergeModel retrieveMergeAsMergeModelThroughDeterminingDatatable1DuplicateKeysCountUsingMergeModel(MergeModel mergeModel, Connection connection) {
		
		String Datatable1KeyColumnNamesAsSQLCSV = null;
	
		if (connection != null) {
			
			if (mergeModel.getDatatable1Model().getKeyColumnNamesAsStringList().size() > 0) {
			
			      StringBuffer stringBuffer   = new StringBuffer(2 * (mergeModel.getDatatable1Model().getKeyColumnNamesAsStringList().size() + 1));
		
			      stringBuffer.append('`');
			      for (int i = 0; i < mergeModel.getDatatable1Model().getKeyColumnNamesAsStringList().size(); i++) {
			    	  
			    	  stringBuffer.append(mergeModel.getDatatable1Model().getKeyColumnNamesAsStringList().get(i));
		
			          if (i != mergeModel.getDatatable1Model().getKeyColumnNamesAsStringList().size() - 1) {
			        	  stringBuffer.append('`');
			        	  stringBuffer.append(',');
			        	  stringBuffer.append('`');
			          }
			      }
			      stringBuffer.append('`');
		
		
			      Datatable1KeyColumnNamesAsSQLCSV = stringBuffer.toString();
			      
			      
			      try{
			    	  //SELECT SUM(duplicateValuesCount) AS totalDuplicateValuesCount FROM (SELECT COUNT(CONCAT(`Row`,`ID`)) AS duplicateValuesCount FROM `datatable_3` GROUP BY CONCAT(`Row`,`ID`) HAVING duplicateValuesCount > 1) AS duplicateValuesCounts;
			    	  String statementSQL = "SELECT SUM(duplicateValuesCount) AS totalDuplicateValuesCount FROM (SELECT COUNT(CONCAT(" + Datatable1KeyColumnNamesAsSQLCSV +  ")) AS duplicateValuesCount FROM `" + mergeModel.getDatatable1Model().getName()+ "` GROUP BY CONCAT(" + Datatable1KeyColumnNamesAsSQLCSV +  ") HAVING duplicateValuesCount > 1) AS duplicateValuesCounts;";
			    	  
			    	  
			    	  //logger.info(statementSQL);
			    	  
			    	  
			    	  PreparedStatement preparedStatement = connection.prepareStatement(statementSQL);
			          preparedStatement.executeQuery();
			          ResultSet resultSet = preparedStatement.getResultSet();
			          
			          if (resultSet.next()) {
			        	  
			        	  resultSet.first();
		
			        	  Integer totalDuplicateValuesCount = resultSet.getInt("totalDuplicateValuesCount");
			        	  
			        	  if (totalDuplicateValuesCount != null) {
			        		  
			        		  mergeModel.getDatatable1Model().setDuplicateKeysCount(totalDuplicateValuesCount);
			        		  
			        	  } else {
			        		
			        		  //FIXME:
			        		  //This happens when there are genuinely no duplicate keys (because the Having clause returns no records, which is then Summed, producing a null value). 
			        		  mergeModel.getDatatable1Model().setDuplicateKeysCount(0);
			        	  }
			        	  
			      	  } else {
			      		  
			      		  //This, however, is unexpected.
			      		  this.logger.severe("No results from duplicate keys count.");
			      	  }
			          
			          resultSet.close();
			          preparedStatement.close();
			          
		
			        }
			        catch(SQLException sqlException){
				    	sqlException.printStackTrace();
			        } 
	
			} else {
				//There are no key columns
				mergeModel.getDatatable1Model().setDuplicateKeysCount(0);
			}
			
			MergesCRUD mergesCRUD = new MergesCRUD();
        	mergesCRUD.updateMergeUsingMergeModel(mergeModel, connection);
			
		} else {
			logger.severe("connection is null");
		}
		
			
		        
	     return mergeModel;
	}

	//TODO: Condense these two identical methods into one.
	public MergeModel retrieveMergeAsMergeModelThroughDeterminingDatatable2DuplicateKeysCountUsingMergeModel(MergeModel mergeModel, Connection connection) {
		
		String Datatable2KeyColumnNamesAsSQLCSV = null;
	
		if (connection != null) {
			
			if (mergeModel.getDatatable2Model().getKeyColumnNamesAsStringList().size() > 0) {
			
			      StringBuffer stringBuffer   = new StringBuffer(2 * (mergeModel.getDatatable2Model().getKeyColumnNamesAsStringList().size() + 1));
		
			      stringBuffer.append('`');
			      for (int i = 0; i < mergeModel.getDatatable2Model().getKeyColumnNamesAsStringList().size(); i++) {
			    	  
			    	  stringBuffer.append(mergeModel.getDatatable2Model().getKeyColumnNamesAsStringList().get(i));
		
			          if (i != mergeModel.getDatatable2Model().getKeyColumnNamesAsStringList().size() - 1) {
			        	  stringBuffer.append('`');
			        	  stringBuffer.append(',');
			        	  stringBuffer.append('`');
			          }
			      }
			      stringBuffer.append('`');
		
		
			      Datatable2KeyColumnNamesAsSQLCSV = stringBuffer.toString();
			      
			      //TODO:
			      ////System.out.println("Datatable2KeyColumnNamesAsSQLCSV: " + Datatable2KeyColumnNamesAsSQLCSV);
				
			      try{
			    	  //SELECT SUM(duplicateValuesCount) AS totalDuplicateValuesCount FROM (SELECT COUNT(CONCAT(`Row`,`ID`)) AS duplicateValuesCount FROM `datatable_3` GROUP BY CONCAT(`Row`,`ID`) HAVING duplicateValuesCount > 1) AS duplicateValuesCounts;
		
			    	  
			    	  
			    	  PreparedStatement preparedStatement = connection.prepareStatement("SELECT SUM(duplicateValuesCount) AS totalDuplicateValuesCount FROM (SELECT COUNT(CONCAT(" + Datatable2KeyColumnNamesAsSQLCSV +  ")) AS duplicateValuesCount FROM `" + mergeModel.getDatatable2Model().getName()+ "` GROUP BY CONCAT(" + Datatable2KeyColumnNamesAsSQLCSV +  ") HAVING duplicateValuesCount > 1) AS duplicateValuesCounts;");
			          
			    	  preparedStatement.executeQuery();
			          ResultSet resultSet = preparedStatement.getResultSet();
			          
			          if (resultSet.next()) {
			        	  
			        	  resultSet.first();
		
			        	  Integer totalDuplicateValuesCount = resultSet.getInt("totalDuplicateValuesCount");
			        	  
			        	  if (totalDuplicateValuesCount != null) {
			        		  
			        		  mergeModel.getDatatable2Model().setDuplicateKeysCount(totalDuplicateValuesCount);
			        		  
			        	  } else {
			        		  
			        		  //FIXME:
			        		  //This happens when there are genuinely zero duplicate keys.
			        		  mergeModel.getDatatable2Model().setDuplicateKeysCount(0);
			        	  }
			        	  
			      	  } else {
			      		  
			      		  this.logger.severe("No results for duplicate key count.");
			      	  }
			          
			          resultSet.close();
			          preparedStatement.close();
			          
			        }
			        catch(SQLException sqlException){
				    	sqlException.printStackTrace();
			        } 
			        
			} else {
				//There are no key columns
				mergeModel.getDatatable2Model().setDuplicateKeysCount(0);
			}

	        MergesCRUD mergesCRUD = new MergesCRUD();
	        mergesCRUD.updateMergeUsingMergeModel(mergeModel, connection);
	        
		} else {
			
			logger.severe("connection is null");
		}
	        
	       return mergeModel;
	}





	public MergeModel retrieveMergeAsMergeModelThroughDeterminingTotalConflictsCountByColumnUsingMergeModel(
			MergeModel mergeModel, Connection connection) {

		// Should only add to the model.

		//Note: relies on resolutions_by_column

	      try{
	    	  PreparedStatement preparedStatement = connection.prepareStatement("SELECT SUM(conflicts_count) AS totalConflictsCount FROM resolution_by_column WHERE solution_by_column_id IS NULL AND merge_id = ?;");
	    	  preparedStatement.setInt(1, mergeModel.getId());
	    	  preparedStatement.executeQuery();
	          ResultSet resultSet = preparedStatement.getResultSet();
	          
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();

	        	  Integer totalConflictsCount = resultSet.getInt("totalConflictsCount");
	        	  
	        	  if (totalConflictsCount != null) {
	        		  
	        		  mergeModel.setTotalConflictsCount(totalConflictsCount);
	        		  
	        	  } else {
	        		  
	        		  mergeModel.setTotalConflictsCount(null);
	        	  }
	        	  
	      	  } else {
	      		  
	      		mergeModel.setTotalConflictsCount(null);
	      	  }
	          
	          resultSet.close();
	          preparedStatement.close();
	          
	          // Update the db with the new info
	          MergesCRUD mergesModel = new MergesCRUD();
	          
	          //FIXME: This breaks if it doesn't have the full data, e.g. Upload1Model, Upload2Model.
	          //mergesModel.updateMergeUsingMergeModel(mergeModel, connection);
	          mergesModel.updateTotalConflictsCountUsingMergeModel(mergeModel, connection);
	          

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        }
	        
	        // Return the model with the new info
	        return mergeModel;
	}


	public MergeModel retrieveMergeAsMergeModelThroughGuessingJoinsUsingMergeModel(MergeModel mergeModel,
			Connection connection) {
		
		// Get the column names for each of the datatables.
		  
		  List<String> datatable1ColumnNamesAsStringList = mergeModel.getDatatable1Model().getColumnNamesAsStringList();
		  List<String> datatable2ColumnNamesAsStringList = mergeModel.getDatatable2Model().getColumnNamesAsStringList();
		  
		  JoinsCRUD joinsCRUD = new JoinsCRUD();
		  
		  // Cycle through the column names of one table to see if they match the other table.
		  if (datatable1ColumnNamesAsStringList != null) {
			  

			  
			  
			  int i = 0;
			  
		      while (i < datatable1ColumnNamesAsStringList.size()) {
		    	  
		    	  Boolean matchFound = false;
		    	  
		    	  int j = 0;
		    	  
		    	  if (datatable2ColumnNamesAsStringList != null) {
		    	  
			    	  while (j < datatable2ColumnNamesAsStringList.size()) {
			    		  
			    		  // Case insensitive.
			    		  if (datatable1ColumnNamesAsStringList.get(i).toLowerCase().equals(datatable2ColumnNamesAsStringList.get(j).toLowerCase())) {
			    			  
			    			  // We have a match.
			    			  matchFound = true;
			    			  
			    			  
			    			  JoinModel joinModel = new JoinModel();
			    			  
			    			  joinModel.setColumnNumber(joinsCRUD.getNextColumnNumberByMergeId(mergeModel.getId(), connection));
			    			  joinModel.setKey(false);
			    			  joinModel.setDatatable1ColumnName(datatable1ColumnNamesAsStringList.get(i));
			    			  joinModel.setDatatable2ColumnName(datatable2ColumnNamesAsStringList.get(j));
			    			  joinModel.setColumnName(datatable1ColumnNamesAsStringList.get(i));
			    			  
			    			  joinModel.setMergeModel(mergeModel);
			    			  
			    			  joinsCRUD.createJoinUsingJoinModel(joinModel, connection);
			    			  
			    			  
			    			  //Remove items to make this more efficient.
			    			  
			    			  datatable2ColumnNamesAsStringList.remove(j);
			    			  j--;
			    			  
			    			  //TODO: What if there are more than one column with the same name?
			    			  // This algorithm joins only the first occurrence of matching columns.
			    			  break;
			    			  
			    			  //TODO
			    			  ////System.out.println(datatable1ColumnNames[i] + " matches " + datatable2ColumnNames[j]);

			    		  }
			    		  
			    		  j++;
			    		  
			    	  }
			    	  
		    	  } else {
		    		  
		    		  //TODO:
			    	  //System.out.println("datatable1ColumnNamesAsStringList is null");
		    	  }
		    	  
		    	  if (!matchFound) {
		    		  
					  JoinModel joinModel = new JoinModel();
					  
					  joinModel.setMergeModel(mergeModel);
					  
					  joinModel.setColumnNumber(joinsCRUD.getNextColumnNumberByMergeId(mergeModel.getId(), connection));
					  joinModel.setKey(false);
					  joinModel.setDatatable1ColumnName(datatable1ColumnNamesAsStringList.get(i));
					  joinModel.setDatatable2ColumnName(null);
					  joinModel.setColumnName(datatable1ColumnNamesAsStringList.get(i));
					  
					  
					  joinsCRUD.createJoinUsingJoinModel(joinModel, connection);
					   
					  //Remove this item from the list to make this more efficient.
					  datatable1ColumnNamesAsStringList.remove(i);
					  i--;
					  
		    	  } else {
		    		  
		    		  //Remove this item from the list to make this more efficient.
		    		  datatable1ColumnNamesAsStringList.remove(i);
		    		  i--;
		    	  }
		    	  
		    	  
		    	  i++;
		      }
		      
		  } else {
			  
			  //TODO:
			  //System.out.println("datatable1ColumnNamesAsStringList is null");
		  }
		  
		 //TODO: The columns in datatable_2 that don't match datatable_1
		  //TODO: Make this more efficient.
		  
		  if (datatable2ColumnNamesAsStringList != null) {
			  
			  int i = 0;
			  
		      while (i < datatable2ColumnNamesAsStringList.size()) {
		    	  
		    	  Boolean matchFound = false;
		    	  
		    	  if (datatable1ColumnNamesAsStringList != null) {
			    	  for (int j = 0; j < datatable1ColumnNamesAsStringList.size(); j++) {
			    		  
			    		  // Case insensitive.
			    		  if (datatable2ColumnNamesAsStringList.get(i).toLowerCase().equals(datatable1ColumnNamesAsStringList.get(j).toLowerCase())) {
			    			  
			    			  // We have a match.
			    			  matchFound = true;

			    		  }
			    		  
			    	  }
			    	  
		    	  } else {
		    		//TODO:
			    	  //System.out.println("datatable1ColumnNamesAsStringList is null");
		    	  }
		    	  
		    	  if (!matchFound) {
		    		  
					  JoinModel joinModel = new JoinModel();
					  
					  joinModel.setMergeModel(mergeModel);
					  
					  joinModel.setColumnNumber(joinsCRUD.getNextColumnNumberByMergeId(mergeModel.getId(), connection));
					  joinModel.setKey(false);
					  joinModel.setDatatable1ColumnName(null);
					  joinModel.setDatatable2ColumnName(datatable2ColumnNamesAsStringList.get(i));
					  joinModel.setColumnName(datatable2ColumnNamesAsStringList.get(i));
					  
					  joinsCRUD.createJoinUsingJoinModel(joinModel, connection);
					   
					  //Remove this item from the list to make this more efficient.
					  datatable2ColumnNamesAsStringList.remove(i);
					  i--;
					  
		    	  }
		    	  
		    	  
		    	  i++;
		      }
		      
		  } else {
			  
			  //TODO:
			  //System.out.println("datatable2ColumnNamesAsStringList is null");
		  }
		  
		  
		  mergeModel.setJoinsModel(joinsCRUD.retrieveJoinsAsJoinsModelByMergeId(mergeModel.getId(), connection));
					  
		  
		  return mergeModel;
	}	


	public MergeModel retrieveMergeAsMergeModelThroughGuessingKeysUsingMergeModel(
			MergeModel mergeModel, Connection connection) throws SQLException {

		
		JoinsCRUD joinsCRUD = new JoinsCRUD();
		
		mergeModel.getJoinsModel().getCrossDatatableJoinsAsCachedRowSet().beforeFirst();

		if (mergeModel.getJoinsModel().getCrossDatatableJoinsAsCachedRowSet().next()) {

			//because next() skips the first row.
			mergeModel.getJoinsModel().getCrossDatatableJoinsAsCachedRowSet().beforeFirst();

			while (mergeModel.getJoinsModel().getCrossDatatableJoinsAsCachedRowSet().next()) {

				// If there are no duplicate values in this shared column for either datatable...
				if (mergeModel.getDatatable1Model().getDuplicateValuesCountByColumnName(mergeModel.getJoinsModel().getCrossDatatableJoinsAsCachedRowSet().getString("datatable_1_column_name"), connection) == 0
						&& mergeModel.getDatatable2Model().getDuplicateValuesCountByColumnName(mergeModel.getJoinsModel().getCrossDatatableJoinsAsCachedRowSet().getString("datatable_2_column_name"), connection) == 0

				) {

					//// Guess that this join is a key.
					
					// Marshal cached row into a JoinModel, while setting the key to true
					JoinModel joinModel = new JoinModel();
					joinModel
							.getMergeModel()
							.setId(mergeModel
									.getJoinsModel()
									.getCrossDatatableJoinsAsCachedRowSet()
									.getInt("merge_id"));
					joinModel
							.setColumnNumber(mergeModel
									.getJoinsModel()
									.getCrossDatatableJoinsAsCachedRowSet()
									.getInt("column_number"));
					joinModel.setKey(true);
					joinModel
							.setDatatable1ColumnName(mergeModel
									.getJoinsModel()
									.getCrossDatatableJoinsAsCachedRowSet()
									.getString(
											"datatable_1_column_name"));
					joinModel
							.setDatatable2ColumnName(mergeModel
									.getJoinsModel()
									.getCrossDatatableJoinsAsCachedRowSet()
									.getString(
											"datatable_2_column_name"));
					joinModel
							.setConstant1(mergeModel
									.getJoinsModel()
									.getCrossDatatableJoinsAsCachedRowSet()
									.getString("constant_1"));
					joinModel
							.setConstant2(mergeModel
									.getJoinsModel()
									.getCrossDatatableJoinsAsCachedRowSet()
									.getString("constant_2"));
					joinModel
							.setColumnName(mergeModel
									.getJoinsModel()
									.getCrossDatatableJoinsAsCachedRowSet()
									.getString("column_name"));

					// Update the join record in the db.
					joinsCRUD.updateJoinByJoinModel(joinModel, connection);

					
					
					//Record keyColumnNames to save two subsequent requests.
					mergeModel
							.getDatatable1Model()
							.getKeyColumnNamesAsStringList()
							.add(mergeModel.getJoinsModel().getCrossDatatableJoinsAsCachedRowSet()
									.getString("datatable_1_column_name"));
					mergeModel
							.getDatatable2Model()
							.getKeyColumnNamesAsStringList()
							.add(mergeModel.getJoinsModel().getCrossDatatableJoinsAsCachedRowSet()
									.getString("datatable_2_column_name"));

				}

			}

		} else {

			//This is not necessarily an error, because the auto-join might not have found any columns with similar names. 
			//this.logger.info("Did not retrieve any rows in crossDatatableJoinsAsCachedRowSet for the specified merge.");

		}
		
		MergesCRUD mergesModel = new MergesCRUD();
		mergeModel = mergesModel.retrieveMergeAsMergeModelByMergeId(mergeModel.getId(), connection);
		
		return mergeModel;
	}

	
	public MergeModel retrieveMergeAsMergeModelThroughDeterminingDataConflictsUsingMergeModel(
			MergeModel mergeModel, Connection connection) {
		
		
		if (mergeModel.getJoinsModel().getKeysCount() > 0 && mergeModel.getTotalDuplicateKeysCount() == 0) {

			MergesCRUD mergesModel = new MergesCRUD();
			
			mergeModel = mergesModel.retrieveMergeAsMergeModelThroughRecreatingJoinedKeytableUsingMergeModel(mergeModel, connection);
			
			JoinsCRUD joinsModel = new JoinsCRUD();
			mergeModel.getJoinsModel().setKeyJoinsAsCachedRowSet(joinsModel.retrieveKeyJoinsAsCachedRowSetByMergeId(mergeModel.getId(),connection));
			mergeModel.getJoinsModel().setNonKeyCrossDatatableJoinsAsCachedRowSet(joinsModel.retrieveNonKeyCrossDatatableJoinsAsCachedRowsetByMergeId(mergeModel.getId(), connection));

			
			
			try {		
				// Remove all previously recorded conflicts for this merge.
		        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `resolution` WHERE merge_id = ?;");
		        preparedStatement.setInt(1, mergeModel.getId());
		        preparedStatement.executeUpdate();
		        preparedStatement.close();
		
		        
		        HashMap<Integer, String> datatable1ColumnNamesByColumnNumberAsHashMap = joinsModel.retrieveDatatable1ColumnNamesByColumnNumberAsHashMapUsingMergeId(mergeModel.getId(), connection); 
		        HashMap<Integer, String> datatable2ColumnNamesByColumnNumberAsHashMap = joinsModel.retrieveDatatable2ColumnNamesByColumnNumberAsHashMapUsingMergeId(mergeModel.getId(), connection);
		        
		        StringBuffer datatable1JoinSQL = new StringBuffer();
		        StringBuffer datatable2JoinSQL = new StringBuffer();

		        StringBuffer columnDefinitionsUsingKeyColumnNamesSQL = new StringBuffer();
		        StringBuffer joinedKeytableColumnAliasesAsCSVForSelectFromJoinSQL = new StringBuffer();
		        
		        CachedRowSet keyJoinsAsCachedRowSet = joinsModel.retrieveKeyJoinsAsCachedRowSetByMergeId(mergeModel.getId(), connection);
		        
		        keyJoinsAsCachedRowSet.beforeFirst();
		        
		        if (keyJoinsAsCachedRowSet.next()) {
		        	
		        	columnDefinitionsUsingKeyColumnNamesSQL.append("`").append(keyJoinsAsCachedRowSet.getString("column_name")).append("` VARCHAR(36) ");
		        	
		        	datatable1JoinSQL.append("`").append(mergeModel.getDatatable1Model().getName()).append("`.`").append(datatable1ColumnNamesByColumnNumberAsHashMap.get(keyJoinsAsCachedRowSet.getInt("column_number"))).append("` = `").append(mergeModel.getJoinedKeytableModel().getName()).append("`.`key_column_").append(keyJoinsAsCachedRowSet.getInt("column_number")).append("` ");
		        	datatable2JoinSQL.append("`").append(mergeModel.getDatatable2Model().getName()).append("`.`").append(datatable2ColumnNamesByColumnNumberAsHashMap.get(keyJoinsAsCachedRowSet.getInt("column_number"))).append("` = `").append(mergeModel.getJoinedKeytableModel().getName()).append("`.`key_column_").append(keyJoinsAsCachedRowSet.getInt("column_number")).append("` AND `").append(mergeModel.getDatatable2Model().getName()).append("`.`").append(datatable2ColumnNamesByColumnNumberAsHashMap.get(keyJoinsAsCachedRowSet.getInt("column_number"))).append("` = `").append(mergeModel.getDatatable1Model().getName()).append("`.`").append(datatable1ColumnNamesByColumnNumberAsHashMap.get(keyJoinsAsCachedRowSet.getInt("column_number"))).append("` ");
		        	
		        	joinedKeytableColumnAliasesAsCSVForSelectFromJoinSQL.append("`").append(mergeModel.getJoinedKeytableModel().getName()).append("`.`key_column_").append(keyJoinsAsCachedRowSet.getInt("column_number")).append("` AS `").append(keyJoinsAsCachedRowSet.getString("column_name")).append("` ");
		        	
			        while (keyJoinsAsCachedRowSet.next()) {
			        
			        	columnDefinitionsUsingKeyColumnNamesSQL.append(", `").append(keyJoinsAsCachedRowSet.getString("column_name")).append("` VARCHAR(36) ");
			        	
			        	datatable1JoinSQL.append("AND `").append(mergeModel.getDatatable1Model().getName()).append("`.`" + datatable1ColumnNamesByColumnNumberAsHashMap.get(keyJoinsAsCachedRowSet.getInt("column_number"))).append("` = `").append(mergeModel.getJoinedKeytableModel().getName()).append("`.`key_column_").append(keyJoinsAsCachedRowSet.getInt("column_number")).append("` ");
			        	datatable2JoinSQL.append("AND `").append(mergeModel.getDatatable2Model().getName()).append("`.`" + datatable2ColumnNamesByColumnNumberAsHashMap.get(keyJoinsAsCachedRowSet.getInt("column_number"))).append("` = `").append(mergeModel.getJoinedKeytableModel().getName()).append("`.`key_column_").append(keyJoinsAsCachedRowSet.getInt("column_number")).append("` AND `").append(mergeModel.getDatatable2Model().getName()).append("`.`").append(datatable2ColumnNamesByColumnNumberAsHashMap.get(keyJoinsAsCachedRowSet.getInt("column_number"))).append("` = `").append(mergeModel.getDatatable1Model().getName()).append("`.`").append(datatable1ColumnNamesByColumnNumberAsHashMap.get(keyJoinsAsCachedRowSet.getInt("column_number"))).append("` ");
			        	
			        	joinedKeytableColumnAliasesAsCSVForSelectFromJoinSQL.append(", `").append(mergeModel.getJoinedKeytableModel().getName()).append("`.`key_column_").append(keyJoinsAsCachedRowSet.getInt("column_number")).append("` AS `").append(keyJoinsAsCachedRowSet.getString("column_name")).append("` ");
			        }
			        
		        } else {
		        	this.logger.severe("Did not retrieve any key joins as a cached row set using merge Id: " + mergeModel.getId());
		        }
		        
		        
		     // Record the joined datatable name for this merge.
				mergeModel.getJoinedDatatableModel().setName("joined_datatable_" + mergeModel.getId());
				mergesModel.updateMergeJoinedDatatableUsingMergeModel(mergeModel, connection);
		        

		        StringBuffer nonKeyCrossDatatableColumnAliasesAsCSVForSelectFromJoinSQL = new StringBuffer();	        
		        StringBuffer columnDefinitionsUsingNonKeyCrossDatatableColumnAndSourceNumbersSQL = new StringBuffer();
		        StringBuffer conflictUnionsSQL = new StringBuffer();
		        
		        CachedRowSet nonKeyCrossDatatableJoinsAsCachedRowSet = joinsModel.retrieveNonKeyCrossDatatableJoinsAsCachedRowsetByMergeId(mergeModel.getId(), connection);

		        nonKeyCrossDatatableJoinsAsCachedRowSet.beforeFirst();
		        
		        if (nonKeyCrossDatatableJoinsAsCachedRowSet.next()) {
		        	
		        	columnDefinitionsUsingNonKeyCrossDatatableColumnAndSourceNumbersSQL.append("`column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getString("column_number")).append("_source_1` VARCHAR(36), `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getString("column_number")).append("_source_2` VARCHAR(36) ");
		        	
		        	nonKeyCrossDatatableColumnAliasesAsCSVForSelectFromJoinSQL.append("`").append(mergeModel.getDatatable1Model().getName()).append("`.`").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getString("datatable_1_column_name")).append("` AS `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_1`, `").append(mergeModel.getDatatable2Model().getName()).append("`.`").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getString("datatable_2_column_name")).append("` AS `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_2` ");

		        	//TODO: Alter this logic so the same strings aren't repeated
		        	conflictUnionsSQL.append("(")
		        							.append("SELECT ").append(mergeModel.getId()).append(" AS merge_id, joined_keytable_id, ").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append(" AS column_number, 1 AS conflict_id FROM `").append(mergeModel.getJoinedDatatableModel().getName()).append("` ")
		        							.append("WHERE `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_1` != `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_2` AND `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_1` IS NOT NULL AND `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_1` != '' AND `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_2` IS NOT NULL AND `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_2` != ''")
		        							.append(") ");
		        	
		        	conflictUnionsSQL.append("UNION ")
		        							.append("(")
		        							.append("SELECT ").append(mergeModel.getId()).append(" AS merge_id, joined_keytable_id, ").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append(" AS column_number, 2 AS conflict_id FROM `").append(mergeModel.getJoinedDatatableModel().getName()).append("` ")
		        							.append("WHERE (`column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_1` IS NULL OR `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_1` = '') AND `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_2` IS NOT NULL AND `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_2` != ''")
		        							.append(") ");
		        	
		        	conflictUnionsSQL.append("UNION ")
		        							.append("(")
		        							.append("SELECT ").append(mergeModel.getId()).append(" AS merge_id, joined_keytable_id, ").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append(" AS column_number, 3 AS conflict_id FROM `").append(mergeModel.getJoinedDatatableModel().getName()).append("` ")
		        							.append("WHERE (`column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_2` IS NULL OR `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_2` = '') AND `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_1` IS NOT NULL AND `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_1` != ''")
		        							.append(") ");
		        	
			        while (nonKeyCrossDatatableJoinsAsCachedRowSet.next()) {
			        
			        	columnDefinitionsUsingNonKeyCrossDatatableColumnAndSourceNumbersSQL.append(", `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getString("column_number")).append("_source_1` VARCHAR(36), `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getString("column_number")).append("_source_2` VARCHAR(36) ");

			        	nonKeyCrossDatatableColumnAliasesAsCSVForSelectFromJoinSQL.append(", `").append(mergeModel.getDatatable1Model().getName()).append("`.`").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getString("datatable_1_column_name")).append("` AS `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_1`, `").append(mergeModel.getDatatable2Model().getName()).append("`.`").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getString("datatable_2_column_name")).append("` AS `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_2` ");
			        
			        	conflictUnionsSQL.append("UNION ")
			        	.append("(")
			        	.append("SELECT ").append(mergeModel.getId()).append(" AS merge_id, joined_keytable_id, ").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append(" AS column_number, 1 AS conflict_id FROM `").append(mergeModel.getJoinedDatatableModel().getName()).append("` ")
			        	.append("WHERE `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_1` != `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_2` AND `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_1` IS NOT NULL AND `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_1` != '' AND `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_2` IS NOT NULL AND `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_2` != ''")
			        	.append(") ");
			        	
			        	conflictUnionsSQL.append("UNION ")
			        	.append("(")
			        	.append("SELECT ").append(mergeModel.getId()).append(" AS merge_id, joined_keytable_id, ").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append(" AS column_number, 2 AS conflict_id FROM `").append(mergeModel.getJoinedDatatableModel().getName()).append("` ")
			        	.append("WHERE (`column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_1` IS NULL OR `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_1` = '') AND `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_2` IS NOT NULL AND `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_2` != ''")
			        	.append(") ");

						conflictUnionsSQL.append("UNION ")
						.append("(")
						.append("SELECT ").append(mergeModel.getId()).append(" AS merge_id, joined_keytable_id, ").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append(" AS column_number, 3 AS conflict_id FROM `").append(mergeModel.getJoinedDatatableModel().getName()).append("` ")
						.append("WHERE (`column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_2` IS NULL OR `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_2` = '') AND `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_1` IS NOT NULL AND `column_").append(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number")).append("_source_1` != ''")
						.append(") ");
									        	
			        }
			        
		        } else {
		        	
		        	//NOTE: This is not necessarily an error in usage (joins may only have keys in common).
		        	//this.logger.severe("Did not retrieve any non-key cross-datatable joins as a cached row set using merge Id: " + mergeModel.getId());
		        }	        
		        

		        if (columnDefinitionsUsingNonKeyCrossDatatableColumnAndSourceNumbersSQL.length() > 0) {
		        	columnDefinitionsUsingNonKeyCrossDatatableColumnAndSourceNumbersSQL = new StringBuffer(", " + columnDefinitionsUsingNonKeyCrossDatatableColumnAndSourceNumbersSQL.toString());
		        }
		        if (nonKeyCrossDatatableColumnAliasesAsCSVForSelectFromJoinSQL.length() > 0) {
		        	nonKeyCrossDatatableColumnAliasesAsCSVForSelectFromJoinSQL = new StringBuffer(", " + nonKeyCrossDatatableColumnAliasesAsCSVForSelectFromJoinSQL.toString());
		        }
		        
				try {
					
					
						//Recreate the joined datatable
					
						//De-register the joined datatable name (in-case of recreation failure)
						String joinedDatatableName = mergeModel.getJoinedDatatableModel().getName();
						mergeModel.getJoinedDatatableModel().setName(null);
						
						MergesCRUD mergesCRUD = new MergesCRUD();
						//haven't got databaseModel, need to rely on connection
						mergesCRUD.updateMergeJoinedDatatableUsingMergeModel(mergeModel, connection);
						

						String dropTemporaryJoinedDatatableSQL = "DROP TABLE IF EXISTS `" + joinedDatatableName + "`;";

						//this.logger.info(dropTemporaryJoinedDatatableSQL);
						
						PreparedStatement preparedStatement2 = connection.prepareStatement(dropTemporaryJoinedDatatableSQL);
						preparedStatement2.executeUpdate();
						preparedStatement2.close();				
					
					
						String createAndPopulateTemporaryJoinedDatatableSQL = "CREATE TABLE `" + joinedDatatableName + "` (joined_keytable_id BIGINT(255), " + columnDefinitionsUsingKeyColumnNamesSQL.toString() + columnDefinitionsUsingNonKeyCrossDatatableColumnAndSourceNumbersSQL.toString() + ", PRIMARY KEY (joined_keytable_id)) ENGINE=InnoDB " +
															        			"SELECT `" + mergeModel.getJoinedKeytableModel().getName() + "`.id AS joined_keytable_id, " + joinedKeytableColumnAliasesAsCSVForSelectFromJoinSQL.toString() + nonKeyCrossDatatableColumnAliasesAsCSVForSelectFromJoinSQL.toString() + 
															        			"FROM `" + mergeModel.getJoinedKeytableModel().getName() + "` " + 
															        			"JOIN `" + mergeModel.getDatatable1Model().getName() + "` ON " + datatable1JoinSQL.toString() + 
															        			"JOIN `" + mergeModel.getDatatable2Model().getName() + "` ON " + datatable2JoinSQL.toString() + 
															        			"ORDER BY `" + mergeModel.getJoinedKeytableModel().getName() + "`.id" +
															        			";";

						//
						//this.logger.info(createAndPopulateTemporaryJoinedDatatableSQL);
						
						//Re-register joined datatable name (now assuming success)
						mergeModel.getJoinedDatatableModel().setName(joinedDatatableName);
						mergesCRUD.updateMergeJoinedDatatableUsingMergeModel(mergeModel, connection);
						
						
				        PreparedStatement preparedStatement3 = connection.prepareStatement(createAndPopulateTemporaryJoinedDatatableSQL);
				        preparedStatement3.executeUpdate();
				        preparedStatement3.close();

				        if (conflictUnionsSQL.length() > 0) {
				        
							String insertConflictsByCellSQL = "" +
									"INSERT INTO resolution (merge_id, joined_keytable_id, column_number, conflict_id) " +
										conflictUnionsSQL.toString() +
										" ORDER BY joined_keytable_id, column_number, conflict_id " + 
									";";
	
							//
							//this.logger.info(insertConflictsByCellSQL);
							
							PreparedStatement preparedStatement4 = connection.prepareStatement(insertConflictsByCellSQL);
							preparedStatement4.executeUpdate();
							preparedStatement4.close();
							
				        }

						//Don't drop the joined datatable, it will be used again for the export.					
						
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		       
		
		      }
		      catch(SQLException sqlException){
			    	sqlException.printStackTrace();
		      }
			
		      
				// Get an up-to-date count of the total conflicts.
				mergeModel = this.retrieveMergeAsMergeModelThroughDeterminingTotalConflictsCountUsingMergeModel(mergeModel, connection);
	

		}
		return mergeModel;
	}







	public MergeModel retrieveMergeAsMergeModelThroughDeterminingTotalConflictsCountUsingMergeModel(
			MergeModel mergeModel, Connection connection) {

	      try{
	    	  PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) AS totalConflictsCount FROM resolution WHERE (solution_by_column_id IS NULL AND solution_by_row_id IS NULL AND solution_by_cell_id IS NULL) AND merge_id =  ?;");
	    	  preparedStatement.setInt(1, mergeModel.getId());
	    	  preparedStatement.executeQuery();
	          ResultSet resultSet = preparedStatement.getResultSet();
	          
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();

	        	  Integer totalConflictsCount = resultSet.getInt("totalConflictsCount");
	        	  
	        	  if (totalConflictsCount != null) {
	        		  
	        		  mergeModel.setTotalConflictsCount(totalConflictsCount);
	        		  
	        	  } else {
	        		  
	        		  mergeModel.setTotalConflictsCount(null);
	        	  }
	        	  
	      	  } else {
	      		  
	      		mergeModel.setTotalConflictsCount(null);
	      	  }
	          
	          resultSet.close();
	          preparedStatement.close();
	          
	          // Update the db with the new info
	          MergesCRUD mergesModel = new MergesCRUD();
	          mergesModel.updateTotalConflictsCountUsingMergeModel(mergeModel, connection);
	          

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        }
	        
	        // Return the model with the new info
	        return mergeModel;
		
		
	}


}
