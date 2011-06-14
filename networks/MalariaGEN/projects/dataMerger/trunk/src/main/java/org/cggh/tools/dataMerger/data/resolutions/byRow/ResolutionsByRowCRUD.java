package org.cggh.tools.dataMerger.data.resolutions.byRow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.databases.DatabaseModel;
import org.cggh.tools.dataMerger.data.joins.JoinsCRUD;
import org.cggh.tools.dataMerger.data.merges.MergeModel;
import org.cggh.tools.dataMerger.data.resolutions.ResolutionsCRUD;
import org.cggh.tools.dataMerger.functions.data.resolutions.byRow.ResolutionsByRowFunctions;
import org.cggh.tools.dataMerger.scripts.data.merges.MergeScripts;
import org.json.JSONArray;
import org.json.JSONObject;


public class ResolutionsByRowCRUD implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1573490419082418709L;

	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.resolutionsByRow");

	private DatabaseModel databaseModel;
	
	public ResolutionsByRowCRUD() {

		this.setDatabaseModel(new DatabaseModel());
		
	}

    public void setDatabaseModel (final DatabaseModel databaseModel) {
        this.databaseModel  = databaseModel;
    }
    public DatabaseModel getDatabaseModel () {
        return this.databaseModel;
    } 

	public String retrieveResolutionsByRowAsDecoratedXHTMLTableUsingMergeModel (MergeModel mergeModel) {
		
		String resolutionsByRowAsDecoratedXHTMLTable = null;
		
		  CachedRowSet resolutionsByRowAsCachedRowSet = this.retrieveResolutionsByRowAsCachedRowSetUsingMergeModel(mergeModel);

		  if (resolutionsByRowAsCachedRowSet != null) {
			  

			  	ResolutionsByRowFunctions resolutionsByRowFunctions = new ResolutionsByRowFunctions();
			  	resolutionsByRowFunctions.setResolutionsByRowAsCachedRowSet(resolutionsByRowAsCachedRowSet);
			  	resolutionsByRowFunctions.setSolutionsByRowAsCachedRowSet(this.retrieveSolutionsByRowAsCachedRowSet());

			  	JoinsCRUD joinsModel = new JoinsCRUD();
			  	joinsModel.setDatabaseModel(this.getDatabaseModel());
			  	resolutionsByRowFunctions.setJoinColumnNamesByColumnNumberAsHashMap(joinsModel.retrieveJoinColumnNamesByColumnNumberAsHashMapUsingMergeModel(mergeModel));
			  	
			  	/////////TODO:
			  	ResolutionsCRUD resolutionsCRUD = new ResolutionsCRUD();
			  	resolutionsCRUD.setDatabaseModel(this.getDatabaseModel());
			  	resolutionsByRowFunctions.setSolutionByColumnIdUsingCellCoordsAsHashMap(resolutionsCRUD.retrieveSolutionByColumnIdUsingCellCoordsAsHashMapUsingMergeModel(mergeModel));
			  	resolutionsByRowFunctions.setSolutionByCellIdUsingCellCoordsAsHashMap(resolutionsCRUD.retrieveSolutionByCellIdUsingCellCoordsAsHashMapUsingMergeModel(mergeModel));
			  	resolutionsByRowFunctions.setConstantUsingCellCoordsAsHashMap(resolutionsCRUD.retrieveConstantUsingCellCoordsAsHashMapUsingMergeModel(mergeModel));
			  	resolutionsByRowFunctions.setNullOrConstantSolutionUsingColumnNumberAsHashMap(resolutionsCRUD.retrieveNullOrConstantSolutionUsingColumnNumberAsHashMapUsingMergeModel(mergeModel));
			  	
			  	resolutionsByRowFunctions.setResolutionsByRowAsDecoratedXHTMLTableUsingResolutionsByRowAsCachedRowSet();
			  	resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowFunctions.getResolutionsByRowAsDecoratedXHTMLTable();
			    
		  } else {
			  
			  //TODO: Error handling
			  this.logger.warning("Error: resolutionsByRowAsCachedRowSet is null");
			  resolutionsByRowAsDecoratedXHTMLTable = "<p>Error: resolutionsByRowAsCachedRowSet is null</p>";
			  
		  }
		
		return resolutionsByRowAsDecoratedXHTMLTable;
	}

	public CachedRowSet retrieveSolutionsByRowAsCachedRowSet() {
		
		CachedRowSet solutionsByRowAsCachedRowSet = null;

		   String CACHED_ROW_SET_IMPL_CLASS = "com.sun.rowset.CachedRowSetImpl";
		   
			try {

				Connection connection = this.getDatabaseModel().getNewConnection();
				 
				if (connection != null) {

					
				      try{
				          PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from solution_by_row;");
				          preparedStatement.executeQuery();
				          Class<?> cachedRowSetImplClass = Class.forName(CACHED_ROW_SET_IMPL_CLASS);
				          solutionsByRowAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
				          solutionsByRowAsCachedRowSet.populate(preparedStatement.getResultSet());
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
		
		return solutionsByRowAsCachedRowSet;
	}

	public CachedRowSet retrieveResolutionsByRowAsCachedRowSetUsingMergeModel(
			MergeModel mergeModel) {
		
		CachedRowSet resolutionsByRowAsCachedRowSet = null;
		

		
		   String CACHED_ROW_SET_IMPL_CLASS = "com.sun.rowset.CachedRowSetImpl";
		   
			try {

				Connection connection = this.getDatabaseModel().getNewConnection();
				 
				if (connection != null) {

					
					String keyColumnReferencesAsCSVForSelectSQL = "";


					mergeModel.getJoinedKeytableModel().getDataAsCachedRowSet().beforeFirst();
					
					if (mergeModel.getJoinedKeytableModel().getDataAsCachedRowSet().next()) {
						
						//this.logger.info("Got joined keytable data as cached row set.");
						

					    for (int i = 1; i <= mergeModel.getJoinedKeytableModel().getDataAsCachedRowSet().getMetaData().getColumnCount(); i++) {
					    	
					    	keyColumnReferencesAsCSVForSelectSQL += ", " + "`" + mergeModel.getJoinedKeytableModel().getName() + "`.`" + mergeModel.getJoinedKeytableModel().getDataAsCachedRowSet().getMetaData().getColumnName(i) + "`";
					    }
						
						
					} else {
						//This is not necessarily a problem if there are no common keys.
						//this.logger.severe("Did not get joined keytable data as cached row set.");
					}
					
					
					String nonKeyCrossDatatableColumnNameAliasesForSelectSQL = "";
					mergeModel.getJoinsModel().getNonKeyCrossDatatableJoinsAsCachedRowSet().beforeFirst();
					
					if (mergeModel.getJoinsModel().getNonKeyCrossDatatableJoinsAsCachedRowSet().next()) {
						
						//this.logger.info("Got non-key cross-datatable joins data as cached row set.");
						
						mergeModel.getJoinsModel().getNonKeyCrossDatatableJoinsAsCachedRowSet().beforeFirst();
						
						while (mergeModel.getJoinsModel().getNonKeyCrossDatatableJoinsAsCachedRowSet().next()) {
							
							nonKeyCrossDatatableColumnNameAliasesForSelectSQL += ", `" + mergeModel.getDatatable1Model().getName() + "`.`" + mergeModel.getJoinsModel().getNonKeyCrossDatatableJoinsAsCachedRowSet().getString("datatable_1_column_name") + "` AS `column_" + mergeModel.getJoinsModel().getNonKeyCrossDatatableJoinsAsCachedRowSet().getString("column_number") + "_source_1`, `" + mergeModel.getDatatable2Model().getName() + "`.`" + mergeModel.getJoinsModel().getNonKeyCrossDatatableJoinsAsCachedRowSet().getString("datatable_2_column_name") + "` AS `column_" + mergeModel.getJoinsModel().getNonKeyCrossDatatableJoinsAsCachedRowSet().getString("column_number") + "_source_2` ";
						}
						
					} else {
						
						//NOTE: not an error
						//this.logger.severe("Did not get non-key cross-datatable joins data as cached row set.");
					}
					
					
					String datatable1JoinConditionSQL = "";
					String datatable2JoinConditionSQL = "";		
		
					mergeModel.getJoinsModel().getKeyJoinsAsCachedRowSet().beforeFirst();
					
					if (mergeModel.getJoinsModel().getKeyJoinsAsCachedRowSet().next()) {
						
						//this.logger.info("Got key joins data as cached row set.");
						
						mergeModel.getJoinsModel().getKeyJoinsAsCachedRowSet().first();
						
						datatable1JoinConditionSQL += "`" + mergeModel.getDatatable1Model().getName() + "`.`" + mergeModel.getJoinsModel().getKeyJoinsAsCachedRowSet().getString("datatable_1_column_name") + "` = `" + mergeModel.getJoinedKeytableModel().getName() + "`.`key_column_" + mergeModel.getJoinsModel().getKeyJoinsAsCachedRowSet().getString("column_number") + "` ";
						datatable2JoinConditionSQL += "`" + mergeModel.getDatatable2Model().getName() + "`.`" + mergeModel.getJoinsModel().getKeyJoinsAsCachedRowSet().getString("datatable_2_column_name") + "` = `" + mergeModel.getJoinedKeytableModel().getName() + "`.`key_column_" + mergeModel.getJoinsModel().getKeyJoinsAsCachedRowSet().getString("column_number") + "` ";
						
						while (mergeModel.getJoinsModel().getKeyJoinsAsCachedRowSet().next()) {
							
							datatable1JoinConditionSQL += "AND `" + mergeModel.getDatatable1Model().getName() + "`.`" + mergeModel.getJoinsModel().getKeyJoinsAsCachedRowSet().getString("datatable_1_column_name") + "` = `" + mergeModel.getJoinedKeytableModel().getName() + "`.`key_column_" + mergeModel.getJoinsModel().getKeyJoinsAsCachedRowSet().getString("column_number") + "` ";
							datatable2JoinConditionSQL += "AND `" + mergeModel.getDatatable2Model().getName() + "`.`" + mergeModel.getJoinsModel().getKeyJoinsAsCachedRowSet().getString("datatable_2_column_name") + "` = `" + mergeModel.getJoinedKeytableModel().getName() + "`.`key_column_" + mergeModel.getJoinsModel().getKeyJoinsAsCachedRowSet().getString("column_number") + "` ";
						}
						
					} else {
						this.logger.severe("Did not get key joins data as cached row set.");
					}		
					
					
				      try{
				    	  
				    	  
				    	  String selectResolutionsByRowSQL = "SELECT resolution.merge_id, resolution.joined_keytable_id, COUNT(*) AS conflicts_count, resolution.solution_by_row_id, resolution.constant " + 
		        		  keyColumnReferencesAsCSVForSelectSQL + 
		        		  nonKeyCrossDatatableColumnNameAliasesForSelectSQL +  
		        		  "FROM resolution " +
		        		  "JOIN `" + mergeModel.getJoinedKeytableModel().getName() + "` ON `" + mergeModel.getJoinedKeytableModel().getName() + "`.id = resolution.joined_keytable_id " +
		        		  "JOIN `" + mergeModel.getDatatable1Model().getName() + "` ON " + datatable1JoinConditionSQL + 
		        		  "JOIN `" + mergeModel.getDatatable2Model().getName() + "` ON " + datatable2JoinConditionSQL + 
		        		  "WHERE resolution.merge_id = ?  AND resolution.solution_by_column_id IS NULL AND resolution.solution_by_cell_id IS NULL " +
		        		  "GROUP BY resolution.joined_keytable_id " +
		        		  "ORDER BY resolution.joined_keytable_id " +
		        		  ";";				    	  
				    	  
				    	  //this.logger.info("selectResolutionsByRowSQL = " + selectResolutionsByRowSQL);
				    	  
				          PreparedStatement preparedStatement = connection.prepareStatement(selectResolutionsByRowSQL);
				          preparedStatement.setInt(1, mergeModel.getId());
				          
				          preparedStatement.executeQuery();
				          Class<?> cachedRowSetImplClass = Class.forName(CACHED_ROW_SET_IMPL_CLASS);
				          resolutionsByRowAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
				          resolutionsByRowAsCachedRowSet.populate(preparedStatement.getResultSet());
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
	
	
	
	     return resolutionsByRowAsCachedRowSet;
	}

	public void updateResolutionsByRowByMergeIdUsingResolutionsByRowAsJSONObject(
			Integer mergeId, JSONObject resolutionsByRowAsJsonObject) {
		
		//Note: This also updates the merge's total conflict count.
		
		//for each index in column_number, update the fields present (except for keys)
		
		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);
		
		//TODO: Perhaps get all of the model here?

		
			
			Connection connection = this.getDatabaseModel().getNewConnection();
			 
			if (connection != null) {
				
				try {
					
		          //Insert all the joins from this JSON Object
				
		          JSONArray joinedKeytableIds = resolutionsByRowAsJsonObject.optJSONArray("joined_keytable_id");
		          JSONArray solutionByRowIds = resolutionsByRowAsJsonObject.optJSONArray("solution_by_row_id");

		          if (joinedKeytableIds == null) {
		        	  joinedKeytableIds = new JSONArray();
		        	  joinedKeytableIds.put(resolutionsByRowAsJsonObject.optInt("joined_keytable_id"));
		          }
		          if (solutionByRowIds == null) {
		        	  solutionByRowIds = new JSONArray();
		        	  solutionByRowIds.put(resolutionsByRowAsJsonObject.optInt("solution_by_row_id"));
		          }
		          
		          for (int i = 0; i < joinedKeytableIds.length(); i++) {
		        	  
		        	  ResolutionByRowModel resolutionByRowModel = new ResolutionByRowModel();
		        	  
		        	  resolutionByRowModel.setMergeModel(mergeModel);
		        	  resolutionByRowModel.getJoinedKeytableModel().setId(joinedKeytableIds.getInt(i));
		        	  
		        	  
		        	  //FIXME: Don't want to overwrite conflicts_count with null. Don't set to 0 if you have a solution (loses data).
		        	  //Merge's total_conflicts_count will only count conflicts that have no solution.
		        	  
		        	  SolutionByRowModel solutionByRowModel = new SolutionByRowModel();
		        	  
		        	  //optInt returns 0 if not an Integer
		        	  if (solutionByRowIds.optInt(i) != 0) {
		        		  solutionByRowModel.setId(solutionByRowIds.getInt(i));
		        	  } else {
		        		  solutionByRowModel.setId(null);
		        	  }
		        	  
		        	  
		        	  resolutionByRowModel.setSolutionByRowModel(solutionByRowModel);

		        	  if (resolutionsByRowAsJsonObject.has("constant-" + joinedKeytableIds.getInt(i))) {
			        	  
		        		  resolutionByRowModel.setConstant(resolutionsByRowAsJsonObject.getString("constant-" + joinedKeytableIds.getInt(i)));
		        		  
		        	  } else {
		        		  
		        		  resolutionByRowModel.setConstant(null);
		        	  }
		        	  
		        	  
		        	  this.updateResolutionByRowUsingResolutionByRowModel(resolutionByRowModel, connection);
		        	  
		        	  
		        	  
		          }
		          
		        //TODO: Recount the conflicts (take problems with solutions as 0, otherwise use the resolution conflict_count)

	        	  MergeScripts mergeScripts = new MergeScripts();
	        	  

	        	  mergeModel = mergeScripts.retrieveMergeAsMergeModelThroughDeterminingTotalConflictsCountUsingMergeModel(mergeModel, connection);
	        	  
		          
					
				} 
				catch (Exception e) {
					
					this.logger.severe(e.toString());
					e.printStackTrace();
				}finally {
						
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

	public void updateResolutionByRowUsingResolutionByRowModel(
			ResolutionByRowModel resolutionByRowModel, Connection connection) {
		
	      try {

	    	  String updateResolutionByRowSQL = "";
	    	  
	          //FIXME: What is 5? ("Remove entire row")
	    	  
	    	  // This approach is difficult to undo. Instead only apply at export stage. 
//	          if (resolutionByRowModel.getSolutionByRowModel().getId() != null && resolutionByRowModel.getSolutionByRowModel().getId() == 5) {
//	        	  
//	        	  updateResolutionByRowSQL = 
//	    			  "UPDATE resolution SET solution_by_row_id = ?, constant = ? " +
//	    			  "WHERE merge_id = ? AND joined_keytable_id = ? " +
//	    			  ";";
//		          
//	          } else {
//	        	  
//	        	  updateResolutionByRowSQL = 
//	    			  "UPDATE resolution SET solution_by_row_id = ?, constant = ? " +
//	    			  "WHERE merge_id = ? AND joined_keytable_id = ? AND solution_by_column_id IS NULL AND solution_by_cell_id IS NULL " +
//	    			  ";";
//	          }	    	  
	    	  
        	  updateResolutionByRowSQL = 
    			  "UPDATE resolution SET solution_by_row_id = ?, constant = ? " +
    			  "WHERE merge_id = ? AND joined_keytable_id = ? AND solution_by_column_id IS NULL AND solution_by_cell_id IS NULL " +
    			  ";";	    	  
	    	  
	    	  PreparedStatement preparedStatement = connection.prepareStatement(updateResolutionByRowSQL);
	          
	          
	          if (resolutionByRowModel.getSolutionByRowModel().getId() != null) {
	        	  preparedStatement.setInt(1, resolutionByRowModel.getSolutionByRowModel().getId());
	          } else {
	        	  preparedStatement.setNull(1, java.sql.Types.INTEGER);
	          }
	          
	          //FIXME: What is 4? ("Use CONSTANT")
	          if (resolutionByRowModel.getSolutionByRowModel().getId() != null && resolutionByRowModel.getSolutionByRowModel().getId() == 4) {
	        	  
		          if (resolutionByRowModel.getConstant() != null) {
		        	  
		        	  //Allow blank string constants (for future-proofing against null/blank string distinctions, especially in export)
		        	  preparedStatement.setString(2, resolutionByRowModel.getConstant());
		        	  
		          } else {
		        	  preparedStatement.setNull(2, java.sql.Types.VARCHAR);
		          }
		          
	          } else {
	        	  preparedStatement.setNull(2, java.sql.Types.VARCHAR);
	          }
	          
	          preparedStatement.setInt(3, resolutionByRowModel.getMergeModel().getId());
	          preparedStatement.setInt(4, resolutionByRowModel.getJoinedKeytableModel().getId());
	          
	          preparedStatement.executeUpdate();
	          preparedStatement.close();
	          
	
	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 	
	        
	}
    
}
