package org.cggh.tools.dataMerger.files.filebases;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class FilebasesController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2303049244156607708L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.files.filebases");

	
    public FilebasesController() {
        super();

        
    }	
	
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String[] headerAcceptsAsStringArray = request.getHeader("Accept").split(",");
		List<String> headerAcceptsAsStringList = Arrays.asList(headerAcceptsAsStringArray);

		  if (headerAcceptsAsStringList.contains("text/plain")) { 
				 
			  response.setContentType("text/plain");
			  String responseAsPlainText = null;
			  
			  
			  //TODO: code
		  
			  response.getWriter().print(responseAsPlainText);

		  } else {

			  response.setContentType("text/plain");
			  response.getWriter().print("Unsupported accept header.");
			  
		  }	 
		
	}    
    
}
