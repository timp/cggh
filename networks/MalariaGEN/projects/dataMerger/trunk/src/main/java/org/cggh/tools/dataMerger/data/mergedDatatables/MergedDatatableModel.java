package org.cggh.tools.dataMerger.data.mergedDatatables;

import javax.sql.rowset.CachedRowSet;

public class MergedDatatableModel {

	private String name;
	private CachedRowSet dataAsCachedRowSet;

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


	
}
