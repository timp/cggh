package org.cggh.tools.dataMerger.data.uploads;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.DataModel;
import org.cggh.tools.dataMerger.data.users.UserModel;
import org.cggh.tools.dataMerger.data.users.UsersModel;
import org.cggh.tools.dataMerger.functions.uploads.UploadsFunctionsModel;

public class UploadsModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4112178863119955390L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.uploads");
	private DataModel dataModel = null;
	private UserModel userModel;
	
	
	public UploadsModel() {

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

   public CachedRowSet retrieveUploadsAsCachedRowSetUsingUserId(Integer userId) {

	   UserModel userModel = new UserModel();
	   userModel.setId(userId);
	   
	   String CACHED_ROW_SET_IMPL_CLASS = "com.sun.rowset.CachedRowSetImpl";
	   
	   CachedRowSet uploadsAsCachedRowSet = null;
	   
		try {

			Connection connection = this.getDataModel().getNewConnection();
			
			if (!connection.isClosed()) {

			      try{
			          PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, original_filename, created_by_user_id, created_datetime FROM upload WHERE created_by_user_id = ? AND successful = 1;");
			          preparedStatement.setInt(1, userModel.getId());
			          preparedStatement.executeQuery();
			          Class<?> cachedRowSetImplClass = Class.forName(CACHED_ROW_SET_IMPL_CLASS);
			          uploadsAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
			          uploadsAsCachedRowSet.populate(preparedStatement.getResultSet());
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
			//System.out.println("Exception from getUploadsAsCachedRowSet.");
			e.printStackTrace();
		}



     return(uploadsAsCachedRowSet);
   }

	public UploadModel retrieveUploadAsUploadModelByUploadId(Integer uploadId, Connection connection) {
	
		UploadModel uploadModel = new UploadModel();
		
		uploadModel.setId(uploadId);
		
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, original_filename, repository_filepath, successful, created_by_user_id, created_datetime FROM `upload` WHERE id = ?;");
	          preparedStatement.setInt(1, uploadModel.getId());
	          preparedStatement.executeQuery();
	          ResultSet resultSet = preparedStatement.getResultSet();
	          
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();

	        	  //Set the upload data
	        	  uploadModel.setOriginalFilename(resultSet.getString("original_filename"));
	        	  uploadModel.setRepositoryFilepath(resultSet.getString("repository_filepath"));
	        	  uploadModel.setSuccessful(resultSet.getBoolean("successful"));
	        	  uploadModel.getCreatedByUserModel().setId(resultSet.getInt("created_by_user_id"));
	        	  uploadModel.setCreatedDatetime(resultSet.getTimestamp("created_datetime"));
	        	  
	        	  
	        	  //Retrieve the user data
	        	  UsersModel usersModel = new UsersModel();
	        	  uploadModel.setCreatedByUserModel(usersModel.retrieveUserAsUserModelByUserId(uploadModel.getCreatedByUserModel().getId(), connection));
	        	  
	        	  
	        	  //Note: Could potentially get datatable data relating to this upload too (if it exists) but this is handled by the a parent mergeModel.
	        	  
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
		
		
		return uploadModel;
	}
	
	
	public String retrieveUploadsAsDecoratedXHTMLTableUsingUploadsModel (UploadsModel uploadsModel) {
		
		String uploadsAsDecoratedXHTMLTableUsingUploadsModel = "";
		
		  CachedRowSet uploadsAsCachedRowSet = uploadsModel.retrieveUploadsAsCachedRowSetUsingUserId(uploadsModel.getUserModel().getId());

		  if (uploadsAsCachedRowSet != null) {

			  	UploadsFunctionsModel uploadsFunctionsModel = new UploadsFunctionsModel();
			    uploadsFunctionsModel.setCachedRowSet(uploadsAsCachedRowSet);
			    uploadsFunctionsModel.setDecoratedXHTMLTableByCachedRowSet();
			    uploadsAsDecoratedXHTMLTableUsingUploadsModel = uploadsFunctionsModel.getDecoratedXHTMLTable();
			    
		  } else {
			  
			  //TODO: Error handling
			  this.logger.warning("Error: uploadsAsCachedRowSet is null");
			  uploadsAsDecoratedXHTMLTableUsingUploadsModel = "<p>Error: uploadsAsCachedRowSet is null</p>";
			  
		  }
		
		return uploadsAsDecoratedXHTMLTableUsingUploadsModel;
	}

}
