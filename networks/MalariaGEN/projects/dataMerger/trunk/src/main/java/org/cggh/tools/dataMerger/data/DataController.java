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

import org.cggh.tools.dataMerger.data.exports.ExportModel;
import org.cggh.tools.dataMerger.data.exports.ExportsModel;
import org.cggh.tools.dataMerger.data.joins.JoinModel;
import org.cggh.tools.dataMerger.data.joins.JoinsModel;
import org.cggh.tools.dataMerger.data.merges.MergeModel;
import org.cggh.tools.dataMerger.data.merges.MergesModel;
import org.cggh.tools.dataMerger.data.resolutions.byCell.ResolutionsByCellModel;
import org.cggh.tools.dataMerger.data.resolutions.byColumn.ResolutionsByColumnModel;
import org.cggh.tools.dataMerger.data.resolutions.byRow.ResolutionsByRowModel;
import org.cggh.tools.dataMerger.data.uploads.UploadsModel;
import org.cggh.tools.dataMerger.data.users.UserModel;
import org.cggh.tools.dataMerger.data.users.UsersCRUD;
import org.cggh.tools.dataMerger.functions.joins.JoinFunctionsModel;
import org.cggh.tools.dataMerger.functions.merges.MergeFunctionsModel;
import org.cggh.tools.dataMerger.functions.uploads.UploadsFunctionsModel;
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

		DataModel dataModel = new DataModel();
		dataModel.setDataModelUsingServletContext(request.getSession().getServletContext());

		UsersCRUD usersCRUD = new UsersCRUD();
		usersCRUD.setDataModel(dataModel);
		UserModel userModel = usersCRUD.retrieveUserAsUserModelUsingUsername(request.getRemoteUser());
		userModel.setHttpServletRequest(request);

		//TODO: centralize these
		
		 Pattern joinsURLPattern = Pattern.compile("^/merges/(\\d+)/joins$");
		 Matcher joinsURLPatternMatcher = joinsURLPattern.matcher(request.getPathInfo());
		 
		 Pattern resolutionsByColumnURLPattern = Pattern.compile("^/merges/(\\d+)/resolutions-by-column$");
		 Matcher resolutionsByColumnURLPatternMatcher = resolutionsByColumnURLPattern.matcher(request.getPathInfo());
		 
		 Pattern resolutionsByRowURLPattern = Pattern.compile("^/merges/(\\d+)/resolutions-by-row$");
		 Matcher resolutionsByRowURLPatternMatcher = resolutionsByRowURLPattern.matcher(request.getPathInfo());

		 Pattern newJoinURLPattern = Pattern.compile("^/merges/(\\d+)/joins/join$");
		 Matcher newJoinURLPatternMatcher = newJoinURLPattern.matcher(request.getPathInfo());
		 
		 Pattern exportURLPattern = Pattern.compile("^/merges/(\\d+)/exports$");
		 Matcher exportURLPatternMatcher = newJoinURLPattern.matcher(request.getPathInfo());
		 
		  String[] headerAcceptsAsStringArray = request.getHeader("Accept").split(",");
		  List<String> headerAcceptsAsStringList = Arrays.asList(headerAcceptsAsStringArray);		 
		 
		  if (request.getPathInfo().equals("/uploads")) {

			  if (headerAcceptsAsStringList.contains("text/html")) { 
			  
				  //Otherwise degree symbols turn into question-marks
				  response.setCharacterEncoding("UTF-8");
				  
				  response.setContentType("text/html");
				  
				  String uploadsAsHTML = null;
					
				  UploadsModel uploadsModel = new UploadsModel();
				  uploadsModel.setDataModel(dataModel);
				  uploadsModel.setUserModel(userModel);

				  CachedRowSet uploadsAsCachedRowSet = uploadsModel.retrieveUploadsAsCachedRowSetUsingUserId(userModel.getId());
			
				  if (uploadsAsCachedRowSet != null) {
			
					  	UploadsFunctionsModel uploadsFunctionsModel = new UploadsFunctionsModel();
					  
					  	uploadsFunctionsModel.setCachedRowSet(uploadsAsCachedRowSet);
					  	uploadsFunctionsModel.setDecoratedXHTMLTableByCachedRowSet();
					  	
					  	uploadsAsHTML = uploadsFunctionsModel.getDecoratedXHTMLTable();
					    
				  } else {
				  
					  uploadsAsHTML = "<p>Failed to retrieve Uploads As CachedRowSet Using User Id</p>";
					  
				  } 
				  
				  
				  response.getWriter().print(uploadsAsHTML);
				  
			  } else {
				  
				  response.setContentType("text/plain");
				  response.getWriter().println("Unhandled Header Accept: " + request.getHeader("Accept"));
				  
			  }
			
		  }
		  else if (joinsURLPatternMatcher.find()) {
			  
				 // Get the mergeId 
				 MergeModel mergeModel = new MergeModel();
				 mergeModel.setId(Integer.parseInt(joinsURLPatternMatcher.group(1)));  
				 
				 MergesModel mergesModel = new MergesModel();
				 mergesModel.setDataModel(dataModel);
				 mergeModel = mergesModel.retrieveMergeAsMergeModelByMergeId(mergeModel.getId());
			  
				  if (headerAcceptsAsStringList.contains("text/html")) { 
					  
					  //Otherwise degree symbols turn into question-marks
					  response.setCharacterEncoding("UTF-8");
					  
					  response.setContentType("text/html");
					  
					  String joinsAsHTML = null;
						
					  JoinsModel joinsModel = new JoinsModel();
					  joinsModel.setDataModel(dataModel);
					  
					  //FIXME
					  //joinsModel.setUserModel(userModel);

					  CachedRowSet joinsAsCachedRowSet = joinsModel.retrieveJoinsAsCachedRowSetByMergeId(mergeModel.getId());
				
					  if (joinsAsCachedRowSet != null) {
				
						  MergeFunctionsModel mergeFunctionsModel = new MergeFunctionsModel();
						  
						    mergeFunctionsModel.setMergeModel(mergeModel);	    
						    mergeFunctionsModel.setJoinsAsCachedRowSet(joinsAsCachedRowSet);
						    mergeFunctionsModel.setJoinsAsDecoratedXHTMLTableUsingJoinsAsCachedRowSet();
						    joinsAsHTML = mergeFunctionsModel.getJoinsAsDecoratedXHTMLTable();
						    
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
				 
				 MergesModel mergesModel = new MergesModel();
				 mergesModel.setDataModel(dataModel);
				 mergeModel = mergesModel.retrieveMergeAsMergeModelByMergeId(mergeModel.getId());
			  
				  if (headerAcceptsAsStringList.contains("text/html")) { 
					  
					  //Otherwise degree symbols turn into question-marks
					  response.setCharacterEncoding("UTF-8");
					  
					  response.setContentType("text/html");
					  
					  String resolutionsByColumnAsHTML = null;
						

						ResolutionsByColumnModel resolutionsByColumnModel = new ResolutionsByColumnModel();
						resolutionsByColumnModel.setDataModel(dataModel);
						
						//FIXME
						//resolutionsByColumnModel.setUserModel(userModel);

						resolutionsByColumnAsHTML = resolutionsByColumnModel.retrieveResolutionsByColumnAsDecoratedXHTMLTableUsingMergeModel(mergeModel);
					  
					  
					  response.getWriter().print(resolutionsByColumnAsHTML);
					  
				  } else {
					  
					  response.setContentType("text/plain");
					  response.getWriter().println("Unhandled Header Accept: " + request.getHeader("Accept"));
					  
				  }   

				  
		  } else if (resolutionsByRowURLPatternMatcher.find()) {
			  
				 // Get the mergeId 
				 MergeModel mergeModel = new MergeModel();
				 mergeModel.setId(Integer.parseInt(resolutionsByRowURLPatternMatcher.group(1)));  
				 
				 MergesModel mergesModel = new MergesModel();
				 mergesModel.setDataModel(dataModel);
				 mergeModel = mergesModel.retrieveMergeAsMergeModelByMergeId(mergeModel.getId());
			  
				  if (headerAcceptsAsStringList.contains("text/html")) { 
					  
					  //Otherwise degree symbols turn into question-marks
					  response.setCharacterEncoding("UTF-8");
					  
					  response.setContentType("text/html");
					  
					  String resolutionsByRowAsHTML = null;
						

						ResolutionsByRowModel resolutionsByRowModel = new ResolutionsByRowModel();
						resolutionsByRowModel.setDataModel(dataModel);
						
						//FIXME
						//resolutionsByColumnModel.setUserModel(userModel);

						resolutionsByRowAsHTML = resolutionsByRowModel.retrieveResolutionsByRowAsDecoratedXHTMLTableUsingMergeModel(mergeModel);
					  
					  
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
						 
						 MergesModel mergesModel = new MergesModel();
						 mergesModel.setDataModel(dataModel);
						 mergeModel = mergesModel.retrieveMergeAsMergeModelByMergeId(mergeModel.getId());
					  
					  String joinAsDecoratedXHTMLTable = null;
					  
					  	JoinFunctionsModel joinFunctionsModel = new JoinFunctionsModel();
						joinFunctionsModel.setJoinModel(new JoinModel()); // Unnecessary but explicit
						joinFunctionsModel.setMergeModel(mergeModel);
						joinFunctionsModel.setJoinAsDecoratedXHTMLTableByJoinModel();
						
					
						joinAsDecoratedXHTMLTable = joinFunctionsModel.getJoinAsDecoratedXHTMLTable();
					  
					  
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

		DataModel dataModel = new DataModel();
		dataModel.setDataModelUsingServletContext(request.getSession().getServletContext());

		dataModel.setDatabaseConnectableUsingDataModel();
		
		
		UserModel userModel = new UserModel();
		
		if (dataModel.isDatabaseConnectable()) {
		
			UsersCRUD usersCRUD = new UsersCRUD();
			usersCRUD.setDataModel(dataModel);
			userModel = usersCRUD.retrieveUserAsUserModelUsingUsername(request.getRemoteUser());
			userModel.setHttpServletRequest(request);
			
		}
		
		 Pattern exportURLPattern = Pattern.compile("^/merges/(\\d+)/exports$");
		 Matcher exportURLPatternMatcher = exportURLPattern.matcher(request.getPathInfo());
		
		 String[] headerAcceptsAsStringArray = request.getHeader("Accept").split(",");
		 List<String> headerAcceptsAsStringList = Arrays.asList(headerAcceptsAsStringArray);

		   
		 
		 if (request.getPathInfo().equals("/databases")) {
		 
			 //TODO:tmp
			 this.logger.info("Forwarding..");
			 request.getRequestDispatcher("/databases").forward(request, response);
			 
			 
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
							JSONArray uploadIds = jsonObject.getJSONArray("upload_id");
							
							try {


								MergeModel mergeModel = new MergeModel();
								
								mergeModel.getUpload1Model().setId(uploadIds.getInt(0));
								mergeModel.getUpload2Model().setId(uploadIds.getInt(1));
								
								
								
								
								MergesModel mergesModel = new MergesModel();
								
								mergesModel.setDataModel(dataModel);
								mergesModel.setUserModel(userModel);
								
								mergeModel = mergesModel.retrieveMergeAsMergeModelThroughCreatingMergeUsingMergeModel(mergeModel);
								
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
			 
				  response.setContentType("application/json");
				  String responseAsJSON = null;
				  
				  	// Get the mergeId for the new export
				  
				  	ExportModel exportModel = new ExportModel();
				  	exportModel.getMergeModel().setId(Integer.parseInt(exportURLPatternMatcher.group(1)));
				  

					ExportsModel exportsModel = new ExportsModel();
					
					exportsModel.setDataModel(dataModel);
					exportsModel.setUserModel(userModel);
					
					exportModel = exportsModel.retrieveExportAsExportModelThroughCreatingExportUsingExportModel(exportModel);
					
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

		DataModel dataModel = new DataModel();
		dataModel.setDataModelUsingServletContext(request.getSession().getServletContext());

		UsersCRUD usersCRUD = new UsersCRUD();
		usersCRUD.setDataModel(dataModel);
		UserModel userModel = usersCRUD.retrieveUserAsUserModelUsingUsername(request.getRemoteUser());
		userModel.setHttpServletRequest(request);
		
		//TODO: centralize these
		
		 Pattern joinsURLPattern = Pattern.compile("/merges/(\\d+)/joins");
		 Matcher joinsURLPatternMatcher = joinsURLPattern.matcher(request.getPathInfo());
		 
		 Pattern resolutionsByColumnURLPattern = Pattern.compile("/merges/(\\d+)/resolutions-by-column");
		 Matcher resolutionsByColumnURLPatternMatcher = resolutionsByColumnURLPattern.matcher(request.getPathInfo());
		 
		 Pattern resolutionsByRowURLPattern = Pattern.compile("/merges/(\\d+)/resolutions-by-row");
		 Matcher resolutionsByRowURLPatternMatcher = resolutionsByRowURLPattern.matcher(request.getPathInfo());

		 Pattern resolutionsByCellURLPattern = Pattern.compile("/merges/(\\d+)/resolutions-by-cell");
		 Matcher resolutionsByCellURLPatternMatcher = resolutionsByCellURLPattern.matcher(request.getPathInfo());
			 
		 
		 if (joinsURLPatternMatcher.find()) {
			 
			 // Get the mergeId 
			 MergeModel mergeModel = new MergeModel();
			 mergeModel.setId(Integer.parseInt(joinsURLPatternMatcher.group(1)));
			 
			 
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
							
							JoinsModel joinsModel = new JoinsModel();
							
							joinsModel.setDataModel(dataModel);
							
							
							//Consider: Instead of using JSONObject, use JoinsFunctionsModel to convert it to something else? 
							// Maybe a list of JoinModel objects, joinsAsJoinModelList
							
							// This update should also recalculate conflicts and destroy old resolutions
							joinsModel.updateJoinsByMergeIdUsingJoinsAsJSONObject(mergeModel.getId(), jsonObject);
							
							
							
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
							
							ResolutionsByColumnModel resolutionsByColumnModel = new ResolutionsByColumnModel();
							
							resolutionsByColumnModel.setDataModel(dataModel);
							
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
								
								ResolutionsByRowModel resolutionsByRowModel = new ResolutionsByRowModel();
								
								resolutionsByRowModel.setDataModel(dataModel);
								
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
										
										ResolutionsByCellModel resolutionsByCellModel = new ResolutionsByCellModel();
										
										resolutionsByCellModel.setDataModel(dataModel);
										
										resolutionsByCellModel.updateResolutionsByCellByMergeIdUsingResolutionsByCellAsJSONObject(mergeModel.getId(), jsonObject);
										
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
			  
			  
		  } else {
			  
			  //FIXME: This will cause a parser error (Invalid JSON).
			  
			  response.getWriter().println("Unhandled Path Info: " + request.getPathInfo());
		  }
		  
		
	}
}
