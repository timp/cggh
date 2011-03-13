package org.cggh.tools.dataMerger.functions.resolutions.byCell;

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
	
	private HashMap<Integer, Integer> unresolvedByColumnOrRowConflictsCountUsingColumnNumberAsHashMap;
	private HashMap<String, Boolean> unresolvedByColumnOrRowStatusUsingCellCoordsAsHashMap;

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
				
				resolutionsByCellAsDecoratedXHTMLTable += "<table class=\"resolutions-by-cell-table\">";

				 resolutionsByCellAsDecoratedXHTMLTable += "<thead>";
				 resolutionsByCellAsDecoratedXHTMLTable += "<tr>";
				 
				 Pattern joinedKeytableKeyColumnNamePattern = Pattern.compile("^key_column_(\\d+)$");
				 
				 
		          for (int i = 1; i <= this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnCount(); i++) {

		        		  Matcher joinedKeytableKeyColumnNamePatternMatcher = joinedKeytableKeyColumnNamePattern.matcher(this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnName(i));
		        		  
		        		  if (joinedKeytableKeyColumnNamePatternMatcher.find()) {
		        			  
			        		  Integer columnNumber = Integer.parseInt(joinedKeytableKeyColumnNamePatternMatcher.group(1));
			        		  
			        		  //this.logger.info("Got columnNumber: " + columnNumber);
			        		  
			        		  resolutionsByCellAsDecoratedXHTMLTable += "<th class=\"key-heading\" rowspan=\"2\">" + this.getJoinColumnNamesByColumnNumberAsHashMap().get(columnNumber) + "</th>";
			        		  
		        		  }

		          }
				 
				  
				 Pattern resolutionsByCellAsCachedRowSetSource1ColumnNamePattern = Pattern.compile("^column_(\\d+)_source_1$");
				 
				 //this.logger.info("Searching for non-key cross-datatable columns by label...");
				 
		          for (int i = 1; i <= this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnCount(); i++) {

		        		  Matcher resolutionsByCellAsCachedRowSetColumnNamePatternMatcher = resolutionsByCellAsCachedRowSetSource1ColumnNamePattern.matcher(this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnLabel(i));
		        		  
		        		  //this.logger.info("Looking at column label: " +  this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnLabel(i));
		        		  
		        		  if (resolutionsByCellAsCachedRowSetColumnNamePatternMatcher.find()) {
		        		  
			        		  Integer columnNumber = Integer.parseInt(resolutionsByCellAsCachedRowSetColumnNamePatternMatcher.group(1));
			        		  
			        		  //this.logger.info("Got columnNumber: " + columnNumber);
			        		  
			        		  if (this.getUnresolvedByColumnOrRowConflictsCountUsingColumnNumberAsHashMap().get(columnNumber) != null && this.getUnresolvedByColumnOrRowConflictsCountUsingColumnNumberAsHashMap().get(columnNumber) > 0) {
			        			  
			        			  resolutionsByCellAsDecoratedXHTMLTable += "<th class=\"column-heading\" colspan=\"3\">" + this.getJoinColumnNamesByColumnNumberAsHashMap().get(columnNumber) + "</th>";
			        			  
			        		  } else {
			        			  
			        			  resolutionsByCellAsDecoratedXHTMLTable += "<th class=\"column-heading\" colspan=\"2\">" + this.getJoinColumnNamesByColumnNumberAsHashMap().get(columnNumber) + "</th>";
			        		  }
			        		  
		        		  }
		        	  
		          }
				 

				 resolutionsByCellAsDecoratedXHTMLTable += "</tr>";
				 
				 resolutionsByCellAsDecoratedXHTMLTable += "<tr>";



		          for (int i = 1; i <= this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnCount(); i++) {
		        	  
		        	  Matcher resolutionsByCellAsCachedRowSetColumnNamePatternMatcher = resolutionsByCellAsCachedRowSetSource1ColumnNamePattern.matcher(this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnLabel(i));
		        	  
		        	  if (resolutionsByCellAsCachedRowSetColumnNamePatternMatcher.find()) {

		        		  resolutionsByCellAsDecoratedXHTMLTable += "<th class=\"source_1_value-heading\">1</th><th class=\"source_2_value-heading\">2</th>";
		        		  
		        		  Integer columnNumber = Integer.parseInt(resolutionsByCellAsCachedRowSetColumnNamePatternMatcher.group(1));
		        		  
		        		  if (this.getUnresolvedByColumnOrRowConflictsCountUsingColumnNumberAsHashMap().get(columnNumber) != null && this.getUnresolvedByColumnOrRowConflictsCountUsingColumnNumberAsHashMap().get(columnNumber) > 0) {
		        			  resolutionsByCellAsDecoratedXHTMLTable += "<th class=\"solution-heading\">Solution</th>";
		        		  }
		        	  
		        	  }
		          }
				 

				 resolutionsByCellAsDecoratedXHTMLTable += "</tr>";
				 
				 resolutionsByCellAsDecoratedXHTMLTable += "</thead>";
				 
				 resolutionsByCellAsDecoratedXHTMLTable += "<tbody>";
				 
				//because next() skips the first row.
				 this.getResolutionsByCellAsCachedRowSet().beforeFirst();

					String rowStripeClassName = "even "; 
					String rowFirstClassName = "first ";
					String rowLastClassName = ""; 	
				 
				while (this.getResolutionsByCellAsCachedRowSet().next()) {

					if (rowStripeClassName == "odd ") {
						rowStripeClassName = "even ";
					} else {
						rowStripeClassName = "odd ";
					}
					
					//TODO: This might need changing when paging.
					if (this.getResolutionsByCellAsCachedRowSet().isLast()) {
						rowLastClassName = "last ";
					}					
					
					resolutionsByCellAsDecoratedXHTMLTable += "<tr class=\"" + rowStripeClassName + rowFirstClassName + rowLastClassName + "\">";

					
			          for (int i = 1; i <= this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnCount(); i++) {
			        	  
			        	  if (this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnName(i).startsWith("key_column_")) {
			        		  
			        		  resolutionsByCellAsDecoratedXHTMLTable += "<td>" + this.getResolutionsByCellAsCachedRowSet().getString(this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnName(i)) + "</td>";
			        	  
			        	  }
			          }
					
					 
					 
					 //TODO

					 //this.logger.info("Searching for non-key cross-datatable columns by label (to be used for getting data)...");
					 
			          for (int i = 1; i <= this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnCount(); i++) {
			        	  

			        		  Matcher resolutionsByCellAsCachedRowSetColumnNamePatternMatcher = resolutionsByCellAsCachedRowSetSource1ColumnNamePattern.matcher(this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnLabel(i));
			        		  
			        		  //this.logger.info("Looking at column label: " +  this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnLabel(i));
			        		  
			        		  if (resolutionsByCellAsCachedRowSetColumnNamePatternMatcher.find()) {
			        		  
				        		  Integer columnNumber = Integer.parseInt(resolutionsByCellAsCachedRowSetColumnNamePatternMatcher.group(1));
				        		  
				        		  //this.logger.info("Got columnNumber: " + columnNumber);
				        		  
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

				        		  
				        		  //Integer solutionByCellId = this.getResolutionsByCellAsCachedRowSet().getInt("solution_by_cell_id");
				        		  
				        			  if (this.getResolutionsByCellAsCachedRowSet().getString(i).equals(this.getResolutionsByCellAsCachedRowSet().getString(columnIndexForSource2Column))) {

				        					 resolutionsByCellAsDecoratedXHTMLTable += "<td class=\"source_1_value-container\">" + this.getResolutionsByCellAsCachedRowSet().getString(i)  + "</td><td class=\"source_2_value-container\">" + this.getResolutionsByCellAsCachedRowSet().getString(columnIndexForSource2Column) + "</td>";

				        					 if (this.getUnresolvedByColumnOrRowConflictsCountUsingColumnNumberAsHashMap().get(columnNumber) != null && this.getUnresolvedByColumnOrRowConflictsCountUsingColumnNumberAsHashMap().get(columnNumber) > 0) {
				        					 
				        						 resolutionsByCellAsDecoratedXHTMLTable += "<td class=\"solution_by_cell_id-container\"><!-- No solution selector required here because there is no conflict --></td>";
				        					 
				        					 }
				        					 
				        					 
				        			  } else {
				        				
				        				  // There is a conflict
				        				  
				        				  if (this.getUnresolvedByColumnOrRowStatusUsingCellCoordsAsHashMap().get(this.getResolutionsByCellAsCachedRowSet().getInt("joined_keytable_id") + "," + columnNumber.toString()) != null) {
				        			  
				        					  // This conflict hasn't been resolved by column or row.
				        					  
					        					  	resolutionsByCellAsDecoratedXHTMLTable += "<td class=\"source_1_value-container\">" + this.getResolutionsByCellAsCachedRowSet().getString(i)  + "</td><td class=\"source_2_value-container conflicting-data\">" + this.getResolutionsByCellAsCachedRowSet().getString(columnIndexForSource2Column) + "</td>";
					        			  	  
					        				  
					        					  	resolutionsByCellAsDecoratedXHTMLTable += "<td class=\"solution_by_cell_id-container\">";
		        							
				        							//because the cursor may have been moved to the end previously (we are in a loop).
				        							this.getSolutionsByCellAsCachedRowSet().beforeFirst();
					        							
				        							if (this.getSolutionsByCellAsCachedRowSet().next()) {
				        								
				        								if (this.getResolutionsByCellAsCachedRowSet().getInt("solution_by_cell_id") == 0) {
				        									resolutionsByCellAsDecoratedXHTMLTable += "<select name=\"solution_by_cell_id-" + this.getResolutionsByCellAsCachedRowSet().getInt("joined_keytable_id") + "-" + columnNumber.toString() + "\" class=\"unresolved\">";
				        								} else {
				        									resolutionsByCellAsDecoratedXHTMLTable += "<select name=\"solution_by_cell_id-" + this.getResolutionsByCellAsCachedRowSet().getInt("joined_keytable_id") + "-" + columnNumber.toString() + "\">";
				        								}
			
				        								//FIXME: Should the null option be in the table?
				        								
				        								resolutionsByCellAsDecoratedXHTMLTable += "<option value=\"\">";
			
				        								 resolutionsByCellAsDecoratedXHTMLTable += "Unresolved";
				        								 
				        								 resolutionsByCellAsDecoratedXHTMLTable += "</option>";				
				        								 
				        								//because next() skips the first row.
				        								 this.getSolutionsByCellAsCachedRowSet().beforeFirst();
			
					        								while (this.getSolutionsByCellAsCachedRowSet().next()) {
					        									
					        									String selectedAttribute = "";
					        									
						        									if (this.getResolutionsByCellAsCachedRowSet().getInt("solution_by_cell_id") == this.getSolutionsByCellAsCachedRowSet().getInt("id")) {
						        										selectedAttribute = " selected=\"selected\"";
						        									}
					        									
					        									resolutionsByCellAsDecoratedXHTMLTable += "<option value=\"" + this.getSolutionsByCellAsCachedRowSet().getString("id") + "\"" + selectedAttribute + ">";
				
					        									 resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.concat(this.getSolutionsByCellAsCachedRowSet().getString("description"));
					        									 
					        									 resolutionsByCellAsDecoratedXHTMLTable += "</option>";
					        								}
				        									 
				        								resolutionsByCellAsDecoratedXHTMLTable += "</select>";
				        								
				        								
				        							} else {
				        								
				        								resolutionsByCellAsDecoratedXHTMLTable = "There are no solutions by cell.";
				        								
				        							}
	
				        					 
					        						if (this.getResolutionsByCellAsCachedRowSet().getString("constant") != null) {
					        							resolutionsByCellAsDecoratedXHTMLTable += "<label for=\"constant-" + this.getResolutionsByCellAsCachedRowSet().getInt("joined_keytable_id") + "-" + columnNumber.toString() + "\">Constant:</label><input type=\"text\" name=\"constant-" + this.getResolutionsByCellAsCachedRowSet().getInt("joined_keytable_id") + "-" + columnNumber.toString() + "\" value=\"" + this.getResolutionsByCellAsCachedRowSet().getString("constant") + "\"/>";
					        						} else {
					        							resolutionsByCellAsDecoratedXHTMLTable += "<label for=\"constant-" + this.getResolutionsByCellAsCachedRowSet().getInt("joined_keytable_id") + "-" + columnNumber.toString() + "\" style=\"display:none;\">Constant:</label><input type=\"text\" name=\"constant-" + this.getResolutionsByCellAsCachedRowSet().getInt("joined_keytable_id") +  "-" + columnNumber.toString() + "\" value=\"\" style=\"display:none;\"/>";
					        						}
								        						
						        					 resolutionsByCellAsDecoratedXHTMLTable += "</td>";
					        				  
				        					  
					        				  } else {
					        					  
					        					  // The conflict has been resolved by column or row.
					        					  
						        					 if (this.getUnresolvedByColumnOrRowConflictsCountUsingColumnNumberAsHashMap().get(columnNumber) != null && this.getUnresolvedByColumnOrRowConflictsCountUsingColumnNumberAsHashMap().get(columnNumber) > 0) {
							        				
						        						 // There are other conflicts to be resolved by cell
						        						 
						        						 resolutionsByCellAsDecoratedXHTMLTable += "<td class=\"source_1_value-container resolved-conflicting-data\">" + this.getResolutionsByCellAsCachedRowSet().getString(i)  + "</td><td class=\"source_2_value-container resolved-conflicting-data\">" + this.getResolutionsByCellAsCachedRowSet().getString(columnIndexForSource2Column) + "</td>";
						        						 
						        						 resolutionsByCellAsDecoratedXHTMLTable += "<td class=\"solution_by_cell_id-container\">Resolved</td>";
						        					 
						        					 } else {
						        						 
						        						 resolutionsByCellAsDecoratedXHTMLTable += "<td class=\"source_1_value-container resolved-conflicting-data\">" + this.getResolutionsByCellAsCachedRowSet().getString(i)  + "</td><td class=\"source_2_value-container conflicting-data\">" + this.getResolutionsByCellAsCachedRowSet().getString(columnIndexForSource2Column) + "</td>";
						        					 }
						        					 
						        					 
					        				  }	 
				        			  }
				        		  
			        		  }
			        	  
			          }
					 
					 
					 resolutionsByCellAsDecoratedXHTMLTable += "</tr>";
					 
					 rowFirstClassName = "";
				}
					 
				resolutionsByCellAsDecoratedXHTMLTable += "</tbody>";
				 
				resolutionsByCellAsDecoratedXHTMLTable += "</table>";
				
				resolutionsByCellAsDecoratedXHTMLTable += "<!-- <div>TODO: paging</div> -->";
				resolutionsByCellAsDecoratedXHTMLTable += "<!-- <div>TODO: Show solved columns <input type=\"checkbox\"/></div> -->";
				
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

	public void setUnresolvedByColumnOrRowConflictsCountUsingColumnNumberAsHashMap(
			HashMap<Integer, Integer> unresolvedByColumnOrRowConflictsCountUsingColumnNumberAsHashMap) {
		this.unresolvedByColumnOrRowConflictsCountUsingColumnNumberAsHashMap = unresolvedByColumnOrRowConflictsCountUsingColumnNumberAsHashMap;
	}

	public HashMap<Integer, Integer> getUnresolvedByColumnOrRowConflictsCountUsingColumnNumberAsHashMap() {
		return unresolvedByColumnOrRowConflictsCountUsingColumnNumberAsHashMap;
	}

	public void setUnresolvedByColumnOrRowStatusUsingCellCoordsAsHashMap(
			HashMap<String, Boolean> unresolvedByColumnOrRowStatusUsingCellCoordsAsHashMap) {
		this.unresolvedByColumnOrRowStatusUsingCellCoordsAsHashMap = unresolvedByColumnOrRowStatusUsingCellCoordsAsHashMap;
	}

	public HashMap<String, Boolean> getUnresolvedByColumnOrRowStatusUsingCellCoordsAsHashMap() {
		return unresolvedByColumnOrRowStatusUsingCellCoordsAsHashMap;
	}

	
}