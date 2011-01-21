package org.cggh.tools.dataMerger.data.uploads.controller;

import org.cggh.tools.dataMerger.data.uploads.model.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class uploads
 */
public class UploadsController extends HttpServlet implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private List<String> uploadsAsArrayList;
	private String uploadsAsXHTMLTable;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadsController() {
        super();

        
        
	    UploadsModel uploadsModel = new UploadsModel();

	    setUploadsAsXHTMLTable(uploadsModel.getUploadsAsXHTMLTable());	     
	    
	    setUploadsAsArrayList(uploadsModel.getUploadsAsArrayList());	       
        
        
    }


    public List<String> getUploadsAsArrayList() {
        return this.uploadsAsArrayList;
    }   
    
    public String getUploadsAsXHTMLTable() {
        return this.uploadsAsXHTMLTable;
    }
    
 
 

    

    public void setUploadsAsArrayList(final List<String> uploadsAsArrayList) {
        this.uploadsAsArrayList = uploadsAsArrayList;
    }  
    
    public void setUploadsAsXHTMLTable(final String uploadsAsXHTMLTable) {
        this.uploadsAsXHTMLTable = uploadsAsXHTMLTable;
    }    
    

    
    
    
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
	
		
	    // The results will be passed back (as an attribute) to the JSP view
	    // The attribute will be a name/value pair, the value in this case will be a List object 
	    //request.setAttribute("middleNames", middleNames);
	    //RequestDispatcher view = request.getRequestDispatcher(request.getRequestURL().toString());
	    //view.forward(request, response);
	    

	    PrintWriter out = response.getWriter();
	    out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " +
	                "Transitional//EN\">\n" +
	                "<html>\n" +
	                "<head><title>Uploads</title></head>\n" +
	                "<body>\n" +
	                "<h1>Uploads as XHTML</h1>\n" +
	                getUploadsAsXHTMLTable() +
	                "</body></html>");	    
	    
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
