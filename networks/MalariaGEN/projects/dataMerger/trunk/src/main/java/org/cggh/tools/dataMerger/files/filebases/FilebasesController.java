package org.cggh.tools.dataMerger.files.filebases;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cggh.tools.dataMerger.data.databases.DatabasesCRUD;
import org.cggh.tools.dataMerger.data.users.UserModel;
import org.cggh.tools.dataMerger.data.users.UsersCRUD;
import org.cggh.tools.dataMerger.scripts.files.filebases.FilebaseScripts;


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
					
					File filebaseInstallationLogAsCSVFile = new File(filebaseInstallationLogAsCSVFilePath);
					
					if (!filebaseInstallationLogAsCSVFile.exists()) {
						
						File filebaseInstallationLogAsCSVFileParent = new File(filebaseInstallationLogAsCSVFile.getParent());
						
						if (filebaseInstallationLogAsCSVFileParent.mkdirs()) {
							
							if (filebaseInstallationLogAsCSVFile.createNewFile()) {
							
								Calendar calendar = Calendar.getInstance();
							    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								
								String filebaseInstallationLogAsCSVHeadings = "major_version_number,minor_version_number,revision_version_number,created_by_user_id,created_by_username,created_datetime"; 
								String filebaseInstallationLogAsCSVEntry = "1,1,0," + userModel.getId() + "," + userModel.getUsername() + "," + simpleDateFormat.format(calendar.getTime());
								
								try {
									FileWriter filebaseInstallationLogAsCSVFileWriter = new FileWriter(filebaseInstallationLogAsCSVFilePath); // a second parameter of true in the FileWriter constructor will switch on append mode.
									BufferedWriter filebaseInstallationLogAsCSVFileBufferedWriter = new BufferedWriter(filebaseInstallationLogAsCSVFileWriter);
									
									filebaseInstallationLogAsCSVFileBufferedWriter.write(filebaseInstallationLogAsCSVHeadings);
									filebaseInstallationLogAsCSVFileBufferedWriter.newLine();
									
									filebaseInstallationLogAsCSVFileBufferedWriter.write(filebaseInstallationLogAsCSVEntry);
									filebaseInstallationLogAsCSVFileBufferedWriter.newLine();
									
									filebaseInstallationLogAsCSVFileBufferedWriter.close();
									
									responseAsPlainText = "Created filebase directory and installation log file.";
									
								} catch (IOException e) {
									e.printStackTrace();
								}
								
							} else {
								responseAsPlainText = "An error occurred while trying to create the installation log file.";
								logger.severe("filebaseInstallationLogAsCSVFile.createNewFile() was false");
							}
							
						} else {
							responseAsPlainText = "An error occurred while trying to create the directory for the installation log file.";
							logger.severe("filebaseInstallationLogAsCSVFile.mkdirs() was false");
						}
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
			  
			  FilebasesCRUD filebasesCRUD = new FilebasesCRUD();
			  FilebaseModel filebaseModel = filebasesCRUD.retrieveFilebaseAsFilebaseModelUsingServletContext(request.getSession().getServletContext());
			  
			  FilebaseScripts filebaseScripts = new FilebaseScripts();
			  responseAsPlainText = filebaseScripts.deleteFilebaseUsingFilebaseModel(filebaseModel);
			
			  response.getWriter().print(responseAsPlainText);
		
		  } else {
		
			  response.setContentType("text/plain");
			  response.getWriter().print("Unsupported accept header.");
			  
		  }	 
		
	}    
    

	
}
