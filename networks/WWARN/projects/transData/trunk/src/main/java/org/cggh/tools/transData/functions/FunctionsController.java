package org.cggh.tools.transData.functions;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cggh.tools.transData.functions.data.DataFunctions;



/**
 * Servlet implementation class FunctionsController
 */
public class FunctionsController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -254795990818465808L;
	private final Logger logger = Logger.getLogger(this.getClass().getPackage().getName());
       
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
		
		  if (request.getPathInfo().equals("/data/transformers/XMLURL-to-CSVRowsFile")) {
			  
			  DataFunctions dataFunctions = new DataFunctions();
			  List<String> headerAcceptsAsStringList = dataFunctions.convertAcceptHeaderAsStringIntoHeaderAcceptsAsStringList(request.getHeader("Accept"));
			  
			  if (headerAcceptsAsStringList.contains("text/csv") || headerAcceptsAsStringList.contains("*/*")) {
			  
				  response.setContentType("text/csv");

				  if (!dataFunctions.isInvalidURL(request.getParameter("URL"))) {
					  
					  String dataAsCSVString = dataFunctions.convertXMLFromURLIntoCSVRowsWithXPathFieldLabelsAsStringUsingUrlAsString(request.getParameter("URL"));
					  
					  if (dataAsCSVString != null) {
					  
						   //Serve the string as a file
						  
						  //TODO: How to get the file name?
						  
						    String fileName = dataFunctions.convertURLAsStringIntoFileName(request.getParameter("URL"));
							
						    //FIXME: sanitize
						    
				            response.setHeader("Content-Disposition", "attachment; fileName=" + fileName);
			
				            response.setContentLength(dataAsCSVString.toString().getBytes().length);
				            
				            int length = 0;
				            
				            byte[] byteBuffer = new byte[2048];
				            
				              DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(dataAsCSVString.toString().getBytes()));
				
				              while ((dataInputStream != null) && ((length = dataInputStream.read(byteBuffer)) != -1)) {
				            	  response.getOutputStream().write(byteBuffer, 0, length);
				              }
				
				              dataInputStream.close();
				              response.getOutputStream().flush();
				              response.getOutputStream().close();	
					
					  } else {
						
						  //TODO: Is this an error, or is there just no data?
						logger.severe("dataAsCSVString is null");
						response.setContentType("text/plain");
						response.getWriter().println("Error: dataAsCSVString is null.");
					  }
					  
				  } else {
					 
					  response.setContentType("text/plain");
					  response.getWriter().println("Invalid URL: " + request.getParameter("URL"));
					  
				  }
				  
				  
		              
			  } else {

				response.setContentType("text/plain");
				response.getWriter().println("Unhandled Header Accept: " + request.getHeader("Accept"));
					
			  }
				  
		  } else {
			 
			  response.setContentType("text/plain");
			  response.getWriter().println("Unhandled Path Info: " + request.getPathInfo());
		  }
			
		
	}


	public Logger getLogger() {
		return logger;
	}

}
