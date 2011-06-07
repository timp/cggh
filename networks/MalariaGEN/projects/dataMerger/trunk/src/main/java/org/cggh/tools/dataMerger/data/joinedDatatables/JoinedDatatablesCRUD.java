package org.cggh.tools.dataMerger.data.joinedDatatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.joinedDatatables.JoinedDatatableModel;
import org.cggh.tools.dataMerger.data.merges.MergeModel;

public class JoinedDatatablesCRUD {

	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.joinedDatatables");
	
	
	public JoinedDatatableModel retrieveJoinedDatatableAsJoinedDatatableModelUsingMergeId(
			Integer mergeId, Connection connection) {
		
		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);
		
		JoinedDatatableModel joinedDatatableModel = new JoinedDatatableModel();
		
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT joined_datatable_name FROM `merge` WHERE id = ?;");
	          preparedStatement.setInt(1, mergeModel.getId());
	          preparedStatement.executeQuery();
	          ResultSet resultSet = preparedStatement.getResultSet();
	          
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();

	        	  //Set the joinedKeytable properties
	        	  joinedDatatableModel.setName(resultSet.getString("joined_datatable_name"));
	        	  
	        	  if (joinedDatatableModel.getName() != null) {
	        		  //Retrieve the datatable data
	        		  joinedDatatableModel.setDataAsCachedRowSet(this.retrieveDataAsCachedRowSetByJoinedDatatableName(joinedDatatableModel.getName(), connection));
	        	  }
	        	  
	      	  } else {
	      		  
	      		  //This is not necessarily an error, since might just be checking for existence.
	      		  //this.logger.info("Did not retrieve joined datatable with the specified merge Id.");
	      		  
	      		  
	      	  }
	          
	          resultSet.close();
	          
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
		
		return joinedDatatableModel;
	}

	
	public CachedRowSet retrieveDataAsCachedRowSetByJoinedDatatableName(String joinedDatatableName,
			Connection connection) {
		
		JoinedDatatableModel joinedDatatableModel = new JoinedDatatableModel();
		joinedDatatableModel.setName(joinedDatatableName);
		
        Class<?> cachedRowSetImplClass = null;
		try {
			cachedRowSetImplClass = Class.forName("com.sun.rowset.CachedRowSetImpl");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		CachedRowSet dataAsCachedRowSet = null;
		try {
			dataAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `" + joinedDatatableModel.getName() + "`;");
	          preparedStatement.executeQuery();
	         
	          dataAsCachedRowSet.populate(preparedStatement.getResultSet());
	          
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
		return dataAsCachedRowSet;
	}


	public Logger getLogger() {
		return logger;
	}	
	
}
