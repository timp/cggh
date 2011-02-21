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
	private Connection connection;


	public DataModel() {

	}

	public void setDataModelByServletContext (final ServletContext servletContext) {
		
		this.setServletContext(servletContext);
		this.setURL(servletContext.getInitParameter("dbBasePath") + servletContext.getInitParameter("dbName"));
		this.setUsername(servletContext.getInitParameter("dbUsername"));
		this.setPassword(servletContext.getInitParameter("dbPassword"));
		
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
	
	public void createConnection() {
		
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

			this.setConnection(DriverManager.getConnection(this.getURL(), this.getUsername(), this.getPassword()));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	public void setLastInsertIdByConnection(Connection connection) {

		this.setConnection(connection);
		
        try{
	    	  //TODO: Is this cross-db compatible? ref: @@IDENTITY
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT LAST_INSERT_ID();");
	          preparedStatement.executeQuery();
	          
	          ResultSet resultSet = preparedStatement.getResultSet();
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();
	        	  this.setLastInsertId(resultSet.getInt(1));
	        	  
	          } else {
	        	  
	        	  
	        	  System.out.println("Unexpected: !resultSet.next()");
	          }
	          
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        }

	}

	public Integer getLastInsertId() {
		return this.lastInsertId;
	}

	public void setLastInsertId(int lastInsertId) {
		this.lastInsertId = lastInsertId;
	}

	public void setConnection(Connection connection) {

		//TODO: sanity check.
		if (this.getConnection() != null) {
			try {
				this.getConnection().close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		this.connection = connection;
	}
	public Connection getConnection() {
		return this.connection;
	}	
	
	
}