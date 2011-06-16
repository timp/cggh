package org.cggh.tools.dataMerger.functions;

import javax.sql.rowset.CachedRowSet;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;



public class Functions implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7087898366227739676L;
	private CachedRowSet cachedRowSet = null;
	private String xhtmlTable = null;
	private String decoratedXHTMLTable = null;
	
	public Functions() {
		
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
		
		StringBuffer xhtmlTable = null;

			try {
				if (cachedRowSet.next()) {

					
					xhtmlTable = new StringBuffer();
					
					xhtmlTable.append("<table>");
					
					ResultSetMetaData resultSetMetaData = cachedRowSet.getMetaData(); 
					 int columnCount = resultSetMetaData.getColumnCount();
					 
					 xhtmlTable.append("<thead>");
					 xhtmlTable.append("<tr>");
					 for (int i = 1; i <= columnCount; i++) {
						 xhtmlTable.append("<th>" + resultSetMetaData.getColumnLabel(i) + "</th>");
					   }
					 xhtmlTable.append("</tr>");
					 xhtmlTable.append("</thead>");
					
					//because the check using next() skips the first row.
					cachedRowSet.beforeFirst();

					while (cachedRowSet.next()) {
						 
						xhtmlTable.append("<tr>");

						 for (int i = 1; i <= columnCount; i++) {
							 xhtmlTable.append("<td>" + cachedRowSet.getString(i) + "</td>");
						 }
						 
						 
						 xhtmlTable.append("</tr>");
					  }

					xhtmlTable.append("</tbody>");
					 
					xhtmlTable.append("</table>");
	
				} else {
					
					xhtmlTable = new StringBuffer("There are no records.");
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		this.setXHTMLTable(xhtmlTable.toString());
	
	}



	
}
