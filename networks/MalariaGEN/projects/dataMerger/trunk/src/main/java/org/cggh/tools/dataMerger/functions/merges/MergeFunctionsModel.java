package org.cggh.tools.dataMerger.functions.merges;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.merges.MergeModel;

import java.sql.SQLException;



public class MergeFunctionsModel implements java.io.Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = 8859380104215152126L;
	private CachedRowSet joinsAsCachedRowSet = null;
	private String joinsAsXHTMLTable = null;
	private String joinsAsDecoratedXHTMLTable = null;
	private MergeModel mergeModel;
	
	public MergeFunctionsModel() {
		
		this.setMergeModel(new MergeModel());
		
	}

	
	public void setJoinsAsCachedRowSet (final CachedRowSet cachedRowSet) {
		
		this.joinsAsCachedRowSet = cachedRowSet;
	}
	public CachedRowSet getJoinsAsCachedRowSet () {
		
		return this.joinsAsCachedRowSet;
	}	
	public void setJoinsAsXHTMLTable (final String xhtmlTable) {
		
		this.joinsAsXHTMLTable = xhtmlTable;
	}
	public String getJoinsAsXHTMLTable () {
		
		return this.joinsAsXHTMLTable;
	}	
	
	public void setJoinsAsDecoratedXHTMLTable (final String decoratedXHTMLTable) {
		this.joinsAsDecoratedXHTMLTable = decoratedXHTMLTable;
	}
	public String getJoinsAsDecoratedXHTMLTable () {
		return this.joinsAsDecoratedXHTMLTable;
	}	
	
	public void setMergeModel (final MergeModel mergeModel) {
		this.mergeModel = mergeModel;
	}
	public MergeModel getMergeModel () {
		return this.mergeModel;
	}	
	
	
	public void setJoinsAsDecoratedXHTMLTableByJoinsAsCachedRowSet () {

		String decoratedXHTMLTable = null;

			try {
				if (this.getJoinsAsCachedRowSet().next()) {

					decoratedXHTMLTable = "";
					
					decoratedXHTMLTable = decoratedXHTMLTable.concat("<table>");

					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<thead>");
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<tr>");
					 
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th><!-- Above heading for Col.# --></th>");
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th><!-- Above heading for Key --></th>");
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th colspan=\"2\"><a href=\"/dataMerger/files/uploads?id=" + this.getMergeModel().getUpload1Model().getId() + "\">" + this.getMergeModel().getUpload1Model().getOriginalFilename() + "</a></th>");
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th colspan=\"2\"><a href=\"/dataMerger/files/uploads?id=" + this.getMergeModel().getUpload2Model().getId() + "\">" + this.getMergeModel().getUpload2Model().getOriginalFilename() + "</a></th>");
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th><!-- Above heading for Column name --></th>");
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th><!-- Above column for Col.# update buttons --></th>");
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th><!-- Above column for Remove column button --></th>");
					 
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("</tr>");
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<tr>");
					 
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th>Col.#</th>");
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th>Key</th>");
					 
					 if (this.getMergeModel().getDatatable1Model().getDuplicateKeysCount() > 0) {
						 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th colspan=\"2\"><a href=\"javascript:TODOshowDuplicateKeys()\">" + this.getMergeModel().getDatatable1Model().getDuplicateKeysCount() + " duplicate keys</a></th>");
					 } else {
						 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th colspan=\"2\"></th>");
					 }
					 
					 if (this.getMergeModel().getDatatable2Model().getDuplicateKeysCount() > 0) {
						 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th colspan=\"2\"><a href=\"javascript:TODOshowDuplicateKeys()\">" + this.getMergeModel().getDatatable2Model().getDuplicateKeysCount() + " duplicate keys</a></th>");
					 } else {
						 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th colspan=\"2\"></th>");
					 }
					 
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th>Column name</th>");
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th><!-- Above column for Col.# update buttons --></th>");
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th><!-- Above column for Remove column button --></th>");
					 
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("</tr>");
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("</thead>");
					 
					//because next() skips the first row.
					 this.getJoinsAsCachedRowSet().beforeFirst();

					while (this.getJoinsAsCachedRowSet().next()) {
						 
						decoratedXHTMLTable = decoratedXHTMLTable.concat("<tr>");

						decoratedXHTMLTable = decoratedXHTMLTable.concat("<td>" + this.getJoinsAsCachedRowSet().getInt("column_number") + "</td>");
						
						if (this.getJoinsAsCachedRowSet().getBoolean("key")) {
							decoratedXHTMLTable = decoratedXHTMLTable.concat("<td><input type=\"checkbox\" name=\"key_" + this.getJoinsAsCachedRowSet().getInt("id") + "\" value=\"" + this.getJoinsAsCachedRowSet().getBoolean("key") + "\" checked=\"checked\"/></td>");
						} else {
							decoratedXHTMLTable = decoratedXHTMLTable.concat("<td><input type=\"checkbox\" name=\"key_" + this.getJoinsAsCachedRowSet().getInt("id") + "\" value=\"" + this.getJoinsAsCachedRowSet().getBoolean("key") + "\" /></td>");
						}
						
						decoratedXHTMLTable = decoratedXHTMLTable.concat("<td>" + this.getJoinsAsCachedRowSet().getString("datatable_1_column_name") + "</td>");
						decoratedXHTMLTable = decoratedXHTMLTable.concat("<td>TODO: Sample of data</td>");
						
						decoratedXHTMLTable = decoratedXHTMLTable.concat("<td>" + this.getJoinsAsCachedRowSet().getString("datatable_2_column_name") + "</td>");
						decoratedXHTMLTable = decoratedXHTMLTable.concat("<td>TODO: Sample of data</td>");
						
						decoratedXHTMLTable = decoratedXHTMLTable.concat("<td><input type=\"text\" name=\"column_name_" + this.getJoinsAsCachedRowSet().getInt("id") + "\" value=\"" + this.getJoinsAsCachedRowSet().getString("column_name") + "\" /></td>");
						
						//TODO: Improve move function calls/naming.
						
						if (this.getJoinsAsCachedRowSet().getInt("column_number") > 1) {
							decoratedXHTMLTable = decoratedXHTMLTable.concat("<td><button class=\"move up\">Up</button></td>");
						} else {
							decoratedXHTMLTable = decoratedXHTMLTable.concat("<td><button class=\"move up\" disabled=\"disabled\">Up</button></td>");
						}
						
						if (this.getJoinsAsCachedRowSet().getInt("column_number") < this.getJoinsAsCachedRowSet().size()) {
							decoratedXHTMLTable = decoratedXHTMLTable.concat("<td><button class=\"move down\">Down</button></td>");
						} else {	
							decoratedXHTMLTable = decoratedXHTMLTable.concat("<td><button class=\"move down\" disabled=\"disabled\">Down</button></td>");
						}
						
						decoratedXHTMLTable = decoratedXHTMLTable.concat("<td><button class=\"delete-join\" onclick=\"deleteJoinByJoinId('" + this.getJoinsAsCachedRowSet().getInt("id") + "')\">Remove</button></td>");
						
						decoratedXHTMLTable = decoratedXHTMLTable.concat("</tr>");
					  }

					decoratedXHTMLTable = decoratedXHTMLTable.concat("</tbody>");
					 
					decoratedXHTMLTable = decoratedXHTMLTable.concat("</table>");
					
					decoratedXHTMLTable = decoratedXHTMLTable.concat("<div>TODO: paging</div>");
					
	
				} else {
					
					decoratedXHTMLTable = "There are no columns.";
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		this.setJoinsAsDecoratedXHTMLTable(decoratedXHTMLTable);
	
	}


	
}
