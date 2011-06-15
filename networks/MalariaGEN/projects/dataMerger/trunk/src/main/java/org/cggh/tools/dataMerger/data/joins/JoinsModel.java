package org.cggh.tools.dataMerger.data.joins;

import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

public class JoinsModel {

	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.joins");
	

	private CachedRowSet joinsAsCachedRowSet;
	private Integer keysCount;
	private CachedRowSet crossDatatableJoinsAsCachedRowSet;

	private CachedRowSet keyJoinsAsCachedRowset;
	private CachedRowSet nonKeyCrossDatatableJoinsAsCachedRowset;
	private CachedRowSet nonCrossDatatableJoinsAsCachedRowset;
	private String exportRepositoryFilepath;
	

	public void setJoinsAsCachedRowSet(CachedRowSet joinsAsCachedRowSet) {
		this.joinsAsCachedRowSet = joinsAsCachedRowSet;	
	}   
    public CachedRowSet getJoinsAsCachedRowSet () {
        return this.joinsAsCachedRowSet;
    }

    

	public void setKeysCount(Integer keysCount) {

		this.keysCount = keysCount;
		
	}    
    public Integer getKeysCount () {
        return this.keysCount;
    }  
    


	


	public void setCrossDatatableJoinsAsCachedRowSet(
			final CachedRowSet crossDatatableJoinsAsCachedRowSet) {
		this.crossDatatableJoinsAsCachedRowSet = crossDatatableJoinsAsCachedRowSet;
	}

	



	public CachedRowSet getCrossDatatableJoinsAsCachedRowSet() {
		return this.crossDatatableJoinsAsCachedRowSet;
	}
	


	public void setNonCrossDatatableJoinsAsCachedRowSet(
			CachedRowSet nonCrossDatatableJoinsAsCachedRowset) {
		this.nonCrossDatatableJoinsAsCachedRowset = nonCrossDatatableJoinsAsCachedRowset;
	}


	public CachedRowSet getNonCrossDatatableJoinsAsCachedRowSet() {
		return this.nonCrossDatatableJoinsAsCachedRowset;
	}


	public void setExportRepositoryFilepath(String exportRepositoryFilepath) {
		this.exportRepositoryFilepath = exportRepositoryFilepath;
	}


	public String getExportRepositoryFilepath() {
		return this.exportRepositoryFilepath;
	}


	public void setKeyJoinsAsCachedRowSet(
			CachedRowSet keyJoinsAsCachedRowset) {
		this.keyJoinsAsCachedRowset = keyJoinsAsCachedRowset;
	}


	public void setNonKeyCrossDatatableJoinsAsCachedRowSet(
			CachedRowSet nonKeyCrossDatatableJoinsAsCachedRowset) {
		this.nonKeyCrossDatatableJoinsAsCachedRowset = nonKeyCrossDatatableJoinsAsCachedRowset;
	}


	public CachedRowSet getKeyJoinsAsCachedRowSet() {
		
		return this.keyJoinsAsCachedRowset;
	}


	public CachedRowSet getNonKeyCrossDatatableJoinsAsCachedRowSet() {

		return this.nonKeyCrossDatatableJoinsAsCachedRowset;
	}
	public Logger getLogger() {
		return logger;
	}	
	
}
