package org.cggh.tools.dataMerger.data.merges;

import java.sql.Timestamp;

public class MergeModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5306097444255740574L;
	private Integer id = null;
	private Timestamp createdDatetime = null;
	private Timestamp updatedDatetime = null;
	
	public MergeModel() {

	}

	public void setId (final Integer id) {
		
		this.id = id;
	}
	public Integer getId () {
		
		return this.id;
	}	


	public void setCreatedDatetime (final Timestamp datetime) {
		
		this.createdDatetime = datetime;
	}
	public Timestamp getCreatedDatetime () {
		
		return this.createdDatetime;
	}	
	
	
	
	
	public void setUpdatedDatetime (final Timestamp datetime) {
		
		this.updatedDatetime = datetime;
	}
	public Timestamp getUpdatedDatetime () {
		
		return this.updatedDatetime;
	}	
	
}