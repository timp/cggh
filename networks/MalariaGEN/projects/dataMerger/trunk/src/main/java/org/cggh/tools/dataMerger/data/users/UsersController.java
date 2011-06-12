package org.cggh.tools.dataMerger.data.users;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.cggh.tools.dataMerger.data.users.userbases.UserbaseModel;
import org.cggh.tools.dataMerger.data.users.userbases.UserbasesCRUD;
import org.json.JSONException;
import org.json.JSONObject;


public class UsersController extends HttpServlet {



	
    /**
	 * 
	 */
	private static final long serialVersionUID = 2142411300172699330L;
	private final Logger logger = Logger.getLogger(this.getClass().getPackage().getName());

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


							        		
									
							        
							        
							        UserbasesCRUD userbasesCRUD = new UserbasesCRUD();
							        UserbaseModel userbaseModel = userbasesCRUD.retrieveUserbaseAsUserbaseModelUsingServletContext(getServletContext()); 
							        
									
							        UsersCRUD usersCRUD = new UsersCRUD();
							        usersCRUD.setUserbaseModel(userbaseModel);
							        
							        
							        
							        if (usersCRUD.retrieveAuthenticatedAsBooleanUsingUsernameAndPasswordHashAsStrings(jsonObject.getString("username"), getHashAsHexStringUsingStringAsStringAndHashFunctionNameAsString(jsonObject.getString("password"), getServletContext().getInitParameter("userbasePasswordHashFunctionName")))) {
							        
							        	
							        	UserModel userModel = usersCRUD.retrieveUserAsUserModelUsingUsername(jsonObject.getString("username"));
							        	
							        	userModel.setAuthenticated(true);
							        	
							        	request.getSession().setAttribute("userModel", userModel);
							        	
							        	responseAsJSON = "{\"success\": \"true\"}";
							        	
							        } else {
							        	
							        	request.getSession().invalidate();
							        	
							        	responseAsJSON = "{\"username\": \"false\"}";
							        }
							        
									
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
	
    public String getHashAsHexStringUsingStringAsStringAndHashFunctionNameAsString (String stringAsString, String hashFunctionNameAsString) {
    	
    	//hashFunctionNameAsString = "SHA-512"
    	
    	String hashAsHexString = null;
    	
            try {
            	MessageDigest messageDigest= MessageDigest.getInstance(hashFunctionNameAsString);
				messageDigest.update(stringAsString.getBytes());
	            
	            byte[] hashAsByteArray = messageDigest.digest();
	            
	            if (hashAsByteArray.length > 0) {
		            
		            hashAsHexString = "";
		            
		            for (int i = 0; i < hashAsByteArray.length; i++) {
		            	
		                byte hashByte = hashAsByteArray[i];
		                String hashByteAsHexString = Integer.toHexString(new Byte(hashByte));
		                while (hashByteAsHexString.length() < 2) {
		                    hashByteAsHexString = "0" + hashByteAsHexString;
		                }
		                hashByteAsHexString = hashByteAsHexString.substring(hashByteAsHexString.length() - 2);
		                hashAsHexString += hashByteAsHexString;
		            }
		            
	            }
				
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
 
            
 
		return hashAsHexString;
    }
}
