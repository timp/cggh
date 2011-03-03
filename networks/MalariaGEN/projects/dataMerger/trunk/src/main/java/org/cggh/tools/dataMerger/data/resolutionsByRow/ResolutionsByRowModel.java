package org.cggh.tools.dataMerger.data.resolutionsByRow;

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
		// TODO Auto-generated method stub
		return null;
	}
    
}
