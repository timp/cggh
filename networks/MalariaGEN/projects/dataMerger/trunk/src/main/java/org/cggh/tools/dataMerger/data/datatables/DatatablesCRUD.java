package org.cggh.tools.dataMerger.data.datatables;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

import org.apache.commons.lang.StringEscapeUtils;
import org.cggh.tools.dataMerger.code.settings.SettingsCRUD;
import org.cggh.tools.dataMerger.code.settings.SettingsModel;
import org.cggh.tools.dataMerger.data.databases.DatabaseModel;
import org.cggh.tools.dataMerger.data.files.FileModel;
import org.cggh.tools.dataMerger.data.files.FilesCRUD;


public class DatatablesCRUD implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3509510173706652464L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.datatables");

	private DatabaseModel databaseModel;

	
	private String stringsToNullifyAsCSV;
	
	public DatatablesCRUD() {
		
		//Prefer not to construct
		//this.setDatabaseModel(new DatabaseModel());
	}


	public void setDatabaseModel (final DatabaseModel databaseModel) {
        this.databaseModel  = databaseModel;
    }
    public DatabaseModel getDatabaseModel () {
        return this.databaseModel;
    } 
	

  


	public DatatableModel retrieveDatatableAsDatatableModelThroughCreatingDatatableUsingFileId(Integer fileId,
			Connection connection) {

		FilesCRUD filesCRUD = new FilesCRUD();
		FileModel fileModel = filesCRUD.retrieveFileAsFileModelByFileId(fileId, connection);
		
		DatatableModel datatableModel = new DatatableModel();
		datatableModel.setName("datatable_" + fileModel.getId());
		
		fileModel.setDatatableModel(datatableModel);
		filesCRUD.updateFileDatatableNameUsingFileModel(fileModel, connection);

	         //Get the column names
         		try {

         			FileInputStream fileInputStream = new FileInputStream(fileModel.getFilepath());
	        	    DataInputStream dataInputStream = new DataInputStream(fileInputStream);
	        	    
	        	    //TODO: Character-set detection
	        	    //http://jchardet.sourceforge.net/
	        	    
	        	    //NOTE: This translates the expected encoding (ISO-8859-1, Latin1) into Unicode.
	        	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream,"ISO-8859-1"));
	        	    String strLine;

	        	    if ((strLine = bufferedReader.readLine()) != null)   {
	
	        	      String[] columnNames = strLine.split(",");
	        	      List<String> columnNamesAsStringList = new ArrayList<String>();
	        	      
	        	      if (columnNames.length > 0) {
	        	    	  
	        	    	  fileModel.setColumnsCount(columnNames.length);
	        	    	  filesCRUD.updateFileColumnsCountUsingFileModel(fileModel, connection);
		        	      
		        	      StringBuffer columnDefinitionsForCreateTableSQL = new StringBuffer();
		        	      
		        	      for (int i = 0; i < columnNames.length; i++) {
		        	    	  
		        	    	  //Convert column name to Unicode
		        	    	  //columnNames[i] = new String(columnNames[i].getBytes(Charset.forName("UTF-8")), Charset.forName("UTF-8"));
		        	    	  
		        	    	  //Remove leading and trailing quotes, escape the rest.
		        	    	  if (columnNames[i].startsWith("\"")) {
		        	    		  columnNames[i] = columnNames[i].substring(1, columnNames[i].length());
		        	          }
		        	          if (columnNames[i].endsWith("\"") && columnNames[i].length() > 1) {
		        	        	  columnNames[i] = columnNames[i].substring(0, columnNames[i].length() - 1);
		        	          }
		        	          
		        	          if (columnNames[i].length() > 0) {
		        	          
		        	        	  //FIXME: sanitation
		        	        	  
		        	        	  //TODO: This will be more portable, but will need to use instead of ` everywhere. 
		        	        	  //DatabaseMetaData databaseMetaData = connection.getMetaData();
			        	          //String identifierQuoteString = databaseMetaData.getIdentifierQuoteString();
		        	        	  
			        	    	  columnNames[i] = columnNames[i].replaceAll("`", "``");
			        	    	  
			        	    	  columnNames[i] = StringEscapeUtils.escapeSql(columnNames[i]);
			        	    	
			        	    	  //Remove characters outside Basic Multilingual Plane (MySQL column name character restriction).
			        	    	  columnNames[i] = columnNames[i].replaceAll("[^\u0000-\uFFFF]", "");
			        	    	  
			        	    	  //TODO: The above isn't good enough, so being harsher, but this is not ideal.
			        	    	  //Replace all non-word characters (same as [\W])
			        	    	  columnNames[i] = columnNames[i].replaceAll("[^\\w]", "");
			        	    	  
			        	    	  //TODO: Escape for CSV when exporting to CSV
			        	    	  //columnNames[i] = StringEscapeUtils.escapeCsv(columnNames[i]);
			        	    	  
		        	          } else {
		        	        	  columnNames[i] = Integer.toString(i);
		        	          }
		        	    	  
		        	          //Note: It don't like blank column names.
		        	          //Note: Don't compare strings with == or !=, use the equals() method.
		        	          if (columnNames[i] != null && columnNames[i].length() > 0) {
		        	        	  
		        	        	  columnNamesAsStringList.add(columnNames[i]);
			        	    	  columnDefinitionsForCreateTableSQL.append("`" + columnNames[i] + "` VARCHAR(36) NULL");
			        	    	  
			        	    	  if (i != columnNames.length - 1) {
			        	    		  columnDefinitionsForCreateTableSQL.append(", ");
			        	    	  }
		        	          } else {
		        	        	  
		        	        	  //FIXME: How to remove [i]?
		        	          }
		        	          
		        	          //NOTE: Stop using the columnNames array. Use columnNamesAsStringList instead.
		        	          
		        	    	  
		        	    	  
		        	    	  
		        	      }
	
		        	      datatableModel.setColumnNamesAsStringList(columnNamesAsStringList);
		        	      
		        	      if (columnDefinitionsForCreateTableSQL.length() > 0) {
			        	      
			        	      // Create the table
			    		      try {
						          Statement statement = connection.createStatement();
						          String statementSQL = "CREATE TABLE `" + datatableModel.getName() + "` (" + 
				        		  						columnDefinitionsForCreateTableSQL + 
				        		  						") ENGINE=InnoDB;";
						          
						          //
						          //logger.info(statementSQL);
						          
						          statement.executeUpdate(statementSQL);
						          statement.close();
		
						          // Disable the STRICT_TRANS_TABLES to enable automatic truncation

						          Statement statement1 = connection.createStatement();
						          String statementSQL1 = "SET sql_mode = 'NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';";
						          statement1.executeUpdate(statementSQL1);
						          statement1.close();
						          
						          
			 	     	         // Load the data in
				    		      try {
				    		    	  //TODO: OPTIONALLY ENCLOSED BY '"' ESCAPED BY '\'
				    		    	  //ENCLOSED BY '\"' ESCAPED BY '\\\\'
				    		          PreparedStatement preparedStatement2 = connection.prepareStatement("LOAD DATA INFILE ? INTO TABLE `" + datatableModel.getName() + "` FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' LINES TERMINATED BY '\n' IGNORE 1 LINES ;");
				    		          preparedStatement2.setString(1, fileModel.getFilepath());
				    		          preparedStatement2.executeUpdate();
				    		          preparedStatement2.close();
				    		          
				    		          
				    		          Statement statement11 = connection.createStatement();
							          String statementSQL11 = "SET sql_mode = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';";
							          statement11.executeUpdate(statementSQL11);
							          statement11.close();
				    		          
							          filesCRUD.updateFileRowsCountUsingFileModel(fileModel, connection);
							          
				    		          
				    		          //TODO: Get a list of all the strings to nullify.
							          SettingsCRUD settingsCRUD = new SettingsCRUD();
				    		          SettingsModel settingsModel = settingsCRUD.retrieveSettingsAsSettingsModel(connection);
				    		          
				    		          if (settingsModel.getSettingsAsHashMap().get("stringsToNullifyAsCSV") != null) {
				    		        	  
				    		        	  String[] stringsToNullifyAsStringArray = settingsModel.getSettingsAsHashMap().get("stringsToNullifyAsCSV").split(",");
					    		        	 
				    		        	  for (int i = 0; i < columnNamesAsStringList.size(); i++) {
				    		        		
				    		        		  String columnName = columnNamesAsStringList.get(i);
				    		        		  
					    		        	  StringBuffer conditionForUpdateSQL = new StringBuffer();
					    		        	  
						    		          for (int j = 0; j < stringsToNullifyAsStringArray.length; j++) {
						    		        	  
						    		        	  String stringToNullify = stringsToNullifyAsStringArray[j];
						    		        	  
						    		        	  if (j > 0) {
						    		        		  
						    		        		  conditionForUpdateSQL.append(" OR ");
						    		        	  }
						    		        	  
						    		        	  conditionForUpdateSQL.append("`").append(columnName).append("` = '").append(stringToNullify).append("'");
						    		          }
						    		          
						    		          String statementSQL3 = "UPDATE `" + datatableModel.getName() + "` SET `" + columnName + "` = NULL WHERE " + conditionForUpdateSQL.toString() + ";";
						    		          PreparedStatement preparedStatement3 = connection.prepareStatement(statementSQL3);
						    		          
						    		          //
						    		          //logger.info(statementSQL3);
						    		          
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
		        	    	  logger.severe("columnDefinitionsForCreateTableSQL == \"\"");
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






	public DatatableModel retrieveDatatableAsDatatableModelUsingFileId(
			Integer uploadId, Connection connection) {

		FileModel fileModel = new FileModel();
		fileModel.setId(uploadId);
		
		DatatableModel datatableModel = new DatatableModel();

	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, datatable_name FROM `file` WHERE id = ?;");
	          preparedStatement.setInt(1, fileModel.getId());
	          preparedStatement.executeQuery();
	          ResultSet resultSet = preparedStatement.getResultSet();
	          
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();

	        	  //Set the datatable properties
	        	  datatableModel.setName(resultSet.getString("datatable_name"));
	        	  
	        	  if (datatableModel.getName() != null) {
		        		  
		        	  //Retrieve the datatable data
		        	  datatableModel.setColumnsAsCachedRowSet(this.retrieveColumnsAsCachedRowSetUsingDatatableName(datatableModel.getName(), connection));
		        	  
		        	  //Retrieve columnNamesAsStringList
		        	  List<String> columnNamesAsStringList = new ArrayList<String>();
		        	  
		        	  datatableModel.getColumnsAsCachedRowSet().beforeFirst();
		        	  
		        	  while (datatableModel.getColumnsAsCachedRowSet().next()) {
		        		  columnNamesAsStringList.add(datatableModel.getColumnsAsCachedRowSet().getString("COLUMN_NAME"));
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


	public CachedRowSet retrieveColumnsAsCachedRowSetUsingDatatableName(String datatableName,
			Connection connection) {

		DatatableModel datatableModel = new DatatableModel();
		datatableModel.setName(datatableName);
		
        Class<?> cachedRowSetImplClass = null;
		try {
			cachedRowSetImplClass = Class.forName("com.sun.rowset.CachedRowSetImpl");
		} catch (ClassNotFoundException e1) {

			e1.printStackTrace();
		}
		
		CachedRowSet dataAsCachedRowSet = null;
		try {
			dataAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
		} catch (InstantiationException e1) {

			e1.printStackTrace();
		} catch (IllegalAccessException e1) {

			e1.printStackTrace();
		}
		
	      try{
	    	  
	    	  DatabaseMetaData databaseMetaData = connection.getMetaData();
	    	  dataAsCachedRowSet.populate(databaseMetaData.getColumns(null, null, datatableModel.getName(), null));


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