package org.cggh.tools.dataMerger.scripts.merges;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.merges.MergeModel;
import org.cggh.tools.dataMerger.data.merges.MergesModel;
import org.cggh.tools.dataMerger.data.resolutionsByColumn.ProblemByColumnModel;
import org.cggh.tools.dataMerger.data.resolutionsByColumn.ResolutionByColumnModel;
import org.cggh.tools.dataMerger.data.resolutionsByColumn.ResolutionsByColumnModel;



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
	        		  
	        		  mergeModel.getDatatable1Model().setDuplicateKeysCount(0);
	        	  }
	        	  
	      	  } else {
	      		  
	      		  mergeModel.getDatatable1Model().setDuplicateKeysCount(0);
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
	        		  
	        		  mergeModel.getDatatable2Model().setDuplicateKeysCount(0);
	        	  }
	        	  
	      	  } else {
	      		  
	      		  mergeModel.getDatatable2Model().setDuplicateKeysCount(0);
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




	public MergeModel retrieveMergeAsMergeModelThroughDeterminingConflictsCountByColumnUsingMergeModel(
			MergeModel mergeModel, Connection connection) {
		
		
  
		CachedRowSet keyCrossDatatableJoinsAsCachedRowSet = mergeModel.getJoinsModel().getKeyCrossDatatableJoinsAsCachedRowSet();
		CachedRowSet nonKeyCrossDatatableJoinsAsCachedRowSet = mergeModel.getJoinsModel().getNonKeyCrossDatatableJoinsAsCachedRowSet();
		
		String joinSQL = "";
		try {
			if (keyCrossDatatableJoinsAsCachedRowSet.next()) {
				
				//because next() skips the first row.
				keyCrossDatatableJoinsAsCachedRowSet.first();
				
				joinSQL = joinSQL.concat("`" + mergeModel.getDatatable1Model().getName() + "`.`" + keyCrossDatatableJoinsAsCachedRowSet.getString("datatable_1_column_name") + "` = `" + mergeModel.getDatatable2Model().getName() + "`.`" + keyCrossDatatableJoinsAsCachedRowSet.getString("datatable_2_column_name") + "` ");
				
				while (keyCrossDatatableJoinsAsCachedRowSet.next()) {
					
					joinSQL = joinSQL.concat("AND `" + mergeModel.getDatatable1Model().getName() + "`.`" + keyCrossDatatableJoinsAsCachedRowSet.getString("datatable_1_column_name") + "` = `" + mergeModel.getDatatable2Model().getName() + "`.`" + keyCrossDatatableJoinsAsCachedRowSet.getString("datatable_2_column_name") + "` ");
					
					
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//For each nonKeyCrossJoin

		//TODO
		//SELECT * FROM datatable_1
		  //JOIN datatable_2 ON datatable_2.key_column_A = datatable_1.key_column_B AND datatable_2.key_column_X = datatable_1.key_column_Y
		  
		  //Problems can be one of problem_by_column:
		  //conflicting values								WHERE datatable_1.column_x != datatable_2.column_y AND datatable_1.column_x IS NOT NULL AND datatable_2.column_y IS NOT NULL
		  //NULLs in source 1 versus values in source 2		WHERE datatable_1.column_x != datatable_2.column_y AND datatable_1.column_x IS NULL
		  //NULLs in source 2 versus values in source 1		WHERE datatable_1.column_x != datatable_2.column_y AND datatable_2.column_y IS NULL
				
		//NOTE: NULL and blank strings are synonymous here, since imports are from text files using blank strings in place of nulls
		//However, to future-proof in case NULLs appear in the text files, NULLs and '' are treated equally here.
		
		try {
			if (nonKeyCrossDatatableJoinsAsCachedRowSet.next()) {
				
				//because next() skips the first row.
				nonKeyCrossDatatableJoinsAsCachedRowSet.first();
				
				ResolutionsByColumnModel resolutionsByColumnModel = new ResolutionsByColumnModel();
				
				while (nonKeyCrossDatatableJoinsAsCachedRowSet.next()) {
				
			      try{
			    	  //conflicting values
			    	  
			    	  ProblemByColumnModel problemByColumnModel = new ProblemByColumnModel();
			    	  problemByColumnModel.setId(1);
			    	  
			    	  
			    	  PreparedStatement preparedStatement = connection.prepareStatement(
			    			  "SELECT COUNT(*) AS columnValueConflictsCount FROM `" + mergeModel.getDatatable1Model().getName() + "` " + 
			    			  "JOIN `" + mergeModel.getDatatable2Model().getName() + "` ON " + 
			    			  joinSQL +
			    			  "WHERE `" + mergeModel.getDatatable1Model().getName() + "`.`" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("datatable_1_column_name") + "` != " + "`" + mergeModel.getDatatable2Model().getName() + "`.`" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("datatable_2_column_name") + "` " + 
			    			  "AND `" + mergeModel.getDatatable1Model().getName() + "`.`" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("datatable_1_column_name") + "` IS NOT NULL AND `" + mergeModel.getDatatable2Model().getName() + "`.`" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("datatable_2_column_name") + "` IS NOT NULL " +
			    			  "AND `" + mergeModel.getDatatable1Model().getName() + "`.`" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("datatable_1_column_name") + "` != '' AND `" + mergeModel.getDatatable2Model().getName() + "`.`" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("datatable_2_column_name") + "`!= '' " +
			    			  ";"
			    	  );
			          preparedStatement.executeQuery();
			          ResultSet resultSet = preparedStatement.getResultSet();
			          
			          if (resultSet.next()) {
			        	  
			        	  resultSet.first();
		
			        	  //TODO
			        	  Integer columnValueConflictsCount = resultSet.getInt("columnValueConflictsCount");
			        	  
			        	  
			        	  if (columnValueConflictsCount != null) {
			        		
			        		  if (columnValueConflictsCount > 0) {
			        		  
				        		  ResolutionByColumnModel resolutionByColumnModel = new ResolutionByColumnModel();
				        		  
				        		  resolutionByColumnModel.setMergeModel(mergeModel);
				        		  resolutionByColumnModel.setColumnNumber(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number"));
				        		  resolutionByColumnModel.setProblemByColumnModel(problemByColumnModel);
				        		  resolutionByColumnModel.setConflictsCount(columnValueConflictsCount);
				        		  
				        		  resolutionsByColumnModel.createResolutionByColumnUsingResolutionByColumnModel(resolutionByColumnModel, connection);
				        		  
			        		  } else {
			        			  
			        			  //Do nothing
			        		  }
			        		  
			        	  } else {
			        		  
			        		  //TODO:
			        		  this.logger.warning("Unexpected: columnValueConflictsCount == null.");
			        	  }
			        	  
			      	  } else {
			      		  
			      		//TODO
			      	  }
			          
			          resultSet.close();
			          preparedStatement.close();
			         
		
			        }
			        catch(SQLException sqlException){
				    	sqlException.printStackTrace();
			        }
			        
			        //NULLs in source 1 versus values in source 2		WHERE datatable_1.column_x != datatable_2.column_y AND datatable_1.column_x IS NULL
				      try{
				    	  ProblemByColumnModel problemByColumnModel = new ProblemByColumnModel();
				    	  problemByColumnModel.setId(2);
				    	  
				    	  
				    	  PreparedStatement preparedStatement = connection.prepareStatement(
				    			  "SELECT COUNT(*) AS columnValueConflictsCount FROM `" + mergeModel.getDatatable1Model().getName() + "` " + 
				    			  "JOIN `" + mergeModel.getDatatable2Model().getName() + "` ON " + 
				    			  joinSQL +
				    			  "WHERE `" + mergeModel.getDatatable1Model().getName() + "`.`" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("datatable_1_column_name") + "` != " + "`" + mergeModel.getDatatable2Model().getName() + "`.`" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("datatable_2_column_name") + "` " + 
				    			  "AND (`" + mergeModel.getDatatable1Model().getName() + "`.`" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("datatable_1_column_name") + "` IS NULL OR `" + mergeModel.getDatatable1Model().getName() + "`.`" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("datatable_1_column_name") + "` = '') " +
				    			  ";"
				    	  );
				          preparedStatement.executeQuery();
				          ResultSet resultSet = preparedStatement.getResultSet();
				          
				          if (resultSet.next()) {
				        	  
				        	  resultSet.first();
			
				        	  //TODO
				        	  Integer columnValueConflictsCount = resultSet.getInt("columnValueConflictsCount");
				        	  
				        	  
				        	  if (columnValueConflictsCount != null && columnValueConflictsCount > 0) {
				        		  
				        		  ResolutionByColumnModel resolutionByColumnModel = new ResolutionByColumnModel();
				        		  
				        		  resolutionByColumnModel.setMergeModel(mergeModel);
				        		  resolutionByColumnModel.setColumnNumber(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number"));
				        		  resolutionByColumnModel.setProblemByColumnModel(problemByColumnModel);
				        		  resolutionByColumnModel.setConflictsCount(columnValueConflictsCount);
				        		  
				        		  resolutionsByColumnModel.createResolutionByColumnUsingResolutionByColumnModel(resolutionByColumnModel, connection);
				        		  
				        	  } else {
				        		  
				        		  //TODO:
				        		  this.logger.warning("Unexpected: columnValueConflictsCount == null.");
				        	  }
				        	  
				      	  } else {
				      		  
				      		//TODO
				      	  }
				          
				          resultSet.close();
				          preparedStatement.close();
				         
			
				        }
				        catch(SQLException sqlException){
					    	sqlException.printStackTrace();
				        }
			        
			        
			        //NULLs in source 2 versus values in source 1		WHERE datatable_1.column_x != datatable_2.column_y AND datatable_2.column_y IS NULL
					      try{
					    	  ProblemByColumnModel problemByColumnModel = new ProblemByColumnModel();
					    	  problemByColumnModel.setId(3);
					    	  
					    	  
					    	  PreparedStatement preparedStatement = connection.prepareStatement(
					    			  "SELECT COUNT(*) AS columnValueConflictsCount FROM `" + mergeModel.getDatatable1Model().getName() + "` " + 
					    			  "JOIN `" + mergeModel.getDatatable2Model().getName() + "` ON " + 
					    			  joinSQL +
					    			  "WHERE `" + mergeModel.getDatatable1Model().getName() + "`.`" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("datatable_1_column_name") + "` != " + "`" + mergeModel.getDatatable2Model().getName() + "`.`" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("datatable_2_column_name") + "` " + 
					    			  "AND (`" + mergeModel.getDatatable2Model().getName() + "`.`" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("datatable_2_column_name") + "` IS NULL OR `" + mergeModel.getDatatable2Model().getName() + "`.`" + nonKeyCrossDatatableJoinsAsCachedRowSet.getString("datatable_2_column_name") + "` = '') " +
					    			  ";"
					    	  );
					          preparedStatement.executeQuery();
					          ResultSet resultSet = preparedStatement.getResultSet();
					          
					          if (resultSet.next()) {
					        	  
					        	  resultSet.first();
				
					        	  //TODO
					        	  Integer columnValueConflictsCount = resultSet.getInt("columnValueConflictsCount");
					        	  
					        	  
					        	  if (columnValueConflictsCount != null && columnValueConflictsCount > 0) {
					        		  
					        		  ResolutionByColumnModel resolutionByColumnModel = new ResolutionByColumnModel();
					        		  
					        		  resolutionByColumnModel.setMergeModel(mergeModel);
					        		  resolutionByColumnModel.setColumnNumber(nonKeyCrossDatatableJoinsAsCachedRowSet.getInt("column_number"));
					        		  resolutionByColumnModel.setProblemByColumnModel(problemByColumnModel);
					        		  resolutionByColumnModel.setConflictsCount(columnValueConflictsCount);
					        		  
					        		  resolutionsByColumnModel.createResolutionByColumnUsingResolutionByColumnModel(resolutionByColumnModel, connection);
					        		  
					        	  } else {
					        		  
					        		  //TODO:
					        		  this.logger.warning("Unexpected: columnValueConflictsCount == null.");
					        	  }
					        	  
					      	  } else {
					      		  
					      		//TODO
					      	  }
					          
					          resultSet.close();
					          preparedStatement.close();
					         
				
					        }
					        catch(SQLException sqlException){
						    	sqlException.printStackTrace();
					        }
						
			        
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	        
		return mergeModel;
	}

	
}
