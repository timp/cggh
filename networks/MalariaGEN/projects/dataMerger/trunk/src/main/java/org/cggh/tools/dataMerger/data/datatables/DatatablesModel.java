package org.cggh.tools.dataMerger.data.datatables;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
			
			Connection connection = this.getDataModel().getNewConnection();
			
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

		// Creates should not change their own models (but can change the models they are given).
		// So don't do this: this.setUploadModel(uploadModel);
		
		// Determine a name for the new table.
		// Get the max(id) for the datatable table.
		this.getMaxIdByConnection(connection);
		
		Integer nextUniqueInteger = this.getMaxId() + 1;
		
		
		// Get the datatable by name.
		DatatableModel datatableModel = new DatatableModel();
		
		datatableModel.getDatatableModelByName("datatable_" + nextUniqueInteger, connection);
		
		while (datatableModel.getId() != null) {
			
			nextUniqueInteger++;
			
			//TODO: Prevent runaway properly.
			if (nextUniqueInteger == 100) {
				break;
			}
			
			datatableModel.getDatatableModelByName("datatable_" + nextUniqueInteger, connection);
		}
		
		datatableModel.setName("datatable_" + nextUniqueInteger);
		datatableModel.setUploadModel(uploadModel);
		
		
		// Create the datatable record.
	     try {
	         PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO datatable (name, upload_id, created_datetime) VALUES (?, ?, NOW());");
	         preparedStatement.setString(1, datatableModel.getName());
	         preparedStatement.setInt(2, datatableModel.getUploadModel().getId());	          
	         preparedStatement.executeUpdate();
	         preparedStatement.close();
	         
	         //Get the column names
         		try {

         			FileInputStream fileInputStream = new FileInputStream(datatableModel.getUploadModel().getRepositoryFilepath());

	        	    DataInputStream dataInputStream = new DataInputStream(fileInputStream);
	        	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));
	        	    String strLine;

	        	    if ((strLine = bufferedReader.readLine()) != null)   {

	        	      System.out.println (strLine);
	        	      
	        	      String[] columnNames = strLine.split(",");
	        	      
	        	      String strColumnList = "";
	        	      
	        	      for (int i = 0; i < columnNames.length; i++) {
	        	    	  
	        	    	  strColumnList = strColumnList.concat("`" + columnNames[i] + "` VARCHAR(255) NULL");
	        	    	  
	        	    	  if (i != columnNames.length - 1) {
	        	    		  strColumnList = strColumnList.concat(", ");
	        	    	  }
	        	    	  
	        	      }
	        	      //TODO:
	        	      System.out.println("strColumnList = " + strColumnList);
	        	      
	        	    // Create the table
	    		      try {
				          Statement statement = connection.createStatement();
				          statement.executeUpdate("CREATE TABLE `" + datatableModel.getName() + "` (" + 
				        		  strColumnList + 
				        		  ") ENGINE=InnoDB;");
				          statement.close();

	 	     	         // Load the data in
		    		      try {
		    		    	  //TODO: OPTIONALLY ENCLOSED BY '"' ESCAPED BY '\'
		    		    	  //ENCLOSED BY '\"' ESCAPED BY '\\\\'
		    		          PreparedStatement preparedStatement2 = connection.prepareStatement("LOAD DATA INFILE ? IGNORE INTO TABLE `" + datatableModel.getName() + "` FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' IGNORE 1 LINES ;");
		    		          preparedStatement2.setString(1, datatableModel.getUploadModel().getRepositoryFilepath());
		    		          preparedStatement2.executeUpdate();
		    		          preparedStatement2.close();
		    		          
		    		          
		    		          // Update the datatable_created flag to save a query or two.
			    		      try {

			    		          PreparedStatement preparedStatement3 = connection.prepareStatement("UPDATE upload SET datatable_created = 1 WHERE id = ?;");
			    		          preparedStatement3.setInt(1, datatableModel.getUploadModel().getId());
			    		          preparedStatement3.executeUpdate();
			    		          preparedStatement3.close();
			    		          
			    		
			    		        }
			    		        catch(SQLException sqlException){
			    			    	sqlException.printStackTrace();
			    		        } 
		    		          
		    		
		    		        }
		    		        catch(SQLException sqlException){
		    			    	sqlException.printStackTrace();
		    		        } 
	    		          
	    		          
	    		        }
	    		        catch(SQLException sqlException){
	    			    	sqlException.printStackTrace();
	    		        } 
	    	         
	        	      
	        	      
	        	    } else {
	        	    	
	        	    	//TODO:
	        	    	System.out.println("Error getting column headings.");
	        	    }

	        	    dataInputStream.close();
        	    
        	    } catch (Exception e){
        	      System.err.println("Error: " + e.getMessage());
        	    }
	         
	         
	         
	         
	         

	
	       }
	       catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	       } 	
	       
			

	
		
		
	}



	private Integer getMaxId() {
		return this.maxId;
	}


	private void getMaxIdByConnection(Connection connection) {

	      try {
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(id) AS maxId FROM datatable;");  
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