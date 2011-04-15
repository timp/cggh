package org.cggh.tools.dataMerger.files.filebases;

import java.util.List;

public class FilebaseModel {

	
	private Boolean readable = null;
	private Boolean writable = null;
	private String serverPath = null;
	private String versionAsString = null;
	private List<String> filesAsStringList = null;

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

	public void setFilesAsStringList(List<String> filesAsStringList) {
		this.filesAsStringList = filesAsStringList;
	}

	public List<String> getFilesAsStringList() {
		return filesAsStringList;
	}


}
