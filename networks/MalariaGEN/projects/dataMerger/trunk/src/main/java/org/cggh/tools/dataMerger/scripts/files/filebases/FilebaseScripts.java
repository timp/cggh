package org.cggh.tools.dataMerger.scripts.files.filebases;

import java.io.File;

import org.cggh.tools.dataMerger.files.filebases.FilebaseModel;

public class FilebaseScripts {

	
	
	
	  public String deleteFilebaseUsingFilebaseModel (final FilebaseModel filebaseModel) {
		  
		  //TODO: Make sure this doesn't do something silly, like delete the wrong stuff.
		  
		  String responseAsPlainText = null;
		  
		  File filebaseServerDirectory = new File(filebaseModel.getServerPath());
		  
		  if (filebaseServerDirectory.isDirectory()) {
			  
			  if (deleteFileAndAllChildren(filebaseServerDirectory)) {
			  
				  responseAsPlainText = "The filebase has now been deleted.";
				  
			  } else {
				  
				  responseAsPlainText = "An error occurred while trying to delete the filebase.";
			  }
			  
		  } else {
			  responseAsPlainText = "Refused to delete filebase. Path specified in web.xml is not a directory.";
		  }
		  
	      return responseAsPlainText;
	  }

	  
	  private Boolean deleteFileAndAllChildren (final File file) {
		  
		  if (file.isDirectory()) {

			  //Delete all the files in this directory, if there are any.
			  if (file.listFiles().length > 0) {
				  
				  //Need to take a copy because the list will dynamically change
				  File[] filesAsFileArray = file.listFiles();
				  
	              for (int i = 0; i < filesAsFileArray.length; i++) {
	            	  
	            	  if (filesAsFileArray[i].isDirectory()) {

	            		  //Process this sub-directory 
	            		  if (!deleteFileAndAllChildren(filesAsFileArray[i])) {
	            			  return false;
	            		  }
	            		  
	            	  } else {
	            		  
	            		  //Delete this file from the directory
	            		  if (!filesAsFileArray[i].delete()) {
	            			  return false;
	            		  }
	            	  }
	                  
	              }
			  }

			  //Delete this empty directory
        	  if (!file.delete()) {
        		 return false; 
        	  }
              
	      } else {
	    	  
	    	  //Delete this file
        	  if (!file.delete()) {
        		  return false;
        	  }
        	  
	      }
		  
		  return true;
	  }
	

	  
	  
	  
}
