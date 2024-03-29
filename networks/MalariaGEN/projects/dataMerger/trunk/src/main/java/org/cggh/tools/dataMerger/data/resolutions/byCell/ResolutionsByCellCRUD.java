package org.cggh.tools.dataMerger.data.resolutions.byCell;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.databases.DatabaseModel;
import org.cggh.tools.dataMerger.data.joins.JoinsCRUD;
import org.cggh.tools.dataMerger.data.merges.MergeModel;
import org.cggh.tools.dataMerger.data.resolutions.ResolutionsCRUD;
import org.cggh.tools.dataMerger.functions.data.resolutions.byCell.ResolutionsByCellFunctions;
import org.cggh.tools.dataMerger.scripts.data.merges.MergeScripts;
import org.json.JSONArray;
import org.json.JSONObject;


public class ResolutionsByCellCRUD implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -145618744750618078L;

	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.resolutionsByCell");

	private DatabaseModel databaseModel;
	
	public ResolutionsByCellCRUD() {

		this.setDatabaseModel(new DatabaseModel());
		
	}

    public void setDatabaseModel (final DatabaseModel databaseModel) {
        this.databaseModel  = databaseModel;
    }
    public DatabaseModel getDatabaseModel () {
        return this.databaseModel;
    } 

	public String retrieveResolutionsByCellAsDecoratedXHTMLTableUsingMergeModel (MergeModel mergeModel) {
		
		String resolutionsByCellAsDecoratedXHTMLTable = null;
		
		  CachedRowSet resolutionsByCellAsCachedRowSet = this.retrieveResolutionsByCellAsCachedRowSetUsingMergeModel(mergeModel);

		  if (resolutionsByCellAsCachedRowSet != null) {
			  

			  	ResolutionsByCellFunctions resolutionsByCellFunctions = new ResolutionsByCellFunctions();
			  	resolutionsByCellFunctions.setResolutionsByCellAsCachedRowSet(resolutionsByCellAsCachedRowSet);
			  	resolutionsByCellFunctions.setSolutionsByCellAsCachedRowSet(this.retrieveSolutionsByCellAsCachedRowSet());

			  	JoinsCRUD joinsModel = new JoinsCRUD();
			  	joinsModel.setDatabaseModel(this.getDatabaseModel());
			  	resolutionsByCellFunctions.setJoinColumnNamesByColumnNumberAsHashMap(joinsModel.retrieveJoinColumnNamesByColumnNumberAsHashMapUsingMergeModel(mergeModel));
			  	
			  	ResolutionsCRUD resolutionsModel = new ResolutionsCRUD();
			  	resolutionsModel.setDatabaseModel(this.getDatabaseModel());
			  	resolutionsByCellFunctions.setUnresolvedByColumnOrRowConflictsCountUsingColumnNumberAsHashMap(resolutionsModel.retrieveUnresolvedByColumnOrRowConflictsCountUsingColumnNumberAsHashMapUsingMergeModel(mergeModel));
			  	resolutionsByCellFunctions.setUnresolvedByColumnOrRowStatusUsingCellCoordsAsHashMap(resolutionsModel.retrieveUnresolvedByColumnOrRowStatusUsingCellCoordsAsHashMapUsingMergeModel(mergeModel));

			  	
			  	resolutionsByCellFunctions.setSolutionByColumnIdUsingCellCoordsAsHashMap(resolutionsModel.retrieveSolutionByColumnIdUsingCellCoordsAsHashMapUsingMergeModel(mergeModel));
			  	resolutionsByCellFunctions.setSolutionByRowIdUsingCellCoordsAsHashMap(resolutionsModel.retrieveSolutionByRowIdUsingCellCoordsAsHashMapUsingMergeModel(mergeModel));
			  	resolutionsByCellFunctions.setSolutionByCellIdUsingCellCoordsAsHashMap(resolutionsModel.retrieveSolutionByCellIdUsingCellCoordsAsHashMapUsingMergeModel(mergeModel));			  	
			  	
			  	resolutionsByCellFunctions.setConstantUsingCellCoordsAsHashMap(resolutionsModel.retrieveConstantUsingCellCoordsAsHashMapUsingMergeModel(mergeModel));
			  	resolutionsByCellFunctions.setNullOrConstantSolutionUsingColumnNumberAsHashMap(resolutionsModel.retrieveNullOrConstantSolutionUsingColumnNumberAsHashMapUsingMergeModel(mergeModel));
					  	
			  	
			  	resolutionsByCellFunctions.setResolutionsByCellAsDecoratedXHTMLTableUsingResolutionsByCellAsCachedRowSet();
			  	resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellFunctions.getResolutionsByCellAsDecoratedXHTMLTable();
			    
		  } else {
			  
			  //TODO: Error handling
			  this.logger.warning("Error: resolutionsByCellAsCachedRowSet is null");
			  resolutionsByCellAsDecoratedXHTMLTable = "<p>Error: resolutionsByCellAsCachedRowSet is null</p>";
			  
		  }
		
		return resolutionsByCellAsDecoratedXHTMLTable;
	}

	public CachedRowSet retrieveSolutionsByCellAsCachedRowSet() {
		
		CachedRowSet solutionsByCellAsCachedRowSet = null;

		   String CACHED_ROW_SET_IMPL_CLASS = "com.sun.rowset.CachedRowSetImpl";
		   
			try {

				Connection connection = this.getDatabaseModel().getNewConnection();
				 
				if (connection != null) {

					
				      try{
				          PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from solution_by_cell;");
				          preparedStatement.executeQuery();
				          Class<?> cachedRowSetImplClass = Class.forName(CACHED_ROW_SET_IMPL_CLASS);
				          solutionsByCellAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
				          solutionsByCellAsCachedRowSet.populate(preparedStatement.getResultSet());
				          preparedStatement.close();
	
				        }
				        catch(SQLException sqlException){
				        	this.logger.severe(sqlException.toString());
					    	sqlException.printStackTrace();
				        } finally {
							
							try {
								connection.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							
						} 	
					
				} else {
					this.logger.severe("connection.isClosed");
				}
					
			} 
			catch (Exception e) {
				this.logger.severe(e.toString());
				e.printStackTrace();
			}
		
		return solutionsByCellAsCachedRowSet;
	}

	public CachedRowSet retrieveResolutionsByCellAsCachedRowSetUsingMergeModel(
			MergeModel mergeModel) {
		
		CachedRowSet resolutionsByCellAsCachedRowSet = null;
		

		
		   String CACHED_ROW_SET_IMPL_CLASS = "com.sun.rowset.CachedRowSetImpl";
		   
			try {

				Connection connection = this.getDatabaseModel().getNewConnection();
				 
				if (connection != null) {

					
					
					StringBuffer keyColumnReferencesAsCSVForSelectSQL = new StringBuffer();


					mergeModel.getJoinedKeytableModel().getDataAsCachedRowSet().beforeFirst();
					
					if (mergeModel.getJoinedKeytableModel().getDataAsCachedRowSet().next()) {
						
						//this.logger.info("Got joined keytable data as cached row set.");
						

					    for (int i = 1; i <= mergeModel.getJoinedKeytableModel().getDataAsCachedRowSet().getMetaData().getColumnCount(); i++) {
					    	
					    	keyColumnReferencesAsCSVForSelectSQL.append(", ").append("`").append(mergeModel.getJoinedKeytableModel().getName()).append("`.`").append(mergeModel.getJoinedKeytableModel().getDataAsCachedRowSet().getMetaData().getColumnName(i)).append("`");
					    }
						
						
					} else {
						//This is not necessarily a problem if there are no common keys.
						//this.logger.severe("Did not get joined keytable data as cached row set.");
					}
					
					
					StringBuffer nonKeyCrossDatatableColumnNameAliasesForSelectSQL = new StringBuffer();
					
					mergeModel.getJoinsModel().getNonKeyCrossDatatableJoinsAsCachedRowSet().beforeFirst();
					
					if (mergeModel.getJoinsModel().getNonKeyCrossDatatableJoinsAsCachedRowSet().next()) {
						
						//this.logger.info("Got non-key cross-datatable joins data as cached row set.");
						
						mergeModel.getJoinsModel().getNonKeyCrossDatatableJoinsAsCachedRowSet().beforeFirst();
						
						while (mergeModel.getJoinsModel().getNonKeyCrossDatatableJoinsAsCachedRowSet().next()) {
							
							nonKeyCrossDatatableColumnNameAliasesForSelectSQL.append(", `").append(mergeModel.getDatatable1Model().getName()).append("`.`").append(mergeModel.getJoinsModel().getNonKeyCrossDatatableJoinsAsCachedRowSet().getString("datatable_1_column_name")).append("` AS `column_").append(mergeModel.getJoinsModel().getNonKeyCrossDatatableJoinsAsCachedRowSet().getString("column_number")).append("_source_1`, `").append(mergeModel.getDatatable2Model().getName()).append("`.`").append(mergeModel.getJoinsModel().getNonKeyCrossDatatableJoinsAsCachedRowSet().getString("datatable_2_column_name")).append("` AS `column_").append(mergeModel.getJoinsModel().getNonKeyCrossDatatableJoinsAsCachedRowSet().getString("column_number")).append("_source_2` ");
						}
						
					} else {
						//NOTE: not an error
						//this.logger.severe("Did not get non-key cross-datatable joins data as cached row set.");
					}
					
					
					StringBuffer datatable1JoinConditionSQL = new StringBuffer();
					StringBuffer datatable2JoinConditionSQL = new StringBuffer();	
		
					mergeModel.getJoinsModel().getKeyJoinsAsCachedRowSet().beforeFirst();
					
					if (mergeModel.getJoinsModel().getKeyJoinsAsCachedRowSet().next()) {
						
						//this.logger.info("Got key joins data as cached row set.");
						
						mergeModel.getJoinsModel().getKeyJoinsAsCachedRowSet().first();
						
						datatable1JoinConditionSQL.append("`").append(mergeModel.getDatatable1Model().getName()).append("`.`").append(mergeModel.getJoinsModel().getKeyJoinsAsCachedRowSet().getString("datatable_1_column_name") + "` = `").append(mergeModel.getJoinedKeytableModel().getName()).append("`.`key_column_").append(mergeModel.getJoinsModel().getKeyJoinsAsCachedRowSet().getString("column_number")).append("` ");
						datatable2JoinConditionSQL.append("`").append(mergeModel.getDatatable2Model().getName()).append("`.`").append(mergeModel.getJoinsModel().getKeyJoinsAsCachedRowSet().getString("datatable_2_column_name") + "` = `").append(mergeModel.getJoinedKeytableModel().getName()).append("`.`key_column_").append(mergeModel.getJoinsModel().getKeyJoinsAsCachedRowSet().getString("column_number")).append("` ");
						
						while (mergeModel.getJoinsModel().getKeyJoinsAsCachedRowSet().next()) {
							
							datatable1JoinConditionSQL.append("AND `").append(mergeModel.getDatatable1Model().getName()).append("`.`").append(mergeModel.getJoinsModel().getKeyJoinsAsCachedRowSet().getString("datatable_1_column_name")).append("` = `").append(mergeModel.getJoinedKeytableModel().getName()).append("`.`key_column_").append(mergeModel.getJoinsModel().getKeyJoinsAsCachedRowSet().getString("column_number")).append("` ");
							datatable2JoinConditionSQL.append("AND `").append(mergeModel.getDatatable2Model().getName()).append("`.`").append(mergeModel.getJoinsModel().getKeyJoinsAsCachedRowSet().getString("datatable_2_column_name")).append("` = `").append(mergeModel.getJoinedKeytableModel().getName()).append("`.`key_column_").append(mergeModel.getJoinsModel().getKeyJoinsAsCachedRowSet().getString("column_number")).append("` ");
						}
						
					} else {
						this.logger.severe("Did not get key joins data as cached row set.");
					}		
					
					
				      try{
				    	  
				    	  String selectResolutionsByCellSQL = "SELECT resolution.merge_id, resolution.joined_keytable_id, resolution.solution_by_cell_id, resolution.constant " + 
											        		  keyColumnReferencesAsCSVForSelectSQL.toString() + 
											        		  nonKeyCrossDatatableColumnNameAliasesForSelectSQL.toString() +  
											        		  "FROM resolution " +
											        		  "JOIN `" + mergeModel.getJoinedKeytableModel().getName() + "` ON `" + mergeModel.getJoinedKeytableModel().getName() + "`.id = resolution.joined_keytable_id " +
											        		  "JOIN `" + mergeModel.getDatatable1Model().getName() + "` ON " + datatable1JoinConditionSQL.toString() + 
											        		  "JOIN `" + mergeModel.getDatatable2Model().getName() + "` ON " + datatable2JoinConditionSQL.toString() + 
											        		  "WHERE resolution.merge_id = ? AND resolution.solution_by_column_id IS NULL AND resolution.solution_by_row_id IS NULL " +
											        		  "GROUP BY resolution.joined_keytable_id " +
											        		  "ORDER BY resolution.joined_keytable_id " +
											        		  ";";
				    	  
				    	  //this.logger.info("selectResolutionsByCellSQL = " + selectResolutionsByCellSQL);
				    	  
				          PreparedStatement preparedStatement = connection.prepareStatement(selectResolutionsByCellSQL);
				          preparedStatement.setInt(1, mergeModel.getId());
				          
				          preparedStatement.executeQuery();
				          Class<?> cachedRowSetImplClass = Class.forName(CACHED_ROW_SET_IMPL_CLASS);
				          resolutionsByCellAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
				          resolutionsByCellAsCachedRowSet.populate(preparedStatement.getResultSet());
				          preparedStatement.close();
	
				        }
				        catch(SQLException sqlException){
				        	this.logger.severe(sqlException.toString());
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
				//System.out.println("Exception from getMergesAsCachedRowSet.");
				e.printStackTrace();
			}
	
	
	
	     return resolutionsByCellAsCachedRowSet;
	}

	public void updateResolutionsByCellByMergeIdUsingResolutionsByCellAsJSONObject(
			Integer mergeId, JSONObject resolutionsByCellAsJsonObject) {
		
		//Note: This also updates the merge's total conflict count.
		
		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);
		

			
			Connection connection = this.getDatabaseModel().getNewConnection();
			 
			if (connection != null) {

				try {				
				
				
				JSONArray keys = resolutionsByCellAsJsonObject.names();
				
				 Pattern solutionByCellIdKeyPattern = Pattern.compile("^solution_by_cell_id-(\\d+)-(\\d+)$");
				 
					 
				 
				
				for(int i = 0; i < keys.length(); i++) {
					
					////this.logger.info("key " + keys.getString(i) + " = " + resolutionsByCellAsJsonObject.get(keys.getString(i)));
					
					Matcher solutionByCellIdKeyPatternMatcher = solutionByCellIdKeyPattern.matcher(keys.getString(i));
					
					 if (solutionByCellIdKeyPatternMatcher.find()) {
						 
						 
						 ResolutionByCellModel resolutionByCellModel = new ResolutionByCellModel();
						 
						
			        	  resolutionByCellModel.setMergeModel(mergeModel);
			        	  
			        	  ////this.logger.info("joinedKeytableID: " + solutionByCellIdKeyPatternMatcher.group(1));
			        	  resolutionByCellModel.getJoinedKeytableModel().setId(Integer.parseInt(solutionByCellIdKeyPatternMatcher.group(1)));
			        	  
			        	  ////this.logger.info("columnNumber: " + solutionByCellIdKeyPatternMatcher.group(2));
			        	  resolutionByCellModel.getJoinModel().setColumnNumber(Integer.parseInt(solutionByCellIdKeyPatternMatcher.group(2)));

			        	//Note: Merge's total_conflicts_count will only count conflicts that have no solution.

			        	  if (resolutionsByCellAsJsonObject.optInt(keys.getString(i)) != 0) {
			        		  
			        		  //this.logger.info("solutionByCellId: " + resolutionsByCellAsJsonObject.getInt(keys.getString(i)));
			        		  resolutionByCellModel.getSolutionByCellModel().setId(resolutionsByCellAsJsonObject.getInt(keys.getString(i)));
			        	  } else {
			        		  resolutionByCellModel.getSolutionByCellModel().setId(null);
			        	  }
			        	  
			        	  
			        	  
			        	  if (resolutionsByCellAsJsonObject.has("constant-" + resolutionByCellModel.getJoinedKeytableModel().getId() + "-" + resolutionByCellModel.getJoinModel().getColumnNumber())) {
			        	  
			        		  //this.logger.info("solutionByCellId: " + resolutionsByCellAsJsonObject.getString("constant-" + resolutionByCellModel.getJoinedKeytableModel().getId() + "-" + resolutionByCellModel.getJoinModel().getColumnNumber()));
			        		  resolutionByCellModel.setConstant(resolutionsByCellAsJsonObject.getString("constant-" + resolutionByCellModel.getJoinedKeytableModel().getId() + "-" + resolutionByCellModel.getJoinModel().getColumnNumber()));
		        		  
			        	  } else {
			        		  
			        		  resolutionByCellModel.setConstant(null);
			        	  }
			        	  
			        	  this.updateResolutionByCellUsingResolutionByCellModel(resolutionByCellModel, connection);
			        	  
			        	  
					 }
					
					
				}
				
	        	  // Recount the conflicts

	        	  MergeScripts mergeScripts = new MergeScripts();
	        	  mergeModel = mergeScripts.retrieveMergeAsMergeModelThroughDeterminingTotalConflictsCountUsingMergeModel(mergeModel, connection);
	        	  
				} 
				catch (Exception e) {
					
					this.logger.severe(e.toString());
					e.printStackTrace();
				} finally {
						
						try {
							connection.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						
					}
				
			} else {
				
				this.logger.severe("connection.isClosed");
			}
				

	}

	public void updateResolutionByCellUsingResolutionByCellModel(
			ResolutionByCellModel resolutionByCellModel, Connection connection) {
		
	      try {

	          PreparedStatement preparedStatement = connection.prepareStatement(
	        		  "UPDATE resolution SET solution_by_cell_id = ?, constant = ? " +
	        		  "WHERE merge_id = ? AND joined_keytable_id = ? AND column_number = ? AND solution_by_column_id IS NULL AND solution_by_row_id IS NULL " +
	        		  ";");
	          
	          
	          if (resolutionByCellModel.getSolutionByCellModel().getId() != null) {
	        	  preparedStatement.setInt(1, resolutionByCellModel.getSolutionByCellModel().getId());
	        	  ////this.logger.info("1 solution_by_cell_id: " + resolutionByCellModel.getSolutionByCellModel().getId());
	          } else {
	        	  preparedStatement.setNull(1, java.sql.Types.INTEGER);
	          }
	          
	          //FIXME: What is 4? ("Use CONSTANT")
	          if (resolutionByCellModel.getSolutionByCellModel().getId() != null && resolutionByCellModel.getSolutionByCellModel().getId() == 4) {
	        	  
		          if (resolutionByCellModel.getConstant() != null) {
		        	  
		        	  //Allow blank string constants (for future-proofing against null/blank string distinctions, especially in export)
		        	  preparedStatement.setString(2, resolutionByCellModel.getConstant());
		        	  
		        	  ////this.logger.info("2 constant: " + resolutionByCellModel.getConstant());
		        	  
		          } else {
		        	  preparedStatement.setNull(2, java.sql.Types.VARCHAR);
		          }
		          
	          } else {
	        	  preparedStatement.setNull(2, java.sql.Types.VARCHAR);
	          }
	          
	          ////this.logger.info("3 mergeId: " + resolutionByCellModel.getMergeModel().getId());
	          ////this.logger.info("4 joinedKeytableId: " + resolutionByCellModel.getJoinedKeytableModel().getId());
	          ////this.logger.info("5 constant: " + resolutionByCellModel.getJoinModel().getColumnNumber());
	          
	          preparedStatement.setInt(3, resolutionByCellModel.getMergeModel().getId());
	          preparedStatement.setInt(4, resolutionByCellModel.getJoinedKeytableModel().getId());
	          preparedStatement.setInt(5, resolutionByCellModel.getJoinModel().getColumnNumber());
	          
	          preparedStatement.executeUpdate();
	          preparedStatement.close();
	          
	
	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 	
	        
	}
    
}
