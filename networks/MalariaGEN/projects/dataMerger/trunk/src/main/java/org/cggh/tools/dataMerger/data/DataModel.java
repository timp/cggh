package org.cggh.tools.dataMerger.data;

import java.sql.Connection;
import java.sql.DriverManager;
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

	/**
	 * 
	 */
	

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
	
	public Connection getConnection() {
		
		
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
		
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(this.getURL(), this.getUsername(), this.getPassword());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return connection;
		
		
	}
	
}