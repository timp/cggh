package org.cggh.tools.dataMerger.data.uploads.controller;

import org.cggh.tools.dataMerger.data.uploads.model.*;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class uploads
 */
public class UploadsController extends HttpServlet implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	
	private String middleName;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadsController() {
        super();
        // TODO Auto-generated constructor stub
    }

    
    /**
     * Property <code>name</code> (note capitalization) readable/writable.
     */
    public String getMiddleName() {
        return this.middleName;
    }
 
    /**
     * Setter for property <code>name</code>.
     * @param name
     */
    public void setMiddleName(final String middleName) {
        this.middleName = middleName;
    }    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
	    UploadsModel uploads = new UploadsModel();

	    List middleNames = uploads.getMiddleNames("Bob");		
		
	    // The results will be passed back (as an attribute) to the JSP view
	    // The attribute will be a name/value pair, the value in this case will be a List object 
	    request.setAttribute("middleNames", middleNames);
	    RequestDispatcher view = request.getRequestDispatcher(request.getRequestURL().toString());
	    view.forward(request, response); 		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
