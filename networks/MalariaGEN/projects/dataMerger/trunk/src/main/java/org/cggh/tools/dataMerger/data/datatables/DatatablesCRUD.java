package org.cggh.tools.dataMerger.data.datatables;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.databases.DatabaseModel;
import org.cggh.tools.dataMerger.data.uploads.UploadModel;
import org.cggh.tools.dataMerger.data.uploads.UploadsCRUD;
import org.cggh.tools.dataMerger.data.users.UserModel;


public class DatatablesCRUD implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3509510173706652464L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.datatables");

	private DatabaseModel databaseModel;

	
	private String stringsToNullifyAsCSV;
	
	public DatatablesCRUD() {
		this.setDatabaseModel(new DatabaseModel());
	}


	public void setDatabaseModel (final DatabaseModel databaseModel) {
        this.databaseModel  = databaseModel;
    }
    public DatabaseModel getDatabaseModel () {
        return this.databaseModel;
    } 
	

  


	public DatatableModel retrieveDatatableAsDatatableModelThroughCreatingDatatableUsingUploadId(Integer uploadId,
			Connection connection) {

		UploadsCRUD uploadsCRUD = new UploadsCRUD();
		UploadModel uploadModel = uploadsCRUD.retrieveUploadAsUploadModelByUploadId(uploadId, connection);
		
		DatatableModel datatableModel = new DatatableModel();
		datatableModel.setName("datatable_" + uploadModel.getId());
		
		//NOTE: Not necessary
		//uploadModel.setDatatableModel(datatableModel);
		//uploadsCRUD.updateUploadDatatableNameUsingUploadModel(uploadModel, connection);

	         //Get the column names
         		try {

         			FileInputStream fileInputStream = new FileInputStream(uploadModel.getRepositoryFilepath());
	        	    DataInputStream dataInputStream = new DataInputStream(fileInputStream);
	        	    
	        	    //TODO: Character-set detection
	        	    //http://jchardet.sourceforge.net/
	        	    
	        	    //NOTE: This translates the expected encoding (ISO-8859-1) into Unicode.
	        	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream,"ISO-8859-1"));
	        	    String strLine;

	        	    if ((strLine = bufferedReader.readLine()) != null)   {
	
	        	      String[] columnNames = strLine.split(",");
	        	      List<String> columnNamesAsStringList = new ArrayList<String>();
	        	      
	        	      if (columnNames.length > 0) {
		        	      
		        	      String columnDefinitionsForCreateTableSQL = "";
		        	      
		        	      for (int i = 0; i < columnNames.length; i++) {
		        	    	  
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
			    		          PreparedStatement preparedStatement2 = connection.prepareStatement("LOAD DATA INFILE ? INTO TABLE `" + datatableModel.getName() + "` FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' LINES TERMINATED BY '\n' IGNORE 1 LINES ;");
			    		          preparedStatement2.setString(1, uploadModel.getRepositoryFilepath());
			    		          preparedStatement2.executeUpdate();
			    		          preparedStatement2.close();
			    		          
			    		          
			    		          //TODO: Get a list of all the strings to nullify.
			    		          
			    		          
			    		          if (this.getDatabaseModel().getServletContext().getInitParameter("stringsToNullifyAsCSV") != null) {
			    		        	  
			    		        	  String[] stringsToNullifyAsStringArray = this.getDatabaseModel().getServletContext().getInitParameter("stringsToNullifyAsCSV").split(",");
				    		        	 
			    		        	  for (int i = 0; i < columnNames.length; i++) {
			    		        		
			    		        		  String columnName = columnNames[i];
			    		        		  
				    		        	  String conditionForUpdateSQL = "";
				    		        	  
					    		          for (int j = 0; j < stringsToNullifyAsStringArray.length; j++) {
					    		        	  
					    		        	  String stringToNullify = stringsToNullifyAsStringArray[j];
					    		        	  
					    		        	  if (j > 0) {
					    		        		  
					    		        		  conditionForUpdateSQL += " OR ";
					    		        	  }
					    		        	  
					    		        	  conditionForUpdateSQL += "`" + columnName + "` = '" + stringToNullify + "'";
					    		          }
					    		          
					    		          PreparedStatement preparedStatement3 = connection.prepareStatement("UPDATE `" + datatableModel.getName() + "` SET `" + columnName + "` = NULL WHERE " + conditionForUpdateSQL + ";");
					    		          preparedStatement3.executeUpdate();
					    		          preparedStatement3.close();
					    		          
			    		        	  }
			    		        	  
			    		          } else {
			    		        	  //When no NULLs are specified, the default behavior is to interpret these values as NULL: NULL, \N and "\N"
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
	        	    	  
	        	    	  //TODO: appropriate error
	        	    	  //columnNames.length <= 0
	        	      }
	        	      
	        	    } else {
	        	    	
	        	    	logger.severe("bufferedReader.readLine() is null");
	        	    }

	        	    dataInputStream.close();
        	    
        	    } catch (Exception e){
        	    	e.printStackTrace();
        	    }


	       return datatableModel;
		
		
	}




	public DatatableModel retrieveDatatableAsDatatableModelUsingDatatableName(String datatableName,
			Connection connection) {

		DatatableModel datatableModel = new DatatableModel();
		

		datatableModel.setName(datatableName);


	      try {
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM upload WHERE datatable_name = ?;");
	          preparedStatement.setString(1, datatableModel.getName());				          
	          preparedStatement.executeQuery();

	          ResultSet resultSet = preparedStatement.getResultSet();

	          // There may be no such datatable.
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();
	        	  
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
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, datatable_name FROM `upload` WHERE id = ?;");
	          preparedStatement.setInt(1, uploadModel.getId());
	          preparedStatement.executeQuery();
	          ResultSet resultSet = preparedStatement.getResultSet();
	          
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();

	        	  //Set the datatable properties
	        	  datatableModel.setName(resultSet.getString("datatable_name"));
	        	  
	        	  if (datatableModel.getName() != null) {
		        		  
		        	  //Retrieve the datatable data
		        	  datatableModel.setDataAsCachedRowSet(this.retrieveDataAsCachedRowSetByDatatableName(datatableModel.getName(), connection));
		        	  
		        	  //Retrieve columnNamesAsStringList
		        	  List<String> columnNamesAsStringList = new ArrayList<String>();
		        	  for (int i = 1; i <= datatableModel.getDataAsCachedRowSet().getMetaData().getColumnCount(); i++) {
		        		  columnNamesAsStringList.add(datatableModel.getDataAsCachedRowSet().getMetaData().getColumnName(i));
		        	  }
		        	  datatableModel.setColumnNamesAsStringList(columnNamesAsStringList);
		        	  
		        	  
	        	  } else {
	        		  
	        		  //Not necessarily an error. Might just be checking for existence.
	        	  }
	        	  
	        	  
	      	  } else {
	      		  
	      		  //TODO: logging
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


	public void setStringsToNullifyAsCSV(String stringsToNullifyAsCSV) {
		this.stringsToNullifyAsCSV = stringsToNullifyAsCSV;
	}


	public String getStringsToNullifyAsCSV() {
		return stringsToNullifyAsCSV;
	}

}