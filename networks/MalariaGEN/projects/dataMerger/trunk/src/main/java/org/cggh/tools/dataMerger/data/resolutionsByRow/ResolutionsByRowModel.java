package org.cggh.tools.dataMerger.data.resolutionsByRow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.DataModel;
import org.cggh.tools.dataMerger.data.merges.MergeModel;
import org.cggh.tools.dataMerger.functions.resolutionsByColumn.ResolutionsByColumnFunctionsModel;
import org.cggh.tools.dataMerger.functions.resolutionsByRow.ResolutionsByRowFunctionsModel;


public class ResolutionsByRowModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1573490419082418709L;

	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.resolutions");

	private DataModel dataModel;
	
	public ResolutionsByRowModel() {

		this.setDataModel(new DataModel());
		
	}

    public void setDataModel (final DataModel dataModel) {
        this.dataModel  = dataModel;
    }
    public DataModel getDataModel () {
        return this.dataModel;
    } 

	public String retrieveResolutionsByRowAsDecoratedXHTMLTableUsingMergeModel (MergeModel mergeModel) {
		
		String resolutionsByRowAsDecoratedXHTMLTable = null;
		
		//TODO
		  CachedRowSet resolutionsByRowAsCachedRowSet = this.retrieveResolutionsByRowAsCachedRowSetUsingMergeId(mergeModel.getId());

		  if (resolutionsByRowAsCachedRowSet != null) {
			  

			  //TODO
			  	CachedRowSet solutionsByRowAsCachedRowSet = this.retrieveSolutionsByRowAsCachedRowSet();

			  	ResolutionsByRowFunctionsModel resolutionsByRowFunctionsModel = new ResolutionsByRowFunctionsModel();
			  	resolutionsByRowFunctionsModel.setResolutionsByRowAsCachedRowSet(resolutionsByRowAsCachedRowSet);
			  	resolutionsByRowFunctionsModel.setSolutionsByRowAsCachedRowSet(solutionsByRowAsCachedRowSet);
			  	//TODO
			  	resolutionsByRowFunctionsModel.setResolutionsByRowAsDecoratedXHTMLTableUsingResolutionsByRowAsCachedRowSet();
			  	resolutionsByRowAsDecoratedXHTMLTable = resolutionsByRowFunctionsModel.getResolutionsByRowAsDecoratedXHTMLTable();
			    
		  } else {
			  
			  //TODO: Error handling
			  this.logger.warning("Error: resolutionsByRowAsCachedRowSet is null");
			  resolutionsByRowAsDecoratedXHTMLTable = "<p>Error: resolutionsByRowAsCachedRowSet is null</p>";
			  
		  }
		
		return resolutionsByRowAsDecoratedXHTMLTable;
	}

	private CachedRowSet retrieveSolutionsByRowAsCachedRowSet() {
		// TODO Auto-generated method stub
		return null;
	}

	public CachedRowSet retrieveResolutionsByRowAsCachedRowSetUsingMergeId(
			Integer mergeId) {
		
		CachedRowSet resolutionsByRowAsCachedRowSet = null;
		
		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);
		
		   String CACHED_ROW_SET_IMPL_CLASS = "com.sun.rowset.CachedRowSetImpl";
		   
			try {
				
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				
				Connection connection = this.getDataModel().getNewConnection();
				 
				if (!connection.isClosed()) {
				
					//FIXME
					//TODO
					
//					USE datamerger;
//					SELECT resolution_by_row.merge_id, resolution_by_row.joined_keytable_id, resolution_by_row.conflicts_count, resolution_by_row.solution_by_row_id, resolution_by_row.constant
//					/* , TODO: key columns go here */
//					, datatable_1.`Row` AS `column_1_source_1`, datatable_1.`Row` AS `column_1_source_2`
//					FROM resolution_by_row 
//					JOIN joined_keytable_1 ON joined_keytable_1.id = resolution_by_row.joined_keytable_id
//					JOIN datatable_1 ON datatable_1.`ID` = joined_keytable_1.key_column_2 AND datatable_1.`Sample Label` = joined_keytable_1.key_column_20
//					JOIN datatable_2 ON datatable_2.`ID` = joined_keytable_1.key_column_2 AND datatable_2.`Sample Label` = joined_keytable_1.key_column_20
//					ORDER BY resolution_by_row.joined_keytable_id
//					;
					
					
					String keyColumnNameAliasesForSelectSQL = "";
					String nonKeyCrossDatatableColumnNameAliasesForSelectSQL = "";
					
				      try{
				          PreparedStatement preparedStatement = connection.prepareStatement(
				        		  "SELECT " + keyColumnNameAliasesForSelectSQL + nonKeyCrossDatatableColumnNameAliasesForSelectSQL + ", resolution_by_row.conflicts_count, resolution_by_row.solution_by_row_id, resolution_by_row.constant " + 
				        		  "FROM resolution_by_row " +
				        		  "WHERE resolution_by_row.merge_id = ?" + 
				        		  ";");
				          preparedStatement.setInt(1, mergeModel.getId());
				          preparedStatement.executeQuery();
				          Class<?> cachedRowSetImplClass = Class.forName(CACHED_ROW_SET_IMPL_CLASS);
				          resolutionsByRowAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
				          resolutionsByRowAsCachedRowSet.populate(preparedStatement.getResultSet());
				          preparedStatement.close();
	
				        }
				        catch(SQLException sqlException){
				        	this.logger.severe(sqlException.toString());
					    	sqlException.printStackTrace();
				        } 	
				
					connection.close();
					
				} else {
					
					System.out.println("connection.isClosed");
				}
					
			} 
			catch (Exception e) {
				System.out.println("Exception from getMergesAsCachedRowSet.");
				e.printStackTrace();
			}
	
	
	
	     return resolutionsByRowAsCachedRowSet;
	}
    
}
