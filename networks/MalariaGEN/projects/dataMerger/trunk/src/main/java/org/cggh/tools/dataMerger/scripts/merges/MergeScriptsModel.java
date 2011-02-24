package org.cggh.tools.dataMerger.scripts.merges;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.cggh.tools.dataMerger.data.merges.MergeModel;
import org.cggh.tools.dataMerger.data.merges.MergesModel;



public class MergeScriptsModel implements java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3828652891254659545L;



	public MergeScriptsModel() {
		
	}



	public void doDatatable1DuplicateKeysCountUsingMergeId(Integer mergeId, Connection connection) {
		
		MergesModel mergesModel = new MergesModel();
		MergeModel mergeModel = mergesModel.retrieveMergeAsMergeModelByMergeId(mergeId, connection);
		
		 //mergeModel should contain joinsAsCachedRowset, two datatableModels, each with a name and dataAsCachedRowSet set.
		
		
	      try{
	    	  // Get the datatable_1_column_names with keys
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT datatable_1_column_name FROM `join` WHERE merge_id = ? AND `key` = ?;");
	          preparedStatement.setInt(1, mergeModel.getId());
	          preparedStatement.setBoolean(2, true);
	          preparedStatement.executeQuery();
	          ResultSet resultSet = preparedStatement.getResultSet();
	          
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();

	        	  //TODO
	        	  
	        	  //Get a duplicateKeysCount for the datatable
	        	//SELECT SUM(duplicateValuesCount) As totalDuplicateValuesCount FROM (SELECT COUNT(`" + columnName + "`) AS duplicateValuesCount FROM `" + this.getName() + "` GROUP BY `" + columnName + "` HAVING duplicateValuesCount > 1) AS duplicateValuesCounts;
	    	      
	        	  //DatatableModel has a getDuplicateValuesCountByColumnName, but not sure it's useful here.
	        	  
	      	  } else {
	      		  
	      		  //TODO:
	      		  System.out.println("No merge found with the specified id.");
	      		  
	      	  }
	          
	          resultSet.close();
	          
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 

	}



	
}
