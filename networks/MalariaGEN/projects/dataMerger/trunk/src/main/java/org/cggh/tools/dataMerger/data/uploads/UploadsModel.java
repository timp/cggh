package org.cggh.tools.dataMerger.data.uploads;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class UploadsModel implements java.io.Serializable {

	private HttpServletRequest httpServletRequest;
	
	public UploadsModel() {
		
		//TODO: Get all the uploads?
		
	}

	public UploadsModel(String user) {
		
		//TODO: Get the uploads for a user (need username and roles(rolenames) )
		
	}


    public void setHttpServletRequest (final HttpServletRequest  httpServletRequest ) {
        this.httpServletRequest  = httpServletRequest;
    }
    public HttpServletRequest getHttpServletRequest () {
        return this.httpServletRequest;
    }    
    	
	
   public ResultSet getUploadsAsResultSet() {

	   ServletContext servletContext = getHttpServletRequest().getSession().getServletContext();
	   
	   ResultSet uploadsAsResultSet = null;
	   
		try {
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection connection = DriverManager.getConnection(servletContext.getInitParameter("dbBasePath") + servletContext.getInitParameter("dbName"), servletContext.getInitParameter("dbUsername"), servletContext.getInitParameter("dbPassword"));
			 
			if (!connection.isClosed()) {
		
				// Get the user_id
				Integer user_id = null;
				
			      try{
			          PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM user WHERE username = ?;");
			          preparedStatement.setString(1, getHttpServletRequest().getRemoteUser());
			          preparedStatement.executeQuery();
			          ResultSet resultSet = preparedStatement.getResultSet();

			          // There may be no user in the user table.
			          if (resultSet.next()) {
			        	  user_id = resultSet.getInt("id");
			          } else {
			        	  System.out.println("Did not find user in user table. This user is not registered. Db query gives !resultSet.next()");
			          }

			          resultSet.close();
			          preparedStatement.close();
			          
			        }
			        catch(SQLException sqlException){
			        	System.out.println("<p>" + sqlException + "</p>");
				    	sqlException.printStackTrace();
			        } 				
				
			      try{
			          PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, path, created_by_user_id, created_on_datetime FROM upload WHERE created_by_user_id = ?;");
			          preparedStatement.setInt(1, user_id);
			          preparedStatement.executeQuery();
			          uploadsAsResultSet = preparedStatement.getResultSet();
			          preparedStatement.close();

			        }
			        catch(SQLException sqlException){
			        	System.out.println("<p>" + sqlException + "</p>");
				    	sqlException.printStackTrace();
			        } 	
			
				connection.close();
				
			} else {
				
				System.out.println("connection.isClosed");
			}
				
		} 
		catch (Exception e) {
			System.out.println("Exception from getUploadsAsResultSet.");
			e.printStackTrace();
		}



     return(uploadsAsResultSet);
   }
}
