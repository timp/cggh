package org.cggh.tools.dataMerger.functions.resolutionsByColumn;

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
				
				resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("<table>");

				 resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("<thead>");
				 resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("<tr>");
				 
				 resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("<th>Column</th>");
				 resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("<th colspan=\"2\">Problem</th>");
				 resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("<th colspan=\"2\">Solution</th>");

				 resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("</tr>");
				 resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("</thead>");
				 
				 resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("<tbody>");
				 
				//because next() skips the first row.
				 this.getResolutionsByColumnAsCachedRowSet().beforeFirst();

				while (this.getResolutionsByColumnAsCachedRowSet().next()) {
					 
					resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("<tr>");

					 resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("<td><input type=\"hidden\" name=\"column_number\" value=\"" + this.getResolutionsByColumnAsCachedRowSet().getString("column_number") + "\"/>" + this.getResolutionsByColumnAsCachedRowSet().getString("column_name") + "</td>");
					 
					 resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("<td><input type=\"hidden\" name=\"problem_by_column_id\" value=\"" + this.getResolutionsByColumnAsCachedRowSet().getString("problem_by_column_id") + "\"/>" + this.getResolutionsByColumnAsCachedRowSet().getInt("conflicts_count") + " " + this.getResolutionsByColumnAsCachedRowSet().getString("description") + "</td>");
					 resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("<td><a href=\"TODO\">[TODO: link to view]</td>");
					 
					 resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("<td>");
					 
						try {
							
							//because the cursor may have been moved to the end previously (we are in a loop).
							 this.getSolutionsByColumnAsCachedRowSet().beforeFirst();
							
							if (this.getSolutionsByColumnAsCachedRowSet().next()) {
								
								resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("<select name=\"solution_by_column_id\">");

								//FIXME: Should the null option be in the table?
								
								resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("<option value=\"\">");

								 resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("Unresolved");
								 
								 resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("</option>");				
								 
								//because next() skips the first row.
								 this.getSolutionsByColumnAsCachedRowSet().beforeFirst();

								while (this.getSolutionsByColumnAsCachedRowSet().next()) {
									
									String selectedAttribute = "";
									
									if (this.getResolutionsByColumnAsCachedRowSet().getInt("solution_by_column_id") == this.getSolutionsByColumnAsCachedRowSet().getInt("id")) {
										selectedAttribute = " selected=\"selected\"";
									}
									
									resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("<option value=\"" + this.getSolutionsByColumnAsCachedRowSet().getString("id") + "\"" + selectedAttribute + ">");

									 resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat(this.getSolutionsByColumnAsCachedRowSet().getString("description"));
									 
									 resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("</option>");
								}
									 
								resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("</select>");
								
								
							} else {
								
								resolutionsByColumnAsDecoratedXHTMLTable = "There are no solutions by column.";
								
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					 
						if (this.getResolutionsByColumnAsCachedRowSet().getString("constant") != null) {
							resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("<label for=\"constant\">Constant:</label><input type=\"text\" name=\"constant\" value=\"" + this.getResolutionsByColumnAsCachedRowSet().getString("constant") + "\"/>");
						} else {
							resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("<label for=\"constant\">Constant:</label><input type=\"text\" name=\"constant\" value=\"\"/>");
						}
						
					 resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("</td>");
					 
					 
					 resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("<td><a href=\"TODO\">[TODO: link to view]</td>");
					 
					 resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("</tr>");
				}
					 
				resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("</tbody>");
				 
				resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("</table>");
				
				resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.concat("<div>TODO: paging</div>");
				
			} else {
				
				resolutionsByColumnAsDecoratedXHTMLTable = "There are no conflicts.";
				
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
