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
	
	public FunctionsModel() {
		
	}

	
	public void setCachedRowSet (final CachedRowSet cachedRowSet) {
		
		//TODO:
		System.out.println("setCachedRowSet size " + cachedRowSet.size());
		
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
	
	public void transformCachedRowSetIntoXHTMLTable () {
		
		CachedRowSet cachedRowSet = this.getCachedRowSet();
		
		String xhtmlTable = null;
		
		//TODO:
		System.out.println("transformCachedRowSetIntoXHTMLTable cachedRowSet size " + cachedRowSet.size());

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
					
					//TODO:
					 //System.out.println("xhtmlTable: " + xhtmlTable);
					 
				} else {
					
					xhtmlTable = "There are no records.";
					
					//System.out.println("There are no records");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		this.setXHTMLTable(xhtmlTable);
	
	}
	
}
