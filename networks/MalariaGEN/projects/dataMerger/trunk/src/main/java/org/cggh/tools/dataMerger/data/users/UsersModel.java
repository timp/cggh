package org.cggh.tools.dataMerger.data.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.cggh.tools.dataMerger.data.DataModel;

public class UsersModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 269809433850318241L;
	private HttpServletRequest httpServletRequest;
	private DataModel dataModel;	
	private UserModel userModel;
	
	public UsersModel() {
		
		this.setDataModel(new DataModel());		
		this.setUserModel(new UserModel());
	}

    public void setDataModel (final DataModel dataModel) {
        this.dataModel  = dataModel;
    }
    public DataModel getDataModel () {
        return this.dataModel;
    } 	
	
    public void setHttpServletRequest (final HttpServletRequest  httpServletRequest ) {
        this.httpServletRequest  = httpServletRequest;
    }
    public HttpServletRequest getHttpServletRequest () {
        return this.httpServletRequest;
    }  	
	
	public void setUserModel (final UserModel userModel) {
		
		this.userModel = userModel;
	}
	public UserModel getUserModel () {
		
		return this.userModel;
	}	
	
	public Boolean isUsernameCreated (final String username) {

		Boolean usernameCreated = null;
		
		try {
			
			this.getDataModel().createConnection();
			Connection connection = this.getDataModel().getConnection();
			
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
	
	
	
	public void createUserByUsername (String username) {

		try {
			
			this.getDataModel().createConnection();
			Connection connection = this.getDataModel().getConnection();
			
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
