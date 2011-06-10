package org.cggh.tools.dataMerger.users;

import org.cggh.tools.dataMerger.users.userbases.UserbaseModel;

public class UsersCRUD {
	
	
	private UserbaseModel userbaseModel;

	public void setUserbaseModel(UserbaseModel userbaseModel) {
		this.userbaseModel = userbaseModel;
	}

	public UserbaseModel getUserbaseModel() {
		return userbaseModel;
	}

}
