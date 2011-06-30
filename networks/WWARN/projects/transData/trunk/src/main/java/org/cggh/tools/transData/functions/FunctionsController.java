package org.cggh.tools.transData.functions;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.cggh.tools.transData.data.fields.FieldModel;
import org.cggh.tools.transData.functions.data.DataFunctions;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;



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
					  
					  try {
						  
					  
						String urlResponseAsString = dataFunctions.convertUrlAsStringIntoUrlResponseAsString(request.getParameter("URL"));
					  
						//
						//this.getLogger().info(urlResponseAsString);
					  
					  	// Checking that the URL response is actually XML is achieved through attempting to parse it.
					    
						Document xmlAsDocument = dataFunctions.convertXmlAsStringIntoXmlAsDocument(urlResponseAsString);
						
						//Note: Could introduce prefix here.
						String parentNodeBaseXPathAsString = null;
						ArrayList<FieldModel> dataAsFieldModelArrayListWithXpathFieldLabels = dataFunctions.convertNodeListIntoFieldModelArrayListWithXPathFieldLabels(xmlAsDocument.getChildNodes(), parentNodeBaseXPathAsString);
						
						String dataAsCSVRowsString = dataFunctions.convertDataAsFieldModelArrayListIntoCSVRowsWithMappedCustomFieldLabelsAsString(dataAsFieldModelArrayListWithXpathFieldLabels); 
						
						//FIXME: sanitize
						String filename = dataFunctions.convertURLAsStringIntoFileName(request.getParameter("URL"));
						
						this.respondWithStringAsFileUsingResponseAndStringAndFilename(response, dataAsCSVRowsString, filename);
						
						  
					  } catch (IOException e) {
						  e.printStackTrace();
					  } catch (ParserConfigurationException e) {
						e.printStackTrace();
					} catch (SAXException e) {
						e.printStackTrace();
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


	private void respondWithStringAsFileUsingResponseAndStringAndFilename(
			HttpServletResponse response, String string,
			String filename) throws IOException {
		
		if (string != null) {
			  
	            response.setHeader("Content-Disposition", "attachment; fileName=" + filename);

	            response.setContentLength(string.getBytes().length);
	            
	            int length = 0;
	            
	            byte[] byteBuffer = new byte[2048];
	            
	              DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(string.getBytes()));
	
	              while ((dataInputStream != null) && ((length = dataInputStream.read(byteBuffer)) != -1)) {
	            	  response.getOutputStream().write(byteBuffer, 0, length);
	              }
	
	              dataInputStream.close();
	              response.getOutputStream().flush();
	              response.getOutputStream().close();	
		
	              
		  } else {
			
			//TODO: Is this an error, or is there just no data?
			response.setContentType("text/plain");
			response.getWriter().println("Error: string is null.");
		  }
		
	}

	public Logger getLogger() {
		return logger;
	}

}
