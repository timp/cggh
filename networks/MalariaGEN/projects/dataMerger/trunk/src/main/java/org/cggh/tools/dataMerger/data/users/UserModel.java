package org.cggh.tools.dataMerger.data.users;

public class UserModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 60066823075291273L;

	private String username = null;
	
	private Boolean registered = null;
	
	public UserModel() {

	}
	
	public UserModel(String username) {
		
		//TODO: This needs to be set by a controller.
		this.setRegistered(false);
		
	}	

	public void setUsername (final String username) {
		
		this.username = username;
	}
	public String getUsername () {
		
		return this.username;
	}	
	
	public boolean isRegistered () {
		return this.registered;
	}

	public void setRegistered (final Boolean registered) {
		
		this.registered = registered;
	}
	
}