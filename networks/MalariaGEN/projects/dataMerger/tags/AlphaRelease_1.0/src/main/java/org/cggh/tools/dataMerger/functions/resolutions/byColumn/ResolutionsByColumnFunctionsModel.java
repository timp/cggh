package org.cggh.tools.dataMerger.functions.resolutions.byColumn;

import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;



public class ResolutionsByColumnFunctionsModel implements java.io.Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7663105154570885669L;
	private CachedRowSet resolutionsByColumnAsCachedRowSet;
	private String resolutionsByColumnAsDecoratedXHTMLTable;
	private CachedRowSet solutionsByColumnAsCachedRowSet;

	public ResolutionsByColumnFunctionsModel () {
		
	}

	public void setResolutionsByColumnAsCachedRowSet(
			CachedRowSet resolutionsByColumnAsCachedRowSet) {
		this.resolutionsByColumnAsCachedRowSet = resolutionsByColumnAsCachedRowSet;
	}

	public void setResolutionsByColumnAsDecoratedXHTMLTableUsingResolutionsByColumnAsCachedRowSet() {
		
		String resolutionsByColumnAsDecoratedXHTMLTable = null;
		
		
		try {
			
			//Make sure we are at the beginning of the RowSet.
			this.getResolutionsByColumnAsCachedRowSet().beforeFirst();
			
			if (this.getResolutionsByColumnAsCachedRowSet().next()) {

				resolutionsByColumnAsDecoratedXHTMLTable = "";
				
				resolutionsByColumnAsDecoratedXHTMLTable += "<table class=\"resolutions-by-column-table\">";

				 resolutionsByColumnAsDecoratedXHTMLTable += "<thead>";
				 resolutionsByColumnAsDecoratedXHTMLTable += "<tr>";
				 
				 resolutionsByColumnAsDecoratedXHTMLTable += "<th class=\"column_name-heading\">Column</th>";
				 
				 //TODO: colspan=2 along with link to view data (TODO)
				 resolutionsByColumnAsDecoratedXHTMLTable += "<th class=\"problem-heading\">Problem</th>";
				 resolutionsByColumnAsDecoratedXHTMLTable += "<th class=\"solution-heading\">Solution</th>";

				 resolutionsByColumnAsDecoratedXHTMLTable += "</tr>";
				 resolutionsByColumnAsDecoratedXHTMLTable += "</thead>";
				 
				 resolutionsByColumnAsDecoratedXHTMLTable += "<tbody>";
				 
				//because next() skips the first row.
				 this.getResolutionsByColumnAsCachedRowSet().beforeFirst();

					String rowStripeClassName = "even "; 
					String rowFirstClassName = "first ";
					String rowLastClassName = ""; 	
					
				while (this.getResolutionsByColumnAsCachedRowSet().next()) {

					
					if (rowStripeClassName == "odd ") {
						rowStripeClassName = "even ";
					} else {
						rowStripeClassName = "odd ";
					}
					
					//TODO: This might need changing when paging.
					if (this.getResolutionsByColumnAsCachedRowSet().isLast()) {
						rowLastClassName = "last ";
					}
					
					resolutionsByColumnAsDecoratedXHTMLTable += "<tr class=\"" + rowStripeClassName + rowFirstClassName + rowLastClassName + "\">";

					 resolutionsByColumnAsDecoratedXHTMLTable += "<td class=\"column_name-container\"><input type=\"hidden\" name=\"column_number\" value=\"" + this.getResolutionsByColumnAsCachedRowSet().getString("column_number") + "\"/>" + this.getResolutionsByColumnAsCachedRowSet().getString("column_name") + "</td>";
					 
					 Integer solutionByColumnId = this.getResolutionsByColumnAsCachedRowSet().getInt("solution_by_column_id");
					 
					 if (solutionByColumnId == 0) {
						 resolutionsByColumnAsDecoratedXHTMLTable += "<td class=\"problem_description-container unresolved-problem-container\"><input type=\"hidden\" name=\"conflict_id\" value=\"" + this.getResolutionsByColumnAsCachedRowSet().getString("conflict_id") + "\"/>" + this.getResolutionsByColumnAsCachedRowSet().getInt("conflicts_count") + " " + this.getResolutionsByColumnAsCachedRowSet().getString("description") +"</td>";
					 } else {
						 resolutionsByColumnAsDecoratedXHTMLTable += "<td class=\"problem_description-container\"><input type=\"hidden\" name=\"conflict_id\" value=\"" + this.getResolutionsByColumnAsCachedRowSet().getString("conflict_id") + "\"/>" + this.getResolutionsByColumnAsCachedRowSet().getInt("conflicts_count") + " " + this.getResolutionsByColumnAsCachedRowSet().getString("description") + "</td>";
					 }
					 
					 //TODO
					 //resolutionsByColumnAsDecoratedXHTMLTable += "<td><a href=\"TODO\">[TODO: link to view]</td>";
					 
					 resolutionsByColumnAsDecoratedXHTMLTable += "<td class=\"solution_by_column_id-container\">";
					 
						try {
							
							//because the cursor may have been moved to the end previously (we are in a loop).
							 this.getSolutionsByColumnAsCachedRowSet().beforeFirst();
							
							if (this.getSolutionsByColumnAsCachedRowSet().next()) {
								
								if (solutionByColumnId == 0) {
									resolutionsByColumnAsDecoratedXHTMLTable += "<select class=\"unresolved\" name=\"solution_by_column_id\">";
								} else {
									resolutionsByColumnAsDecoratedXHTMLTable += "<select name=\"solution_by_column_id\">";
								}

								//FIXME: Should the null option be in the table?
								
								resolutionsByColumnAsDecoratedXHTMLTable += "<option value=\"\">";

								 resolutionsByColumnAsDecoratedXHTMLTable += "Unresolved";
								 
								 resolutionsByColumnAsDecoratedXHTMLTable += "</option>";				
								 
								//because next() skips the first row.
								 this.getSolutionsByColumnAsCachedRowSet().beforeFirst();

								 
								while (this.getSolutionsByColumnAsCachedRowSet().next()) {

									
									String selectedAttribute = "";
									
									if (this.getResolutionsByColumnAsCachedRowSet().getInt("solution_by_column_id") == this.getSolutionsByColumnAsCachedRowSet().getInt("id")) {
										selectedAttribute = " selected=\"selected\"";
									}
									
									resolutionsByColumnAsDecoratedXHTMLTable += "<option value=\"" + this.getSolutionsByColumnAsCachedRowSet().getString("id") + "\"" + selectedAttribute + ">";

									 resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat(this.getSolutionsByColumnAsCachedRowSet().getString("description"));
									 
									 resolutionsByColumnAsDecoratedXHTMLTable += "</option>";
								}
									 
								resolutionsByColumnAsDecoratedXHTMLTable += "</select>";
								
								
							} else {
								
								resolutionsByColumnAsDecoratedXHTMLTable = "There are no solutions by column.";
								
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					 
						if (this.getResolutionsByColumnAsCachedRowSet().getString("constant") != null) {
							resolutionsByColumnAsDecoratedXHTMLTable += "<label for=\"constant-" + this.getResolutionsByColumnAsCachedRowSet().getInt("column_number") + "\">Constant:</label><input type=\"text\" name=\"constant-" + this.getResolutionsByColumnAsCachedRowSet().getInt("column_number") + "\" value=\"" + this.getResolutionsByColumnAsCachedRowSet().getString("constant") + "\"/>";
						} else {
							resolutionsByColumnAsDecoratedXHTMLTable += "<label for=\"constant-" + this.getResolutionsByColumnAsCachedRowSet().getInt("column_number") + "\" style=\"display:none;\">Constant:</label><input type=\"text\" name=\"constant-" + this.getResolutionsByColumnAsCachedRowSet().getInt("column_number") + "\" value=\"\" style=\"display:none;\"/>";
						}
						
					 resolutionsByColumnAsDecoratedXHTMLTable += "</td>";
					 
					 //TODO
					 //resolutionsByColumnAsDecoratedXHTMLTable += "<td><a href=\"TODO\">[TODO: link to view]</td>";
					 
					 resolutionsByColumnAsDecoratedXHTMLTable += "</tr>";
					 
					 rowFirstClassName = "";
				}
					 
				resolutionsByColumnAsDecoratedXHTMLTable += "</tbody>";
				 
				resolutionsByColumnAsDecoratedXHTMLTable += "</table>";
				
				resolutionsByColumnAsDecoratedXHTMLTable += "<!--<div>TODO: paging</div>-->";
				
			} else {
				
				resolutionsByColumnAsDecoratedXHTMLTable = "There are no unresolved conflicts.";
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable;
	}


	public CachedRowSet getResolutionsByColumnAsCachedRowSet() {
		return this.resolutionsByColumnAsCachedRowSet;
	}

	public String getResolutionsByColumnAsDecoratedXHTMLTable() {
		return this.resolutionsByColumnAsDecoratedXHTMLTable;
	}

	public void setSolutionsByColumnAsCachedRowSet(
			CachedRowSet solutionsByColumnAsCachedRowSet) {
		this.solutionsByColumnAsCachedRowSet = solutionsByColumnAsCachedRowSet;
		
	}



	public CachedRowSet getSolutionsByColumnAsCachedRowSet() {
		return this.solutionsByColumnAsCachedRowSet;
	}


}
