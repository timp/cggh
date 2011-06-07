package org.cggh.tools.dataMerger.data.files;

import java.sql.Timestamp;

import org.cggh.tools.dataMerger.data.datatables.DatatableModel;
import org.cggh.tools.dataMerger.data.users.UserModel;

public class FileModel implements java.io.Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6542800631952380713L;

	private Integer id;
	private String filename;
	private String filepath;
	private Long fileSizeInBytes;
	private UserModel createdByUserModel;
	private Timestamp createdDatetime;
	private DatatableModel datatableModel;
	private Integer rowsCount;
	private Integer columnsCount;
	private FileOriginModel fileOriginModel;

	public FileModel() {

	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFileSizeInBytes(Long fileSizeInBytes) {
		this.fileSizeInBytes = fileSizeInBytes;
	}

	public Long getFileSizeInBytes() {
		return fileSizeInBytes;
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

	public void setRowsCount(Integer rowsCount) {
		this.rowsCount = rowsCount;
	}

	public Integer getRowsCount() {
		return rowsCount;
	}

	public void setColumnsCount(Integer columnsCount) {
		this.columnsCount = columnsCount;
	}

	public Integer getColumnsCount() {
		return columnsCount;
	}

	public void setFileOriginModel(FileOriginModel fileOriginModel) {
		this.fileOriginModel = fileOriginModel;
	}

	public FileOriginModel getFileOriginModel() {
		return fileOriginModel;
	}

	public void setDatatableModel(DatatableModel datatableModel) {
		this.datatableModel = datatableModel;
	}

	public DatatableModel getDatatableModel() {
		return datatableModel;
	}	




}
