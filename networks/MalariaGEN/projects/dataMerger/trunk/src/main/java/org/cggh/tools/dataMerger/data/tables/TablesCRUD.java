package org.cggh.tools.dataMerger.data.tables;

import java.util.logging.Logger;

import org.cggh.tools.dataMerger.data.DataModel;
import org.cggh.tools.dataMerger.data.users.UserModel;

public class TablesCRUD {

	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.databases");
	
	private DataModel dataModel = null;
	private UserModel userModel = null;

	public TablesCRUD () {
		
		this.setDataModel(new DataModel());
		this.setUserModel(new UserModel());
		
	}

	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
	}

	public DataModel getDataModel() {
		return dataModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}

	public UserModel getUserModel() {
		return userModel;
	}	
	
}
