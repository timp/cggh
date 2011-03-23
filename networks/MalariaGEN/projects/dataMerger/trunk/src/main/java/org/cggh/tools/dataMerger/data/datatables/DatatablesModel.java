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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.DataModel;
import org.cggh.tools.dataMerger.data.uploads.UploadModel;
import org.cggh.tools.dataMerger.data.uploads.UploadsModel;
import org.cggh.tools.dataMerger.data.users.UserModel;


public class DatatablesModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3509510173706652464L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.datatables");

	private DataModel dataModel;
	private UserModel userModel;

	private DatatableModel datatableModel;
	
	private UploadModel uploadModel;

	
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


	public DatatableModel retrieveDatatableAsDatatableModelThroughCreatingDatatableUsingUploadId(Integer uploadId,
			Connection connection) {

		DatatableModel datatableModel = new DatatableModel();

		UploadsModel uploadsModel = new UploadsModel();
		datatableModel.setUploadModel(uploadsModel.retrieveUploadAsUploadModelByUploadId(uploadId, connection));
				
		// Determine a name for the new datatable.
		// Try to get the nextUniqueInteger for the datatable table.
		Integer nextUniqueInteger = this.retrieveMaxIdAsIntegerUsingConnection(connection) + 1;
		
		
		
		DatatableModel testDatatableModel = this.retrieveDatatableAsDatatableModelUsingName("datatable_" + nextUniqueInteger, connection);
		
		while (testDatatableModel.getId() != null) {
			
			nextUniqueInteger++;
			
			//TODO: Prevent runaway properly.
			if (nextUniqueInteger == 100) {
				break;
			}
			
			testDatatableModel = this.retrieveDatatableAsDatatableModelUsingName("datatable_" + nextUniqueInteger, connection);
		}
		

		datatableModel.setName("datatable_" + nextUniqueInteger);

		
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
	        	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream,"ISO-8859-1"));
	        	    String strLine;

	        	    if ((strLine = bufferedReader.readLine()) != null)   {

	        	    	//TODO: Rationalize
	        	    	
	        	      String[] columnNames = strLine.split(",");
	        	      List<String> columnNamesAsStringList = new ArrayList<String>();
	        	      
	        	      
	        	      String columnDefinitionsForCreateTableSQL = "";
	        	      
	        	      for (int i = 0; i < columnNames.length; i++) {
	        	    	  
	        	    	  //this.logger.info("Got column name: " + columnNames[i]);
	        	    	  
	        	    	  //Strip out quote marks from column names
	        	    	  columnNames[i] = columnNames[i].replace("\"", "");
	        	    	  
	        	    	  columnNamesAsStringList.add(columnNames[i]);
	        	    	  
	        	    	  columnDefinitionsForCreateTableSQL = columnDefinitionsForCreateTableSQL.concat("`" + columnNames[i] + "` VARCHAR(255) NULL");
	        	    	  
	        	    	  if (i != columnNames.length - 1) {
	        	    		  columnDefinitionsForCreateTableSQL = columnDefinitionsForCreateTableSQL.concat(", ");
	        	    	  }
	        	    	  
	        	      }

	        	      datatableModel.setColumnNamesAsStringList(columnNamesAsStringList);
	        	      
	        	      
	        	    // Create the table
	    		      try {
				          Statement statement = connection.createStatement();
				          statement.executeUpdate("CREATE TABLE `" + datatableModel.getName() + "` (" + 
				        		  columnDefinitionsForCreateTableSQL + 
				        		  ") ENGINE=InnoDB;");
				          statement.close();

	 	     	         // Load the data in
		    		      try {
		    		    	  //TODO: OPTIONALLY ENCLOSED BY '"' ESCAPED BY '\'
		    		    	  //ENCLOSED BY '\"' ESCAPED BY '\\\\'
		    		          PreparedStatement preparedStatement2 = connection.prepareStatement("LOAD DATA INFILE ? IGNORE INTO TABLE `" + datatableModel.getName() + "` FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' LINES TERMINATED BY '\n' IGNORE 1 LINES ;");
		    		          preparedStatement2.setString(1, datatableModel.getUploadModel().getRepositoryFilepath());
		    		          preparedStatement2.executeUpdate();
		    		          preparedStatement2.close();
		    		          
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
	        	    	//System.out.println("Error getting column headings.");
	        	    }

	        	    dataInputStream.close();
        	    
        	    } catch (Exception e){
        	      System.err.println("Error getting the column names: " + e.getMessage());
        	    }
	         
	         
	         
	         
	         

	
	       }
	       catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	       } 	
	       
			

	       return datatableModel;
		
		
	}

	public DatatableModel retrieveDatatableAsDatatableModelUsingName(String name, Connection connection) {

		DatatableModel datatableModel = new DatatableModel();
		
		datatableModel.setName(name);

		  //TODO: Data
		
	      try {
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM datatable WHERE name = ?;");
	          preparedStatement.setString(1, datatableModel.getName());				          
	          preparedStatement.executeQuery();

	          ResultSet resultSet = preparedStatement.getResultSet();

	          // There may be no such datatable.
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();

	        	  datatableModel.setId(resultSet.getInt("id"));
	        	  
	        	  datatableModel = this.retrieveDatatableAsDatatableModelUsingDatatableId(datatableModel.getId(), connection);

	          } else {
	        	  //TODO: proper logging and error handling
	        	  //System.out.println("Did not find datatable with this name. Db query gives !resultSet.next()");
	          }

	          resultSet.close();
	          preparedStatement.close();
	          

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
		return datatableModel;
	}


	public Integer retrieveMaxIdAsIntegerUsingConnection(Connection connection) {

		Integer maxId = null;
		
	      try {
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(id) AS maxId FROM datatable;");  
	          preparedStatement.executeQuery();

	          ResultSet resultSet = preparedStatement.getResultSet();

	          
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();
	        	  
	        	  maxId = resultSet.getInt("maxId");
	        	  
	        	  if (maxId == null) {
	        		  
	        		  // There may be no datatables.
	        		  maxId = 1;
	        	  }
	        	  
	        	  
	          } else {
	        	  //TODO: proper logging and error handling
	        	  //System.out.println("Db query gives !resultSet.next()");
	          }

	          resultSet.close();
	          preparedStatement.close();
	          

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
		return maxId;
	}



	public DatatableModel retrieveDatatableAsDatatableModelUsingDatatableId(Integer datatableId,
			Connection connection) {

		DatatableModel datatableModel = new DatatableModel();
		

		datatableModel.setId(datatableId);


	      try {
	          PreparedStatement preparedStatement = connection.prepareStatement(
	        		  "SELECT id, " + 
	        		  "name, " +
	        		  "upload_id, " +  
	        		  "created_datetime " + 
	        		  "FROM datatable WHERE id = ?;");
	          preparedStatement.setInt(1, datatableModel.getId());				          
	          preparedStatement.executeQuery();

	          ResultSet resultSet = preparedStatement.getResultSet();

	          // There may be no such datatable.
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();
	        	  
	        	  datatableModel.setId(resultSet.getInt("id"));
	        	  datatableModel.setName(resultSet.getString("name"));
	        	  datatableModel.getUploadModel().setId(resultSet.getInt("upload_id"));
	        	  datatableModel.setCreatedDatetime(resultSet.getTimestamp("created_datetime"));
	        	  
	        	  //TODO: Data
	        	  
			      try{
			          PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM `" + datatableModel.getName() + "`;");
			          preparedStatement2.executeQuery();
			          
			          String CACHED_ROW_SET_IMPL_CLASS = "com.sun.rowset.CachedRowSetImpl";
			          Class<?> cachedRowSetImplClass = Class.forName(CACHED_ROW_SET_IMPL_CLASS);
			          
			          //TODO: Make this more memory efficient.
			          
			          CachedRowSet dataAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
			          dataAsCachedRowSet.populate(preparedStatement2.getResultSet());
			          
			          datatableModel.setDataAsCachedRowSet(dataAsCachedRowSet);
			          
			          List<String> columnNamesAsStringList = new ArrayList<String>();
			          
			          for (int i = 0; i < datatableModel.getDataAsCachedRowSet().getMetaData().getColumnCount(); i++) {
			        	  
			        	  columnNamesAsStringList.add(datatableModel.getDataAsCachedRowSet().getMetaData().getColumnName(i + 1));
			        	  
			          }
			          
			          
			          datatableModel.setColumnNamesAsStringList(columnNamesAsStringList);
			          
			          preparedStatement2.close();

			        }
			        catch(SQLException sqlException){
				    	sqlException.printStackTrace();
			        } catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
	        	  
	        	  
	        	  
	        	  
	          } else {
	        	  //TODO: proper logging and error handling
	        	  //System.out.println("Did not find datatable with this id. Db query gives !resultSet.next()");
	          }

	          resultSet.close();
	          preparedStatement.close();
	          

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
		
		return datatableModel;
	}


	public DatatableModel retrieveDatatableAsDatatableModelUsingUploadId(
			Integer uploadId, Connection connection) {

		UploadModel uploadModel = new UploadModel();
		uploadModel.setId(uploadId);
		
		DatatableModel datatableModel = new DatatableModel();

	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, name, upload_id, created_datetime FROM `datatable` WHERE upload_id = ?;");
	          preparedStatement.setInt(1, uploadModel.getId());
	          preparedStatement.executeQuery();
	          ResultSet resultSet = preparedStatement.getResultSet();
	          
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();

	        	  //Set the datatable properties
	        	  datatableModel.setId(resultSet.getInt("id"));
	        	  datatableModel.setName(resultSet.getString("name"));
	        	  datatableModel.getUploadModel().setId(resultSet.getInt("upload_id"));
	        	  datatableModel.setCreatedDatetime(resultSet.getTimestamp("created_datetime"));
	        	  
	        	  //Retrieve the datatable data
	        	  datatableModel.setDataAsCachedRowSet(this.retrieveDataAsCachedRowSetByDatatableName(datatableModel.getName(), connection));
	        	  
	        	  //Retrieve columnNamesAsStringList
	        	  List<String> columnNamesAsStringList = new ArrayList<String>();
	        	  for (int i = 1; i <= datatableModel.getDataAsCachedRowSet().getMetaData().getColumnCount(); i++) {
	        		  columnNamesAsStringList.add(datatableModel.getDataAsCachedRowSet().getMetaData().getColumnName(i));
	        	  }
	        	  datatableModel.setColumnNamesAsStringList(columnNamesAsStringList);
	        	  
	        	  //Retrieve the upload data
	        	  UploadsModel uploadsModel = new UploadsModel();
	        	  datatableModel.setUploadModel(uploadsModel.retrieveUploadAsUploadModelByUploadId(datatableModel.getUploadModel().getId(), connection));
	        	  
	        	  
	        	  
	      	  } else {
	      		  
	      		  //This is not necessarily an error, since might just be checking for existence.
	      		  //this.logger.info("Did not retrieve datatable with the specified upload Id.");
	      		  
	      		  
	      	  }
	          
	          resultSet.close();
	          
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
		
		return datatableModel;
	}


	private CachedRowSet retrieveDataAsCachedRowSetByDatatableName(String datatableName,
			Connection connection) {

		DatatableModel datatableModel = new DatatableModel();
		datatableModel.setName(datatableName);
		
        Class<?> cachedRowSetImplClass = null;
		try {
			cachedRowSetImplClass = Class.forName("com.sun.rowset.CachedRowSetImpl");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		CachedRowSet dataAsCachedRowSet = null;
		try {
			dataAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `" + datatableModel.getName() + "`;");
	          preparedStatement.executeQuery();
	         
	          dataAsCachedRowSet.populate(preparedStatement.getResultSet());
	          
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
		return dataAsCachedRowSet;
	}


	
}