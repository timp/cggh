package org.cggh.tools.dataMerger.files.filebases;

import java.io.File;

import javax.servlet.ServletContext;

public class FilebasesCRUD {

	
	
	public FilebaseModel retrieveFilebaseAsFilebaseModelUsingServletContext (final ServletContext servletContext) {
		
		FilebaseModel filebaseModel = new FilebaseModel();
		filebaseModel.setServerPath(servletContext.getInitParameter("filebaseServerPath"));
		
		File serverPath = new File(filebaseModel.getServerPath());

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
			
			//TODO: read version from file
			
			filebaseModel.setVersionAsString(versionAsString);
		}
	
		return filebaseModel;
	}	
	
}
