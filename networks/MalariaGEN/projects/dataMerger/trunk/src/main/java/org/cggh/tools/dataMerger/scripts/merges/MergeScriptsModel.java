package org.cggh.tools.dataMerger.scripts.merges;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.joins.JoinModel;
import org.cggh.tools.dataMerger.data.joins.JoinsModel;
import org.cggh.tools.dataMerger.data.merges.MergeModel;
import org.cggh.tools.dataMerger.data.merges.MergesModel;



public class MergeScriptsModel implements java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3828652891254659545L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.scripts.merges");


	public MergeScriptsModel() {
		
	}



	
	public MergeModel retrieveMergeAsMergeModelThroughDeterminingDatatable1DuplicateKeysCountUsingMergeModel(MergeModel mergeModel, Connection connection) {
		
		String Datatable1KeyColumnNamesAsSQLCSV = null;

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
	    	  PreparedStatement preparedStatement = connection.prepareStatement("SELECT SUM(duplicateValuesCount) AS totalDuplicateValuesCount FROM (SELECT COUNT(CONCAT(" + Datatable1KeyColumnNamesAsSQLCSV +  ")) AS duplicateValuesCount FROM `" + mergeModel.getDatatable1Model().getName()+ "` GROUP BY CONCAT(" + Datatable1KeyColumnNamesAsSQLCSV +  ") HAVING duplicateValuesCount > 1) AS duplicateValuesCounts;");
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
	          
	          
	          //TODO:
	          //System.out.println("Setting dt1 duplicate keys count to: " + mergeModel.getDatatable1Model().getDuplicateKeysCount());
	          
	          MergesModel mergesModel = new MergesModel();
	          
	          mergesModel.updateMergeUsingMergeModel(mergeModel, connection);
	          

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 

	        
	        return mergeModel;
	}

	//TODO: Condense these two identical methods into one.
	public MergeModel retrieveMergeAsMergeModelThroughDeterminingDatatable2DuplicateKeysCountUsingMergeModel(MergeModel mergeModel, Connection connection) {
		
		String Datatable2KeyColumnNamesAsSQLCSV = null;

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
	      //System.out.println("Datatable2KeyColumnNamesAsSQLCSV: " + Datatable2KeyColumnNamesAsSQLCSV);
		
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
	          
	          
	          MergesModel mergesModel = new MergesModel();
	          
	          mergesModel.updateMergeUsingMergeModel(mergeModel, connection);
	          

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
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
	          MergesModel mergesModel = new MergesModel();
	          
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
			    			  
			    			  joinModel.setColumnNumber(mergeModel.getJoinsModel().getNextColumnNumberByMergeId(mergeModel.getId(), connection));
			    			  joinModel.setKey(false);
			    			  joinModel.setDatatable1ColumnName(datatable1ColumnNamesAsStringList.get(i));
			    			  joinModel.setDatatable2ColumnName(datatable2ColumnNamesAsStringList.get(j));
			    			  joinModel.setColumnName(datatable1ColumnNamesAsStringList.get(i));
			    			  
			    			  joinModel.setMergeModel(mergeModel);
			    			  
			    			  mergeModel.getJoinsModel().createJoinUsingJoinModel(joinModel, connection);
			    			  
			    			  
			    			  //Remove items to make this more efficient.
			    			  
			    			  datatable2ColumnNamesAsStringList.remove(j);
			    			  j--;
			    			  
			    			  //TODO: What if there are more than one column with the same name?
			    			  // This algorithm joins only the first occurrence of matching columns.
			    			  break;
			    			  
			    			  //TODO
			    			  //System.out.println(datatable1ColumnNames[i] + " matches " + datatable2ColumnNames[j]);

			    		  }
			    		  
			    		  j++;
			    		  
			    	  }
			    	  
		    	  } else {
		    		  
		    		  //TODO:
			    	  System.out.println("datatable1ColumnNamesAsStringList is null");
		    	  }
		    	  
		    	  if (!matchFound) {
		    		  
					  JoinModel joinModel = new JoinModel();
					  
					  joinModel.setMergeModel(mergeModel);
					  
					  joinModel.setColumnNumber(mergeModel.getJoinsModel().getNextColumnNumberByMergeId(mergeModel.getId(), connection));
					  joinModel.setKey(false);
					  joinModel.setDatatable1ColumnName(datatable1ColumnNamesAsStringList.get(i));
					  joinModel.setDatatable2ColumnName(null);
					  joinModel.setColumnName(datatable1ColumnNamesAsStringList.get(i));
					  
					  
					  mergeModel.getJoinsModel().createJoinUsingJoinModel(joinModel, connection);
					   
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
			  System.out.println("datatable1ColumnNamesAsStringList is null");
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
			    	  System.out.println("datatable1ColumnNamesAsStringList is null");
		    	  }
		    	  
		    	  if (!matchFound) {
		    		  
					  JoinModel joinModel = new JoinModel();
					  
					  joinModel.setMergeModel(mergeModel);
					  
					  joinModel.setColumnNumber(mergeModel.getJoinsModel().getNextColumnNumberByMergeId(mergeModel.getId(), connection));
					  joinModel.setKey(false);
					  joinModel.setDatatable1ColumnName(null);
					  joinModel.setDatatable2ColumnName(datatable2ColumnNamesAsStringList.get(i));
					  joinModel.setColumnName(datatable2ColumnNamesAsStringList.get(i));
					  
					  mergeModel.getJoinsModel().createJoinUsingJoinModel(joinModel, connection);
					   
					  //Remove this item from the list to make this more efficient.
					  datatable2ColumnNamesAsStringList.remove(i);
					  i--;
					  
		    	  }
		    	  
		    	  
		    	  i++;
		      }
		      
		  } else {
			  
			  //TODO:
			  System.out.println("datatable2ColumnNamesAsStringList is null");
		  }
		  
		  
		  mergeModel.setJoinsModel(mergeModel.getJoinsModel().retrieveJoinsAsJoinsModelByMergeId(mergeModel.getId(), connection));
					  
		  
		  return mergeModel;
	}	


	public MergeModel retrieveMergeAsMergeModelThroughGuessingKeysUsingMergeModel(
			MergeModel mergeModel, Connection connection) throws SQLException {
		
		
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
					mergeModel.getJoinsModel()
							.updateJoinByJoinModel(joinModel,
									connection);

					
					
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
			this.logger.info("Did not retrieve any rows in crossDatatableJoinsAsCachedRowSet for the specified merge.");

		}
		
		MergesModel mergesModel = new MergesModel();
		mergeModel = mergesModel.retrieveMergeAsMergeModelByMergeId(mergeModel.getId(), connection);
		
		return mergeModel;
	}

	
	public MergeModel retrieveMergeAsMergeModelThroughDeterminingDataConflictsUsingMergeModel(
			MergeModel mergeModel, Connection connection) {
		
		
		if (mergeModel.getJoinsModel().getKeysCount() > 0 && mergeModel.getTotalDuplicateKeysCount() == 0) {

			//TODO
			// By Cell (also covers by column and row)
			
			this.logger.info("About to determine conflicts (generally)...");
			//////////////////////////////////////////////
			
			//Use the cell-named method until it has developed into general solution.
			
			//Need this stuff (res-by-row currently does too)
			
			MergesModel mergesModel = new MergesModel();
			
			mergeModel = mergesModel.retrieveMergeAsMergeModelThroughRecreatingJoinedKeytableUsingMergeModel(mergeModel, connection);
			
			JoinsModel joinsModel = new JoinsModel();
			mergeModel.getJoinsModel().setKeyJoinsAsCachedRowSet(joinsModel.retrieveKeyJoinsAsCachedRowsetByMergeId(mergeModel.getId(),connection));
			mergeModel.getJoinsModel().setNonKeyCrossDatatableJoinsAsCachedRowSet(joinsModel.retrieveNonKeyCrossDatatableJoinsAsCachedRowsetByMergeId(mergeModel.getId(), connection));

			
			
			try {		
				// Remove all previously recorded conflicts for this merge.
		        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `resolution` WHERE merge_id = ?;");
		        preparedStatement.setInt(1, mergeModel.getId());
		        preparedStatement.executeUpdate();
		        preparedStatement.close();
		
		        
		        HashMap<Integer, String> datatable1ColumnNamesByColumnNumberAsHashMap = joinsModel.retrieveDatatable1ColumnNamesByColumnNumberAsHashMapUsingMergeId(mergeModel.getId(), connection); 
		        HashMap<Integer, String> datatable2ColumnNamesByColumnNumberAsHashMap = joinsModel.retrieveDatatable2ColumnNamesByColumnNumberAsHashMapUsingMergeId(mergeModel.getId(), connection);
		        
		        String datatable1JoinSQL = "";
		        String datatable2JoinSQL = "";

		        String columnDefinitionsUsingKeyColumnNamesSQL = "";
		        String joinedKeytableColumnAliasesAsCSVForSelectFromJoinSQL = "";
		        
		        CachedRowSet keyJoinsAsCachedRowSet = joinsModel.retrieveKeyJoinsAsCachedRowsetByMergeId(mergeModel.getId(), connection);
		        
		        keyJoinsAsCachedRowSet.beforeFirst();
		        
		        if (keyJoinsAsCachedRowSet.next()) {
		        	
		        	columnDefinitionsUsingKeyColumnNamesSQL += "`" + keyJoinsAsCachedRowSet.getString("column_name") + "` VARCHAR(255) ";
		        	
		        	datatable1JoinSQL += "`" + mergeModel.getDatatable1Model().getName() + "`.`" + datatable1ColumnNamesByColumnNumberAsHashMap.get(keyJoinsAsCachedRowSet.getInt("column_number")) + "` = `" + mergeModel.getJoinedKeytableModel().getName() + "`.`key_column_" + keyJoinsAsCachedRowSet.getInt("column_number") + "` ";
		        	datatable2JoinSQL += "`" + mergeModel.getDatatable2Model().getName() + "`.`" + datatable2ColumnNamesByColumnNumberAsHashMap.get(keyJoinsAsCachedRowSet.getInt("column_number")) + "` = `" + mergeModel.getJoinedKeytableModel().getName() + "`.`key_column_" + keyJoinsAsCachedRowSet.getInt("column_number") + "` AND `" + mergeModel.getDatatable2Model().getName() + "`.`" + datatable2ColumnNamesByColumnNumberAsHashMap.get(keyJoinsAsCachedRowSet.getInt("column_number")) + "` = `" + mergeModel.getDatatable1Model().getName() + "`.`" + datatable1ColumnNamesByColumnNumberAsHashMap.get(keyJoinsAsCachedRowSet.getInt("column_number")) + "` ";
		        	
		        	joinedKeytableColumnAliasesAsCSVForSelectFromJoinSQL += "`"  + mergeModel.getJoinedKeytableModel().getName() + "`.`key_column_" + keyJoinsAsCachedRowSet.getInt("column_number") + "` AS `" + keyJoinsAsCachedRowSet.getString("column_name") + "` ";
		        	
			        while (keyJoinsAsCachedRowSet.next()) {
			        
			        	columnDefinitionsUsingKeyColumnNamesSQL += ", `" + keyJoinsAsCachedRowSet.getString("column_name") + "` VARCHAR(255) ";
			        	
			        	datatable1JoinSQL += "AND `" + mergeModel.getDatatable1Model().getName() + "`.`" + datatable1ColumnNamesByColumnNumberAsHashMap.get(keyJoinsAsCachedRowSet.getInt("column_number")) + "` = `" + mergeModel.getJoinedKeytableModel().getName() + "`.`key_column_" + keyJoinsAsCachedRowSet.getInt("column_number") + "` ";
			        	datatable2JoinSQL += "AND `" + mergeModel.getDatatable2Model().getName() + "`.`" + datatable2ColumnNamesByColumnNumberAsHashMap.get(keyJoinsAsCachedRowSet.getInt("column_number")) + "` = `" + mergeModel.getJoinedKeytableModel().getName() + "`.`key_column_" + keyJoinsAsCachedRowSet.getInt("column_number") + "` AND `" + mergeModel.getDatatable2Model().getName() + "`.`" + datatable2ColumnNamesByColumnNumberAsHashMap.get(keyJoinsAsCachedRowSet.getInt("column_number")) + "` = `" + mergeModel.getDatatable1Model().getName() + "`.`" + datatable1ColumnNamesByColumnNumberAsHashMap.get(keyJoinsAsCachedRowSet.getInt("column_number")) + "` ";
			        	
			        	joinedKeytableColumnAliasesAsCSVForSelectFromJoinSQL += ", `"  + mergeModel.getJoinedKeytableModel().getName() + "`.`key_column_" + keyJoinsAsCachedRowSet.getInt("column_number") + "` AS `" + keyJoinsAsCachedRowSet.getString("column_name") + "` ";
			        }
			        
		        } else {
		        	this.logger.severe("Did not retrieve any key joins as a cached row set using merge Id: " + mergeModel.getId());
		        }
		        
		        

		        //TODO
		        String nonKeyCrossDatatableColumnAliasesAsCSVForSelectFromJoinSQL = "";	        
		        
		        String columnDefinitionsUsingNonKeyCrossDatatableColumnAndSourceNumbersSQL = "";

		        String conflictUnionsSQL = "";
		        
		        CachedRowSet nonKeyCrossDatatableJoinsAsCachedRowSet = joinsModel.retrieveNonKeyCrossDatatableJoinsAsCachedRowsetByMergeId(mergeModel.getId(), connection);

		        nonKeyCrossDatatableJoinsAsCachedRowSet.beforeFirst();
		        
		        if (nonKeyCrossDatatableJoinsAsCachedRowSet.next()) {
		        	
		        	columnDefinitionsUsingNonKeyCrossDatatableColumnAndSourceNumbersSQL = columnDefinitionsUsingNonKeyCrossDatatableColumnAndSourceNumbersSQL.concat("`column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("column_number") + "_source_1` VARCHAR(255), `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("column_number") + "_source_2` VARCHAR(255) ");
		        	
		        	nonKeyCrossDatatableColumnAliasesAsCSVForSelectFromJoinSQL = nonKeyCrossDatatableColumnAliasesAsCSVForSelectFromJoinSQL.concat("`" + mergeModel.getDatatable1Model().getName() + "`.`" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("datatable_1_column_name") + "` AS `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_1`, `" + mergeModel.getDatatable2Model().getName() + "`.`" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("datatable_2_column_name") + "` AS `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_2` ");

		        	//TODO: Alter this logic so the same strings aren't repeated
		        	conflictUnionsSQL += 	"(" +
		        							"SELECT " + mergeModel.getId() + " AS merge_id, joined_keytable_id, " + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + " AS column_number, 1 AS conflict_id FROM `tmp_joined_datatable_" + mergeModel.getId() + "` " +
		        							"WHERE `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_1` != `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_2` AND `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_1` IS NOT NULL AND `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_1` != '' AND `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_2` IS NOT NULL AND `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_2` != ''" +
		        							") ";
		        	
		        	conflictUnionsSQL += 	"UNION " +
		        							"(" +
											"SELECT " + mergeModel.getId() + " AS merge_id, joined_keytable_id, " + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + " AS column_number, 2 AS conflict_id FROM `tmp_joined_datatable_" + mergeModel.getId() + "` " +
											"WHERE `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_1` != `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_2` AND (`column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_1` IS NULL OR `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_1` = '') AND `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_2` IS NOT NULL AND `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_2` != ''" +
											") ";
		        	
		        	conflictUnionsSQL += 	"UNION " +
		        							"(" +
											"SELECT " + mergeModel.getId() + " AS merge_id, joined_keytable_id, " + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + " AS column_number, 3 AS conflict_id FROM `tmp_joined_datatable_" + mergeModel.getId() + "` " +
											"WHERE `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_1` != `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_2` AND (`column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_2` IS NULL OR `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_2` = '') AND `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_1` IS NOT NULL AND `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_1` != ''" +
											") ";
		        	
			        while (nonKeyCrossDatatableJoinsAsCachedRowSet.next()) {
			        
			        	columnDefinitionsUsingNonKeyCrossDatatableColumnAndSourceNumbersSQL += ", `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("column_number") + "_source_1` VARCHAR(255), `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("column_number") + "_source_2` VARCHAR(255) ";

			        	nonKeyCrossDatatableColumnAliasesAsCSVForSelectFromJoinSQL += ", `" + mergeModel.getDatatable1Model().getName() + "`.`" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("datatable_1_column_name") + "` AS `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_1`, `" + mergeModel.getDatatable2Model().getName() + "`.`" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("datatable_2_column_name") + "` AS `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_2` ";
			        
			        	conflictUnionsSQL += 	"UNION " +
									        	"(" +
												"SELECT " + mergeModel.getId() + " AS merge_id, joined_keytable_id, " + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + " AS column_number, 1 AS conflict_id FROM `tmp_joined_datatable_" + mergeModel.getId() + "` " +
												"WHERE `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_1` != `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_2` AND `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_1` IS NOT NULL AND `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_1` != '' AND `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_2` IS NOT NULL AND `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_2` != ''" +
												") ";
			        	
			        	conflictUnionsSQL += 	"UNION " +
												"(" +
												"SELECT " + mergeModel.getId() + " AS merge_id, joined_keytable_id, " + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + " AS column_number, 2 AS conflict_id FROM `tmp_joined_datatable_" + mergeModel.getId() + "` " +
												"WHERE `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_1` != `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_2` AND (`column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_1` IS NULL OR `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_1` = '') AND `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_2` IS NOT NULL AND `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_2` != ''" +
												") ";

						conflictUnionsSQL += 	"UNION " +
												"(" +
												"SELECT " + mergeModel.getId() + " AS merge_id, joined_keytable_id, " + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + " AS column_number, 3 AS conflict_id FROM `tmp_joined_datatable_" + mergeModel.getId() + "` " +
												"WHERE `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_1` != `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_2` AND (`column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_2` IS NULL OR `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_2` = '') AND `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_1` IS NOT NULL AND `column_" + nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number") + "_source_1` != ''" +
												") ";
									        	
			        }
			        
		        } else {
		        	this.logger.severe("Did not retrieve any non-key cross-datatable joins as a cached row set using merge Id: " + mergeModel.getId());
		        }	        
		        

		        
		        
		        

		        
				try {

						
						//FIXME: Change this to a TEMPORARY table after testing

						String dropTemporaryJoinedDatatableSQL = "DROP TABLE IF EXISTS `tmp_joined_datatable_" + mergeModel.getId() + "`;";

						this.logger.info(dropTemporaryJoinedDatatableSQL);
						
						PreparedStatement preparedStatement2 = connection.prepareStatement(dropTemporaryJoinedDatatableSQL);
						preparedStatement2.executeUpdate();
						preparedStatement2.close();				
					
					
						String createAndPopulateTemporaryJoinedDatatableSQL = "CREATE TABLE `tmp_joined_datatable_" + mergeModel.getId() + "` (joined_keytable_id BIGINT(255), " + columnDefinitionsUsingKeyColumnNamesSQL + ", " + columnDefinitionsUsingNonKeyCrossDatatableColumnAndSourceNumbersSQL + ", PRIMARY KEY (joined_keytable_id)) ENGINE=InnoDB " +
															        			"SELECT `" + mergeModel.getJoinedKeytableModel().getName() + "`.id AS joined_keytable_id, " + joinedKeytableColumnAliasesAsCSVForSelectFromJoinSQL + ", " + nonKeyCrossDatatableColumnAliasesAsCSVForSelectFromJoinSQL + 
															        			"FROM `" + mergeModel.getJoinedKeytableModel().getName() + "` " + 
															        			"JOIN `" + mergeModel.getDatatable1Model().getName() + "` ON " + datatable1JoinSQL + 
															        			"JOIN `" + mergeModel.getDatatable2Model().getName() + "` ON " + datatable2JoinSQL + 
															        			"ORDER BY `" + mergeModel.getJoinedKeytableModel().getName() + "`.id" +
															        			";";

						this.logger.info(createAndPopulateTemporaryJoinedDatatableSQL);
						
				        PreparedStatement preparedStatement3 = connection.prepareStatement(createAndPopulateTemporaryJoinedDatatableSQL);
				        preparedStatement3.executeUpdate();
				        preparedStatement3.close();

				        //TODO
						String insertConflictsByCellSQL = "" +
								"INSERT INTO resolution (merge_id, joined_keytable_id, column_number, conflict_id) " +
									conflictUnionsSQL +
									" ORDER BY joined_keytable_id, column_number, conflict_id " + 
								";";

						this.logger.info(insertConflictsByCellSQL);
						
						PreparedStatement preparedStatement4 = connection.prepareStatement(insertConflictsByCellSQL);
						preparedStatement4.executeUpdate();
						preparedStatement4.close();				
					
					
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
			
			//////////////////////////////////////////////
			this.logger.info("Done determining conflicts (generally).");
			
			
			
			
			
			
			//FIXME
			//TODO: refactor this to use the general resolutions table.
			
			
			//By Column

			//Already got mergeModel.getDatatable1Model().getKeyColumnNamesAsStringList(), but also need the joined column_name

//
//			this.logger.info("about to determine conflicts by column");
//
//			
//			//Note: This doesn't update the merge's total conflicts count. See separate script call below. 
//			mergeModel = this.retrieveMergeAsMergeModelThroughDeterminingProblemsByColumnUsingMergeModel(mergeModel, connection);
//
//			this.logger.info("done determining conflicts by column");
//
//			
//			// Get an up-to-date count of the total conflicts.
//			mergeModel = this.retrieveMergeAsMergeModelThroughDeterminingTotalConflictsCountByColumnUsingMergeModel(mergeModel, connection);
//			
//
//			// If there were no conflicts, no need to look for them again.
//			if (mergeModel.getTotalConflictsCount() > 0) {
//							
//				
//				//TODO: By Row
//				this.logger.info("about to determine conflicts by row");
//				
//				// Create a joined_keytable for the merge data
//				MergesModel mergesModel = new MergesModel();
//				
//				mergeModel = mergesModel.retrieveMergeAsMergeModelThroughRecreatingJoinedKeytableUsingMergeModel(mergeModel, connection);
//				
//				JoinsModel joinsModel = new JoinsModel();
//				mergeModel.getJoinsModel().setKeyJoinsAsCachedRowSet(joinsModel.retrieveKeyJoinsAsCachedRowsetByMergeId(mergeModel.getId(),connection));
//				mergeModel.getJoinsModel().setNonKeyCrossDatatableJoinsAsCachedRowSet(joinsModel.retrieveNonKeyCrossDatatableJoinsAsCachedRowsetByMergeId(mergeModel.getId(), connection));
//
//				
//				
//				//TODO:
//				mergeModel = this.retrieveMergeAsMergeModelThroughDeterminingProblemsByRowUsingMergeModel(mergeModel, connection);
//				
//				
//				// Get an up-to-date count of the total conflicts.
//				mergeModel = this.retrieveMergeAsMergeModelThroughDeterminingTotalConflictsCountByRowUsingMergeModel(mergeModel, connection);
//				
//				
//				this.logger.info("done determining conflicts by row");
//				
//				
//				this.logger.info("about to determine conflicts by cell");
//				
//				//TODO
//				//mergeModel = this.retrieveMergeAsMergeModelThroughDeterminingProblemsByCellUsingMergeModel(mergeModel, connection);
//				
//				this.logger.info("done determining conflicts by cell");
//				
//			} else {
//				
//				this.logger.info("Skipped determining conflicts by row because totalConflictsCount = " + mergeModel.getTotalConflictsCount());
//			}
			
			
			
			

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
	          MergesModel mergesModel = new MergesModel();
	          mergesModel.updateTotalConflictsCountUsingMergeModel(mergeModel, connection);
	          

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        }
	        
	        // Return the model with the new info
	        return mergeModel;
		
		
	}






	public MergeModel retrieveMergeAsMergeModelThroughDeterminingProblemsByCellUsingMergeModel(
			MergeModel mergeModel, Connection connection) {


		
		
		
		
		
		return mergeModel;
		
	}








	public MergeModel retrieveMergeAsMergeModelThroughDeterminingTotalConflictsCountByCellUsingMergeModel(
			MergeModel mergeModel, Connection connection) {
		// TODO Auto-generated method stub
		return null;		
	
		
	}	
	
}
