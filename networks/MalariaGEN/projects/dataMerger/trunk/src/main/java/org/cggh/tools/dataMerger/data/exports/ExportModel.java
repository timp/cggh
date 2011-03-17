package org.cggh.tools.dataMerger.data.exports;

import org.cggh.tools.dataMerger.data.exportedDatatables.ExportedDatatableModel;
import org.cggh.tools.dataMerger.data.joinedKeytables.JoinedKeytableModel;
import org.cggh.tools.dataMerger.data.merges.MergeModel;

public class ExportModel implements java.io.Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6542800631952380713L;

	private Integer id;
	private MergeModel mergeModel;

	private String datatableName;

	private ExportedDatatableModel exportedDatatableModel;

	public ExportModel() {

		//Construct a new vanilla one
		this.setId(null);
		this.setMergeModel(new MergeModel());		
		this.setExportedDatatableModel(new ExportedDatatableModel());
		
	}	


	public void setExportedDatatableModel(
			ExportedDatatableModel exportedDatatableModel) {
		this.exportedDatatableModel = exportedDatatableModel;
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

	public ExportedDatatableModel getExportedDatatableModel() {		
		return this.exportedDatatableModel;
	}

}
