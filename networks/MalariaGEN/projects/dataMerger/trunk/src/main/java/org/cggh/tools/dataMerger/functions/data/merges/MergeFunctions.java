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

		String decoratedXHTMLTable = null;

			try {
				if (this.getJoinsAsCachedRowSet().next()) {

					decoratedXHTMLTable = "";
					
					decoratedXHTMLTable += "<table class=\"joins-table\">";

					 decoratedXHTMLTable += "<thead>";
					 decoratedXHTMLTable += "<tr>";
					 
					 decoratedXHTMLTable += "<th><!-- Above heading for Col.# --></th>";
					 decoratedXHTMLTable += "<th><!-- Above heading for Key --></th>";
					 
					 //TODO: This path needs to be dynamic.
					 decoratedXHTMLTable += "<th class=\"file-link-container\"><a href=\"/dataMerger/files/uploads/" + this.getMergeModel().getFile1Model().getId() + "\">" + this.getMergeModel().getFile1Model().getFilename() + "</a></th>";
					 decoratedXHTMLTable += "<th class=\"file-link-container\"><a href=\"/dataMerger/files/uploads/" + this.getMergeModel().getFile2Model().getId() + "\">" + this.getMergeModel().getFile2Model().getFilename() + "</a></th>";
					 decoratedXHTMLTable += "<th><!-- Above heading for Column name --></th>";
					 decoratedXHTMLTable += "<th><!-- Above column for Col.# update buttons --></th>";
					 decoratedXHTMLTable += "<th><!-- Above column for Remove column button --></th>";
					 
					 decoratedXHTMLTable += "</tr>";
					 decoratedXHTMLTable += "<tr>";
					 
					 decoratedXHTMLTable += "<th class=\"column_number-heading\">Col. #</th>";
					 decoratedXHTMLTable += "<th class=\"key-heading\">Key</th>";
					 
					 //TODO: Link these problem messages to a view of the duplicate keys
					 
					 if (this.getMergeModel().getDatatable1Model().getDuplicateKeysCount() == 1) {
						 decoratedXHTMLTable += "<th class=\"problem-message-container\">" + this.getMergeModel().getDatatable1Model().getDuplicateKeysCount() + " duplicate key</th>";
					 }
					 else if (this.getMergeModel().getDatatable1Model().getDuplicateKeysCount() > 1) {
						 decoratedXHTMLTable += "<th class=\"problem-message-container\">" + this.getMergeModel().getDatatable1Model().getDuplicateKeysCount() + " duplicate keys</th>";
					 } else {
						 decoratedXHTMLTable += "<th class=\"problem-message-container\"></th>";
					 }
					 
					 if (this.getMergeModel().getDatatable2Model().getDuplicateKeysCount() == 1) {
						 decoratedXHTMLTable += "<th class=\"problem-message-container\">" + this.getMergeModel().getDatatable2Model().getDuplicateKeysCount() + " duplicate key</th>";
					 }
					 else if (this.getMergeModel().getDatatable2Model().getDuplicateKeysCount() > 1) {
						 decoratedXHTMLTable += "<th class=\"problem-message-container\">" + this.getMergeModel().getDatatable2Model().getDuplicateKeysCount() + " duplicate keys</th>";
					 } else {
						 decoratedXHTMLTable += "<th class=\"problem-message-container\"></th>";
					 }
					 
					 decoratedXHTMLTable += "<th class=\"column_name-heading\">Column name</th>";
					 decoratedXHTMLTable += "<th><!-- Above column for Col.# update buttons --></th>";
					 decoratedXHTMLTable += "<th><!-- Above column for Remove column button --></th>";
					 
					 decoratedXHTMLTable += "</tr>";
					 decoratedXHTMLTable += "</thead>";
					 
					 decoratedXHTMLTable += "<tbody>";
					 
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
						
						decoratedXHTMLTable += "<tr class=\"" + rowStripeClassName + rowFirstClassName + rowLastClassName + "\">";

						decoratedXHTMLTable += "<td class=\"column_number-container\"><input readonly=\"readonly\" type=\"text\" name=\"column_number\" value=\"" + this.getJoinsAsCachedRowSet().getInt("column_number") + "\"/></td>";
						
						
						if (this.getJoinsAsCachedRowSet().getString("datatable_1_column_name") == null || this.getJoinsAsCachedRowSet().getString("datatable_2_column_name") == null) {
							//Can only have a key relevant to both tables.
							decoratedXHTMLTable += "<td class=\"key-container\"><input type=\"checkbox\" name=\"key-" + this.getJoinsAsCachedRowSet().getInt("column_number") + "\" value=\"" + this.getJoinsAsCachedRowSet().getBoolean("key") + "\" disabled=\"disabled\" /></td>";							
						} else {
							if (this.getJoinsAsCachedRowSet().getBoolean("key")) {
								decoratedXHTMLTable += "<td class=\"key-container\"><input type=\"checkbox\" name=\"key-" + this.getJoinsAsCachedRowSet().getInt("column_number") + "\" value=\"" + this.getJoinsAsCachedRowSet().getBoolean("key") + "\" checked=\"checked\"/></td>";
							} else {
								decoratedXHTMLTable += "<td class=\"key-container\"><input type=\"checkbox\" name=\"key-" + this.getJoinsAsCachedRowSet().getInt("column_number") + "\" value=\"" + this.getJoinsAsCachedRowSet().getBoolean("key") + "\" /></td>";
							}
						}
							
						if (this.getJoinsAsCachedRowSet().getString("datatable_1_column_name") != null) {
							decoratedXHTMLTable += "<td class=\"datatable_1_column_name-container\">";
							decoratedXHTMLTable += "<input type=\"text\" readonly=\"readonly\" name=\"datatable_1_column_name-" + this.getJoinsAsCachedRowSet().getInt("column_number") + "\" value=\"" + this.getJoinsAsCachedRowSet().getString("datatable_1_column_name") + "\"/>";
							
							//TODO
							//decoratedXHTMLTable += "<textarea readonly=\"readonly\">TODO: Sample of data</textarea>";
							
							decoratedXHTMLTable += "</td>";
						} else {
							if (this.getJoinsAsCachedRowSet().getString("constant_1") != null) {
								decoratedXHTMLTable += "<td class=\"constant_1-container\"><label for=\"constant_1-" + this.getJoinsAsCachedRowSet().getInt("column_number") + "\">Constant:</label><input type=\"text\" readonly=\"readonly\" name=\"constant_1-" + this.getJoinsAsCachedRowSet().getInt("column_number") + "\" value=\"" + this.getJoinsAsCachedRowSet().getString("constant_1") + "\"/></td>";
							} else {
								decoratedXHTMLTable += "<td class=\"null-container\">NULL</td>";
							}
						}
							
						
						
						if (this.getJoinsAsCachedRowSet().getString("datatable_2_column_name") != null) {
							decoratedXHTMLTable += "<td class=\"datatable_2_column_name-container\">";
							decoratedXHTMLTable += "<input type=\"text\" readonly=\"readonly\" name=\"datatable_2_column_name-" + this.getJoinsAsCachedRowSet().getInt("column_number") + "\" value=\"" + this.getJoinsAsCachedRowSet().getString("datatable_2_column_name") + "\"/>";
							
							//TODO
							//decoratedXHTMLTable += "<textarea readonly=\"readonly\">TODO: Sample of data</textarea>";
							
							decoratedXHTMLTable += "</td>";
						} else {
							if (this.getJoinsAsCachedRowSet().getString("constant_2") != null) {
								decoratedXHTMLTable += "<td class=\"constant_2-container\"><label for=\"constant_2-" + this.getJoinsAsCachedRowSet().getInt("column_number") + "\">Constant:</label><input type=\"text\" readonly=\"readonly\" name=\"constant_2-" + this.getJoinsAsCachedRowSet().getInt("column_number") + "\" value=\"" + this.getJoinsAsCachedRowSet().getString("constant_2") + "\"/></td>";
							} else {
								decoratedXHTMLTable += "<td class=\"null-container\">NULL</td>";
							}
						}
						
						
						
						decoratedXHTMLTable += "<td class=\"column_name-container\"><input type=\"text\" name=\"column_name\" value=\"" + this.getJoinsAsCachedRowSet().getString("column_name") + "\" /></td>";
						
						//TODO: Improve move function calls/naming.
						
						decoratedXHTMLTable += "<td class=\"move-row-buttons-container\">";
						
						if (this.getJoinsAsCachedRowSet().getInt("column_number") > 1) {
							decoratedXHTMLTable += "<button class=\"move up\"><img src=\"/dataMerger/pages/shared/png/up.png\" title=\"Up\" /></button>";
						} else {
							decoratedXHTMLTable += "<button class=\"move up\" disabled=\"disabled\"><img src=\"/dataMerger/pages/shared/png/up.png\" title=\"Up\" /></button>";
						}
						
						if (this.getJoinsAsCachedRowSet().getInt("column_number") < this.getJoinsAsCachedRowSet().size()) {
							decoratedXHTMLTable += "<button class=\"move down\"><img src=\"/dataMerger/pages/shared/png/down.png\" title=\"Down\" /></button>";
						} else {	
							decoratedXHTMLTable += "<button class=\"move down\" disabled=\"disabled\"><img src=\"/dataMerger/pages/shared/png/down.png\" title=\"Down\" /></button>";
						}
						
						decoratedXHTMLTable += "</td>";
						
						decoratedXHTMLTable += "<td class=\"remove-button-container\"><button class=\"remove\">Remove</button></td>";
						
						decoratedXHTMLTable += "</tr>";
						
						rowFirstClassName = "";
					  }

					decoratedXHTMLTable += "</tbody>";
					 
					decoratedXHTMLTable += "</table>";
					
					decoratedXHTMLTable += "<!-- <div>TODO: paging</div> -->";
					
	
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
