package org.cggh.tools.dataMerger.data.uploads.model;

import java.util.*;

public class UploadsModel {

	public String getUploadsAsXHTMLTable () {
		
		
		
		return "<table><tr><th>name</th><th>id</th></tr><tr><td>file1</td><td>1</td></tr><tr><td>file2</td><td>2</td></tr></table>";
		
	}
	
	
   public List<String> getUploadsAsArrayList() {

     List<String> uploads = new ArrayList<String>();


     uploads.add("upload1");
     uploads.add("upload2");
     uploads.add("upload3");

     return(uploads);
   }
}
