package org.cggh.tools.dataMerger.data.exports;

import org.cggh.tools.dataMerger.data.DataModel;
import org.cggh.tools.dataMerger.data.users.UserModel;

public class ExportsModel {

	private DataModel dataModel;
	private UserModel userModel;
	
	public ExportsModel() {

		this.setDataModel(new DataModel());
		this.setUserModel(new UserModel());			
	
		
	}


	public ExportModel retrieveExportAsExportModelThroughCreatingExportUsingExportModel(
			ExportModel exportModel) {
		
		
		// TODO
		//////////////////
		
		
		
		
		return exportModel;
	}


	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
	}


	public DataModel getDataModel() {
		return this.dataModel;
	}


	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}


	public UserModel getUserModel() {
		return this.userModel;
	}

}
