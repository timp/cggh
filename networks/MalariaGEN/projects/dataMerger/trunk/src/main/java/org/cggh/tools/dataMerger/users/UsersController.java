package org.cggh.tools.dataMerger.users;

import java.io.BufferedReader;
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
import org.cggh.tools.dataMerger.data.merges.MergeModel;
import org.cggh.tools.dataMerger.data.merges.MergesCRUD;
import org.cggh.tools.dataMerger.users.userbases.UserbaseModel;
import org.cggh.tools.dataMerger.users.userbases.UserbasesCRUD;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class UsersController extends HttpServlet {



	
    /**
	 * 
	 */
	private static final long serialVersionUID = 2142411300172699330L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.users");

    public UsersController() {
        super();

        
    }

    
    //TODO: Not sure how login/logout should map onto HTTP methods, in a RESTful sense. 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		  String[] headerAcceptsAsStringArray = request.getHeader("Accept").split(",");
		  List<String> headerAcceptsAsStringList = Arrays.asList(headerAcceptsAsStringArray);		 
		 
		  if (request.getPathInfo().equals("/authentication")) {

			  if (request.getContentType() != null && request.getContentType().equals("application/json; charset=UTF-8")) {
			  
			  	if (headerAcceptsAsStringList.contains("application/json")) { 
			  
					  response.setCharacterEncoding("UTF-8");
					  
					  response.setContentType("application/json");
					  
					  String responseAsJSON = null;
					  
					  try {
						  
						    //NOTE: This is applicable to POST, not GET
						    BufferedReader reader = request.getReader();
						    String line = null;
						    StringBuffer stringBuffer = new StringBuffer();
						    
						    while ((line = reader.readLine()) != null) {
						      stringBuffer.append(line);
						    }
						    
						    
							try {
								JSONObject jsonObject = new JSONObject(stringBuffer.toString());
								
								
								try {


							        responseAsJSON = "{\"username\": \"" + jsonObject.getString("username") + "\"}";		
									
							        
							        
							        UserbasesCRUD userbasesCRUD = new UserbasesCRUD();
							        UserbaseModel userbaseModel = userbasesCRUD.retrieveUserbaseAsUserbaseModelUsingServletContext(getServletContext()); 
							        
									
							        UsersCRUD usersCRUD = new UsersCRUD();
							        usersCRUD.setUserbaseModel(userbaseModel);
							        UserModel userModel = usersCRUD.retrieveUserAsUserModelUsingUsernameAndPasswordHash();
							        
									
								} catch (JSONException e) {

									e.printStackTrace();
								}
								
				
							} catch (JSONException e1) {

								e1.printStackTrace();
							} 
					    
					    
					  } catch (Exception e) { 

						  e.printStackTrace(); 
					  
					  }
					  
					  response.getWriter().print(responseAsJSON);
				  
				  } else {
					  
					  response.setContentType("text/plain");
					  response.getWriter().println("Unhandled Header Accept: " + request.getHeader("Accept"));
					  
				  }

				  
			  } else {
				  
				  response.setContentType("text/plain");
				  response.getWriter().println("Unhandled Content Type: " + request.getContentType());
				  
			  }	  
				  
				  
		  } else {
			  
			  response.getWriter().println("Unhandled Path Info: " + request.getPathInfo());
		  }
	}



	public Logger getLogger() {
		return logger;
	}
}
