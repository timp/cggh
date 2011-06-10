package org.cggh.tools.dataMerger.users.userbases;

public class UserbaseModel {

	
	private String databaseDriverFullyQualifiedClassName;
	private String databaseServerPath;
	private String databaseName;
	private String databaseUsername;
	private String databasePassword;
	private String tableName;
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

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
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

	
}
