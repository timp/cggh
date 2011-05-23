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
import org.cggh.tools.dataMerger.scripts.data.tables.TablesScripts;


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


        if (request.getPathInfo().equals("/data/tables/create-and-initialize")) {
        	
        	String[] headerAcceptsAsStringArray = request.getHeader("Accept").split(",");
        	List<String> headerAcceptsAsStringList = Arrays.asList(headerAcceptsAsStringArray);

        	if (headerAcceptsAsStringList.contains("text/plain")) { 
  				 
  			  response.setContentType("text/plain");
  			  String responseAsPlainText = null;
		  
  			  DatabasesCRUD databasesCRUD = new DatabasesCRUD();
  			  DatabaseModel databaseModel = databasesCRUD.retrieveDatabaseAsDatabaseModelUsingServletContext(request.getSession().getServletContext());
				
  					if (databaseModel.isServerConnectable()) {
  						
  						if (databaseModel.isConnectable()) {
  						
  							TablesScripts tablesScripts = new TablesScripts();
  							
  							if (tablesScripts.createInitialTablesUsingDatabaseModelAndUsername(databaseModel, request.getRemoteUser())) {
  								
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

		
	}
	
	protected void doDelete (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        if (request.getPathInfo().equals("/data/tables/delete-all")) {
        	
        	String[] headerAcceptsAsStringArray = request.getHeader("Accept").split(",");
        	List<String> headerAcceptsAsStringList = Arrays.asList(headerAcceptsAsStringArray);

        	if (headerAcceptsAsStringList.contains("text/plain")) { 
  				 
  			  response.setContentType("text/plain");
  			  String responseAsPlainText = null;
		  
  			  DatabasesCRUD databasesCRUD = new DatabasesCRUD();
  			  DatabaseModel databaseModel = databasesCRUD.retrieveDatabaseAsDatabaseModelUsingServletContext(request.getSession().getServletContext());
				
  					if (databaseModel.isServerConnectable()) {
  						
  						if (databaseModel.isConnectable()) {
  						
  							TablesScripts tablesScripts = new TablesScripts();
  							
  							if (tablesScripts.deleteTablesUsingDatabaseModel(databaseModel)) {
  								
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

		
	}

}
