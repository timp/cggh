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
					
					filesAsDecoratedXHTMLTable += "<tr class=\"" + rowStripeClassName + rowFirstClassName + rowLastClassName + "\">";

					filesAsDecoratedXHTMLTable += "<td><input type=\"checkbox\" name=\"file_id\" value=\"" + this.getFilesAsCachedRowSet().getString("id") + "\" /></td>";
					 
					 
					 filesAsDecoratedXHTMLTable += "<td class=\"idContainer\">" + this.getFilesAsCachedRowSet().getString("id") + "</td>";
					 filesAsDecoratedXHTMLTable += "<td><a href=\"/dataMerger/files/" + this.getFilesAsCachedRowSet().getString("id") + "\">" + this.getFilesAsCachedRowSet().getString("filename") + "</a></td>";
					 
					 if (this.getFilesAsCachedRowSet().getString("origin") != null) {
						 filesAsDecoratedXHTMLTable += "<td>" + this.getFilesAsCachedRowSet().getString("origin") + "</td>";
					 } else {
						 filesAsDecoratedXHTMLTable += "<td>---</td>";
					 }
					 
					 
					 filesAsDecoratedXHTMLTable += "<td>" + dateFormat.format(this.getFilesAsCachedRowSet().getTimestamp("created_datetime")) + "</td>";
					 filesAsDecoratedXHTMLTable += "<td class=\"fileSizeContainer\">" + convertBytesAsLongIntoMegaBytesAsString(this.getFilesAsCachedRowSet().getLong("file_size_in_bytes")) + "</td>";
					 
					 
					 if (this.getFilesAsCachedRowSet().getString("rows_count") != null) {
						 filesAsDecoratedXHTMLTable += "<td class=\"rowsCountContainer\">" + this.getFilesAsCachedRowSet().getString("rows_count") + "</td>";
					 } else {
						 filesAsDecoratedXHTMLTable += "<td class=\"rowsCountContainer\">---</td>";
					 }
					 
					 if (this.getFilesAsCachedRowSet().getString("columns_count") != null) {
						 filesAsDecoratedXHTMLTable += "<td class=\"columnsCountContainer\">" + this.getFilesAsCachedRowSet().getString("columns_count") + "</td>";
					 } else {
						 filesAsDecoratedXHTMLTable += "<td class=\"columnsCountContainer\">---</td>";
					 }
					 
					 filesAsDecoratedXHTMLTable += "<td><a href=\"/dataMerger/files/" + this.getFilesAsCachedRowSet().getString("id") + "\">Download</a></td>";
					 
					 filesAsDecoratedXHTMLTable += "</tr>";
					 
					 
					 rowFirstClassName = "";
				  }

				filesAsDecoratedXHTMLTable += "</tbody>";
				 
				filesAsDecoratedXHTMLTable += "</table>";
				
				filesAsDecoratedXHTMLTable += "<!-- <div>TODO: paging</div> -->";
				
				filesAsDecoratedXHTMLTable += "<p>";
				
				filesAsDecoratedXHTMLTable += "<button class=\"merge-files-button\">Merge</button><img class=\"merging-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Merging...\"/>";
				
				filesAsDecoratedXHTMLTable += "<button class=\"hide-files-button\">Hide</button><img class=\"hiding-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Hiding...\"/>";
				
				filesAsDecoratedXHTMLTable += "<button class=\"show-files-button\">Show</button><img class=\"showing-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Showing...\"/>";
				
				filesAsDecoratedXHTMLTable += "<button class=\"remove-files-button\">Remove</button><img class=\"removing-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Removing...\"/>";
				
				filesAsDecoratedXHTMLTable += "</p>";

			} else {
				
				filesAsDecoratedXHTMLTable = "<p>You have no files.</p>";
				
				//TODO: If there are any hidden.
				filesAsDecoratedXHTMLTable += "<p>";
				filesAsDecoratedXHTMLTable += "<button class=\"show-files-button\">Show hidden files</button><img class=\"showing-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Showing...\"/>";
				filesAsDecoratedXHTMLTable += "</p>";
				
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
