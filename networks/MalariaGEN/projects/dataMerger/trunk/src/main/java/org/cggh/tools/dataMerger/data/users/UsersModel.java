package org.cggh.tools.dataMerger.data.users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
		
		this.setCurrentUser(new UserModel());
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
	
	public Boolean isUsernameCreated (final String username) {
		
		ServletContext servletContext = getHttpServletRequest().getSession().getServletContext();
		
		Boolean usernameCreated = null;
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver").newInstance(); 
			Connection connection = DriverManager.getConnection(servletContext.getInitParameter("dbBasePath") + servletContext.getInitParameter("dbName"), servletContext.getInitParameter("dbUsername"), servletContext.getInitParameter("dbPassword"));
			 
			if (!connection.isClosed()) {		
		
		      try{
		          PreparedStatement preparedStatement = connection.prepareStatement("SELECT username FROM user WHERE username = ?;");
		          preparedStatement.setString(1, username);
		          preparedStatement.executeQuery();
		          
		          ResultSet resultSet = preparedStatement.getResultSet();
		          
		          if (resultSet.next()) {
		        	  
		        	  resultSet.first();
		        	  
		        	  if (resultSet.getString("username").equals(username)) {
		        		  
		        		  usernameCreated = true;
		        		  
		        	  } else {
		        		  
		        		  // Sanity check. Username found by query is not the same username.
		        		  System.out.println("Unexpected: username parameter != username from query ");
		        		  
		        	  }
		        	  
		        	  
		      	  } else {
		      		  
		      		  // Username not found by query
		      		  usernameCreated = false;
		      	  }
		          
		          resultSet.close();
		          
		          preparedStatement.close();
	
		        }
		        catch(SQLException sqlException){
		        	System.out.println(sqlException);
			    	sqlException.printStackTrace();
		        } 

		        connection.close();	        

			} else {
				System.out.println("Unexpected: connection.isClosed");
			}
				
		} 
		catch (Exception exception) {
			exception.printStackTrace();
		}
		
		return usernameCreated;
		
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
				System.out.println("Unexpected: connection.isClosed");
			}
				
		} 
		catch (Exception exception) {
			exception.printStackTrace();
		}
			
			
	}
	
}
