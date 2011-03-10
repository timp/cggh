package org.cggh.tools.dataMerger.functions.resolutionsByCell;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.rowset.CachedRowSet;



public class ResolutionsByCellFunctionsModel implements java.io.Serializable {





	/**
	 * 
	 */
	private static final long serialVersionUID = 8062791815977978265L;

	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.functions.resolutionsByCell");
	
	private CachedRowSet resolutionsByCellAsCachedRowSet;
	private String resolutionsByCellAsDecoratedXHTMLTable;
	private CachedRowSet solutionsByRowAsCachedRowSet;
	private HashMap<Integer, String> joinColumnNamesByColumnNumberAsHashMap;

	public ResolutionsByCellFunctionsModel () {
		
	}

	public void setResolutionsByCellAsCachedRowSet(
			CachedRowSet resolutionsByCellAsCachedRowSet) {
		this.resolutionsByCellAsCachedRowSet = resolutionsByCellAsCachedRowSet;
	}

	//TODO
	public void setResolutionsByCellAsDecoratedXHTMLTableUsingResolutionsByCellAsCachedRowSet() {
		
		String resolutionsByCellAsDecoratedXHTMLTable = null;
		
		
		try {
			
			//Make sure we are at the beginning of the RowSet.
			this.getResolutionsByCellAsCachedRowSet().beforeFirst();
			
			if (this.getResolutionsByCellAsCachedRowSet().next()) {

				resolutionsByCellAsDecoratedXHTMLTable = "";
				
				resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat("<table class=\"resolutions-by-row-table\">");

				 resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat("<thead>");
				 resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat("<tr>");
				 
				 Pattern joinedKeytableKeyColumnNamePattern = Pattern.compile("^key_column_(\\d+)$");
				 
				 
		          for (int i = 1; i <= this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnCount(); i++) {

		        		  Matcher joinedKeytableKeyColumnNamePatternMatcher = joinedKeytableKeyColumnNamePattern.matcher(this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnName(i));
		        		  
		        		  if (joinedKeytableKeyColumnNamePatternMatcher.find()) {
		        			  
			        		  Integer columnNumber = Integer.parseInt(joinedKeytableKeyColumnNamePatternMatcher.group(1));
			        		  
			        		  this.logger.info("Got columnNumber: " + columnNumber);
			        		  
			        		  resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat("<th rowspan=\"2\">" + this.getJoinColumnNamesByColumnNumberAsHashMap().get(columnNumber) + "</th>");
			        		  
		        		  }

		          }
				 
				 
				 resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat("<th rowspan=\"2\">Solution</th>");
				 
				 
				 Pattern resolutionsByCellAsCachedRowSetSource1ColumnNamePattern = Pattern.compile("^column_(\\d+)_source_1$");
				 
				 this.logger.info("Searching for non-key cross-datatable columns by label...");
				 
		          for (int i = 1; i <= this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnCount(); i++) {

		        		  Matcher resolutionsByCellAsCachedRowSetColumnNamePatternMatcher = resolutionsByCellAsCachedRowSetSource1ColumnNamePattern.matcher(this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnLabel(i));
		        		  
		        		  this.logger.info("Looking at column label: " +  this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnLabel(i));
		        		  
		        		  if (resolutionsByCellAsCachedRowSetColumnNamePatternMatcher.find()) {
		        		  
			        		  Integer columnNumber = Integer.parseInt(resolutionsByCellAsCachedRowSetColumnNamePatternMatcher.group(1));
			        		  
			        		  this.logger.info("Got columnNumber: " + columnNumber);
			        		  
			        		  resolutionsByCellAsDecoratedXHTMLTable += "<th colspan=\"2\">" + this.getJoinColumnNamesByColumnNumberAsHashMap().get(columnNumber) + "</th>";
		        		  
		        		  }
		        	  
		          }
				 

				 resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat("</tr>");
				 
				 resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat("<tr>");



		          for (int i = 1; i <= this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnCount(); i++) {
		        	  
		        	  Matcher resolutionsByCellAsCachedRowSetColumnNamePatternMatcher = resolutionsByCellAsCachedRowSetSource1ColumnNamePattern.matcher(this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnLabel(i));
		        	  
		        	  if (resolutionsByCellAsCachedRowSetColumnNamePatternMatcher.find()) {

		        		  resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat("<th>1</th><th>2</th>");
		        	  
		        	  }
		          }
				 

				 resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat("</tr>");
				 
				 resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat("</thead>");
				 
				 resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat("<tbody>");
				 
				//because next() skips the first row.
				 this.getResolutionsByCellAsCachedRowSet().beforeFirst();

				while (this.getResolutionsByCellAsCachedRowSet().next()) {
					 
					resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat("<tr>");

			          for (int i = 1; i <= this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnCount(); i++) {
			        	  
			        	  if (this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnName(i).startsWith("key_column_")) {
			        		  
			        		  resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat("<td>" + this.getResolutionsByCellAsCachedRowSet().getString(this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnName(i)) + "</td>");
			        	  
			        	  }
			          }
					
					 resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat("<td>");
					 
					 resolutionsByCellAsDecoratedXHTMLTable += "<input type=\"hidden\" name=\"joined_keytable_id\" value=\"" + this.getResolutionsByCellAsCachedRowSet().getString("joined_keytable_id") + "\" />";
					 
						try {
							
							//because the cursor may have been moved to the end previously (we are in a loop).
							 this.getSolutionsByCellAsCachedRowSet().beforeFirst();
							
							if (this.getSolutionsByCellAsCachedRowSet().next()) {
								
								resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat("<select name=\"solution_by_row_id\">");

								//FIXME: Should the null option be in the table?
								
								resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat("<option value=\"\">");

								 resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat("Unresolved");
								 
								 resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat("</option>");				
								 
								//because next() skips the first row.
								 this.getSolutionsByCellAsCachedRowSet().beforeFirst();

								while (this.getSolutionsByCellAsCachedRowSet().next()) {
									
									String selectedAttribute = "";
									
									if (this.getResolutionsByCellAsCachedRowSet().getInt("solution_by_row_id") == this.getSolutionsByCellAsCachedRowSet().getInt("id")) {
										selectedAttribute = " selected=\"selected\"";
									}
									
									resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat("<option value=\"" + this.getSolutionsByCellAsCachedRowSet().getString("id") + "\"" + selectedAttribute + ">");

									 resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat(this.getSolutionsByCellAsCachedRowSet().getString("description"));
									 
									 resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat("</option>");
								}
									 
								resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat("</select>");
								
								
							} else {
								
								resolutionsByCellAsDecoratedXHTMLTable = "There are no solutions by row.";
								
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					 
						if (this.getResolutionsByCellAsCachedRowSet().getString("constant") != null) {
							resolutionsByCellAsDecoratedXHTMLTable += "<label for=\"constant-" + this.getResolutionsByCellAsCachedRowSet().getInt("joined_keytable_id") + "\">Constant:</label><input type=\"text\" name=\"constant-" + this.getResolutionsByCellAsCachedRowSet().getInt("joined_keytable_id") + "\" value=\"" + this.getResolutionsByCellAsCachedRowSet().getString("constant") + "\"/>";
						} else {
							resolutionsByCellAsDecoratedXHTMLTable += "<label for=\"constant-" + this.getResolutionsByCellAsCachedRowSet().getInt("joined_keytable_id") + "\" style=\"display:none;\">Constant:</label><input type=\"text\" name=\"constant-" + this.getResolutionsByCellAsCachedRowSet().getInt("joined_keytable_id") + "\" value=\"\" style=\"display:none;\"/>";
						}
						
					 resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat("</td>");
					 
					 //TODO

					 this.logger.info("Searching for non-key cross-datatable columns by label (to be used for getting data)...");
					 
			          for (int i = 1; i <= this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnCount(); i++) {
			        	  
			        		  Matcher resolutionsByCellAsCachedRowSetColumnNamePatternMatcher = resolutionsByCellAsCachedRowSetSource1ColumnNamePattern.matcher(this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnLabel(i));
			        		  
			        		  this.logger.info("Looking at column label: " +  this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnLabel(i));
			        		  
			        		  if (resolutionsByCellAsCachedRowSetColumnNamePatternMatcher.find()) {
			        		  
				        		  Integer columnNumber = Integer.parseInt(resolutionsByCellAsCachedRowSetColumnNamePatternMatcher.group(1));
				        		  
				        		  this.logger.info("Got columnNumber: " + columnNumber);
				        		  
				        		  String columnLabelForSource2Column = "column_" + columnNumber.toString() + "_source_2";
				        		  
				        		  //FIXME: Annoying bug in CachedRowSet means we can't grab the column by its label. :-(
				        		  //Workaround: look for the column for source 2
				        		  
				        		  //TODO:
				        		  Integer columnIndexForSource2Column = i+1;
				        		  for (int j = 1; j <= this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnCount(); j++) {
				        			  
				        			  if (columnLabelForSource2Column.equals(this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnLabel(j))) {
				        			  
				        				  columnIndexForSource2Column = j;
				        				  break;
				        			  }
				        				  
				        		  }
				        		  

				        		  
				        		  // Concatenate both sources at the same time, to make sure they are displayed together

				        			  if (this.getResolutionsByCellAsCachedRowSet().getString(i).equals(this.getResolutionsByCellAsCachedRowSet().getString(columnIndexForSource2Column))) {

				        					 resolutionsByCellAsDecoratedXHTMLTable += "<td>" + this.getResolutionsByCellAsCachedRowSet().getString(i)  + "</td><td>" + this.getResolutionsByCellAsCachedRowSet().getString(columnIndexForSource2Column) + "</td>";

				        			  } else {
				        				  resolutionsByCellAsDecoratedXHTMLTable += "<td class=\"conflicting-data\">" + this.getResolutionsByCellAsCachedRowSet().getString(i)  + "</td><td class=\"conflicting-data\">" + this.getResolutionsByCellAsCachedRowSet().getString(columnIndexForSource2Column) + "</td>";
				        			  }
				        		  
			        		  }
			        	  
			          }
					 
					 
					 resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat("</tr>");
				}
					 
				resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat("</tbody>");
				 
				resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat("</table>");
				
				resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat("<!-- <div>TODO: paging</div> -->");
				resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat("<!-- <div>TODO: Show solved columns <input type=\"checkbox\"/></div> -->");
				
			} else {
				
				resolutionsByCellAsDecoratedXHTMLTable = "There are no conflicts.";
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable;
	}


	public CachedRowSet getResolutionsByCellAsCachedRowSet() {
		return this.resolutionsByCellAsCachedRowSet;
	}

	public String getResolutionsByCellAsDecoratedXHTMLTable() {
		return this.resolutionsByCellAsDecoratedXHTMLTable;
	}

	public void setSolutionsByCellAsCachedRowSet(
			CachedRowSet solutionsByRowAsCachedRowSet) {
		this.solutionsByRowAsCachedRowSet = solutionsByRowAsCachedRowSet;
		
	}



	public CachedRowSet getSolutionsByCellAsCachedRowSet() {
		return this.solutionsByRowAsCachedRowSet;
	}

	public void setJoinColumnNamesByColumnNumberAsHashMap(
			HashMap<Integer, String> joinColumnNamesByColumnNumberAsHashMap) {
		this.joinColumnNamesByColumnNumberAsHashMap = joinColumnNamesByColumnNumberAsHashMap;
	}
	public HashMap<Integer, String> getJoinColumnNamesByColumnNumberAsHashMap () {
		return this.joinColumnNamesByColumnNumberAsHashMap;
	}

}
