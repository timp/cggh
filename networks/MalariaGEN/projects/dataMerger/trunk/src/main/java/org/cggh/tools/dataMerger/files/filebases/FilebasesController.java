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
			  
			  
			  //TODO: code
			  
			  
			  //TODO: Create the filebase and a version file.
			  
			  UsersCRUD usersCRUD = new UsersCRUD();
			  UserModel userModel = usersCRUD.retrieveUserAsUserModelUsingUsername(request.getRemoteUser());
			  
			  FilebasesCRUD filebasesCRUD = new FilebasesCRUD();
			  FilebaseModel filebaseModel = filebasesCRUD.retrieveFilebaseAsFilebaseModelUsingServletContext(request.getSession().getServletContext());
			  
			  //TODO:code
			  
			  File filebaseServerDirectory = new File(filebaseModel.getServerPath());
				
			  if (filebaseServerDirectory.mkdirs()) {
					
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

				  this.logger.info("Failed to create filebase directory and installation log file.");
				  responseAsPlainText = "Failed to create filebase directory and installation log file.";
			  }				
		  
			  
			  response.getWriter().print(responseAsPlainText);
		
		  } else {
		
			  response.setContentType("text/plain");
			  response.getWriter().print("Unsupported accept header.");
			  
		  }	 
		
	}    
    
}