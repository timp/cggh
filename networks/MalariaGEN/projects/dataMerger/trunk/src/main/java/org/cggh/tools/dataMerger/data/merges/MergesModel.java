package org.cggh.tools.dataMerger.data.merges;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.DataModel;
import org.cggh.tools.dataMerger.data.datatables.DatatablesModel;
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
    public Integer createMergeByUploadIds (Integer upload1Id, Integer upload2Id) {
    	
    	this.getMergeModel().getUpload1Model().setId(upload1Id);
    	this.getMergeModel().getUpload2Model().setId(upload2Id);
    	
		try {
			
			this.getDataModel().createConnection();
			Connection connection = this.getDataModel().getConnection();
			
			if (!connection.isClosed()) {
		
			
				
			      try {
			          PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO merge (upload_1_id, upload_2_id, created_by_user_id, created_datetime, updated_datetime) VALUES (?, ?, ?, NOW(), NOW());");
			          preparedStatement.setInt(1, this.getMergeModel().getUpload1Model().getId());
			          preparedStatement.setInt(2, this.getMergeModel().getUpload2Model().getId());
			          preparedStatement.setInt(3, this.getUserModel().getId());				          
			          preparedStatement.executeUpdate();
			          preparedStatement.close();
			          
			          //TODO:
				      System.out.println("Inserted merge.");
			          
				      this.getDataModel().setLastInsertIdByConnection(connection);
			          this.mergeModel.setId(this.getDataModel().getLastInsertId());
			          
			          //TODO:
				      System.out.println("Got merge id = " + this.mergeModel.getId());
				      

				      //TODO: see if the datatables have already been loaded in.
				      
				      //This populates the model with the latest db data.
				      this.getMergeModel().getUpload1Model().getUploadModelById( this.getMergeModel().getUpload1Model().getId(), connection);
				      
				      if (!this.getMergeModel().getUpload1Model().isDatatableCreated()) {
				    	  
				    	  this.getDatatablesModel().createDatatableByUploadModel(this.getMergeModel().getUpload1Model(), connection);
				    	  
				    	  //This populates the model with the latest db data.
					      this.getMergeModel().getUpload1Model().getUploadModelById( this.getMergeModel().getUpload1Model().getId(), connection);
					      
					      //TODO: checks again to see if the datatable has been created.
				    	  
				      }
				      
				      //TODO: same for upload2
				      
			          
			          
	
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
    	
		
		return this.mergeModel.getId();
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
				
				this.getDataModel().createConnection();
				Connection connection = this.getDataModel().getConnection();
				 
				if (!connection.isClosed()) {
			
					// Get the user_id
					Integer user_id = null;
					
				      try{
				          PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM user WHERE username = ?;");
				          preparedStatement.setString(1, this.userModel.getUsername());
				          preparedStatement.executeQuery();
				          ResultSet resultSet = preparedStatement.getResultSet();
	
				          // There may be no user in the user table.
				          if (resultSet.next()) {
				        	  resultSet.first();
				        	  user_id = resultSet.getInt("id");
				          } else {
				        	  System.out.println("Did not find user in user table. This user is not registered. Db query gives !resultSet.next()");
				          }
	
				          resultSet.close();
				          preparedStatement.close();
				          
				        }
				        catch(SQLException sqlException){
				        	System.out.println("<p>" + sqlException + "</p>");
					    	sqlException.printStackTrace();
				        } 				
					
				      try{
				          PreparedStatement preparedStatement = connection.prepareStatement("SELECT merge.id, upload_1.original_filename, upload_2.original_filename, created_datetime, updated_datetime FROM upload INNER JOIN upload AS upload_1 ON upload_1.id = merge.upload_1_id INNER JOIN upload AS upload_2 ON upload_2.id = merge.upload_2_id WHERE created_by_user_id = ?;");
				          preparedStatement.setInt(1, user_id);
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
