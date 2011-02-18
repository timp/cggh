package org.cggh.tools.dataMerger.data.uploads;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.CachedRowSet;

public class UploadsModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4112178863119955390L;
	private HttpServletRequest httpServletRequest;
	
	public UploadsModel() {

	}



    public void setHttpServletRequest (final HttpServletRequest  httpServletRequest ) {
        this.httpServletRequest  = httpServletRequest;
    }
    public HttpServletRequest getHttpServletRequest () {
        return this.httpServletRequest;
    }    
    	
	
   public CachedRowSet getUploadsAsCachedRowSet() {

	   ServletContext servletContext = this.getHttpServletRequest().getSession().getServletContext();
	   
	   String CACHED_ROW_SET_IMPL_CLASS = "com.sun.rowset.CachedRowSetImpl";
	   
	   CachedRowSet uploadsAsCachedRowSet = null;
	   
		try {
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection connection = DriverManager.getConnection(servletContext.getInitParameter("dbBasePath") + servletContext.getInitParameter("dbName"), servletContext.getInitParameter("dbUsername"), servletContext.getInitParameter("dbPassword"));
			 
			if (!connection.isClosed()) {
		
				// Get the user_id
				Integer user_id = null;
				
			      try{
			          PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM user WHERE username = ?;");
			          preparedStatement.setString(1, this.getHttpServletRequest().getRemoteUser());
			          preparedStatement.executeQuery();
			          ResultSet resultSet = preparedStatement.getResultSet();

			          // There may be no user in the user table.
			          if (resultSet.next()) {
			        	  resultSet.first();
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
			          PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, original_filename, created_by_user_id, created_datetime FROM upload WHERE created_by_user_id = ? AND successful = 1;");
			          preparedStatement.setInt(1, user_id);
			          preparedStatement.executeQuery();
			          Class<?> cachedRowSetImplClass = Class.forName(CACHED_ROW_SET_IMPL_CLASS);
			          uploadsAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
			          uploadsAsCachedRowSet.populate(preparedStatement.getResultSet());
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
			System.out.println("Exception from getUploadsAsCachedRowSet.");
			e.printStackTrace();
		}



     return(uploadsAsCachedRowSet);
   }
}
