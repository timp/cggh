package org.cggh.tools.dataMerger.code.settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.cggh.tools.dataMerger.data.databases.DatabaseModel;

public class SettingsCRUD {

	private final Logger logger = Logger.getLogger(this.getClass().getPackage().getName());
	private DatabaseModel databaseModel;

	
	public SettingsModel retrieveSettingsAsSettingsModel () {
		
		SettingsModel settingsModel = new SettingsModel();
		
		

		Connection connection = this.getDatabaseModel().getNewConnection();
		 
		if (connection != null) {
			
			
		      try{
		    	  PreparedStatement preparedStatement = connection.prepareStatement(
		    			  
		    			  "SELECT * FROM setting;"
		    	  
		    	  );
		          preparedStatement.executeQuery();
		          
		          ResultSet resultSet = preparedStatement.getResultSet();
		          
		          if (resultSet.next()) {
		        	  
		        	  settingsModel.setSettingsAsHashMap(new HashMap<String, String>());
		        	  
		        	  resultSet.beforeFirst();
		        	  
		        	  while (resultSet.next()) {
		        		  
		        		  settingsModel.getSettingsAsHashMap().put(resultSet.getString("name"), resultSet.getString("value"));
		        		  
		        	  }

		        	  
		        	  
		      	  } else {
		      		  
		      		  logger.severe("No settings found.");
		      		  
		      	  }
		          
		          resultSet.close();
		          
		          preparedStatement.close();

		        } 
		      	catch (SQLException sqlException){
			    	sqlException.printStackTrace();
		        } finally {
		        	try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
		        }
		
			
			
		} else {
			
			logger.severe("connection is null");
		}

		
		return settingsModel;
	}
	
	
	public void setDatabaseModel(DatabaseModel databaseModel) {
		this.databaseModel = databaseModel;
	}

	public DatabaseModel getDatabaseModel() {
		return databaseModel;
	}
	
}
