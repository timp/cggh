package org.cggh.tools.transData.functions;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class FunctionsController
 */
public class FunctionsController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FunctionsController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String[] headerAcceptsAsStringArray = request.getHeader("Accept").split(",");
		List<String> headerAcceptsAsStringList = Arrays.asList(headerAcceptsAsStringArray);	
		
			  if (request.getPathInfo().equals("/data/transformers/xmlurl-to-csvfile")) {
				  
				  if (headerAcceptsAsStringList.contains("application/json")) {
				  
					  response.setContentType("application/json");
					  response.getWriter().println("{\"foo\":\"bar\"}");
					  
				  } else {

					response.setContentType("text/plain");
					response.getWriter().println("Unhandled Header Accept: " + request.getHeader("Accept"));
						
				  }
					  
			  } else {
				 
				  response.setContentType("text/plain");
				  response.getWriter().println("Unhandled Path Info: " + request.getPathInfo());
			  }
			
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
