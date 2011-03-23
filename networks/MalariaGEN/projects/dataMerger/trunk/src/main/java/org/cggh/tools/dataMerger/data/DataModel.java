package org.cggh.tools.dataMerger.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;

public class DataModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6445378947804522984L;
	private ServletContext servletContext = null;
	private String url = null;
	private String username = null;
	private String password = null;
	private int lastInsertId;


	public DataModel() {

	}

	//TODO: Add transaction support
	
	public void setDataModelByServletContext (final ServletContext servletContext) {
		
		this.setServletContext(servletContext);
		this.setURL(servletContext.getInitParameter("databaseBasePath") + servletContext.getInitParameter("databaseName"));
		this.setUsername(servletContext.getInitParameter("databaseUsername"));
		this.setPassword(servletContext.getInitParameter("databasePassword"));
		
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
	
	public Connection getNewConnection() {
		
		Connection connection = null;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return connection;
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
	
	
}