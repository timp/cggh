package org.cggh.tools.dataMerger.functions.data.resolutions.byCell;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.rowset.CachedRowSet;



public class ResolutionsByCellFunctions implements java.io.Serializable {





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

	private HashMap<String, Integer> solutionByColumnIdUsingCellCoordsAsHashMap;
	private HashMap<String, Integer> solutionByRowIdUsingCellCoordsAsHashMap;
	private HashMap<String, Integer> solutionByCellIdUsingCellCoordsAsHashMap;
	
	private HashMap<String, String> constantUsingCellCoordsAsHashMap;
	private HashMap<Integer, Boolean> nullOrConstantSolutionUsingColumnNumberAsHashMap;	
	
	public ResolutionsByCellFunctions () {
		
	}

	public void setResolutionsByCellAsCachedRowSet(
			CachedRowSet resolutionsByCellAsCachedRowSet) {
		this.resolutionsByCellAsCachedRowSet = resolutionsByCellAsCachedRowSet;
	}

	//TODO
	public void setResolutionsByCellAsDecoratedXHTMLTableUsingResolutionsByCellAsCachedRowSet() {
		
		StringBuffer resolutionsByCellAsDecoratedXHTMLTable = null;
		
		
		try {
			
			//Make sure we are at the beginning of the RowSet.
			this.getResolutionsByCellAsCachedRowSet().beforeFirst();
			
			if (this.getResolutionsByCellAsCachedRowSet().next()) {

				resolutionsByCellAsDecoratedXHTMLTable = new StringBuffer();
				
				resolutionsByCellAsDecoratedXHTMLTable.append("<table class=\"resolutions-by-cell-table\">");

				 resolutionsByCellAsDecoratedXHTMLTable.append("<thead>");
				 resolutionsByCellAsDecoratedXHTMLTable.append("<tr>");
				 
				 Pattern joinedKeytableKeyColumnNamePattern = Pattern.compile("^key_column_(\\d+)$");
				 
				 
		          for (int i = 1; i <= this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnCount(); i++) {

		        		  Matcher joinedKeytableKeyColumnNamePatternMatcher = joinedKeytableKeyColumnNamePattern.matcher(this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnName(i));
		        		  
		        		  if (joinedKeytableKeyColumnNamePatternMatcher.find()) {
		        			  
			        		  Integer columnNumber = Integer.parseInt(joinedKeytableKeyColumnNamePatternMatcher.group(1));
			        		  
			        		  ////this.logger.info("Got columnNumber: ").append(columnNumber);
			        		  
			        		  resolutionsByCellAsDecoratedXHTMLTable.append("<th class=\"key-heading\" rowspan=\"2\">").append(this.getJoinColumnNamesByColumnNumberAsHashMap().get(columnNumber)).append("</th>");
			        		  
		        		  }

		          }
				 
				  
				 Pattern resolutionsByCellAsCachedRowSetSource1ColumnNamePattern = Pattern.compile("^column_(\\d+)_source_1$");
				 
				 ////this.logger.info("Searching for non-key cross-datatable columns by label...");
				 
		          for (int i = 1; i <= this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnCount(); i++) {

		        		  Matcher resolutionsByCellAsCachedRowSetColumnNamePatternMatcher = resolutionsByCellAsCachedRowSetSource1ColumnNamePattern.matcher(this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnLabel(i));
		        		  
		        		  ////this.logger.info("Looking at column label: ").append( this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnLabel(i));
		        		  
		        		  if (resolutionsByCellAsCachedRowSetColumnNamePatternMatcher.find()) {
		        		  
			        		  Integer columnNumber = Integer.parseInt(resolutionsByCellAsCachedRowSetColumnNamePatternMatcher.group(1));
			        		  
			        		  ////this.logger.info("Got columnNumber: ").append(columnNumber);
			        		  
			        		  if (this.getUnresolvedByColumnOrRowConflictsCountUsingColumnNumberAsHashMap().get(columnNumber) != null && this.getUnresolvedByColumnOrRowConflictsCountUsingColumnNumberAsHashMap().get(columnNumber) > 0) {
			        			  
			        			  if (this.getNullOrConstantSolutionUsingColumnNumberAsHashMap().get(columnNumber) != null) {
			        				  
			        				  resolutionsByCellAsDecoratedXHTMLTable.append("<th class=\"column-heading\" colspan=\"4\">").append(this.getJoinColumnNamesByColumnNumberAsHashMap().get(columnNumber)).append("</th>");
			        				  
			        			  } else {
			        				  resolutionsByCellAsDecoratedXHTMLTable.append("<th class=\"column-heading\" colspan=\"3\">").append(this.getJoinColumnNamesByColumnNumberAsHashMap().get(columnNumber)).append("</th>");
			        			  }
			        				  
			        		  } else {
			        			  
			        			  if (this.getNullOrConstantSolutionUsingColumnNumberAsHashMap().get(columnNumber) != null) {
			        				  
			        				  resolutionsByCellAsDecoratedXHTMLTable.append("<th class=\"column-heading\" colspan=\"3\">").append(this.getJoinColumnNamesByColumnNumberAsHashMap().get(columnNumber)).append("</th>");
			        			  } else {
			        				  
			        				  resolutionsByCellAsDecoratedXHTMLTable.append("<th class=\"column-heading\" colspan=\"2\">").append(this.getJoinColumnNamesByColumnNumberAsHashMap().get(columnNumber)).append("</th>");
			        			  }
			        		  }
			        		  
			        		  
		        		  }
		        	  
		          }
				 

				 resolutionsByCellAsDecoratedXHTMLTable.append("</tr>");
				 
				 resolutionsByCellAsDecoratedXHTMLTable.append("<tr>");



		          for (int i = 1; i <= this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnCount(); i++) {
		        	  
		        	  Matcher resolutionsByCellAsCachedRowSetColumnNamePatternMatcher = resolutionsByCellAsCachedRowSetSource1ColumnNamePattern.matcher(this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnLabel(i));
		        	  
		        	  if (resolutionsByCellAsCachedRowSetColumnNamePatternMatcher.find()) {

		        		  resolutionsByCellAsDecoratedXHTMLTable.append("<th class=\"source_1_value-heading\">1</th><th class=\"source_2_value-heading\">2</th>");
		        		  
		        		  Integer columnNumber = Integer.parseInt(resolutionsByCellAsCachedRowSetColumnNamePatternMatcher.group(1));
		        		  
		        		  if (this.getUnresolvedByColumnOrRowConflictsCountUsingColumnNumberAsHashMap().get(columnNumber) != null && this.getUnresolvedByColumnOrRowConflictsCountUsingColumnNumberAsHashMap().get(columnNumber) > 0) {
		        			  resolutionsByCellAsDecoratedXHTMLTable.append("<th class=\"solution-heading\">Solution</th>");
		        		  }
		        	  
		        		  if (this.getNullOrConstantSolutionUsingColumnNumberAsHashMap().get(columnNumber) != null) {
							  resolutionsByCellAsDecoratedXHTMLTable.append("<th class=\"constant-heading\">Constant</th>");
						 }
		        		  
		        	  }
		          }
				 

				 resolutionsByCellAsDecoratedXHTMLTable.append("</tr>");
				 
				 resolutionsByCellAsDecoratedXHTMLTable.append("</thead>");
				 
				 resolutionsByCellAsDecoratedXHTMLTable.append("<tbody>");
				 
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
					
					resolutionsByCellAsDecoratedXHTMLTable.append("<tr class=\"").append(rowStripeClassName).append(rowFirstClassName).append(rowLastClassName).append("\">");

					
			          for (int i = 1; i <= this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnCount(); i++) {
			        	  
			        	  if (this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnName(i).startsWith("key_column_")) {
			        		  
			        		  resolutionsByCellAsDecoratedXHTMLTable.append("<td>").append(this.getResolutionsByCellAsCachedRowSet().getString(this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnName(i))).append("</td>");
			        	  
			        	  }
			          }
					
					 
					 
					 //TODO

					 ////this.logger.info("Searching for non-key cross-datatable columns by label (to be used for getting data)...");
					 
			          for (int i = 1; i <= this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnCount(); i++) {
			        	  

			        		  Matcher resolutionsByCellAsCachedRowSetColumnNamePatternMatcher = resolutionsByCellAsCachedRowSetSource1ColumnNamePattern.matcher(this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnLabel(i));
			        		  
			        		  ////this.logger.info("Looking at column label: ").append( this.getResolutionsByCellAsCachedRowSet().getMetaData().getColumnLabel(i));
			        		  
			        		  if (resolutionsByCellAsCachedRowSetColumnNamePatternMatcher.find()) {
			        		  
				        		  Integer columnNumber = Integer.parseInt(resolutionsByCellAsCachedRowSetColumnNamePatternMatcher.group(1));
				        		  
				        		  ////this.logger.info("Got columnNumber: ").append(columnNumber);
				        		  
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

				        				  // There is no conflict.
				        				  
				        					 resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container\">").append(this.getResolutionsByCellAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container\">").append(this.getResolutionsByCellAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td>");

				        					 if (this.getUnresolvedByColumnOrRowConflictsCountUsingColumnNumberAsHashMap().get(columnNumber) != null && this.getUnresolvedByColumnOrRowConflictsCountUsingColumnNumberAsHashMap().get(columnNumber) > 0) {
				        					 
				        						 resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"solution_by_cell_id-container\"><!-- No solution selector required here because there is no conflict --></td>");
				        					 
				        					 }
				        					 
				        					 if (this.getNullOrConstantSolutionUsingColumnNumberAsHashMap().get(columnNumber) != null) {
				        						 resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"constant-container\"><!-- Above/below constants --></td>");
			        						  }
				        					 
				        					 
				        			  } else {
				        				
				        				  // There is a conflict
				        				  
				        				  
				        				  
				        				  //TODO:
				        				  //FIXME:

				        				  Integer solutionByColumnId = this.getSolutionByColumnIdUsingCellCoordsAsHashMap().get(this.getResolutionsByCellAsCachedRowSet().getInt("joined_keytable_id") + "," + columnNumber.toString());
				        				  
				        				  Integer solutionByRowId = this.getSolutionByRowIdUsingCellCoordsAsHashMap().get(this.getResolutionsByCellAsCachedRowSet().getInt("joined_keytable_id") + "," + columnNumber.toString());
				        				  
				        				  Integer solutionByCellId = this.getSolutionByCellIdUsingCellCoordsAsHashMap().get(this.getResolutionsByCellAsCachedRowSet().getInt("joined_keytable_id") + "," + columnNumber.toString());
			        					  
				        				  
				        				  
				        				  String constant = "";
				        				  if (this.getConstantUsingCellCoordsAsHashMap().get(this.getResolutionsByCellAsCachedRowSet().getInt("joined_keytable_id") + "," + columnNumber.toString()) != null) {
				        					  constant = this.getConstantUsingCellCoordsAsHashMap().get(this.getResolutionsByCellAsCachedRowSet().getInt("joined_keytable_id") + "," + columnNumber.toString());
				        				  }

				        				  
				        				  ///////////////////////////////////////////
				        				  
				        				  
				          				  
				        				  if (this.getSolutionByColumnIdUsingCellCoordsAsHashMap().get(this.getResolutionsByCellAsCachedRowSet().getInt("joined_keytable_id") + "," + columnNumber.toString()) != null && this.getSolutionByColumnIdUsingCellCoordsAsHashMap().get(this.getResolutionsByCellAsCachedRowSet().getInt("joined_keytable_id") + "," + columnNumber.toString()) != 0) {
				        					  
				        					  // Solved by Column
				        					  
				        					  // TODO: What is id 1? Need some sort of enumeration.
				        					  
				        					  if (solutionByColumnId != null && solutionByColumnId == 1) {
				        						  // Prefer source 1
				        						  resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data preferred-source\">").append(this.getResolutionsByCellAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td>");
				        						  if (this.getNullOrConstantSolutionUsingColumnNumberAsHashMap().get(columnNumber) != null) {
				        							  resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"constant-container\"><!-- Above/below constants --></td>");
				        						  }
				        					  } else if (solutionByColumnId != null && solutionByColumnId == 2) {
				        						  // Prefer source 2
				        						  resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data preferred-source\">").append(this.getResolutionsByCellAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td>");
				        						  if (this.getNullOrConstantSolutionUsingColumnNumberAsHashMap().get(columnNumber) != null) {
				        							  resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"constant-container\"><!-- Above/below constants --></td>");
				        						  }
				        					  } else if (solutionByColumnId != null && solutionByColumnId == 3) {
				        						  // Use NULL	
				        						  resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td><td class=\"constant-container\">NULL</td>");
				        					  } else if (solutionByColumnId != null && solutionByColumnId == 4) {
				        						  // Constant				        						  
				        						  resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td><td class=\"constant-container\">").append(constant).append("</td>");
				        					  } else {
				        						  //TODO: Error, unexpect ID
				        						  resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td>");
				        						  if (this.getNullOrConstantSolutionUsingColumnNumberAsHashMap().get(columnNumber) != null) {
				        							  resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"constant-container\"><!-- Above/below constants --></td>");
				        						  }
				        					  }
				        					  
				        				  } 
				        				  else if (this.getSolutionByCellIdUsingCellCoordsAsHashMap().get(this.getResolutionsByCellAsCachedRowSet().getInt("joined_keytable_id") + "," + columnNumber.toString()) != null && this.getSolutionByCellIdUsingCellCoordsAsHashMap().get(this.getResolutionsByCellAsCachedRowSet().getInt("joined_keytable_id") + "," + columnNumber.toString()) != 0) {
				        					  
				        					// Solved by Cell
				        					  
				        					  
				        					  
				        					  // TODO: What is id 1? Need some sort of enumeration.
				        					  
				        					  if (solutionByCellId != null && solutionByCellId == 1) {
				        						  // Prefer source 1
				        						  resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data preferred-source\">").append(this.getResolutionsByCellAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td>");
				        						  if (this.getNullOrConstantSolutionUsingColumnNumberAsHashMap().get(columnNumber) != null) {
				        							  resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"constant-container\"><!-- Above/below constants --></td>");
				        						  }
				        					  } else if (solutionByCellId != null && solutionByCellId == 2) {
				        						  // Prefer source 2
				        						  resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data preferred-source\">").append(this.getResolutionsByCellAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td>");
				        						  if (this.getNullOrConstantSolutionUsingColumnNumberAsHashMap().get(columnNumber) != null) {
				        							  resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"constant-container\"><!-- Above/below constants --></td>");
				        						  }
				        					  } else if (solutionByCellId != null && solutionByCellId == 3) {
				        						  // Use NULL	
				        						  resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td><td class=\"constant-container\">NULL</td>");
				        					  } else if (solutionByCellId != null && solutionByCellId == 4) {
				        						  // Constant				        						  
				        						  resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td><td class=\"constant-container\">").append(constant).append("</td>");
				        					  } else {
				        						  //TODO: Error, unexpected ID
				        						  resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td>");
				        						  if (this.getNullOrConstantSolutionUsingColumnNumberAsHashMap().get(columnNumber) != null) {
				        							  resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"constant-container\"><!-- Above/below constants --></td>");
				        						  }
				        					  }

				        				  } else if (solutionByRowId != null && solutionByRowId != 0) {
					        					  
						        					// Solved by Row
						        					  
						        					  // TODO: What is id 1? Need some sort of enumeration.
						        					  
						        					  if (solutionByRowId != null && solutionByRowId == 1) {
						        						  // Prefer source 1
						        						  resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data preferred-source\">").append(this.getResolutionsByCellAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td>");
						        						  if (this.getNullOrConstantSolutionUsingColumnNumberAsHashMap().get(columnNumber) != null) {
						        							  resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"constant-container\"><!-- Above/below constants --></td>");
						        						  }
						        					  } else if (solutionByRowId != null && solutionByRowId == 2) {
						        						  // Prefer source 2
						        						  resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data preferred-source\">").append(this.getResolutionsByCellAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td>");
						        						  if (this.getNullOrConstantSolutionUsingColumnNumberAsHashMap().get(columnNumber) != null) {
						        							  resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"constant-container\"><!-- Above/below constants --></td>");
						        						  }
						        					  } else if (solutionByRowId != null && solutionByRowId == 3) {
						        						  // Use NULL	
						        						  resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td><td class=\"constant-container\">NULL</td>");
						        					  } else if (solutionByRowId != null && solutionByRowId == 4) {
						        						  // Constant				        						  
						        						  resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td><td class=\"constant-container\">").append(constant).append("</td>");
						        					  } else if (solutionByRowId != null && solutionByRowId == 5) {
						        						  // Remove entire row				        						  
						        						  resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td>");
						        					  } else {
						        						  //TODO: Error, unexpected ID
						        						  resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container resolved-conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container resolved-conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td>");
						        						  if (this.getNullOrConstantSolutionUsingColumnNumberAsHashMap().get(columnNumber) != null) {
						        							  resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"constant-container\"><!-- Above/below constants --></td>");
						        						  }
						        					  }
						        					 				        					  
				        					  
				        				  } else {
				        					  resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td>");  
				        					  if (this.getNullOrConstantSolutionUsingColumnNumberAsHashMap().get(columnNumber) != null) {
				        						  resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"constant-container\"><!-- Above/below constants --></td>");
			        						  }
				        				  } 
				        				  
				        				  
				        				  
				        				  /////////////////////////////////////////
				        				  
				        				  
				        				  
				        				  
				        				  if (this.getUnresolvedByColumnOrRowStatusUsingCellCoordsAsHashMap().get(this.getResolutionsByCellAsCachedRowSet().getInt("joined_keytable_id") + "," + columnNumber.toString()) != null) {
				        			  
				        					  // This conflict hasn't been resolved by column or row.
				        					  
					        					  	//resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"source_1_value-container\">").append(this.getResolutionsByCellAsCachedRowSet().getString(i) ).append("</td><td class=\"source_2_value-container conflicting-data\">").append(this.getResolutionsByCellAsCachedRowSet().getString(columnIndexForSource2Column)).append("</td>");
					        			  	  
					        				  
					        					  	
					        					  	
					        					  	
					        					  	resolutionsByCellAsDecoratedXHTMLTable.append("<td class=\"solution_by_cell_id-container\">");
		        							
				        							//because the cursor may have been moved to the end previously (we are in a loop).
				        							this.getSolutionsByCellAsCachedRowSet().beforeFirst();
					        							
				        							if (this.getSolutionsByCellAsCachedRowSet().next()) {
				        								
				        								if (solutionByCellId != null && solutionByCellId != 0) {
				        									resolutionsByCellAsDecoratedXHTMLTable.append("<select name=\"solution_by_cell_id-").append(this.getResolutionsByCellAsCachedRowSet().getInt("joined_keytable_id")).append("-").append(columnNumber.toString()).append("\">");
				        								} else {
				        									resolutionsByCellAsDecoratedXHTMLTable.append("<select name=\"solution_by_cell_id-").append(this.getResolutionsByCellAsCachedRowSet().getInt("joined_keytable_id")).append("-").append(columnNumber.toString()).append("\" class=\"unresolved\">");
				        									
				        								}
			
				        								//FIXME: Should the null option be in the table?
				        								
				        								resolutionsByCellAsDecoratedXHTMLTable.append("<option value=\"\">");
			
				        								 resolutionsByCellAsDecoratedXHTMLTable.append("Unresolved");
				        								 
				        								 resolutionsByCellAsDecoratedXHTMLTable.append("</option>");				
				        								 
				        								//because next() skips the first row.
				        								 this.getSolutionsByCellAsCachedRowSet().beforeFirst();
			
					        								while (this.getSolutionsByCellAsCachedRowSet().next()) {
					        									
					        									String selectedAttribute = "";
					        									
						        									if (solutionByCellId == this.getSolutionsByCellAsCachedRowSet().getInt("id")) {
						        										selectedAttribute = " selected=\"selected\"";
						        									}
					        									
					        									resolutionsByCellAsDecoratedXHTMLTable.append("<option value=\"").append(this.getSolutionsByCellAsCachedRowSet().getString("id")).append("\"").append(selectedAttribute).append(">");
				
					        									 resolutionsByCellAsDecoratedXHTMLTable.append(this.getSolutionsByCellAsCachedRowSet().getString("description"));
					        									 
					        									 resolutionsByCellAsDecoratedXHTMLTable.append("</option>");
					        								}
				        									 
				        								resolutionsByCellAsDecoratedXHTMLTable.append("</select>");
				        								
				        								
				        							} else {
				        								
				        								resolutionsByCellAsDecoratedXHTMLTable = new StringBuffer("There are no solutions by cell.");
				        								
				        							}
	
				        					 
					        						if (this.getResolutionsByCellAsCachedRowSet().getString("constant") != null) {
					        							resolutionsByCellAsDecoratedXHTMLTable.append("<label for=\"constant-").append(this.getResolutionsByCellAsCachedRowSet().getInt("joined_keytable_id")).append("-").append(columnNumber.toString()).append("\">Constant:</label><input type=\"text\" name=\"constant-").append(this.getResolutionsByCellAsCachedRowSet().getInt("joined_keytable_id")).append("-").append(columnNumber.toString()).append("\" value=\"").append(this.getResolutionsByCellAsCachedRowSet().getString("constant")).append("\"/>");
					        						} else {
					        							resolutionsByCellAsDecoratedXHTMLTable.append("<label for=\"constant-").append(this.getResolutionsByCellAsCachedRowSet().getInt("joined_keytable_id")).append("-").append(columnNumber.toString()).append("\" style=\"display:none;\">Constant:</label><input type=\"text\" name=\"constant-").append(this.getResolutionsByCellAsCachedRowSet().getInt("joined_keytable_id")).append( "-").append(columnNumber.toString()).append("\" value=\"\" style=\"display:none;\"/>");
					        						}
								        						
						        					 resolutionsByCellAsDecoratedXHTMLTable.append("</td>");
					        				  
						        					 
				        					  
					        				  }
				        				  
				        			  }
				        		  
			        		  }
			        	  
			          }
					 
					 
					 resolutionsByCellAsDecoratedXHTMLTable.append("</tr>");
					 
					 rowFirstClassName = "";
				}
					 
				resolutionsByCellAsDecoratedXHTMLTable.append("</tbody>");
				 
				resolutionsByCellAsDecoratedXHTMLTable.append("</table>");
				
				resolutionsByCellAsDecoratedXHTMLTable.append("<!-- <div>TODO: paging</div> -->");
				resolutionsByCellAsDecoratedXHTMLTable.append("<!-- <div>TODO: Show solved columns <input type=\"checkbox\"/></div> -->");
				
			} else {
				
				resolutionsByCellAsDecoratedXHTMLTable = new StringBuffer("There are no unresolved conflicts.");
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.resolutionsByCellAsDecoratedXHTMLTable = resolutionsByCellAsDecoratedXHTMLTable.toString();
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

	public void setSolutionByColumnIdUsingCellCoordsAsHashMap(
			HashMap<String, Integer> solutionByColumnIdUsingCellCoordsAsHashMap) {
		this.solutionByColumnIdUsingCellCoordsAsHashMap = solutionByColumnIdUsingCellCoordsAsHashMap;
	}

	public HashMap<String, Integer> getSolutionByColumnIdUsingCellCoordsAsHashMap() {
		return solutionByColumnIdUsingCellCoordsAsHashMap;
	}

	public void setSolutionByRowIdUsingCellCoordsAsHashMap(
			HashMap<String, Integer> solutionByRowIdUsingCellCoordsAsHashMap) {
		this.solutionByRowIdUsingCellCoordsAsHashMap = solutionByRowIdUsingCellCoordsAsHashMap;
	}

	public HashMap<String, Integer> getSolutionByRowIdUsingCellCoordsAsHashMap() {
		return solutionByRowIdUsingCellCoordsAsHashMap;
	}

	public void setSolutionByCellIdUsingCellCoordsAsHashMap(
			HashMap<String, Integer> solutionByCellIdUsingCellCoordsAsHashMap) {
		this.solutionByCellIdUsingCellCoordsAsHashMap = solutionByCellIdUsingCellCoordsAsHashMap;
	}

	public HashMap<String, Integer> getSolutionByCellIdUsingCellCoordsAsHashMap() {
		return solutionByCellIdUsingCellCoordsAsHashMap;
	}

	public Logger getLogger() {
		return logger;
	}

	
}
