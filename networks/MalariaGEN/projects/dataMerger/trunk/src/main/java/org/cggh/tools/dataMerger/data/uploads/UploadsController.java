package org.cggh.tools.dataMerger.data.uploads;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class uploads
 */
public class UploadsController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String uploadsAsDecoratedHTMLTable;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadsController() {
        super();

        
        
	    //UploadsModel uploadsModel = new UploadsModel();


        
    }


    public void setUploadsAsDecoratedHTMLTable (final String uploadsAsDecoratedHTMLTable) {
        this.uploadsAsDecoratedHTMLTable = uploadsAsDecoratedHTMLTable;
    }  
    public String getUploadsAsDecoratedHTMLTable() {
        return this.uploadsAsDecoratedHTMLTable;
    }    
  
    

    
    
    
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		String uploadsAsDecoratedHTMLTable = getUploadsAsDecoratedHTMLTable();
		
	    PrintWriter out = response.getWriter();
	    out.println(uploadsAsDecoratedHTMLTable);	    
	    
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
