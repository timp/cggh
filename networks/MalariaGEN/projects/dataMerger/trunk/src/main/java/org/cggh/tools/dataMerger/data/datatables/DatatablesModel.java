package org.cggh.tools.dataMerger.data.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.cggh.tools.dataMerger.data.DataModel;
import org.cggh.tools.dataMerger.data.uploads.UploadModel;
import org.cggh.tools.dataMerger.data.users.UserModel;


public class DatatablesModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3509510173706652464L;

	private DataModel dataModel;
	private UserModel userModel;

	private DatatableModel datatableModel;
	
	private UploadModel uploadModel;

	private Integer maxId;
	
	
	public DatatablesModel() {

		this.setDataModel(new DataModel());
		this.setUserModel(new UserModel());		
		
		this.setDatatableModel(new DatatableModel());
		this.setUploadModel(new UploadModel());
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
    
    public void setDatatableModel (final DatatableModel datatableModel) {
        this.datatableModel  = datatableModel;
    }
    public DatatableModel getDatatableModel () {
        return this.datatableModel;
    } 
    
    public void setUploadModel (final UploadModel uploadModel) {
        this.uploadModel  = uploadModel;
    }
    public UploadModel getUploadModel () {
        return this.uploadModel;
    }     
    
	
    //TODO: Just an example connection wrapper.
    // With a new connection.
	public void createDatatableByDatatableModel(DatatableModel datatableModel) {
		
		// Required in both with and without versions of this method, since either may be called independently.
		this.setDatatableModel(datatableModel);

		try {
			
			this.getDataModel().createConnection();
			Connection connection = this.getDataModel().getConnection();
			
			if (!connection.isClosed()) {
					
				
				//createDatatableByDatatableModel(datatableModel, connection);
		
			} else {
				
				System.out.println("connection.isClosed");
			}
				
		} 
		catch (Exception e) {
			System.out.println("Exception from createDatatableByDatatableModel.");
			e.printStackTrace();
		}
    	
	}

	// With a supplied connection.
	public void createDatatableByUploadModel(UploadModel uploadModel,
			Connection connection) {

		this.setUploadModel(uploadModel);
		
		//TODO: Determine a name for the new table.
		// Get the max(id) for the datatable table.
		this.getMaxIdByConnection(connection);
		
		Integer nextUniqueInteger = this.getMaxId() + 1;
		
		
		// Get the datatable by name.
		this.getDatatableModel().getDatatableModelByName("datatable_" + nextUniqueInteger, connection);
		
		while (this.getDatatableModel().getId() == null) {
			
			nextUniqueInteger++;
			
			//TODO: Prevent runaway properly.
			if (nextUniqueInteger == 1000) {
				break;
			}
			
			this.getDatatableModel().getDatatableModelByName("datatable_" + nextUniqueInteger, connection);
		}
		
		this.getDatatableModel().setName("datatable_" + nextUniqueInteger);
		this.getDatatableModel().setUploadModel(uploadModel);
		
		
		//TODO: Create the datatable record.
	     try {
	         PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO datatable (name, upload_id, created_datetime) VALUES (?, ?, NOW());");
	         preparedStatement.setString(1, this.datatableModel.getName());
	         preparedStatement.setInt(2, this.datatableModel.getUploadModel().getId());	          
	         preparedStatement.executeUpdate();
	         preparedStatement.close();
	         
	         
		      try {
		          PreparedStatement preparedStatement1 = connection.prepareStatement("LOAD DATA INFILE ? INTO TABLE `" + this.getDatatableModel().getName() + "`;");
		          preparedStatement1.setString(1, this.getDatatableModel().getUploadModel().getRepositoryFilepath());
		          preparedStatement1.executeUpdate();
		          preparedStatement1.close();
		
		        }
		        catch(SQLException sqlException){
			    	sqlException.printStackTrace();
		        } 
	         
	
	       }
	       catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	       } 	
	       
			//TODO: Load the datatable
	       
	       

	
		
		
	}



	private Integer getMaxId() {
		return this.maxId;
	}


	private void getMaxIdByConnection(Connection connection) {

	      try {
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(id) AS maxId FROM datatables;");  
	          preparedStatement.executeQuery();

	          ResultSet resultSet = preparedStatement.getResultSet();

	          
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();
	        	  
	        	  Integer maxId = resultSet.getInt("maxId");
	        	  
	        	  if (maxId != null) {
	        		  
	        		  this.setMaxId(maxId);
	        		  
	        	  } else {
	        		  // There may be no datatables.
	        		  this.setMaxId(1);
	        	  }
	        	  
	        	  
	          } else {
	        	  //TODO: proper logging and error handling
	        	  System.out.println("Db query gives !resultSet.next()");
	          }

	          resultSet.close();
	          preparedStatement.close();
	          

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
		
	}


	public void setMaxId(final int maxId) {
		this.maxId = maxId;
	}	
	
}