package org.cggh.tools.dataMerger.data.mergedDatatables;

import javax.sql.rowset.CachedRowSet;

public class MergedDatatableModel {

	private String name;
	private CachedRowSet dataAsCachedRowSet;
	private String exportRepositoryFilepath;
	private boolean exportSuccessful;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDataAsCachedRowSet(
			CachedRowSet dataAsCachedRowSet) {
		this.dataAsCachedRowSet = dataAsCachedRowSet;
	}
	public CachedRowSet getDataAsCachedRowSet() {
		return this.dataAsCachedRowSet;
	}

	public void setExportRepositoryFilepath(String exportRepositoryFilepath) {
		this.exportRepositoryFilepath = exportRepositoryFilepath;
	}

	public void setExportSuccessful(boolean exportSuccessful) {
		this.exportSuccessful = exportSuccessful;
	}

	public String getExportRepositoryFilepath() {
		return this.exportRepositoryFilepath;
	}

	public Boolean getExportSuccessful() {
		return this.exportSuccessful;
	}
	
}
