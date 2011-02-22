package org.cggh.tools.dataMerger.data.joins;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class JoinsModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2278497615197327793L;

	private JoinModel joinModel;

	private Integer nextColumnNumber;

	public JoinsModel() {

		this.setJoinModel(new JoinModel());
	}


    public void setJoinModel (final JoinModel joinModel) {
        this.joinModel  = joinModel;
    }
    public JoinModel getJoinModel () {
        return this.joinModel;
    }

	public Integer getNextColumnNumberByMergeId(Integer mergeId,
			Connection connection) {
		
		this.getJoinModel().getMergeModel().setId(mergeId);
		
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(column_number) AS maxColumnNumber FROM `join` WHERE merge_id = ?;");
	          preparedStatement.setInt(1, this.getJoinModel().getMergeModel().getId());
	          preparedStatement.executeQuery();
	          ResultSet resultSet = preparedStatement.getResultSet();
	          
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();

	        	  Integer maxColumnNumber = resultSet.getInt("maxColumnNumber");
	        	  
	        	  if (maxColumnNumber != null) {
	        		  
	        		  this.setNextColumnNumber(maxColumnNumber + 1);
	        		  
	        	  } else {
	        		  
	        		  this.setNextColumnNumber(1);
	        	  }

	        	  
	        	  
	      	  } else {
	      		  
	      		  this.setNextColumnNumber(1);
	      		  
	      	  }
	          
	          resultSet.close();
	          
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
		return this.getNextColumnNumber();
	}


	private void setNextColumnNumber(int nextColumnNumber) {
		this.nextColumnNumber = nextColumnNumber;
	}


	private Integer getNextColumnNumber() {
		return this.nextColumnNumber;
	}


	public void createJoin(Integer mergeId, Integer columnNumber, Boolean key,
			String datatable1ColumnName, String datatable2ColumnName,
			String columnName, Connection connection) {
		
		this.getJoinModel().getMergeModel().setId(mergeId);
		this.getJoinModel().setColumnNumber(columnNumber);
		this.getJoinModel().setKey(key);
		this.getJoinModel().setDatatable1ColumnName(datatable1ColumnName);
		this.getJoinModel().setDatatable2ColumnName(datatable2ColumnName);
		this.getJoinModel().setColumnName(columnName);
		
		
	      try {

	          PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `join` (merge_id, column_number, `key`, datatable_1_column_name, datatable_2_column_name, column_name) VALUES (?, ?, ?, ?, ?, ?);");
	          preparedStatement.setInt(1, this.getJoinModel().getMergeModel().getId());
	          preparedStatement.setInt(2, this.getJoinModel().getColumnNumber());
	          preparedStatement.setBoolean(3, this.getJoinModel().getKey());
	          preparedStatement.setString(4, this.getJoinModel().getDatatable1ColumnName());
	          preparedStatement.setString(5, this.getJoinModel().getDatatable2ColumnName());
	          preparedStatement.setString(6, this.getJoinModel().getColumnName());
	          preparedStatement.executeUpdate();
	          preparedStatement.close();
	          
	
	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 	
	}  
    
 
}
