package org.cggh.tools.dataMerger.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.merges.MergeModel;
import org.cggh.tools.dataMerger.data.merges.MergesModel;
import org.cggh.tools.dataMerger.data.uploads.UploadsModel;
import org.cggh.tools.dataMerger.data.users.UserModel;
import org.cggh.tools.dataMerger.functions.uploads.UploadsFunctionsModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




public class DataController extends HttpServlet {



	
    /**
	 * 
	 */
	private static final long serialVersionUID = 2142411300172699330L;
	private DataModel dataModel = null;
	private UserModel userModel;

    public DataController() {
        super();
        
        //TODO: Set up a DataModel to store the connection details for each resource. Perhaps access by dataModel.getResource("uploads").getConnectionString()... dataModel.getResourcesAsList().
        
        this.setDataModel(new DataModel());
    	this.setUserModel(new UserModel());

        
    }

    public void setDataModel (final DataModel dataModel) {
        this.dataModel  = dataModel;
    }
    public DataModel getDataModel () {
        return this.dataModel;
    }     

    public void setUserModel (final UserModel userModel) {
        this.userModel  = userModel;
    }
    public UserModel getUserModel () {
        return this.userModel;
    }     
    
    
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		this.getDataModel().setDataModelByServletContext(request.getSession().getServletContext());
		this.getUserModel().setDataModel(this.getDataModel());
		this.getUserModel().setUserModelByUsername(request.getRemoteUser());
		

		  if (request.getPathInfo().equals("/uploads")) {

			  
			  String[] headerAcceptsAsStringArray = request.getHeader("Accept").split(",");
			  List<String> headerAcceptsAsStringList = Arrays.asList(headerAcceptsAsStringArray);
			  
			  if (headerAcceptsAsStringList.contains("text/html")) { 
			  
				  response.setContentType("text/html");
				  
				  String uploadsAsHTML = null;
					
				  UploadsModel uploadsModel = new UploadsModel();
				  uploadsModel.setDataModel(this.getDataModel());
				  uploadsModel.setUserModel(this.getUserModel());

				  CachedRowSet uploadsAsCachedRowSet = uploadsModel.retrieveUploadsAsCachedRowSetUsingUserId(this.getUserModel().getId());
			
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
			  
		  } else {
			  
			  response.getWriter().println("Unhandled Path Info: " + request.getPathInfo());
		  }
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		this.getDataModel().setDataModelByServletContext(request.getSession().getServletContext());
		this.getUserModel().setDataModel(this.getDataModel());
		this.getUserModel().setUserModelByUsername(request.getRemoteUser());
				
		
		 if (request.getPathInfo().equals("/merges")) {
			 
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
							JSONArray uploadIds = jsonObject.getJSONArray("upload_id");
							
							try {


								MergeModel mergeModel = new MergeModel();
								mergeModel.getUpload1Model().setId(uploadIds.getInt(0));
								mergeModel.getUpload2Model().setId(uploadIds.getInt(1));
								
								MergesModel mergesModel = new MergesModel();
								
								mergesModel.setDataModel(this.getDataModel());
								mergesModel.setUserModel(this.getUserModel());
								
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
				  
				  response.setContentType("text/plain");
				  response.getWriter().println("Unhandled Header Accept: " + request.getHeader("Accept"));
				  
			  }
			  
		  } else {
			  
			  response.getWriter().println("Unhandled Path Info: " + request.getPathInfo());
		  }
		  
		
	}

}
