package org.cggh.tools.dataMerger.functions.data.users;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

public class UsersFunctions {

	public String getUsersAsDecoratedXHTMLTableUsingUsersAsCachedRowSet(
			CachedRowSet usersAsCachedRowSet) {
		
		StringBuffer usersAsDecoratedXHTMLTable = null;

		try {
			if (usersAsCachedRowSet.next()) {

				usersAsDecoratedXHTMLTable = new StringBuffer();
				
				usersAsDecoratedXHTMLTable.append("<table class=\"data-table\">");

				 usersAsDecoratedXHTMLTable.append("<thead>");
				 usersAsDecoratedXHTMLTable.append("<tr>");				 
				 usersAsDecoratedXHTMLTable.append("<th>Username</th>");
				 usersAsDecoratedXHTMLTable.append("</thead>");
				 
				 usersAsDecoratedXHTMLTable.append("<tbody>");
				 
				//because next() skips the first row.
				 usersAsCachedRowSet.beforeFirst();

				String rowStripeClassName = "even "; 
				String rowFirstClassName = "first ";
				String rowLastClassName = ""; 
				
				 
				while (usersAsCachedRowSet.next()) {
					
					if (rowStripeClassName == "odd ") {
						rowStripeClassName = "even ";
					} else {
						rowStripeClassName = "odd ";
					}
					
					//TODO: This might need changing when paging.
					if (usersAsCachedRowSet.isLast()) {
						rowLastClassName = "last ";
					}
					
					usersAsDecoratedXHTMLTable.append("<tr class=\"").append(rowStripeClassName).append(rowFirstClassName).append(rowLastClassName).append("\">");

					 
					 usersAsDecoratedXHTMLTable.append("<td>").append(usersAsCachedRowSet.getString(1)).append("</td>");
					  
					 usersAsDecoratedXHTMLTable.append("</tr>");
					 
					 
					 rowFirstClassName = "";
				  }

				usersAsDecoratedXHTMLTable.append("</tbody>");
				 
				usersAsDecoratedXHTMLTable.append("</table>");


			} else {
				
				usersAsDecoratedXHTMLTable = new StringBuffer("<p>There are no users.</p>");
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		
		return usersAsDecoratedXHTMLTable.toString();
	}

	
	   public String convertStringIntoHashAsHexStringUsingStringAndHashFunctionName (String string, String hashFunctionName) {
	    	
	    	//hashFunctionNameAsString = "SHA-512"
	    	
	    	String hashAsHexString = null;
	    	
	            try {
	            	MessageDigest messageDigest= MessageDigest.getInstance(hashFunctionName);
					messageDigest.update(string.getBytes());
		            
		            byte[] hashAsByteArray = messageDigest.digest();
		            
		            if (hashAsByteArray.length > 0) {
			            
			            hashAsHexString = "";
			            
			            for (int i = 0; i < hashAsByteArray.length; i++) {
			            	
			                byte hashByte = hashAsByteArray[i];
			                String hashByteAsHexString = Integer.toHexString(new Byte(hashByte));
			                while (hashByteAsHexString.length() < 2) {
			                    hashByteAsHexString = "0" + hashByteAsHexString;
			                }
			                hashByteAsHexString = hashByteAsHexString.substring(hashByteAsHexString.length() - 2);
			                hashAsHexString += hashByteAsHexString;
			            }
			            
		            }
					
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
	 
	            
	 
			return hashAsHexString;
	    }
	
}
