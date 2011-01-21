package org.cggh.tools.dataMerger.data.uploads.model;

import java.util.*;

public class UploadsModel {
	
   public List getMiddleNames(String firstName) {

     List lastNames = new ArrayList();

     if (firstName.equals("Bob")) {
    	 lastNames.add("Bib");
    	 lastNames.add("Bab");
     }
     else if (firstName.equals("Fred")) {
    	 lastNames.add("Frod");
    	 lastNames.add("Frud");
    	 lastNames.add("Frad");
     }
     else if (firstName.equals("Jane")) {
    	 lastNames.add("Jen");
     }
     else {
    	 lastNames.add("?");
     }
     return(lastNames);
   }
}
