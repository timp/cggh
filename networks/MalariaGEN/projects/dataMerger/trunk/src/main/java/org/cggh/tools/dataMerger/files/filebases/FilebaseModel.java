package org.cggh.tools.dataMerger.files.filebases;

public class FilebaseModel {

	private Boolean existent = null;
	private Boolean readable = null;
	private Boolean writable = null;
	private String serverPath = null;
	private String versionAsString = null;
	private String[] filesAsStringArray = null;
	private String filebaseInstallationLogAsCSVFilePathRelativeToFilebaseServerPath = null;
	private Boolean unix = null;
	private Boolean mac = null;
	private Boolean windows = null;
	private String filepathSeparator = null;

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

	public void setFilesAsStringArray(String[] filesAsStringArray) {
		this.filesAsStringArray = filesAsStringArray;
	}

	public String[] getFilesAsStringArray() {
		return filesAsStringArray;
	}

	public void setExistent(Boolean existent) {
		this.existent = existent;
	}

	public Boolean isExistent() {
		return existent;
	}

	public void setFilebaseInstallationLogAsCSVFilePathRelativeToFilebaseServerPath(
			String filebaseInstallationLogAsCSVFilePathRelativeToFilebaseServerPath) {
		this.filebaseInstallationLogAsCSVFilePathRelativeToFilebaseServerPath = filebaseInstallationLogAsCSVFilePathRelativeToFilebaseServerPath;
	}

	public String getFilebaseInstallationLogAsCSVFilePathRelativeToFilebaseServerPath() {
		return filebaseInstallationLogAsCSVFilePathRelativeToFilebaseServerPath;
	}

	public void setUnix(Boolean unix) {
		this.unix = unix;
	}

	public Boolean isUnix() {
		return unix;
	}

	public void setMac(Boolean mac) {
		this.mac = mac;
	}

	public Boolean isMac() {
		return mac;
	}

	public void setWindows(Boolean windows) {
		this.windows = windows;
	}

	public Boolean isWindows() {
		return windows;
	}

	public void setFilepathSeparator(String filepathSeparator) {
		this.filepathSeparator = filepathSeparator;
	}

	public String getFilepathSeparator() {
		return filepathSeparator;
	}



}
