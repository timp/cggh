package org.cggh.tools.dataMerger.scripts;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cggh.tools.dataMerger.data.databases.DatabaseModel;
import org.cggh.tools.dataMerger.data.databases.DatabasesCRUD;
import org.cggh.tools.dataMerger.data.users.UserModel;
import org.cggh.tools.dataMerger.data.users.UsersCRUD;
import org.cggh.tools.dataMerger.files.filebases.FilebaseModel;
import org.cggh.tools.dataMerger.files.filebases.FilebasesCRUD;
import org.cggh.tools.dataMerger.scripts.data.databases.tables.DatabaseTablesScripts;
import org.cggh.tools.dataMerger.scripts.files.filebases.directories.FilebaseDirectoriesScripts;


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


        if (request.getPathInfo().equals("/data/databases/tables/create-and-initialize")) {
        	
        	String[] headerAcceptsAsStringArray = request.getHeader("Accept").split(",");
        	List<String> headerAcceptsAsStringList = Arrays.asList(headerAcceptsAsStringArray);

        	if (headerAcceptsAsStringList.contains("text/plain")) { 
  				 
  			  response.setContentType("text/plain");
  			  String responseAsPlainText = null;
		  
  			  DatabasesCRUD databasesCRUD = new DatabasesCRUD();
  			  DatabaseModel databaseModel = databasesCRUD.retrieveDatabaseAsDatabaseModelUsingServletContext(request.getSession().getServletContext());
				
				if (databaseModel.isServerConnectable()) {
					
					if (databaseModel.isConnectable()) {
					
						DatabaseTablesScripts databaseTablesScripts = new DatabaseTablesScripts();
						
						if (databaseTablesScripts.createInitialTablesUsingDatabaseModelAndUsername(databaseModel, request.getRemoteUser())) {
							
							responseAsPlainText = "Database Tables created and initialized.";
							
							
						} else {
							
							responseAsPlainText = "Database Tables creation and initialization failed.";
							
						}
						
					} else {
						
						responseAsPlainText = "Database is not connectable.";
						
					}
				
				} else {
					
					responseAsPlainText = "Database server is not connectable.";
					
				}
  					
  		  
  			  response.getWriter().print(responseAsPlainText);

  		  } else {

  			  response.setContentType("text/plain");
  			  response.getWriter().print("Unsupported accept header.");
  			  
  		  }	
		

		}
        else if (request.getPathInfo().equals("/files/filebases/directories/create-and-initialize")) {
        	
        	String[] headerAcceptsAsStringArray = request.getHeader("Accept").split(",");
        	List<String> headerAcceptsAsStringList = Arrays.asList(headerAcceptsAsStringArray);

        	if (headerAcceptsAsStringList.contains("text/plain")) { 
  				 
  			  response.setContentType("text/plain");
  			  String responseAsPlainText = null;
		  
  			  FilebasesCRUD filebasesCRUD = new FilebasesCRUD();
  			  FilebaseModel filebaseModel = filebasesCRUD.retrieveFilebaseAsFilebaseModelUsingServletContext(request.getSession().getServletContext());
				
				if (filebaseModel.isExistent()) {
					
					if (filebaseModel.isWritable()) {
					
					  	DatabasesCRUD databasesCRUD = new DatabasesCRUD();
					  	DatabaseModel databaseModel = databasesCRUD.retrieveDatabaseAsDatabaseModelUsingServletContext(getServletContext());
					  	
					  	if (databaseModel.isInitialized()) {
						  	
						  	UsersCRUD usersCRUD = new UsersCRUD();
						  	usersCRUD.setDatabaseModel(databaseModel);
						  	UserModel userModel = usersCRUD.retrieveUserAsUserModelUsingUsername(request.getRemoteUser());
							
							
							FilebaseDirectoriesScripts filebaseDirectoriesScripts = new FilebaseDirectoriesScripts();
							
							if (filebaseDirectoriesScripts.createInitialDirectoriesUsingFilebaseModelAndUserModel(filebaseModel, userModel)) {
								
								responseAsPlainText = "The filebase directories have now been created and initialized.";
								
								
							} else {
								
								responseAsPlainText = "An error occurred while trying to create and initialize the filebase directories.";
								
							}
							
					  	} else {
					  		responseAsPlainText = "The database has not been initialized.";
					  	}
						
					} else {
						
						responseAsPlainText = "Filebase is not writable.";
						
					}
				
				} else {
					
					responseAsPlainText = "Filebase does not exist.";
					
				}
  					
  		  
  			  	response.getWriter().print(responseAsPlainText);

	  		  } else {
	
	  			  response.setContentType("text/plain");
	  			  response.getWriter().print("Unsupported accept header.");
	  			  
	  		  }	
		

		} else {
			
			  response.setContentType("text/plain");
  			  response.getWriter().print("Unsupported path info.");	
        }

		
	}
	
	protected void doDelete (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        if (request.getPathInfo().equals("/data/databases/tables/delete-all")) {
        	
        	String[] headerAcceptsAsStringArray = request.getHeader("Accept").split(",");
        	List<String> headerAcceptsAsStringList = Arrays.asList(headerAcceptsAsStringArray);

        	if (headerAcceptsAsStringList.contains("text/plain")) { 
  				 
  			  response.setContentType("text/plain");
  			  String responseAsPlainText = null;
		  
  			  DatabasesCRUD databasesCRUD = new DatabasesCRUD();
  			  DatabaseModel databaseModel = databasesCRUD.retrieveDatabaseAsDatabaseModelUsingServletContext(request.getSession().getServletContext());
				
				if (databaseModel.isServerConnectable()) {
					
					if (databaseModel.isConnectable()) {
					
						DatabaseTablesScripts databaseTablesScripts = new DatabaseTablesScripts();
						
						if (databaseTablesScripts.deleteTablesUsingDatabaseModel(databaseModel)) {
							
							responseAsPlainText = "Database Tables deleted.";
							
							
						} else {
							
							responseAsPlainText = "Database Tables deletion failed.";
							
						}
						
					} else {
						
						responseAsPlainText = "Database is not connectable.";
						
					}
				
				} else {
					
					responseAsPlainText = "Database server is not connectable.";
					
				}
				
  		  
  			  response.getWriter().print(responseAsPlainText);

  		  } else {

  			  response.setContentType("text/plain");
  			  response.getWriter().print("Unsupported accept header.");
  			  
  		  }	
		

		}
        else if (request.getPathInfo().equals("/files/filebases/directories/delete-all")) {
        	
        	String[] headerAcceptsAsStringArray = request.getHeader("Accept").split(",");
        	List<String> headerAcceptsAsStringList = Arrays.asList(headerAcceptsAsStringArray);

        	if (headerAcceptsAsStringList.contains("text/plain")) { 
  				 
  			  response.setContentType("text/plain");
  			  String responseAsPlainText = null;
		  
  			  FilebasesCRUD filebasesCRUD = new FilebasesCRUD();
  			  FilebaseModel filebaseModel = filebasesCRUD.retrieveFilebaseAsFilebaseModelUsingServletContext(request.getSession().getServletContext());
				
				if (filebaseModel.isExistent()) {
					
					if (filebaseModel.isWritable()) {
					
						FilebaseDirectoriesScripts filebaseDirectoriesScripts = new FilebaseDirectoriesScripts();
						
						if (filebaseDirectoriesScripts.deleteDirectoriesUsingFilebaseModel(filebaseModel)) {
							
							responseAsPlainText = "The filebase directories have now been deleted.";
							
							
						} else {
							
							responseAsPlainText = "An error occurred while trying to delete the filebase directories.";
							
						}
						
					} else {
						
						responseAsPlainText = "The filebase is not writable.";
						
					}
				
				} else {
					
					responseAsPlainText = "The filebase does not exist.";
					
				}
  					
  		  
  			  response.getWriter().print(responseAsPlainText); 	
        	
  			  
    		  } else {

      			  response.setContentType("text/plain");
      			  response.getWriter().print("Unsupported accept header.");
      			  
      		  }	
  			  
        } else {
        	
			  response.setContentType("text/plain");
  			  response.getWriter().print("Unsupported path info.");
        	
        }

		
	}

	public Logger getLogger() {
		return logger;
	}

}
