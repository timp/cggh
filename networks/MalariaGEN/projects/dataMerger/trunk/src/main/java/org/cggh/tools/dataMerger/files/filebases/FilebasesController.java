package org.cggh.tools.dataMerger.files.filebases;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cggh.tools.dataMerger.data.databases.DatabasesCRUD;
import org.cggh.tools.dataMerger.data.users.UserModel;
import org.cggh.tools.dataMerger.data.users.UsersCRUD;


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
			  
			  // Create the filebase and a version file.
			  
			  FilebasesCRUD filebasesCRUD = new FilebasesCRUD();
			  FilebaseModel filebaseModel = filebasesCRUD.retrieveFilebaseAsFilebaseModelUsingServletContext(request.getSession().getServletContext());
			  
			  File filebaseServerDirectory = new File(filebaseModel.getServerPath());
			  
			  if (filebaseServerDirectory.mkdirs()) {
					

				  	DatabasesCRUD databasesCRUD = new DatabasesCRUD();
				  	UsersCRUD usersCRUD = new UsersCRUD();
				  	usersCRUD.setDatabaseModel(databasesCRUD.retrieveDatabaseAsDatabaseModelUsingServletContext(getServletContext()));
				  	UserModel userModel = usersCRUD.retrieveUserAsUserModelUsingUsername(request.getRemoteUser());
				  
				  
					String filebaseInstallationLogAsCSVFilePath = filebaseModel.getServerPath() + filebaseModel.getFilebaseInstallationLogAsCSVFilePathRelativeToFilebaseServerPath();
					String filebaseInstallationLogAsCSVHeadings = "major_version_number,minor_version_number,revision_version_number,created_by_user_id,created_datetime"; 
					String filebaseInstallationLogAsCSVEntry = "1,1,0," + userModel.getId() + ",created_datetime";
					
					try {
						FileWriter filebaseInstallationLogAsCSVFileWriter = new FileWriter(filebaseInstallationLogAsCSVFilePath); // a second parameter of true in the FileWriter constructor will switch on append mode.
						BufferedWriter filebaseInstallationLogAsCSVFileBufferedWriter = new BufferedWriter(filebaseInstallationLogAsCSVFileWriter);
						
						filebaseInstallationLogAsCSVFileBufferedWriter.write(filebaseInstallationLogAsCSVHeadings);
						filebaseInstallationLogAsCSVFileBufferedWriter.newLine();
						
						filebaseInstallationLogAsCSVFileBufferedWriter.write(filebaseInstallationLogAsCSVEntry);
						filebaseInstallationLogAsCSVFileBufferedWriter.newLine();
						
						
						responseAsPlainText = "Created filebase directory and installation log file.";
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					
			  } else {

				  responseAsPlainText = "Failed to create filebase directory and installation log file.";
			  }				
		  
			  
			  response.getWriter().print(responseAsPlainText);
		
		  } else {
		
			  response.setContentType("text/plain");
			  response.getWriter().print("Unsupported accept header.");
			  
		  }	 
		
	}    
	
	
	protected void doDelete (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String[] headerAcceptsAsStringArray = request.getHeader("Accept").split(",");
		List<String> headerAcceptsAsStringList = Arrays.asList(headerAcceptsAsStringArray);

			if (headerAcceptsAsStringList.contains("text/plain")) { 
				 
			  response.setContentType("text/plain");
			  String responseAsPlainText = null;
			  
			  
			  //TODO:Code
			  FilebasesCRUD filebasesCRUD = new FilebasesCRUD();
			  FilebaseModel filebaseModel = filebasesCRUD.retrieveFilebaseAsFilebaseModelUsingServletContext(request.getSession().getServletContext());
			  File filebaseServerDirectory = new File(filebaseModel.getServerPath());
			  
			  if (filebaseServerDirectory.isDirectory()) {
				  
				  if (filebaseServerDirectory.list().length == 0) {
					  
					  filebaseServerDirectory.delete();
					  
					  responseAsPlainText = "Filebase deleted.";
					  
				  } else {
					
					  responseAsPlainText = "Will not delete filebase. Directory is not empty.";
				  }
				  
				  
			  } else {
				  responseAsPlainText = "Failed to delete filebase. Path specified in web.xml is not a directory.";
			  }
		  
			  
			  response.getWriter().print(responseAsPlainText);
		
		  } else {
		
			  response.setContentType("text/plain");
			  response.getWriter().print("Unsupported accept header.");
			  
		  }	 
		
	}    
    
	//TODO: Code
	  public static void deleteFileAndAllChildren (final File file) {
		  
	     
	      if (file.isDirectory()) {
	    	  
	          if (file.listFiles() != null) {
	        	  
	              for (int i = 0; i < file.listFiles().length; i++) {
	            	  
	                  if (file.listFiles()[i].isDirectory()) {
	                	  deleteFileAndAllChildren(file.listFiles()[i]);
	                  }
	                  file.listFiles()[i].delete();
	              }
	          }
	          
	          
	          
	      } else {
	    	  if (!file.delete()) {
		            //logger.severe("Could not delete file: " + file);
	    	  }
	      }
	      
	  }
	
}
