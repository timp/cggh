package org.cggh.tools.dataMerger.data.userbases;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.cggh.tools.dataMerger.data.userbases.UserbaseModel;
import org.cggh.tools.dataMerger.data.userbases.UserbasesCRUD;
import org.cggh.tools.dataMerger.data.users.UsersCRUD;
import org.cggh.tools.dataMerger.functions.data.users.UsersFunctions;
import org.json.JSONException;
import org.json.JSONObject;


public class UserbasesController extends HttpServlet {



	
	/**
	 * 
	 */
	private static final long serialVersionUID = 38313151712112693L;
	private final Logger logger = Logger.getLogger(this.getClass().getPackage().getName());

    public UserbasesController() {
        super();

        
    }

    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	
    	String[] headerAcceptsAsStringArray = request.getHeader("Accept").split(",");
		  List<String> headerAcceptsAsStringList = Arrays.asList(headerAcceptsAsStringArray);		
    	
    	//FIXME: Make sure only admin can get this.
		  if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {

			  if (headerAcceptsAsStringList.contains("text/html")) { 
			  
				  //Otherwise degree symbols turn into question-marks
				  response.setCharacterEncoding("UTF-8");
				  
				  response.setContentType("text/html");
				  
				  String usersAsHTML = null;
					
				  
				  UserbasesCRUD userbasesCRUD = new UserbasesCRUD();
				  UserbaseModel userbaseModel = userbasesCRUD.retrieveUserbaseAsUserbaseModelUsingServletContext(getServletContext());
					
				  UsersCRUD usersCRUD = new UsersCRUD();
				  usersCRUD.setUserbaseModel(userbaseModel);

				  
				  usersAsHTML = usersCRUD.retrieveUsersAsDecoratedXHTMLTable();
				  
				  response.getWriter().print(usersAsHTML);
				  
			  } else {
				  
				  response.setContentType("text/plain");
				  response.getWriter().println("Unhandled Header Accept: " + request.getHeader("Accept"));
				  
			  }
			
		  } else {
			  
			  response.getWriter().println("Unhandled Path Info: " + request.getPathInfo());
		  }
    }
    
    
    

	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
			  
			
			  String[] headerAcceptsAsStringArray = request.getHeader("Accept").split(",");
			  List<String> headerAcceptsAsStringList = Arrays.asList(headerAcceptsAsStringArray);
			  
			  if (headerAcceptsAsStringList.contains("application/json")) { 
			 
				  response.setContentType("application/json");
				  String responseAsJSON = null;
				  
				  try {
					  
					    BufferedReader reader = request.getReader();
					    String line = null;
					    StringBuffer stringBuffer = new StringBuffer();
					    
					    while ((line = reader.readLine()) != null) {
					      stringBuffer.append(line);
					    }
					    
						try {
							
							JSONObject jsonObject = new JSONObject(stringBuffer.toString());
							
							UserbasesCRUD userbasesCRUD = new UserbasesCRUD();
							UserbaseModel userbaseModel = userbasesCRUD.retrieveUserbaseAsUserbaseModelUsingServletContext(getServletContext());
							
							UsersCRUD usersCRUD = new UsersCRUD();
							usersCRUD.setUserbaseModel(userbaseModel);
							
							UsersFunctions usersFunctions = new UsersFunctions();
							
							if (usersCRUD.createUserUsingUsernameAndPasswordHash(jsonObject.getString("username"), usersFunctions.convertStringIntoHashAsHexStringUsingStringAndHashFunctionName(jsonObject.getString("password"), getServletContext().getInitParameter("userbasePasswordHashFunctionName")))) {

								responseAsJSON = "{\"success\": \"true\"}";
									
							} else {
								
								responseAsJSON = "{\"success\": \"false\"}";
								
							}
							
			
						} catch (JSONException e1) {

							e1.printStackTrace();
						} 
				    
				    
				  } catch (Exception e) { 

					  e.printStackTrace(); 
				  
				  }
				  
				  response.getWriter().print(responseAsJSON);

				  
				  
				  
			  } else {
				  
				  //FIXME: This will cause a parser error (Invalid JSON).
				  
				  response.setContentType("text/plain");
				  response.getWriter().println("Unhandled Header Accept: " + request.getHeader("Accept"));
				  
			  }	
			  
		  }
	}

		

	public Logger getLogger() {
		return logger;
	}
	
 
}
