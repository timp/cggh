package org.cggh.tools.dataMerger.data.resolutions.byCell;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.DataModel;
import org.cggh.tools.dataMerger.data.joins.JoinsModel;
import org.cggh.tools.dataMerger.data.merges.MergeModel;
import org.cggh.tools.dataMerger.data.resolutions.ResolutionsModel;
import org.cggh.tools.dataMerger.functions.resolutions.byCell.ResolutionsByCellFunctionsModel;
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
			  	
			  	ResolutionsModel resolutionsModel = new ResolutionsModel();
			  	resolutionsModel.setDataModel(this.getDataModel());
			  	resolutionsByCellFunctionsModel.setUnresolvedByColumnOrRowConflictsCountUsingColumnNumberAsHashMap(resolutionsModel.retrieveUnresolvedByColumnOrRowConflictsCountUsingColumnNumberAsHashMapUsingMergeModel(mergeModel));
			  	resolutionsByCellFunctionsModel.setUnresolvedByColumnOrRowStatusUsingCellCoordsAsHashMap(resolutionsModel.retrieveUnresolvedByColumnOrRowStatusUsingCellCoordsAsHashMapUsingMergeModel(mergeModel));
			  	
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
					
					//FIXME: This needs to return a solution_by_cell_id for each cell (not just each row!)
					// solution_by_cell_id_
					
					
					
					
					//Example of current SQL 
//					SELECT resolution.merge_id, resolution.joined_keytable_id, resolution.solution_by_cell_id, resolution.constant , `joined_keytable_1`.`id`, `joined_keytable_1`.`key_column_2`, `joined_keytable_1`.`key_column_30`, `datatable_1`.`Row` AS `column_1_source_1`, `datatable_2`.`Row` AS `column_1_source_2` , `datatable_1`.`Admission Date` AS `column_3_source_1`, `datatable_2`.`Admission Date` AS `column_3_source_2` , `datatable_1`.`Admission Time` AS `column_4_source_1`, `datatable_2`.`Admission Time` AS `column_4_source_2` , `datatable_1`.`Blantyre Coma Score` AS `column_5_source_1`, `datatable_2`.`Blantyre Coma Score` AS `column_5_source_2` , `datatable_1`.`Blantyre Eye Score` AS `column_6_source_1`, `datatable_2`.`Blantyre Eye Score` AS `column_6_source_2` , `datatable_1`.`Blantyre Motor Score` AS `column_7_source_1`, `datatable_2`.`Blantyre Motor Score` AS `column_7_source_2` , `datatable_1`.`Blantyre Verbal Score` AS `column_8_source_1`, `datatable_2`.`Blantyre Verbal Score` AS `column_8_source_2` , `datatable_1`.`Blood Transfusion Received` AS `column_9_source_1`, `datatable_2`.`Blood Transfusion Received` AS `column_9_source_2` , `datatable_1`.`Case Or Control` AS `column_10_source_1`, `datatable_2`.`Case Or Control` AS `column_10_source_2` , `datatable_1`.`Consent Obtained` AS `column_11_source_1`, `datatable_2`.`Consent Obtained` AS `column_11_source_2` , `datatable_1`.`Convulsions Within24 Hours Prior To Admission` AS `column_12_source_1`, `datatable_2`.`Convulsions Within24 Hours Prior To Admission` AS `column_12_source_2` , `datatable_1`.`Date Death Discharge Absconded` AS `column_13_source_1`, `datatable_2`.`Date Death Discharge Absconded` AS `column_13_source_2` , `datatable_1`.`Dehydrated` AS `column_14_source_1`, `datatable_2`.`Dehydrated` AS `column_14_source_2` , `datatable_1`.`Estimated Age [months]` AS `column_15_source_1`, `datatable_2`.`Estimated Age [months]` AS `column_15_source_2` , `datatable_1`.`Ethnic Group Father` AS `column_16_source_1`, `datatable_2`.`Ethnic Group Father` AS `column_16_source_2` , `datatable_1`.`Fever Within Past48 Hours` AS `column_17_source_1`, `datatable_2`.`Fever Within Past48 Hours` AS `column_17_source_2` , `datatable_1`.`Glucose [mmol/L]` AS `column_18_source_1`, `datatable_2`.`Glucose [mmol/L]` AS `column_18_source_2` , `datatable_1`.`Hematocrit [%]` AS `column_19_source_1`, `datatable_2`.`Hematocrit [%]` AS `column_19_source_2` , `datatable_1`.`Jaundice Now` AS `column_20_source_1`, `datatable_2`.`Jaundice Now` AS `column_20_source_2` , `datatable_1`.`Location Region` AS `column_21_source_1`, `datatable_2`.`Location Region` AS `column_21_source_2` , `datatable_1`.`Location Village` AS `column_22_source_1`, `datatable_2`.`Location Village` AS `column_22_source_2` , `datatable_1`.`Other Diagnosis Gastroenteritis` AS `column_23_source_1`, `datatable_2`.`Other Diagnosis Gastroenteritis` AS `column_23_source_2` , `datatable_1`.`Other Diagnosis Other` AS `column_25_source_1`, `datatable_2`.`Other Diagnosis Other` AS `column_25_source_2` , `datatable_1`.`Other Diagnosis Sickle Cell` AS `column_26_source_1`, `datatable_2`.`Other Diagnosis Sickle Cell` AS `column_26_source_2` , `datatable_1`.`Outcome` AS `column_27_source_1`, `datatable_2`.`Outcome` AS `column_27_source_2` , `datatable_1`.`Parasitemia [parasites/µL]` AS `column_28_source_1`, `datatable_2`.`Parasitemia [parasites/µL]` AS `column_28_source_2` , `datatable_1`.`Respiratory Distress` AS `column_29_source_1`, `datatable_2`.`Respiratory Distress` AS `column_29_source_2` , `datatable_1`.`Sex` AS `column_31_source_1`, `datatable_2`.`Sex` AS `column_31_source_2` , `datatable_1`.`Temperature [°C]` AS `column_33_source_1`, `datatable_2`.`Temperature [°C]` AS `column_33_source_2` , `datatable_1`.`Weight [kg]` AS `column_34_source_1`, `datatable_2`.`Weight [kg]` AS `column_34_source_2` 
//					FROM resolution 
//					JOIN `joined_keytable_1` ON `joined_keytable_1`.id = resolution.joined_keytable_id 
//					JOIN `datatable_1` ON `datatable_1`.`ID` = `joined_keytable_1`.`key_column_2` AND `datatable_1`.`Sample Label` = `joined_keytable_1`.`key_column_30` 
//					JOIN `datatable_2` ON `datatable_2`.`ID` = `joined_keytable_1`.`key_column_2` AND `datatable_2`.`Sample Label` = `joined_keytable_1`.`key_column_30` 
//					WHERE resolution.merge_id = 1 AND resolution.solution_by_column_id IS NULL AND resolution.solution_by_row_id IS NULL 
//					GROUP BY resolution.joined_keytable_id 
//					ORDER BY resolution.joined_keytable_id ;
					
					
					
					

					
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
				    	  
				    	  String selectResolutionsByCellSQL = "SELECT resolution.merge_id, resolution.joined_keytable_id, resolution.solution_by_cell_id, resolution.constant " + 
											        		  keyColumnReferencesAsCSVForSelectSQL + 
											        		  nonKeyCrossDatatableColumnNameAliasesForSelectSQL +  
											        		  "FROM resolution " +
											        		  "JOIN `" + mergeModel.getJoinedKeytableModel().getName() + "` ON `" + mergeModel.getJoinedKeytableModel().getName() + "`.id = resolution.joined_keytable_id " +
											        		  "JOIN `" + mergeModel.getDatatable1Model().getName() + "` ON " + datatable1JoinConditionSQL + 
											        		  "JOIN `" + mergeModel.getDatatable2Model().getName() + "` ON " + datatable2JoinConditionSQL + 
											        		  "WHERE resolution.merge_id = ? AND resolution.solution_by_column_id IS NULL AND resolution.solution_by_row_id IS NULL " +
											        		  "GROUP BY resolution.joined_keytable_id " +
											        		  "ORDER BY resolution.joined_keytable_id " +
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
				
				JSONArray keys = resolutionsByCellAsJsonObject.names();
				
				 Pattern solutionByCellIdKeyPattern = Pattern.compile("^solution_by_cell_id-(\\d+)-(\\d+)$");
				 
					 
				 
				
				for(int i = 0; i < keys.length(); i++) {
					
					this.logger.info("key " + keys.getString(i) + " = " + resolutionsByCellAsJsonObject.get(keys.getString(i)));
					
					Matcher solutionByCellIdKeyPatternMatcher = solutionByCellIdKeyPattern.matcher(keys.getString(i));
					
					 if (solutionByCellIdKeyPatternMatcher.find()) {
						 
						 
						 ResolutionByCellModel resolutionByCellModel = new ResolutionByCellModel();
						 
						
			        	  resolutionByCellModel.setMergeModel(mergeModel);
			        	  
			        	  this.logger.info("joinedKeytableID: " + solutionByCellIdKeyPatternMatcher.group(1));
			        	  resolutionByCellModel.getJoinedKeytableModel().setId(Integer.parseInt(solutionByCellIdKeyPatternMatcher.group(1)));
			        	  
			        	  this.logger.info("columnNumber: " + solutionByCellIdKeyPatternMatcher.group(2));
			        	  resolutionByCellModel.getJoinModel().setColumnNumber(Integer.parseInt(solutionByCellIdKeyPatternMatcher.group(2)));

			        	//Note: Merge's total_conflicts_count will only count conflicts that have no solution.

			        	  if (resolutionsByCellAsJsonObject.optInt(keys.getString(i)) != 0) {
			        		  
			        		  this.logger.info("solutionByCellId: " + resolutionsByCellAsJsonObject.getInt(keys.getString(i)));
			        		  resolutionByCellModel.getSolutionByCellModel().setId(resolutionsByCellAsJsonObject.getInt(keys.getString(i)));
			        	  } else {
			        		  resolutionByCellModel.getSolutionByCellModel().setId(null);
			        	  }
			        	  
			        	  
			        	  
			        	  if (resolutionsByCellAsJsonObject.has("constant-" + resolutionByCellModel.getJoinedKeytableModel().getId() + "-" + resolutionByCellModel.getJoinModel().getColumnNumber())) {
			        	  
			        		  this.logger.info("solutionByCellId: " + resolutionsByCellAsJsonObject.getString("constant-" + resolutionByCellModel.getJoinedKeytableModel().getId() + "-" + resolutionByCellModel.getJoinModel().getColumnNumber()));
			        		  resolutionByCellModel.setConstant(resolutionsByCellAsJsonObject.getString("constant-" + resolutionByCellModel.getJoinedKeytableModel().getId() + "-" + resolutionByCellModel.getJoinModel().getColumnNumber()));
		        		  
			        	  } else {
			        		  
			        		  resolutionByCellModel.setConstant(null);
			        	  }
			        	  
			        	  this.updateResolutionByCellUsingResolutionByCellModel(resolutionByCellModel, connection);
			        	  
			        	  
					 }
					
					
				}
				
	        	  //TODO: Recount the conflicts (take problems with solutions as 0, otherwise use the resolution conflict_count)

	        	  MergeScriptsModel mergeScriptsModel = new MergeScriptsModel();
	        	  mergeModel = mergeScriptsModel.retrieveMergeAsMergeModelThroughDeterminingTotalConflictsCountUsingMergeModel(mergeModel, connection);
	        	  
		          
					
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
			ResolutionByCellModel resolutionByCellModel, Connection connection) {
		
	      try {

	          PreparedStatement preparedStatement = connection.prepareStatement(
	        		  "UPDATE resolution SET solution_by_cell_id = ?, constant = ? " +
	        		  "WHERE merge_id = ? AND joined_keytable_id = ? AND column_number = ? AND solution_by_column_id IS NULL AND solution_by_row_id IS NULL " +
	        		  ";");
	          
	          
	          if (resolutionByCellModel.getSolutionByCellModel().getId() != null) {
	        	  preparedStatement.setInt(1, resolutionByCellModel.getSolutionByCellModel().getId());
	        	  this.logger.info("1 solution_by_cell_id: " + resolutionByCellModel.getSolutionByCellModel().getId());
	          } else {
	        	  preparedStatement.setNull(1, java.sql.Types.INTEGER);
	          }
	          
	          //FIXME: What is 4? ("Use CONSTANT")
	          if (resolutionByCellModel.getSolutionByCellModel().getId() != null && resolutionByCellModel.getSolutionByCellModel().getId() == 4) {
	        	  
		          if (resolutionByCellModel.getConstant() != null) {
		        	  
		        	  //Allow blank string constants (for future-proofing against null/blank string distinctions, especially in export)
		        	  preparedStatement.setString(2, resolutionByCellModel.getConstant());
		        	  
		        	  this.logger.info("2 constant: " + resolutionByCellModel.getConstant());
		        	  
		          } else {
		        	  preparedStatement.setNull(2, java.sql.Types.VARCHAR);
		          }
		          
	          } else {
	        	  preparedStatement.setNull(2, java.sql.Types.VARCHAR);
	          }
	          
	          this.logger.info("3 mergeId: " + resolutionByCellModel.getMergeModel().getId());
	          this.logger.info("4 joinedKeytableId: " + resolutionByCellModel.getJoinedKeytableModel().getId());
	          this.logger.info("5 constant: " + resolutionByCellModel.getJoinModel().getColumnNumber());
	          
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
