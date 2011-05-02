package org.cggh.tools.dataMerger.files.filebases;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

public class FilebasesCRUD {

	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.files.filebases");

	
	public FilebaseModel retrieveFilebaseAsFilebaseModelUsingServletContext (final ServletContext servletContext) {
		
		FilebaseModel filebaseModel = new FilebaseModel();
		filebaseModel.setServerPath(servletContext.getInitParameter("filebaseServerPath"));
		filebaseModel.setFilebaseInstallationLogAsCSVFilePathRelativeToFilebaseServerPath(servletContext.getInitParameter("filebaseInstallationLogAsCSVFilePathRelativeToFilebaseServerPath"));
		
		File serverPath = new File(filebaseModel.getServerPath());

		// Determine existence.
		if (serverPath.exists()) {
			filebaseModel.setExistent(true);
		} else {
			filebaseModel.setExistent(false);
		}
		
		// Determine write-ability.
		if (serverPath.canWrite()) {
			filebaseModel.setWritable(true);
		} else {
			filebaseModel.setWritable(false);
		}

		// Determine readability.
		if (serverPath.canRead()) {
			filebaseModel.setReadable(true);
			
			filebaseModel.setFilesAsStringArray(serverPath.list());
			
		} else {
			filebaseModel.setReadable(false);
		}

		if (filebaseModel.isReadable()) {
			
			String versionAsString = null;
			
			//NOTE: FilebasesController POST should have created the filebase and a version file.
			
			try {
				
				FileInputStream fileInputStream = new FileInputStream(filebaseModel.getServerPath() + filebaseModel.getFilebaseInstallationLogAsCSVFilePathRelativeToFilebaseServerPath());
			    DataInputStream dataInputStream = new DataInputStream(fileInputStream);
			    InputStreamReader inputStreamReader = new InputStreamReader(dataInputStream);
			    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			    
			    
			    
			    String headingsAsCSV = bufferedReader.readLine();
			    
			    if (headingsAsCSV != null) {
			    	
			    	String[] headingsAsStringArray = headingsAsCSV.split(",");
				    	
			    	HashMap<String, String> entryAsHashMap = new HashMap<String, String>();
			    	
				    String entryAsCSV = null;
				    while ((entryAsCSV = bufferedReader.readLine()) != null)   {
				    	
				      String[] entryAsStringArray = entryAsCSV.split(",");
				      
				      for (int i = 0; i < headingsAsStringArray.length; i++) {
				    	  
				    	  entryAsHashMap.put(headingsAsStringArray[i], entryAsStringArray[i]);
				    	  
				      }
				      
				    }
				    
				    //TODO: Array of hashmaps, get the last entry, get the version as a string.
			    
			    } else {
			    	//TODO: error
			    	this.logger.severe("headingsAsCSV is null");
			    }
			    
			    inputStreamReader.close();
			    
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			
			filebaseModel.setVersionAsString(versionAsString);
		}
		
		
	
		return filebaseModel;
	}	
	
}
