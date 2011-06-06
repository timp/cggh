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
				
				exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<table class=\"exports-table\">");

				 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<thead>");
				 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<tr>");
				 
				 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<th>ID<!--  <a href=\"javascript:TODOsort();\">sort up/down</a> --></th>");
				 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<th>File 1<!--  <a href=\"javascript:TODOsort();\">sort up/down</a> --></th>");
				 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<th>File 2<!--  <a href=\"javascript:TODOsort();\">sort up/down</a> --></th>");
				 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<th><!-- --></th>");
				 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<th><!-- --></th>");
				 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<th>Created<!--  <a href=\"javascript:TODOsort();\">sort up/down</a> --></th>");
				 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<th><!-- --></th>");
				 
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

					 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<td>" + this.getExportsAsCachedRowSet().getString("id") + "</td>");
					 
					 //FIXME: Apparently a bug in CachedRowSet using getX('columnAlias') aka columnLabel, which actually only works with getX('columnName'), so using getX('columnIndex').
					 
					 //TODO: This URL shouldn't be hard-coded
					 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<td><a href=\"/dataMerger/files/uploads/" + this.getExportsAsCachedRowSet().getInt(2) + "\">" + this.getExportsAsCachedRowSet().getString(3) + "</a></td>");
					 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<td><a href=\"/dataMerger/files/uploads/" + this.getExportsAsCachedRowSet().getInt(4) + "\">" + this.getExportsAsCachedRowSet().getString(5) + "</a></td>");

					 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<td><a href=\"/dataMerger/files/exports/" + this.getExportsAsCachedRowSet().getInt("id") + "/joins\">Join</a></td>");
					 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<td><a href=\"/dataMerger/files/exports/" + this.getExportsAsCachedRowSet().getInt("id") + "/resolutions\">Resolutions</a></td>");
					 					 
					 
					//TODO: format datetime 02 Jan 2011
					 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<td>" + dateFormat.format(this.getExportsAsCachedRowSet().getTimestamp("created_datetime")) + "</td>");
					 
					 
					 
					 exportsAsDecoratedXHTMLTable = exportsAsDecoratedXHTMLTable.concat("<td><a href=\"/dataMerger/files/exports/" + this.getExportsAsCachedRowSet().getInt("id") + "/merged\">Download</a></td>");
					 
					 
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
