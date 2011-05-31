package org.cggh.tools.dataMerger.files.filebases;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
			
			ArrayList<String> filesAsStringArrayList = new ArrayList<String>(); 
			
			String[] filesAsStringArray = serverPath.list();
			for (int i = 0; i < filesAsStringArray.length; i++) {
				filesAsStringArrayList.add(filesAsStringArray[i]);
			}
			
			filebaseModel.setFilesAsStringArrayList(filesAsStringArrayList);
			
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
				    
				    
				    versionAsString = entryAsHashMap.get("major_version_number") + "." + entryAsHashMap.get("minor_version_number") + "." + entryAsHashMap.get("revision_version_number");
			    
			    } else {
			    	this.logger.severe("headingsAsCSV is null");
			    }
			    
			    inputStreamReader.close();
			    
			} catch (FileNotFoundException e) {
				
				//The filebase installation log file was introduced in version 1.1.0
				versionAsString = "1.0.x";
				
			} catch (IOException e) {
				e.printStackTrace();
			}

			
			filebaseModel.setVersionAsString(versionAsString);
		}
		
		String os = System.getProperty("os.name").toLowerCase();
		
		if (os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0) {
			
			filebaseModel.setUnix(true);
			filebaseModel.setFilepathSeparator("/");
		}
		else if (os.indexOf( "mac" ) >= 0) {
			
			filebaseModel.setMac(true);
			filebaseModel.setFilepathSeparator("/");
		}
		else if (os.indexOf( "win" ) >= 0) {
			
			filebaseModel.setWindows(true);
			filebaseModel.setFilepathSeparator("\\");
		}

	
		return filebaseModel;
	}	
	
}
