package org.cggh.tools.dataMerger.functions.data.merges;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.sql.rowset.CachedRowSet;


public class MergesFunctions implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1021181895735718787L;


	public String getMergesAsDecoratedXHTMLTableUsingMergesAsCachedRowSet(
			CachedRowSet mergesAsCachedRowSet) {
		
		StringBuffer mergesAsDecoratedXHTMLTable = null;
		
		try {
			if (mergesAsCachedRowSet.next()) {

				mergesAsDecoratedXHTMLTable = new StringBuffer();
				
				mergesAsDecoratedXHTMLTable.append("<table class=\"data-table\">");

				 mergesAsDecoratedXHTMLTable.append("<thead>");
				 mergesAsDecoratedXHTMLTable.append("<tr>");
				 
				 mergesAsDecoratedXHTMLTable.append("<th>ID</th>");
				 mergesAsDecoratedXHTMLTable.append("<th>Files</th>");
				 mergesAsDecoratedXHTMLTable.append("<th>Created</th>");
				 mergesAsDecoratedXHTMLTable.append("<th>Updated</th>");
				 mergesAsDecoratedXHTMLTable.append("<th><!-- Column for edit-join links --></th>");
				 mergesAsDecoratedXHTMLTable.append("<th><!-- Column for edit-resolutions links or duplicate keys count--></th>");
				 mergesAsDecoratedXHTMLTable.append("<th><!-- Column for remove button --></th>");
				 mergesAsDecoratedXHTMLTable.append("<th><!-- Column for export button or conflicts count --></th>");
				 
				 mergesAsDecoratedXHTMLTable.append("</tr>");
				 mergesAsDecoratedXHTMLTable.append("</thead>");
				 
				 mergesAsDecoratedXHTMLTable.append("<tbody>");
				 
				//because next() skips the first row.
				 mergesAsCachedRowSet.beforeFirst();

					String rowStripeClassName = "even "; 
					String rowFirstClassName = "first ";
					String rowLastClassName = ""; 
					
					DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");  
				 
				while (mergesAsCachedRowSet.next()) {
					 
					if (rowStripeClassName == "odd ") {
						rowStripeClassName = "even ";
					} else {
						rowStripeClassName = "odd ";
					}
					
					//TODO: This might need changing when paging.
					if (mergesAsCachedRowSet.isLast()) {
						rowLastClassName = "last ";
					}
					
					
					mergesAsDecoratedXHTMLTable.append("<tr class=\"" + rowStripeClassName + rowFirstClassName + rowLastClassName + "\">");

					 mergesAsDecoratedXHTMLTable.append("<td><input type=\"hidden\" name=\"merge_id\" value=\"" + mergesAsCachedRowSet.getString("id")  + "\"/>" + mergesAsCachedRowSet.getString("id") + "</td>");
					 
					 //FIXME: Apparently a bug in CachedRowSet using getX('columnAlias') aka columnLabel, which actually only works with getX('columnName'), so using getX('columnIndex').
					 
					 //TODO: This URL shouldn't be hard-coded
					 mergesAsDecoratedXHTMLTable.append("<td>");
					 mergesAsDecoratedXHTMLTable.append("<a href=\"/dataMerger/files/" + mergesAsCachedRowSet.getInt(2) + "\">" + mergesAsCachedRowSet.getString(3) + "</a><br/>");
					 mergesAsDecoratedXHTMLTable.append("<a href=\"/dataMerger/files/" + mergesAsCachedRowSet.getInt(4) + "\">" + mergesAsCachedRowSet.getString(5) + "</a>");
					 mergesAsDecoratedXHTMLTable.append("</td>");
					 //TODO: format datetime 02 Jan 2011
					 mergesAsDecoratedXHTMLTable.append("<td>" + dateFormat.format(mergesAsCachedRowSet.getTimestamp("created_datetime")) + "</td>");
					 mergesAsDecoratedXHTMLTable.append("<td>" + dateFormat.format(mergesAsCachedRowSet.getTimestamp("updated_datetime")) + "</td>");
					 
					 
					 mergesAsDecoratedXHTMLTable.append("<td>");
					 //TODO: Change this URL to a) not hard-coded b) /dataMerger/pages/merges/3/joins
					 mergesAsDecoratedXHTMLTable.append("<a href=\"/dataMerger/pages/merges/joins?merge_id=" + mergesAsCachedRowSet.getInt("id") + "\">Edit Join</a><br/>");
					 
					 //FIXME: totalDuplicateKeysCount == 0, even when null in db
					 
					 Integer totalDuplicateKeysCount = mergesAsCachedRowSet.getInt("total_duplicate_keys_count");
					 
					 if (totalDuplicateKeysCount != null) {
						 
						 if (totalDuplicateKeysCount == 0) {
							 //TODO: Change this URL to a) not hard-coded b) /dataMerger/pages/merges/3/resolutions-by-column
							 mergesAsDecoratedXHTMLTable.append("<a href=\"/dataMerger/pages/merges/resolutions/by-column?merge_id=" + mergesAsCachedRowSet.getInt("id") + "\">Edit Resolutions</a>");
						 } 
						 else if (totalDuplicateKeysCount == 1) {
							 mergesAsDecoratedXHTMLTable.append("<span class=\"problem-message-container\">" + totalDuplicateKeysCount + " duplicate key</span>");
						 }
						 else {
							 mergesAsDecoratedXHTMLTable.append("<span class=\"problem-message-container\">" + totalDuplicateKeysCount + " duplicate keys</span>");
						 }
						 
					 } else {
						 mergesAsDecoratedXHTMLTable.append("<!-- pending a total duplicate keys count -->");
					 }
					
					 mergesAsDecoratedXHTMLTable.append("</td>");
					 
					 
					 mergesAsDecoratedXHTMLTable.append("<td><button class=\"deleteMergeButton\">Delete</button><img class=\"deleting-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Deleting...\"/></td>");
					 
					 
					 //FIXME: totalConflictsCount == 0, even when null in db
					 
					 Integer totalConflictsCount = mergesAsCachedRowSet.getInt("total_conflicts_count");
					 
					 if (totalConflictsCount != null && totalDuplicateKeysCount == 0) {
						 
						 if (totalConflictsCount == 0) {
							 mergesAsDecoratedXHTMLTable.append("<td><input type=\"text\" name=\"mergedFileFilename\" value=\"merged_file_" + mergesAsCachedRowSet.getInt("id") + ".csv\"/><button class=\"export-button\">Export</button><img class=\"exporting-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Exporting...\"/></td>");
						 }
						 else if (totalConflictsCount == 1) {
							 mergesAsDecoratedXHTMLTable.append("<td class=\"problem-message-container\">" + totalConflictsCount + " conflict</td>");
						 } else {
							 mergesAsDecoratedXHTMLTable.append("<td class=\"problem-message-container\">" + totalConflictsCount + " conflicts</td>");
						 } 
					 
					 } else {
						 mergesAsDecoratedXHTMLTable.append("<td><!-- pending a total conflicts count --></td>");
					 }
					 
					 mergesAsDecoratedXHTMLTable.append("</tr>");
					 
					 rowFirstClassName = "";
				  }

				mergesAsDecoratedXHTMLTable.append("</tbody>");
				 
				mergesAsDecoratedXHTMLTable.append("</table>");
				
				mergesAsDecoratedXHTMLTable.append("<!-- <div>TODO: paging</div> -->");
				
			} else {
				
				mergesAsDecoratedXHTMLTable = new StringBuffer("<p>You have no merges.</p>");
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return mergesAsDecoratedXHTMLTable.toString();
	}

	

	
}
