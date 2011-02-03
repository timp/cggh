package org.cggh.tools.dataMerger.data.controller;

import org.cggh.tools.dataMerger.data.model.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class uploads
 */
public class DataController extends HttpServlet implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1244304670693323712L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DataController() {
        super();

        
    }

    public void createDatabase () {

    }     
    
    
    
    
    //In case this needs to be a servlet
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    PrintWriter out = response.getWriter();
	    out.println("Hey! What are you doing here?");	    
	    
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
