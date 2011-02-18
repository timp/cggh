package org.cggh.tools.dataMerger.data.merges;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.cggh.tools.dataMerger.data.DataModel;
import org.cggh.tools.dataMerger.data.datatables.DatatableModel;
import org.cggh.tools.dataMerger.data.uploads.UploadModel;
import org.cggh.tools.dataMerger.data.users.UserModel;

public class MergeModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5306097444255740574L;
	private DataModel dataModel;
	private UserModel userModel;
	
	private Integer id = null;
	private Timestamp createdDatetime = null;
	private Timestamp updatedDatetime = null;
	
	//TODO: Update these to use UploadModel. Ref using mergeModel.getUpload1Model().getId();
	private Integer upload1Id = null;
	private Integer upload2Id = null;
	private Integer createdByUserId = null;
	private Integer totalDuplicateKeysCount = null;
	
	//TODO: Update these to use DatatableModel. Ref using mergeModel.getDatatable1Model().getDuplicateKeysCount();	
	private Integer datatable2DuplicateKeysCount = null;
	private Integer datatable1DuplicateKeysCount = null;	
	
	public MergeModel() {
		
		this.setDataModel(new DataModel());
		this.setUserModel(new UserModel());		
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
    public UserModel getUserModel () {
        return this.userModel;
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

	public void setUpload1Id(Integer upload1Id) {
		this.upload1Id = upload1Id;
	}
	public Integer getUpload1Id() {
		return this.upload1Id;
	}	

	public void setUpload2Id(Integer upload2Id) {
		this.upload2Id = upload2Id;
	}
	public Integer getUpload2Id() {
		return this.upload2Id;
	}

	public void setCreatedByUserId(Integer createdByUserId) {
		this.createdByUserId = createdByUserId;
	}
	public Integer getCreatedByUserId() {
		return this.createdByUserId;
	}

	public void setTotalDuplicateKeysCount(Integer totalDuplicateKeysCount) {
		this.totalDuplicateKeysCount = totalDuplicateKeysCount;
	}
	public Integer getTotalDuplicateKeysCount() {
		return this.totalDuplicateKeysCount;
	}

	public void setDatatable2DuplicateKeysCount(Integer datatable2DuplicateKeysCount) {
		this.datatable2DuplicateKeysCount = datatable2DuplicateKeysCount;
	}
	public Integer getDatatable2DuplicateKeysCount() {
		return this.datatable2DuplicateKeysCount;
	}

	public void setDatatable1DuplicateKeysCount(Integer datatable1DuplicateKeysCount) {
		this.datatable1DuplicateKeysCount = datatable1DuplicateKeysCount;
	}	
	public Integer setDatatable1DuplicateKeysCount() {
		return this.datatable1DuplicateKeysCount;
	}	
	
	   public void setMergeModelById (final Integer id) {
		    
	    	this.setId(id);

			try {
				
				Connection connection = this.getDataModel().getConnection();
				 
				if (!connection.isClosed()) {
			
			
					
				      try{
				          PreparedStatement preparedStatement = connection.prepareStatement(
				        		  	"SELECT id, upload_1_id, upload_2_id, created_by_user_id, created_datetime, updated_datetime, datatable_1_duplicate_keys_count, datatable_2_duplicate_keys_count, total_duplicate_keys_count FROM merge " +
				        		  	"WHERE id = ? AND created_by_user_id = ?;"
				        		  	);
				          preparedStatement.setInt(1, this.getId());
				          preparedStatement.setInt(2, this.userModel.getId());
				          preparedStatement.executeQuery();
				          ResultSet resultSet = preparedStatement.getResultSet();
				          
				          // There may be no such record
				          if (resultSet.next()) {
				        	  
				        	  resultSet.first();
				        	  this.setUpload1Id(resultSet.getInt("upload_1_id"));
				        	  this.setUpload2Id(resultSet.getInt("upload_2_id"));
				        	  this.setCreatedByUserId(resultSet.getInt("created_by_user_id"));
				        	  this.setCreatedDatetime(resultSet.getTimestamp("created_datetime"));
				        	  this.setUpdatedDatetime(resultSet.getTimestamp("updated_datetime"));
				        	  this.setDatatable1DuplicateKeysCount(resultSet.getInt("datatable_1_duplicate_keys_count"));
				        	  this.setDatatable2DuplicateKeysCount(resultSet.getInt("datatable_1_duplicate_keys_count"));
				        	  this.setTotalDuplicateKeysCount(resultSet.getInt("total_duplicate_keys_count"));
				        	  
				          } else {
				        	  //TODO: proper logging and error handling
				        	  System.out.println("Did not find user in user table. This user is not registered. Db query gives !resultSet.next()");
				          }
				          
				          preparedStatement.close();

				        }
				        catch(SQLException sqlException){
				        	System.out.println("<p>" + sqlException + "</p>");
					    	sqlException.printStackTrace();
				        } 	
				        
					    //TODO: Get the rest of the merge data (column joins, etc.).
				        
				
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


	public DatatableModel getDatatable1Model() {
		// TODO Auto-generated method stub
		return null;
	}


	public UploadModel getUpload1Model() {
		// TODO Auto-generated method stub
		return null;
	}


	public DatatableModel getDatatable2Model() {
		// TODO Auto-generated method stub
		return null;
	}


	public UploadModel getUpload2Model() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}