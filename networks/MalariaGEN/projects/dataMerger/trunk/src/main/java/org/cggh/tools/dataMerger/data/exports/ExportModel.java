package org.cggh.tools.dataMerger.data.exports;

import org.cggh.tools.dataMerger.data.files.FileModel;
import org.cggh.tools.dataMerger.data.mergedDatatables.MergedDatatableModel;
import org.cggh.tools.dataMerger.data.merges.MergeModel;
import org.cggh.tools.dataMerger.data.users.UserModel;

public class ExportModel implements java.io.Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6542800631952380713L;

	private Integer id;
	private FileModel mergedFileAsFileModel;
	private MergeModel mergeModel;
	private UserModel createdByUserModel;
	private String joinsRecordFilepath;
	private String resolutionsRecordFilepath;
	private String settingsRecordFilepath;


	private MergedDatatableModel mergedDatatableModel;

	public ExportModel() {

		//Construct a new vanilla one
		this.setId(null);
		this.setMergeModel(new MergeModel());		
		this.setMergedDatatableModel(new MergedDatatableModel());
		
	}	


	public void setMergedDatatableModel(
			MergedDatatableModel mergedDatatableModel) {
		this.mergedDatatableModel = mergedDatatableModel;
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

	public MergedDatatableModel getMergedDatatableModel() {		
		return this.mergedDatatableModel;
	}



	public void setCreatedByUserModel(UserModel createdByUserModel) {
		this.createdByUserModel = createdByUserModel;
	}


	public UserModel getCreatedByUserModel() {
		return createdByUserModel;
	}


	public void setMergedFileAsFileModel(FileModel mergedFileAsFileModel) {
		this.mergedFileAsFileModel = mergedFileAsFileModel;
	}


	public FileModel getMergedFileAsFileModel() {
		return mergedFileAsFileModel;
	}


	public void setJoinsRecordFilepath(String joinsRecordFilepath) {
		this.joinsRecordFilepath = joinsRecordFilepath;
	}


	public String getJoinsRecordFilepath() {
		return joinsRecordFilepath;
	}


	public void setResolutionsRecordFilepath(String resolutionsRecordFilepath) {
		this.resolutionsRecordFilepath = resolutionsRecordFilepath;
	}


	public String getResolutionsRecordFilepath() {
		return resolutionsRecordFilepath;
	}


	public void setSettingsRecordFilepath(String settingsRecordFilepath) {
		this.settingsRecordFilepath = settingsRecordFilepath;
	}


	public String getSettingsRecordFilepath() {
		return settingsRecordFilepath;
	}

}
