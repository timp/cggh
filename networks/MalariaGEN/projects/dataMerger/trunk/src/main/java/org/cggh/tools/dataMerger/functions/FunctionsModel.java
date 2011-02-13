package org.cggh.tools.dataMerger.functions;

import javax.sql.rowset.CachedRowSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;



public class FunctionsModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7087898366227739676L;
	private CachedRowSet cachedRowSet = null;
	private String xhtmlTable = null;
	private String decoratedXHTMLTable = null;
	
	public FunctionsModel() {
		
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
	
	public void transformCachedRowSetIntoXHTMLTable () {
		
		CachedRowSet cachedRowSet = this.getCachedRowSet();
		
		String xhtmlTable = null;

			try {
				if (cachedRowSet.next()) {

					xhtmlTable = "";
					
					xhtmlTable = xhtmlTable.concat("<table>");
					
					ResultSetMetaData resultSetMetaData = cachedRowSet.getMetaData(); 
					 int columnCount = resultSetMetaData.getColumnCount();
					 
					 xhtmlTable = xhtmlTable.concat("<thead>");
					 xhtmlTable = xhtmlTable.concat("<tr>");
					 for (int i = 1; i <= columnCount; i++) {
						 xhtmlTable = xhtmlTable.concat("<th>" + resultSetMetaData.getColumnLabel(i) + "</th>");
					   }
					 xhtmlTable = xhtmlTable.concat("</tr>");
					 xhtmlTable = xhtmlTable.concat("</thead>");
					 


					while (cachedRowSet.next()) {
						 
						xhtmlTable = xhtmlTable.concat("<tr>");

						 for (int i = 1; i <= columnCount; i++) {
							 xhtmlTable = xhtmlTable.concat("<td>" + cachedRowSet.getString(i) + "</td>");
						 }
						 
						 
						 xhtmlTable = xhtmlTable.concat("</tr>");
					  }

					xhtmlTable = xhtmlTable.concat("</tbody>");
					 
					xhtmlTable = xhtmlTable.concat("</table>");
	
				} else {
					
					xhtmlTable = "There are no records.";
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		this.setXHTMLTable(xhtmlTable);
	
	}

	public void transformUploadsCachedRowSetIntoDecoratedXHTMLTable () {
		
		CachedRowSet cachedRowSet = this.getCachedRowSet();
		
		String decoratedXHTMLTable = null;

			try {
				if (cachedRowSet.next()) {

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
					 


					while (cachedRowSet.next()) {
						 
						decoratedXHTMLTable = decoratedXHTMLTable.concat("<tr>");

						 decoratedXHTMLTable = decoratedXHTMLTable.concat("<td><input type=\"checkbox\" TODOname=\"\" /></td>");
						 decoratedXHTMLTable = decoratedXHTMLTable.concat("<td>" + cachedRowSet.getString("id") + "</td>");
						 decoratedXHTMLTable = decoratedXHTMLTable.concat("<td><a href=\"files/TODO-Download-URL\">" + cachedRowSet.getString("original_filename") + "</a></td>");
						 //TODO: format datetime 02 Jan 2011
						 decoratedXHTMLTable = decoratedXHTMLTable.concat("<td>" + cachedRowSet.getString("created_on_datetime") + "</td>");
						 decoratedXHTMLTable = decoratedXHTMLTable.concat("<td><a href=\"files/TODO-Download-URL\">Download</a></td>");
						 
						 decoratedXHTMLTable = decoratedXHTMLTable.concat("</tr>");
					  }

					decoratedXHTMLTable = decoratedXHTMLTable.concat("</tbody>");
					 
					decoratedXHTMLTable = decoratedXHTMLTable.concat("</table>");
					
					decoratedXHTMLTable = decoratedXHTMLTable.concat("<div>TODO: paging</div>");
					
					decoratedXHTMLTable = decoratedXHTMLTable.concat("<script>/* Scripts for sorting and paging */</script>");
	
				} else {
					
					decoratedXHTMLTable = "There are no records.";
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		this.setDecoratedXHTMLTable(decoratedXHTMLTable);
	
	}	
	
}
