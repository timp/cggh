package org.cggh.tools.dataMerger.scripts;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cggh.tools.dataMerger.data.databases.DatabaseModel;
import org.cggh.tools.dataMerger.data.databases.DatabasesCRUD;
import org.cggh.tools.dataMerger.data.users.UserModel;
import org.cggh.tools.dataMerger.data.users.UsersCRUD;
import org.cggh.tools.dataMerger.scripts.data.Database1_0CRUD;


/**
 * Servlet implementation class uploads
 */
public class ScriptsController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1244304670693323712L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.scripts");

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ScriptsController() {
        super();

        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    //TODO: Provide links to all the scripts that this requester can run.
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();		
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
		
        String scriptsControllerBasePathURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        
        //TODO: Factor these out into separate classes.
        

        if (request.getPathInfo().equals("/install-v1.0")) {
			
			//TODO: If install fails (note, cannot use transactions for table creations, etc.) then run the uninstall for the same version.

        	
        	DatabasesCRUD databasesCRUD = new DatabasesCRUD();
			DatabaseModel databaseModel = databasesCRUD.retrieveDatabaseAsDatabaseModelUsingServletContext(request.getSession().getServletContext());
			
			UsersCRUD usersCRUD = new UsersCRUD();
			UserModel userModel = usersCRUD.retrieveUserAsUserModelUsingUsername(request.getRemoteUser());
			
			Database1_0CRUD database1_0CRUD = new Database1_0CRUD();
			database1_0CRUD.setDatabaseModel(databaseModel);
			database1_0CRUD.setUserModel(userModel);
			
					
			if (database1_0CRUD.getDatabaseModel().getVersionAsString() == null || database1_0CRUD.getDatabaseModel().getVersionAsString() == "") {
				
				if (database1_0CRUD.create()) {
				
//					Filebase1_0CRUD filebase1_0CRUD = new Filebase1_0CRUD();
//					filebase1_0CRUD.setUserModel(userModel);
//					
//					if (filebase1_0CRUD.create()) {
//						
//						//success
//						
//					} else {
//						response.sendRedirect(scriptsControllerBasePathURL + "pages/guides/installation/errors/filebase-creation");
//					}
					
				
				} else {
					
					response.sendRedirect(scriptsControllerBasePathURL + "pages/guides/installation/errors/database-creation");
					
				}
				
			} else {

				//String currentVersionAsString = database1_0.getDatabaseModel().getVersionAsIntegerArray()[1] + "." + database1_0.getDatabaseModel().getVersionAsIntegerArray()[2] + "." + database1_0.getDatabaseModel().getVersionAsIntegerArray()[3];
				
				
				response.sendRedirect(scriptsControllerBasePathURL + "pages/guides/installation/errors/database-version");	
				
			}
		

		}
		else if (request.getPathInfo().equals("/uninstall-v1.0")) {

			try {
				
				//FIXME: Use DatabaseModel
				
				Class.forName(getServletContext().getInitParameter("databaseDriverFullyQualifiedClassName")).newInstance(); 
				
				Connection connection = DriverManager.getConnection(getServletContext().getInitParameter("databaseBasePath"), getServletContext().getInitParameter("databaseUsername"), getServletContext().getInitParameter("databasePassword"));
				 
				if (connection != null) {
					
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

			
		}
		
	}

}
