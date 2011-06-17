package org.cggh.tools.dataMerger.functions.data.resolutions.byRow;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.rowset.CachedRowSet;



public class ResolutionsByRowFunctions implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 2410465698437549757L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.functions.data.resolutions.byRow");
	
	private CachedRowSet resolutionsByRowAsCachedRowSet;
	private String resolutionsByRowAsDecoratedXHTMLTable;
	private CachedRowSet solutionsByRowAsCachedRowSet;
	private HashMap<Integer, String> joinColumnNamesByColumnNumberAsHashMap;
	
	private HashMap<String, Integer> solutionByColumnIdUsingCellCoordsAsHashMap;
	private HashMap<String, Integer> solutionByCellIdUsingCellCoordsAsHashMap;
	private HashMap<String, String> constantUsingCellCoordsAsHashMap;
	private HashMap<Integer, Boolean> nullOrConstantSolutionUsingColumnNumberAsHashMap;

	public ResolutionsByRowFunctions () {
		
	}

	public void setResolutionsByRowAsCachedRowSet(
			CachedRowSet resolutionsByRowAsCachedRowSet) {
		this.resolutionsByRowAsCachedRowSet = resolutionsByRowAsCachedRowSet;
	}

	//TODO
	public void setResolutionsByRowAsDecoratedXHTMLTableUsingResolutionsByRowAsCachedRowSet() {
		
		StringBuffer resolutionsByRowAsDecoratedXHTMLTable = null;
		
		
		try {
			
			//Make sure we are at the beginning of the RowSet.
			this.getResolutionsByRowAsCachedRowSet().beforeFirst();
			
			if (this.getResolutionsByRowAsCachedRowSet().next()) {

				resolutionsByRowAsDecoratedXHTMLTable = new StringBuffer();
				
				resolutionsByRowAsDecoratedXHTMLTable.append("<table class=\"resolutions-by-row-table\">");

				 resolutionsByRowAsDecoratedXHTMLTable.append("<thead>");
				 resolutionsByRowAsDecoratedXHTMLTable.append("<tr>");
				 
				 Pattern joinedKeytableKeyColumnNamePattern = Pattern.compile("^key_column_(\\d+)$");
				 
				 
		          for (int i = 1; i <= this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnCount(); i++) {

		        		  Matcher joinedKeytableKeyColumnNamePatternMatcher = joinedKeytableKeyColumnNamePattern.matcher(this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnName(i));
		        		  
		        		  if (joinedKeytableKeyColumnNamePatternMatcher.find()) {
		        			  
			        		  Integer columnNumber = Integer.parseInt(joinedKeytableKeyColumnNamePatternMatcher.group(1));
			        		  
			        		  ////this.logger.info("Got columnNumber: ").append(columnNumber);
			        		  
			        		  resolutionsByRowAsDecoratedXHTMLTable.append("<th class=\"key-heading\" rowspan=\"2\">").append(this.getJoinColumnNamesByColumnNumberAsHashMap().get(columnNumber)).append("</th>");
			        		  
		        		  }

		          }
				 
				 
				 resolutionsByRowAsDecoratedXHTMLTable.append("<th class=\"solution-heading\" rowspan=\"2\">Solution</th>");
				 
				 
				 Pattern resolutionsByRowAsCachedRowSetSource1ColumnNamePattern = Pattern.compile("^column_(\\d+)_source_1$");
				 
				 ////this.logger.info("Searching for non-key cross-datatable columns by label...");
				 
		          for (int i = 1; i <= this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnCount(); i++) {

		        		  Matcher resolutionsByRowAsCachedRowSetColumnNamePatternMatcher = resolutionsByRowAsCachedRowSetSource1ColumnNamePattern.matcher(this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnLabel(i));
		        		  
		        		  ////this.logger.info("Looking at column label: ").append( this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnLabel(i));
		        		  
		        		  if (resolutionsByRowAsCachedRowSetColumnNamePatternMatcher.find()) {
		        		  
			        		  Integer columnNumber = Integer.parseInt(resolutionsByRowAsCachedRowSetColumnNamePatternMatcher.group(1));
			        		  
			        		  ////this.logger.info("Got columnNumber: ").append(columnNumber);
			        		  
			        		  

			        		  if (this.getNullOrConstantSolutionUsingColumnNumberAsHashMap().get(columnNumber) != null) {
			        			  resolutionsByRowAsDecoratedXHTMLTable.append("<th class=\"column-heading\" colspan=\"3\">").append(this.getJoinColumnNamesByColumnNumberAsHashMap().get(columnNumber)).append("</th>");
			        		  } else {
			        			  resolutionsByRowAsDecoratedXHTMLTable.append("<th class=\"column-heading\" colspan=\"2\">").append(this.getJoinColumnNamesByColumnNumberAsHashMap().get(columnNumber)).append("</th>");  
			        		  }
		        		  }
		        	  
		          }
				 

				 resolutionsByRowAsDecoratedXHTMLTable.append("</tr>");
				 
				 resolutionsByRowAsDecoratedXHTMLTable.append("<tr>");



		          for (int i = 1; i <= this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnCount(); i++) {
		        	  
		        	  Matcher resolutionsByRowAsCachedRowSetColumnNamePatternMatcher = resolutionsByRowAsCachedRowSetSource1ColumnNamePattern.matcher(this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnLabel(i));
		        	  
		        	  if (resolutionsByRowAsCachedRowSetColumnNamePatternMatcher.find()) {

		        		  resolutionsByRowAsDecoratedXHTMLTable.append("<th class=\"source_1_value-heading\">1</th><th class=\"source_2_value-heading\">2</th>");
		        	  
		        		  Integer columnNumber = Integer.parseInt(resolutionsByRowAsCachedRowSetColumnNamePatternMatcher.group(1));
		        		  
		        		  if (this.getNullOrConstantSolutionUsingColumnNumberAsHashMap().get(columnNumber) != null) {
		        			  resolutionsByRowAsDecoratedXHTMLTable.append("<th class=\"constant-heading\">Constant</th>");
		        		  }
		        		  
		        	  }
		          }
				 

				 resolutionsByRowAsDecoratedXHTMLTable.append("</tr>");
				 
				 resolutionsByRowAsDecoratedXHTMLTable.append("</thead>");
				 
				 resolutionsByRowAsDecoratedXHTMLTable.append("<tbody>");
				 
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
					
					resolutionsByRowAsDecoratedXHTMLTable.append("<tr class=\"").append(rowStripeClassName).append(rowFirstClassName).append(rowLastClassName).append("\">");

			          for (int i = 1; i <= this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnCount(); i++) {
			        	  
			        	  if (this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnName(i).startsWith("key_column_")) {
			        		  
			        		  resolutionsByRowAsDecoratedXHTMLTable.append("<td>").append(this.getResolutionsByRowAsCachedRowSet().getString(this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnName(i))).append("</td>");
			        	  
			        	  }
			          }
					
					 resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"solution_by_row_id-container\">");
					 
					 resolutionsByRowAsDecoratedXHTMLTable.append("<input type=\"hidden\" name=\"joined_keytable_id\" value=\"").append(this.getResolutionsByRowAsCachedRowSet().getString("joined_keytable_id")).append("\" />");
					 
						try {
							
							//because the cursor may have been moved to the end previously (we are in a loop).
							 this.getSolutionsByRowAsCachedRowSet().beforeFirst();
							
							if (this.getSolutionsByRowAsCachedRowSet().next()) {
								
								
								if (this.getResolutionsByRowAsCachedRowSet().getInt("solution_by_row_id") == 0) {
									resolutionsByRowAsDecoratedXHTMLTable.append("<select name=\"solution_by_row_id\" class=\"unresolved\">");
								} else {
									resolutionsByRowAsDecoratedXHTMLTable.append("<select name=\"solution_by_row_id\">");
								}

								//FIXME: Should the null option be in the table?
								
								resolutionsByRowAsDecoratedXHTMLTable.append("<option class=\"unresolved-option\" value=\"\">");

								 resolutionsByRowAsDecoratedXHTMLTable.append("Unresolved");
								 
								 resolutionsByRowAsDecoratedXHTMLTable.append("</option>");				
								 
								//because next() skips the first row.
								 this.getSolutionsByRowAsCachedRowSet().beforeFirst();

								while (this.getSolutionsByRowAsCachedRowSet().next()) {
									
									String selectedAttribute = "";
									
									if (this.getResolutionsByRowAsCachedRowSet().getInt("solution_by_row_id") == this.getSolutionsByRowAsCachedRowSet().getInt("id")) {
										selectedAttribute = " selected=\"selected\"";
									}
									
									resolutionsByRowAsDecoratedXHTMLTable.append("<option value=\"").append(this.getSolutionsByRowAsCachedRowSet().getString("id")).append("\"").append(selectedAttribute).append(">");

									 resolutionsByRowAsDecoratedXHTMLTable.append(this.getSolutionsByRowAsCachedRowSet().getString("description"));
									 
									 resolutionsByRowAsDecoratedXHTMLTable.append("</option>");
								}
									 
								resolutionsByRowAsDecoratedXHTMLTable.append("</select>");
								
								
							} else {
								
								resolutionsByRowAsDecoratedXHTMLTable = new StringBuffer("There are no solutions by row.");
								
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					 
						if (this.getResolutionsByRowAsCachedRowSet().getString("constant") != null) {
							resolutionsByRowAsDecoratedXHTMLTable.append("<label for=\"constant-").append(this.getResolutionsByRowAsCachedRowSet().getInt("joined_keytable_id")).append("\">Constant:</label><input type=\"text\" name=\"constant-").append(this.getResolutionsByRowAsCachedRowSet().getInt("joined_keytable_id")).append("\" value=\"").append(this.getResolutionsByRowAsCachedRowSet().getString("constant")).append("\"/>");
						} else {
							resolutionsByRowAsDecoratedXHTMLTable.append("<label for=\"constant-").append(this.getResolutionsByRowAsCachedRowSet().getInt("joined_keytable_id")).append("\" style=\"display:none;\">Constant:</label><input type=\"text\" name=\"constant-").append(this.getResolutionsByRowAsCachedRowSet().getInt("joined_keytable_id")).append("\" value=\"\" style=\"display:none;\"/>");
						}
						
					 resolutionsByRowAsDecoratedXHTMLTable.append("</td>");

					 ////this.logger.info("Searching for non-key cross-datatable columns by label (to be used for getting data)...");
					 
			          for (int i = 1; i <= this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnCount(); i++) {
			        	  
			        		  Matcher resolutionsByRowAsCachedRowSetColumnNamePatternMatcher = resolutionsByRowAsCachedRowSetSource1ColumnNamePattern.matcher(this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnLabel(i));
			        		  
			        		  ////this.logger.info("Looking at column label: ").append( this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnLabel(i));
			        		  
			        		  if (resolutionsByRowAsCachedRowSetColumnNamePatternMatcher.find()) {
			        		  
				        		  Integer columnNumber = Integer.parseInt(resolutionsByRowAsCachedRowSetColumnNamePatternMatcher.group(1));
				        		  
				        		  ////this.logger.info("Got columnNumber: ").append(columnNumber);
				        		  
				        		  String columnLabelForSource2Column = "column_" + columnNumber.toString() + "_source_2";
				        		  
				        		  //FIXME: Annoying bug in CachedRowSet means we can't grab the column by its label. :-(
				        		  //Workaround: look for the column for source 2
				        		  
				        		  Integer columnIndexForSource2Column = i+1;
				        		  for (int j = 1; j <= this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnCount(); j++) {
				        			  
				        			  if (columnLabelForSource2Column.equals(this.getResolutionsByRowAsCachedRowSet().getMetaData().getColumnLabel(j))) {
				        			  
				        				  columnIndexForSource2Column = j;
				        				  break;
				        			  }
				        				  
				        		  }
				        		  

				        		  
				        		  // Concatenate both sources at the same time, to make sure they are displayed together

				        		  
				        		  //Integer solutionByRowId = this.getResolutionsByRowAsCachedRowSet().getInt("solution_by_row_id");
				        		  
				        		  //
				        		  //logger.info("this string: " + this.getResolutionsByRowAsCachedRowSet().getString(i));
				        		  //logger.info("that string: " + this.getResolutionsByRowAsCachedRowSet().getString(columnIndexForSource2Column));
				        		  
				        			  if ((this.getResolutionsByRowAsCachedRowSet().getString(i) == null && this.getResolutionsByRowAsCachedRowSet().getString(columnIndexForSource2Column) == null) || this.getResolutionsByRowAsCachedRowSet().getString(i).equals(this.getResolutionsByRowAsCachedRowSet().getString(columnIndexForSource2Column))) {

				        				  // There is no conflict
				        				  
				        					 resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container\">").append(this.getResolutionsByRowAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container\">").append(this.getResolutionsByRowAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td>");
				        					 if (this.getNullOrConstantSolutionUsingColumnNumberAsHashMap().get(columnNumber) != null) {
			        							  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"constant-container\"><!-- Above/below constants --></td>");
			        						  }

				        			  } else {
				        				  
				        				  // There is a conflict
				        				  Integer solutionByRowId = this.getResolutionsByRowAsCachedRowSet().getInt("solution_by_row_id");
				        				  
				        				  String constant = this.getConstantUsingCellCoordsAsHashMap().get(this.getResolutionsByRowAsCachedRowSet().getInt("joined_keytable_id") + "," + columnNumber.toString());
				        				  
				        				  if (this.getSolutionByColumnIdUsingCellCoordsAsHashMap().get(this.getResolutionsByRowAsCachedRowSet().getInt("joined_keytable_id") + "," + columnNumber.toString()) != null && this.getSolutionByColumnIdUsingCellCoordsAsHashMap().get(this.getResolutionsByRowAsCachedRowSet().getInt("joined_keytable_id") + "," + columnNumber.toString()) != 0) {
				        					  
				        					  // Solved by Column
				        					  
				        					  Integer solutionByColumnID = this.getSolutionByColumnIdUsingCellCoordsAsHashMap().get(this.getResolutionsByRowAsCachedRowSet().getInt("joined_keytable_id") + "," + columnNumber.toString());
				        					  
				        					  // TODO: What is id 1? Need some sort of enumeration.
				        					  
				        					  if (solutionByColumnID != null && solutionByColumnID == 1) {
				        						  // Prefer source 1
				        						  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data preferred-source\">").append(this.getResolutionsByRowAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td>");
				        						  if (this.getNullOrConstantSolutionUsingColumnNumberAsHashMap().get(columnNumber) != null) {
				        							  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"constant-container\"><!-- Above/below constants --></td>");
				        						  }
				        					  } else if (solutionByColumnID != null && solutionByColumnID == 2) {
				        						  // Prefer source 2
				        						  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data preferred-source\">").append(this.getResolutionsByRowAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td>");
				        						  if (this.getNullOrConstantSolutionUsingColumnNumberAsHashMap().get(columnNumber) != null) {
				        							  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"constant-container\"><!-- Above/below constants --></td>");
				        						  }
				        					  } else if (solutionByColumnID != null && solutionByColumnID == 3) {
				        						  // Use NULL	
				        						  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td><td class=\"constant-container\">NULL</td>");
				        					  } else if (solutionByColumnID != null && solutionByColumnID == 4) {
				        						  // Constant				        						  
				        						  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td><td class=\"constant-container\">").append(constant).append("</td>");
				        					  } else {
				        						  //TODO: Error, unexpect ID
				        						  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td>");
				        						  if (this.getNullOrConstantSolutionUsingColumnNumberAsHashMap().get(columnNumber) != null) {
				        							  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"constant-container\"><!-- Above/below constants --></td>");
				        						  }
				        					  }
				        					  
				        				  } 
				        				  else if (this.getSolutionByCellIdUsingCellCoordsAsHashMap().get(this.getResolutionsByRowAsCachedRowSet().getInt("joined_keytable_id") + "," + columnNumber.toString()) != null && this.getSolutionByCellIdUsingCellCoordsAsHashMap().get(this.getResolutionsByRowAsCachedRowSet().getInt("joined_keytable_id") + "," + columnNumber.toString()) != 0) {
				        					  
				        					// Solved by Cell
				        					  
				        					  Integer solutionByCellId = this.getSolutionByCellIdUsingCellCoordsAsHashMap().get(this.getResolutionsByRowAsCachedRowSet().getInt("joined_keytable_id") + "," + columnNumber.toString());
				        					  
				        					  
				        					  // TODO: What is id 1? Need some sort of enumeration.
				        					  
				        					  if (solutionByCellId != null && solutionByCellId == 1) {
				        						  // Prefer source 1
				        						  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data preferred-source\">").append(this.getResolutionsByRowAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td>");
				        						  if (this.getNullOrConstantSolutionUsingColumnNumberAsHashMap().get(columnNumber) != null) {
				        							  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"constant-container\"><!-- Above/below constants --></td>");
				        						  }
				        					  } else if (solutionByCellId != null && solutionByCellId == 2) {
				        						  // Prefer source 2
				        						  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data preferred-source\">").append(this.getResolutionsByRowAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td>");
				        						  if (this.getNullOrConstantSolutionUsingColumnNumberAsHashMap().get(columnNumber) != null) {
				        							  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"constant-container\"><!-- Above/below constants --></td>");
				        						  }
				        					  } else if (solutionByCellId != null && solutionByCellId == 3) {
				        						  // Use NULL	
				        						  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td><td class=\"constant-container\">NULL</td>");
				        					  } else if (solutionByCellId != null && solutionByCellId == 4) {
				        						  // Constant				        						  
				        						  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td><td class=\"constant-container\">").append(constant).append("</td>");
				        					  } else {
				        						  //TODO: Error, unexpected ID
				        						  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td>");
				        						  if (this.getNullOrConstantSolutionUsingColumnNumberAsHashMap().get(columnNumber) != null) {
				        							  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"constant-container\"><!-- Above/below constants --></td>");
				        						  }
				        					  }

				        				  } else if (solutionByRowId != null && solutionByRowId != 0) {
					        					  
						        					// Solved by Row
						        					  
						        					  // TODO: What is id 1? Need some sort of enumeration.
						        					  
						        					  if (solutionByRowId != null && solutionByRowId == 1) {
						        						  // Prefer source 1
						        						  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data preferred-source\">").append(this.getResolutionsByRowAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td>");
						        						  if (this.getNullOrConstantSolutionUsingColumnNumberAsHashMap().get(columnNumber) != null) {
						        							  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"constant-container\"><!-- Above/below constants --></td>");
						        						  }
						        					  } else if (solutionByRowId != null && solutionByRowId == 2) {
						        						  // Prefer source 2
						        						  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data preferred-source\">").append(this.getResolutionsByRowAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td>");
						        						  if (this.getNullOrConstantSolutionUsingColumnNumberAsHashMap().get(columnNumber) != null) {
						        							  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"constant-container\"><!-- Above/below constants --></td>");
						        						  }
						        					  } else if (solutionByRowId != null && solutionByRowId == 3) {
						        						  // Use NULL	
						        						  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td><td class=\"constant-container\">NULL</td>");
						        					  } else if (solutionByRowId != null && solutionByRowId == 4) {
						        						  // Constant				        						  
						        						  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td><td class=\"constant-container\">").append(constant).append("</td>");
						        					  } else if (solutionByRowId != null && solutionByRowId == 5) {
						        						  // Remove entire row				        						  
						        						  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td>");
						        					  } else {
						        						  //TODO: Error, unexpected ID
						        						  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td>");
						        						  if (this.getNullOrConstantSolutionUsingColumnNumberAsHashMap().get(columnNumber) != null) {
						        							  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"constant-container\"><!-- Above/below constants --></td>");
						        						  }
						        					  }
						        					 				        					  
				        					  
				        				  } else {
				        					  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container conflicting-data\">").append(this.getResolutionsByRowAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td>");  
				        					  if (this.getNullOrConstantSolutionUsingColumnNumberAsHashMap().get(columnNumber) != null) {
			        							  resolutionsByRowAsDecoratedXHTMLTable.append("<td class=\"constant-container\"><!-- Above/below constants --></td>");
			        						  }
				        				  }
				        				  
				        			  }
				        		  
			        		  }
			        	  
			          }
					 
					 
					 resolutionsByRowAsDecoratedXHTMLTable.append("</tr>");
					 
					 rowFirstClassName = "";
				}
					 
				resolutionsByRowAsDecoratedXHTMLTable.append("</tbody>");
				 
				resolutionsByRowAsDecoratedXHTMLTable.append("</table>");
				
				resolutionsByRowAsDecoratedXHTMLTable.append("<!-- <div>TODO: paging</div> -->");
				resolutionsByRowAsDecoratedXHTMLTable.append("<!-- <div>TODO: Show solved columns <input type=\"checkbox\"/></div> -->");
				
			} else {
				
				resolutionsByRowAsDecoratedXHTMLTable = new StringBuffer("There are no unresolved conflicts.");
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowAsDecoratedXHTMLTable.toString();
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

	public void setSolutionByColumnIdUsingCellCoordsAsHashMap(
			HashMap<String, Integer> solutionByColumnIdUsingCellCoordsAsHashMap) {
		this.solutionByColumnIdUsingCellCoordsAsHashMap = solutionByColumnIdUsingCellCoordsAsHashMap;
	}

	public HashMap<String, Integer> getSolutionByColumnIdUsingCellCoordsAsHashMap() {
		return solutionByColumnIdUsingCellCoordsAsHashMap;
	}

	public void setSolutionByCellIdUsingCellCoordsAsHashMap(
			HashMap<String, Integer> solutionByCellIdUsingCellCoordsAsHashMap) {
		this.solutionByCellIdUsingCellCoordsAsHashMap = solutionByCellIdUsingCellCoordsAsHashMap;
	}

	public HashMap<String, Integer> getSolutionByCellIdUsingCellCoordsAsHashMap() {
		return solutionByCellIdUsingCellCoordsAsHashMap;
	}

	public void setConstantUsingCellCoordsAsHashMap(
			HashMap<String, String> constantUsingCellCoordsAsHashMap) {
		this.constantUsingCellCoordsAsHashMap = constantUsingCellCoordsAsHashMap;
	}

	public HashMap<String, String> getConstantUsingCellCoordsAsHashMap() {
		return constantUsingCellCoordsAsHashMap;
	}

	public void setNullOrConstantSolutionUsingColumnNumberAsHashMap(
			HashMap<Integer, Boolean> nullOrConstantSolutionUsingColumnNumberAsHashMap) {
		this.nullOrConstantSolutionUsingColumnNumberAsHashMap = nullOrConstantSolutionUsingColumnNumberAsHashMap;
	}

	public HashMap<Integer, Boolean> getNullOrConstantSolutionUsingColumnNumberAsHashMap() {
		return nullOrConstantSolutionUsingColumnNumberAsHashMap;
	}

	public Logger getLogger() {
		return logger;
	}

}
