package org.cggh.tools.dataMerger.data.users;

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

	
}