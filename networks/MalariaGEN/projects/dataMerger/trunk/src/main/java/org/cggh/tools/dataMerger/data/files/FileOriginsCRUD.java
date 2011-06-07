package org.cggh.tools.dataMerger.data.files;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;


public class FileOriginsCRUD {

	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.files");
	
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

}
