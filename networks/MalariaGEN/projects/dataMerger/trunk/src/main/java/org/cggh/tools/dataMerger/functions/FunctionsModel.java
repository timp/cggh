package org.cggh.tools.dataMerger.functions;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.functions.uploads.UploadsFunctionsModel;

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
	private UploadsFunctionsModel uploadsFunctionsModel;
	
	public FunctionsModel() {
		
		this.setUploadsFunctionsModel(new UploadsFunctionsModel());
	}

	
	public void setUploadsFunctionsModel(final UploadsFunctionsModel uploadsFunctionsModel) {
		this.uploadsFunctionsModel = uploadsFunctionsModel;
	}
	public UploadsFunctionsModel getUploadsFunctionsModel() {

		return this.uploadsFunctionsModel;
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
					
					//because the check using next() skips the first row.
					cachedRowSet.beforeFirst();

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



	
}
