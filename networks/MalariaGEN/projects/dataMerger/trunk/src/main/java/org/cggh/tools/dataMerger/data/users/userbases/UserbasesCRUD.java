package org.cggh.tools.dataMerger.data.users.userbases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;



public class UserbasesCRUD {

	public UserbaseModel retrieveUserbaseAsUserbaseModelUsingServletContext(
			ServletContext servletContext) {
		
		UserbaseModel userbaseModel = new UserbaseModel();
		

		
		userbaseModel.setDatabaseDriverFullyQualifiedClassName(servletContext.getInitParameter("userDatabaseDriverFullyQualifiedClassName"));
		userbaseModel.setDatabaseServerPath(servletContext.getInitParameter("userDatabaseServerPath"));
		userbaseModel.setDatabaseName(servletContext.getInitParameter("userDatabaseName"));
		userbaseModel.setDatabaseUsername(servletContext.getInitParameter("userDatabaseUsernameName"));
		userbaseModel.setDatabasePassword(servletContext.getInitParameter("userDatabasePasswordName"));
		
		userbaseModel.setDatabaseTableName(servletContext.getInitParameter("userDatabaseTableName"));
		userbaseModel.setUsernameColumnName(servletContext.getInitParameter("userUsernameColumnName"));
		userbaseModel.setPasswordHashColumnName(servletContext.getInitParameter("userbasePasswordHashColumnName"));
		userbaseModel.setPasswordHashFunctionName(servletContext.getInitParameter("userbasePasswordHashFunctionName"));
		
		
		// Determine connect-ability.		
		userbaseModel.setDatabaseServerConnectable(this.retrieveDatabaseServerConnectableUsingUserbaseModel(userbaseModel));
		userbaseModel.setDatabaseConnectable(this.retrieveDatabaseConnectableUsingUserbaseModel(userbaseModel));

		if (userbaseModel.isDatabaseConnectable()) {
		
			//TODO: See if can connect to table
			
		}
		
		return userbaseModel;
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
