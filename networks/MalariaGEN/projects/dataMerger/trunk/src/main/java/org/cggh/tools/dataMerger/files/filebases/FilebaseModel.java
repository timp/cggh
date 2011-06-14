package org.cggh.tools.dataMerger.files.filebases;

import java.util.ArrayList;

public class FilebaseModel {

	private Boolean existent = null;
	private Boolean readable = null;
	private Boolean writable = null;
	private String serverPath = null;
	private String versionAsString = null;
	private ArrayList<String> filesAsStringArrayList = null;
	private String fileRepositoryInstallationLogPathRelativeToRepositoryBasePath = null;
	private String operatingSystem;
	private String filepathSeparator = null;

	public FilebaseModel () {

		setFilesAsStringArrayList(new ArrayList<String>());
	}
	
	public void setReadable(Boolean readable) {
		this.readable = readable;
	}

	public Boolean isReadable() {
		return readable;
	}

	public void setWritable(Boolean writable) {
		this.writable = writable;
	}

	public Boolean isWritable() {
		return writable;
	}

	public void setServerPath(String serverPath) {
		this.serverPath = serverPath;
	}

	public String getServerPath() {
		return serverPath;
	}

	public void setVersionAsString(String versionAsString) {
		this.versionAsString = versionAsString;
	}

	public String getVersionAsString() {
		return versionAsString;
	}

	public void setFilesAsStringArrayList(ArrayList<String> filesAsStringArrayList) {
		this.filesAsStringArrayList = filesAsStringArrayList;
	}

	public ArrayList<String> getFilesAsStringArrayList() {
		return filesAsStringArrayList;
	}

	public void setExistent(Boolean existent) {
		this.existent = existent;
	}

	public Boolean isExistent() {
		return existent;
	}

	public void setFilebaseInstallationLogAsCSVFilePathRelativeToFilebaseServerPath(
			String fileRepositoryInstallationLogPathRelativeToRepositoryBasePath) {
		this.fileRepositoryInstallationLogPathRelativeToRepositoryBasePath = fileRepositoryInstallationLogPathRelativeToRepositoryBasePath;
	}

	public String getFilebaseInstallationLogAsCSVFilePathRelativeToFilebaseServerPath() {
		return fileRepositoryInstallationLogPathRelativeToRepositoryBasePath;
	}


	public void setFilepathSeparator(String filepathSeparator) {
		this.filepathSeparator = filepathSeparator;
	}

	public String getFilepathSeparator() {
		return filepathSeparator;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}



}
