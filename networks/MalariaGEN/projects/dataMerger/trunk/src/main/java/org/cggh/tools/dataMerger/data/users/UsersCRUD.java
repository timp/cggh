package org.cggh.tools.dataMerger.data.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.cggh.tools.dataMerger.data.databases.DatabaseModel;

public class UsersCRUD implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7013664820380395350L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.users");

	private DatabaseModel databaseModel = null;
	
	public UsersCRUD () {
		
		this.setDatabaseModel(new DatabaseModel());
	}
	
	
	public UserModel retrieveUserAsUserModelUsingUsername (String username) {
		
		UserModel userModel = new UserModel();
		userModel.setUsername(username);
		
		try {
			
			Connection connection = this.getDatabaseModel().getNewConnection();
			
			if (connection != null) {		
		
		      try{
		          PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE username = ?;");
		          preparedStatement.setString(1, userModel.getUsername());
		          preparedStatement.executeQuery();
		          
		          ResultSet resultSet = preparedStatement.getResultSet();
		          
		          if (resultSet.next()) {
		        	  
		        	  resultSet.first();
		        	  
		        	  userModel.setId(resultSet.getInt("id"));
		        	  
		        	  
		      	  } else {
		      		  // user record not found using username
		      	  }
		          
		          resultSet.close();
		          
		          preparedStatement.close();
	
		        }
		        catch(SQLException sqlException){
			    	sqlException.printStackTrace();
		        } 
		        finally {
		        	connection.close();
		        }

			} else {
				//CRUD should not be attempted unless isDatabaseConnectable was true.
				this.logger.severe("connection is null");
			}
				
		} 
		catch (Exception exception) {
			exception.printStackTrace();
		}
		
		
		return userModel;
	}

	public void createUserUsingUsername (String username) {
		
		UserModel userModel = new UserModel();
		userModel.setUsername(username);
		
		try {
			
			Connection connection = this.getDatabaseModel().getNewConnection();
			
			if (connection != null) {		
		
		      try{
		          PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user (username) VALUES (?);");
		          preparedStatement.setString(1, userModel.getUsername());
		          preparedStatement.executeUpdate();		          
		          preparedStatement.close();
	
		        }
		        catch(SQLException sqlException){
			    	sqlException.printStackTrace();
		        } 
		        finally {
		        	connection.close();
		        }

			} else {
				//CRUD should not be attempted unless isConnectable.
				this.logger.severe("connection is null");
			}
				
		} 
		catch (Exception exception) {
			exception.printStackTrace();
		}
		
	}

	public UserModel retrieveUserAsUserModelUsingId (Integer id, Connection connection) {
		
		UserModel userModel = new UserModel();
		userModel.setId(id);

		if (connection != null) {		
	
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE username = ?;");
	          preparedStatement.setString(1, userModel.getUsername());
	          preparedStatement.executeQuery();
	          
	          ResultSet resultSet = preparedStatement.getResultSet();
	          
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();
	        	  
	        	  userModel.setUsername(resultSet.getString("username"));
	        	  
	        	  
	      	  } else {
	      		  // user record not found using id
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

		
		return userModel;
	}	


	public void setDatabaseModel(DatabaseModel databaseModel) {
		this.databaseModel = databaseModel;
	}


	public DatabaseModel getDatabaseModel() {
		return databaseModel;
	}
	
	
	
	
	
	
	
}