package org.cggh.tools.dataMerger.data.tables;

import java.util.logging.Logger;

import org.cggh.tools.dataMerger.data.databases.DatabaseModel;
import org.cggh.tools.dataMerger.data.users.UserModel;

public class TablesCRUD {

	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.databases");
	
	private DatabaseModel databaseModel = null;
	private UserModel userModel = null;

	public TablesCRUD () {
		
		this.setDatabaseModel(new DatabaseModel());
		this.setUserModel(new UserModel());
		
	}

	public void setDatabaseModel(DatabaseModel databaseModel) {
		this.databaseModel = databaseModel;
	}

	public DatabaseModel getDatabaseModel() {
		return databaseModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}

	public UserModel getUserModel() {
		return userModel;
	}	
	
}
