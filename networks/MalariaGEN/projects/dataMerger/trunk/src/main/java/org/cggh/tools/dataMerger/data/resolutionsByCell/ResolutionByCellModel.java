package org.cggh.tools.dataMerger.data.resolutionsByCell;

import org.cggh.tools.dataMerger.data.joinedKeytables.JoinedKeytableModel;
import org.cggh.tools.dataMerger.data.merges.MergeModel;


public class ResolutionByCellModel implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 2343864992845023644L;
	private MergeModel mergeModel;
	private JoinedKeytableModel joinedKeytableModel;
	private SolutionByCellModel solutionByCellModel;
	private String constant;

	public ResolutionByCellModel() {

		this.setMergeModel(new MergeModel());
		this.setJoinedKeytableModel(new JoinedKeytableModel());
		this.setSolutionByCellModel(new SolutionByCellModel());
	}

	public void setSolutionByCellModel(
			SolutionByCellModel solutionByCellModel) {
		this.solutionByCellModel = solutionByCellModel;
		
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

	public JoinedKeytableModel getJoinedKeytableModel() {
		return this.joinedKeytableModel;
	}



	public SolutionByCellModel getSolutionByCellModel() {

		return this.solutionByCellModel;
	}

	public String getConstant() {
		return this.constant;
	}

	public void setConstant(String constant) {
		this.constant = constant;
	}

	
	
}