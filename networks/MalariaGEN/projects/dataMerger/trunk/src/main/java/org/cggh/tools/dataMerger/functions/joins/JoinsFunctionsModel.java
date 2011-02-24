package org.cggh.tools.dataMerger.functions.joins;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.merges.MergeModel;

import java.sql.SQLException;



public class JoinsFunctionsModel implements java.io.Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = 8859380104215152126L;
	private CachedRowSet joinsAsCachedRowSet = null;
	private String joinsAsXHTMLTable = null;
	private String joinsAsDecoratedXHTMLTable = null;
	private MergeModel mergeModel;
	private CachedRowSet keyJoinsAsCachedRowSet;
	
	public JoinsFunctionsModel() {
		
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
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th colspan=\"2\"><a href=\"javascript:TODOshowDuplicateKeys()\">" + this.getMergeModel().getDatatable1Model().getDuplicateKeysCount() + " duplicate keys</a></th>");
					 decoratedXHTMLTable = decoratedXHTMLTable.concat("<th colspan=\"2\"><a href=\"javascript:TODOshowDuplicateKeys()\">" + this.getMergeModel().getDatatable2Model().getDuplicateKeysCount() + " duplicate keys</a></th>");
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
							decoratedXHTMLTable = decoratedXHTMLTable.concat("<td><input type=\"checkbox\" name=\"join_id\" value=\"" + this.getJoinsAsCachedRowSet().getInt("id") + "\" checked=\"checked\"/></td>");
						} else {
							decoratedXHTMLTable = decoratedXHTMLTable.concat("<td><input type=\"checkbox\" name=\"join_id\" value=\"" + this.getJoinsAsCachedRowSet().getInt("id") + "\" /></td>");
						}
						
						decoratedXHTMLTable = decoratedXHTMLTable.concat("<td>" + this.getJoinsAsCachedRowSet().getString("datatable_1_column_name") + "</td>");
						decoratedXHTMLTable = decoratedXHTMLTable.concat("<td>TODO: Sample of data</td>");
						
						decoratedXHTMLTable = decoratedXHTMLTable.concat("<td>" + this.getJoinsAsCachedRowSet().getString("datatable_2_column_name") + "</td>");
						decoratedXHTMLTable = decoratedXHTMLTable.concat("<td>TODO: Sample of data</td>");
						
						decoratedXHTMLTable = decoratedXHTMLTable.concat("<td><input type=\"text\" name=\"column_name\" value=\"" + this.getJoinsAsCachedRowSet().getString("column_name") + "\" /></td>");
						
						//TODO: mechanism for mapping button to column-specific action
						decoratedXHTMLTable = decoratedXHTMLTable.concat("<td><button class=\"update-join-column_number-up\">Up</button></td>");
						decoratedXHTMLTable = decoratedXHTMLTable.concat("<td><button class=\"update-join-column_number-down\">Down</button></td>");
						
						decoratedXHTMLTable = decoratedXHTMLTable.concat("<td><button class=\"delete-join\">Remove</button></td>");
						
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


	public void setKeyJoinsAsCachedRowSetByJoinsAsCachedRowSet() {

		CachedRowSet keyJoinsAsCachedRowSet = null;
		
		try {
			if (this.getJoinsAsCachedRowSet().next()) {		
		
				//because next() skips the first row.
				 this.getJoinsAsCachedRowSet().beforeFirst();

				while (this.getJoinsAsCachedRowSet().next()) {
				
					

				}
				
				
			} else {
				
				//TODO:
				System.out.println("There are no joins.");
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		this.setKeyJoinsAsCachedRowSet(keyJoinsAsCachedRowSet);
	}


	public void setKeyJoinsAsCachedRowSet(CachedRowSet keyJoinsAsCachedRowSet) {


		this.keyJoinsAsCachedRowSet = keyJoinsAsCachedRowSet;
	}



	
}
