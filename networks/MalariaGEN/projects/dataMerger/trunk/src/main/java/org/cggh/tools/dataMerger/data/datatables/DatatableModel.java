package org.cggh.tools.dataMerger.data.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.uploads.UploadModel;


public class DatatableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8176100759738568138L;
	private Integer id;
	private String name;
	private UploadModel uploadModel;
	private Integer duplicateKeysCount; //Only relevant in the context of a merge with joins with key(s)
	private Timestamp createdDatetime;
	private CachedRowSet dataAsCachedRowSet;
	private List<String> columnNamesAsStringList;
	private Integer duplicateValuesCount;
	private List<String> keyColumnNamesAsStringList;
	
	
	public DatatableModel() {

		this.setUploadModel(new UploadModel());
		this.setColumnNamesAsStringList(new ArrayList<String>());
		this.setKeyColumnNamesAsStringList(new ArrayList<String>());
		
	}

	//To honor no-argument bean constructor
	public void setDatatableModel(DatatableModel datatableModel) {
		
		//TODO: There must be a better way of doing this!? Pity we can't this = that.
		this.setId(datatableModel.getId());
		this.setName(datatableModel.getName());
		this.setUploadModel(datatableModel.getUploadModel());
		this.setCreatedDatetime(datatableModel.getCreatedDatetime());
		this.setDuplicateKeysCount(datatableModel.getDuplicateKeysCount());
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
	public void setUploadModel(UploadModel uploadModel) {
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

	
	public List<String> getKeyColumnNamesAsStringList() {
		return this.keyColumnNamesAsStringList;
	}

	public void setKeyColumnNamesAsStringList(List<String> keyColumnNamesAsStringList) {
		this.keyColumnNamesAsStringList = keyColumnNamesAsStringList;
	}	
	
	
	//TODO: Refactor references to use (copy query to) datatablesModel.retrieveDatatableAsDatatableModelUsingName(String name, Connection connection)
	//TODO: With subsquent datatableModel.setDatatableModel(Datatable Model)
	public void setDatatableModelByName(String name, Connection connection) {

		this.setName(name);
	
		  //Init to prevent previous persistence
	  	  this.setId(null);
		  this.getUploadModel().setId(null);
		  this.setCreatedDatetime(null);
		
		  //TODO: Data
		
	      try {
	          PreparedStatement preparedStatement = connection.prepareStatement(
	        		  "SELECT id, " + 
	        		  "name, " +
	        		  "upload_id, " +  
	        		  "created_datetime " + 
	        		  "FROM datatable WHERE name = ?;");
	          preparedStatement.setString(1, this.getName());				          
	          preparedStatement.executeQuery();

	          ResultSet resultSet = preparedStatement.getResultSet();

	          // There may be no such datatable.
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();

	        	  this.setId(resultSet.getInt("id"));
	        	  
	        	  this.setDatatableModelById(this.getId(), connection);

	          } else {
	        	  //TODO: proper logging and error handling
	        	  System.out.println("Did not find datatable with this name. Db query gives !resultSet.next()");
	          }

	          resultSet.close();
	          preparedStatement.close();
	          

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
		
	}



	public void setDataAsCachedRowSet(CachedRowSet cachedRowSet) {
		
		this.dataAsCachedRowSet = cachedRowSet;
		
	}
	public CachedRowSet getDataAsCachedRowSet() {
		
		return this.dataAsCachedRowSet;
		
	}
	


	
	public void setColumnNamesAsStringList(List<String> columnNamesAsStringList) {
		
		this.columnNamesAsStringList = columnNamesAsStringList;
		
	}

	public List<String> getColumnNamesAsStringList() {

		return this.columnNamesAsStringList;
	}




	public void setDatatableModelByUploadId(Integer uploadId, Connection connection) {

		this.getUploadModel().setId(uploadId);
		
	      try {
	          PreparedStatement preparedStatement = connection.prepareStatement(
	        		  "SELECT id, " + 
	        		  "name, " +
	        		  "upload_id, " +  
	        		  "created_datetime " + 
	        		  "FROM datatable WHERE upload_id = ?;");
	          preparedStatement.setInt(1, this.getUploadModel().getId());				          
	          preparedStatement.executeQuery();

	          ResultSet resultSet = preparedStatement.getResultSet();

	          // There may be no such datatable.
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();
	        	  
	        	  this.setId(resultSet.getInt("id"));
	   		 
	        	  // All roads lead to Rome.
	        	  this.setDatatableModelById(this.getId(), connection);	        	  
	        	  
	          } else {
	        	  //TODO: proper logging and error handling
	        	  System.out.println("Did not find datatable with this upload_id. Db query gives !resultSet.next()");
	          }

	          resultSet.close();
	          preparedStatement.close();
	          

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 	        	  
	        	  

		
		
	}



	public void setDatatableModelById(Integer id, Connection connection) {

		DatatablesModel datatablesModel = new DatatablesModel();
		
		this.setDatatableModel(datatablesModel.retrieveDatatableModelById(id, connection));

		
	}




	//TODO: Perhaps one value per columnName
	public Integer getDuplicateValuesCountByColumnName(String columnName, Connection connection) {

		//nullify
		this.setDuplicateValuesCount(null);
		
	      try {
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT SUM(duplicateValuesCount) As totalDuplicateValuesCount FROM (SELECT COUNT(`" + columnName + "`) AS duplicateValuesCount FROM `" + this.getName() + "` GROUP BY `" + columnName + "` HAVING duplicateValuesCount > 1) AS duplicateValuesCounts;");				          
	          preparedStatement.executeQuery();

	          ResultSet resultSet = preparedStatement.getResultSet();

	          // There may be no such datatable.
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();
	        	  
	        	  this.setDuplicateValuesCount(resultSet.getInt("totalDuplicateValuesCount"));	
		
	        	  
	          } else {
	        	  //TODO: proper logging and error handling
	        	  System.out.println("Did not get totalDuplicateValuesCount for this columnName. Db query gives !resultSet.next()");
	          }

	          resultSet.close();
	          preparedStatement.close();
	          

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 		
		
		return this.getDuplicateValuesCount();
	}



	public Integer getDuplicateValuesCount() {
		return this.duplicateValuesCount;
	}



	public void setDuplicateValuesCount(final Integer duplicateValuesCount) {
		
		this.duplicateValuesCount = duplicateValuesCount;
	}



	public void setDuplicateKeysCountByRetrievingById(Connection connection) {
		
		DatatablesModel datatablesModel = new DatatablesModel();
		
		this.setDuplicateKeysCount(datatablesModel.retrieveDatatableModelById(this.getId(), connection).getDuplicateKeysCount());
	}

	public void setDatatableModelById(Connection connection) {
		
		DatatablesModel datatablesModel = new DatatablesModel();
		
		//Get what is in the database.
		//Set to what is in the database. Use other methods to process or update.
		this.setDatatableModel(datatablesModel.retrieveDatatableModelById(this.getId(), connection));
		
	}








	



}