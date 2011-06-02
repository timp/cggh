package org.cggh.tools.dataMerger.scripts.files.filebases.directories;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

import org.cggh.tools.dataMerger.data.users.UserModel;
import org.cggh.tools.dataMerger.files.filebases.FilebaseModel;

public class FilebaseDirectoriesScripts {

	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.scripts.files.filebases.directories");
	
	public Boolean createInitialDirectoriesUsingFilebaseModelAndUserModel(FilebaseModel filebaseModel, UserModel userModel) {

		Boolean success = null;
	  
		String filebaseInstallationLogAsCSVFilePath = filebaseModel.getServerPath() + filebaseModel.getFilebaseInstallationLogAsCSVFilePathRelativeToFilebaseServerPath();
		
		File filebaseInstallationLogAsCSVFile = new File(filebaseInstallationLogAsCSVFilePath);
		
		if (!filebaseInstallationLogAsCSVFile.exists()) {
			
			File filebaseInstallationLogAsCSVFileParent = new File(filebaseInstallationLogAsCSVFile.getParent());
			
			if (filebaseInstallationLogAsCSVFileParent.mkdirs()) {
				
				try {
					
					
					//Create log file
					filebaseInstallationLogAsCSVFile.createNewFile();
				
					Calendar calendar = Calendar.getInstance();
				    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					
					String filebaseInstallationLogAsCSVHeadings = "major_version_number,minor_version_number,revision_version_number,created_by_user_id,created_by_username,created_datetime"; 
					String filebaseInstallationLogAsCSVEntry = "1,1,0," + userModel.getId() + "," + userModel.getUsername() + "," + simpleDateFormat.format(calendar.getTime());

					FileWriter filebaseInstallationLogAsCSVFileWriter = new FileWriter(filebaseInstallationLogAsCSVFilePath); // a second parameter of true in the FileWriter constructor will switch on append mode.
					BufferedWriter filebaseInstallationLogAsCSVFileBufferedWriter = new BufferedWriter(filebaseInstallationLogAsCSVFileWriter);
					
					filebaseInstallationLogAsCSVFileBufferedWriter.write(filebaseInstallationLogAsCSVHeadings);
					filebaseInstallationLogAsCSVFileBufferedWriter.newLine();
					
					filebaseInstallationLogAsCSVFileBufferedWriter.write(filebaseInstallationLogAsCSVEntry);
					filebaseInstallationLogAsCSVFileBufferedWriter.newLine();
					
					filebaseInstallationLogAsCSVFileBufferedWriter.close();
					
					
					//Create uploads directory
					File uploadsDirectory = new File(filebaseModel.getServerPath() + "uploads");
					if (uploadsDirectory.mkdir()) {
						
						//Create exports directory
						File exportsDirectory = new File(filebaseModel.getServerPath() + "exports");
						if (exportsDirectory.mkdir()) {
						
							success = true;
							
						} else {
							
							logger.severe("exportsDirectory.mkdir() was false");
							success = false;
						}
						
					} else {
						
						logger.severe("uploadsDirectory.mkdir() was false");
						success = false;
					}

				} catch (IOException e) {
					e.printStackTrace();
					success = false;
				}
				
			} else {
				logger.severe("filebaseInstallationLogAsCSVFile.mkdirs() was false");
				success = false;
			}
		}
		
		return success;

	}

	public boolean deleteDirectoriesUsingFilebaseModel(FilebaseModel filebaseModel) {
		  
		  File filebaseServerDirectory = new File(filebaseModel.getServerPath());
		  
		  if (filebaseServerDirectory.isDirectory()) {
			  
			  if (!deleteAllFileChildren(filebaseServerDirectory)) {
			  
				  return false;
			  }
			  
		  } else {
			  return false;
		  }
		  
	      return true;
		
	}
	
	
	
	  private Boolean deleteAllFileChildren (final File file) {
		  
		  if (file.isDirectory()) {

			  //Delete all the files in this directory, if there are any.
			  if (file.listFiles().length > 0) {
				  
				  //Need to take a copy because the list will dynamically change
				  File[] filesAsFileArray = file.listFiles();
				  
	              for (int i = 0; i < filesAsFileArray.length; i++) {
	            	  
	            	  if (filesAsFileArray[i].isDirectory()) {

	            		  //Process this sub-directory 
	            		  if (!deleteAllFileChildren(filesAsFileArray[i]) ) {  
	            			  return false;
	            		  }
	            		  
	            	  }
	            	  
	            	  //Delete this file/sub-directory from the directory
            		  if (!filesAsFileArray[i].delete()) {
            			  return false;
            		  }
	                  
	              }
			  }
			  
			  return true;
              
	      } else {
	    	  //File is not a directory
	    	  return false;
	      }
		  
	  }





}
