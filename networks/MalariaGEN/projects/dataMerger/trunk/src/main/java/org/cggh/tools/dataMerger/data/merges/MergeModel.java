package org.cggh.tools.dataMerger.data.merges;

import java.sql.Timestamp;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.DataModel;
import org.cggh.tools.dataMerger.data.datatables.DatatableModel;
import org.cggh.tools.dataMerger.data.files.FileModel;
import org.cggh.tools.dataMerger.data.joinedDatatables.JoinedDatatableModel;
import org.cggh.tools.dataMerger.data.joinedKeytables.JoinedKeytableModel;
import org.cggh.tools.dataMerger.data.joins.JoinsCRUD;
import org.cggh.tools.dataMerger.data.users.UserModel;

public class MergeModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5306097444255740574L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.merges");
	
	private UserModel createdByUserModel;
	private JoinsCRUD joinsModel;
	
	private Integer id = null;

	private FileModel file1Model;
	private FileModel file2Model;
	
	private Timestamp createdDatetime = null;
	private Timestamp updatedDatetime = null;
	
	private DatatableModel datatable1Model;
	private DatatableModel datatable2Model;

	private Integer totalDuplicateKeysCount = null;
	
	
	//FIXME: This can't be right?
	private CachedRowSet joinsAsCachedRowSet;
	
	private Integer totalConflictsCount;
	private JoinedKeytableModel joinedKeytableModel;
	private JoinedDatatableModel joinedDatatableModel;

	
	
	public MergeModel() {
		
		
		//TODO: Need all this stuff? Operate on a new when needed policy.
		this.setCreatedByUserModel(new UserModel());		
		this.setJoinsModel(new JoinsCRUD());	
		
		this.setFile1Model(new FileModel());
		this.setFile2Model(new FileModel());
		this.setDatatable1Model(new DatatableModel());
		this.setDatatable2Model(new DatatableModel());	
		
		this.setJoinedKeytableModel(new JoinedKeytableModel());
		this.setJoinedDatatableModel(new JoinedDatatableModel());
		
	}
	






	public void setJoinedDatatableModel(
			JoinedDatatableModel joinedDatatableModel) {
		this.joinedDatatableModel = joinedDatatableModel; 
	}




	
    public void setCreatedByUserModel (final UserModel userModel) {
        this.createdByUserModel  = userModel;
    }
    public UserModel getCreatedByUserModel () {
        return this.createdByUserModel;
    }     
    
    public void setJoinsModel (final JoinsCRUD joinsModel) {
        this.joinsModel  = joinsModel;
    }
    public JoinsCRUD getJoinsModel () {
        return this.joinsModel;
    } 
    
	public void setId (final Integer id) {
		this.id = id;
	}
	public Integer getId () {
		return this.id;
	}	


	public void setCreatedDatetime (final Timestamp datetime) {
		this.createdDatetime = datetime;
	}
	public Timestamp getCreatedDatetime () {
		return this.createdDatetime;
	}	
	
	public void setUpdatedDatetime (final Timestamp datetime) {
		this.updatedDatetime = datetime;
	}
	public Timestamp getUpdatedDatetime () {
		return this.updatedDatetime;
	}

	
	
	
	
	
    public void setDatatable1Model(DatatableModel datatableModel) {
    	this.datatable1Model = datatableModel;
	}

	public void setDatatable2Model(DatatableModel datatableModel) {
		this.datatable2Model = datatableModel;
	}


	public Integer getTotalDuplicateKeysCount() {
		return this.totalDuplicateKeysCount;
	}
		
	public void setTotalDuplicateKeysCount(Integer totalDuplicateKeysCount) {
		this.totalDuplicateKeysCount = totalDuplicateKeysCount;
	}







	public DatatableModel getDatatable1Model() {
		return this.datatable1Model;
	}

	public DatatableModel getDatatable2Model() {
		return this.datatable2Model;
	}


	public void setJoinsAsCachedRowSet(
			CachedRowSet cachedRowSet) {

		this.joinsAsCachedRowSet = cachedRowSet;
	}
	public CachedRowSet getJoinsAsCachedRowSet() {
		return this.joinsAsCachedRowSet;
	}







	public void setTotalConflictsCount(Integer totalConflictsCount) {
		this.totalConflictsCount = totalConflictsCount;
	}

	public Integer getTotalConflictsCount() {
		return this.totalConflictsCount;
	}







	public void setJoinedKeytableModel(
			JoinedKeytableModel joinedKeytableModel) {

		this.joinedKeytableModel = joinedKeytableModel; 
	}

	public JoinedKeytableModel getJoinedKeytableModel() {

		return this.joinedKeytableModel;
	}







	public JoinedDatatableModel getJoinedDatatableModel() {

		return this.joinedDatatableModel;
	}



	public void setFile1Model(FileModel file1Model) {
		this.file1Model = file1Model;
	}







	public FileModel getFile1Model() {
		return file1Model;
	}







	public void setFile2Model(FileModel file2Model) {
		this.file2Model = file2Model;
	}







	public FileModel getFile2Model() {
		return file2Model;
	}







	public Logger getLogger() {
		return logger;
	}

	
	
	
	
	


















	
	
	
}