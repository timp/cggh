package org.cggh.tools.dataMerger.data.users.userbases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.datatables.DatatablesCRUD;
import org.cggh.tools.dataMerger.data.files.FileOriginsCRUD;
import org.cggh.tools.dataMerger.data.users.UsersCRUD;



public class UserbasesCRUD {

	private final Logger logger = Logger.getLogger(this.getClass().getPackage().getName());
	
	
	public UserbaseModel retrieveUserbaseAsUserbaseModelUsingServletContext(
			ServletContext servletContext) {
		
		UserbaseModel userbaseModel = new UserbaseModel();
		

		
		userbaseModel.setDatabaseDriverFullyQualifiedClassName(servletContext.getInitParameter("userDatabaseDriverFullyQualifiedClassName"));
		userbaseModel.setDatabaseServerPath(servletContext.getInitParameter("userDatabaseServerPath"));
		userbaseModel.setDatabaseName(servletContext.getInitParameter("userDatabaseName"));
		userbaseModel.setDatabaseUsername(servletContext.getInitParameter("userDatabaseUsername"));
		userbaseModel.setDatabasePassword(servletContext.getInitParameter("userDatabasePassword"));
		
		userbaseModel.setDatabaseTableName(servletContext.getInitParameter("userDatabaseTableName"));
		userbaseModel.setUsernameColumnName(servletContext.getInitParameter("userUsernameColumnName"));
		userbaseModel.setPasswordHashColumnName(servletContext.getInitParameter("userbasePasswordHashColumnName"));
		userbaseModel.setPasswordHashFunctionName(servletContext.getInitParameter("userbasePasswordHashFunctionName"));
		
		
		// Determine connect-ability.		
		userbaseModel.setDatabaseServerConnectable(this.retrieveDatabaseServerConnectableUsingUserbaseModel(userbaseModel));
		userbaseModel.setDatabaseConnectable(this.retrieveDatabaseConnectableUsingUserbaseModel(userbaseModel));
		userbaseModel.setDatabaseDataRetrievable(this.retrieveDatabaseDataRetrievableUsingUserbaseModel(userbaseModel));
		
		return userbaseModel;
	}


	private Boolean retrieveDatabaseDataRetrievableUsingUserbaseModel(
			UserbaseModel userbaseModel) {
		
		Boolean dataRetrievable = null;

		if (userbaseModel.isDatabaseConnectable()) {
		
			Connection connection = userbaseModel.getNewDatabaseConnection();
			 
			if (connection != null) {
			
			      try{
			          PreparedStatement preparedStatement = connection.prepareStatement(
			        		  
			        		  "SELECT `" + userbaseModel.getUsernameColumnName() + "`, `" + userbaseModel.getPasswordHashColumnName() + "` FROM `" + userbaseModel.getDatabaseTableName() + "` LIMIT 1;"
			        		  
			          );
			          preparedStatement.executeQuery();
			          ResultSet resultSet = preparedStatement.getResultSet();
			          
			          if (resultSet.next()) {
			        	  
				          dataRetrievable = true;
	
			      	  } else {
			      		  
			      		  //There is no data in the table.		      		  
				          dataRetrievable = false;
			      		  
			      	  }
			          
			          resultSet.close();
			          preparedStatement.close();
			          
	
			        }
			        catch(SQLException sqlException){
			        	
			        	//SQL is invalid.
				    	dataRetrievable = false;
				    	
			        } finally {
			        	
			        	try {
							connection.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
			        }
			        
				
				
			} else {
				//connection is null
				dataRetrievable = false;
			}
			
		} else {
			//database is not connectable
			dataRetrievable = false;
		}
		
		return dataRetrievable;
	}


	private Boolean retrieveDatabaseConnectableUsingUserbaseModel(
			UserbaseModel userbaseModel) {
		try {
			
			Class.forName(userbaseModel.getDatabaseDriverFullyQualifiedClassName()).newInstance();
			Connection connection = DriverManager.getConnection(userbaseModel.getDatabaseServerPath() + userbaseModel.getDatabaseName(), userbaseModel.getDatabaseUsername(), userbaseModel.getDatabasePassword());
					
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

	private Boolean retrieveDatabaseServerConnectableUsingUserbaseModel(
			UserbaseModel userbaseModel) {
		try {

			Class.forName(userbaseModel.getDatabaseDriverFullyQualifiedClassName()).newInstance();
			Connection connection = DriverManager.getConnection(userbaseModel.getDatabaseServerPath(), userbaseModel.getDatabaseUsername(), userbaseModel.getDatabasePassword());

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


}
