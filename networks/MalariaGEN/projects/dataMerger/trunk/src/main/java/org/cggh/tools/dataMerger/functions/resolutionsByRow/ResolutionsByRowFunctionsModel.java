package org.cggh.tools.dataMerger.functions.resolutionsByRow;

import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;



public class ResolutionsByRowFunctionsModel implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 2410465698437549757L;
	private CachedRowSet resolutionsByRowAsCachedRowSet;
	private String resolutionsByRowAsDecoratedXHTMLTable;
	private CachedRowSet solutionsByRowAsCachedRowSet;

	public ResolutionsByRowFunctionsModel () {
		
	}

	public void setResolutionsByRowAsCachedRowSet(
			CachedRowSet resolutionsByRowAsCachedRowSet) {
		this.resolutionsByRowAsCachedRowSet = resolutionsByRowAsCachedRowSet;
	}

	//TODO
	public void setResolutionsByRowAsDecoratedXHTMLTableUsingResolutionsByRowAsCachedRowSet() {
		
		String resolutionsByRowAsDecoratedXHTMLTable = null;
		
		
		try {
			
			//Make sure we are at the beginning of the RowSet.
			this.getResolutionsByRowAsCachedRowSet().beforeFirst();
			
			if (this.getResolutionsByRowAsCachedRowSet().next()) {

				resolutionsByRowAsDecoratedXHTMLTable = "";
				
				resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable.concat("<table>");

				 resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable.concat("<thead>");
				 resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable.concat("<tr>");
				 
				 resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable.concat("<th>Solution</th>");

				 resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable.concat("</tr>");
				 resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable.concat("</thead>");
				 
				 resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable.concat("<tbody>");
				 
				//because next() skips the first row.
				 this.getResolutionsByRowAsCachedRowSet().beforeFirst();

				while (this.getResolutionsByRowAsCachedRowSet().next()) {
					 
					resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable.concat("<tr>");

				
					 resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable.concat("<td>");
					 
						try {
							
							//because the cursor may have been moved to the end previously (we are in a loop).
							 this.getSolutionsByRowAsCachedRowSet().beforeFirst();
							
							if (this.getSolutionsByRowAsCachedRowSet().next()) {
								
								resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable.concat("<select name=\"solution_by_row_id\">");

								//FIXME: Should the null option be in the table?
								
								resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable.concat("<option value=\"\">");

								 resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable.concat("Unresolved");
								 
								 resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable.concat("</option>");				
								 
								//because next() skips the first row.
								 this.getSolutionsByRowAsCachedRowSet().beforeFirst();

								while (this.getSolutionsByRowAsCachedRowSet().next()) {
									
									String selectedAttribute = "";
									
									if (this.getResolutionsByRowAsCachedRowSet().getInt("solution_by_row_id") == this.getSolutionsByRowAsCachedRowSet().getInt("id")) {
										selectedAttribute = " selected=\"selected\"";
									}
									
									resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable.concat("<option value=\"" + this.getSolutionsByRowAsCachedRowSet().getString("id") + "\"" + selectedAttribute + ">");

									 resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable.concat(this.getSolutionsByRowAsCachedRowSet().getString("description"));
									 
									 resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable.concat("</option>");
								}
									 
								resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable.concat("</select>");
								
								
							} else {
								
								resolutionsByRowAsDecoratedXHTMLTable = "There are no solutions by row.";
								
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					 
						if (this.getResolutionsByRowAsCachedRowSet().getString("constant") != null) {
							resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable.concat("<label for=\"constant-" + this.getResolutionsByRowAsCachedRowSet().getInt("column_number") + "\">Constant:</label><input type=\"text\" name=\"constant-" + this.getResolutionsByRowAsCachedRowSet().getInt("column_number") + "\" value=\"" + this.getResolutionsByRowAsCachedRowSet().getString("constant") + "\"/>");
						} else {
							resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable.concat("<label for=\"constant-" + this.getResolutionsByRowAsCachedRowSet().getInt("column_number") + "\" style=\"display:none;\">Constant:</label><input type=\"text\" name=\"constant-" + this.getResolutionsByRowAsCachedRowSet().getInt("column_number") + "\" value=\"\" style=\"display:none;\"/>");
						}
						
					 resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable.concat("</td>");
					 
					 
					 resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable.concat("</tr>");
				}
					 
				resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable.concat("</tbody>");
				 
				resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable.concat("</table>");
				
				resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable.concat("<div>TODO: paging</div>");
				resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable.concat("<div>TODO: Show solved columns <input type=\"checkbox\"/></div>");
				
			} else {
				
				resolutionsByRowAsDecoratedXHTMLTable = "There are no conflicts.";
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable;
	}


	public CachedRowSet getResolutionsByRowAsCachedRowSet() {
		return this.resolutionsByRowAsCachedRowSet;
	}

	public String getResolutionsByRowAsDecoratedXHTMLTable() {
		return this.resolutionsByRowAsDecoratedXHTMLTable;
	}

	public void setSolutionsByRowAsCachedRowSet(
			CachedRowSet solutionsByRowAsCachedRowSet) {
		this.solutionsByRowAsCachedRowSet = solutionsByRowAsCachedRowSet;
		
	}



	public CachedRowSet getSolutionsByRowAsCachedRowSet() {
		return this.solutionsByRowAsCachedRowSet;
	}


}
