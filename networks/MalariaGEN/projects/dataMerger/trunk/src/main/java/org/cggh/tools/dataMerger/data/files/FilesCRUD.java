package org.cggh.tools.dataMerger.data.files;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.databases.DatabaseModel;
import org.cggh.tools.dataMerger.data.users.UserModel;
import org.cggh.tools.dataMerger.functions.data.files.FilesFunctions;



public class FilesCRUD implements java.io.Serializable  {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6872757755874307990L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.files");
	
	private DatabaseModel databaseModel;
	
	public String retrieveFilesAsDecoratedXHTMLTableUsingUserId (Integer userId) {
		
		String filesAsDecoratedXHTMLTable = "";
		
		
		  CachedRowSet filesAsCachedRowSet = this.retrieveFilesAsCachedRowSetUsingUserId(userId);

		  if (filesAsCachedRowSet != null) {

			  	FilesFunctions filesFunctions = new FilesFunctions();
			  	filesFunctions.setFilesAsCachedRowSet(filesAsCachedRowSet);
			  	filesFunctions.setFilesAsDecoratedXHTMLTableUsingFilesAsCachedRowSet();
			  	filesAsDecoratedXHTMLTable = filesFunctions.getFilesAsDecoratedXHTMLTable();
			    
		  } else {
			  
			  //TODO: Error handling
			  this.logger.severe("filesAsCachedRowSet is null");
			  filesAsDecoratedXHTMLTable = "<p>Error: filesAsCachedRowSet is null</p>";
			  
		  }
		
		return filesAsDecoratedXHTMLTable;
	}

	public CachedRowSet retrieveFilesAsCachedRowSetUsingUserId(Integer userId) {
		
		CachedRowSet filesAsCachedRowSet = null;
		
		UserModel userModel = new UserModel();
		userModel.setId(userId);
		
		   String CACHED_ROW_SET_IMPL_CLASS = "com.sun.rowset.CachedRowSetImpl";
		   
				

				Connection connection = this.getDatabaseModel().getNewConnection();
				 
				if (connection != null) {
					
					 //FIXME: Apparently a bug in CachedRowSet using getX('columnAlias') aka columnLabel, which actually only works with getX('columnName'), so using getX('columnIndex').
					 
					
				      try{
				    	  PreparedStatement preparedStatement = connection.prepareStatement(
				    			  
				    			  "SELECT id, original_filename AS filename, created_datetime, file_size_in_bytes, 'upload' AS sourceTable " +
				    			  "FROM upload " +
				    			  "WHERE created_by_user_id = ? AND repository_filepath IS NOT NULL " +
				    			  "UNION " +
				    			  "SELECT id, filename, created_datetime, merged_datatable_export_file_size_in_bytes, 'export' AS sourceTable " +
				    			  "FROM export " +
				    			  "WHERE created_by_user_id = ? AND merged_datatable_export_repository_filepath IS NOT NULL " +
				    			  ";"
				    	  
				    	  );
				          preparedStatement.setInt(1, userModel.getId());
				          preparedStatement.setInt(2, userModel.getId());
				          preparedStatement.executeQuery();
				          Class<?> cachedRowSetImplClass = Class.forName(CACHED_ROW_SET_IMPL_CLASS);
				          filesAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
				          filesAsCachedRowSet.populate(preparedStatement.getResultSet());
				          preparedStatement.close();
	
				        } 
				      	catch (SQLException sqlException){
				        	//System.out.println("<p>" + sqlException + "</p>");
					    	sqlException.printStackTrace();
				        } catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} finally {
				        	try {
								connection.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
				        }
				
					
					
				} else {
					
					//System.out.println("connection.isClosed");
				}
		
	
	
	
	     return filesAsCachedRowSet;
	     
	}

	public void setDatabaseModel(DatabaseModel databaseModel) {
		this.databaseModel = databaseModel;
	}

	public DatabaseModel getDatabaseModel() {
		return databaseModel;
	}
}
