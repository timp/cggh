package org.cggh.tools.dataMerger.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.merges.MergesModel;
import org.cggh.tools.dataMerger.data.uploads.UploadsModel;
import org.cggh.tools.dataMerger.data.users.UserModel;
import org.cggh.tools.dataMerger.functions.FunctionsModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




/**
 * Servlet implementation class uploads
 */
public class DataController extends HttpServlet {



	
    /**
	 * 
	 */
	private static final long serialVersionUID = 2142411300172699330L;
	private DataModel dataModel = null;
	private UserModel userModel;
	
	private UploadsModel uploadsModel = null;
	private FunctionsModel functionsModel = null;	
	private MergesModel mergesModel = null;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public DataController() {
        super();
        
        //TODO: Set up a DataModel to store the connection details for each resource. Perhaps access by dataModel.getResource("uploads").getConnectionString()... dataModel.getResourcesAsList().
        
        this.setDataModel(new DataModel());
    	this.setUserModel(new UserModel());
    	
        this.setUploadsModel(new UploadsModel());
        this.setFunctionsModel(new FunctionsModel());
        this.setMergesModel(new MergesModel());
        
        

        
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
    
    public void setUploadsModel (final UploadsModel uploadsModel) {
        this.uploadsModel = uploadsModel;
    }  
    public UploadsModel getUploadsModel() {
        return this.uploadsModel;
    } 
    
    
    public void setFunctionsModel (final FunctionsModel functionsModel) {
        this.functionsModel = functionsModel;
    }  
    public FunctionsModel getFunctionsModel() {
        return this.functionsModel;
    }      
    
    public void setMergesModel (final MergesModel mergesModel) {
        this.mergesModel = mergesModel;
    }  
    public MergesModel getMergesModel() {
        return this.mergesModel;
    }     
    
    
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		this.dataModel.setDataModelByServletContext(request.getSession().getServletContext());
		this.userModel.setDataModel(this.getDataModel());
		this.userModel.setUserModelByUsername(request.getRemoteUser());
		

		  if (request.getPathInfo().equals("/uploads")) {

				this.uploadsModel.setDataModel(this.getDataModel());
				this.uploadsModel.setUserModel(this.getUserModel());

			  
			  CachedRowSet uploadsAsCachedRowSet = this.uploadsModel.getUploadsAsCachedRowSet();
		
			  PrintWriter out = response.getWriter();
			  
			  if (uploadsAsCachedRowSet != null) {
		
				    this.functionsModel.getUploadsFunctionsModel().setCachedRowSet(uploadsAsCachedRowSet);
				    this.functionsModel.getUploadsFunctionsModel().setDecoratedXHTMLTableByCachedRowSet();
				    out.print(functionsModel.getUploadsFunctionsModel().getDecoratedXHTMLTable());
				    
			  } else {
				  
				  out.print("<p>Error: uploadsAsCachedRowSet is null</p>");
					  
				  } 
			  
		  } else {
			  
			  System.out.println("Unhandled pathInfo.");
		  }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		this.getDataModel().setDataModelByServletContext(request.getSession().getServletContext());
		this.getUserModel().setDataModel(this.getDataModel());
		this.getUserModel().setUserModelByUsername(request.getRemoteUser());
				
		
		 if (request.getPathInfo().equals("/merges")) {
			  
		        this.getMergesModel().setDataModel(this.getDataModel());
		        this.getMergesModel().setUserModel(this.getUserModel());			  
			  
				  StringBuffer jb = new StringBuffer();
				  String line = null;
				  try {
				    BufferedReader reader = request.getReader();
				    while ((line = reader.readLine()) != null)
				      jb.append(line);
				  } catch (Exception e) { e.printStackTrace(); }

				  
				  JSONArray uploadIds = null;
					try {
						JSONObject jsonObject = new JSONObject(jb.toString());
						uploadIds = jsonObject.getJSONArray("upload_id");
		
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}


					try {


						this.getMergesModel().getMergeModel().getUpload1Model().setId(uploadIds.getInt(0));
						this.getMergesModel().getMergeModel().getUpload2Model().setId(uploadIds.getInt(1));
						
						
						this.getMergesModel().setMergeModelByCreatingMerge();
						
						
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
	         response.setContentType("application/json");
	         response.setCharacterEncoding("UTF-8");				
			
	         
	         //TODO: This needs to depend on the request, e.g. if the post was not ajax.
 
	         //TODO
	        log("merge_id: " + this.getMergesModel().getMergeModel().getId().toString());
	         
	        PrintWriter out = response.getWriter();
			out.println("{\"id\": \"" + this.getMergesModel().getMergeModel().getId().toString() + "\"}");		
			  
		  } else {
			  
			  System.out.println("Unhandled pathInfo.");
		  }
		  
		
	}

}
