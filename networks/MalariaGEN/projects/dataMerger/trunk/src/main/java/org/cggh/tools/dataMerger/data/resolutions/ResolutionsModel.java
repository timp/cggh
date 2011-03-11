package org.cggh.tools.dataMerger.data.resolutions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.cggh.tools.dataMerger.data.DataModel;
import org.cggh.tools.dataMerger.data.merges.MergeModel;

public class ResolutionsModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3371872420630068087L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.resolutions");
	
	private DataModel dataModel;

	public ResolutionsModel() {

		//this.setDataModel(new DataModel());
	}	
	
	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
	}
	public DataModel getDataModel() {
		return this.dataModel;
	}

	public HashMap<Integer, Integer> retrieveUnresolvedConflictsCountByColumnNumberAsHashMapUsingMergeModel(
			MergeModel mergeModel) {

		HashMap<Integer, Integer> unresolvedConflictsCountByColumnNumberAsHashMap = new HashMap<Integer, Integer>();

		try {
			
			Connection connection = this.getDataModel().getNewConnection();
			 
			if (!connection.isClosed()) {		
		
			      try{
			          PreparedStatement preparedStatement = connection.prepareStatement("SELECT column_number, COUNT(*) AS conflicts_count FROM `resolution` WHERE merge_id = ? AND solution_by_column_id IS NULL AND solution_by_row_id IS NULL AND solution_by_cell_id IS NULL GROUP BY column_number ORDER BY column_number;");
			          preparedStatement.setInt(1, mergeModel.getId());
			          preparedStatement.executeQuery();
			          ResultSet resultSet = preparedStatement.getResultSet();
			          
			          if (resultSet.next()) {
			        	  
			        	  resultSet.beforeFirst();
			        	  
			        	  while(resultSet.next()){
		
			        		  unresolvedConflictsCountByColumnNumberAsHashMap.put(resultSet.getInt("column_number"), resultSet.getInt("conflicts_count"));
			        	  
			        	  }
		
			      	  } else {
			      		  
			      		  //There may be no unresolved conflicts
			      		  this.logger.info("Did not retrieve any unresolved conflicts for the specified merge id: " + mergeModel.getId());
			      		  
			      	  }
			          
			          resultSet.close();
			          
			          preparedStatement.close();
		
			        }
			        catch(SQLException sqlException){
				    	sqlException.printStackTrace();
			        } 		
	        
			} else {
				
				this.logger.severe("connection.isClosed");
			}
				
		} 
		catch (Exception e) {
			
			this.logger.severe(e.getMessage());
			e.printStackTrace();
		}
		
		return unresolvedConflictsCountByColumnNumberAsHashMap;		
		
	}

	public HashMap<String, Boolean> retrieveUnresolvedStatusByCellCoordsAsHashMapUsingMergeModel(
			MergeModel mergeModel) {
		
		HashMap<String, Boolean> unresolvedConflictsCountByCellCoordsAsHashMap = new HashMap<String, Boolean>();

		try {
			
			Connection connection = this.getDataModel().getNewConnection();
			 
			if (!connection.isClosed()) {		
		
			      try{
			          PreparedStatement preparedStatement = connection.prepareStatement("SELECT joined_keytable_id, column_number FROM `resolution` WHERE merge_id = ? AND solution_by_column_id IS NULL AND solution_by_row_id IS NULL AND solution_by_cell_id IS NULL GROUP BY column_number ORDER BY joined_keytable_id, column_number;");
			          preparedStatement.setInt(1, mergeModel.getId());
			          preparedStatement.executeQuery();
			          ResultSet resultSet = preparedStatement.getResultSet();
			          
			          if (resultSet.next()) {
			        	  
			        	  resultSet.beforeFirst();
			        	  
			        	  while(resultSet.next()){
		
			        		 
			        		  Integer joinedKeytableId = resultSet.getInt("joined_keytable_id");
			        		  Integer columnNumber = resultSet.getInt("column_number");
			        		  String cellCoords = joinedKeytableId.toString() + "," + columnNumber.toString();
			        		  
			        		  unresolvedConflictsCountByCellCoordsAsHashMap.put(cellCoords, true);
			        	  
			        	  }
		
			      	  } else {
			      		  
			      		  //There may be no unresolved conflicts
			      		  this.logger.info("Did not retrieve any unresolved conflicts the specified merge id: " + mergeModel.getId());
			      		  
			      	  }
			          
			          resultSet.close();
			          
			          preparedStatement.close();
		
			        }
			        catch(SQLException sqlException){
				    	sqlException.printStackTrace();
			        } 		
	        
			} else {
				
				this.logger.severe("connection.isClosed");
			}
				
		} 
		catch (Exception e) {
			
			this.logger.severe(e.getMessage());
			e.printStackTrace();
		}
		
		return unresolvedConflictsCountByCellCoordsAsHashMap;		
	}

}
