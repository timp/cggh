package org.cggh.tools.dataMerger.functions.data.files;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.sql.rowset.CachedRowSet;

public class FilesFunctions {

	private CachedRowSet filesAsCachedRowSet;
	private String filesAsDecoratedXHTMLTable;

	public void setFilesAsCachedRowSet(CachedRowSet filesAsCachedRowSet) {
		this.filesAsCachedRowSet = filesAsCachedRowSet;
	}

	public CachedRowSet getFilesAsCachedRowSet() {
		return filesAsCachedRowSet;
	}

	public void setFilesAsDecoratedXHTMLTableUsingFilesAsCachedRowSet() {
		String filesAsDecoratedXHTMLTable = null;

		try {
			if (this.getFilesAsCachedRowSet().next()) {

				filesAsDecoratedXHTMLTable = "";
				
				filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("<table class=\"data-table\">");

				 filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("<thead>");
				 filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("<tr>");
				 
				 filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("<th><!-- Column for checkboxes --></th>");
				 filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("<th class=\"idHeadingContainer\">ID<!--  <a href=\"javascript:TODOsort();\">sort up/down</a> --></th>");
				 filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("<th>Filename<!--  <a href=\"javascript:TODOsort();\">sort up/down</a> --></th>");
				 filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("<th>Created<!--  <a href=\"javascript:TODOsort();\">sort up/down</a> --></th>");
				 filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("<th class=\"fileSizeHeadingContainer\">Size<!--  <a href=\"javascript:TODOsort();\">sort up/down</a> --></th>");
				 filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("<th>Origin<!--  <a href=\"javascript:TODOsort();\">sort up/down</a> --></th>");
				 filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("<th><!-- Column for download links --></th>");
				 
				 filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("</tr>");
				 filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("</thead>");
				 
				 filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("<tbody>");
				 
				//because next() skips the first row.
				 this.getFilesAsCachedRowSet().beforeFirst();

				String rowStripeClassName = "even "; 
				String rowFirstClassName = "first ";
				String rowLastClassName = ""; 
				
				DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");  

				 
				while (this.getFilesAsCachedRowSet().next()) {
					
					if (rowStripeClassName == "odd ") {
						rowStripeClassName = "even ";
					} else {
						rowStripeClassName = "odd ";
					}
					
					//TODO: This might need changing when paging.
					if (this.getFilesAsCachedRowSet().isLast()) {
						rowLastClassName = "last ";
					}
					
					filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("<tr class=\"" + rowStripeClassName + rowFirstClassName + rowLastClassName + "\">");

					 if (this.getFilesAsCachedRowSet().getString("sourceTable").equals("upload")) {
						 filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("<td><input type=\"checkbox\" name=\"upload_id\" value=\"" + this.getFilesAsCachedRowSet().getString("id") + "\" /></td>");
					 }
					 else if (this.getFilesAsCachedRowSet().getString("sourceTable").equals("export")) {
						 filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("<td><input type=\"checkbox\" name=\"export_id\" value=\"" + this.getFilesAsCachedRowSet().getString("id") + "\" /></td>");
					 } else {
						 
						 filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("<td>???</td>");
					 }
					 
					 filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("<td class=\"idContainer\">" + this.getFilesAsCachedRowSet().getString("id") + "</td>");
					 filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("<td><a href=\"/dataMerger/files/uploads/" + this.getFilesAsCachedRowSet().getString("id") + "\">" + this.getFilesAsCachedRowSet().getString("filename") + "</a></td>");
					 filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("<td>" + dateFormat.format(this.getFilesAsCachedRowSet().getTimestamp("created_datetime")) + "</td>");
					 filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("<td class=\"fileSizeContainer\">" + convertBytesAsLongIntoMegaBytesAsString(this.getFilesAsCachedRowSet().getLong("file_size_in_bytes")) + "</td>");
					 filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("<td>" + this.getFilesAsCachedRowSet().getString("sourceTable") + "</td>");
					 filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("<td><a href=\"/dataMerger/files/uploads/" + this.getFilesAsCachedRowSet().getString("id") + "\">Download</a></td>");
					 
					 filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("</tr>");
					 
					 
					 rowFirstClassName = "";
				  }

				filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("</tbody>");
				 
				filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("</table>");
				
				filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("<!-- <div>TODO: paging</div> -->");
				
				filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("<p>");
				
				filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("<button class=\"merge-files-button\">Merge</button><img class=\"merging-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Merging...\"/>");
				
				filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("<button class=\"hide-files-button\">Hide</button><img class=\"hiding-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Hiding...\"/>");
				
				filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("<button class=\"show-files-button\">Show</button><img class=\"showing-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Showing...\"/>");
				
				filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("<button class=\"remove-files-button\">Remove</button><img class=\"removing-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Removing...\"/>");
				
				filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable.concat("</p>");

			} else {
				
				filesAsDecoratedXHTMLTable = "<p>You have no active files.</p>";
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		this.setFilesAsDecoratedXHTMLTable(filesAsDecoratedXHTMLTable);
	}

	public void setFilesAsDecoratedXHTMLTable(String filesAsDecoratedXHTMLTable) {
		this.filesAsDecoratedXHTMLTable = filesAsDecoratedXHTMLTable;
	}

	public String getFilesAsDecoratedXHTMLTable() {
		return filesAsDecoratedXHTMLTable;
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
