package org.cggh.tools.dataMerger.data.merges;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.DataModel;
import org.cggh.tools.dataMerger.data.datatables.DatatablesModel;
import org.cggh.tools.dataMerger.data.joinedDatatables.JoinedDatatablesModel;
import org.cggh.tools.dataMerger.data.joinedKeytables.JoinedKeytablesModel;
import org.cggh.tools.dataMerger.data.joins.JoinModel;
import org.cggh.tools.dataMerger.data.joins.JoinsModel;
import org.cggh.tools.dataMerger.data.uploads.UploadsModel;
import org.cggh.tools.dataMerger.data.users.UserModel;
import org.cggh.tools.dataMerger.functions.joins.JoinFunctionsModel;
import org.cggh.tools.dataMerger.functions.merges.MergeFunctionsModel;
import org.cggh.tools.dataMerger.functions.merges.MergesFunctionsModel;
import org.cggh.tools.dataMerger.scripts.merges.MergeScriptsModel;


public class MergesModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1917678012559065867L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.merges");
	
	
	private DataModel dataModel;
	private UserModel userModel;
	
	
	public MergesModel() {

		this.setDataModel(new DataModel());
		this.setUserModel(new UserModel());			
	
		
	}

    public void setDataModel (final DataModel dataModel) {
        this.dataModel  = dataModel;
    }
    public DataModel getDataModel () {
        return this.dataModel;
    } 
	
    public void setUserModel (final UserModel userModel) {
        this.userModel  = userModel;
    }
    public UserModel getUserModel () {
        return this.userModel;
    } 

    

    public MergeModel retrieveMergeAsMergeModelByMergeId(Integer mergeId) {
    	
    	MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection connection = this.getDataModel().getNewConnection();
			 
			if (!connection.isClosed()) {		

				mergeModel = retrieveMergeAsMergeModelByMergeId(mergeModel.getId(), connection);
				
				connection.close();
				
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


	public MergeModel retrieveMergeAsMergeModelByMergeId(Integer mergeId, Connection connection) {

		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);
		
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, upload_1_id, upload_2_id, created_by_user_id, created_datetime, updated_datetime, datatable_1_duplicate_keys_count, datatable_2_duplicate_keys_count, total_duplicate_keys_count, total_conflicts_count, joined_keytable_name FROM `merge` " + 
	        		  "WHERE id = ? ORDER BY id;");
	          preparedStatement.setInt(1, mergeModel.getId());
	          preparedStatement.executeQuery();
	          ResultSet resultSet = preparedStatement.getResultSet();
	          
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();

	        	  //Set the merge data
	        	  mergeModel.getUpload1Model().setId(resultSet.getInt("upload_1_id"));
	        	  mergeModel.getUpload2Model().setId(resultSet.getInt("upload_2_id"));
	        	  mergeModel.getCreatedByUserModel().setId(resultSet.getInt("created_by_user_id"));
	        	  mergeModel.setCreatedDatetime(resultSet.getTimestamp("created_datetime"));
	        	  mergeModel.setUpdatedDatetime(resultSet.getTimestamp("updated_datetime"));
	        	  mergeModel.setTotalDuplicateKeysCount(resultSet.getInt("total_duplicate_keys_count"));
	        	  mergeModel.setTotalConflictsCount(resultSet.getInt("total_conflicts_count"));
	        	  
	        	  //Done further down
	        	  //mergeModel.getJoinedKeytableModel().setName(resultSet.getString("joined_keytable_name"));
	        	  
	        	  //Retrieve the upload data
	        	  UploadsModel uploadsModel = new UploadsModel();
	        	  uploadsModel.setDataModel(this.getDataModel());
	        	  mergeModel.setUpload1Model(uploadsModel.retrieveUploadAsUploadModelByUploadId(mergeModel.getUpload1Model().getId(), connection));
	        	  mergeModel.setUpload2Model(uploadsModel.retrieveUploadAsUploadModelByUploadId(mergeModel.getUpload2Model().getId(), connection));
	        	  
	        	  //Retrieve the datatable data
	        	  DatatablesModel datatablesModel = new DatatablesModel();
	        	  datatablesModel.setDataModel(this.getDataModel());
	        	  mergeModel.setDatatable1Model(datatablesModel.retrieveDatatableAsDatatableModelUsingUploadId(mergeModel.getUpload1Model().getId(), connection));
	        	  mergeModel.setDatatable2Model(datatablesModel.retrieveDatatableAsDatatableModelUsingUploadId(mergeModel.getUpload2Model().getId(), connection));
	        	  
	        	  //Add the merge-specific datatable data from the merge data to the datatable models
	        	  mergeModel.getDatatable1Model().setDuplicateKeysCount(resultSet.getInt("datatable_1_duplicate_keys_count"));
	        	  mergeModel.getDatatable2Model().setDuplicateKeysCount(resultSet.getInt("datatable_2_duplicate_keys_count"));
	        	  
	        	  ////Retrieve the joins data
	        	  JoinsModel joinsModel = new JoinsModel();
	        	  joinsModel.setDataModel(this.getDataModel());
	        	  mergeModel.setJoinsModel(joinsModel.retrieveJoinsAsJoinsModelByMergeId(mergeModel.getId(), connection));
	        	  
	        	  //FIXME: This seems wrong. Refactor to joinsModel.setDataAsCachedRowSet or review naming.
	        	  mergeModel.setJoinsAsCachedRowSet(joinsModel.retrieveJoinsAsCachedRowSetByMergeId(mergeModel.getId(), connection));
	        	  
	        	  //Retrieve the join-specific datatable data from the db and add to the datatable models.
	        	  mergeModel.getDatatable1Model().setKeyColumnNamesAsStringList(joinsModel.retrieveDatatable1KeyColumnNamesAsStringListByMergeId(mergeModel.getId(), connection));
	        	  mergeModel.getDatatable2Model().setKeyColumnNamesAsStringList(joinsModel.retrieveDatatable2KeyColumnNamesAsStringListByMergeId(mergeModel.getId(), connection));
	        	  
	        	  //Retrieve the joined keytable data
	        	  JoinedKeytablesModel joinedKeytablesModel = new JoinedKeytablesModel();
	        	  mergeModel.setJoinedKeytableModel(joinedKeytablesModel.retrieveJoinedKeytableAsJoinedKeytableModelUsingMergeId(mergeModel.getId(), connection));
	        	  
	        	  //TODO: Retrieve joined datatable data?
	        	  JoinedDatatablesModel joinedDatatablesModel = new JoinedDatatablesModel();
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


	public MergeModel retrieveMergeAsMergeModelThroughCreatingMergeUsingMergeModel(
			MergeModel mergeModel) {

		
		

		try {
			
			Connection connection = this.getDataModel().getNewConnection();
			
			if (!connection.isClosed()) {
		
			
			      try {
			    	  

			  		  // Retrieve the merge's upload models from the db.
					  UploadsModel uploadsModel = new UploadsModel();
					  mergeModel.setUpload1Model(uploadsModel.retrieveUploadAsUploadModelByUploadId(mergeModel.getUpload1Model().getId(), connection));
					  mergeModel.setUpload2Model(uploadsModel.retrieveUploadAsUploadModelByUploadId(mergeModel.getUpload2Model().getId(), connection));

			    	  // If the uploads in the provided model actually exist
			    	  if (mergeModel.getUpload1Model().getRepositoryFilepath() != null && mergeModel.getUpload1Model().getRepositoryFilepath() != null) {
			    		  
						//Insert the merge record
						PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO merge (upload_1_id, upload_2_id, created_by_user_id, created_datetime, updated_datetime) VALUES (?, ?, ?, NOW(), NOW());");
						preparedStatement.setInt(1, mergeModel.getUpload1Model().getId());
						preparedStatement.setInt(2, mergeModel.getUpload2Model().getId());
						preparedStatement.setInt(3, this.getUserModel().getId());
						preparedStatement.executeUpdate();
						preparedStatement.close();
						
						
						// Get the model Id (the last insert id)
						mergeModel.setId(this.getDataModel().retrieveLastInsertIdAsIntegerUsingConnection(connection));
						
						
						// Create datatables if necessary.
						mergeModel = this.retrieveMergeAsMergeModelThroughCreatingUncreatedDatatablesUsingMergeModel(mergeModel, connection);
						
						
						// Guess joins automatically.
						MergeScriptsModel mergeScriptsModel = new MergeScriptsModel();
						mergeModel = mergeScriptsModel.retrieveMergeAsMergeModelThroughGuessingJoinsUsingMergeModel(mergeModel, connection);

						
						// Guess keys automatically.
						mergeModel = mergeScriptsModel.retrieveMergeAsMergeModelThroughGuessingKeysUsingMergeModel(mergeModel, connection);
						
						
						// Count the duplicate keys in each datatable (according to the join) and the total.
						mergeModel = this.retrieveMergeAsMergeModelThroughCountingDuplicateKeysUsingMergeModel(mergeModel, connection);
						
						
						//this.logger.info("keysCount=" + mergeModel.getJoinsModel().getKeysCount());
						//this.logger.info("totalDuplicateKeysCount=" + mergeModel.getTotalDuplicateKeysCount());
						
						// Count the data conflicts (if there are keys and no duplicates)
						
						mergeModel = mergeScriptsModel.retrieveMergeAsMergeModelThroughDeterminingDataConflictsUsingMergeModel(mergeModel, connection);
						
						
						
						
						
						
					} else {
						this.logger.severe("Did not retrieve uploads by the specified upload Ids in the merge model.");
					}
					  
					  
					  
					  
					  
			          
				      //End of merge algorithm
				      
	
			        }
			        catch(SQLException sqlException){
				    	sqlException.printStackTrace();
			        } 			

						        
						        
						
						        
			
				connection.close();
				
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
		
		MergeScriptsModel mergeScriptsModel = new MergeScriptsModel();
		
		//TODO: Merge 1 + 2
		mergeModel = mergeScriptsModel
				.retrieveMergeAsMergeModelThroughDeterminingDatatable1DuplicateKeysCountUsingMergeModel(
						mergeModel, connection);
		
		mergeModel = mergeScriptsModel
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
		MergesModel mergesModel = new MergesModel();
		mergesModel.updateMergeUsingMergeModel(mergeModel,connection);
		
		// Unnecessary if model is already retrieved and set above.
		// Get the data back from the db.
		//mergeModel = mergesModel.retrieveMergeAsMergeModelByMergeId(mergeModel.getId(), connection);
		
		return mergeModel;
	}



	public MergeModel retrieveMergeAsMergeModelThroughCreatingUncreatedDatatablesUsingMergeModel(MergeModel mergeModel,
			Connection connection) {

		  DatatablesModel datatablesModel = new DatatablesModel();

		  mergeModel.setDatatable1Model(datatablesModel.retrieveDatatableAsDatatableModelUsingUploadId(mergeModel.getUpload1Model().getId(), connection));
		  mergeModel.setDatatable2Model(datatablesModel.retrieveDatatableAsDatatableModelUsingUploadId(mergeModel.getUpload2Model().getId(), connection));
		  
		  if (mergeModel.getDatatable1Model().getId() == null) {
			  mergeModel.setDatatable1Model(datatablesModel.retrieveDatatableAsDatatableModelThroughCreatingDatatableUsingUploadId(mergeModel.getUpload1Model().getId(), connection));
		  }

		  if (mergeModel.getDatatable2Model().getId() == null) {
			  mergeModel.setDatatable2Model(datatablesModel.retrieveDatatableAsDatatableModelThroughCreatingDatatableUsingUploadId(mergeModel.getUpload2Model().getId(), connection));
		  }
		  
		  return mergeModel;
	}

	public MergeModel retrieveMergeAsMergeModelThroughRecreatingJoinedKeytableUsingMergeModel(MergeModel mergeModel,
			Connection connection) {

		  JoinedKeytablesModel joinedKeytablesModel = new JoinedKeytablesModel();

		  
		  //this.logger.info("1 Joined Keytable Name=" + mergeModel.getJoinedKeytableModel().getName());
		  
		  mergeModel.setJoinedKeytableModel(joinedKeytablesModel.retrieveJoinedKeytableAsJoinedKeytableModelUsingMergeId(mergeModel.getId(), connection));

		  //this.logger.info("2 Joined Keytable Name=" + mergeModel.getJoinedKeytableModel().getName());
		  
		  if (mergeModel.getJoinedKeytableModel().getName() != null) {

			  // Erase the old joined keytable
			  joinedKeytablesModel.dropJoinedKeytableUsingName(mergeModel.getJoinedKeytableModel().getName(), connection);
			  
			  mergeModel.getJoinedKeytableModel().setName(null);
			  
			  MergesModel mergesModel = new MergesModel();
			  mergesModel.updateMergeUsingMergeModel(mergeModel, connection);
			  
		  }
		  
		  mergeModel.setJoinedKeytableModel(joinedKeytablesModel.retrieveJoinedKeytableAsJoinedKeytableModelThroughCreatingJoinedKeytableUsingMergeModel(mergeModel, connection));
		  
		  return mergeModel;
	}
	

	public void updateMergeUsingMergeModel(MergeModel mergeModel,
			Connection connection) {
		
	      try {

	          PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `merge` SET upload_1_id = ?, upload_2_id = ?, updated_datetime = NOW(), datatable_1_duplicate_keys_count = ?, datatable_2_duplicate_keys_count = ?, total_duplicate_keys_count = ?, total_conflicts_count = ?, joined_keytable_name = ? WHERE id = ?;");
	          preparedStatement.setInt(1, mergeModel.getUpload1Model().getId());
	          preparedStatement.setInt(2, mergeModel.getUpload2Model().getId());
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
	

		public String retrieveMergesAsDecoratedXHTMLTableUsingMergesModel (MergesModel mergesModel) {
			
			String mergesAsDecoratedXHTMLTableUsingMergesModel = null;
			
			  CachedRowSet mergesAsCachedRowSet = this.retrieveMergesAsCachedRowSetUsingUserId(mergesModel.getUserModel().getId());

			  if (mergesAsCachedRowSet != null) {

				  	MergesFunctionsModel mergesFunctionsModel = new MergesFunctionsModel();
				  	mergesFunctionsModel.setMergesAsCachedRowSet(mergesAsCachedRowSet);
				  	mergesFunctionsModel.setMergesAsDecoratedXHTMLTableUsingMergesAsCachedRowSet();
				  	mergesAsDecoratedXHTMLTableUsingMergesModel = mergesFunctionsModel.getMergesAsDecoratedXHTMLTable();
				    
			  } else {
				  
				  //TODO: Error handling
				  this.logger.warning("Error: mergesAsCachedRowSet is null");
				  mergesAsDecoratedXHTMLTableUsingMergesModel = "<p>Error: mergesAsCachedRowSet is null</p>";
				  
			  }
			
			return mergesAsDecoratedXHTMLTableUsingMergesModel;
		}
	
	public CachedRowSet retrieveMergesAsCachedRowSetUsingUserId(Integer userId) {
		
		CachedRowSet mergesAsCachedRowSet = null;
		
		UserModel userModel = new UserModel();
		userModel.setId(userId);
		
		   String CACHED_ROW_SET_IMPL_CLASS = "com.sun.rowset.CachedRowSetImpl";
		   
			try {
				
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				
				Connection connection = this.getDataModel().getNewConnection();
				 
				if (!connection.isClosed()) {
				
					 //FIXME: Apparently a bug in CachedRowSet using getX('columnAlias') aka columnLabel, which actually only works with getX('columnName'), so using getX('columnIndex').
					 
					
				      try{
				          PreparedStatement preparedStatement = connection.prepareStatement("SELECT merge.id, upload_1.id AS upload_1_id, upload_1.original_filename AS upload_1_original_filename, upload_2.id AS 'upload_2_id', upload_2.original_filename AS 'upload_2_original_filename', merge.created_datetime, merge.updated_datetime, merge.total_duplicate_keys_count, merge.total_conflicts_count FROM merge INNER JOIN upload AS upload_1 ON upload_1.id = merge.upload_1_id INNER JOIN upload AS upload_2 ON upload_2.id = merge.upload_2_id WHERE merge.created_by_user_id = ? ORDER BY merge.id;");
				          preparedStatement.setInt(1, userModel.getId());
				          preparedStatement.executeQuery();
				          Class<?> cachedRowSetImplClass = Class.forName(CACHED_ROW_SET_IMPL_CLASS);
				          mergesAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
				          mergesAsCachedRowSet.populate(preparedStatement.getResultSet());
				          preparedStatement.close();
	
				        }
				        catch(SQLException sqlException){
				        	//System.out.println("<p>" + sqlException + "</p>");
					    	sqlException.printStackTrace();
				        } 	
				
					connection.close();
					
				} else {
					
					//System.out.println("connection.isClosed");
				}
					
			} 
			catch (Exception e) {
				//System.out.println("Exception from getMergesAsCachedRowSet.");
				e.printStackTrace();
			}
	
	
	
	     return mergesAsCachedRowSet;
	}

	
	public String retrieveJoinsAsDecoratedXHTMLTableUsingMergeModel(MergeModel mergeModel) {
		
		String joinsAsDecoratedXHTMLTable = "";
		
		  CachedRowSet joinsAsCachedRowSet = mergeModel.getJoinsModel().getJoinsAsCachedRowSet();
			
		  if (joinsAsCachedRowSet != null) {
		
			  MergeFunctionsModel mergeFunctionsModel = new MergeFunctionsModel();
			  
			    mergeFunctionsModel.setMergeModel(mergeModel);	    
			    mergeFunctionsModel.setJoinsAsCachedRowSet(joinsAsCachedRowSet);
			    mergeFunctionsModel.setJoinsAsDecoratedXHTMLTableUsingJoinsAsCachedRowSet();
			    joinsAsDecoratedXHTMLTable = mergeFunctionsModel.getJoinsAsDecoratedXHTMLTable();
			    
		  } else {
			  
			  //TODO: Error handling
			  this.logger.warning("Error: joinsAsCachedRowSet is null");
			  joinsAsDecoratedXHTMLTable = "<p>Error: joinsAsCachedRowSet is null</p>";
			  
		  }
		
		return joinsAsDecoratedXHTMLTable;
	}

	public String retrieveNewJoinAsDecoratedXHTMLTableUsingMergeModel (MergeModel mergeModel) {
		
		String newJoinAsDecoratedXHTMLTable = "";
		
		JoinFunctionsModel joinFunctionsModel = new JoinFunctionsModel();
		joinFunctionsModel.setJoinModel(new JoinModel()); // Unnecessary but explicit
		joinFunctionsModel.setMergeModel(mergeModel);
		joinFunctionsModel.setJoinAsDecoratedXHTMLTableByJoinModel();
		
		newJoinAsDecoratedXHTMLTable = joinFunctionsModel.getJoinAsDecoratedXHTMLTable();
	
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
}
