package org.cggh.tools.dataMerger.data.exports;

import org.cggh.tools.dataMerger.data.DataModel;
import org.cggh.tools.dataMerger.data.merges.MergeModel;
import org.cggh.tools.dataMerger.data.users.UserModel;

public class ExportModel implements java.io.Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6542800631952380713L;

	private Integer id;
	private MergeModel mergeModel;

	public ExportModel() {

		//Construct a new vanilla one
		this.setId(null);
		this.setMergeModel(new MergeModel());		
	
		
	}	


	public void setMergeModel(MergeModel mergeModel) {
		this.mergeModel = mergeModel;
	}

	public MergeModel getMergeModel() {
		return this.mergeModel;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getId() {
		return this.id;
	}

}
