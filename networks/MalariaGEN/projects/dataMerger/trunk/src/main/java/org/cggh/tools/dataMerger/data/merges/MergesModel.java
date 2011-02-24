package org.cggh.tools.dataMerger.data.merges;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.DataModel;
import org.cggh.tools.dataMerger.data.datatables.DatatablesModel;
import org.cggh.tools.dataMerger.data.joins.JoinModel;
import org.cggh.tools.dataMerger.data.joins.JoinsModel;
import org.cggh.tools.dataMerger.data.uploads.UploadsModel;
import org.cggh.tools.dataMerger.data.users.UserModel;
import org.cggh.tools.dataMerger.scripts.merges.MergeScriptsModel;


public class MergesModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1917678012559065867L;
	
	
	private DataModel dataModel;
	private UserModel userModel;
	
	private MergeModel mergeModel;
	private DatatablesModel datatablesModel;
	
	
	public MergesModel() {

		this.setDataModel(new DataModel());
		this.setUserModel(new UserModel());			
		

		this.setMergeModel(new MergeModel());
		this.setDatatablesModel(new DatatablesModel());	
		
		
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


	public void setMergeModel (final MergeModel mergeModel) {
		
		this.mergeModel = mergeModel;
	}
	public MergeModel getMergeModel () {
		
		return this.mergeModel;
	}	    

	


	public MergeModel retrieveMergeAsMergeModelByMergeId(Integer mergeId, Connection connection) {

		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);
		
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, upload_1_id, upload_2_id, created_by_user_id, created_datetime, updated_datetime, datatable_1_duplicate_keys_count, datatable_2_duplicate_keys_count, total_duplicate_keys_count FROM `merge` WHERE id = ?;");
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
	        	  
	        	  //Retrieve the upload data
	        	  UploadsModel uploadsModel = new UploadsModel();
	        	  mergeModel.setUpload1Model(uploadsModel.retrieveUploadAsUploadModelByUploadId(mergeModel.getUpload1Model().getId(), connection));
	        	  mergeModel.setUpload2Model(uploadsModel.retrieveUploadAsUploadModelByUploadId(mergeModel.getUpload2Model().getId(), connection));
	        	  
	        	  //Retrieve the datatable data
	        	  DatatablesModel datatablesModel = new DatatablesModel();
	        	  mergeModel.setDatatable1Model(datatablesModel.retrieveDatatableAsDatatableModelByUploadId(mergeModel.getUpload1Model().getId(), connection));
	        	  mergeModel.setDatatable2Model(datatablesModel.retrieveDatatableAsDatatableModelByUploadId(mergeModel.getUpload2Model().getId(), connection));
	        	  
	        	  //Retrieve the joins data
	        	  JoinsModel joinsModel = new JoinsModel();
	        	  mergeModel.setJoinsAsCachedRowSet(joinsModel.retrieveJoinsAsCachedRowSetByMergeId(mergeModel.getId(), connection));
	        	  
	      	  } else {
	      		  
	      		  //TODO:
	      		  System.out.println("No merge found with the specified id.");
	      		  
	      	  }
	          
	          resultSet.close();
	          
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
		return mergeModel;
	}

	public DatatablesModel getDatatablesModel() {
		return this.datatablesModel;
	}
	public void setDatatablesModel(final DatatablesModel datatablesModel) {
		this.datatablesModel = datatablesModel;
	}
	
	

	public CachedRowSet getMergesAsCachedRowSet() {
	
		  	   
		   String CACHED_ROW_SET_IMPL_CLASS = "com.sun.rowset.CachedRowSetImpl";
		   
		   CachedRowSet mergesAsCachedRowSet = null;
		   
			try {
				
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				
				Connection connection = this.getDataModel().getNewConnection();
				 
				if (!connection.isClosed()) {
				
					
				      try{
				          PreparedStatement preparedStatement = connection.prepareStatement("SELECT merge.id, upload_1.original_filename, upload_2.original_filename, created_datetime, updated_datetime FROM upload INNER JOIN upload AS upload_1 ON upload_1.id = merge.upload_1_id INNER JOIN upload AS upload_2 ON upload_2.id = merge.upload_2_id WHERE created_by_user_id = ?;");
				          preparedStatement.setInt(1, this.userModel.getId());
				          preparedStatement.executeQuery();
				          Class<?> cachedRowSetImplClass = Class.forName(CACHED_ROW_SET_IMPL_CLASS);
				          mergesAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
				          mergesAsCachedRowSet.populate(preparedStatement.getResultSet());
				          preparedStatement.close();
	
				        }
				        catch(SQLException sqlException){
				        	System.out.println("<p>" + sqlException + "</p>");
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
	
	
	
	     return(mergesAsCachedRowSet);
	   }

	public MergeModel retrieveMergeAsMergeModelAfterCreatingMergeUsingMergeModel(
			MergeModel mergeModel) {


		try {
			
			Connection connection = this.getDataModel().getNewConnection();
			
			if (!connection.isClosed()) {
		
			
			      try {
			          PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO merge (upload_1_id, upload_2_id, created_by_user_id, created_datetime, updated_datetime) VALUES (?, ?, ?, NOW(), NOW());");
			          preparedStatement.setInt(1, mergeModel.getUpload1Model().getId());
			          preparedStatement.setInt(2, mergeModel.getUpload2Model().getId());
			          preparedStatement.setInt(3, this.getUserModel().getId());				          
			          preparedStatement.executeUpdate();
			          preparedStatement.close();

			          // Get the model Id
			          mergeModel.setId(this.getDataModel().retrieveLastInsertIdAsIntegerUsingConnection(connection));

				      // Check whether the datatables have already been created and populated.

				      // Re-populate the models with the latest data from the db.
				      
				      UploadsModel uploadsModel = new UploadsModel();
				      mergeModel.setUpload1Model(uploadsModel.retrieveUploadAsUploadModelByUploadId(mergeModel.getUpload1Model().getId(), connection));
				      mergeModel.setUpload2Model(uploadsModel.retrieveUploadAsUploadModelByUploadId(mergeModel.getUpload2Model().getId(), connection));
				      
				      
				      DatatablesModel datatablesModel = new DatatablesModel();
				      
				      // Create datatables if not already created and update the mergeModel with the data.
				      
				      if (!mergeModel.getUpload1Model().isDatatableCreated()) {
				    	  mergeModel.setDatatable1Model(datatablesModel.retrieveDatatableAsDatatableModelAfterCreatingDatatableUsingUploadId(mergeModel.getUpload1Model().getId(), connection));
				      }
 
				      if (!mergeModel.getUpload2Model().isDatatableCreated()) {
				    	  mergeModel.setDatatable2Model(datatablesModel.retrieveDatatableAsDatatableModelAfterCreatingDatatableUsingUploadId(mergeModel.getUpload2Model().getId(), connection));
				      }
			          

				      // Create an auto-join

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
						    			  
						    			  Integer columnNumber = mergeModel.getJoinsModel().getNextColumnNumberByMergeId(mergeModel.getId(), connection);
						    			  Boolean key = false;
						    			  String datatable1ColumnName = datatable1ColumnNamesAsStringList.get(i);
						    			  String datatable2ColumnName = datatable2ColumnNamesAsStringList.get(j);
						    			  String columnName = datatable1ColumnNamesAsStringList.get(i);
						    			  
						    			  mergeModel.getJoinsModel().createJoin(mergeModel.getId(), columnNumber, key, datatable1ColumnName, datatable2ColumnName, columnName, connection);
						    			  
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
					    		  
				    			  Integer columnNumber = mergeModel.getJoinsModel().getNextColumnNumberByMergeId(mergeModel.getId(), connection);
				    			  Boolean key = false;
				    			  String datatable1ColumnName = datatable1ColumnNamesAsStringList.get(i);
				    			  String datatable2ColumnName = null;
				    			  String columnName = datatable1ColumnNamesAsStringList.get(i);
				    			  
				    			  mergeModel.getJoinsModel().createJoin(mergeModel.getId(), columnNumber, key, datatable1ColumnName, datatable2ColumnName, columnName, connection);
				    			   
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
					    		  
				    			  Integer columnNumber = mergeModel.getJoinsModel().getNextColumnNumberByMergeId(mergeModel.getId(), connection);
				    			  Boolean key = false;
				    			  String datatable1ColumnName = null;
				    			  String datatable2ColumnName = datatable2ColumnNamesAsStringList.get(i);
				    			  String columnName = datatable2ColumnNamesAsStringList.get(i);
				    			  
				    			  mergeModel.getJoinsModel().createJoin(mergeModel.getId(), columnNumber, key, datatable1ColumnName, datatable2ColumnName, columnName, connection);
				    			   
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
				      
				      //TODO: Create auto-resolution
				      //Need at least one key column in the join
				      
				      //Determine which columns in the join are unique.
				      
				      //Get the columns that include both datatables.
				      mergeModel.getJoinsModel().setJoinsModelByMergeModel(mergeModel, connection);
				      
				      CachedRowSet crossDatatableJoinsAsCachedRowSet = mergeModel.getJoinsModel().getCrossDatatableJoinsAsCachedRowSet();
				      
				      // For each join, for each datatable columns, see if the column contains only unique values
				      // If both contain only unique values, then mark it as a key.
				      
						if (crossDatatableJoinsAsCachedRowSet.next()) {

							//because the check using next() skips the first row.
							 crossDatatableJoinsAsCachedRowSet.beforeFirst();

							while (crossDatatableJoinsAsCachedRowSet.next()) {
								 
								 	//Count the duplicate keys.
									 if (
											 mergeModel.getDatatable1Model().getDuplicateValuesCountByColumnName(crossDatatableJoinsAsCachedRowSet.getString("datatable_1_column_name"), connection) == 0
											 &&
											 mergeModel.getDatatable1Model().getDuplicateValuesCountByColumnName(crossDatatableJoinsAsCachedRowSet.getString("datatable_2_column_name"), connection) == 0
											 
									 ) {
										 
										 JoinModel joinModel = new JoinModel();
										 
										 joinModel.setId(crossDatatableJoinsAsCachedRowSet.getInt("id"));
										 joinModel.getMergeModel().setId(crossDatatableJoinsAsCachedRowSet.getInt("merge_id"));
										 joinModel.setColumnNumber(crossDatatableJoinsAsCachedRowSet.getInt("column_number"));
										 joinModel.setKey(true);
										 joinModel.setDatatable1ColumnName(crossDatatableJoinsAsCachedRowSet.getString("datatable_1_column_name"));
										 joinModel.setDatatable2ColumnName(crossDatatableJoinsAsCachedRowSet.getString("datatable_2_column_name"));
										 joinModel.setConstant1(crossDatatableJoinsAsCachedRowSet.getString("constant_1"));
										 joinModel.setConstant2(crossDatatableJoinsAsCachedRowSet.getString("constant_2"));
										 joinModel.setColumnName(crossDatatableJoinsAsCachedRowSet.getString("column_name"));
										 
										 mergeModel.getJoinsModel().updateJoinByJoinModel(joinModel, connection);
										 
									 }
								 
								 
							  }

			
						} else {
							
							//TODO:
							System.out.println("There are no records in crossDatatableJoinsAsCachedRowSet.");
							
						}
				      
						
						//Work out the duplicate keys
				      //Calculate, update, [optionally retrieve, set] (potentially test with retrieve, get)
				
					  // Ideally
					  //Integer mergeId = this.getMergeModel().getId();//TODO: Ideally Merges shouldn't have a native MergeModel
					  
					  
					  //THis is already done by the mergeScript
					  //MergeModel mergeModel = this.retrieveMergeAsMergeModelByMergeId(mergeId, connection);
					  
					  
					  //At this stage, mergeModel should contain joinsAsCachedRowset, two datatableModels, each with a name and dataAsCachedRowSet set.
					  
					  //Don't use mergeFunctions because it will be easier and give better performance to count on the database server.
					  //Don't use mergesModel, because methods are restricted to getters/setters and CRUD.
					  
					  MergeScriptsModel mergeScriptsModel = new MergeScriptsModel();
					  
					  mergeScriptsModel.doDatatable1DuplicateKeysCountUsingMergeId(mergeModel.getId(), connection);
					  
					  
					  
					  
					  // At this stage, mergeFunctionsModel.getMergeModel() should contain an updated model.
					  
					  //this is already done by the mergeScript
					  //this.updateMergeByMergeModel(mergeFunctionsModel.getMergeModel(), connection);
						
					
			          
				      //End of merge algorithm
				      
	
			        }
			        catch(SQLException sqlException){
				    	sqlException.printStackTrace();
			        } 			

						        
						        
						
						        
			
				connection.close();
				
			} else {
				
				System.out.println("connection.isClosed");
			}
				
		} 
		catch (Exception e) {
			System.out.println("Exception from createMerge.");
			e.printStackTrace();
		}
    	
		
		
		return mergeModel;
	}
}
