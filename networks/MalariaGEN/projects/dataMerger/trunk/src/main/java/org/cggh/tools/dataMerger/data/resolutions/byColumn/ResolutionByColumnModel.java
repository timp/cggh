package org.cggh.tools.dataMerger.data.resolutions.byColumn;

import org.cggh.tools.dataMerger.data.merges.MergeModel;


public class ResolutionByColumnModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 740975466778430807L;
	private MergeModel mergeModel;
	private Integer columnNumber;
	private ConflictModel conflictModel;
	private Integer conflictsCount;
	private SolutionByColumnModel solutionByColumnModel;
	private String constant;

	public ResolutionByColumnModel() {

		this.setMergeModel(new MergeModel());
		this.setConflictModel(new ConflictModel());
		this.setSolutionByColumnModel(new SolutionByColumnModel());
	}

	public void setSolutionByColumnModel(
			SolutionByColumnModel solutionByColumnModel) {
		this.solutionByColumnModel = solutionByColumnModel;
		
	}

	public void setMergeModel(MergeModel mergeModel) {
		this.mergeModel = mergeModel;
	}

	public void setColumnNumber(Integer columnNumber) {
		this.columnNumber = columnNumber;
	}

	public void setConflictModel(
			ConflictModel conflictModel) {
		this.conflictModel = conflictModel;
		
	}

	public void setConflictsCount(Integer conflictsCount) {
		this.conflictsCount = conflictsCount;
	}

	public MergeModel getMergeModel() {

		return this.mergeModel;
	}

	public Integer getColumnNumber() {
		return this.columnNumber;
	}

	public ConflictModel getConflictModel() {
		return this.conflictModel;
	}

	public Integer getConflictsCount() {
		return this.conflictsCount;
	}

	public SolutionByColumnModel getSolutionByColumnModel() {

		return this.solutionByColumnModel;
	}

	public String getConstant() {
		return this.constant;
	}

	public void setConstant(String constant) {
		this.constant = constant;
	}

	
	
}
