package org.cggh.tools.dataMerger.functions.resolutions.byRow;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.rowset.CachedRowSet;



public class ResolutionsByRowFunctionsModel implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 2410465698437549757L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.functions.resolutionsByRow");
	
	private CachedRowSet resolutionsByRowAsCachedRowSet;
	private String resolutionsByRowAsDecoratedXHTMLTable;
	private CachedRowSet solutionsByRowAsCachedRowSet;
	private HashMap<Integer, String> joinColumnNamesByColumnNumberAsHashMap;

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
				
				resolutionsByRowAsDecoratedXHTMLTable += "<table class=\"resolutions-by-row-table\">";

				 resolutionsByRowAsDecoratedXHTMLTable += "<thead>";
				 resolutionsByRowAsDecoratedXHTMLTable += "<tr>";
				 
				 Pattern joinedKeytableKeyColumnNamePattern = Pattern.compile("^key_column_(\\d+)$");
				 
				 
		          for (int i = 1; i <= this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnCount(); i++) {

		        		  Matcher joinedKeytableKeyColumnNamePatternMatcher = joinedKeytableKeyColumnNamePattern.matcher(this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnName(i));
		        		  
		        		  if (joinedKeytableKeyColumnNamePatternMatcher.find()) {
		        			  
			        		  Integer columnNumber = Integer.parseInt(joinedKeytableKeyColumnNamePatternMatcher.group(1));
			        		  
			        		  //this.logger.info("Got columnNumber: " + columnNumber);
			        		  
			        		  resolutionsByRowAsDecoratedXHTMLTable += "<th class=\"key-heading\" rowspan=\"2\">" + this.getJoinColumnNamesByColumnNumberAsHashMap().get(columnNumber) + "</th>";
			        		  
		        		  }

		          }
				 
				 
				 resolutionsByRowAsDecoratedXHTMLTable += "<th class=\"solution-heading\" rowspan=\"2\">Solution</th>";
				 
				 
				 Pattern resolutionsByRowAsCachedRowSetSource1ColumnNamePattern = Pattern.compile("^column_(\\d+)_source_1$");
				 
				 //this.logger.info("Searching for non-key cross-datatable columns by label...");
				 
		          for (int i = 1; i <= this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnCount(); i++) {

		        		  Matcher resolutionsByRowAsCachedRowSetColumnNamePatternMatcher = resolutionsByRowAsCachedRowSetSource1ColumnNamePattern.matcher(this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnLabel(i));
		        		  
		        		  //this.logger.info("Looking at column label: " +  this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnLabel(i));
		        		  
		        		  if (resolutionsByRowAsCachedRowSetColumnNamePatternMatcher.find()) {
		        		  
			        		  Integer columnNumber = Integer.parseInt(resolutionsByRowAsCachedRowSetColumnNamePatternMatcher.group(1));
			        		  
			        		  //this.logger.info("Got columnNumber: " + columnNumber);
			        		  
			        		  resolutionsByRowAsDecoratedXHTMLTable += "<th class=\"column-heading\" colspan=\"2\">" + this.getJoinColumnNamesByColumnNumberAsHashMap().get(columnNumber) + "</th>";
		        		  
		        		  }
		        	  
		          }
				 

				 resolutionsByRowAsDecoratedXHTMLTable += "</tr>";
				 
				 resolutionsByRowAsDecoratedXHTMLTable += "<tr>";



		          for (int i = 1; i <= this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnCount(); i++) {
		        	  
		        	  Matcher resolutionsByRowAsCachedRowSetColumnNamePatternMatcher = resolutionsByRowAsCachedRowSetSource1ColumnNamePattern.matcher(this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnLabel(i));
		        	  
		        	  if (resolutionsByRowAsCachedRowSetColumnNamePatternMatcher.find()) {

		        		  resolutionsByRowAsDecoratedXHTMLTable += "<th class=\"source_1_value-heading\">1</th><th class=\"source_2_value-heading\">2</th>";
		        	  
		        	  }
		          }
				 

				 resolutionsByRowAsDecoratedXHTMLTable += "</tr>";
				 
				 resolutionsByRowAsDecoratedXHTMLTable += "</thead>";
				 
				 resolutionsByRowAsDecoratedXHTMLTable += "<tbody>";
				 
				//because next() skips the first row.
				 this.getResolutionsByRowAsCachedRowSet().beforeFirst();

					String rowStripeClassName = "even "; 
					String rowFirstClassName = "first ";
					String rowLastClassName = ""; 	
				 
				while (this.getResolutionsByRowAsCachedRowSet().next()) {

					if (rowStripeClassName == "odd ") {
						rowStripeClassName = "even ";
					} else {
						rowStripeClassName = "odd ";
					}
					
					//TODO: This might need changing when paging.
					if (this.getResolutionsByRowAsCachedRowSet().isLast()) {
						rowLastClassName = "last ";
					}					
					
					resolutionsByRowAsDecoratedXHTMLTable += "<tr class=\"" + rowStripeClassName + rowFirstClassName + rowLastClassName + "\">";

			          for (int i = 1; i <= this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnCount(); i++) {
			        	  
			        	  if (this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnName(i).startsWith("key_column_")) {
			        		  
			        		  resolutionsByRowAsDecoratedXHTMLTable += "<td>" + this.getResolutionsByRowAsCachedRowSet().getString(this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnName(i)) + "</td>";
			        	  
			        	  }
			          }
					
					 resolutionsByRowAsDecoratedXHTMLTable += "<td class=\"solution_by_row_id-container\">";
					 
					 resolutionsByRowAsDecoratedXHTMLTable += "<input type=\"hidden\" name=\"joined_keytable_id\" value=\"" + this.getResolutionsByRowAsCachedRowSet().getString("joined_keytable_id") + "\" />";
					 
						try {
							
							//because the cursor may have been moved to the end previously (we are in a loop).
							 this.getSolutionsByRowAsCachedRowSet().beforeFirst();
							
							if (this.getSolutionsByRowAsCachedRowSet().next()) {
								
								
								if (this.getResolutionsByRowAsCachedRowSet().getInt("solution_by_row_id") == 0) {
									resolutionsByRowAsDecoratedXHTMLTable += "<select name=\"solution_by_row_id\" class=\"unresolved\">";
								} else {
									resolutionsByRowAsDecoratedXHTMLTable += "<select name=\"solution_by_row_id\">";
								}

								//FIXME: Should the null option be in the table?
								
								resolutionsByRowAsDecoratedXHTMLTable += "<option class=\"unresolved-option\" value=\"\">";

								 resolutionsByRowAsDecoratedXHTMLTable += "Unresolved";
								 
								 resolutionsByRowAsDecoratedXHTMLTable += "</option>";				
								 
								//because next() skips the first row.
								 this.getSolutionsByRowAsCachedRowSet().beforeFirst();

								while (this.getSolutionsByRowAsCachedRowSet().next()) {
									
									String selectedAttribute = "";
									
									if (this.getResolutionsByRowAsCachedRowSet().getInt("solution_by_row_id") == this.getSolutionsByRowAsCachedRowSet().getInt("id")) {
										selectedAttribute = " selected=\"selected\"";
									}
									
									resolutionsByRowAsDecoratedXHTMLTable += "<option value=\"" + this.getSolutionsByRowAsCachedRowSet().getString("id") + "\"" + selectedAttribute + ">";

									 resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable.concat(this.getSolutionsByRowAsCachedRowSet().getString("description"));
									 
									 resolutionsByRowAsDecoratedXHTMLTable += "</option>";
								}
									 
								resolutionsByRowAsDecoratedXHTMLTable += "</select>";
								
								
							} else {
								
								resolutionsByRowAsDecoratedXHTMLTable = "There are no solutions by row.";
								
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					 
						if (this.getResolutionsByRowAsCachedRowSet().getString("constant") != null) {
							resolutionsByRowAsDecoratedXHTMLTable += "<label for=\"constant-" + this.getResolutionsByRowAsCachedRowSet().getInt("joined_keytable_id") + "\">Constant:</label><input type=\"text\" name=\"constant-" + this.getResolutionsByRowAsCachedRowSet().getInt("joined_keytable_id") + "\" value=\"" + this.getResolutionsByRowAsCachedRowSet().getString("constant") + "\"/>";
						} else {
							resolutionsByRowAsDecoratedXHTMLTable += "<label for=\"constant-" + this.getResolutionsByRowAsCachedRowSet().getInt("joined_keytable_id") + "\" style=\"display:none;\">Constant:</label><input type=\"text\" name=\"constant-" + this.getResolutionsByRowAsCachedRowSet().getInt("joined_keytable_id") + "\" value=\"\" style=\"display:none;\"/>";
						}
						
					 resolutionsByRowAsDecoratedXHTMLTable += "</td>";
					 
					 //TODO

					 //this.logger.info("Searching for non-key cross-datatable columns by label (to be used for getting data)...");
					 
			          for (int i = 1; i <= this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnCount(); i++) {
			        	  
			        		  Matcher resolutionsByRowAsCachedRowSetColumnNamePatternMatcher = resolutionsByRowAsCachedRowSetSource1ColumnNamePattern.matcher(this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnLabel(i));
			        		  
			        		  //this.logger.info("Looking at column label: " +  this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnLabel(i));
			        		  
			        		  if (resolutionsByRowAsCachedRowSetColumnNamePatternMatcher.find()) {
			        		  
				        		  Integer columnNumber = Integer.parseInt(resolutionsByRowAsCachedRowSetColumnNamePatternMatcher.group(1));
				        		  
				        		  //this.logger.info("Got columnNumber: " + columnNumber);
				        		  
				        		  String columnLabelForSource2Column = "column_" + columnNumber.toString() + "_source_2";
				        		  
				        		  //FIXME: Annoying bug in CachedRowSet means we can't grab the column by its label. :-(
				        		  //Workaround: look for the column for source 2
				        		  
				        		  //TODO:
				        		  Integer columnIndexForSource2Column = i+1;
				        		  for (int j = 1; j <= this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnCount(); j++) {
				        			  
				        			  if (columnLabelForSource2Column.equals(this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnLabel(j))) {
				        			  
				        				  columnIndexForSource2Column = j;
				        				  break;
				        			  }
				        				  
				        		  }
				        		  

				        		  
				        		  // Concatenate both sources at the same time, to make sure they are displayed together

				        		  
				        		  Integer solutionByRowId = this.getResolutionsByRowAsCachedRowSet().getInt("solution_by_row_id");
				        		  
				        			  if (this.getResolutionsByRowAsCachedRowSet().getString(i).equals(this.getResolutionsByRowAsCachedRowSet().getString(columnIndexForSource2Column)) || solutionByRowId != 0) {

				        					 resolutionsByRowAsDecoratedXHTMLTable += "<td class=\"source_1_value-container\">" + this.getResolutionsByRowAsCachedRowSet().getString(i)  + "</td><td class=\"source_2_value-container\">" + this.getResolutionsByRowAsCachedRowSet().getString(columnIndexForSource2Column) + "</td>";

				        			  } else {
				        				  
				        				  resolutionsByRowAsDecoratedXHTMLTable += "<td class=\"source_1_value-container conflicting-data\">" + this.getResolutionsByRowAsCachedRowSet().getString(i)  + "</td><td class=\"source_2_value-container conflicting-data\">" + this.getResolutionsByRowAsCachedRowSet().getString(columnIndexForSource2Column) + "</td>";
				        			  }
				        		  
			        		  }
			        	  
			          }
					 
					 
					 resolutionsByRowAsDecoratedXHTMLTable += "</tr>";
					 
					 rowFirstClassName = "";
				}
					 
				resolutionsByRowAsDecoratedXHTMLTable += "</tbody>";
				 
				resolutionsByRowAsDecoratedXHTMLTable += "</table>";
				
				resolutionsByRowAsDecoratedXHTMLTable += "<!-- <div>TODO: paging</div> -->";
				resolutionsByRowAsDecoratedXHTMLTable += "<!-- <div>TODO: Show solved columns <input type=\"checkbox\"/></div> -->";
				
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

	public void setJoinColumnNamesByColumnNumberAsHashMap(
			HashMap<Integer, String> joinColumnNamesByColumnNumberAsHashMap) {
		this.joinColumnNamesByColumnNumberAsHashMap = joinColumnNamesByColumnNumberAsHashMap;
	}
	public HashMap<Integer, String> getJoinColumnNamesByColumnNumberAsHashMap () {
		return this.joinColumnNamesByColumnNumberAsHashMap;
	}

}
