package org.cggh.tools.dataMerger.data.users;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class UsersModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 269809433850318241L;
	private HttpServletRequest httpServletRequest;
	private UserModel currentUser;
	
	public UsersModel() {
		
		//Note: Can't do this on construction because request object gets set after construction (when the bean is initialized).
		//this.currentUser = new UserModel(httpServletRequest.getRemoteUser());
		
		this.currentUser = new UserModel();
	}

    public void setHttpServletRequest (final HttpServletRequest  httpServletRequest ) {
        this.httpServletRequest  = httpServletRequest;
    }
    public HttpServletRequest getHttpServletRequest () {
        return this.httpServletRequest;
    }  	
	
	public void setCurrentUser (final UserModel user) {
		
		this.currentUser = user;
	}
	public UserModel getCurrentUser () {
		
		return this.currentUser;
	}	
	
	
	
	
	public void createUser (String username) {

		ServletContext servletContext = getHttpServletRequest().getSession().getServletContext();
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver").newInstance(); 
			Connection connection = DriverManager.getConnection(servletContext.getInitParameter("dbBasePath") + servletContext.getInitParameter("dbName"), servletContext.getInitParameter("dbUsername"), servletContext.getInitParameter("dbPassword"));
			 
			if (!connection.isClosed()) {		
		
		      try{
		          PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user (username) VALUES (?);");
		          preparedStatement.setString(1, username);
		          preparedStatement.executeUpdate();
		          preparedStatement.close();
	
		        }
		        catch(SQLException sqlException){
		        	System.out.println(sqlException);
			    	sqlException.printStackTrace();
		        } 

		        connection.close();	        

			} else {
				System.out.println("connection.isClosed");
			}
				
		} 
		catch (Exception exception) {
			System.out.println("Failed to connect to database server.");
			System.out.println(exception);
			exception.printStackTrace();
		}
			
			
	}
	
}
