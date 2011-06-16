package org.cggh.tools.dataMerger.data.files;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;


import org.cggh.tools.dataMerger.data.databases.DatabaseModel;


public class FileOriginsCRUD {

	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.files");
	private DatabaseModel databaseModel = new DatabaseModel();
	
	public FileOriginModel retrieveFileOriginAsFileOriginModelUsingId(int fileOriginId,
			Connection connection) {

		FileOriginModel fileOriginModel = new FileOriginModel();
		fileOriginModel.setId(fileOriginId);

		if (connection != null) {		
	
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM file_origin WHERE id = ?;");
	          preparedStatement.setInt(1, fileOriginModel.getId());
	          preparedStatement.executeQuery();
	          
	          ResultSet resultSet = preparedStatement.getResultSet();
	          
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();
	        	  
	        	  fileOriginModel.setOrigin(resultSet.getString("origin"));
	        	  
	        	  
	      	  } else {
	      		  // file_origin record not found using id
	      	  }
	          
	          resultSet.close();
	          
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        }

		} else {
			//CRUD should not be attempted unless isDatabaseConnectable was true.
			this.logger.severe("connection is null");
		}
		
		return fileOriginModel;
		
		
	}

	public HashMap<String, Integer> retrieveFileOriginsAsOriginToIdHashMap () {
		
		HashMap<String, Integer> originToIdHashMap = null;
		

		Connection connection = this.getDatabaseModel().getNewConnection();
		 
		if (connection != null) {

		    	  retrieveFileOriginsAsOriginToIdHashMap(connection);

	        	try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

		} else {
			
			logger.severe("connection is null");
		}
		
		return originToIdHashMap;
		
	}

	
	public HashMap<String, Integer> retrieveFileOriginsAsOriginToIdHashMap (Connection connection) {
		
		HashMap<String, Integer> originToIdHashMap = null;
		
		if (connection != null) {
			
			
		      try{
		    	  PreparedStatement preparedStatement = connection.prepareStatement(
		    			  
		    			  "SELECT id, origin FROM file_origin;"
		    	  
		    	  );
		          preparedStatement.executeQuery();
		          

		          ResultSet resultSet = preparedStatement.getResultSet();
		          
		          if (resultSet.next()) {
		        	  
		        	  originToIdHashMap = new HashMap<String, Integer>();
		        	  
		        	  resultSet.beforeFirst();
		        	  
		        	  while (resultSet.next()) {
		        		  
		        		  originToIdHashMap.put(resultSet.getString("origin"), resultSet.getInt("id"));
		        		  
		        	  }
		          
		          
			          
		          }
		          
		          resultSet.close();
		          preparedStatement.close();
		          
		        } 
		      	catch (SQLException sqlException){
			    	sqlException.printStackTrace();
		        }
		
			
			
		} else {
			
			logger.severe("connection is null");
		}
		
		return originToIdHashMap;
		
	}	
	
	public void setDatabaseModel(DatabaseModel databaseModel) {
		this.databaseModel = databaseModel;
	}

	public DatabaseModel getDatabaseModel() {
		return databaseModel;
	}
	
}
