package org.cggh.tools.dataMerger.functions.uploads;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;



public class UploadsFunctionsModel implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5429836473276144235L;
	private CachedRowSet cachedRowSet = null;
	private String xhtmlTable = null;
	private String decoratedXHTMLTable = null;
	
	public UploadsFunctionsModel() {
		
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
					
					decoratedXHTMLTable = decoratedXHTMLTable.concat("<table>");

					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<thead>");
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<tr>");
					 
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th><!-- Column for checkboxes --></th>");
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th>ID <a href=\"javascript:TODOsort();\">sort up/down</a></th>");
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th>Filename <a href=\"javascript:TODOsort();\">sort up/down</a></th>");
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th>Uploaded <a href=\"javascript:TODOsort();\">sort up/down</a></th>");
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th><!-- Column for download links --></th>");
					 
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("</tr>");
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("</thead>");
					 
					//because next() skips the first row.
					 this.getCachedRowSet().beforeFirst();

					while (this.getCachedRowSet().next()) {
						 
						decoratedXHTMLTable = decoratedXHTMLTable.concat("<tr>");

						 decoratedXHTMLTable = decoratedXHTMLTable.concat("<td><input type=\"checkbox\" name=\"upload_id\" value=\"" + this.getCachedRowSet().getString("id") + "\" /></td>");
						 decoratedXHTMLTable = decoratedXHTMLTable.concat("<td>" + this.getCachedRowSet().getString("id") + "</td>");
						 decoratedXHTMLTable = decoratedXHTMLTable.concat("<td><a href=\"/dataMerger/files/uploads?id=" + this.getCachedRowSet().getString("id") + "\">" + this.getCachedRowSet().getString("original_filename") + "</a></td>");
						 //TODO: format datetime 02 Jan 2011
						 decoratedXHTMLTable = decoratedXHTMLTable.concat("<td>" + this.getCachedRowSet().getString("created_datetime") + "</td>");
						 decoratedXHTMLTable = decoratedXHTMLTable.concat("<td><a href=\"/dataMerger/files/uploads?id=" + this.getCachedRowSet().getString("id") + "\">Download</a></td>");
						 
						 decoratedXHTMLTable = decoratedXHTMLTable.concat("</tr>");
					  }

					decoratedXHTMLTable = decoratedXHTMLTable.concat("</tbody>");
					 
					decoratedXHTMLTable = decoratedXHTMLTable.concat("</table>");
					
					decoratedXHTMLTable = decoratedXHTMLTable.concat("<div>TODO: paging</div>");
					
					decoratedXHTMLTable = decoratedXHTMLTable.concat("<button class=\"merge-button\" onclick=\"createMergeUsingUploadIdsAsJSON();\">Merge</button>");
	
				} else {
					
					decoratedXHTMLTable = "You have no uploads.";
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		this.setDecoratedXHTMLTable(decoratedXHTMLTable);
	
	}	
	
}
