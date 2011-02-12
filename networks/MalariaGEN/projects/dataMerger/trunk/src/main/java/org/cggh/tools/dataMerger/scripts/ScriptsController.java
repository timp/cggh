package org.cggh.tools.dataMerger.scripts;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Servlet implementation class uploads
 */
public class ScriptsController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1244304670693323712L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ScriptsController() {
        super();

        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();		
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
		
        //TODO: Factor these out into separate classes. Use interfaces?
        
		if (request.getPathInfo().equals("/install-db-v0.0.1")) {
			
			//InstallDb_0_0_1 installDb_0_0_1 = new InstallDb_0_0_1();
			//installDb_0_0_1.run();
			
			//ScriptController scriptController = new ScriptController("install-db-v0.0.1");
			//scriptController.run();
			
			//dataMerger.scripts.getScript("install-db-v0.0.1").run();
			
			//dataMerger.scripts.runScript("install-db-v0.0.1");
			
			try {
				
				Class.forName("com.mysql.jdbc.Driver").newInstance(); 
				Connection connection = DriverManager.getConnection(getServletContext().getInitParameter("dbBasePath"), getServletContext().getInitParameter("dbUsername"), getServletContext().getInitParameter("dbPassword"));
				 
				if (!connection.isClosed()) {
			
					out.println("<p>Connected to database server.</p>");
				
						//NB: Can't pass tablenames directly into a prepared statement, have to use the string.

					  try {
						  
				        Statement statement = connection.createStatement();
				        statement.executeUpdate("CREATE DATABASE `" + getServletContext().getInitParameter("dbName") +  "` CHARACTER SET UTF8 COLLATE utf8_bin;");
				        statement.close();
				       
				      }
				      catch (SQLException sqlException){
				    	  out.println("<p>" + sqlException + "</p>");
				    	  sqlException.printStackTrace();
				      }
				      
					  try {
						  
					        Statement statement = connection.createStatement();
					        statement.executeUpdate("USE `" + getServletContext().getInitParameter("dbName") +  "`;");
					        statement.close();
					       
					      }
					      catch (SQLException sqlException){
					    	  out.println("<p>" + sqlException + "</p>");
					    	  sqlException.printStackTrace();
					      }				      
				
				      try{
				          Statement statement = connection.createStatement();
				          statement.executeUpdate("CREATE TABLE user (id TINYINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, username VARCHAR(255) NOT NULL, PRIMARY KEY (id), CONSTRAINT unique_username_constraint UNIQUE (username)) ENGINE=InnoDB;");
				          statement.close();

				        }
				        catch(SQLException sqlException){
				        	out.println("<p>" + sqlException + "</p>");
					    	sqlException.printStackTrace();
				        }	
				        
					      try{
					          Statement statement = connection.createStatement();
					          statement.executeUpdate("CREATE TABLE upload (" + 
					        		  "id TINYINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
					        		  "original_filename VARCHAR(255) NOT NULL, " + 
					        		  "repository_path VARCHAR(255) NULL, " + 
					        		  "successful BOOLEAN NULL, " + 
					        		  "created_by_user_id TINYINT(255) UNSIGNED NOT NULL, " + 
					        		  "created_on_datetime DATETIME NOT NULL, " +
					        		  "PRIMARY KEY (id), " +
					        		  "CONSTRAINT unique_path_constraint UNIQUE (repository_path), " +
					        		  "INDEX created_by_user_id_index (created_by_user_id), " + 
					        		  "FOREIGN KEY (created_by_user_id) REFERENCES user(id) " + 
					        		  "ON DELETE CASCADE " + 
					        		  "ON UPDATE CASCADE " + 
					        		  ") ENGINE=InnoDB;");
					          statement.close();

					        }
					        catch(SQLException sqlException){
					        	out.println("<p>" + sqlException + "</p>");
						    	sqlException.printStackTrace();
					        } 
				        

					        // Insert the current user into the user table
					        
						      try{
						          PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user (username) VALUES (?);");
						          preparedStatement.setString(1, request.getRemoteUser());
						          preparedStatement.executeUpdate();
						          preparedStatement.close();

						        }
						        catch(SQLException sqlException){
						        	out.println("<p>" + sqlException + "</p>");
							    	sqlException.printStackTrace();
						        } 					        
				        
			
					connection.close();
					out.println("Done.");
					
				} else {
		        	out.println("<p>connection.isClosed</p>");
				}
					
			} 
			catch (Exception exception) {
				System.out.println("Failed to connect to database server.");
				out.println("<p>" + exception + "</p>");
				exception.printStackTrace();
			}
		

		}
		else if (request.getPathInfo().equals("/uninstall-db-v0.0.1")) {

			try {
				
				Class.forName("com.mysql.jdbc.Driver").newInstance(); 
				Connection connection = DriverManager.getConnection(getServletContext().getInitParameter("dbBasePath"), getServletContext().getInitParameter("dbUsername"), getServletContext().getInitParameter("dbPassword"));
				 
				if (!connection.isClosed()) {
					
					  try {
				        Statement statement = connection.createStatement();
				        
				        statement.executeUpdate("DROP DATABASE `" + getServletContext().getInitParameter("dbName") +  "`;");
				      }
				      catch (SQLException sqlException){
				    	  out.println("<p>" + sqlException + "</p>");
				    	  sqlException.printStackTrace();
				      }	
				      
				      
				      connection.close();
				      out.println("Done.");
					
				} else {
		        	out.println("<p>connection.isClosed</p>");
				}
			
			
			} 
			catch (Exception exception) {
				System.out.println("Failed to connect to database server.");
				out.println("<p>" + exception + "</p>");
				exception.printStackTrace();
			}

			
		} else {
			
//	     	{
//	  	  "foo": "The quick brown fox jumps over the lazy dog.",
//	  	  "bar": "ABCDEFG",
//	  	  "baz": [52, 97]
//	  	}
	   		
			
	        JSONArray list = new JSONArray();
	        list.put(52);
	        list.put("er2... " + request.getPathInfo());		
			
			 JSONObject json = new JSONObject();
			 
	         try {
	 
	             json.put("foo", "The quick brown fox jumps over the lazy dog.");
	 
	             json.put("bar", "ABCDEFG");

	             json.put("baz", list);
	 
	 
	         } catch (JSONException e) {
	 
	             e.printStackTrace();
	 
	         }

	        

	         
	         response.setContentType("application/json");
	         response.setCharacterEncoding("UTF-8");
	         out.println(json);

		}


		out.close();
		
	}

}
