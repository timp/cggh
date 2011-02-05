package org.cggh.tools.dataMerger.scripts;

import org.cggh.tools.dataMerger.scripts.ScriptsModel;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
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

    public void createDatabase () {

    }     
    
    
    
    
    //In case this needs to be a servlet
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();		
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
		
		if (request.getPathInfo().equals("/install-db-v0.0.1")) {
			
			try {
				
				Class.forName("com.mysql.jdbc.Driver").newInstance(); 
				Connection connection = DriverManager.getConnection(getServletContext().getInitParameter("dbPath"), getServletContext().getInitParameter("dbUsername"), getServletContext().getInitParameter("dbPassword"));
				 
				if (!connection.isClosed()) {
			
					out.println("<p>Connected to database server.</p>");
				
					  try {
				        Statement statement = connection.createStatement();
				        statement.executeUpdate("CREATE DATABASE datamerger CHARACTER SET UTF8 COLLATE utf8_bin;");
				      }
				      catch (SQLException sqlException){
				    	  out.println("<p>" + sqlException + "</p>");
				    	  sqlException.printStackTrace();
				      }
				
				      try{
				          Statement statement = connection.createStatement();
				          statement.executeUpdate("CREATE TABLE datamerger.user (id TINYINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, username VARCHAR(255) NOT NULL, PRIMARY KEY (id), CONSTRAINT unique_username_constraint UNIQUE (username)) ENGINE=InnoDB;");

				        }
				        catch(SQLException sqlException){
				        	out.println("<p>" + sqlException + "</p>");
					    	sqlException.printStackTrace();
				        }	
				        
					      try{
					          Statement statement = connection.createStatement();
					          statement.executeUpdate("CREATE TABLE datamerger.upload (" + 
					        		  "id TINYINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
					        		  "path VARCHAR(255) NOT NULL, " + 
					        		  "created_by_user_id TINYINT(255) UNSIGNED NOT NULL, " + 
					        		  "created_on_datetime DATETIME NOT NULL, " +
					        		  "PRIMARY KEY (id), " +
					        		  "CONSTRAINT unique_path_constraint UNIQUE (path), " +
					        		  "INDEX created_by_user_id_index (created_by_user_id), " + 
					        		  "FOREIGN KEY (created_by_user_id) REFERENCES user(id) " + 
					        		  "ON DELETE CASCADE " + 
					        		  "ON UPDATE CASCADE " + 
					        		  ") ENGINE=InnoDB;");

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
				System.out.println("Failed to connect to database server. Using path " + getServletContext().getInitParameter("dbPath") + ", user " + getServletContext().getInitParameter("dbUsername"));
				out.println("<p>" + exception + "</p>");
				exception.printStackTrace();
			}

	         out.close();			

		}
		else if (request.getPathInfo().equals("/uninstall-db-v0.0.1")) {

			try {
				
				Class.forName("com.mysql.jdbc.Driver").newInstance(); 
				Connection connection = DriverManager.getConnection(getServletContext().getInitParameter("dbPath"), getServletContext().getInitParameter("dbUsername"), getServletContext().getInitParameter("dbPassword"));
				 
				if (!connection.isClosed()) {
					
					  try {
				        Statement statement = connection.createStatement();
				        statement.executeUpdate("DROP DATABASE dataMerger;");
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
				System.out.println("Failed to connect to database server. Using path " + getServletContext().getInitParameter("dbPath") + ", user " + getServletContext().getInitParameter("dbUsername"));
				out.println("<p>" + exception + "</p>");
				exception.printStackTrace();
			}

	         out.close();			
			
			
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

	         out.close();			
			
		}


		
		
	}

}
