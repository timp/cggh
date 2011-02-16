package org.cggh.tools.dataMerger.data.uploads;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.uploads.UploadsModel;
import org.cggh.tools.dataMerger.functions.FunctionsModel;

/**
 * Servlet implementation class uploads
 */
public class UploadsController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UploadsModel uploadsModel = null;
	private FunctionsModel functionsModel = null;
	private String uploadsAsDecoratedHTMLTable = null;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadsController() {
        super();

        
        
	    UploadsModel uploadsModel = new UploadsModel();
	    this.setUploadsModel(uploadsModel);

	    FunctionsModel functionsModel = new FunctionsModel();
	    this.setFunctionsModel(functionsModel);
        
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
    

    public void setUploadsAsDecoratedHTMLTable (final String uploadsAsDecoratedHTMLTable) {
        this.uploadsAsDecoratedHTMLTable = uploadsAsDecoratedHTMLTable;
    }  
    public String getUploadsAsDecoratedHTMLTable() {
        return this.uploadsAsDecoratedHTMLTable;
    }    
  
    

    
    
    
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		//TODO: This is a bit of a short-cut. Currently the only GET request for uploads by AJAX is for decorated HTML.
		
		this.uploadsModel.setHttpServletRequest(request);
		
		  CachedRowSet uploadsAsCachedRowSet = this.uploadsModel.getUploadsAsCachedRowSet();

		  PrintWriter out = response.getWriter();
		  
		  if (uploadsAsCachedRowSet != null) {

			    this.functionsModel.setCachedRowSet(uploadsAsCachedRowSet);
			    this.functionsModel.transformUploadsCachedRowSetIntoDecoratedXHTMLTable();
			    out.print(functionsModel.getDecoratedXHTMLTable());
			    
		  } else {
			  
			  out.print("<p>Error: uploadsAsCachedRowSet is null</p>");
			  
		  } 
	    
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
