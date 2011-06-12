package org.cggh.tools.dataMerger.data.users;

import java.util.ArrayList;
import java.util.logging.Logger;


public class UserModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 60066823075291273L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.users");

	private Integer id = null;
	private String username = null;
	private Boolean registered = null;
	private ArrayList<String> rolesAsStringArrayList = null;
	private Boolean authenticated;


	public UserModel() {

	}

    
	public void setId (final Integer id) {
		this.id = id;
	}
	public Integer getId () {
		return this.id;
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


	public Logger getLogger() {
		return logger;
	}

	public Boolean isUserInRole (String role) {
		
		Boolean userInRole = null;
		
		if (getRolesAsStringArrayList().contains(role)) {
			userInRole = true;
		} else {
			userInRole = false;
		}
		
		return userInRole;
		
	}


	public void setRolesAsStringArrayList(ArrayList<String> rolesAsStringArrayList) {
		this.rolesAsStringArrayList = rolesAsStringArrayList;
	}


	public ArrayList<String> getRolesAsStringArrayList() {
		return rolesAsStringArrayList;
	}


	public void setAuthenticated(Boolean authenticated) {
		this.authenticated = authenticated;
	}


	public Boolean isAuthenticated() {
		return authenticated;
	}


}