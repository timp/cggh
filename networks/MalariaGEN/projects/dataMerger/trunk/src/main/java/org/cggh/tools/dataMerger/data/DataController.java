package org.cggh.tools.dataMerger.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.databases.DatabaseModel;
import org.cggh.tools.dataMerger.data.databases.DatabasesCRUD;
import org.cggh.tools.dataMerger.data.exports.ExportModel;
import org.cggh.tools.dataMerger.data.exports.ExportsCRUD;
import org.cggh.tools.dataMerger.data.files.FileModel;
import org.cggh.tools.dataMerger.data.files.FilesCRUD;
import org.cggh.tools.dataMerger.data.joins.JoinModel;
import org.cggh.tools.dataMerger.data.joins.JoinsCRUD;
import org.cggh.tools.dataMerger.data.merges.MergeModel;
import org.cggh.tools.dataMerger.data.merges.MergesCRUD;
import org.cggh.tools.dataMerger.data.resolutions.byCell.ResolutionsByCellCRUD;
import org.cggh.tools.dataMerger.data.resolutions.byColumn.ResolutionsByColumnCRUD;
import org.cggh.tools.dataMerger.data.resolutions.byRow.ResolutionsByRowCRUD;
import org.cggh.tools.dataMerger.data.userbases.UserbaseModel;
import org.cggh.tools.dataMerger.data.userbases.UserbasesCRUD;
import org.cggh.tools.dataMerger.data.users.UserModel;
import org.cggh.tools.dataMerger.data.users.UsersCRUD;
import org.cggh.tools.dataMerger.files.filebases.FilebaseModel;
import org.cggh.tools.dataMerger.files.filebases.FilebasesCRUD;
import org.cggh.tools.dataMerger.functions.data.files.FilesFunctions;
import org.cggh.tools.dataMerger.functions.data.joins.JoinFunctions;
import org.cggh.tools.dataMerger.functions.data.merges.MergeFunctions;
import org.cggh.tools.dataMerger.functions.data.users.UsersFunctions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




public class DataController extends HttpServlet {



	
    /**
	 * 
	 */
	private static final long serialVersionUID = 2142411300172699330L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data");

    public DataController() {
        super();

        
    }

    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		DatabasesCRUD databasesCRUD = new DatabasesCRUD();
		DatabaseModel databaseModel = databasesCRUD.retrieveDatabaseAsDatabaseModelUsingServletContext(request.getSession().getServletContext());

		UsersCRUD usersCRUD = new UsersCRUD();
		usersCRUD.setDatabaseModel(databaseModel);
		UserModel userModel = usersCRUD.retrieveUserAsUserModelUsingUsername((String)request.getSession().getAttribute("username"));

		//TODO: centralize these
		
		 Pattern joinsURLPattern = Pattern.compile("^/merges/(\\d+)/joins$");
		 Matcher joinsURLPatternMatcher = joinsURLPattern.matcher(request.getPathInfo());
		 
		 Pattern resolutionsByColumnURLPattern = Pattern.compile("^/merges/(\\d+)/resolutions-by-column$");
		 Matcher resolutionsByColumnURLPatternMatcher = resolutionsByColumnURLPattern.matcher(request.getPathInfo());
		 
		 Pattern resolutionsByRowURLPattern = Pattern.compile("^/merges/(\\d+)/resolutions-by-row$");
		 Matcher resolutionsByRowURLPatternMatcher = resolutionsByRowURLPattern.matcher(request.getPathInfo());

		 Pattern newJoinURLPattern = Pattern.compile("^/merges/(\\d+)/joins/join$");
		 Matcher newJoinURLPatternMatcher = newJoinURLPattern.matcher(request.getPathInfo());
		 
		  String[] headerAcceptsAsStringArray = request.getHeader("Accept").split(",");
		  List<String> headerAcceptsAsStringList = Arrays.asList(headerAcceptsAsStringArray);		 
		  if (request.getPathInfo().equals("/files")) {

			  if (headerAcceptsAsStringList.contains("text/html")) { 
			  
				  //Otherwise degree symbols turn into question-marks
				  response.setCharacterEncoding("UTF-8");
				  
				  response.setContentType("text/html");
				  
				  String filesAsHTML = null;
					
				  FilesCRUD filesCRUD = new FilesCRUD();
				  filesCRUD.setDatabaseModel(databaseModel);

				  
				  if (request.getParameter("hidden") != null) {
				  
					  CachedRowSet hiddenFilesAsCachedRowSet = filesCRUD.retrieveHiddenFilesAsCachedRowSetUsingUserId(userModel.getId());
				
					  if (hiddenFilesAsCachedRowSet != null) {
				
						  	FilesFunctions filesFunctions = new FilesFunctions();
						  	filesAsHTML = filesFunctions.getHiddenFilesAsDecoratedXHTMLTableUsingHiddenFilesAsCachedRowSet(hiddenFilesAsCachedRowSet);
						    
					  } else {
					  
						  filesAsHTML = "<p>Failed to retrieve Files As CachedRowSet Using User Id</p>";
						  
					  } 
			
				  } 
				  else if (request.getParameter("sort") != null) {

					  if (request.getParameter("sort").equals("filename")) {
						
						  CachedRowSet filesSortedByFilenameAsCachedRowSet = filesCRUD.retrieveFilesSortedByFilenameAsCachedRowSetUsingUserId(userModel.getId());
							
						  if (filesSortedByFilenameAsCachedRowSet != null) {
					
							  	FilesFunctions filesFunctions = new FilesFunctions();
							  	filesAsHTML = filesFunctions.getFilesAsDecoratedXHTMLTableUsingFilesAsCachedRowSet(filesSortedByFilenameAsCachedRowSet);
							    
						  } else {
						  
							  filesAsHTML = "<p>Failed to retrieve Files Sorted By Filename As CachedRowSet Using User Id</p>";
							  
						  }
						  
					  } else {
						  filesAsHTML = "<p>Unsupported sort request.</p>";
						  
					  }
					  
					  
				  }
				  else {
					  
					  
					  
					  CachedRowSet filesAsCachedRowSet = filesCRUD.retrieveFilesAsCachedRowSetUsingUserId(userModel.getId());
				
					  if (filesAsCachedRowSet != null) {
				
						  	FilesFunctions filesFunctions = new FilesFunctions();

						  	filesAsHTML = filesFunctions.getFilesAsDecoratedXHTMLTableUsingFilesAsCachedRowSet(filesAsCachedRowSet);
						    
					  } else {
					  
						  filesAsHTML = "<p>Failed to retrieve Files As CachedRowSet Using User Id</p>";
						  
					  } 
					  
				  }
				  
				  
				  response.getWriter().print(filesAsHTML);
				  
			  } else {
				  
				  response.setContentType("text/plain");
				  response.getWriter().println("Unhandled Header Accept: " + request.getHeader("Accept"));
				  
			  }
			
		  }
		  else if (joinsURLPatternMatcher.find()) {
			  
				 // Get the mergeId 
				 MergeModel mergeModel = new MergeModel();
				 mergeModel.setId(Integer.parseInt(joinsURLPatternMatcher.group(1)));  
				 
				 MergesCRUD mergesCRUD = new MergesCRUD();
				 mergesCRUD.setDatabaseModel(databaseModel);
				 mergeModel = mergesCRUD.retrieveMergeAsMergeModelByMergeId(mergeModel.getId());
			  
				  if (headerAcceptsAsStringList.contains("text/html")) { 
					  
					  //Otherwise degree symbols turn into question-marks
					  response.setCharacterEncoding("UTF-8");
					  
					  response.setContentType("text/html");
					  
					  String joinsAsHTML = null;
						
					  JoinsCRUD joinsCRUD = new JoinsCRUD();
					  joinsCRUD.setDatabaseModel(databaseModel);
					  
					  //FIXME
					  //joinsCRUD.setUserModel(userModel);

					  CachedRowSet joinsAsCachedRowSet = joinsCRUD.retrieveJoinsAsCachedRowSetByMergeId(mergeModel.getId());
				
					  if (joinsAsCachedRowSet != null) {
				
						  MergeFunctions mergeFunctions = new MergeFunctions();
						  
						    mergeFunctions.setMergeModel(mergeModel);	    
						    mergeFunctions.setJoinsAsCachedRowSet(joinsAsCachedRowSet);
						    mergeFunctions.setJoinsAsDecoratedXHTMLTableUsingJoinsAsCachedRowSet();
						    joinsAsHTML = mergeFunctions.getJoinsAsDecoratedXHTMLTable();
						    
					  } else {
					  
						  joinsAsHTML = "<p>Failed to retrieve Joins As CachedRowSet Using Merge Id</p>";
						  
					  } 
					  
					  
					  response.getWriter().print(joinsAsHTML);
					  
				  } else {
					  
					  response.setContentType("text/plain");
					  response.getWriter().println("Unhandled Header Accept: " + request.getHeader("Accept"));
					  
				  } 

		  } else if (resolutionsByColumnURLPatternMatcher.find()) {
			  
				 // Get the mergeId 
				 MergeModel mergeModel = new MergeModel();
				 mergeModel.setId(Integer.parseInt(resolutionsByColumnURLPatternMatcher.group(1)));  
				 
				 MergesCRUD mergesModel = new MergesCRUD();
				 mergesModel.setDatabaseModel(databaseModel);
				 mergeModel = mergesModel.retrieveMergeAsMergeModelByMergeId(mergeModel.getId());
			  
				  if (headerAcceptsAsStringList.contains("text/html")) { 
					  
					  //Otherwise degree symbols turn into question-marks
					  response.setCharacterEncoding("UTF-8");
					  
					  response.setContentType("text/html");
					  
					  String resolutionsByColumnAsHTML = null;
						

						ResolutionsByColumnCRUD resolutionsByColumnCRUD = new ResolutionsByColumnCRUD();
						resolutionsByColumnCRUD.setDatabaseModel(databaseModel);
						
						//FIXME
						//resolutionsByColumnModel.setUserModel(userModel);

						resolutionsByColumnAsHTML = resolutionsByColumnCRUD.retrieveResolutionsByColumnAsDecoratedXHTMLTableUsingMergeModel(mergeModel);
					  
					  
					  response.getWriter().print(resolutionsByColumnAsHTML);
					  
				  } else {
					  
					  response.setContentType("text/plain");
					  response.getWriter().println("Unhandled Header Accept: " + request.getHeader("Accept"));
					  
				  }   

				  
		  } else if (resolutionsByRowURLPatternMatcher.find()) {
			  
				 // Get the mergeId 
				 MergeModel mergeModel = new MergeModel();
				 mergeModel.setId(Integer.parseInt(resolutionsByRowURLPatternMatcher.group(1)));  
				 
				 MergesCRUD mergesModel = new MergesCRUD();
				 mergesModel.setDatabaseModel(databaseModel);
				 mergeModel = mergesModel.retrieveMergeAsMergeModelByMergeId(mergeModel.getId());
			  
				  if (headerAcceptsAsStringList.contains("text/html")) { 
					  
					  //Otherwise degree symbols turn into question-marks
					  response.setCharacterEncoding("UTF-8");
					  
					  response.setContentType("text/html");
					  
					  String resolutionsByRowAsHTML = null;
						

						ResolutionsByRowCRUD resolutionsByRowCRUD = new ResolutionsByRowCRUD();
						resolutionsByRowCRUD.setDatabaseModel(databaseModel);
						
						//FIXME
						//resolutionsByColumnModel.setUserModel(userModel);

						resolutionsByRowAsHTML = resolutionsByRowCRUD.retrieveResolutionsByRowAsDecoratedXHTMLTableUsingMergeModel(mergeModel);
					  
					  
					  response.getWriter().print(resolutionsByRowAsHTML);
					  
				  } else {
					  
					  response.setContentType("text/plain");
					  response.getWriter().println("Unhandled Header Accept: " + request.getHeader("Accept"));
					  
				  }   				  
				  
				  
				  
		  }
		  
		  else if (newJoinURLPatternMatcher.find()) {
				  
			  //FIXME: This content-type isn't necessary, just a test
			  if (request.getContentType() != null && request.getContentType().equals("application/json")) {
				  
				  
				  if (headerAcceptsAsStringList.contains("text/html")) { 
				  
					  //Otherwise degree symbols turn into question-marks
					  response.setCharacterEncoding("UTF-8");
					  
					  response.setContentType("text/html");
					  
					  	// Get the mergeId 
						 MergeModel mergeModel = new MergeModel();
						 mergeModel.setId(Integer.parseInt(newJoinURLPatternMatcher.group(1)));  
						 
						 MergesCRUD mergesModel = new MergesCRUD();
						 mergesModel.setDatabaseModel(databaseModel);
						 mergeModel = mergesModel.retrieveMergeAsMergeModelByMergeId(mergeModel.getId());
					  
					  String joinAsDecoratedXHTMLTable = null;
					  
					  	JoinFunctions joinFunctions = new JoinFunctions();
						joinFunctions.setJoinModel(new JoinModel()); // Unnecessary but explicit
						joinFunctions.setMergeModel(mergeModel);
						joinFunctions.setJoinAsDecoratedXHTMLTableByJoinModel();
						
					
						joinAsDecoratedXHTMLTable = joinFunctions.getJoinAsDecoratedXHTMLTable();
					  
					  
					  if (joinAsDecoratedXHTMLTable == null) {
	
						  joinAsDecoratedXHTMLTable = "<p>Failed to retrieve a join as a decorated XHTML table</p>";
						  
					  } 
					  
					  
					  response.getWriter().print(joinAsDecoratedXHTMLTable);
					  
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


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		DatabasesCRUD databasesCRUD = new DatabasesCRUD();
		DatabaseModel databaseModel = databasesCRUD.retrieveDatabaseAsDatabaseModelUsingServletContext(request.getSession().getServletContext());

		FilebasesCRUD filebasesCRUD = new FilebasesCRUD();
		FilebaseModel filebaseModel = filebasesCRUD.retrieveFilebaseAsFilebaseModelUsingServletContext(request.getSession().getServletContext());
		
		UserModel userModel = new UserModel();
		
		// Get the user, if possible
		if (databaseModel.isInitialized()) {
		
			UsersCRUD usersCRUD = new UsersCRUD();
			usersCRUD.setDatabaseModel(databaseModel);
			userModel = usersCRUD.retrieveUserAsUserModelUsingUsername((String)request.getSession().getAttribute("username"));
			
		}
		
		 Pattern exportURLPattern = Pattern.compile("^/merges/(\\d+)/exports$");
		 Matcher exportURLPatternMatcher = exportURLPattern.matcher(request.getPathInfo());
		
		 String[] headerAcceptsAsStringArray = request.getHeader("Accept").split(",");
		 List<String> headerAcceptsAsStringList = Arrays.asList(headerAcceptsAsStringArray);

		//TODO: Not sure how login/logout should map onto HTTP methods, in a RESTful sense. 
		 if (request.getPathInfo().equals("/users/authentication")) {

			  if (request.getContentType() != null && request.getContentType().startsWith("application/json")) {
			  
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
							        
							        UsersFunctions usersFunctions = new UsersFunctions();
							        
							        if (usersCRUD.retrieveAuthenticatedAsBooleanUsingUsernameAndPasswordHashAsStrings(jsonObject.getString("username"), usersFunctions.convertStringIntoHashAsHexStringUsingStringAndHashFunctionName(jsonObject.getString("password"), getServletContext().getInitParameter("userbasePasswordHashFunctionName")))) {
							        
							        	request.getSession().setAttribute("userAuthenticated", true);
							        	request.getSession().setAttribute("username", jsonObject.getString("username"));
							        	
							        	responseAsJSON = "{\"success\": \"true\"}";
							        	
							        } else {
							        	
							        	request.getSession().invalidate();
							        	
							        	responseAsJSON = "{\"success\": \"false\"}";
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
				  
				  
		  } 
		 else if (request.getPathInfo().equals("/merges")) {
			 
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
							JSONArray fileIds = jsonObject.getJSONArray("file_id");
							
							
							try {


								MergeModel mergeModel = new MergeModel();
								
								mergeModel.getFile1Model().setId(fileIds.getInt(0));
								mergeModel.getFile2Model().setId(fileIds.getInt(1));
								mergeModel.setCreatedByUserModel(userModel);
								
								MergesCRUD mergesCRUD = new MergesCRUD();
								mergesCRUD.setDatabaseModel(databaseModel);
								mergeModel = mergesCRUD.retrieveMergeAsMergeModelThroughCreatingMergeUsingMergeModel(mergeModel);
								
						        responseAsJSON = "{\"id\": \"" + mergeModel.getId().toString() + "\"}";		
								
								
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
			
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
				    
				    
				  } catch (Exception e) { 
					  //TODO:
					  e.printStackTrace(); 
				  
				  }
				  
				  response.getWriter().print(responseAsJSON);

			  } else {
				  
				  //FIXME: This will cause a parser error (Invalid JSON).
				  
				  response.setContentType("text/plain");
				  response.getWriter().println("Unhandled Header Accept: " + request.getHeader("Accept"));
				  
			  }
			  
		  
		 
		 }
		 else if (exportURLPatternMatcher.find()) {
			 
			  //TODO: Just testing
			  if (headerAcceptsAsStringList.contains("application/json")) { 

				  if (request.getContentType().startsWith("application/json")) {
				  
					  response.setContentType("application/json");
					  String responseAsJSON = null;
					  
					  	// Get the mergeId for the new export
					  
					  	ExportModel exportModel = new ExportModel();
					  	exportModel.setCreatedByUserModel(userModel);
					  	exportModel.getMergeModel().setId(Integer.parseInt(exportURLPatternMatcher.group(1)));
					  
					  	
					  	// Get the filename for the new export
					  	BufferedReader reader = request.getReader();
					    String line = null;
					    StringBuffer stringBuffer = new StringBuffer();
					    
					    while ((line = reader.readLine()) != null) {
					      stringBuffer.append(line);
					    }
					    
							
						try {
							JSONObject jsonObject = new JSONObject(stringBuffer.toString());
							
							exportModel.setMergedFileAsFileModel(new FileModel());
							
							exportModel.getMergedFileAsFileModel().setFilename(jsonObject.optString("mergedFileFilename"));
							
							//
							//logger.info("got mergedFileFilename: " + exportModel.getFilename());
							
							ExportsCRUD exportsCRUD = new ExportsCRUD();
							
							exportsCRUD.setDatabaseModel(databaseModel);
							exportsCRUD.setFilebaseModel(filebaseModel);
							
							exportModel = exportsCRUD.retrieveExportAsExportModelThroughCreatingExportUsingExportModel(exportModel);
							
							if (exportModel.getId() != null) {
								
						        responseAsJSON = "{\"id\": \"" + exportModel.getId() + "\"}";
						        response.getWriter().print(responseAsJSON);
					        
							} else {
								
								 //FIXME: This will cause a parser error (Invalid JSON).
								  
								String logMessage = "Did not get an export ID after attempting to create an export using the merge ID.";
								
								//this.log(logMessage);
								
								  response.setContentType("text/plain");
								  response.getWriter().println(logMessage);
								
							}
							
						} catch (JSONException e) {
							
							e.printStackTrace();
							
							response.setContentType("text/plain");
							response.getWriter().println("An error occurred while trying to parse the filename."); 
						}
					  	

				  } else {
					  response.setContentType("text/plain");
					  response.getWriter().println("Unhandled Content Type: " + request.getContentType()); 
				  }
						
			  } else {
				  
				  //FIXME: This will cause a parser error (Invalid JSON).
				  
				  response.setContentType("text/plain");
				  response.getWriter().println("Unhandled Header Accept: " + request.getHeader("Accept"));
				  
			  }	 
			 
		 
		 } else {
			  
			  //FIXME: This will cause a parser error (Invalid JSON).
			  
			  response.getWriter().println("Unhandled Path Info: " + request.getPathInfo());
		  }
		  
		
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		DatabasesCRUD databasesCRUD = new DatabasesCRUD();
		DatabaseModel databaseModel = databasesCRUD.retrieveDatabaseAsDatabaseModelUsingServletContext(request.getSession().getServletContext());

		
		//TODO: centralize these
		
		 Pattern joinsURLPattern = Pattern.compile("/merges/(\\d+)/joins");
		 Matcher joinsURLPatternMatcher = joinsURLPattern.matcher(request.getPathInfo());
		 
		 Pattern resolutionsByColumnURLPattern = Pattern.compile("/merges/(\\d+)/resolutions-by-column");
		 Matcher resolutionsByColumnURLPatternMatcher = resolutionsByColumnURLPattern.matcher(request.getPathInfo());
		 
		 Pattern resolutionsByRowURLPattern = Pattern.compile("/merges/(\\d+)/resolutions-by-row");
		 Matcher resolutionsByRowURLPatternMatcher = resolutionsByRowURLPattern.matcher(request.getPathInfo());

		 Pattern resolutionsByCellURLPattern = Pattern.compile("/merges/(\\d+)/resolutions-by-cell");
		 Matcher resolutionsByCellURLPatternMatcher = resolutionsByCellURLPattern.matcher(request.getPathInfo());
		
		 
		 String[] headerAcceptsAsStringArray = request.getHeader("Accept").split(",");
		  List<String> headerAcceptsAsStringList = Arrays.asList(headerAcceptsAsStringArray);
		 
		 if (joinsURLPatternMatcher.find()) {
			 
			 // Get the mergeId 
			 MergeModel mergeModel = new MergeModel();
			 mergeModel.setId(Integer.parseInt(joinsURLPatternMatcher.group(1)));
			 
			 
			 
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
							
							JoinsCRUD joinsCRUD = new JoinsCRUD();
							
							joinsCRUD.setDatabaseModel(databaseModel);
							
							
							//Consider: Instead of using JSONObject, use JoinsFunctions to convert it to something else? 
							// Maybe a list of JoinModel objects, joinsAsJoinModelList
							
							// This update should also recalculate conflicts and destroy old resolutions
							joinsCRUD.updateJoinsByMergeIdUsingJoinsAsJSONObject(mergeModel.getId(), jsonObject);
							
							
							
							//TODO:
							responseAsJSON = "{\"success\": \"true\"}";
							
			
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
				    
				    
				  } catch (Exception e) { 
					  //TODO:
					  e.printStackTrace(); 
				  
				  }
				  
				  response.getWriter().print(responseAsJSON);

			  } else {
				  
				  //FIXME: This will cause a parser error (Invalid JSON).
				  
				  response.setContentType("text/plain");
				  response.getWriter().println("Unhandled Header Accept: " + request.getHeader("Accept"));
				  
			  }

		  }
		  else if (resolutionsByColumnURLPatternMatcher.find()) {
			 
			 // Get the mergeId 
			 MergeModel mergeModel = new MergeModel();
			 mergeModel.setId(Integer.parseInt(resolutionsByColumnURLPatternMatcher.group(1)));
			 
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
							
							ResolutionsByColumnCRUD resolutionsByColumnModel = new ResolutionsByColumnCRUD();
							
							resolutionsByColumnModel.setDatabaseModel(databaseModel);
							
							//FIXME: This ultimately breaks because the mergeModel is incomplete and a full update is attempted.
							resolutionsByColumnModel.updateResolutionsByColumnByMergeIdUsingResolutionsByColumnAsJSONObject(mergeModel.getId(), jsonObject);
							
							//Note: Total conflicts for the merge is updated as a side-effect of updating the resolutions.
							
							
							//TODO:
							responseAsJSON = "{\"success\": \"true\"}";
							
			
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
				    
				    
				  } catch (Exception e) { 
					  //TODO:
					  e.printStackTrace(); 
				  
				  }
				  
				  response.getWriter().print(responseAsJSON);

			  } else {
				  
				  //FIXME: This will cause a parser error (Invalid JSON).
				  
				  response.setContentType("text/plain");
				  response.getWriter().println("Unhandled Header Accept: " + request.getHeader("Accept"));
				  
			  }

		  }	  
		  else if (resolutionsByRowURLPatternMatcher.find()) {
				 
				 // Get the mergeId 
				 MergeModel mergeModel = new MergeModel();
				 mergeModel.setId(Integer.parseInt(resolutionsByRowURLPatternMatcher.group(1)));
				 
				 
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
								
								ResolutionsByRowCRUD resolutionsByRowModel = new ResolutionsByRowCRUD();
								
								resolutionsByRowModel.setDatabaseModel(databaseModel);
								
								resolutionsByRowModel.updateResolutionsByRowByMergeIdUsingResolutionsByRowAsJSONObject(mergeModel.getId(), jsonObject);
								
								//Note: Total conflicts for the merge is updated as a side-effect of updating the resolutions.
								
								
								//TODO:
								responseAsJSON = "{\"success\": \"true\"}";
								
				
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} 
					    
					    
					  } catch (Exception e) { 
						  //TODO:
						  e.printStackTrace(); 
					  
					  }
					  
					  response.getWriter().print(responseAsJSON);

					  
					  
					  
				  } else {
					  
					  //FIXME: This will cause a parser error (Invalid JSON).
					  
					  response.setContentType("text/plain");
					  response.getWriter().println("Unhandled Header Accept: " + request.getHeader("Accept"));
					  
				  }			  
			  
			  
		  }	  
		  else if (resolutionsByCellURLPatternMatcher.find()) {
						 
						 // Get the mergeId 
						 MergeModel mergeModel = new MergeModel();
						 mergeModel.setId(Integer.parseInt(resolutionsByCellURLPatternMatcher.group(1)));
						 
						 
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
										
										ResolutionsByCellCRUD resolutionsByCellCRUD = new ResolutionsByCellCRUD();
										
										resolutionsByCellCRUD.setDatabaseModel(databaseModel);
										
										resolutionsByCellCRUD.updateResolutionsByCellByMergeIdUsingResolutionsByCellAsJSONObject(mergeModel.getId(), jsonObject);
										
										//Note: Total conflicts for the merge is updated as a side-effect of updating the resolutions.
										
										
										//TODO:
										responseAsJSON = "{\"success\": \"true\"}";
										
						
									} catch (JSONException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									} 
							    
							    
							  } catch (Exception e) { 
								  //TODO:
								  e.printStackTrace(); 
							  
							  }
							  
							  response.getWriter().print(responseAsJSON);

							  
							  
							  
						  } else {
							  
							  //FIXME: This will cause a parser error (Invalid JSON).
							  
							  response.setContentType("text/plain");
							  response.getWriter().println("Unhandled Header Accept: " + request.getHeader("Accept"));
							  
						  }				  
			  
			
		  } 
		  else if (request.getPathInfo().equals("/files")) {
			 
			  if (headerAcceptsAsStringList.contains("application/json")) { 
			 
				  if (request.getParameter("hide") != null ^ request.getParameter("unhide") != null) {
					  
					  	Boolean hidden = null;
						
						if (request.getParameter("hide") != null) {
							hidden = true;
						} else if (request.getParameter("unhide") != null) {
							hidden = false;
						}
						
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
								
								JSONArray fileIds = jsonObject.optJSONArray("file_id");
								if (fileIds == null) {
									fileIds = new JSONArray();
									fileIds.put(jsonObject.getInt("file_id"));
								}
								
								
								FilesCRUD filesCRUD = new FilesCRUD();
								filesCRUD.setDatabaseModel(databaseModel);
								
								UsersCRUD usersCRUD = new UsersCRUD();
								usersCRUD.setDatabaseModel(databaseModel);
								UserModel userModel = usersCRUD.retrieveUserAsUserModelUsingUsername((String)request.getSession().getAttribute("username"));
								
								if (filesCRUD.updateFileHiddensUsingFileIdsAsJSONArrayAndHiddenAsBooleanAndUserId(fileIds, hidden, userModel.getId())) {
									responseAsJSON = "{\"success\": \"true\"}";
								} else {
									responseAsJSON = "{\"success\": \"false\"}";
								}
									
				
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} 
					    
					    
					  } catch (Exception e) { 
						  //TODO:
						  e.printStackTrace(); 
					  
					  }
					  
					  response.getWriter().print(responseAsJSON);

				  } else {
					//FIXME: This will cause a parser error (Invalid JSON).
					  
					  response.setContentType("text/plain");
					  response.getWriter().println("Unhandled Query Parameters");
					  
				  }
					  
			  } else {
				  
				  //FIXME: This will cause a parser error (Invalid JSON).
				  
				  response.setContentType("text/plain");
				  response.getWriter().println("Unhandled Header Accept: " + request.getHeader("Accept"));
				  
			  }
			  
		  
		 
		 } else {
			  
			  //FIXME: This will cause a parser error (Invalid JSON).
			  
			  response.getWriter().println("Unhandled Path Info: " + request.getPathInfo());
		  }
		  
		
	}

	protected void doDelete (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
			DatabasesCRUD databasesCRUD = new DatabasesCRUD();
			DatabaseModel databaseModel = databasesCRUD.retrieveDatabaseAsDatabaseModelUsingServletContext(request.getSession().getServletContext());


		  String[] headerAcceptsAsStringArray = request.getHeader("Accept").split(",");
		  List<String> headerAcceptsAsStringList = Arrays.asList(headerAcceptsAsStringArray);		 
		 
		  if (request.getPathInfo().equals("/users/authentication")) {

			  if (request.getContentType() != null && request.getContentType().startsWith("application/json")) {
			  
			  	if (headerAcceptsAsStringList.contains("application/json")) { 
			  
					  response.setCharacterEncoding("UTF-8");
					  
					  response.setContentType("application/json");
					  
					  String responseAsJSON = null;
					  
							  
				        	request.getSession().invalidate();
				        	
				        	responseAsJSON = "{\"success\": \"true\"}";
				     
				        	
					  response.getWriter().print(responseAsJSON);
				  
				  } else {
					  
					  response.setContentType("text/plain");
					  response.getWriter().println("Unhandled Header Accept: " + request.getHeader("Accept"));
					  
				  }

				  
			  } else {
				  
				  response.setContentType("text/plain");
				  response.getWriter().println("Unhandled Content Type: " + request.getContentType());
				  
			  }	  
				  
				  
		  }
		  else if (request.getPathInfo().equals("/files")) {
				 
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
								
								JSONArray fileIds = jsonObject.optJSONArray("file_id");
								if (fileIds == null) {
									fileIds = new JSONArray();
									fileIds.put(jsonObject.getInt("file_id"));
								}
								
								FilesCRUD filesCRUD = new FilesCRUD();
								filesCRUD.setDatabaseModel(databaseModel);
								
								UsersCRUD usersCRUD = new UsersCRUD();
								usersCRUD.setDatabaseModel(databaseModel);
								UserModel userModel = usersCRUD.retrieveUserAsUserModelUsingUsername((String)request.getSession().getAttribute("username"));
								
								if (filesCRUD.deleteFilesUsingFileIdsAsJSONArrayAndUserId(fileIds, userModel.getId())) {
									responseAsJSON = "{\"success\": \"true\"}";
								} else {
									responseAsJSON = "{\"success\": \"false\"}";
								}
									
				
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} 
					    
					    
					  } catch (Exception e) { 
						  //TODO:
						  e.printStackTrace(); 
					  
					  }
					  
					  response.getWriter().print(responseAsJSON);

					  
			  } else {
				  
				  //FIXME: This will cause a parser error (Invalid JSON).
				  
				  response.setContentType("text/plain");
				  response.getWriter().println("Unhandled Header Accept: " + request.getHeader("Accept"));
				  
			  }
			  
		  
		 
		 } else {
			  
			  response.getWriter().println("Unhandled Path Info: " + request.getPathInfo());
		 }
	}

	public Logger getLogger() {
		return logger;
	}
}
