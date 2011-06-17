package org.cggh.tools.dataMerger.functions.data.files;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.sql.rowset.CachedRowSet;

public class FilesFunctions {



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

	public String getFilesAsDecoratedXHTMLTableUsingFilesAsCachedRowSet(
			CachedRowSet filesAsCachedRowSet) {
		
		StringBuffer filesAsDecoratedXHTMLTable = null;

		try {
			if (filesAsCachedRowSet.next()) {

				filesAsDecoratedXHTMLTable = new StringBuffer();
				
				filesAsDecoratedXHTMLTable.append("<table class=\"data-table\">");

				 filesAsDecoratedXHTMLTable.append("<thead>");
				 filesAsDecoratedXHTMLTable.append("<tr>");
				 
				 filesAsDecoratedXHTMLTable.append("<th><!-- Column for checkboxes --></th>");
				 filesAsDecoratedXHTMLTable.append("<th class=\"idHeadingContainer\"><a class=\"sortByIdLink\">ID</a></th>");
				 filesAsDecoratedXHTMLTable.append("<th><a class=\"sortByFilenameLink\">Filename</a></th>");
				 filesAsDecoratedXHTMLTable.append("<th><a class=\"sortByFileOriginLink\">Origin</a></th>");
				 filesAsDecoratedXHTMLTable.append("<th><a class=\"sortByCreatedDateLink\">Created</a></th>");
				 filesAsDecoratedXHTMLTable.append("<th class=\"fileSizeHeadingContainer\"><a class=\"sortByFileSizeLink\">Size</a></th>");
				 
				 filesAsDecoratedXHTMLTable.append("<th><a class=\"sortByRowsCountLink\">Rows</a></th>");
				 filesAsDecoratedXHTMLTable.append("<th><a class=\"sortByColumnsCountLink\">Cols</a></th>");
				 filesAsDecoratedXHTMLTable.append("<th><!-- Column for download links --></th>");
				 
				 filesAsDecoratedXHTMLTable.append("</tr>");
				 filesAsDecoratedXHTMLTable.append("</thead>");
				 
				 filesAsDecoratedXHTMLTable.append("<tbody>");
				 
				//because next() skips the first row.
				 filesAsCachedRowSet.beforeFirst();

				String rowStripeClassName = "even "; 
				String rowFirstClassName = "first ";
				String rowLastClassName = ""; 
				
				DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");  

				 
				while (filesAsCachedRowSet.next()) {
					
					if (rowStripeClassName == "odd ") {
						rowStripeClassName = "even ";
					} else {
						rowStripeClassName = "odd ";
					}
					
					//TODO: This might need changing when paging.
					if (filesAsCachedRowSet.isLast()) {
						rowLastClassName = "last ";
					}
					
					filesAsDecoratedXHTMLTable.append("<tr class=\"").append(rowStripeClassName).append(rowFirstClassName).append(rowLastClassName).append("\">");

					filesAsDecoratedXHTMLTable.append("<td><input type=\"checkbox\" name=\"file_id\" value=\"").append(filesAsCachedRowSet.getString("id")).append("\" /></td>");
					 
					 
					 filesAsDecoratedXHTMLTable.append("<td class=\"idContainer\">").append(filesAsCachedRowSet.getString("id")).append("</td>");
					 filesAsDecoratedXHTMLTable.append("<td><a href=\"/dataMerger/files/").append(filesAsCachedRowSet.getString("id")).append("\">").append(filesAsCachedRowSet.getString("filename")).append("</a></td>");
					 
					 if (filesAsCachedRowSet.getString("origin") != null) {
						 filesAsDecoratedXHTMLTable.append("<td>").append(filesAsCachedRowSet.getString("origin")).append("</td>");
					 } else {
						 filesAsDecoratedXHTMLTable.append("<td>---</td>");
					 }
					 
					 
					 filesAsDecoratedXHTMLTable.append("<td>").append(dateFormat.format(filesAsCachedRowSet.getTimestamp("created_datetime"))).append("</td>");
					 filesAsDecoratedXHTMLTable.append("<td class=\"fileSizeContainer\">").append(convertBytesAsLongIntoMegaBytesAsString(filesAsCachedRowSet.getLong("file_size_in_bytes"))).append("</td>");
					 
					 
					 if (filesAsCachedRowSet.getString("rows_count") != null) {
						 filesAsDecoratedXHTMLTable.append("<td class=\"rowsCountContainer\">").append(filesAsCachedRowSet.getString("rows_count")).append("</td>");
					 } else {
						 filesAsDecoratedXHTMLTable.append("<td class=\"rowsCountContainer\">---</td>");
					 }
					 
					 if (filesAsCachedRowSet.getString("columns_count") != null) {
						 filesAsDecoratedXHTMLTable.append("<td class=\"columnsCountContainer\">").append(filesAsCachedRowSet.getString("columns_count")).append("</td>");
					 } else {
						 filesAsDecoratedXHTMLTable.append("<td class=\"columnsCountContainer\">---</td>");
					 }
					 
					 filesAsDecoratedXHTMLTable.append("<td><a href=\"/dataMerger/files/").append(filesAsCachedRowSet.getString("id")).append("\">Download</a></td>");
					 
					 filesAsDecoratedXHTMLTable.append("</tr>");
					 
					 
					 rowFirstClassName = "";
				  }

				filesAsDecoratedXHTMLTable.append("</tbody>");
				 
				filesAsDecoratedXHTMLTable.append("</table>");
				
				filesAsDecoratedXHTMLTable.append("<p>");
				
				filesAsDecoratedXHTMLTable.append("<button class=\"merge-files-button\">Merge</button><img class=\"merging-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Merging...\"/>");
				
				filesAsDecoratedXHTMLTable.append("<button class=\"hide-files-button\">Hide</button><img class=\"hiding-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Hiding...\"/>");
				
				filesAsDecoratedXHTMLTable.append("<button class=\"show-hidden-files-button\">Show hidden</button><img class=\"showing-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Showing...\"/>");
				
				filesAsDecoratedXHTMLTable.append("<button class=\"remove-files-button\">Delete</button><img class=\"removing-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Removing...\"/>");
				
				filesAsDecoratedXHTMLTable.append("</p>");

			} else {
				
				filesAsDecoratedXHTMLTable = new StringBuffer("<p>You have no files.</p>");
				
				//TODO: If there are any hidden.
				filesAsDecoratedXHTMLTable.append("<p>");
				filesAsDecoratedXHTMLTable.append("<button class=\"show-hidden-files-button\">Show hidden</button><img class=\"showing-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Showing...\"/>");
				filesAsDecoratedXHTMLTable.append("</p>");
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return filesAsDecoratedXHTMLTable.toString();
	}

	public String getHiddenFilesAsDecoratedXHTMLTableUsingHiddenFilesAsCachedRowSet(
			CachedRowSet hiddenFilesAsCachedRowSet) {
		
		StringBuffer hiddenFilesAsDecoratedXHTMLTable = null;

		try {
			if (hiddenFilesAsCachedRowSet.next()) {

				hiddenFilesAsDecoratedXHTMLTable = new StringBuffer();
				
				hiddenFilesAsDecoratedXHTMLTable.append("<table class=\"data-table\">");

				 hiddenFilesAsDecoratedXHTMLTable.append("<thead>");
				 hiddenFilesAsDecoratedXHTMLTable.append("<tr>");
				 
				 hiddenFilesAsDecoratedXHTMLTable.append("<th><!-- Column for checkboxes --></th>");
				 hiddenFilesAsDecoratedXHTMLTable.append("<th class=\"idHeadingContainer\">ID</th>");
				 hiddenFilesAsDecoratedXHTMLTable.append("<th>Filename</th>");
				 hiddenFilesAsDecoratedXHTMLTable.append("<th>Origin</th>");
				 hiddenFilesAsDecoratedXHTMLTable.append("<th>Created</th>");
				 hiddenFilesAsDecoratedXHTMLTable.append("<th class=\"fileSizeHeadingContainer\">Size</th>");
				 
				 hiddenFilesAsDecoratedXHTMLTable.append("<th>Rows</th>");
				 hiddenFilesAsDecoratedXHTMLTable.append("<th>Cols</th>");
				 hiddenFilesAsDecoratedXHTMLTable.append("<th>Hidden</th>");
				 hiddenFilesAsDecoratedXHTMLTable.append("<th><!-- Column for download links --></th>");
				 
				 hiddenFilesAsDecoratedXHTMLTable.append("</tr>");
				 hiddenFilesAsDecoratedXHTMLTable.append("</thead>");
				 
				 hiddenFilesAsDecoratedXHTMLTable.append("<tbody>");
				 
				//because next() skips the first row.
				 hiddenFilesAsCachedRowSet.beforeFirst();

				String rowStripeClassName = "even "; 
				String rowFirstClassName = "first ";
				String rowLastClassName = ""; 
				
				DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");  

				 
				while (hiddenFilesAsCachedRowSet.next()) {
					
					if (rowStripeClassName == "odd ") {
						rowStripeClassName = "even ";
					} else {
						rowStripeClassName = "odd ";
					}
					
					//TODO: This might need changing when paging.
					if (hiddenFilesAsCachedRowSet.isLast()) {
						rowLastClassName = "last ";
					}
					
					hiddenFilesAsDecoratedXHTMLTable.append("<tr class=\"").append(rowStripeClassName).append(rowFirstClassName).append(rowLastClassName).append("\">");

					hiddenFilesAsDecoratedXHTMLTable.append("<td><input type=\"checkbox\" name=\"file_id\" value=\"").append(hiddenFilesAsCachedRowSet.getString("id")).append("\" /></td>");
					 
					 
					 hiddenFilesAsDecoratedXHTMLTable.append("<td class=\"idContainer\">").append(hiddenFilesAsCachedRowSet.getString("id")).append("</td>");
					 hiddenFilesAsDecoratedXHTMLTable.append("<td><a href=\"/dataMerger/files/").append(hiddenFilesAsCachedRowSet.getString("id")).append("\">").append(hiddenFilesAsCachedRowSet.getString("filename")).append("</a></td>");
					 
					 if (hiddenFilesAsCachedRowSet.getString("origin") != null) {
						 hiddenFilesAsDecoratedXHTMLTable.append("<td>").append(hiddenFilesAsCachedRowSet.getString("origin")).append("</td>");
					 } else {
						 hiddenFilesAsDecoratedXHTMLTable.append("<td>---</td>");
					 }
					 
					 
					 hiddenFilesAsDecoratedXHTMLTable.append("<td>").append(dateFormat.format(hiddenFilesAsCachedRowSet.getTimestamp("created_datetime"))).append("</td>");
					 hiddenFilesAsDecoratedXHTMLTable.append("<td class=\"fileSizeContainer\">").append(convertBytesAsLongIntoMegaBytesAsString(hiddenFilesAsCachedRowSet.getLong("file_size_in_bytes"))).append("</td>");
					 
					 
					 if (hiddenFilesAsCachedRowSet.getString("rows_count") != null) {
						 hiddenFilesAsDecoratedXHTMLTable.append("<td class=\"rowsCountContainer\">").append(hiddenFilesAsCachedRowSet.getString("rows_count")).append("</td>");
					 } else {
						 hiddenFilesAsDecoratedXHTMLTable.append("<td class=\"rowsCountContainer\">---</td>");
					 }
					 
					 if (hiddenFilesAsCachedRowSet.getString("columns_count") != null) {
						 hiddenFilesAsDecoratedXHTMLTable.append("<td class=\"columnsCountContainer\">").append(hiddenFilesAsCachedRowSet.getString("columns_count")).append("</td>");
					 } else {
						 hiddenFilesAsDecoratedXHTMLTable.append("<td class=\"columnsCountContainer\">---</td>");
					 }
					 
					 Boolean hidden = hiddenFilesAsCachedRowSet.getBoolean("hidden");
					 
					 if (hidden != null && hidden) {
						 hiddenFilesAsDecoratedXHTMLTable.append("<td class=\"columnsCountContainer\">Yes</td>");
					 } else {
						 hiddenFilesAsDecoratedXHTMLTable.append("<td class=\"columnsCountContainer\">No</td>");
					 }
					 
					 hiddenFilesAsDecoratedXHTMLTable.append("<td><a href=\"/dataMerger/files/").append(hiddenFilesAsCachedRowSet.getString("id")).append("\">Download</a></td>");
					 
					 hiddenFilesAsDecoratedXHTMLTable.append("</tr>");
					 
					 
					 rowFirstClassName = "";
				  }

				hiddenFilesAsDecoratedXHTMLTable.append("</tbody>");
				 
				hiddenFilesAsDecoratedXHTMLTable.append("</table>");
				
				hiddenFilesAsDecoratedXHTMLTable.append("<!-- <div>TODO: paging</div> -->");
				
				hiddenFilesAsDecoratedXHTMLTable.append("<p>");
				
				hiddenFilesAsDecoratedXHTMLTable.append("<button class=\"merge-files-button\">Merge</button><img class=\"merging-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Merging...\"/>");
				
				hiddenFilesAsDecoratedXHTMLTable.append("<button class=\"show-unhidden-files-button\">Show unhidden</button><img class=\"showing-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Showing...\"/>");
				
				hiddenFilesAsDecoratedXHTMLTable.append("<button class=\"unhide-files-button\">Unhide</button><img class=\"unhiding-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Unhiding...\"/>");
				
				hiddenFilesAsDecoratedXHTMLTable.append("<button class=\"remove-files-button\">Delete</button><img class=\"removing-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Removing...\"/>");
				
				hiddenFilesAsDecoratedXHTMLTable.append("</p>");

			} else {
				
				hiddenFilesAsDecoratedXHTMLTable = new StringBuffer("<p>You have no hidden files.</p>");
				
				//TODO: If there are any hidden.
				hiddenFilesAsDecoratedXHTMLTable.append("<p>");
				hiddenFilesAsDecoratedXHTMLTable.append("<button class=\"show-unhidden-files-button\">Show unhidden</button><img class=\"showing-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Showing...\"/>");
				hiddenFilesAsDecoratedXHTMLTable.append("</p>");
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return hiddenFilesAsDecoratedXHTMLTable.toString();
	}	
	
}
