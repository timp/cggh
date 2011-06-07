package org.cggh.tools.dataMerger.data.files;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.databases.DatabaseModel;
import org.cggh.tools.dataMerger.data.datatables.DatatablesCRUD;
import org.cggh.tools.dataMerger.data.files.FileModel;
import org.cggh.tools.dataMerger.data.users.UserModel;
import org.cggh.tools.dataMerger.data.users.UsersCRUD;
import org.cggh.tools.dataMerger.functions.data.files.FilesFunctions;



public class FilesCRUD implements java.io.Serializable  {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6872757755874307990L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.files");
	
	private DatabaseModel databaseModel;
	
	public String retrieveFilesAsDecoratedXHTMLTableUsingUserId (Integer userId) {
		
		String filesAsDecoratedXHTMLTable = "";
		
		
		  CachedRowSet filesAsCachedRowSet = this.retrieveFilesAsCachedRowSetUsingUserId(userId);

		  if (filesAsCachedRowSet != null) {

			  	FilesFunctions filesFunctions = new FilesFunctions();
			  	filesFunctions.setFilesAsCachedRowSet(filesAsCachedRowSet);
			  	filesFunctions.setFilesAsDecoratedXHTMLTableUsingFilesAsCachedRowSet();
			  	filesAsDecoratedXHTMLTable = filesFunctions.getFilesAsDecoratedXHTMLTable();
			    
		  } else {
			  
			  //TODO: Error handling
			  this.logger.severe("filesAsCachedRowSet is null");
			  filesAsDecoratedXHTMLTable = "<p>Error: filesAsCachedRowSet is null</p>";
			  
		  }
		
		return filesAsDecoratedXHTMLTable;
	}

	public CachedRowSet retrieveFilesAsCachedRowSetUsingUserId(Integer userId) {
		
		CachedRowSet filesAsCachedRowSet = null;
		
		UserModel userModel = new UserModel();
		userModel.setId(userId);
		
		   String CACHED_ROW_SET_IMPL_CLASS = "com.sun.rowset.CachedRowSetImpl";
		   
				

				Connection connection = this.getDatabaseModel().getNewConnection();
				 
				if (connection != null) {
					
					
				      try{
				    	  PreparedStatement preparedStatement = connection.prepareStatement(
				    			  
				    			  "SELECT file.id, filename, filepath, file_size_in_bytes, created_by_user_id, created_datetime, rows_count, columns_count, file_origin.origin " +
				    			  "FROM file " +
				    			  "LEFT JOIN file_origin ON file_origin.id = file.file_origin_id "  +
				    			  "WHERE created_by_user_id = ? " +
				    			  ";"
				    	  
				    	  );
				          preparedStatement.setInt(1, userModel.getId());
				          preparedStatement.executeQuery();
				          Class<?> cachedRowSetImplClass = Class.forName(CACHED_ROW_SET_IMPL_CLASS);
				          filesAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
				          filesAsCachedRowSet.populate(preparedStatement.getResultSet());
				          preparedStatement.close();
	
				        } 
				      	catch (SQLException sqlException){
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
					
					logger.severe("connection is null");
				}
		
	
	
	
	     return filesAsCachedRowSet;
	     
	}

	public void setDatabaseModel(DatabaseModel databaseModel) {
		this.databaseModel = databaseModel;
	}

	public DatabaseModel getDatabaseModel() {
		return databaseModel;
	}

	public void createFileUsingFileModel(FileModel fileModel, Connection connection) {
		
		if (connection != null) {
			
			try {
	          PreparedStatement preparedStatement = connection.prepareStatement(
	        		  "INSERT INTO file (" +
	        		  "filename, " +
	        		  "file_origin_id, " +
	        		  "created_by_user_id, " +
	        		  "created_datetime" +
	        		  ") " +
	        		  "VALUES (" +
	        		  "?, " +
	        		  "?, " +
	        		  "?, " +
	        		  "NOW()" +
	        		  ")" +
	        		  ";");
	          preparedStatement.setString(1, fileModel.getFilename());
	          preparedStatement.setInt(2, fileModel.getFileOriginModel().getId());
	          preparedStatement.setInt(3, fileModel.getCreatedByUserModel().getId());
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

	public void updateFileFilepathAndFileSizeInBytesUsingFileModel(
			FileModel fileModel, Connection connection) {
		
		if (connection != null) {
			
			try {
	          PreparedStatement preparedStatement = connection.prepareStatement("UPDATE file SET " +
	          																		"filepath = ?, " +
	          																		"file_size_in_bytes = ? " +
	          																		"WHERE id = ?;");
	          preparedStatement.setString(1, fileModel.getFilepath());
	          preparedStatement.setLong(2, fileModel.getFileSizeInBytes());
	          preparedStatement.setInt(3, fileModel.getId());
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
	
	public FileModel retrieveFileCreatedByUserAsFileModelUsingFileIdAndUserModel (Integer fileId, UserModel userModel) {
		
		FileModel fileModel = new FileModel();
		fileModel.setId(fileId);
		
		Connection connection = this.getDatabaseModel().getNewConnection();
		 
		if (connection != null) {
		
		      try{
		          PreparedStatement preparedStatement = connection.prepareStatement(
		        		  
		        		  "SELECT file.id, filename, filepath, file_size_in_bytes, created_by_user_id, created_datetime, datatable_name, rows_count, columns_count, file_origin_id, file_origin.origin " +
		    			  "FROM file " +
		    			  "LEFT JOIN file_origin ON file_origin.id = file.file_origin_id "  +
		    			  "WHERE file.id = ? AND created_by_user_id = ?" +
		    			  ";"
		        		  
		          );
		          preparedStatement.setInt(1, fileModel.getId());
		          preparedStatement.setInt(2, userModel.getId());
		          preparedStatement.executeQuery();
		          ResultSet resultSet = preparedStatement.getResultSet();
		          
		          if (resultSet.next()) {
		        	  
		        	  resultSet.first();

		        	  //Set the file data
		        	  fileModel.setFilename(resultSet.getString("filename"));
		        	  fileModel.setFilepath(resultSet.getString("filepath"));
		        	  fileModel.setFileSizeInBytes(resultSet.getLong("file_size_in_bytes"));
		        	  UsersCRUD usersCRUD = new UsersCRUD();
		        	  fileModel.setCreatedByUserModel(usersCRUD.retrieveUserAsUserModelUsingId(resultSet.getInt("created_by_user_id"), connection));
		        	  fileModel.setCreatedDatetime(resultSet.getTimestamp("created_datetime"));  	  
		        	  //TODO: This could be too expensive.
		        	  DatatablesCRUD datatablesCRUD = new DatatablesCRUD();
		        	  fileModel.setDatatableModel(datatablesCRUD.retrieveDatatableAsDatatableModelUsingDatatableName(resultSet.getString("datatable_name"), connection));
		        	  fileModel.setRowsCount(resultSet.getInt("rows_count"));
		        	  fileModel.setColumnsCount(resultSet.getInt("columns_count"));
		        	  FileOriginsCRUD fileOriginsCRUD = new FileOriginsCRUD();
		        	  fileModel.setFileOriginModel(fileOriginsCRUD.retrieveFileOriginAsFileOriginModelUsingId(resultSet.getInt("file_origin_id"), connection));
		        	  
		        	  
		      	  } else {
		      		  
		      		  logger.warning("File not found with the specified file id and user id.");
		      		  
		      	  }
		          
		          resultSet.close();
		          
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
		
		return fileModel;
	}
	
	public FileModel retrieveFileAsFileModelByFileId(Integer fileId, Connection connection) {
		
		FileModel fileModel = new FileModel();
		
		fileModel.setId(fileId);
		
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement(
	        		  
	        		  "SELECT file.id, filename, filepath, file_size_in_bytes, created_by_user_id, created_datetime, datatable_name, rows_count, columns_count, file_origin_id, file_origin.origin " +
	    			  "FROM file " +
	    			  "LEFT JOIN file_origin ON file_origin.id = file.file_origin_id "  +
	    			  "WHERE file.id = ? " +
	    			  ";"
	        		  
	          );
	          preparedStatement.setInt(1, fileModel.getId());
	          preparedStatement.executeQuery();
	          ResultSet resultSet = preparedStatement.getResultSet();
	          
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();

	        	  //Set the file data
	        	  fileModel.setFilename(resultSet.getString("filename"));
	        	  fileModel.setFilepath(resultSet.getString("filepath"));
	        	  fileModel.setFileSizeInBytes(resultSet.getLong("file_size_in_bytes"));
	        	  UsersCRUD usersCRUD = new UsersCRUD();
	        	  fileModel.setCreatedByUserModel(usersCRUD.retrieveUserAsUserModelUsingId(resultSet.getInt("created_by_user_id"), connection));
	        	  fileModel.setCreatedDatetime(resultSet.getTimestamp("created_datetime"));  	  
	        	  //TODO: This could be too expensive.
	        	  DatatablesCRUD datatablesCRUD = new DatatablesCRUD();
	        	  fileModel.setDatatableModel(datatablesCRUD.retrieveDatatableAsDatatableModelUsingDatatableName(resultSet.getString("datatable_name"), connection));
	        	  fileModel.setRowsCount(resultSet.getInt("rows_count"));
	        	  fileModel.setColumnsCount(resultSet.getInt("columns_count"));
	        	  FileOriginsCRUD fileOriginsCRUD = new FileOriginsCRUD();
	        	  fileModel.setFileOriginModel(fileOriginsCRUD.retrieveFileOriginAsFileOriginModelUsingId(resultSet.getInt("file_origin_id"), connection));
	        	  
	        	  
	      	  } else {
	      		  
	      		  logger.warning("File not found with the specified id.");
	      		  
	      	  }
	          
	          resultSet.close();
	          
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
		
		return fileModel;
	}
}
