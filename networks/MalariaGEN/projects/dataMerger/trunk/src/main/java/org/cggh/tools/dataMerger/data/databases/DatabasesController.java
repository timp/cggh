package org.cggh.tools.dataMerger.data.databases;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class DatabasesController extends HttpServlet {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9178755992963930280L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.databases");

    public DatabasesController() {
        super();

        
    }	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		String[] headerAcceptsAsStringArray = request.getHeader("Accept").split(",");
		List<String> headerAcceptsAsStringList = Arrays.asList(headerAcceptsAsStringArray);

		  if (headerAcceptsAsStringList.contains("text/plain")) { 
				 
			  response.setContentType("text/plain");
			  String responseAsPlainText = null;
			  
					DatabasesCRUD databasesCRUD = new DatabasesCRUD();
					DatabaseModel databaseModel = databasesCRUD.retrieveDatabaseAsDatabaseModelUsingServletContext(request.getSession().getServletContext());
					
					if (databaseModel.isServerConnectable()) {
					
						if (databasesCRUD.createDatabaseUsingDatabaseModel(databaseModel)) {
							
							responseAsPlainText = "Database created.";
							
							
						} else {
							
							responseAsPlainText = "Database creation failed.";
							
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
	
	protected void doDelete (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String[] headerAcceptsAsStringArray = request.getHeader("Accept").split(",");
		List<String> headerAcceptsAsStringList = Arrays.asList(headerAcceptsAsStringArray);

		  if (headerAcceptsAsStringList.contains("text/plain")) { 
				 
			  response.setContentType("text/plain");
			  String responseAsPlainText = null;
			  
					DatabasesCRUD databasesCRUD = new DatabasesCRUD();
					DatabaseModel databaseModel = databasesCRUD.retrieveDatabaseAsDatabaseModelUsingServletContext(request.getSession().getServletContext());
					
					if (databaseModel.isServerConnectable()) {
					
						if (databasesCRUD.deleteDatabaseUsingDatabaseModel(databaseModel)) {
							
							responseAsPlainText = "Database deleted.";
							
							
						} else {
							
							responseAsPlainText = "Database deleting failed.";
							
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
