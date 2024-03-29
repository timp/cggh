package org.cggh.tools.dataMerger.data.merges;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.databases.DatabaseModel;
import org.cggh.tools.dataMerger.data.databases.DatabasesCRUD;
import org.cggh.tools.dataMerger.data.datatables.DatatablesCRUD;
import org.cggh.tools.dataMerger.data.joinedDatatables.JoinedDatatablesCRUD;
import org.cggh.tools.dataMerger.data.joinedKeytables.JoinedKeytablesCRUD;
import org.cggh.tools.dataMerger.data.joins.JoinModel;
import org.cggh.tools.dataMerger.data.joins.JoinsCRUD;
import org.cggh.tools.dataMerger.data.files.FilesCRUD;
import org.cggh.tools.dataMerger.data.users.UserModel;
import org.cggh.tools.dataMerger.functions.data.joins.JoinFunctions;
import org.cggh.tools.dataMerger.functions.data.merges.MergeFunctions;
import org.cggh.tools.dataMerger.functions.data.merges.MergesFunctions;
import org.cggh.tools.dataMerger.scripts.data.merges.MergeScripts;



public class MergesCRUD implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1917678012559065867L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.merges");
	
	
	private DatabaseModel databaseModel;

	
	public MergesCRUD() {

		
	}

    public void setDatabaseModel (final DatabaseModel databaseModel) {
        this.databaseModel  = databaseModel;
    }
    public DatabaseModel getDatabaseModel () {
        return this.databaseModel;
    } 

    

    public MergeModel retrieveMergeAsMergeModelUsingMergeIdAndUserId(Integer mergeId, Integer userId) {
    	
    	MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);
		
		try {

			Connection connection = this.getDatabaseModel().getNewConnection();
			 
			if (connection != null) {		

				mergeModel = retrieveMergeAsMergeModelUsingMergeIdAndUserId(mergeModel.getId(), userId, connection);
				
						
						try {
							connection.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						
				
			} else {
				
				//System.out.println("connection.isClosed");
			}
				
		} 
		catch (Exception e) {
			//System.out.println("Exception from getMergesAsCachedRowSet.");
			e.printStackTrace();
		}				
				
    	return mergeModel;
    }


	public MergeModel retrieveMergeAsMergeModelUsingMergeIdAndUserId(Integer mergeId, Integer userId, Connection connection) {

		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);
		
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, file_1_id, file_2_id, created_by_user_id, created_datetime, updated_datetime, datatable_1_duplicate_keys_count, datatable_2_duplicate_keys_count, total_duplicate_keys_count, total_conflicts_count, joined_keytable_name FROM `merge` " + 
	        		  "WHERE id = ? ORDER BY id;");
	          preparedStatement.setInt(1, mergeModel.getId());
	          preparedStatement.executeQuery();
	          ResultSet resultSet = preparedStatement.getResultSet();
	          
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();

	        	  //Set the merge data
	        	  mergeModel.getFile1Model().setId(resultSet.getInt("file_1_id"));
	        	  mergeModel.getFile2Model().setId(resultSet.getInt("file_2_id"));
	        	  mergeModel.getCreatedByUserModel().setId(resultSet.getInt("created_by_user_id"));
	        	  mergeModel.setCreatedDatetime(resultSet.getTimestamp("created_datetime"));
	        	  mergeModel.setUpdatedDatetime(resultSet.getTimestamp("updated_datetime"));
	        	  mergeModel.setTotalDuplicateKeysCount(resultSet.getInt("total_duplicate_keys_count"));
	        	  mergeModel.setTotalConflictsCount(resultSet.getInt("total_conflicts_count"));
	        	  
	        	  //Done further down
	        	  //mergeModel.getJoinedKeytableModel().setName(resultSet.getString("joined_keytable_name"));
	        	  
	        	  //Retrieve the upload data
	        	  FilesCRUD filesCRUD = new FilesCRUD();
	        	  filesCRUD.setDatabaseModel(this.getDatabaseModel());
	        	  mergeModel.setFile1Model(filesCRUD.retrieveFileAsFileModelByFileId(mergeModel.getFile1Model().getId(), connection));
	        	  mergeModel.setFile2Model(filesCRUD.retrieveFileAsFileModelByFileId(mergeModel.getFile2Model().getId(), connection));
	        	  
	        	  //Retrieve the datatable data
	        	  DatatablesCRUD datatablesCRUD = new DatatablesCRUD();
	        	  mergeModel.setDatatable1Model(datatablesCRUD.retrieveDatatableAsDatatableModelUsingFileId(mergeModel.getFile1Model().getId(), connection));
	        	  mergeModel.setDatatable2Model(datatablesCRUD.retrieveDatatableAsDatatableModelUsingFileId(mergeModel.getFile2Model().getId(), connection));
	        	  
	        	  //Add the merge-specific datatable data from the merge data to the datatable models
	        	  mergeModel.getDatatable1Model().setDuplicateKeysCount(resultSet.getInt("datatable_1_duplicate_keys_count"));
	        	  mergeModel.getDatatable2Model().setDuplicateKeysCount(resultSet.getInt("datatable_2_duplicate_keys_count"));
	        	  
	        	  ////Retrieve the joins data
	        	  JoinsCRUD joinsCRUD = new JoinsCRUD();
	        	  joinsCRUD.setDatabaseModel(this.getDatabaseModel());
	        	  mergeModel.setJoinsModel(joinsCRUD.retrieveJoinsAsJoinsModelByMergeId(mergeModel.getId(), connection));
	        	  
	        	  //FIXME: This seems wrong. Refactor to joinsModel.setDataAsCachedRowSet or review naming.
	        	  mergeModel.setJoinsAsCachedRowSet(joinsCRUD.retrieveJoinsAsCachedRowSetUsingMergeIdAndUserId(mergeModel.getId(), userId, connection));
	        	  
	        	  //Retrieve the join-specific datatable data from the db and add to the datatable models.
	        	  mergeModel.getDatatable1Model().setKeyColumnNamesAsStringList(joinsCRUD.retrieveDatatable1KeyColumnNamesAsStringListByMergeId(mergeModel.getId(), connection));
	        	  mergeModel.getDatatable2Model().setKeyColumnNamesAsStringList(joinsCRUD.retrieveDatatable2KeyColumnNamesAsStringListByMergeId(mergeModel.getId(), connection));
	        	  
	        	  //Retrieve the joined keytable data
	        	  JoinedKeytablesCRUD joinedKeytablesModel = new JoinedKeytablesCRUD();
	        	  mergeModel.setJoinedKeytableModel(joinedKeytablesModel.retrieveJoinedKeytableAsJoinedKeytableModelUsingMergeId(mergeModel.getId(), connection));
	        	  
	        	  //TODO: Retrieve joined datatable data?
	        	  JoinedDatatablesCRUD joinedDatatablesModel = new JoinedDatatablesCRUD();
	        	  mergeModel.setJoinedDatatableModel(joinedDatatablesModel.retrieveJoinedDatatableAsJoinedDatatableModelUsingMergeId(mergeModel.getId(), connection));
	        	  
	        	  
	        	  
	      	  } else {
	      		  
	      		  //TODO:
	      		  //System.out.println("No merge found with the specified id.");
	      		  
	      	  }
	          
	          resultSet.close();
	          
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
		return mergeModel;
	}


	public MergeModel retrieveMergeAsMergeModelThroughCreatingMergeUsingMergeModelAndUserId(
			MergeModel mergeModel, Integer userId) {

		try {
			
			Connection connection = this.getDatabaseModel().getNewConnection();
			
			if (connection != null) {
		
			
			      try {
			    	  

			  		  // Retrieve the merge's file models from the db.
					  FilesCRUD filesCRUD = new FilesCRUD();
					  filesCRUD.setDatabaseModel(this.getDatabaseModel());
					  mergeModel.setFile1Model(filesCRUD.retrieveFileAsFileModelByFileId(mergeModel.getFile1Model().getId(), connection));
					  mergeModel.setFile2Model(filesCRUD.retrieveFileAsFileModelByFileId(mergeModel.getFile2Model().getId(), connection));

			    	  // If the files in the provided model actually exist
			    	  if (mergeModel.getFile1Model().getFilepath() != null && mergeModel.getFile1Model().getFilepath() != null) {
			    		  
						//Insert the merge record
						PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO merge (file_1_id, file_2_id, created_by_user_id, created_datetime, updated_datetime) VALUES (?, ?, ?, NOW(), NOW());");
						preparedStatement.setInt(1, mergeModel.getFile1Model().getId());
						preparedStatement.setInt(2, mergeModel.getFile2Model().getId());
						preparedStatement.setInt(3, mergeModel.getCreatedByUserModel().getId());
						preparedStatement.executeUpdate();
						preparedStatement.close();
						
						
						// Get the model Id (the last insert id)
						DatabasesCRUD databasesCRUD = new DatabasesCRUD();
						mergeModel.setId(databasesCRUD.retrieveLastInsertIdAsIntegerUsingConnection(connection));
						
						
						// Create datatables if necessary.
						mergeModel = this.retrieveMergeAsMergeModelThroughCreatingUncreatedDatatablesUsingMergeModel(mergeModel, connection);
						
						if (mergeModel.getDatatable1Model().getName() != null && mergeModel.getDatatable2Model().getName() != null) {
							
							// Guess joins automatically.
							MergeScripts mergeScripts = new MergeScripts();
							mergeModel = mergeScripts.retrieveMergeAsMergeModelThroughGuessingJoinsUsingMergeModel(mergeModel, connection);
	
							
							// Guess keys automatically.
							mergeModel = mergeScripts.retrieveMergeAsMergeModelThroughGuessingKeysUsingMergeModelAndUserId(mergeModel, userId, connection);
							
							
							// Count the duplicate keys in each datatable (according to the join) and the total.
							mergeModel = this.retrieveMergeAsMergeModelThroughCountingDuplicateKeysUsingMergeModel(mergeModel, connection);
							
							
							//this.logger.info("keysCount=" + mergeModel.getJoinsModel().getKeysCount());
							//this.logger.info("totalDuplicateKeysCount=" + mergeModel.getTotalDuplicateKeysCount());
							
							// Count the data conflicts (if there are keys and no duplicates)
							
							mergeModel = mergeScripts.retrieveMergeAsMergeModelThroughDeterminingDataConflictsUsingMergeModel(mergeModel, connection);
							
						
						} else {
							logger.severe("mergeModel.getDatatable1Model().getName() or mergeModel.getDatatable2Model().getName() is null");
						}
						
						
						
					} else {
						this.logger.severe("Did not retrieve uploads by the specified upload Ids in the merge model.");
					}
					  
					  
					  
					  
					  
			          
				      //End of merge algorithm
				      
	
			        }
			        catch(SQLException sqlException){
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
			//System.out.println("Exception from createMerge.");
			e.printStackTrace();
		}
    	
		
		
		return mergeModel;
	}



	public MergeModel retrieveMergeAsMergeModelThroughCountingDuplicateKeysUsingMergeModel(
			MergeModel mergeModel, Connection connection) {
		
		MergeScripts mergeScripts = new MergeScripts();
		
		//TODO: Merge 1 + 2
		mergeModel = mergeScripts
				.retrieveMergeAsMergeModelThroughDeterminingDatatable1DuplicateKeysCountUsingMergeModel(
						mergeModel, connection);
		
		mergeModel = mergeScripts
				.retrieveMergeAsMergeModelThroughDeterminingDatatable2DuplicateKeysCountUsingMergeModel(
						mergeModel, connection);
		
		// Add up the duplicate keys
		if (mergeModel.getDatatable1Model()
				.getDuplicateKeysCount() != null
				&& mergeModel.getDatatable2Model()
						.getDuplicateKeysCount() != null) {
			
			mergeModel.setTotalDuplicateKeysCount(mergeModel
					.getDatatable1Model()
					.getDuplicateKeysCount()
					+ mergeModel.getDatatable2Model()
							.getDuplicateKeysCount());
			
		} else {
			mergeModel.setTotalDuplicateKeysCount(null);
		}
		
		
		//Save the result back to the database
		MergesCRUD mergesModel = new MergesCRUD();
		mergesModel.updateMergeUsingMergeModel(mergeModel,connection);
		
		// Unnecessary if model is already retrieved and set above.
		// Get the data back from the db.
		//mergeModel = mergesModel.retrieveMergeAsMergeModelByMergeId(mergeModel.getId(), connection);
		
		return mergeModel;
	}



	public MergeModel retrieveMergeAsMergeModelThroughCreatingUncreatedDatatablesUsingMergeModel(MergeModel mergeModel,
			Connection connection) {

		  DatatablesCRUD datatablesCRUD = new DatatablesCRUD();
		  datatablesCRUD.setDatabaseModel(this.getDatabaseModel());

		  mergeModel.setDatatable1Model(datatablesCRUD.retrieveDatatableAsDatatableModelUsingFileId(mergeModel.getFile1Model().getId(), connection));
		  mergeModel.setDatatable2Model(datatablesCRUD.retrieveDatatableAsDatatableModelUsingFileId(mergeModel.getFile2Model().getId(), connection));
		  
		  if (mergeModel.getDatatable1Model().getName() == null) {
			  mergeModel.setDatatable1Model(datatablesCRUD.retrieveDatatableAsDatatableModelThroughCreatingDatatableUsingFileId(mergeModel.getFile1Model().getId(), connection));
		  }

		  if (mergeModel.getDatatable2Model().getName() == null) {
			  mergeModel.setDatatable2Model(datatablesCRUD.retrieveDatatableAsDatatableModelThroughCreatingDatatableUsingFileId(mergeModel.getFile2Model().getId(), connection));
		  }
		  
		  return mergeModel;
	}

	public MergeModel retrieveMergeAsMergeModelThroughRecreatingJoinedKeytableUsingMergeModel(MergeModel mergeModel,
			Connection connection) {

		  JoinedKeytablesCRUD joinedKeytablesModel = new JoinedKeytablesCRUD();

		  
		  //this.logger.info("1 Joined Keytable Name=" + mergeModel.getJoinedKeytableModel().getName());
		  
		  mergeModel.setJoinedKeytableModel(joinedKeytablesModel.retrieveJoinedKeytableAsJoinedKeytableModelUsingMergeId(mergeModel.getId(), connection));

		  //this.logger.info("2 Joined Keytable Name=" + mergeModel.getJoinedKeytableModel().getName());
		  
		  if (mergeModel.getJoinedKeytableModel().getName() != null) {

			  // Erase the old joined keytable
			  joinedKeytablesModel.dropJoinedKeytableUsingName(mergeModel.getJoinedKeytableModel().getName(), connection);
			  
			  mergeModel.getJoinedKeytableModel().setName(null);
			  
			  MergesCRUD mergesModel = new MergesCRUD();
			  mergesModel.updateMergeUsingMergeModel(mergeModel, connection);
			  
		  }
		  
		  mergeModel.setJoinedKeytableModel(joinedKeytablesModel.retrieveJoinedKeytableAsJoinedKeytableModelThroughCreatingJoinedKeytableUsingMergeModel(mergeModel, connection));
		  
		  return mergeModel;
	}
	

	public void updateMergeUsingMergeModel(MergeModel mergeModel,
			Connection connection) {
		
	      try {

	          PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `merge` SET file_1_id = ?, file_2_id = ?, updated_datetime = NOW(), datatable_1_duplicate_keys_count = ?, datatable_2_duplicate_keys_count = ?, total_duplicate_keys_count = ?, total_conflicts_count = ?, joined_keytable_name = ? WHERE id = ?;");
	          preparedStatement.setInt(1, mergeModel.getFile1Model().getId());
	          preparedStatement.setInt(2, mergeModel.getFile2Model().getId());
	          if (mergeModel.getDatatable1Model().getDuplicateKeysCount() != null) {
	        	  preparedStatement.setInt(3, mergeModel.getDatatable1Model().getDuplicateKeysCount());
	        	  
	          } else {
	        	  preparedStatement.setNull(3, java.sql.Types.INTEGER);
	        	  
	          }
	          if (mergeModel.getDatatable2Model().getDuplicateKeysCount() != null) {
	        	  preparedStatement.setInt(4, mergeModel.getDatatable2Model().getDuplicateKeysCount());
	        	  
	        	
	          } else {
	        	  preparedStatement.setNull(4, java.sql.Types.INTEGER);
	        	  
	          }
	          if (mergeModel.getTotalDuplicateKeysCount() != null) {
	        	  preparedStatement.setInt(5, mergeModel.getTotalDuplicateKeysCount());
	          } else {
	        	  preparedStatement.setNull(5, java.sql.Types.INTEGER);
	          }
	          
	          if (mergeModel.getTotalConflictsCount() != null) {
	        	  preparedStatement.setInt(6, mergeModel.getTotalConflictsCount());
	          } else {
	        	  preparedStatement.setNull(6, java.sql.Types.INTEGER);
	          }
	          
	          if (mergeModel.getJoinedKeytableModel().getName() != null) {
	        	  preparedStatement.setString(7, mergeModel.getJoinedKeytableModel().getName());
	          } else {
	        	  preparedStatement.setNull(7, java.sql.Types.VARCHAR);
	          }
	          
	          preparedStatement.setInt(8, mergeModel.getId());
	          
	          preparedStatement.executeUpdate();
	          preparedStatement.close();
	          
	
	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
	}
	

		public String retrieveMergesAsDecoratedXHTMLTableUsingUserId (Integer userId) {
			
			String mergesAsDecoratedXHTMLTable = null;
			
			  CachedRowSet mergesAsCachedRowSet = this.retrieveMergesAsCachedRowSetUsingUserId(userId);

			  if (mergesAsCachedRowSet != null) {

				  	MergesFunctions mergesFunctions = new MergesFunctions();
				  	mergesAsDecoratedXHTMLTable = mergesFunctions.getMergesAsDecoratedXHTMLTableUsingMergesAsCachedRowSet(mergesAsCachedRowSet);
				    
			  } else {

				  this.logger.warning("Error: mergesAsCachedRowSet is null");
				  mergesAsDecoratedXHTMLTable = "<p>Error: mergesAsCachedRowSet is null</p>";
				  
			  }
			
			return mergesAsDecoratedXHTMLTable;
		}
	
	public CachedRowSet retrieveMergesAsCachedRowSetUsingUserId(Integer userId) {
		
		CachedRowSet mergesAsCachedRowSet = null;
		
		UserModel userModel = new UserModel();
		userModel.setId(userId);
		
		   String CACHED_ROW_SET_IMPL_CLASS = "com.sun.rowset.CachedRowSetImpl";
		   
			try {

				Connection connection = this.getDatabaseModel().getNewConnection();
				 
				if (connection != null) {
				
					 //FIXME: Apparently a bug in CachedRowSet using getX('columnAlias') aka columnLabel, which actually only works with getX('columnName'), so using getX('columnIndex').
					 
					
					//FIXME: The joined_datatable_name is being used instead of a hasAtLeastOneKey (is a valid join)
					
				      try{
				          PreparedStatement preparedStatement = connection.prepareStatement(
				        		"SELECT merge.id, file_1.id AS file_1_id, file_1.filename AS file_1_filename, file_2.id AS 'file_2_id', file_2.filename AS 'file_2_filename', merge.created_datetime, merge.updated_datetime, merge.total_duplicate_keys_count, merge.total_conflicts_count, merge.joined_keytable_name " +
				          		"FROM merge " +
				          		"INNER JOIN file AS file_1 ON file_1.id = merge.file_1_id " +
				          		"INNER JOIN file AS file_2 ON file_2.id = merge.file_2_id " +
				          		"WHERE merge.created_by_user_id = ? " +
				          		"ORDER BY merge.id;");
				          preparedStatement.setInt(1, userModel.getId());
				          preparedStatement.executeQuery();
				          Class<?> cachedRowSetImplClass = Class.forName(CACHED_ROW_SET_IMPL_CLASS);
				          mergesAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
				          mergesAsCachedRowSet.populate(preparedStatement.getResultSet());
				          preparedStatement.close();
	
				        }
				        catch(SQLException sqlException){
					    	sqlException.printStackTrace();
				        } finally {
							
							try {
								connection.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							
						} 	
					
				} else {
					
					logger.severe("connection is null");
				}
					
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
	
	
	
	     return mergesAsCachedRowSet;
	}

	
	public String retrieveJoinsAsDecoratedXHTMLTableUsingMergeModel(MergeModel mergeModel) {
		
		String joinsAsDecoratedXHTMLTable = "";
		
		  CachedRowSet joinsAsCachedRowSet = mergeModel.getJoinsModel().getJoinsAsCachedRowSet();
			
		  if (joinsAsCachedRowSet != null) {
		
			  MergeFunctions mergeFunctions = new MergeFunctions();
			  
			    mergeFunctions.setMergeModel(mergeModel);	    
			    mergeFunctions.setJoinsAsCachedRowSet(joinsAsCachedRowSet);
			    mergeFunctions.setJoinsAsDecoratedXHTMLTableUsingJoinsAsCachedRowSet();
			    joinsAsDecoratedXHTMLTable = mergeFunctions.getJoinsAsDecoratedXHTMLTable();
			    
		  } else {
			  
			  //TODO: Error handling
			  this.logger.warning("Error: joinsAsCachedRowSet is null");
			  joinsAsDecoratedXHTMLTable = "<p>Error: joinsAsCachedRowSet is null</p>";
			  
		  }
		
		return joinsAsDecoratedXHTMLTable;
	}

	public String retrieveNewJoinAsDecoratedXHTMLTableUsingMergeModel (MergeModel mergeModel) {
		
		String newJoinAsDecoratedXHTMLTable = "";
		
		JoinFunctions joinFunctions = new JoinFunctions();
		joinFunctions.setJoinModel(new JoinModel()); // Unnecessary but explicit
		joinFunctions.setMergeModel(mergeModel);
		joinFunctions.setJoinAsDecoratedXHTMLTableByJoinModel();
		
		newJoinAsDecoratedXHTMLTable = joinFunctions.getJoinAsDecoratedXHTMLTable();
	
		return newJoinAsDecoratedXHTMLTable;
	}

	public void updateTotalConflictsCountUsingMergeModel(MergeModel mergeModel,
			Connection connection) {
		
	      try {

	          PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `merge` SET total_conflicts_count = ?, updated_datetime = NOW() WHERE id = ?;");

	          if (mergeModel.getTotalConflictsCount() != null) {
	        	  preparedStatement.setInt(1, mergeModel.getTotalConflictsCount());
	          } else {
	        	  preparedStatement.setNull(1, java.sql.Types.INTEGER);
	          }

	          preparedStatement.setInt(2, mergeModel.getId());
	          
	          preparedStatement.executeUpdate();
	          preparedStatement.close();
	          
	
	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
	        
	}

	public void updateMergeJoinedDatatableUsingMergeModel(
			MergeModel mergeModel, Connection connection) {
	      try {

	          PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `merge` SET joined_datatable_name = ?, updated_datetime = NOW() WHERE id = ?;");

	          if (mergeModel.getTotalConflictsCount() != null) {
	        	  preparedStatement.setString(1, mergeModel.getJoinedDatatableModel().getName());
	          } else {
	        	  preparedStatement.setNull(1, java.sql.Types.INTEGER);
	          }

	          preparedStatement.setInt(2, mergeModel.getId());
	          
	          preparedStatement.executeUpdate();
	          preparedStatement.close();
	          
	
	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
	}

	public boolean deleteMergeUsingMergeIdAndUserId(Integer mergeId, Integer userId) {
		
		Boolean success = null;
		
		Connection connection = this.getDatabaseModel().getNewConnection();
		 
		if (connection != null) {
			
				try {
					
		          PreparedStatement preparedStatement = connection.prepareStatement(
		        		  "DELETE FROM merge WHERE id = ? AND created_by_user_id = ?;");
		          preparedStatement.setInt(1, mergeId);
		          preparedStatement.setInt(2, userId);
		          preparedStatement.executeUpdate();
		          preparedStatement.close();
		          
		          PreparedStatement preparedStatement1 = connection.prepareStatement(
        		  		"DROP TABLE IF EXISTS `joined_keytable_" + mergeId + "`;");
		          preparedStatement1.executeUpdate();
		          preparedStatement1.close();
          
		          PreparedStatement preparedStatement2 = connection.prepareStatement(
		        		  "DROP TABLE IF EXISTS `joined_datatable_" + mergeId + "`;");
		          preparedStatement2.executeUpdate();
		          preparedStatement2.close();
		          
		          PreparedStatement preparedStatement3 = connection.prepareStatement(
		        		  "DROP TABLE IF EXISTS `merged_datatable_" + mergeId + "`;");
		          preparedStatement3.executeUpdate();
		          preparedStatement3.close();
		          
		          success = true;

		        } catch (SQLException e) {
					e.printStackTrace();
					success = false;
					
				} finally {
			
					try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
				}
			
		} else {
			
			logger.severe("connection is null");
			
			success = false;
		}

		return success;
	}
}
