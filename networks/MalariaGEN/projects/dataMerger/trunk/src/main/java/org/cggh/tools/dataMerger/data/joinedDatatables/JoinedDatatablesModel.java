package org.cggh.tools.dataMerger.data.joinedDatatables;

import java.sql.Connection;

import org.cggh.tools.dataMerger.data.merges.MergeModel;

public class JoinedDatatablesModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4036224766935438609L;

	public JoinedDatatableModel retrieveJoinedDatatableAsJoinedDatatableModelUsingMergeId(
			Integer mergeId, Connection connection) {
		
		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);
		
		JoinedDatatableModel joinedDatatableModel = new JoinedDatatableModel();
		
		// TODO Auto-generated method stub
		
		
		return joinedDatatableModel;
	}

	public JoinedDatatableModel retrieveJoinedDatatableAsJoinedDatatableModelThroughCreatingJoinedDatatableUsingMergeId(
			Integer mergeId, Connection connection) {

		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);		
		
		JoinedDatatableModel joinedDatatableModel = new JoinedDatatableModel();
		
		// TODO Auto-generated method stub
		
		
		return joinedDatatableModel;
	}

}
