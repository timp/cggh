package org.cggh.tools.dataMerger.data.databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.installations.InstallationModel;
import org.cggh.tools.dataMerger.data.users.UsersCRUD;
import org.cggh.tools.dataMerger.functions.data.databases.DatabaseFunctions;
import org.cggh.tools.dataMerger.functions.data.installations.InstallationFunctions;



public class DatabasesCRUD {

	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.databases");
	
	public DatabasesCRUD () {
		
	
	}
	

	public Boolean createDatabaseUsingDatabaseModel (DatabaseModel databaseModel) {
		
		Connection serverConnection = databaseModel.getNewServerConnection();
		
		if (serverConnection != null) {
		
			 try {
			  
				Statement statement = serverConnection.createStatement();
				statement.executeUpdate("CREATE DATABASE `" + databaseModel.getName() +  "` CHARACTER SET UTF8 COLLATE utf8_bin;");
				statement.close();
		        serverConnection.close();
		        return true;
			  }
			  catch (SQLException sqlException){
				  
				  sqlException.printStackTrace();
				  return false;
			  }

		} else {
			
			//CRUD should not have been attempted unless isServerConnectable
			this.logger.severe("serverConnection is null");
			return false;
		}
	    
	}
	
	public DatabaseModel retrieveDatabaseAsDatabaseModelUsingServletContext (final ServletContext servletContext) {
		
		DatabaseModel databaseModel = new DatabaseModel();

		
		databaseModel.setDriverFullyQualifiedClassName(servletContext.getInitParameter("databaseDriverFullyQualifiedClassName"));
		databaseModel.setServerPath(servletContext.getInitParameter("databaseServerPath"));
		databaseModel.setName(servletContext.getInitParameter("databaseName"));
		databaseModel.setUsername(servletContext.getInitParameter("databaseUsername"));
		databaseModel.setPassword(servletContext.getInitParameter("databasePassword"));
		
		
		// Determine connect-ability.		
		databaseModel.setServerConnectable(this.retrieveServerConnectableUsingDatabaseModel(databaseModel));
		databaseModel.setConnectable(this.retrieveConnectableUsingDatabaseModel(databaseModel));

		if (databaseModel.isConnectable()) {
		
			databaseModel.setTablesAsCachedRowSet(this.retrieveTablesAsCachedRowSetUsingDatabaseModel(databaseModel));
			
			if (databaseModel.getTablesAsCachedRowSet().size() > 0) {
				
				DatabaseFunctions databaseFunctions = new DatabaseFunctions();
				databaseModel.setTableNamesAsStringArrayList(databaseFunctions.convertTablesAsCachedRowSetIntoTableNamesAsStringArrayList(databaseModel.getTablesAsCachedRowSet()));
				
				if (databaseModel.getTableNamesAsStringArrayList().contains("data_installation")) {
				
					databaseModel.setCurrentInstallationModel(this.retrieveCurrentInstallationAsInstallationModelUsingDatabaseModel(databaseModel));
				
					InstallationFunctions installationFunctions = new InstallationFunctions();
				
					databaseModel.setVersionAsString(installationFunctions.determineVersionAsStringUsingInstallationModel(databaseModel.getCurrentInstallationModel()));
				
					databaseModel.setInitialized(true);
					
				} else {
					
					//The database installation table was introduced in version 1.1.0
					databaseModel.setVersionAsString("1.0.x");
				}
				
			}
			
		}
		
		return databaseModel;
	}


	public InstallationModel retrieveCurrentInstallationAsInstallationModelUsingDatabaseModel(DatabaseModel databaseModel) {

		InstallationModel currentInstallationModel = new InstallationModel();
		
		Connection connection = databaseModel.getNewConnection();
		
		if (connection != null) {	
			
			try {
				
				  //http://dev.mysql.com/doc/refman/5.0/en/example-maximum-column-group-row.html
				  //"The LEFT JOIN works on the basis that when s1.price is at its maximum value, there is no s2.price with a greater value and the s2 rows values will be NULL."
					
				  PreparedStatement preparedStatement = connection.prepareStatement(
						  "SELECT i1.* " +
						  "FROM data_installation i1 " +
						  "LEFT JOIN data_installation i2 ON i1.id = i2.id AND i1.created_datetime < i2.created_datetime " +
						  "WHERE i2.id IS NULL" +
						  ";");
				  preparedStatement.executeQuery();
				  
		          ResultSet resultSet = preparedStatement.getResultSet();
		          if (resultSet.next()) {
		        	  
		        	  resultSet.first();

		        	  currentInstallationModel.setId(resultSet.getInt("id"));
		        	  currentInstallationModel.setMajorVersionNumber(resultSet.getInt("major_version_number"));
		        	  currentInstallationModel.setMinorVersionNumber(resultSet.getInt("minor_version_number"));
		        	  currentInstallationModel.setRevisionVersionNumber(resultSet.getInt("revision_version_number"));
		        	  currentInstallationModel.getCreatedByUserModel().setId(resultSet.getInt("created_by_user_id"));
		        	  currentInstallationModel.setCreatedDatetime(resultSet.getTimestamp("created_datetime"));
		        	  
		        	  
		        	  //Retrieve the user data
		        	  UsersCRUD usersCRUD = new UsersCRUD();
		        	  currentInstallationModel.setCreatedByUserModel(usersCRUD.retrieveUserAsUserModelUsingId(currentInstallationModel.getCreatedByUserModel().getId(), connection));
		        	  
		        	  
		        	  //Note: Could potentially get datatable data relating to this upload too (if it exists) but this is handled by the a parent mergeModel.
		        	  
		      	  } else {
		      		  
		      		  this.logger.severe("no results");
		      		  
		      	  }
		          
		          resultSet.close();
				  preparedStatement.close();
				  
		
			} 
			catch (SQLException e) {
				e.printStackTrace();
			} finally {
				
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		
		
		} else {
			//CRUD should not have been attempted unless isConnectable
			this.logger.severe("connection is null");
		}
			
		
		return currentInstallationModel;
	}


	public CachedRowSet retrieveTablesAsCachedRowSetUsingDatabaseModel(DatabaseModel databaseModel) {

		CachedRowSet tablesAsCachedRowSet = null;

		Connection connection = databaseModel.getNewConnection();
		
		if (connection != null) {			
		
			try {
				
				Class<?> cachedRowSetImplClass = Class.forName("com.sun.rowset.CachedRowSetImpl");
				tablesAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
		  
				PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM information_schema.tables WHERE TABLE_SCHEMA = ?;");
				preparedStatement.setString(1, databaseModel.getName());
				preparedStatement.executeQuery();
				tablesAsCachedRowSet.populate(preparedStatement.getResultSet());
				preparedStatement.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
					
					try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
			}

		} else {
			//CRUD should not have been attempted unless isConnectable.
			this.logger.severe("connection is null");
		}		
		
		return tablesAsCachedRowSet;
	}




	public Boolean retrieveServerConnectableUsingDatabaseModel(DatabaseModel databaseModel) {

		try {

			Class.forName(databaseModel.getDriverFullyQualifiedClassName()).newInstance();
			Connection connection = DriverManager.getConnection(databaseModel.getServerPath(), databaseModel.getUsername(), databaseModel.getPassword());

			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			return true;
			
		} catch (SQLException e) {
			
			//Not necessarily an error.
			return false;
			
		} catch (InstantiationException e) {
			e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		
	}

	public Boolean retrieveConnectableUsingDatabaseModel(DatabaseModel databaseModel) {

		try {
			
			Class.forName(databaseModel.getDriverFullyQualifiedClassName()).newInstance();
			Connection connection = DriverManager.getConnection(databaseModel.getServerPath() + databaseModel.getName(), databaseModel.getUsername(), databaseModel.getPassword());
					
					try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				
			
			
			return true;
			
		} catch (SQLException e) {
			
			//Not necessarily an error.
			return false;
			
		} catch (InstantiationException e) {
			e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	
	public Integer retrieveLastInsertIdAsIntegerUsingConnection(Connection connection) {

		Integer lastInsertId = null;
		
        	try{
	    	  //TODO: Is this cross-db compatible? ref: @@IDENTITY
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT LAST_INSERT_ID() AS lastInsertId;");
	          preparedStatement.executeQuery();
	          
	          ResultSet resultSet = preparedStatement.getResultSet();
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();
	        	  
	        	  lastInsertId =  resultSet.getInt("lastInsertId");
	        	  
	          } else {
	        	  
	        	  
	        	  //System.out.println("Unexpected: !resultSet.next()");
	          }
	          
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        }

	    return lastInsertId;   
	}


	public boolean deleteDatabaseUsingDatabaseModel(DatabaseModel databaseModel) {
		
		Connection serverConnection = databaseModel.getNewServerConnection();
		
		if (serverConnection != null) {
		
			 try {
			  
				Statement statement = serverConnection.createStatement();
				statement.executeUpdate("DROP DATABASE `" + databaseModel.getName() +  "`;");
				statement.close();
		        
		        return true;
			  }
			  catch (SQLException sqlException){
				  
				  sqlException.printStackTrace();
				
			  }
			  finally {
				  
				try {
					serverConnection.close();
				} 
				catch (SQLException e) {
					e.printStackTrace();
				}
				
			  }
			  
			  return false;

		} else {
			
			//CRUD should not have been attempted unless isServerConnectable
			this.logger.severe("serverConnection is null");
			return false;
		}
	}
	
}