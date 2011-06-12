package org.cggh.tools.dataMerger.data.users.userbases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UserbaseModel {

	
	private String databaseDriverFullyQualifiedClassName;
	private String databaseServerPath;
	private String databaseName;
	private String databaseUsername;
	private String databasePassword;
	private String databaseTableName;
	private String usernameColumnName;
	private String passwordHashColumnName;
	private String passwordHashFunctionName;
	private Boolean databaseServerConnectable;
	private Boolean databaseConnectable;

	public void setDatabaseDriverFullyQualifiedClassName(
			String databaseDriverFullyQualifiedClassName) {
		this.databaseDriverFullyQualifiedClassName = databaseDriverFullyQualifiedClassName;
	}

	public String getDatabaseDriverFullyQualifiedClassName() {
		return databaseDriverFullyQualifiedClassName;
	}

	public void setDatabaseServerPath(String databaseServerPath) {
		this.databaseServerPath = databaseServerPath;
	}

	public String getDatabaseServerPath() {
		return databaseServerPath;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseUsername(String databaseUsername) {
		this.databaseUsername = databaseUsername;
	}

	public String getDatabaseUsername() {
		return databaseUsername;
	}

	public void setDatabasePassword(String databasePassword) {
		this.databasePassword = databasePassword;
	}

	public String getDatabasePassword() {
		return databasePassword;
	}

	public void setDatabaseTableName(String databaseTableName) {
		this.databaseTableName = databaseTableName;
	}

	public String getDatabaseTableName() {
		return databaseTableName;
	}

	public void setUsernameColumnName(String usernameColumnName) {
		this.usernameColumnName = usernameColumnName;
	}

	public String getUsernameColumnName() {
		return usernameColumnName;
	}

	public void setPasswordHashColumnName(String passwordHashColumnName) {
		this.passwordHashColumnName = passwordHashColumnName;
	}

	public String getPasswordHashColumnName() {
		return passwordHashColumnName;
	}

	public void setPasswordHashFunctionName(String passwordHashFunctionName) {
		this.passwordHashFunctionName = passwordHashFunctionName;
	}

	public String getPasswordHashFunctionName() {
		return passwordHashFunctionName;
	}

	public void setDatabaseConnectable(Boolean databaseConnectable) {
		this.databaseConnectable = databaseConnectable;
	}

	public Boolean isDatabaseConnectable() {
		return databaseConnectable;
	}

	public void setDatabaseServerConnectable(Boolean databaseServerConnectable) {
		this.databaseServerConnectable = databaseServerConnectable;
	}

	public Boolean isDatabaseServerConnectable() {
		return databaseServerConnectable;
	}

	public Connection getNewDatabaseConnection() {
		
		Connection connection = null;

		try {
			
			Class.forName(this.getDatabaseDriverFullyQualifiedClassName()).newInstance();
			connection = DriverManager.getConnection(this.getDatabaseServerPath() + this.getDatabaseName(), this.getDatabaseUsername(), this.getDatabasePassword());
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return connection;
	}
}
