package org.cggh.tools.dataMerger.data.exportedDatatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.exportedDatatables.ExportedDatatableModel;
import org.cggh.tools.dataMerger.data.exports.ExportModel;

public class ExportedDatatablesModel {

	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.exportedDatatables");
	
	
	public ExportedDatatableModel retrieveExportedDatatableAsExportedDatatableModelUsingExportId(
			Integer exportId, Connection connection) {
		
		ExportModel exportModel = new ExportModel();
		exportModel.setId(exportId);
		
		ExportedDatatableModel exportedDatatableModel = new ExportedDatatableModel();
		
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT exported_datatable_name FROM `export` WHERE id = ?;");
	          preparedStatement.setInt(1, exportModel.getId());
	          preparedStatement.executeQuery();
	          ResultSet resultSet = preparedStatement.getResultSet();
	          
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();

	        	  //Set the exportedKeytable properties
	        	  exportedDatatableModel.setName(resultSet.getString("exported_datatable_name"));
	        	  
	        	  if (exportedDatatableModel.getName() != null) {
	        		  //Retrieve the datatable data
	        		  exportedDatatableModel.setDataAsCachedRowSet(this.retrieveDataAsCachedRowSetByExportedDatatableName(exportedDatatableModel.getName(), connection));
	        	  }
	        	  
	      	  } else {
	      		  
	      		  //This is not necessarily an error, since might just be checking for existence.
	      		  this.logger.info("Did not retrieve exported datatable with the specified merge Id.");
	      		  
	      		  
	      	  }
	          
	          resultSet.close();
	          
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
		
		return exportedDatatableModel;
	}

	
	public CachedRowSet retrieveDataAsCachedRowSetByExportedDatatableName(String exportedDatatableName,
			Connection connection) {
		
		ExportedDatatableModel exportedDatatableModel = new ExportedDatatableModel();
		exportedDatatableModel.setName(exportedDatatableName);
		
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
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `" + exportedDatatableModel.getName() + "`;");
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
