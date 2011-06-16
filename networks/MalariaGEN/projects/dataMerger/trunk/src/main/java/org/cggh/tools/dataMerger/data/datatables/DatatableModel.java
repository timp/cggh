package org.cggh.tools.dataMerger.data.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;



public class DatatableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8176100759738568138L;
	private String name;
	private Integer duplicateKeysCount; //Only relevant in the context of a merge with joins with key(s)

	private List<String> columnNamesAsStringList;
	private Integer duplicateValuesCount;
	private List<String> keyColumnNamesAsStringList;
	private CachedRowSet columnsAsCachedRowSet;


	public DatatableModel() {

		this.setColumnNamesAsStringList(new ArrayList<String>());
		this.setKeyColumnNamesAsStringList(new ArrayList<String>());
		
	}




	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}
	public void setName(final String name) {
		this.name = name;
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
	


	
	public void setColumnNamesAsStringList(List<String> columnNamesAsStringList) {
		
		this.columnNamesAsStringList = columnNamesAsStringList;
		
	}

	public List<String> getColumnNamesAsStringList() {

		return this.columnNamesAsStringList;
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




	public void setColumnsAsCachedRowSet(CachedRowSet columnsAsCachedRowSet) {
		this.columnsAsCachedRowSet = columnsAsCachedRowSet;
	}




	public CachedRowSet getColumnsAsCachedRowSet() {
		return columnsAsCachedRowSet;
	}






	



}