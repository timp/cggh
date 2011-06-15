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
	private String exportsAsDecoratedXHTMLTable;
	private CachedRowSet exportsAsCachedRowSet;

	public ExportsFunctions() {
		
		
	}	
	
	public void setExportsAsCachedRowSet(
			CachedRowSet exportsAsCachedRowSet) {
		this.exportsAsCachedRowSet = exportsAsCachedRowSet;
	}
	public CachedRowSet getExportsAsCachedRowSet() {
		return this.exportsAsCachedRowSet;
	}

	public void setExportsAsDecoratedXHTMLTableUsingExportsAsCachedRowSet() {
		
		String exportsAsDecoratedXHTMLTable = null;
		
		try {
			if (this.getExportsAsCachedRowSet().next()) {

				exportsAsDecoratedXHTMLTable = "";
				
				exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<table class=\"data-table\">");

				 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<thead>");
				 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<tr>");
				 
				 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<th class=\"idHeadingContainer\">ID</th>");
				 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<th>Merged File</th>");
				 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<th>Source 1</th>");
				 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<th>Source 2</th>");
				 
				 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<th>Provenance</th>");
				 
				 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<th>Created</th>");
				 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<th><!-- Above Delete button --></th>");
				 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<th><!-- Above Download link --></th>");
				 
				 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("</tr>");
				 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("</thead>");
				 
				 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<tbody>");
				 
				//because next() skips the first row.
				 this.getExportsAsCachedRowSet().beforeFirst();

					String rowStripeClassName = "even "; 
					String rowFirstClassName = "first ";
					String rowLastClassName = ""; 
					
					DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");  
				 
				while (this.getExportsAsCachedRowSet().next()) {
					 
					if (rowStripeClassName == "odd ") {
						rowStripeClassName = "even ";
					} else {
						rowStripeClassName = "odd ";
					}
					
					//TODO: This might need changing when paging.
					if (this.getExportsAsCachedRowSet().isLast()) {
						rowLastClassName = "last ";
					}
					
					
					exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<tr class=\"" + rowStripeClassName + rowFirstClassName + rowLastClassName + "\">");

					 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<td class=\"idContainer\">" + this.getExportsAsCachedRowSet().getString("id") + "</td>");

					 //FIXME: Apparently a bug in CachedRowSet using getX('columnAlias') aka columnLabel, which actually only works with getX('columnName'), so using getX('columnIndex').
					 
					 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<td><a href=\"/dataMerger/files/" + this.getExportsAsCachedRowSet().getInt("merged_file_id") + "\">" + this.getExportsAsCachedRowSet().getString(3) + "</a></td>");
					 
					 

					 
					 //TODO: This URL shouldn't be hard-coded
					 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<td><a href=\"/dataMerger/files/" + this.getExportsAsCachedRowSet().getInt("source_file_1_id") + "\">" + this.getExportsAsCachedRowSet().getString(1) + "</a></td>");
					 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<td><a href=\"/dataMerger/files/" + this.getExportsAsCachedRowSet().getInt("source_file_2_id") + "\">" + this.getExportsAsCachedRowSet().getString(2) + "</a></td>");

					 
					 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<td>");
					 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<a href=\"/dataMerger/files/exports/" + this.getExportsAsCachedRowSet().getInt("id") + "/settings\">Settings</a>, ");
					 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<a href=\"/dataMerger/files/exports/" + this.getExportsAsCachedRowSet().getInt("id") + "/joins\">Joins</a><br/>");
					 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<a href=\"/dataMerger/files/exports/" + this.getExportsAsCachedRowSet().getInt("id") + "/resolutions\">Resolutions</a>");
					 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("</td>");
					 					 
					 
					//TODO: format datetime 02 Jan 2011
					 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<td>" + dateFormat.format(this.getExportsAsCachedRowSet().getTimestamp("created_datetime")) + "</td>");
					 
					 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<td><input type=\"hidden\" name=\"export_id\" value=\"" + this.getExportsAsCachedRowSet().getInt("id") + "\"/><button class=\"deleteExportButton\">Delete</button><img class=\"deleting-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Deleting...\"/></td>");
					 
					 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<td><a href=\"/dataMerger/files/" + this.getExportsAsCachedRowSet().getInt("merged_file_id") + "\">Download</a></td>");
					 
					 
					 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("</tr>");
					 
					 rowFirstClassName = "";
				  }

				exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("</tbody>");
				 
				exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("</table>");
				
				exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<!-- <div>TODO: paging</div> -->");
				
			} else {
				
				exportsAsDecoratedXHTMLTable = "<p>You have no exports.</p>";
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable;
		
	}

	public String getExportsAsDecoratedXHTMLTable() {
		return this.exportsAsDecoratedXHTMLTable;
	}

	public void setExportsAsDecoratedXHTMLTable(
			String exportsAsDecoratedXHTMLTable) {
		this.exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable;
	}

}
