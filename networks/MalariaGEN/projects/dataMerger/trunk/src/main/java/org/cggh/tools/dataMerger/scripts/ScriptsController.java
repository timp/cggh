package org.cggh.tools.dataMerger.scripts;

import java.io.File;
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();		
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
		
        //TODO: Factor these out into separate classes.
        
		if (request.getPathInfo().equals("/install-db-v0.0.1")) {
			
			//TODO: If install fails (note, cannot use transactions for table creations, etc.) then run the uninstall for the same version.
			
			
			//InstallDb_0_0_1 installDb_0_0_1 = new InstallDb_0_0_1();
			//installDb_0_0_1.run();
			
			//ScriptController scriptController = new ScriptController("install-db-v0.0.1");
			//scriptController.run();
			
			//dataMerger.scripts.getScript("install-db-v0.0.1").run();
			
			//dataMerger.scripts.runScript("install-db-v0.0.1");
			
			try {
				
				Class.forName("com.mysql.jdbc.Driver").newInstance(); 
				Connection connection = DriverManager.getConnection(getServletContext().getInitParameter("databaseBasePath"), getServletContext().getInitParameter("databaseUsername"), getServletContext().getInitParameter("databasePassword"));
				 
				if (!connection.isClosed()) {
			
					//TODO
					//this.log("Connected to database server.");
				
						//NB: Can't pass tablenames directly into a prepared statement, have to use the string.

					  try {
						  
				        Statement statement = connection.createStatement();
				        statement.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + getServletContext().getInitParameter("databaseName") +  "` CHARACTER SET UTF8 COLLATE utf8_bin;");
				        statement.close();
				       
				      }
				      catch (SQLException sqlException){
				    	  out.println("<p>" + sqlException + "</p>");
				    	  sqlException.printStackTrace();
				      }
				      
					  try {
						  
					        Statement statement = connection.createStatement();
					        statement.executeUpdate("USE `" + getServletContext().getInitParameter("databaseName") +  "`;");
					        statement.close();
					       
					      }
					      catch (SQLException sqlException){
					    	  out.println("<p>" + sqlException + "</p>");
					    	  sqlException.printStackTrace();
					      }				      
				
				      try{
				          Statement statement = connection.createStatement();
				          statement.executeUpdate("CREATE TABLE user (id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, username VARCHAR(255) NOT NULL, PRIMARY KEY (id), CONSTRAINT unique_username_constraint UNIQUE (username)) ENGINE=InnoDB;");
				          statement.close();

				        }
				        catch(SQLException sqlException){
				        	out.println("<p>" + sqlException + "</p>");
					    	sqlException.printStackTrace();
				        }	
				        
				        
				        
				        //TODO: Refactor code to use datatable_name in upload table, rather than datatable table.
				      try{
				          Statement statement = connection.createStatement();
				          statement.executeUpdate("CREATE TABLE upload (" + 
				        		  "id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
				        		  "original_filename VARCHAR(255) NOT NULL, " + 
				        		  "repository_filepath VARCHAR(255) NULL, " + 
				        		  "successful BOOLEAN NULL, " + 
				        		  "created_by_user_id BIGINT(255) UNSIGNED NOT NULL, " + 
				        		  "created_datetime DATETIME NOT NULL, " +
				        		  "datatable_name VARCHAR(255) NULL, " + 
				        		  "PRIMARY KEY (id), " +
				        		  "CONSTRAINT unique_path_constraint UNIQUE (repository_filepath), " +
				        		  "CONSTRAINT unique_datatable_name_constraint UNIQUE (datatable_name), " +
				        		  "INDEX created_by_user_id_index (created_by_user_id), " + 
				        		  "FOREIGN KEY (created_by_user_id) REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE " + 
				        		  ") ENGINE=InnoDB;");
				          statement.close();
	
				        }
				        catch(SQLException sqlException){
				        	out.println("<p>" + sqlException + "</p>");
					    	sqlException.printStackTrace();
				        } 
					      
				        
				        
						File uploadsFileRepositoryBasPath = new File(getServletContext().getInitParameter("uploadsFileRepositoryBasePath"));
						//this.log("uploadsFileRepositoryBasPath created: " + uploadsFileRepositoryBasPath.mkdirs());
				        
				        
				      try{
				          Statement statement = connection.createStatement();
				          statement.executeUpdate("CREATE TABLE merge (" + 
				        		  "id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
				        		  "upload_1_id BIGINT(255) UNSIGNED NOT NULL, " + 
				        		  "upload_2_id BIGINT(255) UNSIGNED NOT NULL, " + 
				        		  "created_by_user_id BIGINT(255) UNSIGNED NOT NULL, " + 
				        		  "created_datetime DATETIME NOT NULL, " +
				        		  "updated_datetime DATETIME NOT NULL, " +
				        		  "datatable_1_duplicate_keys_count BIGINT(255) UNSIGNED NULL, " + 
				        		  "datatable_2_duplicate_keys_count BIGINT(255) UNSIGNED NULL, " + 
				        		  "total_duplicate_keys_count BIGINT(255) UNSIGNED NULL, " + 
				        		  "total_conflicts_count BIGINT(255) UNSIGNED NULL, " +
				        		  "joined_keytable_name VARCHAR(255) NULL, " + 
				        		  "joined_datatable_name VARCHAR(255) NULL, " + 
				        		  "PRIMARY KEY (id), " +
				        		  "CONSTRAINT unique_joined_keytable_name_constraint UNIQUE (joined_keytable_name), " +
				        		  "INDEX created_by_user_id_index (created_by_user_id), " + 
				        		  "FOREIGN KEY (created_by_user_id) REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE, " +
				        		  "INDEX upload_1_id_index (upload_1_id), " + 
				        		  "FOREIGN KEY (upload_1_id) REFERENCES upload(id) ON DELETE CASCADE ON UPDATE CASCADE, " +
				        		  "INDEX upload_2_id_index (upload_2_id), " + 
				        		  "FOREIGN KEY (upload_2_id) REFERENCES upload(id) ON DELETE CASCADE ON UPDATE CASCADE " + 
				        		  ") ENGINE=InnoDB;");
				          statement.close();
		
				        }
				        catch(SQLException sqlException){
				        	out.println("<p>" + sqlException + "</p>");
					    	sqlException.printStackTrace();
				        } 
			    

				        //FIXME: Constraint for unique name
				      try{
				          Statement statement = connection.createStatement();
				          statement.executeUpdate("CREATE TABLE datatable (" + 
				        		  "id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
				        		  "name VARCHAR(255) NOT NULL, " +
				        		  "upload_id BIGINT(255) UNSIGNED NOT NULL, " + 
				        		  "created_datetime DATETIME NOT NULL, " +
				        		  "PRIMARY KEY (id), " +
				        		  "CONSTRAINT unique_name_constraint UNIQUE (name), " +
				        		  "INDEX upload_id_index (upload_id), " + 
				        		  "FOREIGN KEY (upload_id) REFERENCES upload(id) ON DELETE CASCADE ON UPDATE CASCADE " +
				        		  ") ENGINE=InnoDB;");
				          statement.close();
	
				        }
				        catch(SQLException sqlException){
				        	out.println("<p>" + sqlException + "</p>");
					    	sqlException.printStackTrace();
				        } 				        
			        
				      try{
				    	  
				    	  //Note: (255) in BIGINT(255) is only the display width, not the capacity.
				    	  
				          Statement statement = connection.createStatement();
				          statement.executeUpdate("CREATE TABLE `join` (" + 
				        		  "merge_id BIGINT(255) UNSIGNED NOT NULL, " + 
				        		  "column_number INT(255) UNSIGNED NOT NULL, " +
				        		  "`key` BOOLEAN NULL, " + 
				        		  "datatable_1_column_name VARCHAR(255) NULL, " +
				        		  "datatable_2_column_name VARCHAR(255) NULL, " +
				        		  "constant_1 VARCHAR(255) NULL, " +
				        		  "constant_2 VARCHAR(255) NULL, " +
				        		  "column_name VARCHAR(255) NULL, " +
				        		  "PRIMARY KEY (merge_id, column_number), " +
				        		  "INDEX merge_id_index (merge_id), " + 
				        		  "FOREIGN KEY (merge_id) REFERENCES merge(id) ON DELETE CASCADE ON UPDATE CASCADE " +
				        		  ") ENGINE=InnoDB;");
				          statement.close();
	
				        }
				        catch(SQLException sqlException){
				        	out.println("<p>" + sqlException + "</p>");
					    	sqlException.printStackTrace();
				        } 

					        
					      try{
					    	  
					          Statement statement = connection.createStatement();
					          statement.executeUpdate("CREATE TABLE `solution_by_column` (" + 
					        		  "id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
					        		  "description VARCHAR(255) NOT NULL, " +
					        		  "PRIMARY KEY (id) " +
					        		  ") ENGINE=InnoDB;");
					          statement.close();
		
					        }
					        catch(SQLException sqlException){
					        	out.println("<p>" + sqlException + "</p>");
						    	sqlException.printStackTrace();
					        } 
				        
					      try{
					    	  
					          Statement statement = connection.createStatement();
					          statement.executeUpdate("INSERT INTO `solution_by_column` (" +
					        		  "description " +
					        		  ") VALUES ('Prefer source 1'), ('Prefer source 2'), ('Use NULL'), ('Use CONSTANT');");
					          statement.close();
		
					        }
					        catch(SQLException sqlException){
					        	out.println("<p>" + sqlException + "</p>");
						    	sqlException.printStackTrace();
					        }						        
						   
				

					try{
			    	  
			          Statement statement = connection.createStatement();
			          statement.executeUpdate("CREATE TABLE `solution_by_row` (" + 
			        		  "id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
			        		  "description VARCHAR(255) NOT NULL, " +
			        		  "PRIMARY KEY (id) " +
			        		  ") ENGINE=InnoDB;");
			          statement.close();

			        }
			        catch(SQLException sqlException){
			        	out.println("<p>" + sqlException + "</p>");
				    	sqlException.printStackTrace();
			        } 
		        
			        try{
			    	  
			          Statement statement = connection.createStatement();
			          statement.executeUpdate("INSERT INTO `solution_by_row` (" +
			        		  "description " +
			        		  ") VALUES ('Prefer source 1'), ('Prefer source 2'), ('Use NULL'), ('Use CONSTANT'), ('Remove entire row');");
			          statement.close();

			        }
			        catch(SQLException sqlException){
			        	out.println("<p>" + sqlException + "</p>");
				    	sqlException.printStackTrace();
			        }
					   


			        
			        
				      try{
				    	  
				          Statement statement = connection.createStatement();
				          statement.executeUpdate("CREATE TABLE `solution_by_cell` (" + 
				        		  "id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
				        		  "description VARCHAR(255) NOT NULL, " +
				        		  "PRIMARY KEY (id) " +
				        		  ") ENGINE=InnoDB;");
				          statement.close();
	
				        }
				        catch(SQLException sqlException){
				        	out.println("<p>" + sqlException + "</p>");
					    	sqlException.printStackTrace();
				        } 
			        
				      try{
				    	  
				          Statement statement = connection.createStatement();
				          statement.executeUpdate("INSERT INTO `solution_by_cell` (" +
				        		  "description " +
				        		  ") VALUES ('Prefer source 1'), ('Prefer source 2'), ('Use NULL'), ('Use CONSTANT');");
				          statement.close();
	
				        }
				        catch(SQLException sqlException){
				        	out.println("<p>" + sqlException + "</p>");
					    	sqlException.printStackTrace();
				        }
			        

			       
				        
					      try{
					    	  
					          Statement statement = connection.createStatement();
					          statement.executeUpdate("CREATE TABLE `conflict` (" + 
					        		  "id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
					        		  "description VARCHAR(255) NOT NULL, " +
					        		  "PRIMARY KEY (id) " +
					        		  ") ENGINE=InnoDB;");
					          statement.close();
		
					        }
					        catch(SQLException sqlException){
					        	out.println("<p>" + sqlException + "</p>");
						    	sqlException.printStackTrace();
					        }

						      try{
						    	  
						          Statement statement = connection.createStatement();
						          statement.executeUpdate("INSERT INTO `conflict` (" +
						        		  "description " +
						        		  ") VALUES ('conflicting values'), ('NULL in source 1 versus value in source 2'), ('NULL in source 2 versus value in source 1');");
						          statement.close();
			
						        }
						        catch(SQLException sqlException){
						        	out.println("<p>" + sqlException + "</p>");
							    	sqlException.printStackTrace();
						        }				        

					      try{
					    	  
					          Statement statement = connection.createStatement();
					          statement.executeUpdate("CREATE TABLE `resolution` (" + 
					        		  "merge_id BIGINT(255) UNSIGNED NOT NULL, " +
					        		  "joined_keytable_id BIGINT(255) UNSIGNED NOT NULL, " +
					        		  "column_number INT(255) UNSIGNED NOT NULL, " +
					        		  "conflict_id BIGINT(255) NOT NULL, " +
					        		  "solution_by_column_id BIGINT(255) NULL, " +
					        		  "solution_by_row_id BIGINT(255) NULL, " +
					        		  "solution_by_cell_id BIGINT(255) NULL, " +
					        		  "constant VARCHAR(255) NULL, " +
					        		  "PRIMARY KEY (merge_id, joined_keytable_id, column_number), " +
					        		  "INDEX merge_id_index (merge_id), " +
					        		  "INDEX joined_keytable_id_index (joined_keytable_id), " + 
					        		  "INDEX column_number_index (column_number), " + 
					        		  "FOREIGN KEY (merge_id) REFERENCES merge(id) ON DELETE CASCADE ON UPDATE CASCADE " +
					        		  ") ENGINE=InnoDB;");
					          statement.close();

					        }
					        catch(SQLException sqlException){
					        	out.println("<p>" + sqlException + "</p>");
						    	sqlException.printStackTrace();
					        } 				        
				        
					        
						      try{
						          Statement statement = connection.createStatement();
						          statement.executeUpdate("CREATE TABLE export (" + 
						        		  "id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
						        		  "upload_1_id BIGINT(255) UNSIGNED NOT NULL, " +
						        		  "upload_2_id BIGINT(255) UNSIGNED NOT NULL, " +
						        		  "created_by_user_id BIGINT(255) UNSIGNED NOT NULL, " + 
						        		  "created_datetime DATETIME NOT NULL, " +
						        		  "merged_datatable_name VARCHAR(255) NULL, " + 
						        		  "merged_datatable_export_repository_filepath VARCHAR(255) NULL, " +
						        		  "joins_export_repository_filepath VARCHAR(255) NULL, " +
						        		  "resolutions_export_repository_filepath VARCHAR(255) NULL, " +
						        		  "merged_datatable_export_successful BOOLEAN NULL, " +
						        		  "joins_export_successful BOOLEAN NULL, " +
						        		  "resolutions_export_successful BOOLEAN NULL, " +
						        		  "PRIMARY KEY (id), " +
						        		  "CONSTRAINT unique_merged_datatable_export_repository_filepath_constraint UNIQUE (merged_datatable_export_repository_filepath), " +
						        		  "CONSTRAINT unique_joins_export_repository_filepath_constraint UNIQUE (joins_export_repository_filepath), " +
						        		  "CONSTRAINT unique_resolutions_export_repository_filepath_constraint UNIQUE (resolutions_export_repository_filepath), " +
						        		  "INDEX created_by_user_id_index (created_by_user_id), " + 
						        		  "FOREIGN KEY (created_by_user_id) REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE, " +
						        		  "INDEX upload_1_id_index (upload_1_id), " + 
						        		  "FOREIGN KEY (upload_1_id) REFERENCES upload(id) ON DELETE CASCADE ON UPDATE CASCADE, " +
						        		  "INDEX upload_2_id_index (upload_2_id), " + 
						        		  "FOREIGN KEY (upload_2_id) REFERENCES upload(id) ON DELETE CASCADE ON UPDATE CASCADE " + 
						        		  ") ENGINE=InnoDB;");
						          statement.close();
			
						        }
						        catch(SQLException sqlException){
						        	out.println("<p>" + sqlException + "</p>");
							    	sqlException.printStackTrace();
						        }
					        
						        File exportsFileRepositoryBasPath = new File(getServletContext().getInitParameter("exportsFileRepositoryBasePath"));
								//this.log("exportsFileRepositoryBasPath created: " + exportsFileRepositoryBasPath.mkdirs());
						        
						        
					connection.close();
					out.println("Done.");
					
				} else {
		        	out.println("<p>connection.isClosed</p>");
				}
					
			} 
			catch (Exception exception) {
				//System.out.println("Failed to connect to database server.");
				out.println("<p>" + exception + "</p>");
				exception.printStackTrace();
			}
		

		}
		else if (request.getPathInfo().equals("/uninstall-db-v0.0.1")) {

			try {
				
				Class.forName("com.mysql.jdbc.Driver").newInstance(); 
				Connection connection = DriverManager.getConnection(getServletContext().getInitParameter("databaseBasePath"), getServletContext().getInitParameter("databaseUsername"), getServletContext().getInitParameter("databasePassword"));
				 
				if (!connection.isClosed()) {
					
					  try {
				        Statement statement = connection.createStatement();
				        
				        statement.executeUpdate("DROP DATABASE IF EXISTS `" + getServletContext().getInitParameter("databaseName") +  "`;");
				      }
				      catch (SQLException sqlException){
				    	  out.println("<p>" + sqlException + "</p>");
				    	  sqlException.printStackTrace();
				      }	
				      
			
				      //TODO: Maybe factor this out into a separate script?
				      
				      //FIXME: This is really dangerous! What if the initParameter was set to a system folder?
				      
				      File uploadsFileRepositoryBasePath = new File(getServletContext().getInitParameter("uploadsFileRepositoryBasePath"));

					   // Get all files in directory
	
					   File[] uploads = uploadsFileRepositoryBasePath.listFiles();
					   
					   if (uploads != null) {
						   for (File file : uploads) {
						      // Delete each file
		
						      if (!file.delete())
						      {
						          // Failed to delete file
		
						          //System.out.println("Failed to delete " + file);
						      }
						   }
					   }
				      
					      File exportsFileRepositoryBasePath = new File(getServletContext().getInitParameter("exportsFileRepositoryBasePath"));

						   // Get all files in directory
		
						   File[] exports = exportsFileRepositoryBasePath.listFiles();
						   
						   if (exports != null) {
							   
							   for (File folder : exports) {
								   
								   if (folder.isDirectory()) {
									   
									   //this.log("Got folder");
									   
									    for (File file : folder.listFiles()) {
									    	
									    	// Delete each file
											
										      if (!file.delete()) {
										    	  
										          // Failed to delete file
										          //System.out.println("Failed to delete " + file);
										          
										      }
									    
									    }
									    
									    if (!folder.delete()) {
									    	  
									          // Failed to delete folder
									          //System.out.println("Failed to delete " + folder);
									          
									      }
									      
								   }
								  
							   }
						   }
				      
				      connection.close();
				      out.println("Done.");
					
				} else {
		        	out.println("<p>connection.isClosed</p>");
				}
			
			
			} 
			catch (Exception exception) {
				out.println("<p>" + exception + "</p>");
				exception.printStackTrace();
			}

			
		} else {
			
//	     	{
//	  	  "foo": "The quick brown fox jumps over the lazy dog.",
//	  	  "bar": "ABCDEFG",
//	  	  "baz": [52, 97]
//	  	}
	   	
			//FIXME: get rid of this test
			
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
