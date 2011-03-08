package org.cggh.tools.dataMerger.data.resolutionsByRow;

import org.cggh.tools.dataMerger.data.joinedKeytables.JoinedKeytableModel;
import org.cggh.tools.dataMerger.data.merges.MergeModel;


public class ResolutionByRowModel implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -4151089078111212868L;
	private MergeModel mergeModel;
	private JoinedKeytableModel joinedKeytableModel;	
	private Integer conflictsCount;
	private SolutionByRowModel solutionByRowModel;
	private String constant;

	public ResolutionByRowModel() {

		this.setMergeModel(new MergeModel());
		this.setJoinedKeytableModel(new JoinedKeytableModel());
		this.setSolutionByRowModel(new SolutionByRowModel());
	}

	public void setSolutionByRowModel(
			SolutionByRowModel solutionByRowModel) {
		this.solutionByRowModel = solutionByRowModel;
		
	}

	public void setMergeModel(MergeModel mergeModel) {
		this.mergeModel = mergeModel;
	}
	public MergeModel getMergeModel() {

		return this.mergeModel;
	}

	public void setJoinedKeytableModel(
			JoinedKeytableModel joinedKeytableModel) {
		this.joinedKeytableModel = joinedKeytableModel;
		
	}

	public void setConflictsCount(Integer conflictsCount) {
		this.conflictsCount = conflictsCount;
	}
	public Integer getConflictsCount() {
		return this.conflictsCount;
	}

	public JoinedKeytableModel getJoinedKeytableModel() {
		return this.joinedKeytableModel;
	}



	public SolutionByRowModel getSolutionByRowModel() {

		return this.solutionByRowModel;
	}

	public String getConstant() {
		return this.constant;
	}

	public void setConstant(String constant) {
		this.constant = constant;
	}

	
	
}
