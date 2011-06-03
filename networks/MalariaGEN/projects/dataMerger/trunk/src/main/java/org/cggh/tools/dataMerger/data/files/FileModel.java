package org.cggh.tools.dataMerger.data.files;

import java.sql.Timestamp;

import org.cggh.tools.dataMerger.data.users.UserModel;

public class FileModel implements java.io.Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6542800631952380713L;

	private Integer id;
	private UserModel createdByUserModel;
	private Timestamp createdDatetime;
	private String repositoryFilePath;
	private Long fileSizeInBytes;

	public FileModel() {

		
	}	




	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getId() {
		return this.id;
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




	public void setRepositoryFilePath(String repositoryFilePath) {
		this.repositoryFilePath = repositoryFilePath;
	}




	public String getRepositoryFilePath() {
		return repositoryFilePath;
	}




	public void setFileSizeInBytes(Long fileSizeInBytes) {
		this.fileSizeInBytes = fileSizeInBytes;
	}




	public Long getFileSizeInBytes() {
		return fileSizeInBytes;
	}


}
