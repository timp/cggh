package org.cggh.tools.dataMerger.functions.merges;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.sql.rowset.CachedRowSet;


public class MergesFunctionsModel implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1021181895735718787L;
	private CachedRowSet mergesAsCachedRowSet;
	private String mergesAsDecoratedXHTMLTable;

	public MergesFunctionsModel() {
		
		
	}

	public void setMergesAsCachedRowSet(CachedRowSet mergesAsCachedRowSet) {
		this.mergesAsCachedRowSet = mergesAsCachedRowSet;
	}

	public void setMergesAsDecoratedXHTMLTableUsingMergesAsCachedRowSet() {
		
		String mergesAsDecoratedXHTMLTable = null;
		
		try {
			if (this.getMergesAsCachedRowSet().next()) {

				mergesAsDecoratedXHTMLTable = "";
				
				mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<table class=\"merges-table\">");

				 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<thead>");
				 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<tr>");
				 
				 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<th>ID<!--  <a href=\"javascript:TODOsort();\">sort up/down</a> --></th>");
				 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<th>File 1<!--  <a href=\"javascript:TODOsort();\">sort up/down</a> --></th>");
				 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<th>File 2<!--  <a href=\"javascript:TODOsort();\">sort up/down</a> --></th>");
				 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<th>Created<!--  <a href=\"javascript:TODOsort();\">sort up/down</a> --></th>");
				 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<th>Updated<!--  <a href=\"javascript:TODOsort();\">sort up/down</a> --></th>");
				 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<th><!-- Column for edit-join links --></th>");
				 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<th><!-- Column for edit-resolutions links or duplicate keys count--></th>");
				 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<th><!-- Column for export button or conflicts count --></th>");
				 
				 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("</tr>");
				 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("</thead>");
				 
				 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<tbody>");
				 
				//because next() skips the first row.
				 this.getMergesAsCachedRowSet().beforeFirst();

					String rowStripeClassName = "even "; 
					String rowFirstClassName = "first ";
					String rowLastClassName = ""; 
					
					DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");  
				 
				while (this.getMergesAsCachedRowSet().next()) {
					 
					if (rowStripeClassName == "odd ") {
						rowStripeClassName = "even ";
					} else {
						rowStripeClassName = "odd ";
					}
					
					//TODO: This might need changing when paging.
					if (this.getMergesAsCachedRowSet().isLast()) {
						rowLastClassName = "last ";
					}
					
					
					mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<tr class=\"" + rowStripeClassName + rowFirstClassName + rowLastClassName + "\">");

					 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<td>" + this.getMergesAsCachedRowSet().getString("id") + "</td>");
					 
					 //FIXME: Apparently a bug in CachedRowSet using getX('columnAlias') aka columnLabel, which actually only works with getX('columnName'), so using getX('columnIndex').
					 
					 //TODO: This URL shouldn't be hard-coded
					 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<td><a href=\"/dataMerger/files/uploads?id=" + this.getMergesAsCachedRowSet().getInt(2) + "\">" + this.getMergesAsCachedRowSet().getString(3) + "</a></td>");
					 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<td><a href=\"/dataMerger/files/uploads?id=" + this.getMergesAsCachedRowSet().getInt(4) + "\">" + this.getMergesAsCachedRowSet().getString(5) + "</a></td>");
					 //TODO: format datetime 02 Jan 2011
					 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<td>" + dateFormat.format(this.getMergesAsCachedRowSet().getTimestamp("created_datetime")) + "</td>");
					 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<td>" + dateFormat.format(this.getMergesAsCachedRowSet().getTimestamp("updated_datetime")) + "</td>");
					 
					 //TODO: Change this URL to a) not hard-coded b) /dataMerger/pages/merges/3/joins
					 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<td><a href=\"/dataMerger/pages/merges/edit-join.jsp?merge_id=" + this.getMergesAsCachedRowSet().getInt("id") + "\">Edit Join</a></td>");
					 
					 //FIXME: totalDuplicateKeysCount == 0, even when null in db
					 
					 Integer totalDuplicateKeysCount = this.getMergesAsCachedRowSet().getInt("total_duplicate_keys_count");
					 
					 if (totalDuplicateKeysCount != null) {
						 
						 if (totalDuplicateKeysCount == 0) {
							 //TODO: Change this URL to a) not hard-coded b) /dataMerger/pages/merges/3/resolutions-by-column
							 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<td><a href=\"/dataMerger/pages/merges/edit-resolutions-by-column.jsp?merge_id=" + this.getMergesAsCachedRowSet().getInt("id") + "\">Edit Resolutions</a></td>");
						 } 
						 else if (totalDuplicateKeysCount == 1) {
							 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<td>" + totalDuplicateKeysCount + " duplicate key</td>");
						 }
						 else {
							 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<td>" + totalDuplicateKeysCount + " duplicate keys</td>");
						 }
						 
					 } else {
						 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<td><!-- pending a total duplicate keys count --></td>");
					 }
					
					 //FIXME: totalConflictsCount == 0, even when null in db
					 
					 Integer totalConflictsCount = this.getMergesAsCachedRowSet().getInt("total_conflicts_count");
					 
					 if (totalConflictsCount != null) {
						 
						 if (totalConflictsCount == 0) {
							 //TODO: Wire this up to AJAX (or direct JS call)
							 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<td><button class=\"export merge\">Export</button></td>");
						 }
						 else if (totalConflictsCount == 1) {
							 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<td>" + totalConflictsCount + " conflict/td>");
						 } else {
							 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<td>" + totalConflictsCount + " conflicts</td>");
						 } 
					 
					 } else {
						 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<td><!-- pending a total conflicts count --></td>");
					 }
					 
					 mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("</tr>");
					 
					 rowFirstClassName = "";
				  }

				mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("</tbody>");
				 
				mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("</table>");
				
				mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable.concat("<!-- <div>TODO: paging</div> -->");
				
			} else {
				
				mergesAsDecoratedXHTMLTable = "<p>You have no merges.</p>";
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.mergesAsDecoratedXHTMLTable = mergesAsDecoratedXHTMLTable;
	}

	public CachedRowSet getMergesAsCachedRowSet() {
		return this.mergesAsCachedRowSet;
	}

	public String getMergesAsDecoratedXHTMLTable() {
		return this.mergesAsDecoratedXHTMLTable;
	}

	

	
}
