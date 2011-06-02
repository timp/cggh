package org.cggh.tools.dataMerger.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

public class DataModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6445378947804522984L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data");
	private ServletContext servletContext = null;
	private String url = null;
	private String username = null;
	private String password = null;
	private int lastInsertId;
	private Boolean databaseConnectable = null;
	private String databaseDriverFullyQualifiedClassName = null;
	private String databaseServerPath = null;
	private String databaseName = null;


	public DataModel() {

	}

	//TODO: Add transaction support

	
	public void setDataModelUsingServletContext (final ServletContext servletContext) {
		
		this.setServletContext(servletContext);
		this.setDatabaseDriverFullyQualifiedClassName(servletContext.getInitParameter("databaseDriverFullyQualifiedClassName"));
		this.setDatabaseServerPath(servletContext.getInitParameter("databaseServerPath"));
		this.setDatabaseName(servletContext.getInitParameter("databaseName"));
		this.setURL(this.getDatabaseServerPath() + this.getDatabaseName());
		this.setUsername(servletContext.getInitParameter("databaseUsername"));
		this.setPassword(servletContext.getInitParameter("databasePassword"));
		this.setDatabaseConnectableUsingDataModel();
	}
	

	public void setServletContext (final ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	public ServletContext getServletContext () {
		return this.servletContext;
	}
	
	
	public void setURL (final String url) {
		
		this.url = url;
	}
	public String getURL () {
		
		return this.url;
	}	

	public void setUsername (final String username) {
		
		this.username = username;
	}
	public String getUsername () {
		
		return this.username;
	}	

	public void setPassword (final String password) {
		
		this.password = password;
	}
	public String getPassword () {
		
		return this.password;
	}	
	
	public Connection getNewDatabaseConnection() {
		
		Connection connection = null;

		try {
			Class.forName(this.getDriverFullyQualifiedClassName()).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {

			connection = DriverManager.getConnection(this.getURL(), this.getUsername(), this.getPassword());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return connection;
	}
	
	public Connection getNewDatabaseServerConnection() {
		
		Connection baseConnection = null;

		try {
			Class.forName(this.getDriverFullyQualifiedClassName()).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {

			baseConnection = DriverManager.getConnection(this.getDatabaseServerPath(), this.getUsername(), this.getPassword());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return baseConnection;
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

	public Integer getLastInsertId() {
		return this.lastInsertId;
	}

	public void setLastInsertId(int lastInsertId) {
		this.lastInsertId = lastInsertId;
	}

	
	
	public void setDatabaseConnectableUsingDataModel() {

		try {
			Class.forName(this.getDriverFullyQualifiedClassName()).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {

			Connection connection = DriverManager.getConnection(this.getURL(), this.getUsername(), this.getPassword());
			
			if (connection != null) {
				
						try {
							connection.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						
			}
			
			this.setDatabaseConnectable(true);
			
		} catch (SQLException e) {
			
			this.setDatabaseConnectable(false);
		}

	}

	public void setDatabaseConnectable(Boolean databaseConnectable) {
		this.databaseConnectable = databaseConnectable;
	}

	public Boolean isDatabaseConnectable() {
		return this.databaseConnectable;
	}

	public void setDatabaseDriverFullyQualifiedClassName(
			String driverFullyQualifiedClassName) {
		this.databaseDriverFullyQualifiedClassName = driverFullyQualifiedClassName;
	}

	public String getDriverFullyQualifiedClassName() {
		return this.databaseDriverFullyQualifiedClassName;
	}

	public Integer[] getVersionAsIntegerArray() {
		
		Integer[] versionAsIntegerArray = new Integer[3];
		
		if (this.isDatabaseConnectable()) {
			
			//look up application table current installation history (last installed) or version number 
			versionAsIntegerArray = new Integer[] {1,0,0};
		}
		
		return versionAsIntegerArray;
	}

	public void setDatabaseServerPath(String basePath) {
		this.databaseServerPath = basePath;
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

}