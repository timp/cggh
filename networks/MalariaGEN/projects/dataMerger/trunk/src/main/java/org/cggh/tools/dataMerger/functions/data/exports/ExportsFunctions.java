package org.cggh.tools.dataMerger.functions.data.exports;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.sql.rowset.CachedRowSet;

public class ExportsFunctions implements java.io.Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4359147852252115757L;


	public ExportsFunctions() {
		
		
	}	


	public String getExportsAsDecoratedXHTMLTableUsingExportsAsCachedRowSet(
			CachedRowSet exportsAsCachedRowSet) {
		
		StringBuffer exportsAsDecoratedXHTMLTable = null;
		
		try {
			if (exportsAsCachedRowSet.next()) {

				exportsAsDecoratedXHTMLTable = new StringBuffer();
				
				exportsAsDecoratedXHTMLTable.append("<table class=\"data-table\">");

				 exportsAsDecoratedXHTMLTable.append("<thead>");
				 exportsAsDecoratedXHTMLTable.append("<tr>");
				 
				 exportsAsDecoratedXHTMLTable.append("<th class=\"idHeadingContainer\">ID</th>");
				 exportsAsDecoratedXHTMLTable.append("<th>Merged File</th>");
				 exportsAsDecoratedXHTMLTable.append("<th>Source 1</th>");
				 exportsAsDecoratedXHTMLTable.append("<th>Source 2</th>");
				 
				 exportsAsDecoratedXHTMLTable.append("<th>Provenance</th>");
				 
				 exportsAsDecoratedXHTMLTable.append("<th>Created</th>");
				 exportsAsDecoratedXHTMLTable.append("<th><!-- Above Delete button --></th>");
				 exportsAsDecoratedXHTMLTable.append("<th><!-- Above Download link --></th>");
				 
				 exportsAsDecoratedXHTMLTable.append("</tr>");
				 exportsAsDecoratedXHTMLTable.append("</thead>");
				 
				 exportsAsDecoratedXHTMLTable.append("<tbody>");
				 
				//because next() skips the first row.
				 exportsAsCachedRowSet.beforeFirst();

					String rowStripeClassName = "even "; 
					String rowFirstClassName = "first ";
					String rowLastClassName = ""; 
					
					DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");  
				 
				while (exportsAsCachedRowSet.next()) {
					 
					if (rowStripeClassName == "odd ") {
						rowStripeClassName = "even ";
					} else {
						rowStripeClassName = "odd ";
					}
					
					//TODO: This might need changing when paging.
					if (exportsAsCachedRowSet.isLast()) {
						rowLastClassName = "last ";
					}
					
					
					exportsAsDecoratedXHTMLTable.append("<tr class=\"" + rowStripeClassName + rowFirstClassName + rowLastClassName + "\">");

					 exportsAsDecoratedXHTMLTable.append("<td class=\"idContainer\">" + exportsAsCachedRowSet.getString("id") + "</td>");

					 //FIXME: Apparently a bug in CachedRowSet using getX('columnAlias') aka columnLabel, which actually only works with getX('columnName'), so using getX('columnIndex').
					 
					 exportsAsDecoratedXHTMLTable.append("<td><a href=\"/dataMerger/files/" + exportsAsCachedRowSet.getInt("merged_file_id") + "\">" + exportsAsCachedRowSet.getString(3) + "</a></td>");
					 
					 

					 
					 //TODO: This URL shouldn't be hard-coded
					 exportsAsDecoratedXHTMLTable.append("<td><a href=\"/dataMerger/files/" + exportsAsCachedRowSet.getInt("source_file_1_id") + "\">" + exportsAsCachedRowSet.getString(1) + "</a></td>");
					 exportsAsDecoratedXHTMLTable.append("<td><a href=\"/dataMerger/files/" + exportsAsCachedRowSet.getInt("source_file_2_id") + "\">" + exportsAsCachedRowSet.getString(2) + "</a></td>");

					 
					 exportsAsDecoratedXHTMLTable.append("<td>");
					 exportsAsDecoratedXHTMLTable.append("<a href=\"/dataMerger/files/exports/" + exportsAsCachedRowSet.getInt("id") + "/settings\">Settings</a>, ");
					 exportsAsDecoratedXHTMLTable.append("<a href=\"/dataMerger/files/exports/" + exportsAsCachedRowSet.getInt("id") + "/joins\">Joins</a><br/>");
					 exportsAsDecoratedXHTMLTable.append("<a href=\"/dataMerger/files/exports/" + exportsAsCachedRowSet.getInt("id") + "/resolutions\">Resolutions</a>");
					 exportsAsDecoratedXHTMLTable.append("</td>");
					 					 
					 
					//TODO: format datetime 02 Jan 2011
					 exportsAsDecoratedXHTMLTable.append("<td>" + dateFormat.format(exportsAsCachedRowSet.getTimestamp("created_datetime")) + "</td>");
					 
					 exportsAsDecoratedXHTMLTable.append("<td><input type=\"hidden\" name=\"export_id\" value=\"" + exportsAsCachedRowSet.getInt("id") + "\"/><button class=\"deleteExportButton\">Delete</button><img class=\"deleting-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Deleting...\"/></td>");
					 
					 exportsAsDecoratedXHTMLTable.append("<td><a href=\"/dataMerger/files/" + exportsAsCachedRowSet.getInt("merged_file_id") + "\">Download</a></td>");
					 
					 
					 exportsAsDecoratedXHTMLTable.append("</tr>");
					 
					 rowFirstClassName = "";
				  }

				exportsAsDecoratedXHTMLTable.append("</tbody>");
				 
				exportsAsDecoratedXHTMLTable.append("</table>");
				
				exportsAsDecoratedXHTMLTable.append("<!-- <div>TODO: paging</div> -->");
				
			} else {
				
				exportsAsDecoratedXHTMLTable = new StringBuffer("<p>You have no exports.</p>");
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return exportsAsDecoratedXHTMLTable.toString();
	}

}
