package org.cggh.tools.dataMerger.data.resolutionsByColumn;

import org.cggh.tools.dataMerger.data.merges.MergeModel;


public class ResolutionByColumnModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 740975466778430807L;
	private MergeModel mergeModel;
	private Integer columnNumber;
	private ProblemByColumnModel problemByColumnModel;
	private Integer conflictsCount;
	private SolutionByColumnModel solutionByColumnModel;
	private String constant;

	public ResolutionByColumnModel() {

		this.setMergeModel(new MergeModel());
		this.setProblemByColumnModel(new ProblemByColumnModel());
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

	public void setProblemByColumnModel(
			ProblemByColumnModel problemByColumnModel) {
		this.problemByColumnModel = problemByColumnModel;
		
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

	public ProblemByColumnModel getProblemByColumnModel() {
		return this.problemByColumnModel;
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
