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
	private Integer duplicateKeysCount;
	private Timestamp createdDatetime;
	private CachedRowSet dataAsCachedRowSet;
	//private String[] columnNamesAsStringArray;
	private List<String> columnNamesAsStringList;
	
	
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

	public void setDatatableModelByName(String name, Connection connection) {

		this.setName(name);
		
		//TODO:
		System.out.println("Looking for datatable with name: " + this.getName());
		
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
	        	  System.out.println("Did not find datatable. Db query gives !resultSet.next()");
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
	






//	public void setColumnNamesAsStringArray(String[] columnNamesAsStringArray) {
//
//		this.columnNamesAsStringArray = columnNamesAsStringArray;
//		
//	}
//	public String[] getColumnNamesAsStringArray() {
//
//		return this.columnNamesAsStringArray;
//	}
	
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
	        	  System.out.println("Did not find datatable. Db query gives !resultSet.next()");
	          }

	          resultSet.close();
	          preparedStatement.close();
	          

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 	        	  
	        	  

		
		
	}



	public void setDatatableModelById(Integer id, Connection connection) {

		  this.setId(id);
		  
		  //clear old data
	  	  this.setName(null);
		  this.getUploadModel().setId(null);
		  this.setCreatedDatetime(null);

	      try {
	          PreparedStatement preparedStatement = connection.prepareStatement(
	        		  "SELECT id, " + 
	        		  "name, " +
	        		  "upload_id, " +  
	        		  "created_datetime " + 
	        		  "FROM datatable WHERE id = ?;");
	          preparedStatement.setInt(1, this.getId());				          
	          preparedStatement.executeQuery();

	          ResultSet resultSet = preparedStatement.getResultSet();

	          // There may be no such datatable.
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();
	        	  
	        	  this.setId(resultSet.getInt("id"));
	        	  this.setName(resultSet.getString("name"));
	        	  this.getUploadModel().setId(resultSet.getInt("upload_id"));
	        	  this.setCreatedDatetime(resultSet.getTimestamp("created_datetime"));
	        	  
	        	  //TODO: Data
	        	  
			      try{
			          PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM `" + this.getName() + "`;");
			          preparedStatement2.executeQuery();
			          
			          String CACHED_ROW_SET_IMPL_CLASS = "com.sun.rowset.CachedRowSetImpl";
			          Class<?> cachedRowSetImplClass = Class.forName(CACHED_ROW_SET_IMPL_CLASS);
			          
			          //TODO: Make this more memory efficient.
			          
			          CachedRowSet dataAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
			          dataAsCachedRowSet.populate(preparedStatement2.getResultSet());
			          
			          this.setDataAsCachedRowSet(dataAsCachedRowSet);
			          
			          //String[] columnNamesAsStringArray = new String[this.getDataAsCachedRowSet().getMetaData().getColumnCount()];
			          
			          List<String> columnNamesAsStringList = new ArrayList<String>();
			          
			          for (int i = 0; i < this.getDataAsCachedRowSet().getMetaData().getColumnCount(); i++) {
			        	  
			        	  //columnNamesAsStringArray[i] = this.getDataAsCachedRowSet().getMetaData().getColumnName(i + 1);
			        	  
			        	  columnNamesAsStringList.add(this.getDataAsCachedRowSet().getMetaData().getColumnName(i + 1));
			          }
			          
			          //this.setColumnNamesAsStringArray(columnNamesAsStringArray);
			          
			          this.setColumnNamesAsStringList(columnNamesAsStringList);
			          
			          preparedStatement2.close();

			        }
			        catch(SQLException sqlException){
				    	sqlException.printStackTrace();
			        } catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
	        	  
	        	  
	        	  
	        	  
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






	



}