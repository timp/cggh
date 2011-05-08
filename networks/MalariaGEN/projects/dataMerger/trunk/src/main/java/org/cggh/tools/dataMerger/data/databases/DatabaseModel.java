package org.cggh.tools.dataMerger.data.databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.installation.InstallationModel;

public class DatabaseModel {

	private String driverFullyQualifiedClassName;
	private String name;
	private String username;
	private String password;
	private Boolean connectable;
	private String serverPath;
	private Boolean serverConnectable;
	private String versionAsString;
	private CachedRowSet tablesAsCachedRowSet;
	private InstallationModel currentInstallationModel;
	private ServletContext servletContext;

	public DatabaseModel () {

		try {
			Class<?> cachedRowSetImplClass = Class.forName("com.sun.rowset.CachedRowSetImpl");
			this.setTablesAsCachedRowSet((CachedRowSet) cachedRowSetImplClass.newInstance());
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	public void setDriverFullyQualifiedClassName(
			String driverFullyQualifiedClassName) {
		this.driverFullyQualifiedClassName = driverFullyQualifiedClassName;
	}

	public String getDriverFullyQualifiedClassName() {
		return driverFullyQualifiedClassName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setConnectable(Boolean connectable) {
		this.connectable = connectable;
	}

	public Boolean isConnectable() {
		return connectable;
	}

	public void setServerPath(String serverPath) {
		this.serverPath = serverPath;
	}

	public String getServerPath() {
		return serverPath;
	}

	public void setServerConnectable(Boolean serverConnectable) {
		this.serverConnectable = serverConnectable;
	}

	public Boolean isServerConnectable() {
		return serverConnectable;
	}

	public void setVersionAsString(String versionAsString) {
		this.versionAsString = versionAsString;
	}

	public String getVersionAsString() {
		return versionAsString;
	}

	public Connection getNewConnection() {
		
		Connection connection = null;

		try {
			
			Class.forName(this.getDriverFullyQualifiedClassName()).newInstance();
			connection = DriverManager.getConnection(this.getServerPath() + this.getName(), this.getUsername(), this.getPassword());
			
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
	
	public Connection getNewServerConnection() {
		
		Connection connection = null;

		try {
			
			Class.forName(this.getDriverFullyQualifiedClassName()).newInstance();
			connection = DriverManager.getConnection(this.getServerPath(), this.getUsername(), this.getPassword());
			
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

	public void setTablesAsCachedRowSet(CachedRowSet tablesAsCachedRowSet) {
		this.tablesAsCachedRowSet = tablesAsCachedRowSet;
	}

	public CachedRowSet getTablesAsCachedRowSet() {
		return tablesAsCachedRowSet;
	}

	public void setCurrentInstallationModel (InstallationModel currentInstallationModel) {
		this.currentInstallationModel = currentInstallationModel;
	}

	public InstallationModel getCurrentInstallationModel() {
		return currentInstallationModel;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	
}
