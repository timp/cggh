package org.cggh.tools.dataMerger.functions.data.merges;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.merges.MergeModel;

import java.sql.SQLException;



public class MergeFunctions implements java.io.Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = 8859380104215152126L;
	private CachedRowSet joinsAsCachedRowSet = null;
	private String joinsAsXHTMLTable = null;
	private String joinsAsDecoratedXHTMLTable = null;
	private MergeModel mergeModel;
	
	public MergeFunctions() {
		
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
	
	
	public void setJoinsAsDecoratedXHTMLTableUsingJoinsAsCachedRowSet () {

		StringBuffer decoratedXHTMLTable = null;

			try {
				if (this.getJoinsAsCachedRowSet().next()) {

					decoratedXHTMLTable = new StringBuffer();
					
					decoratedXHTMLTable.append("<table class=\"joins-table\">");

					 decoratedXHTMLTable.append("<thead>");
					 decoratedXHTMLTable.append("<tr>");
					 
					 decoratedXHTMLTable.append("<th><!-- Above heading for Col.# --></th>");
					 decoratedXHTMLTable.append("<th><!-- Above heading for Key --></th>");
					 
					 //TODO: This path needs to be dynamic.
					 decoratedXHTMLTable.append("<th class=\"file-link-container\"><a href=\"/dataMerger/files/").append(this.getMergeModel().getFile1Model().getId()).append("\">").append(this.getMergeModel().getFile1Model().getFilename()).append("</a></th>");
					 decoratedXHTMLTable.append("<th class=\"file-link-container\"><a href=\"/dataMerger/files/").append(this.getMergeModel().getFile2Model().getId()).append("\">").append(this.getMergeModel().getFile2Model().getFilename()).append("</a></th>");
					 decoratedXHTMLTable.append("<th><!-- Above heading for Column name --></th>");
					 decoratedXHTMLTable.append("<th><!-- Above column for Col.# update buttons --></th>");
					 decoratedXHTMLTable.append("<th><!-- Above column for Remove column button --></th>");
					 
					 decoratedXHTMLTable.append("</tr>");
					 decoratedXHTMLTable.append("<tr>");
					 
					 decoratedXHTMLTable.append("<th class=\"column_number-heading\">Col. #</th>");
					 decoratedXHTMLTable.append("<th class=\"key-heading\">Key</th>");
					 
					 //TODO: Link these problem messages to a view of the duplicate keys
					 
					 if (this.getMergeModel().getDatatable1Model().getDuplicateKeysCount() == 1) {
						 decoratedXHTMLTable.append("<th class=\"problem-message-container\">").append(this.getMergeModel().getDatatable1Model().getDuplicateKeysCount()).append(" duplicate key</th>");
					 }
					 else if (this.getMergeModel().getDatatable1Model().getDuplicateKeysCount() > 1) {
						 decoratedXHTMLTable.append("<th class=\"problem-message-container\">").append(this.getMergeModel().getDatatable1Model().getDuplicateKeysCount()).append(" duplicate keys</th>");
					 } else {
						 decoratedXHTMLTable.append("<th class=\"problem-message-container\"></th>");
					 }
					 
					 if (this.getMergeModel().getDatatable2Model().getDuplicateKeysCount() == 1) {
						 decoratedXHTMLTable.append("<th class=\"problem-message-container\">").append(this.getMergeModel().getDatatable2Model().getDuplicateKeysCount()).append(" duplicate key</th>");
					 }
					 else if (this.getMergeModel().getDatatable2Model().getDuplicateKeysCount() > 1) {
						 decoratedXHTMLTable.append("<th class=\"problem-message-container\">").append(this.getMergeModel().getDatatable2Model().getDuplicateKeysCount()).append(" duplicate keys</th>");
					 } else {
						 decoratedXHTMLTable.append("<th class=\"problem-message-container\"></th>");
					 }
					 
					 decoratedXHTMLTable.append("<th class=\"column_name-heading\">Column name</th>");
					 decoratedXHTMLTable.append("<th><!-- Above column for Col.# update buttons --></th>");
					 decoratedXHTMLTable.append("<th><!-- Above column for Remove column button --></th>");
					 
					 decoratedXHTMLTable.append("</tr>");
					 decoratedXHTMLTable.append("</thead>");
					 
					 decoratedXHTMLTable.append("<tbody>");
					 
					//because next() skips the first row.
					 this.getJoinsAsCachedRowSet().beforeFirst();

						String rowStripeClassName = "even "; 
						String rowFirstClassName = "first ";
						String rowLastClassName = ""; 					 
					 
					while (this.getJoinsAsCachedRowSet().next()) {
						 
						if (rowStripeClassName == "odd ") {
							rowStripeClassName = "even ";
						} else {
							rowStripeClassName = "odd ";
						}
						
						//TODO: This might need changing when paging.
						if (this.getJoinsAsCachedRowSet().isLast()) {
							rowLastClassName = "last ";
						}
						
						decoratedXHTMLTable.append("<tr class=\"").append(rowStripeClassName).append(rowFirstClassName).append(rowLastClassName).append("\">");

						decoratedXHTMLTable.append("<td class=\"column_number-container\"><input readonly=\"readonly\" type=\"text\" name=\"column_number\" value=\"").append(this.getJoinsAsCachedRowSet().getInt("column_number")).append("\"/></td>");
						
						
						if (this.getJoinsAsCachedRowSet().getString("datatable_1_column_name") == null || this.getJoinsAsCachedRowSet().getString("datatable_2_column_name") == null) {
							//Can only have a key relevant to both tables.
							decoratedXHTMLTable.append("<td class=\"key-container\"><input type=\"checkbox\" name=\"key-").append(this.getJoinsAsCachedRowSet().getInt("column_number")).append("\" value=\"").append(this.getJoinsAsCachedRowSet().getBoolean("key")).append("\" disabled=\"disabled\" /></td>");							
						} else {
							if (this.getJoinsAsCachedRowSet().getBoolean("key")) {
								decoratedXHTMLTable.append("<td class=\"key-container\"><input type=\"checkbox\" name=\"key-").append(this.getJoinsAsCachedRowSet().getInt("column_number")).append("\" value=\"").append(this.getJoinsAsCachedRowSet().getBoolean("key")).append("\" checked=\"checked\"/></td>");
							} else {
								decoratedXHTMLTable.append("<td class=\"key-container\"><input type=\"checkbox\" name=\"key-").append(this.getJoinsAsCachedRowSet().getInt("column_number")).append("\" value=\"").append(this.getJoinsAsCachedRowSet().getBoolean("key")).append("\" /></td>");
							}
						}
							
						if (this.getJoinsAsCachedRowSet().getString("datatable_1_column_name") != null) {
							decoratedXHTMLTable.append("<td class=\"datatable_1_column_name-container\">");
							decoratedXHTMLTable.append("<input type=\"text\" readonly=\"readonly\" name=\"datatable_1_column_name-").append(this.getJoinsAsCachedRowSet().getInt("column_number")).append("\" value=\"").append(this.getJoinsAsCachedRowSet().getString("datatable_1_column_name")).append("\"/>");
							
							//TODO
							//decoratedXHTMLTable.append("<textarea readonly=\"readonly\">TODO: Sample of data</textarea>");
							
							decoratedXHTMLTable.append("</td>");
						} else {
							if (this.getJoinsAsCachedRowSet().getString("constant_1") != null) {
								decoratedXHTMLTable.append("<td class=\"constant_1-container\"><label for=\"constant_1-").append(this.getJoinsAsCachedRowSet().getInt("column_number")).append("\">Constant:</label><input type=\"text\" readonly=\"readonly\" name=\"constant_1-").append(this.getJoinsAsCachedRowSet().getInt("column_number")).append("\" value=\"").append(this.getJoinsAsCachedRowSet().getString("constant_1")).append("\"/></td>");
							} else {
								decoratedXHTMLTable.append("<td class=\"null-container\">NULL</td>");
							}
						}
							
						
						
						if (this.getJoinsAsCachedRowSet().getString("datatable_2_column_name") != null) {
							decoratedXHTMLTable.append("<td class=\"datatable_2_column_name-container\">");
							decoratedXHTMLTable.append("<input type=\"text\" readonly=\"readonly\" name=\"datatable_2_column_name-").append(this.getJoinsAsCachedRowSet().getInt("column_number")).append("\" value=\"").append(this.getJoinsAsCachedRowSet().getString("datatable_2_column_name")).append("\"/>");
							
							//TODO
							//decoratedXHTMLTable.append("<textarea readonly=\"readonly\">TODO: Sample of data</textarea>");
							
							decoratedXHTMLTable.append("</td>");
						} else {
							if (this.getJoinsAsCachedRowSet().getString("constant_2") != null) {
								decoratedXHTMLTable.append("<td class=\"constant_2-container\"><label for=\"constant_2-").append(this.getJoinsAsCachedRowSet().getInt("column_number")).append("\">Constant:</label><input type=\"text\" readonly=\"readonly\" name=\"constant_2-").append(this.getJoinsAsCachedRowSet().getInt("column_number")).append("\" value=\"").append(this.getJoinsAsCachedRowSet().getString("constant_2")).append("\"/></td>");
							} else {
								decoratedXHTMLTable.append("<td class=\"null-container\">NULL</td>");
							}
						}
						
						
						
						decoratedXHTMLTable.append("<td class=\"column_name-container\"><input type=\"text\" name=\"column_name\" value=\"").append(this.getJoinsAsCachedRowSet().getString("column_name")).append("\" /></td>");
						
						//TODO: Improve move function calls/naming.
						
						decoratedXHTMLTable.append("<td class=\"move-row-buttons-container\">");
						
						if (this.getJoinsAsCachedRowSet().getInt("column_number") > 1) {
							decoratedXHTMLTable.append("<button class=\"move up\"><img src=\"/dataMerger/pages/shared/png/up.png\" title=\"Up\" /></button>");
						} else {
							decoratedXHTMLTable.append("<button class=\"move up\" disabled=\"disabled\"><img src=\"/dataMerger/pages/shared/png/up.png\" title=\"Up\" /></button>");
						}
						
						if (this.getJoinsAsCachedRowSet().getInt("column_number") < this.getJoinsAsCachedRowSet().size()) {
							decoratedXHTMLTable.append("<button class=\"move down\"><img src=\"/dataMerger/pages/shared/png/down.png\" title=\"Down\" /></button>");
						} else {	
							decoratedXHTMLTable.append("<button class=\"move down\" disabled=\"disabled\"><img src=\"/dataMerger/pages/shared/png/down.png\" title=\"Down\" /></button>");
						}
						
						decoratedXHTMLTable.append("</td>");
						
						decoratedXHTMLTable.append("<td class=\"remove-button-container\"><button class=\"remove\">Remove</button></td>");
						
						decoratedXHTMLTable.append("</tr>");
						
						rowFirstClassName = "";
					  }

					decoratedXHTMLTable.append("</tbody>");
					 
					decoratedXHTMLTable.append("</table>");
					
					decoratedXHTMLTable.append("<!-- <div>TODO: paging</div> -->");
					
	
				} else {
					
					decoratedXHTMLTable = new StringBuffer("There are no columns.");
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		this.setJoinsAsDecoratedXHTMLTable(decoratedXHTMLTable.toString());
	
	}


	
}
