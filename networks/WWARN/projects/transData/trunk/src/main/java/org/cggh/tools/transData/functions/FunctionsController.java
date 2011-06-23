package org.cggh.tools.transData.functions;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cggh.tools.transData.functions.files.FileFunctions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


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
		
		String[] headerAcceptsAsStringArray = null;
		
		//
		//this.getLogger().info(request.getHeader("Accept"));
		//expecting "text/plain, */*; q=0.01"
		
		if (request.getHeader("Accept") != null) {
			
			headerAcceptsAsStringArray = request.getHeader("Accept").split(",");
			for (int i = 0; i < headerAcceptsAsStringArray.length; i++) {
				
				if (headerAcceptsAsStringArray[i].contains(";")) {
				
					headerAcceptsAsStringArray[i] = headerAcceptsAsStringArray[i].substring(0, headerAcceptsAsStringArray[i].indexOf(";"));
				}
				
				headerAcceptsAsStringArray[i] = headerAcceptsAsStringArray[i].trim();
				
				//
				//this.getLogger().info("will handle: " + headerAcceptsAsStringArray[i]);
			}
			
		} else {
			headerAcceptsAsStringArray = new String[0];
		}
		
		List<String> headerAcceptsAsStringList = Arrays.asList(headerAcceptsAsStringArray);	
		
			  if (request.getPathInfo().equals("/data/transformers/xmlurl-to-csvfile")) {
				  
				  if (headerAcceptsAsStringList.contains("text/csv") || headerAcceptsAsStringList.contains("*/*")) {
				  
					  response.setContentType("text/csv");

						URL url = new URL(request.getParameter("url"));
						InputStream inputStream = url.openStream();
						FileFunctions fileFunctions = new FileFunctions();
						Document document = fileFunctions.convertDataAsInputStreamIntoDocument(inputStream);
						
						///////////////////////
						StringBuilder dataAsCSV = new StringBuilder();
						
						for (int i = 0; i < document.getChildNodes().getLength(); i ++) {
							
							if (i != 0) {
								dataAsCSV.append("\n");
							}
							
							dataAsCSV.append(document.getChildNodes().item(i).getNodeName()).append(",").append(document.getChildNodes().item(i).getNodeValue());
							
						}
						
						
						///////////////////////////
						
						
			            response.setHeader("Content-Disposition", "attachment; fileName=data.csv");
	
			            response.setContentLength(dataAsCSV.toString().getBytes().length);
			            
			            int length = 0;
			            
			            byte[] byteBuffer = new byte[2048]; //8192
			            
			              DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(dataAsCSV.toString().getBytes()));
			
			              while ((dataInputStream != null) && ((length = dataInputStream.read(byteBuffer)) != -1))
			              {
			            	  response.getOutputStream().write(byteBuffer, 0, length);
			              }
			
			              dataInputStream.close();
			              response.getOutputStream().flush();
			              response.getOutputStream().close();	
					
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

	public Logger getLogger() {
		return logger;
	}

}
