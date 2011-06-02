package org.cggh.tools.dataMerger.data.uploads;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.cggh.tools.dataMerger.data.datatables.DatatableModel;
import org.cggh.tools.dataMerger.data.users.UserModel;


public class UploadModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8176100759738568138L;
	
	private UserModel createdByUserModel;
	
	private String repositoryFilepath;
	private Integer id;
	private Timestamp createdDatetime;
	private String originalFilename;
	private DatatableModel datatableModel;
	private Long fileSizeInBytes;

	
	
	public UploadModel() {
		
		this.setCreatedByUserModel(new UserModel());
		this.setDatatableModel(new DatatableModel());
		
	}

	public void setCreatedByUserModel(UserModel userModel) {
		this.createdByUserModel = userModel;
	}
	public UserModel getCreatedByUserModel() {
		return this.createdByUserModel;
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



	public void setUploadModelById(Integer id, Connection connection) {
		
		this.setId(id);

	      try {
	          PreparedStatement preparedStatement = connection.prepareStatement(
	        		  "SELECT original_filename, " + 
	        		  "repository_filepath, " +
	        		  "created_by_user_id, " + 
	        		  "created_datetime, " + 
	        		  "FROM upload WHERE id = ?;");
	          preparedStatement.setInt(1, this.getId());				          
	          preparedStatement.executeQuery();

	          ResultSet resultSet = preparedStatement.getResultSet();

	          // There may be no user in the user table.
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();
	        	  
	        	  
	        	  this.setOriginalFilename(resultSet.getString("original_filename"));
	        	  this.setRepositoryFilepath(resultSet.getString("repository_filepath"));
	        	  this.getCreatedByUserModel().setId(resultSet.getInt("created_by_user_id"));
	        	  this.setCreatedDatetime(resultSet.getTimestamp("created_datetime"));
	        	  
	          } else {
	        	  //TODO: proper logging and error handling
	        	  //System.out.println("Did not find upload in upload table. Db query gives !resultSet.next()");
	          }

	          resultSet.close();
	          preparedStatement.close();
	          

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 	
		
		
	}





	public void setCreatedDatetime(final Timestamp createdDatetime) {

		this.createdDatetime = createdDatetime;
	}
	public Timestamp getCreatedDatetime() {

		return this.createdDatetime;
	}




	public void setRepositoryFilepath(final String repositoryFilepath) {

		this.repositoryFilepath = repositoryFilepath;
	}


	public String getOriginalFilename() {
		return this.originalFilename;
	}
	public void setOriginalFilename(final String originalFilename) {
		this.originalFilename = originalFilename;
	}

	public void setDatatableModel(DatatableModel datatableModel) {
		this.datatableModel = datatableModel;
	}

	public DatatableModel getDatatableModel() {
		return datatableModel;
	}

	public void setFileSizeInBytes(Long fileSizeInBytes) {
		this.fileSizeInBytes = fileSizeInBytes;
	}

	public Long getFileSizeInBytes() {
		return fileSizeInBytes;
	}











}