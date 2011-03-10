package org.cggh.tools.dataMerger.data.resolutionsByCell;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.DataModel;
import org.cggh.tools.dataMerger.data.joins.JoinsModel;
import org.cggh.tools.dataMerger.data.merges.MergeModel;
import org.cggh.tools.dataMerger.functions.resolutionsByCell.ResolutionsByCellFunctionsModel;
import org.cggh.tools.dataMerger.scripts.merges.MergeScriptsModel;
import org.json.JSONArray;
import org.json.JSONObject;


public class ResolutionsByCellModel implements java.io.Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = -145618744750618078L;

	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.resolutionsByCell");

	private DataModel dataModel;
	
	public ResolutionsByCellModel() {

		this.setDataModel(new DataModel());
		
	}

    public void setDataModel (final DataModel dataModel) {
        this.dataModel  = dataModel;
    }
    public DataModel getDataModel () {
        return this.dataModel;
    } 

	public String retrieveResolutionsByCellAsDecoratedXHTMLTableUsingMergeModel (MergeModel mergeModel) {
		
		String resolutionsByCellAsDecoratedXHTMLTable = null;
		
		  CachedRowSet resolutionsByCellAsCachedRowSet = this.retrieveResolutionsByCellAsCachedRowSetUsingMergeModel(mergeModel);

		  if (resolutionsByCellAsCachedRowSet != null) {
			  

			  	ResolutionsByCellFunctionsModel resolutionsByCellFunctionsModel = new ResolutionsByCellFunctionsModel();
			  	resolutionsByCellFunctionsModel.setResolutionsByCellAsCachedRowSet(resolutionsByCellAsCachedRowSet);
			  	resolutionsByCellFunctionsModel.setSolutionsByCellAsCachedRowSet(this.retrieveSolutionsByCellAsCachedRowSet());

			  	JoinsModel joinsModel = new JoinsModel();
			  	joinsModel.setDataModel(this.getDataModel());
			  	resolutionsByCellFunctionsModel.setJoinColumnNamesByColumnNumberAsHashMap(joinsModel.retrieveJoinColumnNamesByColumnNumberAsHashMapUsingMergeModel(mergeModel));
			  	resolutionsByCellFunctionsModel.setResolutionsByCellAsDecoratedXHTMLTableUsingResolutionsByCellAsCachedRowSet();
			  	resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellFunctionsModel.getResolutionsByCellAsDecoratedXHTMLTable();
			    
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
				
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				
				Connection connection = this.getDataModel().getNewConnection();
				 
				if (!connection.isClosed()) {

					
				      try{
				          PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from solution_by_row;");
				          preparedStatement.executeQuery();
				          Class<?> cachedRowSetImplClass = Class.forName(CACHED_ROW_SET_IMPL_CLASS);
				          solutionsByCellAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
				          solutionsByCellAsCachedRowSet.populate(preparedStatement.getResultSet());
				          preparedStatement.close();
	
				        }
				        catch(SQLException sqlException){
				        	this.logger.severe(sqlException.toString());
					    	sqlException.printStackTrace();
				        } 	
				
					connection.close();
					
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
				
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				
				Connection connection = this.getDataModel().getNewConnection();
				 
				if (!connection.isClosed()) {

					
					//FIXME
					//TODO
					
//					USE datamerger;
//					SELECT resolution_by_row.merge_id, resolution_by_row.joined_keytable_id, resolution_by_row.conflicts_count, resolution_by_row.solution_by_row_id, resolution_by_row.constant
//					, joined_keytable_1.key_column_2, joined_keytable_1.key_column_20
//					, datatable_1.`Row` AS `column_1_source_1`, datatable_2.`Row` AS `column_1_source_2`
//					FROM resolution_by_row 
//					JOIN joined_keytable_1 ON joined_keytable_1.id = resolution_by_row.joined_keytable_id
//					JOIN datatable_1 ON datatable_1.`ID` = joined_keytable_1.key_column_2 AND datatable_1.`Sample Label` = joined_keytable_1.key_column_20
//					JOIN datatable_2 ON datatable_2.`ID` = joined_keytable_1.key_column_2 AND datatable_2.`Sample Label` = joined_keytable_1.key_column_20
//					WHERE resolution_by_row.merge_id = 1
//					ORDER BY resolution_by_row.joined_keytable_id
//					;
					
					
					String keyColumnReferencesAsCSVForSelectSQL = "";


					mergeModel.getJoinedKeytableModel().getDataAsCachedRowSet().beforeFirst();
					
					if (mergeModel.getJoinedKeytableModel().getDataAsCachedRowSet().next()) {
						
						this.logger.info("Got joined keytable data as cached row set.");
						

					    for (int i = 1; i <= mergeModel.getJoinedKeytableModel().getDataAsCachedRowSet().getMetaData().getColumnCount(); i++) {
					    	
					    	keyColumnReferencesAsCSVForSelectSQL += ", " + "`" + mergeModel.getJoinedKeytableModel().getName() + "`.`" + mergeModel.getJoinedKeytableModel().getDataAsCachedRowSet().getMetaData().getColumnName(i) + "`";
					    }
						
						
					} else {
						this.logger.severe("Did not get joined keytable data as cached row set.");
					}
					
					
					String nonKeyCrossDatatableColumnNameAliasesForSelectSQL = "";
					mergeModel.getJoinsModel().getNonKeyCrossDatatableJoinsAsCachedRowSet().beforeFirst();
					
					if (mergeModel.getJoinsModel().getNonKeyCrossDatatableJoinsAsCachedRowSet().next()) {
						
						this.logger.info("Got non-key cross-datatable joins data as cached row set.");
						
						mergeModel.getJoinsModel().getNonKeyCrossDatatableJoinsAsCachedRowSet().beforeFirst();
						
						while (mergeModel.getJoinsModel().getNonKeyCrossDatatableJoinsAsCachedRowSet().next()) {
							
							nonKeyCrossDatatableColumnNameAliasesForSelectSQL += ", `" + mergeModel.getDatatable1Model().getName() + "`.`" + mergeModel.getJoinsModel().getNonKeyCrossDatatableJoinsAsCachedRowSet().getString("datatable_1_column_name") + "` AS `column_" + mergeModel.getJoinsModel().getNonKeyCrossDatatableJoinsAsCachedRowSet().getString("column_number") + "_source_1`, `" + mergeModel.getDatatable2Model().getName() + "`.`" + mergeModel.getJoinsModel().getNonKeyCrossDatatableJoinsAsCachedRowSet().getString("datatable_2_column_name") + "` AS `column_" + mergeModel.getJoinsModel().getNonKeyCrossDatatableJoinsAsCachedRowSet().getString("column_number") + "_source_2` ";
						}
						
					} else {
						this.logger.severe("Did not get non-key cross-datatable joins data as cached row set.");
					}
					
					
					//FIXME
					//TODO
					String datatable1JoinConditionSQL = "";
					String datatable2JoinConditionSQL = "";		
		
					mergeModel.getJoinsModel().getKeyJoinsAsCachedRowSet().beforeFirst();
					
					if (mergeModel.getJoinsModel().getKeyJoinsAsCachedRowSet().next()) {
						
						this.logger.info("Got key joins data as cached row set.");
						
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
				    	  
				    	  String selectResolutionsByCellSQL = "SELECT resolution_by_row.merge_id, resolution_by_row.joined_keytable_id, resolution_by_row.conflicts_count, resolution_by_row.solution_by_row_id, resolution_by_row.constant " + 
											        		  keyColumnReferencesAsCSVForSelectSQL + 
											        		  nonKeyCrossDatatableColumnNameAliasesForSelectSQL +  
											        		  "FROM resolution_by_row " +
											        		  "JOIN `" + mergeModel.getJoinedKeytableModel().getName() + "` ON `" + mergeModel.getJoinedKeytableModel().getName() + "`.id = resolution_by_row.joined_keytable_id " +
											        		  "JOIN `" + mergeModel.getDatatable1Model().getName() + "` ON " + datatable1JoinConditionSQL + 
											        		  "JOIN `" + mergeModel.getDatatable2Model().getName() + "` ON " + datatable2JoinConditionSQL + 
											        		  "WHERE resolution_by_row.merge_id = ? " +
											        		  "ORDER BY resolution_by_row.joined_keytable_id " +
											        		  ";";
				    	  
				    	  this.logger.info("selectResolutionsByCellSQL = " + selectResolutionsByCellSQL);
				    	  
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
				        } 	
				
					connection.close();
					
				} else {
					
					System.out.println("connection.isClosed");
				}
					
			} 
			catch (Exception e) {
				System.out.println("Exception from getMergesAsCachedRowSet.");
				e.printStackTrace();
			}
	
	
	
	     return resolutionsByCellAsCachedRowSet;
	}

	public void updateResolutionsByCellByMergeIdUsingResolutionsByCellAsJSONObject(
			Integer mergeId, JSONObject resolutionsByCellAsJsonObject) {
		
		//Note: This also updates the merge's total conflict count.
		
		//for each index in column_number, update the fields present (except for keys)
		
		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);
		
		//TODO: Perhaps get all of the model here?

		try {
			
			Connection connection = this.getDataModel().getNewConnection();
			 
			if (!connection.isClosed()) {
					
		          //Insert all the joins from this JSON Object
				
		          JSONArray joinedKeytableIds = resolutionsByCellAsJsonObject.optJSONArray("joined_keytable_id");
		          JSONArray solutionByRowIds = resolutionsByCellAsJsonObject.optJSONArray("solution_by_row_id");

		          if (joinedKeytableIds == null) {
		        	  joinedKeytableIds = new JSONArray();
		        	  joinedKeytableIds.put(resolutionsByCellAsJsonObject.optInt("joined_keytable_id"));
		          }
		          if (solutionByRowIds == null) {
		        	  solutionByRowIds = new JSONArray();
		        	  solutionByRowIds.put(resolutionsByCellAsJsonObject.optInt("solution_by_row_id"));
		          }
		          
		          for (int i = 0; i < joinedKeytableIds.length(); i++) {
		        	  
		        	  ResolutionByCellModel resolutionByRowModel = new ResolutionByCellModel();
		        	  
		        	  resolutionByRowModel.setMergeModel(mergeModel);
		        	  resolutionByRowModel.getJoinedKeytableModel().setId(joinedKeytableIds.getInt(i));
		        	  
		        	  
		        	  //FIXME: Don't want to overwrite conflicts_count with null. Don't set to 0 if you have a solution (loses data).
		        	  //Merge's total_conflicts_count will only count conflicts that have no solution.
		        	  
		        	  SolutionByCellModel solutionByRowModel = new SolutionByCellModel();
		        	  
		        	  //optInt returns 0 if not an Integer
		        	  if (solutionByRowIds.optInt(i) != 0) {
		        		  solutionByRowModel.setId(solutionByRowIds.getInt(i));
		        	  } else {
		        		  solutionByRowModel.setId(null);
		        	  }
		        	  
		        	  
		        	  resolutionByRowModel.setSolutionByCellModel(solutionByRowModel);

		        	  if (resolutionsByCellAsJsonObject.has("constant-" + joinedKeytableIds.getInt(i))) {
			        	  
		        		  resolutionByRowModel.setConstant(resolutionsByCellAsJsonObject.getString("constant-" + joinedKeytableIds.getInt(i)));
		        		  
		        	  } else {
		        		  
		        		  resolutionByRowModel.setConstant(null);
		        	  }
		        	  
		        	  
		        	  this.updateResolutionByCellUsingResolutionByCellModel(resolutionByRowModel, connection);
		        	  
		        	  
		        	  //TODO: Recount the conflicts (take problems with solutions as 0, otherwise use the resolution conflict_count)

		        	  MergeScriptsModel mergeScriptsModel = new MergeScriptsModel();
		        	  
		        	  //FIXME: This breaks if don't have all the merge info in the model, e.g. Upload1Model.
		        	  //TODO
		        	  //mergeModel = mergeScriptsModel.retrieveMergeAsMergeModelThroughDeterminingTotalConflictsCountUsingMergeModel(mergeModel, connection);
		        	  
		          }
		          
					
				connection.close();
				
			} else {
				
				this.logger.severe("connection.isClosed");
			}
				
		} 
		catch (Exception e) {
			
			this.logger.severe(e.toString());
			e.printStackTrace();
		}
	}

	public void updateResolutionByCellUsingResolutionByCellModel(
			ResolutionByCellModel resolutionByRowModel, Connection connection) {
		
	      try {

	          PreparedStatement preparedStatement = connection.prepareStatement("UPDATE resolution_by_row SET solution_by_row_id = ?, constant = ? WHERE merge_id = ? AND joined_keytable_id = ?;");
	          
	          
	          if (resolutionByRowModel.getSolutionByCellModel().getId() != null) {
	        	  preparedStatement.setInt(1, resolutionByRowModel.getSolutionByCellModel().getId());
	          } else {
	        	  preparedStatement.setNull(1, java.sql.Types.INTEGER);
	          }
	          
	          //FIXME: What is 4? ("Use CONSTANT")
	          if (resolutionByRowModel.getSolutionByCellModel().getId() != null && resolutionByRowModel.getSolutionByCellModel().getId() == 4) {
	        	  
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
