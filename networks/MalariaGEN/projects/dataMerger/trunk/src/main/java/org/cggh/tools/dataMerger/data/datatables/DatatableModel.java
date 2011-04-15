package org.cggh.tools.dataMerger.data.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.uploads.UploadModel;


public class DatatableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8176100759738568138L;
	private String name;
	private UploadModel uploadModel;
	private Integer duplicateKeysCount; //Only relevant in the context of a merge with joins with key(s)
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
		this.setName(datatableModel.getName());
		this.setUploadModel(datatableModel.getUploadModel());
		this.setDuplicateKeysCount(datatableModel.getDuplicateKeysCount());
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

	
	public List<String> getKeyColumnNamesAsStringList() {
		return this.keyColumnNamesAsStringList;
	}

	public void setKeyColumnNamesAsStringList(List<String> keyColumnNamesAsStringList) {
		this.keyColumnNamesAsStringList = keyColumnNamesAsStringList;
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




	public void setDatatableModelById(String name, Connection connection) {

		DatatablesModel datatablesModel = new DatatablesModel();
		
		this.setDatatableModel(datatablesModel.retrieveDatatableAsDatatableModelUsingDatatableName(name, connection));

		
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
	        	  //System.out.println("Did not get totalDuplicateValuesCount for this columnName. Db query gives !resultSet.next()");
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
		
		this.setDuplicateKeysCount(datatablesModel.retrieveDatatableAsDatatableModelUsingDatatableName(this.getName(), connection).getDuplicateKeysCount());
	}

	public void setDatatableModelById(Connection connection) {
		
		DatatablesModel datatablesModel = new DatatablesModel();
		
		//Get what is in the database.
		//Set to what is in the database. Use other methods to process or update.
		this.setDatatableModel(datatablesModel.retrieveDatatableAsDatatableModelUsingDatatableName(this.getName(), connection));
		
	}








	



}