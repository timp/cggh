package org.cggh.tools.dataMerger.functions.uploads;

import javax.sql.rowset.CachedRowSet;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;



public class UploadsFunctions implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5429836473276144235L;
	private CachedRowSet cachedRowSet = null;
	private String xhtmlTable = null;
	private String decoratedXHTMLTable = null;
	
	public UploadsFunctions() {
		
	}

	
	public void setCachedRowSet (final CachedRowSet cachedRowSet) {
		
		this.cachedRowSet = cachedRowSet;
	}
	public CachedRowSet getCachedRowSet () {
		
		return this.cachedRowSet;
	}	
	public void setXHTMLTable (final String xhtmlTable) {
		
		this.xhtmlTable = xhtmlTable;
	}
	public String getXHTMLTable () {
		
		return this.xhtmlTable;
	}	
	public void setDecoratedXHTMLTable (final String decoratedXHTMLTable) {
		
		this.decoratedXHTMLTable = decoratedXHTMLTable;
	}
	public String getDecoratedXHTMLTable () {
		
		return this.decoratedXHTMLTable;
	}	

	public void setDecoratedXHTMLTableByCachedRowSet () {
		
		String decoratedXHTMLTable = null;

			try {
				if (this.getCachedRowSet().next()) {

					decoratedXHTMLTable = "";
					
					decoratedXHTMLTable = decoratedXHTMLTable.concat("<table class=\"uploads-table\">");

					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<thead>");
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<tr>");
					 
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th><!-- Column for checkboxes --></th>");
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th>ID<!--  <a href=\"javascript:TODOsort();\">sort up/down</a> --></th>");
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th>Filename<!--  <a href=\"javascript:TODOsort();\">sort up/down</a> --></th>");
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th>Uploaded<!--  <a href=\"javascript:TODOsort();\">sort up/down</a> --></th>");
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th class=\"fileSizeHeadingContainer\">Size<!--  <a href=\"javascript:TODOsort();\">sort up/down</a> --></th>");
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th><!-- Column for download links --></th>");
					 
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("</tr>");
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("</thead>");
					 
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<tbody>");
					 
					//because next() skips the first row.
					 this.getCachedRowSet().beforeFirst();

					String rowStripeClassName = "even "; 
					String rowFirstClassName = "first ";
					String rowLastClassName = ""; 
					
					DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");  

					 
					while (this.getCachedRowSet().next()) {
						
						if (rowStripeClassName == "odd ") {
							rowStripeClassName = "even ";
						} else {
							rowStripeClassName = "odd ";
						}
						
						//TODO: This might need changing when paging.
						if (this.getCachedRowSet().isLast()) {
							rowLastClassName = "last ";
						}
						
						decoratedXHTMLTable = decoratedXHTMLTable.concat("<tr class=\"" + rowStripeClassName + rowFirstClassName + rowLastClassName + "\">");

						 decoratedXHTMLTable = decoratedXHTMLTable.concat("<td><input type=\"checkbox\" name=\"upload_id\" value=\"" + this.getCachedRowSet().getString("id") + "\" /></td>");
						 decoratedXHTMLTable = decoratedXHTMLTable.concat("<td>" + this.getCachedRowSet().getString("id") + "</td>");
						 decoratedXHTMLTable = decoratedXHTMLTable.concat("<td><a href=\"/dataMerger/files/uploads/" + this.getCachedRowSet().getString("id") + "\">" + this.getCachedRowSet().getString("original_filename") + "</a></td>");
						 decoratedXHTMLTable = decoratedXHTMLTable.concat("<td>" + dateFormat.format(this.getCachedRowSet().getTimestamp("created_datetime")) + "</td>");
						 decoratedXHTMLTable = decoratedXHTMLTable.concat("<td class=\"fileSizeContainer\">" + convertBytesAsLongIntoMegaBytesAsString(this.getCachedRowSet().getLong("file_size_in_bytes")) + "</td>");
						 decoratedXHTMLTable = decoratedXHTMLTable.concat("<td><a href=\"/dataMerger/files/uploads/" + this.getCachedRowSet().getString("id") + "\">Download</a></td>");
						 
						 decoratedXHTMLTable = decoratedXHTMLTable.concat("</tr>");
						 
						 
						 rowFirstClassName = "";
					  }

					decoratedXHTMLTable = decoratedXHTMLTable.concat("</tbody>");
					 
					decoratedXHTMLTable = decoratedXHTMLTable.concat("</table>");
					
					decoratedXHTMLTable = decoratedXHTMLTable.concat("<!-- <div>TODO: paging</div> -->");
					
					decoratedXHTMLTable = decoratedXHTMLTable.concat("<button class=\"merge-uploads-button\">Merge</button><img class=\"merging-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Merging...\"/>");
	
				} else {
					
					decoratedXHTMLTable = "<p>You have no uploaded files.</p>";
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		this.setDecoratedXHTMLTable(decoratedXHTMLTable);
	
	}	
	
    public static String convertBytesAsLongIntoMegaBytesAsString(Long bytes) {
    	
        final double BASE = 1024;
		final double KB = BASE;
		final double MB = KB*BASE;
		final double GB = MB*BASE;
        final DecimalFormat decimalFormat = new DecimalFormat("#"); //#.## for 2 decimal places    	
    	
        if (bytes >= GB) {
        	
            return decimalFormat.format(bytes / GB) + " GB";
            
        }
        else if (bytes >= MB) {
        	
            return decimalFormat.format(bytes / MB) + " MB";
            
        }
        else if(bytes >= KB) {
        	
            return decimalFormat.format(bytes / KB) + " KB";
            
        } else {
        	return Long.toString(bytes) + " bytes";
        }
    }
}
