package org.cggh.tools.dataMerger.data.joinedKeytables;

import javax.sql.rowset.CachedRowSet;

public class JoinedKeytableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8738572495689323793L;
	private String name;
	private CachedRowSet dataAsCachedRowSet;
	private Integer id;

	public String getName() {

		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CachedRowSet getDataAsCachedRowSet() {
		return this.dataAsCachedRowSet;
	}

	public void setDataAsCachedRowSet(
			CachedRowSet dataAsCachedRowSet) {
		this.dataAsCachedRowSet = dataAsCachedRowSet;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

}
