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
		
		String filesAsDecoratedXHTMLTable = null;

		try {
			if (filesAsCachedRowSet.next()) {

				filesAsDecoratedXHTMLTable = "";
				
				filesAsDecoratedXHTMLTable += "<table class=\"data-table\">";

				 filesAsDecoratedXHTMLTable += "<thead>";
				 filesAsDecoratedXHTMLTable += "<tr>";
				 
				 filesAsDecoratedXHTMLTable += "<th><!-- Column for checkboxes --></th>";
				 filesAsDecoratedXHTMLTable += "<th class=\"idHeadingContainer\">ID</th>";
				 filesAsDecoratedXHTMLTable += "<th>Filename</th>";
				 filesAsDecoratedXHTMLTable += "<th>Origin</th>";
				 filesAsDecoratedXHTMLTable += "<th>Created</th>";
				 filesAsDecoratedXHTMLTable += "<th class=\"fileSizeHeadingContainer\">Size</th>";
				 
				 filesAsDecoratedXHTMLTable += "<th>Rows</th>";
				 filesAsDecoratedXHTMLTable += "<th>Cols</th>";
				 filesAsDecoratedXHTMLTable += "<th><!-- Column for download links --></th>";
				 
				 filesAsDecoratedXHTMLTable += "</tr>";
				 filesAsDecoratedXHTMLTable += "</thead>";
				 
				 filesAsDecoratedXHTMLTable += "<tbody>";
				 
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
					
					filesAsDecoratedXHTMLTable += "<tr class=\"" + rowStripeClassName + rowFirstClassName + rowLastClassName + "\">";

					filesAsDecoratedXHTMLTable += "<td><input type=\"checkbox\" name=\"file_id\" value=\"" + filesAsCachedRowSet.getString("id") + "\" /></td>";
					 
					 
					 filesAsDecoratedXHTMLTable += "<td class=\"idContainer\">" + filesAsCachedRowSet.getString("id") + "</td>";
					 filesAsDecoratedXHTMLTable += "<td><a href=\"/dataMerger/files/" + filesAsCachedRowSet.getString("id") + "\">" + filesAsCachedRowSet.getString("filename") + "</a></td>";
					 
					 if (filesAsCachedRowSet.getString("origin") != null) {
						 filesAsDecoratedXHTMLTable += "<td>" + filesAsCachedRowSet.getString("origin") + "</td>";
					 } else {
						 filesAsDecoratedXHTMLTable += "<td>---</td>";
					 }
					 
					 
					 filesAsDecoratedXHTMLTable += "<td>" + dateFormat.format(filesAsCachedRowSet.getTimestamp("created_datetime")) + "</td>";
					 filesAsDecoratedXHTMLTable += "<td class=\"fileSizeContainer\">" + convertBytesAsLongIntoMegaBytesAsString(filesAsCachedRowSet.getLong("file_size_in_bytes")) + "</td>";
					 
					 
					 if (filesAsCachedRowSet.getString("rows_count") != null) {
						 filesAsDecoratedXHTMLTable += "<td class=\"rowsCountContainer\">" + filesAsCachedRowSet.getString("rows_count") + "</td>";
					 } else {
						 filesAsDecoratedXHTMLTable += "<td class=\"rowsCountContainer\">---</td>";
					 }
					 
					 if (filesAsCachedRowSet.getString("columns_count") != null) {
						 filesAsDecoratedXHTMLTable += "<td class=\"columnsCountContainer\">" + filesAsCachedRowSet.getString("columns_count") + "</td>";
					 } else {
						 filesAsDecoratedXHTMLTable += "<td class=\"columnsCountContainer\">---</td>";
					 }
					 
					 filesAsDecoratedXHTMLTable += "<td><a href=\"/dataMerger/files/" + filesAsCachedRowSet.getString("id") + "\">Download</a></td>";
					 
					 filesAsDecoratedXHTMLTable += "</tr>";
					 
					 
					 rowFirstClassName = "";
				  }

				filesAsDecoratedXHTMLTable += "</tbody>";
				 
				filesAsDecoratedXHTMLTable += "</table>";
				
				filesAsDecoratedXHTMLTable += "<!-- <div>TODO: paging</div> -->";
				
				filesAsDecoratedXHTMLTable += "<p>";
				
				filesAsDecoratedXHTMLTable += "<button class=\"merge-files-button\">Merge</button><img class=\"merging-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Merging...\"/>";
				
				filesAsDecoratedXHTMLTable += "<button class=\"hide-files-button\">Hide</button><img class=\"hiding-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Hiding...\"/>";
				
				filesAsDecoratedXHTMLTable += "<button class=\"show-hidden-files-button\">Show Hidden</button><img class=\"showing-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Showing...\"/>";
				
				filesAsDecoratedXHTMLTable += "<button class=\"remove-files-button\">Remove</button><img class=\"removing-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Removing...\"/>";
				
				filesAsDecoratedXHTMLTable += "</p>";

			} else {
				
				filesAsDecoratedXHTMLTable = "<p>You have no files.</p>";
				
				//TODO: If there are any hidden.
				filesAsDecoratedXHTMLTable += "<p>";
				filesAsDecoratedXHTMLTable += "<button class=\"show-hidden-files-button\">Show Hidden</button><img class=\"showing-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Showing...\"/>";
				filesAsDecoratedXHTMLTable += "</p>";
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return filesAsDecoratedXHTMLTable;
	}

	public String getHiddenFilesAsDecoratedXHTMLTableUsingHiddenFilesAsCachedRowSet(
			CachedRowSet hiddenFilesAsCachedRowSet) {
		String hiddenFilesAsDecoratedXHTMLTable = null;

		try {
			if (hiddenFilesAsCachedRowSet.next()) {

				hiddenFilesAsDecoratedXHTMLTable = "";
				
				hiddenFilesAsDecoratedXHTMLTable += "<table class=\"data-table\">";

				 hiddenFilesAsDecoratedXHTMLTable += "<thead>";
				 hiddenFilesAsDecoratedXHTMLTable += "<tr>";
				 
				 hiddenFilesAsDecoratedXHTMLTable += "<th><!-- Column for checkboxes --></th>";
				 hiddenFilesAsDecoratedXHTMLTable += "<th class=\"idHeadingContainer\">ID</th>";
				 hiddenFilesAsDecoratedXHTMLTable += "<th>Filename</th>";
				 hiddenFilesAsDecoratedXHTMLTable += "<th>Origin</th>";
				 hiddenFilesAsDecoratedXHTMLTable += "<th>Created</th>";
				 hiddenFilesAsDecoratedXHTMLTable += "<th class=\"fileSizeHeadingContainer\">Size</th>";
				 
				 hiddenFilesAsDecoratedXHTMLTable += "<th>Rows</th>";
				 hiddenFilesAsDecoratedXHTMLTable += "<th>Cols</th>";
				 hiddenFilesAsDecoratedXHTMLTable += "<th>Hidden</th>";
				 hiddenFilesAsDecoratedXHTMLTable += "<th><!-- Column for download links --></th>";
				 
				 hiddenFilesAsDecoratedXHTMLTable += "</tr>";
				 hiddenFilesAsDecoratedXHTMLTable += "</thead>";
				 
				 hiddenFilesAsDecoratedXHTMLTable += "<tbody>";
				 
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
					
					hiddenFilesAsDecoratedXHTMLTable += "<tr class=\"" + rowStripeClassName + rowFirstClassName + rowLastClassName + "\">";

					hiddenFilesAsDecoratedXHTMLTable += "<td><input type=\"checkbox\" name=\"file_id\" value=\"" + hiddenFilesAsCachedRowSet.getString("id") + "\" /></td>";
					 
					 
					 hiddenFilesAsDecoratedXHTMLTable += "<td class=\"idContainer\">" + hiddenFilesAsCachedRowSet.getString("id") + "</td>";
					 hiddenFilesAsDecoratedXHTMLTable += "<td><a href=\"/dataMerger/files/" + hiddenFilesAsCachedRowSet.getString("id") + "\">" + hiddenFilesAsCachedRowSet.getString("filename") + "</a></td>";
					 
					 if (hiddenFilesAsCachedRowSet.getString("origin") != null) {
						 hiddenFilesAsDecoratedXHTMLTable += "<td>" + hiddenFilesAsCachedRowSet.getString("origin") + "</td>";
					 } else {
						 hiddenFilesAsDecoratedXHTMLTable += "<td>---</td>";
					 }
					 
					 
					 hiddenFilesAsDecoratedXHTMLTable += "<td>" + dateFormat.format(hiddenFilesAsCachedRowSet.getTimestamp("created_datetime")) + "</td>";
					 hiddenFilesAsDecoratedXHTMLTable += "<td class=\"fileSizeContainer\">" + convertBytesAsLongIntoMegaBytesAsString(hiddenFilesAsCachedRowSet.getLong("file_size_in_bytes")) + "</td>";
					 
					 
					 if (hiddenFilesAsCachedRowSet.getString("rows_count") != null) {
						 hiddenFilesAsDecoratedXHTMLTable += "<td class=\"rowsCountContainer\">" + hiddenFilesAsCachedRowSet.getString("rows_count") + "</td>";
					 } else {
						 hiddenFilesAsDecoratedXHTMLTable += "<td class=\"rowsCountContainer\">---</td>";
					 }
					 
					 if (hiddenFilesAsCachedRowSet.getString("columns_count") != null) {
						 hiddenFilesAsDecoratedXHTMLTable += "<td class=\"columnsCountContainer\">" + hiddenFilesAsCachedRowSet.getString("columns_count") + "</td>";
					 } else {
						 hiddenFilesAsDecoratedXHTMLTable += "<td class=\"columnsCountContainer\">---</td>";
					 }
					 
					 Boolean hidden = hiddenFilesAsCachedRowSet.getBoolean("hidden");
					 
					 if (hidden != null && hidden) {
						 hiddenFilesAsDecoratedXHTMLTable += "<td class=\"columnsCountContainer\">Yes</td>";
					 } else {
						 hiddenFilesAsDecoratedXHTMLTable += "<td class=\"columnsCountContainer\">No</td>";
					 }
					 
					 hiddenFilesAsDecoratedXHTMLTable += "<td><a href=\"/dataMerger/files/" + hiddenFilesAsCachedRowSet.getString("id") + "\">Download</a></td>";
					 
					 hiddenFilesAsDecoratedXHTMLTable += "</tr>";
					 
					 
					 rowFirstClassName = "";
				  }

				hiddenFilesAsDecoratedXHTMLTable += "</tbody>";
				 
				hiddenFilesAsDecoratedXHTMLTable += "</table>";
				
				hiddenFilesAsDecoratedXHTMLTable += "<!-- <div>TODO: paging</div> -->";
				
				hiddenFilesAsDecoratedXHTMLTable += "<p>";
				
				hiddenFilesAsDecoratedXHTMLTable += "<button class=\"merge-files-button\">Merge</button><img class=\"merging-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Merging...\"/>";
				
				hiddenFilesAsDecoratedXHTMLTable += "<button class=\"show-unhidden-files-button\">Show Unhidden</button><img class=\"showing-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Showing...\"/>";
				
				hiddenFilesAsDecoratedXHTMLTable += "<button class=\"unhide-files-button\">Unhide</button><img class=\"unhiding-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Unhiding...\"/>";
				
				hiddenFilesAsDecoratedXHTMLTable += "<button class=\"remove-files-button\">Remove</button><img class=\"removing-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Removing...\"/>";
				
				hiddenFilesAsDecoratedXHTMLTable += "</p>";

			} else {
				
				hiddenFilesAsDecoratedXHTMLTable = "<p>You have no hidden files.</p>";
				
				//TODO: If there are any hidden.
				hiddenFilesAsDecoratedXHTMLTable += "<p>";
				hiddenFilesAsDecoratedXHTMLTable += "<button class=\"show-unhidden-files-button\">Show Unhidden</button><img class=\"showing-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Showing...\"/>";
				hiddenFilesAsDecoratedXHTMLTable += "</p>";
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return hiddenFilesAsDecoratedXHTMLTable;
	}	
	
}
