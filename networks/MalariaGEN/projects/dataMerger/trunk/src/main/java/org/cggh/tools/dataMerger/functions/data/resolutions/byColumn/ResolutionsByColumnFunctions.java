package org.cggh.tools.dataMerger.functions.data.resolutions.byColumn;

import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;



public class ResolutionsByColumnFunctions implements java.io.Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7663105154570885669L;
	private CachedRowSet resolutionsByColumnAsCachedRowSet;
	private String resolutionsByColumnAsDecoratedXHTMLTable;
	private CachedRowSet solutionsByColumnAsCachedRowSet;

	public ResolutionsByColumnFunctions () {
		
	}

	public void setResolutionsByColumnAsCachedRowSet(
			CachedRowSet resolutionsByColumnAsCachedRowSet) {
		this.resolutionsByColumnAsCachedRowSet = resolutionsByColumnAsCachedRowSet;
	}

	public void setResolutionsByColumnAsDecoratedXHTMLTableUsingResolutionsByColumnAsCachedRowSet() {
		
		StringBuffer resolutionsByColumnAsDecoratedXHTMLTable = null;
		
		
		try {
			
			//Make sure we are at the beginning of the RowSet.
			this.getResolutionsByColumnAsCachedRowSet().beforeFirst();
			
			if (this.getResolutionsByColumnAsCachedRowSet().next()) {

				resolutionsByColumnAsDecoratedXHTMLTable = new StringBuffer();
				
				resolutionsByColumnAsDecoratedXHTMLTable.append("<table class=\"resolutions-by-column-table\">");

				 resolutionsByColumnAsDecoratedXHTMLTable.append("<thead>");
				 resolutionsByColumnAsDecoratedXHTMLTable.append("<tr>");
				 
				 resolutionsByColumnAsDecoratedXHTMLTable.append("<th class=\"column_name-heading\">Column</th>");
				 
				 //TODO: colspan=2 along with link to view data (TODO)
				 resolutionsByColumnAsDecoratedXHTMLTable.append("<th class=\"problem-heading\">Problem</th>");
				 resolutionsByColumnAsDecoratedXHTMLTable.append("<th class=\"solution-heading\">Solution</th>");

				 resolutionsByColumnAsDecoratedXHTMLTable.append("</tr>");
				 resolutionsByColumnAsDecoratedXHTMLTable.append("</thead>");
				 
				 resolutionsByColumnAsDecoratedXHTMLTable.append("<tbody>");
				 
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
					
					resolutionsByColumnAsDecoratedXHTMLTable.append("<tr class=\"").append(rowStripeClassName).append(rowFirstClassName).append(rowLastClassName).append("\">");

					 resolutionsByColumnAsDecoratedXHTMLTable.append("<td class=\"column_name-container\"><input type=\"hidden\" name=\"column_number\" value=\"").append(this.getResolutionsByColumnAsCachedRowSet().getString("column_number")).append("\"/>").append(this.getResolutionsByColumnAsCachedRowSet().getString("column_name")).append("</td>");
					 
					 Integer solutionByColumnId = this.getResolutionsByColumnAsCachedRowSet().getInt("solution_by_column_id");
					 
					 if (solutionByColumnId == 0) {
						 resolutionsByColumnAsDecoratedXHTMLTable.append("<td class=\"problem_description-container unresolved-problem-container\"><input type=\"hidden\" name=\"conflict_id\" value=\"").append(this.getResolutionsByColumnAsCachedRowSet().getString("conflict_id")).append("\"/>").append(this.getResolutionsByColumnAsCachedRowSet().getInt("conflicts_count")).append(" ").append(this.getResolutionsByColumnAsCachedRowSet().getString("description") +"</td>");
					 } else {
						 resolutionsByColumnAsDecoratedXHTMLTable.append("<td class=\"problem_description-container\"><input type=\"hidden\" name=\"conflict_id\" value=\"").append(this.getResolutionsByColumnAsCachedRowSet().getString("conflict_id")).append("\"/>").append(this.getResolutionsByColumnAsCachedRowSet().getInt("conflicts_count")).append(" ").append(this.getResolutionsByColumnAsCachedRowSet().getString("description")).append("</td>");
					 }
					 
					 //TODO
					 //resolutionsByColumnAsDecoratedXHTMLTable.append("<td><a href=\"TODO\">[TODO: link to view]</td>");
					 
					 resolutionsByColumnAsDecoratedXHTMLTable.append("<td class=\"solution_by_column_id-container\">");
					 
						try {
							
							//because the cursor may have been moved to the end previously (we are in a loop).
							 this.getSolutionsByColumnAsCachedRowSet().beforeFirst();
							
							if (this.getSolutionsByColumnAsCachedRowSet().next()) {
								
								if (solutionByColumnId == 0) {
									resolutionsByColumnAsDecoratedXHTMLTable.append("<select class=\"unresolved\" name=\"solution_by_column_id\">");
								} else {
									resolutionsByColumnAsDecoratedXHTMLTable.append("<select name=\"solution_by_column_id\">");
								}

								//FIXME: Should the null option be in the table?
								
								resolutionsByColumnAsDecoratedXHTMLTable.append("<option value=\"\">");

								 resolutionsByColumnAsDecoratedXHTMLTable.append("Unresolved");
								 
								 resolutionsByColumnAsDecoratedXHTMLTable.append("</option>");				
								 
								//because next() skips the first row.
								 this.getSolutionsByColumnAsCachedRowSet().beforeFirst();

								 
								while (this.getSolutionsByColumnAsCachedRowSet().next()) {

									
									String selectedAttribute = "";
									
									if (this.getResolutionsByColumnAsCachedRowSet().getInt("solution_by_column_id") == this.getSolutionsByColumnAsCachedRowSet().getInt("id")) {
										selectedAttribute = " selected=\"selected\"";
									}
									
									resolutionsByColumnAsDecoratedXHTMLTable.append("<option value=\"").append(this.getSolutionsByColumnAsCachedRowSet().getString("id")).append("\"").append(selectedAttribute).append(">");

									 resolutionsByColumnAsDecoratedXHTMLTable.append(this.getSolutionsByColumnAsCachedRowSet().getString("description"));
									 
									 resolutionsByColumnAsDecoratedXHTMLTable.append("</option>");
								}
									 
								resolutionsByColumnAsDecoratedXHTMLTable.append("</select>");
								
								
							} else {
								
								resolutionsByColumnAsDecoratedXHTMLTable = new StringBuffer("There are no solutions by column.");
								
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					 
						if (this.getResolutionsByColumnAsCachedRowSet().getString("constant") != null) {
							resolutionsByColumnAsDecoratedXHTMLTable.append("<label for=\"constant-").append(this.getResolutionsByColumnAsCachedRowSet().getInt("column_number")).append("\">Constant:</label><input type=\"text\" name=\"constant-").append(this.getResolutionsByColumnAsCachedRowSet().getInt("column_number")).append("\" value=\"").append(this.getResolutionsByColumnAsCachedRowSet().getString("constant")).append("\"/>");
						} else {
							resolutionsByColumnAsDecoratedXHTMLTable.append("<label for=\"constant-").append(this.getResolutionsByColumnAsCachedRowSet().getInt("column_number")).append("\" style=\"display:none;\">Constant:</label><input type=\"text\" name=\"constant-").append(this.getResolutionsByColumnAsCachedRowSet().getInt("column_number")).append("\" value=\"\" style=\"display:none;\"/>");
						}
						
					 resolutionsByColumnAsDecoratedXHTMLTable.append("</td>");
					 
					 //TODO
					 //resolutionsByColumnAsDecoratedXHTMLTable.append("<td><a href=\"TODO\">[TODO: link to view]</td>");
					 
					 resolutionsByColumnAsDecoratedXHTMLTable.append("</tr>");
					 
					 rowFirstClassName = "";
				}
					 
				resolutionsByColumnAsDecoratedXHTMLTable.append("</tbody>");
				 
				resolutionsByColumnAsDecoratedXHTMLTable.append("</table>");
				
				resolutionsByColumnAsDecoratedXHTMLTable.append("<!--<div>TODO: paging</div>-->");
				
			} else {
				
				resolutionsByColumnAsDecoratedXHTMLTable = new StringBuffer("There are no unresolved conflicts.");
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnAsDecoratedXHTMLTable.toString();
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
