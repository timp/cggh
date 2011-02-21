package org.cggh.tools.dataMerger.data.uploads;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


public class UploadModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8176100759738568138L;
	private String repositoryFilepath = null;
	private Integer id = null;
	private boolean datatableCreated;
	private Timestamp createdDatetime;
	private int createdByUserId;
	private boolean successful;
	private String repositoryPath;
	private String originalFilename;

	
	
	public UploadModel() {

	}



	public Integer getId() {
		return this.id;
	}



	public String getRepositoryFilepath() {
		return this.repositoryFilepath;
	}



	public void setId(final Integer id) {

		this.id = id;
		
	}



	public void getUploadModelById(Integer id, Connection connection) {
		
		this.setId(id);

	      try {
	          PreparedStatement preparedStatement = connection.prepareStatement(
	        		  "SELECT original_filename, " + 
	        		  "repository_path, " +
	        		  "successful, " + 
	        		  "created_by_user_id, " + 
	        		  "created_datetime, " + 
	        		  "datatable_created " + 
	        		  "FROM upload WHERE id = ?;");
	          preparedStatement.setInt(1, this.getId());				          
	          preparedStatement.executeQuery();

	          ResultSet resultSet = preparedStatement.getResultSet();

	          // There may be no user in the user table.
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();
	        	  
	        	  
	        	  this.setOriginalFilename(resultSet.getString("original_filename"));
	        	  this.setRepositoryPath(resultSet.getString("repository_path"));
	        	  this.setSuccessful(resultSet.getBoolean("successful"));
	        	  this.setCreatedByUserId(resultSet.getInt("created_by_user_id"));
	        	  this.setCreatedDatetime(resultSet.getTimestamp("created_datetime"));
	        	  this.setDatatableCreated(resultSet.getBoolean("datatable_created"));
	        	  
	          } else {
	        	  //TODO: proper logging and error handling
	        	  System.out.println("Did not find upload in upload table. Db query gives !resultSet.next()");
	          }

	          resultSet.close();
	          preparedStatement.close();
	          

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 	
		
		
	}



	public void setDatatableCreated(boolean datatableCreated) {

		this.datatableCreated = datatableCreated;
	}



	public void setCreatedDatetime(Timestamp createdDatetime) {

		this.createdDatetime = createdDatetime;
	}



	public void setCreatedByUserId(int createdByUserId) {

		this.createdByUserId = createdByUserId;
	}



	public void setSuccessful(boolean successful) {

		this.successful = successful;
	}



	public void setRepositoryPath(String repositoryPath) {

		this.repositoryPath = repositoryPath;
	}



	public void setOriginalFilename(String originalFilename) {

		this.originalFilename = originalFilename;
	}



	public boolean isDatatableCreated() {
		return this.datatableCreated;
	}

}