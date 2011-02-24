package org.cggh.tools.dataMerger.data.merges;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.DataModel;
import org.cggh.tools.dataMerger.data.datatables.DatatableModel;
import org.cggh.tools.dataMerger.data.joins.JoinsModel;
import org.cggh.tools.dataMerger.data.uploads.UploadModel;
import org.cggh.tools.dataMerger.data.users.UserModel;

public class MergeModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5306097444255740574L;
	private DataModel dataModel;
	private UserModel userModel;
	private JoinsModel joinsModel;
	
	private Integer id = null;
	private Timestamp createdDatetime = null;
	private Timestamp updatedDatetime = null;
	
	//TODO: Update these to use UploadModel. Ref using mergeModel.getUpload1Model().getId();
	private UploadModel upload1Model;
	private UploadModel upload2Model;
	private DatatableModel datatable1Model;
	private DatatableModel datatable2Model;

	private Integer createdByUserId = null;
	private Integer totalDuplicateKeysCount = null;
	private CachedRowSet joinsAsCachedRowSet;
	
	
	public MergeModel() {
		
		this.setDataModel(new DataModel());
		this.setUserModel(new UserModel());		
		this.setJoinsModel(new JoinsModel());	
		
		this.setUpload1Model(new UploadModel());
		this.setUpload2Model(new UploadModel());
		this.setDatatable1Model(new DatatableModel());
		this.setDatatable2Model(new DatatableModel());	
		
	}
	






	public void setDataModel (final DataModel dataModel) {
        this.dataModel  = dataModel;
    }
    public DataModel getDataModel () {
        return this.dataModel;
    } 
	
    public void setUserModel (final UserModel userModel) {
        this.userModel  = userModel;
    }
    public UserModel getCreatedByUserModel () {
        return this.userModel;
    }     
    
    public void setJoinsModel (final JoinsModel joinsModel) {
        this.joinsModel  = joinsModel;
    }
    public JoinsModel getJoinsModel () {
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


	public void setUpload1Model(UploadModel uploadModel) {
		this.upload1Model = uploadModel;
	}

	public void setUpload2Model(UploadModel uploadModel) {
		this.upload2Model = uploadModel;
	}	
	
	   
	public Integer getTotalDuplicateKeysCount() {
		return this.totalDuplicateKeysCount;
	}
		
	public void setTotalDuplicateKeysCount(Integer totalDuplicateKeysCount) {
		this.totalDuplicateKeysCount = totalDuplicateKeysCount;
	}






	public Integer getCreatedByUserId() {
		return this.createdByUserId;
	}
	public void setCreatedByUserId(Integer createdByUserId) {
		this.createdByUserId = createdByUserId;
	}


	public DatatableModel getDatatable1Model() {
		return this.datatable1Model;
	}

	public DatatableModel getDatatable2Model() {
		return this.datatable2Model;
	}

	public UploadModel getUpload1Model() {
		return this.upload1Model;
	}


	public UploadModel getUpload2Model() {
		return this.upload2Model;
	}
	

	public void setJoinsAsCachedRowSet(
			CachedRowSet cachedRowSet) {

		this.joinsAsCachedRowSet = cachedRowSet;
	}
	public CachedRowSet getJoinsAsCachedRowSet() {
		return this.joinsAsCachedRowSet;
	}

	
	
	
	
	
	   public void setMergeModelById (final Integer id) {
		    
	    	this.setId(id);

			try {
				
				Connection connection = this.getDataModel().getNewConnection();
				 
				if (!connection.isClosed()) {
			
			
					
				      try{
				          PreparedStatement preparedStatement = connection.prepareStatement(
				        		  	"SELECT id, upload_1_id, upload_2_id, created_by_user_id, created_datetime, updated_datetime, total_duplicate_keys_count FROM merge " + 
				        		  	"WHERE id = ? AND created_by_user_id = ?;"
				        		  	);
				          preparedStatement.setInt(1, this.getId());
				          preparedStatement.setInt(2, this.userModel.getId());
				          preparedStatement.executeQuery();
				          ResultSet resultSet = preparedStatement.getResultSet();
				          
				          // There may be no such record
				          if (resultSet.next()) {
				        	  
				        	  resultSet.first();
				        	  this.getUpload1Model().setId(resultSet.getInt("upload_1_id"));
				        	  this.getUpload2Model().setId(resultSet.getInt("upload_2_id"));
				        	  this.setCreatedByUserId(resultSet.getInt("created_by_user_id"));
				        	  this.setCreatedDatetime(resultSet.getTimestamp("created_datetime"));
				        	  this.setUpdatedDatetime(resultSet.getTimestamp("updated_datetime"));
				        	  //this.getDatatable1Model().setDuplicateKeysCount(resultSet.getInt("datatable_1_duplicate_keys_count"));
				        	  //this.getDatatable2Model().setDuplicateKeysCount(resultSet.getInt("datatable_1_duplicate_keys_count"));
				        	  this.setTotalDuplicateKeysCount(resultSet.getInt("total_duplicate_keys_count"));
				        	  
				          } else {
				        	  //TODO: proper logging and error handling
				        	  System.out.println("Db query gives !resultSet.next()");
				          }
				          
				          preparedStatement.close();

				        }
				        catch(SQLException sqlException){
				        	System.out.println("<p>" + sqlException + "</p>");
					    	sqlException.printStackTrace();
				        } 	
				        
					    //TODO: Get the rest of the merge data (column joins, etc.).
				        this.getJoinsModel().setJoinsModelByMergeId(this.getId(), connection);
				        
				        
				
					connection.close();
					
				} else {
					
					System.out.println("connection.isClosed");
				}
					
			} 
			catch (Exception e) {
				System.out.println("Exception in setJoinModelByMergeId");
				e.printStackTrace();
			}


	    }

















	
	
	
}