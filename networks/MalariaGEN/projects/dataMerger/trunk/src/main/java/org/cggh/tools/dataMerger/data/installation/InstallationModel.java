package org.cggh.tools.dataMerger.data.installation;

import java.sql.Timestamp;

import org.cggh.tools.dataMerger.data.users.UserModel;

public class InstallationModel {

	
	  private Integer id = null;
	  private Integer majorVersionNumber = null;
	  private Integer minorVersionNumber = null;
	  private Integer revisionVersionNumber = null;
	  private UserModel createdByUserModel = null;
	  private Timestamp createdDatetime = null;
	  
	  public InstallationModel () {
		  
		  this.setCreatedByUserModel(new UserModel());
		  
	  }
	  
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getId() {
		return id;
	}
	public void setMajorVersionNumber(Integer majorVersionNumber) {
		this.majorVersionNumber = majorVersionNumber;
	}
	public Integer getMajorVersionNumber() {
		return majorVersionNumber;
	}
	public void setMinorVersionNumber(Integer minorVersionNumber) {
		this.minorVersionNumber = minorVersionNumber;
	}
	public Integer getMinorVersionNumber() {
		return minorVersionNumber;
	}
	public void setRevisionVersionNumber(Integer revisionVersionNumber) {
		this.revisionVersionNumber = revisionVersionNumber;
	}
	public Integer getRevisionVersionNumber() {
		return revisionVersionNumber;
	}
	public void setCreatedByUserModel(UserModel createdByUserModel) {
		this.createdByUserModel = createdByUserModel;
	}
	public UserModel getCreatedByUserModel() {
		return createdByUserModel;
	}
	public void setCreatedDatetime(Timestamp createdDatetime) {
		this.createdDatetime = createdDatetime;
	}
	public Timestamp getCreatedDatetime() {
		return createdDatetime;
	}	
	
}
