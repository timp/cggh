package org.cggh.tools.dataMerger.data.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.databases.DatabaseModel;
import org.cggh.tools.dataMerger.data.userbases.UserbaseModel;
import org.cggh.tools.dataMerger.functions.data.users.UserFunctions;
import org.cggh.tools.dataMerger.functions.data.users.UsersFunctions;

public class UsersCRUD implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7013664820380395350L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.users");

	private DatabaseModel databaseModel = null;
	private UserbaseModel userbaseModel;
	
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
	


	public void setUserbaseModel(UserbaseModel userbaseModel) {
		this.userbaseModel = userbaseModel;
	}

	public UserbaseModel getUserbaseModel() {
		return userbaseModel;
	}


	public Boolean retrieveAuthenticatedAsBooleanUsingUsernameAndPasswordHashAsStrings(
			String username,
			String passwordHash) {
		
		
		Boolean authenticated = null;
		

		Connection connection = this.getUserbaseModel().getNewDatabaseConnection();
		 
		if (connection != null) {
			
			
		      try{
		    	  PreparedStatement preparedStatement = connection.prepareStatement(
		    			  
		    			  "SELECT `" + userbaseModel.getUsernameColumnName() + "` " +
		    			  "FROM `" + userbaseModel.getDatabaseTableName() + "` " +
		    			  "WHERE `" + userbaseModel.getUsernameColumnName() + "` = ? AND `" + userbaseModel.getPasswordHashColumnName() + "` = ?" +
		    			  ";"
		    	  
		    	  );
		          preparedStatement.setString(1, username);
		          preparedStatement.setString(2, passwordHash);
		          preparedStatement.executeQuery();
		          
		          ResultSet resultSet = preparedStatement.getResultSet();
		          
		          if (resultSet.next()) {
		        	  
		        	  authenticated = true;
		        	  
		        	
		      	  } else {
		      		  
		      		  authenticated = false;
		      		  
		      		  //Note: This may be a password cracking attempt or just a user error.
		      		  logger.warning("User not found with the specified username and password.");
		      		  
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




		return authenticated;
	}
	
	
	public String retrieveUsersAsDecoratedXHTMLTable () {
		
		String usersAsDecoratedXHTMLTable = "";
		
		
		  CachedRowSet usersAsCachedRowSet = this.retrieveUsersAsCachedRowSet();

		  if (usersAsCachedRowSet != null) {

			  	UsersFunctions usersFunctions = new UsersFunctions();
			  	usersAsDecoratedXHTMLTable = usersFunctions.getUsersAsDecoratedXHTMLTableUsingUsersAsCachedRowSet(usersAsCachedRowSet);
			    
		  } else {
			  
			  this.logger.severe("usersAsCachedRowSet is null");
			  usersAsDecoratedXHTMLTable = "<p>Error: usersAsCachedRowSet is null</p>";
			  
		  }
		
		return usersAsDecoratedXHTMLTable;
	}
	
	public CachedRowSet retrieveUsersAsCachedRowSet() {
		
		CachedRowSet usersAsCachedRowSet = null;
		
		
		   String CACHED_ROW_SET_IMPL_CLASS = "com.sun.rowset.CachedRowSetImpl";
		   
				

				Connection connection = this.getUserbaseModel().getNewDatabaseConnection();
				 
				if (connection != null) {
					
					
				      try{
				    	  PreparedStatement preparedStatement = connection.prepareStatement(
				    			  
				    			  "SELECT `" + userbaseModel.getUsernameColumnName() + "` " +
				    			  "FROM `" + userbaseModel.getDatabaseTableName() + "` " +
				    			  ";"
				    	  
				    	  );
				          preparedStatement.executeQuery();
				          Class<?> cachedRowSetImplClass = Class.forName(CACHED_ROW_SET_IMPL_CLASS);
				          usersAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
				          usersAsCachedRowSet.populate(preparedStatement.getResultSet());
				          preparedStatement.close();
	
				        } 
				      	catch (SQLException sqlException){
					    	sqlException.printStackTrace();
				        } catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
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
		
	
	
	
	     return usersAsCachedRowSet;
	     
	}
	public String retrieveNewUserAsDecoratedXHTMLTable () {
		
		String newUserAsDecoratedXHTMLTable = "";
		
		UserFunctions userFunctions = new UserFunctions();
		newUserAsDecoratedXHTMLTable = userFunctions.getUserAsDecoratedXHTMLTableUsingUserModel(new UserModel());//Unnecessary but explicit.
	
		return newUserAsDecoratedXHTMLTable;
	}


	public Boolean createUserUsingUsernameAndPasswordHash(String username,
			String passwordHash) {
		
		Boolean success = null;

		Connection connection = this.getUserbaseModel().getNewDatabaseConnection();
		 
		if (connection != null) {
			
			
		      try{
		    	  PreparedStatement preparedStatement = connection.prepareStatement(
		    			  
		    			  "INSERT INTO `" + this.getUserbaseModel().getDatabaseTableName() + "` (`" + this.getUserbaseModel().getUsernameColumnName() + "`, `" + this.getUserbaseModel().getPasswordHashColumnName() + "`) " +
		    			  "VALUES ('" + username + "', '" + passwordHash + "') " +
		    			  ";"
		    	  
		    	  );
		          preparedStatement.executeUpdate();
		          preparedStatement.close();

		          success = true;
		          
		        } 
		      	catch (SQLException sqlException){
			    	
		      		sqlException.printStackTrace();
			    	success = false;
			    	
		        } finally {
		        	try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
		        }
		
			
			
		} else {
			
			logger.severe("connection is null");
			success = false;
		}


		
		return success;
	}
}
