package org.cggh.tools.dataMerger.data.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.cggh.tools.dataMerger.data.uploads.UploadModel;


public class DatatableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8176100759738568138L;
	private Integer id;
	private String name;
	private UploadModel uploadModel;
	private Integer duplicateKeysCount;
	private Timestamp createdDatetime;

	
	
	public DatatableModel() {

		this.setUploadModel(new UploadModel());
	}



	public Integer getId() {
		return this.id;
	}



	public void setId(final Integer id) {
		this.id = id;
	}



	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}
	public void setName(final String name) {
		this.name = name;
	}



	public UploadModel getUploadModel() {

		return this.uploadModel;
	}
	public void setName(final UploadModel uploadModel) {
		this.uploadModel = uploadModel;
	}

	public Integer getDuplicateKeysCount() {
		return this.duplicateKeysCount;
	}
	public void setDuplicateKeysCount(Integer duplicateKeysCount) {
		this.duplicateKeysCount = duplicateKeysCount;
	}


	public void setCreatedDatetime(Timestamp createdDatetime) {
		this.createdDatetime = createdDatetime;
	}
	public Timestamp getCreatedDatetime() {
		return this.createdDatetime;
	}

	public void getDatatableModelByName(String name, Connection connection) {

		this.setName(name);
		
		//Init to prevent previous persistence
	  	  this.setId(null);
		  this.getUploadModel().setId(null);
		  this.setCreatedDatetime(null);
		
		
	      try {
	          PreparedStatement preparedStatement = connection.prepareStatement(
	        		  "SELECT id, " + 
	        		  "name, " +
	        		  "upload_Id, " +  
	        		  "created_datetime " + 
	        		  "FROM datatable WHERE name = ?;");
	          preparedStatement.setString(1, this.getName());				          
	          preparedStatement.executeQuery();

	          ResultSet resultSet = preparedStatement.getResultSet();

	          // There may be no such datatable.
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();
	        	  
	        	  
	        	  this.setId(resultSet.getInt("id"));
	        	  this.setName(resultSet.getString("name"));
	        	  this.getUploadModel().setId(resultSet.getInt("upload_Id"));
	        	  this.setCreatedDatetime(resultSet.getTimestamp("created_datetime"));
	        	  
	          } else {
	        	  //TODO: proper logging and error handling
	        	  System.out.println("Did not find datatable. Db query gives !resultSet.next()");
	          }

	          resultSet.close();
	          preparedStatement.close();
	          

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
		
	}



	public void setUploadModel(UploadModel uploadModel) {
		this.uploadModel = uploadModel;
	}




	



}