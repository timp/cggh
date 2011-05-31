package org.cggh.tools.dataMerger.scripts.files.filebases;

import java.io.File;

import org.cggh.tools.dataMerger.files.filebases.FilebaseModel;

public class FilebaseScripts {

	
	
	
	  public String deleteFilebaseUsingFilebaseModel (final FilebaseModel filebaseModel) {
		  
		  //TODO: Make sure this doesn't do something silly, like delete the wrong stuff.
		  
		  String responseAsPlainText = null;
		  
		  File filebaseServerDirectory = new File(filebaseModel.getServerPath());
		  
		  if (filebaseServerDirectory.isDirectory()) {
			  
			  if (filebaseServerDirectory.list().length == 0) {
				  
				  filebaseServerDirectory.delete();
				  
				  responseAsPlainText = "Filebase deleted.";
				  
			  } else {
				
				  FilebaseScripts filebaseScripts = new FilebaseScripts();
				  filebaseScripts.deleteFileAndAllChildren(filebaseServerDirectory);
				  
				  responseAsPlainText = "Filebase and all contained files deleted.";
			  }
			  
			  
		  } else {
			  responseAsPlainText = "Failed to delete filebase. Path specified in web.xml is not a directory.";
		  }
		  
	      return responseAsPlainText;
	  }
	  
	  private void deleteFileAndAllChildren (final File file) {
		  
		  //TODO: Code until it works!
		  //TODO: Remove System outs
		  
		  if (file.isDirectory()) {

              for (int i = 0; i < file.listFiles().length; i++) {
            	  
            	  if (file.listFiles()[i].isDirectory()) {
            		  System.out.println("deleting all of " + file.listFiles()[i].getAbsolutePath());
            		  deleteFileAndAllChildren(file.listFiles()[i]);
            	  } else {
            		  System.out.println("deleting file " + file.getAbsolutePath());
                	  file.delete();
            	  }
                  
              }

              System.out.println("deleting dir " + file.getAbsolutePath());
        	  file.delete();
              
	      } else {
	    	  
	    	  System.out.println("deleting file " + file.getAbsolutePath());
        	  file.delete();
	      }
	  }
	
}
