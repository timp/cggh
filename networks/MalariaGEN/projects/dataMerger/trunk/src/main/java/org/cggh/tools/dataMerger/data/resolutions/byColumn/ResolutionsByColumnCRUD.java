package org.cggh.tools.dataMerger.data.resolutions.byColumn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.databases.DatabaseModel;
import org.cggh.tools.dataMerger.data.merges.MergeModel;
import org.cggh.tools.dataMerger.functions.data.resolutions.byColumn.ResolutionsByColumnFunctions;
import org.cggh.tools.dataMerger.scripts.data.merges.MergeScripts;
import org.json.JSONArray;
import org.json.JSONObject;


public class ResolutionsByColumnCRUD implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2346858760723955545L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.resolutionsByColumn");

	private DatabaseModel databaseModel;
	
	public ResolutionsByColumnCRUD() {

		this.setDatabaseModel(new DatabaseModel());
		
	}

    public void setDatabaseModel (final DatabaseModel databaseModel) {
        this.databaseModel  = databaseModel;
    }
    public DatabaseModel getDatabaseModel () {
        return this.databaseModel;
    } 


	public String retrieveResolutionsByColumnAsDecoratedXHTMLTableUsingMergeModel (MergeModel mergeModel) {
		
		String resolutionsByColumnAsDecoratedXHTMLTable = null;
		
		  CachedRowSet resolutionsByColumnAsCachedRowSet = this.retrieveResolutionsByColumnAsCachedRowSetUsingMergeId(mergeModel.getId());

		  if (resolutionsByColumnAsCachedRowSet != null) {
			  

			  	CachedRowSet solutionsByColumnAsCachedRowSet = this.retrieveSolutionsByColumnAsCachedRowSet();

			  	ResolutionsByColumnFunctions resolutionsByColumnFunctions = new ResolutionsByColumnFunctions();
			  	resolutionsByColumnFunctions.setResolutionsByColumnAsCachedRowSet(resolutionsByColumnAsCachedRowSet);
			  	resolutionsByColumnFunctions.setSolutionsByColumnAsCachedRowSet(solutionsByColumnAsCachedRowSet);
			  	resolutionsByColumnFunctions.setResolutionsByColumnAsDecoratedXHTMLTableUsingResolutionsByColumnAsCachedRowSet();
			  	resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnFunctions.getResolutionsByColumnAsDecoratedXHTMLTable();
			    
		  } else {
			  
			  //TODO: Error handling
			  this.logger.warning("Error: resolutionsByColumnAsCachedRowSet is null");
			  resolutionsByColumnAsDecoratedXHTMLTable = "<p>Error: resolutionsByColumnAsCachedRowSet is null</p>";
			  
		  }
		
		return resolutionsByColumnAsDecoratedXHTMLTable;
	}


	public CachedRowSet retrieveSolutionsByColumnAsCachedRowSet() {
		
		CachedRowSet solutionsByColumnAsCachedRowSet = null;

		   String CACHED_ROW_SET_IMPL_CLASS = "com.sun.rowset.CachedRowSetImpl";
		   
			

				Connection connection = this.getDatabaseModel().getNewConnection();
				 
				if (connection != null) {
				
					try {
					
					 //FIXME: Apparently a bug in CachedRowSet using getX('columnAlias') aka columnLabel, which actually only works with getX('columnName'), so using getX('columnIndex').
					 
					
				      try{
				          PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from solution_by_column;");
				          preparedStatement.executeQuery();
				          Class<?> cachedRowSetImplClass = Class.forName(CACHED_ROW_SET_IMPL_CLASS);
				          solutionsByColumnAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
				          solutionsByColumnAsCachedRowSet.populate(preparedStatement.getResultSet());
				          preparedStatement.close();
	
				        }
				        catch(SQLException sqlException){
				        	this.logger.severe(sqlException.toString());
					    	sqlException.printStackTrace();
				        } 	
				
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
					
			
		
		return solutionsByColumnAsCachedRowSet;
	}

	public CachedRowSet retrieveResolutionsByColumnAsCachedRowSetUsingMergeId(
			Integer mergeId) {
		
		CachedRowSet resolutionsByColumnAsCachedRowSet = null;
		
		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);
		
		   String CACHED_ROW_SET_IMPL_CLASS = "com.sun.rowset.CachedRowSetImpl";
		   
			

				Connection connection = this.getDatabaseModel().getNewConnection();
				 
				if (connection != null) {
				
					try {
					
					 //Note: A bug in CachedRowSet using getX('columnAlias') aka columnLabel, which actually only works with getX('columnName'), so using getX('columnIndex').
					 
					
				      try{
				    	  
				    	  //TODO: The old way (remove when tested the new way)
				    	  
				          
				          PreparedStatement preparedStatement = connection.prepareStatement(
				        		  "SELECT resolution.column_number, `join`.column_name, COUNT(*) AS conflicts_count, resolution.conflict_id, conflict.description, resolution.solution_by_column_id, resolution.constant " + 
				        		  "FROM resolution " +
				        		  "JOIN `join` ON `join`.merge_id = resolution.merge_id AND `join`.column_number = resolution.column_number " +
				        		  "JOIN conflict ON conflict.id = resolution.conflict_id " +
				        		  "WHERE resolution.merge_id = ? AND resolution.solution_by_row_id IS NULL AND resolution.solution_by_cell_id IS NULL " +
				        		  "GROUP BY resolution.column_number " +
				        		  "ORDER BY resolution.column_number " +
				        		  ";");				    	  
				    	  
				          preparedStatement.setInt(1, mergeModel.getId());
				          preparedStatement.executeQuery();
				          Class<?> cachedRowSetImplClass = Class.forName(CACHED_ROW_SET_IMPL_CLASS);
				          resolutionsByColumnAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
				          resolutionsByColumnAsCachedRowSet.populate(preparedStatement.getResultSet());
				          preparedStatement.close();
	
				        }
				        catch(SQLException sqlException){
				        	this.logger.severe(sqlException.toString());
					    	sqlException.printStackTrace();
				        } 	
				
				} 
				catch (Exception e) {
					//System.out.println("Exception from getMergesAsCachedRowSet.");
					e.printStackTrace();
				}finally {
							
							try {
								connection.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							
						} 
					
				} else {
					
					//System.out.println("connection.isClosed");
				}
					
			
	
	
	
	     return resolutionsByColumnAsCachedRowSet;
	}

	public void updateResolutionsByColumnByMergeIdUsingResolutionsByColumnAsJSONObject(
			Integer mergeId, JSONObject resolutionsByColumnAsJsonObject) {
		
		//Note: This also updates the merge's total conflict count.
		
		//for each index in column_number, update the fields present (except for keys)
		
		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);

		
			
			Connection connection = this.getDatabaseModel().getNewConnection();
			 
			if (connection != null) {
				
				try {
					
		          //Insert all the joins from this JSON Object
				
		          JSONArray columnNumbers = resolutionsByColumnAsJsonObject.optJSONArray("column_number");
		          JSONArray conflictIds = resolutionsByColumnAsJsonObject.optJSONArray("conflict_id");
		          JSONArray solutionByColumnIds = resolutionsByColumnAsJsonObject.optJSONArray("solution_by_column_id");

		          if (columnNumbers == null) {
		        	  columnNumbers = new JSONArray();
		        	  columnNumbers.put(resolutionsByColumnAsJsonObject.optInt("column_number"));
		          }
		          if (conflictIds == null) {
		        	  conflictIds = new JSONArray();
		        	  conflictIds.put(resolutionsByColumnAsJsonObject.optInt("conflict_id"));
		          }
		          if (solutionByColumnIds == null) {
		        	  solutionByColumnIds = new JSONArray();
		        	  solutionByColumnIds.put(resolutionsByColumnAsJsonObject.optInt("solution_by_column_id"));
		          }
		          
		          for (int i = 0; i < columnNumbers.length(); i++) {
		        	  
		        	  ResolutionByColumnModel resolutionByColumnModel = new ResolutionByColumnModel();
		        	  
		        	  resolutionByColumnModel.setMergeModel(mergeModel);
		        	  resolutionByColumnModel.setColumnNumber(columnNumbers.getInt(i));
		        	  
		        	  ConflictModel problemByColumnModel = new ConflictModel();
		        	  problemByColumnModel.setId(conflictIds.getInt(i));
		        	  resolutionByColumnModel.setConflictModel(problemByColumnModel);
		        	  
		        	  //FIXME: Don't want to overwrite conflicts_count with null. Don't set to 0 if you have a solution (loses data).
		        	  //Merge's total_conflicts_count will only count conflicts that have no solution.
		        	  
		        	  SolutionByColumnModel solutionByColumnModel = new SolutionByColumnModel();
		        	  
		        	  //optInt returns 0 if not an Integer
		        	  if (solutionByColumnIds.optInt(i) != 0) {
		        		  solutionByColumnModel.setId(solutionByColumnIds.getInt(i));
		        	  } else {
		        		  solutionByColumnModel.setId(null);
		        	  }
		        	  
		        	  
		        	  resolutionByColumnModel.setSolutionByColumnModel(solutionByColumnModel);

		        	  if (resolutionsByColumnAsJsonObject.has("constant-" + columnNumbers.getInt(i))) {
			        	  
		        		  resolutionByColumnModel.setConstant(resolutionsByColumnAsJsonObject.getString("constant-" + columnNumbers.getInt(i)));
		        		  
		        	  } else {
		        		  
		        		  resolutionByColumnModel.setConstant(null);
		        	  }
		        	  
		        	  
		        	  this.updateResolutionByColumnUsingResolutionByColumnModel(resolutionByColumnModel, connection);
		        	  
		        	  

		          }
		          
	        	  //TODO: Recount the conflicts (take problems with solutions as 0, otherwise use the resolution conflict_count)

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

	public void updateResolutionByColumnUsingResolutionByColumnModel(
			ResolutionByColumnModel resolutionByColumnModel,
			Connection connection) {
		
		
	      try {

	    	  PreparedStatement preparedStatement = connection.prepareStatement(
	    			  "UPDATE resolution SET solution_by_column_id = ?, constant = ? " +
	    			  "WHERE merge_id = ? AND column_number = ? AND conflict_id = ? AND solution_by_row_id IS NULL AND solution_by_cell_id IS NULL " +
	    			  ";");
	    	  
	          if (resolutionByColumnModel.getSolutionByColumnModel().getId() != null) {
	        	  preparedStatement.setInt(1, resolutionByColumnModel.getSolutionByColumnModel().getId());
	          } else {
	        	  preparedStatement.setNull(1, java.sql.Types.INTEGER);
	          }
	          
	          //FIXME: What is 4? ("Use CONSTANT")
	          if (resolutionByColumnModel.getSolutionByColumnModel().getId() != null && resolutionByColumnModel.getSolutionByColumnModel().getId() == 4) {
	        	  
		          if (resolutionByColumnModel.getConstant() != null) {
		        	  
		        	  //Allow blank string constants (for future-proofing against null/blank string distinctions, especially in export)
		        	  preparedStatement.setString(2, resolutionByColumnModel.getConstant());
		        	  
		          } else {
		        	  preparedStatement.setNull(2, java.sql.Types.VARCHAR);
		          }
		          
	          } else {
	        	  preparedStatement.setNull(2, java.sql.Types.VARCHAR);
	          }
	          
	          preparedStatement.setInt(3, resolutionByColumnModel.getMergeModel().getId());
	          preparedStatement.setInt(4, resolutionByColumnModel.getColumnNumber());
	          preparedStatement.setInt(5, resolutionByColumnModel.getConflictModel().getId());
	          
	          preparedStatement.executeUpdate();
	          preparedStatement.close();
	          
	
	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 	
		
	}
}
