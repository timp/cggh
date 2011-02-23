package org.cggh.tools.dataMerger.data.merges;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.DataModel;
import org.cggh.tools.dataMerger.data.datatables.DatatablesModel;
import org.cggh.tools.dataMerger.data.joins.JoinModel;
import org.cggh.tools.dataMerger.data.users.UserModel;


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

	
	
    
	//With new connection.
    public void setMergeModelByCreatingMerge () {

    	
		try {
			
			Connection connection = this.getDataModel().getNewConnection();
			
			if (!connection.isClosed()) {
		
			
				
			      try {
			          PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO merge (upload_1_id, upload_2_id, created_by_user_id, created_datetime, updated_datetime) VALUES (?, ?, ?, NOW(), NOW());");
			          preparedStatement.setInt(1, this.getMergeModel().getUpload1Model().getId());
			          preparedStatement.setInt(2, this.getMergeModel().getUpload2Model().getId());
			          preparedStatement.setInt(3, this.getUserModel().getId());				          
			          preparedStatement.executeUpdate();
			          preparedStatement.close();

				      this.getDataModel().setLastInsertIdByConnection(connection);
			          this.getMergeModel().setId(this.getDataModel().getLastInsertId());

				      // See if the datatables have already been loaded in.

				      //This populates the model with the latest db data.
				      this.getMergeModel().getUpload1Model().setUploadModelById(this.getMergeModel().getUpload1Model().getId(), connection);
				      
				      
				      if (!this.getMergeModel().getUpload1Model().isDatatableCreated()) {
				    	  
				    	  this.getDatatablesModel().createDatatableByUploadModel(this.getMergeModel().getUpload1Model(), connection);
			
				    	  //This populates the model with the latest db data.
					      this.getMergeModel().getUpload1Model().setUploadModelById( this.getMergeModel().getUpload1Model().getId(), connection);
		
				      }
 
				      
				      // Same for upload2
				      this.getMergeModel().getUpload2Model().setUploadModelById( this.getMergeModel().getUpload2Model().getId(), connection);

				      if (!this.getMergeModel().getUpload2Model().isDatatableCreated()) {

				    	  this.getDatatablesModel().createDatatableByUploadModel(this.getMergeModel().getUpload2Model(), connection);

				    	  //This populates the model with the latest db data.
					      this.getMergeModel().getUpload2Model().setUploadModelById( this.getMergeModel().getUpload2Model().getId(), connection);

				      }
			          
				      // The datatables have been created.
				      // Create an auto-join

				      // Load the 2 datatables
				      this.getMergeModel().getDatatable1Model().setDatatableModelByUploadId(this.getMergeModel().getUpload1Model().getId(), connection);
				      this.getMergeModel().getDatatable2Model().setDatatableModelByUploadId(this.getMergeModel().getUpload2Model().getId(), connection);

				      // Get the column names for each of the datatables.
				      
				      //String[] datatable1ColumnNamesAsStringArray = this.getMergeModel().getDatatable1Model().getColumnNamesAsStringArray();
				      //String[] datatable2ColumnNamesAsStringArray = this.getMergeModel().getDatatable2Model().getColumnNamesAsStringArray();
				      
				      List<String> datatable1ColumnNamesAsStringList = this.getMergeModel().getDatatable1Model().getColumnNamesAsStringList();
				      List<String> datatable2ColumnNamesAsStringList = this.getMergeModel().getDatatable2Model().getColumnNamesAsStringList();
				      
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
						    			  
						    			  //TODO: version 2? automatic key determination.
						    			  //Boolean isKey = (this.getMergeModel().getDatatable1Model().getDataAsCachedRowSet().get > && );
						    			  
						    			  Integer columnNumber = this.getMergeModel().getJoinsModel().getNextColumnNumberByMergeId(this.getMergeModel().getId(), connection);
						    			  Boolean key = false;
						    			  String datatable1ColumnName = datatable1ColumnNamesAsStringList.get(i);
						    			  String datatable2ColumnName = datatable2ColumnNamesAsStringList.get(j);
						    			  String columnName = datatable1ColumnNamesAsStringList.get(i);
						    			  
						    			  this.getMergeModel().getJoinsModel().createJoin(this.getMergeModel().getId(), columnNumber, key, datatable1ColumnName, datatable2ColumnName, columnName, connection);
						    			  
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
					    		  
				    			  Integer columnNumber = this.getMergeModel().getJoinsModel().getNextColumnNumberByMergeId(this.getMergeModel().getId(), connection);
				    			  Boolean key = false;
				    			  String datatable1ColumnName = datatable1ColumnNamesAsStringList.get(i);
				    			  String datatable2ColumnName = null;
				    			  String columnName = datatable1ColumnNamesAsStringList.get(i);
				    			  
				    			  this.getMergeModel().getJoinsModel().createJoin(this.getMergeModel().getId(), columnNumber, key, datatable1ColumnName, datatable2ColumnName, columnName, connection);
				    			   
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
					    		  
				    			  Integer columnNumber = this.getMergeModel().getJoinsModel().getNextColumnNumberByMergeId(this.getMergeModel().getId(), connection);
				    			  Boolean key = false;
				    			  String datatable1ColumnName = null;
				    			  String datatable2ColumnName = datatable2ColumnNamesAsStringList.get(i);
				    			  String columnName = datatable2ColumnNamesAsStringList.get(i);
				    			  
				    			  this.getMergeModel().getJoinsModel().createJoin(this.getMergeModel().getId(), columnNumber, key, datatable1ColumnName, datatable2ColumnName, columnName, connection);
				    			   
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
				      this.getMergeModel().getJoinsModel().setJoinsModelByMergeModel(this.getMergeModel(), connection);
				      
				      CachedRowSet crossDatatableJoinsAsCachedRowSet = this.getMergeModel().getJoinsModel().getCrossDatatableJoinsAsCachedRowSet();
				      
				      // For each join, for each datatable columns, see if the column contains only unique values
				      // If both contain only unique values, then mark it as a key.
				      
						if (crossDatatableJoinsAsCachedRowSet.next()) {

							//because the check using next() skips the first row.
							 crossDatatableJoinsAsCachedRowSet.beforeFirst();

							while (crossDatatableJoinsAsCachedRowSet.next()) {
								 
								 	//Count the duplicate keys.
									 if (
											 this.getMergeModel().getDatatable1Model().getDuplicateValuesCountByColumnName(crossDatatableJoinsAsCachedRowSet.getString("datatable_1_column_name"), connection) == 0
											 &&
											 this.getMergeModel().getDatatable1Model().getDuplicateValuesCountByColumnName(crossDatatableJoinsAsCachedRowSet.getString("datatable_2_column_name"), connection) == 0
											 
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
										 
										 this.getMergeModel().getJoinsModel().updateJoinByJoinModel(joinModel, connection);
										 
									 }
								 
								 
							  }

			
						} else {
							
							//TODO:
							System.out.println("There are no records in crossDatatableJoinsAsCachedRowSet.");
							
						}
				      
				      //Process duplicate keys
						
				      
				      
				      
				      
				      
				     
				      
			          
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
    	
    }


	private DatatablesModel getDatatablesModel() {
		return this.datatablesModel;
	}
	private void setDatatablesModel(final DatatablesModel datatablesModel) {
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
}
