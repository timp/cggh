package org.cggh.tools.dataMerger.data.uploads;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.databases.DatabaseModel;
import org.cggh.tools.dataMerger.data.users.UserModel;
import org.cggh.tools.dataMerger.data.users.UsersCRUD;
import org.cggh.tools.dataMerger.functions.uploads.UploadsFunctions;

public class UploadsCRUD implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4112178863119955390L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.uploads");
	private DatabaseModel databaseModel = null;
	
	
	public UploadsCRUD() {

        this.setDatabaseModel(new DatabaseModel());
		
	}

    public void setDatabaseModel (final DatabaseModel databaseModel) {
        this.databaseModel  = databaseModel;
    }
    public DatabaseModel getDatabaseModel () {
        return this.databaseModel;
    }     


   public CachedRowSet retrieveUploadsAsCachedRowSetUsingUserId(Integer userId) {

	   UserModel userModel = new UserModel();
	   userModel.setId(userId);
	   
	   String CACHED_ROW_SET_IMPL_CLASS = "com.sun.rowset.CachedRowSetImpl";
	   
	   CachedRowSet uploadsAsCachedRowSet = null;
	   
		

			Connection connection = this.getDatabaseModel().getNewConnection();
			
			if (connection != null) {

					try {
			          PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM upload WHERE created_by_user_id = ? AND repository_filepath IS NOT NULL;");
			          preparedStatement.setInt(1, userModel.getId());
			          preparedStatement.executeQuery();
			          Class<?> cachedRowSetImplClass = Class.forName(CACHED_ROW_SET_IMPL_CLASS);
			          uploadsAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
			          uploadsAsCachedRowSet.populate(preparedStatement.getResultSet());
			          preparedStatement.close();

			        }
			        catch(SQLException sqlException){
				    	sqlException.printStackTrace();
			        } catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
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
				
		



     return(uploadsAsCachedRowSet);
   }

	public UploadModel retrieveUploadAsUploadModelByUploadId(Integer uploadId, Connection connection) {
	
		UploadModel uploadModel = new UploadModel();
		
		uploadModel.setId(uploadId);
		
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, original_filename, repository_filepath, file_size_in_bytes, created_by_user_id, created_datetime FROM `upload` WHERE id = ?;");
	          preparedStatement.setInt(1, uploadModel.getId());
	          preparedStatement.executeQuery();
	          ResultSet resultSet = preparedStatement.getResultSet();
	          
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();

	        	  //Set the upload data
	        	  uploadModel.setOriginalFilename(resultSet.getString("original_filename"));
	        	  uploadModel.setRepositoryFilepath(resultSet.getString("repository_filepath"));
	        	  uploadModel.setFileSizeInBytes(resultSet.getLong("file_size_in_bytes"));
	        	  uploadModel.getCreatedByUserModel().setId(resultSet.getInt("created_by_user_id"));
	        	  uploadModel.setCreatedDatetime(resultSet.getTimestamp("created_datetime"));
	        	  
	        	  
	        	  //Retrieve the user data
	        	  UsersCRUD usersCRUD = new UsersCRUD();
	        	  uploadModel.setCreatedByUserModel(usersCRUD.retrieveUserAsUserModelUsingId(uploadModel.getCreatedByUserModel().getId(), connection));
	        	  
	        	  
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
	
	
	public String retrieveUploadsAsDecoratedXHTMLTableUsingUserId (Integer userId) {
		
		String uploadsAsDecoratedXHTMLTableUsingUploadsModel = "";
		
		  CachedRowSet uploadsAsCachedRowSet = this.retrieveUploadsAsCachedRowSetUsingUserId(userId);

		  if (uploadsAsCachedRowSet != null) {

			  	UploadsFunctions uploadsFunctionsModel = new UploadsFunctions();
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

	public void createUploadUsingUploadModelAndUserModel(UploadModel uploadModel, UserModel userModel, Connection connection) {
		
		if (connection != null) {
		
			try {
	          PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO upload (original_filename, created_by_user_id, created_datetime) VALUES (?, ?, NOW());");
	          preparedStatement.setString(1, uploadModel.getOriginalFilename());
	          preparedStatement.setInt(2, userModel.getId());
	          preparedStatement.executeUpdate();
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
	        
	        
		} else {
			
			logger.severe("connection is null");
		}
		
	}

	public void updateUploadRepositoryFilepathAndFileSizeInBytesUsingUploadModel(UploadModel uploadModel, Connection connection) {
		
		if (connection != null) {
			
			try {
	          PreparedStatement preparedStatement = connection.prepareStatement("UPDATE upload SET " +
	          																		"repository_filepath = ?, " +
	          																		"file_size_in_bytes = ? " +
	          																		"WHERE id = ?;");
	          preparedStatement.setString(1, uploadModel.getRepositoryFilepath());
	          preparedStatement.setLong(2, uploadModel.getFileSizeInBytes());
	          preparedStatement.setInt(3, uploadModel.getId());
	          preparedStatement.executeUpdate();
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
	        
	        
		} else {
			
			logger.severe("connection is null");
		}
		
	}

	public void updateUploadDatatableNameUsingUploadModel(
			UploadModel uploadModel, Connection connection) {
		
		if (connection != null) {
	        
	        try {
	        	PreparedStatement preparedStatement = connection.prepareStatement("UPDATE upload SET datatable_name=? WHERE id=?;");
				preparedStatement.setString(1, uploadModel.getDatatableModel().getName());
				preparedStatement.setInt(2, uploadModel.getId());
		        preparedStatement.executeUpdate();
		        preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
        
		} else {
			logger.severe("connection is null");
		}
		
	}

}
