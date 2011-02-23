package org.cggh.tools.dataMerger.data.joins;

import org.cggh.tools.dataMerger.data.merges.MergeModel;



public class JoinModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4112178863119955390L;
	
	private MergeModel mergeModel;
	
	
	private Integer columnNumber;
	private Boolean key;
	private String datatable1ColumnName;
	private String datatable2ColumnName;
	private String columnName;

	private Integer id;

	private String constant1;

	private String constant2;


	public JoinModel() {

		this.setMergeModel(new MergeModel());
		
	}


	private void setMergeModel(MergeModel mergeModel) {
		this.mergeModel = mergeModel;
	}

	public MergeModel getMergeModel() {
		return this.mergeModel;
	}


	public void setColumnNumber(Integer columnNumber) {
		this.columnNumber = columnNumber;
	}


	public void setKey(Boolean key) {
		this.key = key;
	}


	public void setDatatable1ColumnName(String datatable1ColumnName) {
		this.datatable1ColumnName = datatable1ColumnName;
	}


	public void setDatatable2ColumnName(String datatable2ColumnName) {
		this.datatable2ColumnName = datatable2ColumnName;
	}


	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}


	public int getColumnNumber() {
		return this.columnNumber;
	}


	public boolean getKey() {
		return this.key;
	}


	public String getDatatable1ColumnName() {
		return this.datatable1ColumnName;
	}


	public String getDatatable2ColumnName() {
		return this.datatable2ColumnName;
	}


	public String getColumnName() {
		return this.columnName;
	}


	public void setId(final Integer id) {
		this.id = id;
	}
	public Integer getId() {
		return this.id;
	}


	public void setConstant1(final String constant1) {
		this.constant1 = constant1;
	}


	public void setConstant2(final String constant2) {

		this.constant2 = constant2;
		
	}




    	
	
 




 
}
