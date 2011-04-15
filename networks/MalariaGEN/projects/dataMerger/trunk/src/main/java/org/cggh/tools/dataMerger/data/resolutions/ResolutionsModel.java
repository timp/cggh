package org.cggh.tools.dataMerger.data.resolutions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.DataModel;
import org.cggh.tools.dataMerger.data.merges.MergeModel;

public class ResolutionsModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3371872420630068087L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.resolutions");
	
	private DataModel dataModel;
	private String exportRepositoryFilepath ;
	private boolean exportSuccessful;

	public ResolutionsModel() {

		//this.setDataModel(new DataModel());
	}	
	
	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
	}
	public DataModel getDataModel() {
		return this.dataModel;
	}

	public HashMap<Integer, Integer> retrieveUnresolvedByColumnOrRowConflictsCountUsingColumnNumberAsHashMapUsingMergeModel(
			MergeModel mergeModel) {

		HashMap<Integer, Integer> unresolvedConflictsCountByColumnNumberAsHashMap = new HashMap<Integer, Integer>();

		try {
			
			Connection connection = this.getDataModel().getNewDatabaseConnection();
			 
			if (connection != null) {		
		
			      try{
			          PreparedStatement preparedStatement = connection.prepareStatement("SELECT column_number, COUNT(*) AS conflicts_count FROM `resolution` WHERE merge_id = ? AND solution_by_column_id IS NULL AND solution_by_row_id IS NULL GROUP BY column_number ORDER BY column_number;");
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
			      		  //this.logger.info("Did not retrieve any unresolved conflicts for the specified merge id: " + mergeModel.getId());
			      		  
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

	public HashMap<String, Boolean> retrieveUnresolvedByColumnOrRowStatusUsingCellCoordsAsHashMapUsingMergeModel(
			MergeModel mergeModel) {
		
		HashMap<String, Boolean> unresolvedByColumnOrRowStatusUsingCellCoordsAsHashMap = new HashMap<String, Boolean>();

		try {
			
			Connection connection = this.getDataModel().getNewDatabaseConnection();
			 
			if (connection != null) {		
		
			      try{
			          PreparedStatement preparedStatement = connection.prepareStatement("SELECT joined_keytable_id, column_number FROM `resolution` WHERE merge_id = ? AND solution_by_column_id IS NULL AND solution_by_row_id IS NULL ORDER BY joined_keytable_id, column_number;");
			          preparedStatement.setInt(1, mergeModel.getId());
			          preparedStatement.executeQuery();
			          ResultSet resultSet = preparedStatement.getResultSet();
			          
			          if (resultSet.next()) {
			        	  
			        	  resultSet.beforeFirst();
			        	  
			        	  while(resultSet.next()){
		
			        		 
			        		  Integer joinedKeytableId = resultSet.getInt("joined_keytable_id");
			        		  Integer columnNumber = resultSet.getInt("column_number");
			        		  String cellCoords = joinedKeytableId.toString() + "," + columnNumber.toString();
			        		  
			        		  unresolvedByColumnOrRowStatusUsingCellCoordsAsHashMap.put(cellCoords, true);
			        	  
			        	  }
		
			      	  } else {
			      		  
			      		  //There may be no unresolved conflicts
			      		  //this.logger.info("Did not retrieve any unresolved conflicts the specified merge id: " + mergeModel.getId());
			      		  
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
		
		return unresolvedByColumnOrRowStatusUsingCellCoordsAsHashMap;		
	}

	
	/////////////////TODO:
	// Use 
	// retrieveUnresolvedByColumnOrRowStatusUsingCellCoordsAsHashMapUsingMergeModel
	// above as starting point 
	
	public HashMap<String, Integer> retrieveSolutionByColumnIdUsingCellCoordsAsHashMapUsingMergeModel(
			MergeModel mergeModel) {
		
		HashMap<String, Integer> solutionByColumnIdUsingCellCoordsAsHashMap = new HashMap<String, Integer>();

		try {
			
			Connection connection = this.getDataModel().getNewDatabaseConnection();
			 
			if (connection != null) {		
		
			      try{
			          PreparedStatement preparedStatement = connection.prepareStatement("SELECT joined_keytable_id, column_number, solution_by_column_id FROM `resolution` WHERE merge_id = ? ORDER BY joined_keytable_id, column_number;");
			          preparedStatement.setInt(1, mergeModel.getId());
			          preparedStatement.executeQuery();
			          ResultSet resultSet = preparedStatement.getResultSet();
			          
			          if (resultSet.next()) {
			        	  
			        	  resultSet.beforeFirst();
			        	  
			        	  while(resultSet.next()){
		
			        		 
			        		  Integer joinedKeytableId = resultSet.getInt("joined_keytable_id");
			        		  Integer columnNumber = resultSet.getInt("column_number");
			        		  String cellCoords = joinedKeytableId.toString() + "," + columnNumber.toString();
			        		  
			        		  solutionByColumnIdUsingCellCoordsAsHashMap.put(cellCoords, resultSet.getInt("solution_by_column_id"));
			        	  
			        	  }
		
			      	  } else {
			      		  
			      		  //There may be no unresolved conflicts
			      		  //this.logger.info("Did not retrieve any solution_by_column_ids using the specified merge id: " + mergeModel.getId());
			      		  
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
		
		return solutionByColumnIdUsingCellCoordsAsHashMap;	
	}

	
	public HashMap<String, Integer> retrieveSolutionByRowIdUsingCellCoordsAsHashMapUsingMergeModel(
			MergeModel mergeModel) {
		
		HashMap<String, Integer> solutionByRowIdUsingCellCoordsAsHashMap = new HashMap<String, Integer>();

		try {
			
			Connection connection = this.getDataModel().getNewDatabaseConnection();
			 
			if (connection != null) {		
		
			      try{
			          PreparedStatement preparedStatement = connection.prepareStatement("SELECT joined_keytable_id, column_number, solution_by_row_id FROM `resolution` WHERE merge_id = ? ORDER BY joined_keytable_id, column_number;");
			          preparedStatement.setInt(1, mergeModel.getId());
			          preparedStatement.executeQuery();
			          ResultSet resultSet = preparedStatement.getResultSet();
			          
			          if (resultSet.next()) {
			        	  
			        	  resultSet.beforeFirst();
			        	  
			        	  while(resultSet.next()){
		
			        		 
			        		  Integer joinedKeytableId = resultSet.getInt("joined_keytable_id");
			        		  Integer columnNumber = resultSet.getInt("column_number");
			        		  String cellCoords = joinedKeytableId.toString() + "," + columnNumber.toString();
			        		  
			        		  solutionByRowIdUsingCellCoordsAsHashMap.put(cellCoords, resultSet.getInt("solution_by_row_id"));
			        	  
			        	  }
		
			      	  } else {
			      		  
			      		  //There may be no unresolved conflicts
			      		  //this.logger.info("Did not retrieve any solution_by_row_ids using the specified merge id: " + mergeModel.getId());
			      		  
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
		
		return solutionByRowIdUsingCellCoordsAsHashMap;
		
	}
	
	
	public HashMap<String, Integer> retrieveSolutionByCellIdUsingCellCoordsAsHashMapUsingMergeModel(
			MergeModel mergeModel) {
		
		HashMap<String, Integer> solutionByCellIdUsingCellCoordsAsHashMap = new HashMap<String, Integer>();

		try {
			
			Connection connection = this.getDataModel().getNewDatabaseConnection();
			 
			if (connection != null) {		
		
			      try{
			          PreparedStatement preparedStatement = connection.prepareStatement("SELECT joined_keytable_id, column_number, solution_by_cell_id FROM `resolution` WHERE merge_id = ? ORDER BY joined_keytable_id, column_number;");
			          preparedStatement.setInt(1, mergeModel.getId());
			          preparedStatement.executeQuery();
			          ResultSet resultSet = preparedStatement.getResultSet();
			          
			          if (resultSet.next()) {
			        	  
			        	  resultSet.beforeFirst();
			        	  
			        	  while(resultSet.next()){
		
			        		 
			        		  Integer joinedKeytableId = resultSet.getInt("joined_keytable_id");
			        		  Integer columnNumber = resultSet.getInt("column_number");
			        		  String cellCoords = joinedKeytableId.toString() + "," + columnNumber.toString();
			        		  
			        		  solutionByCellIdUsingCellCoordsAsHashMap.put(cellCoords, resultSet.getInt("solution_by_cell_id"));
			        	  
			        	  }
		
			      	  } else {
			      		  
			      		  //There may be no unresolved conflicts
			      		  //this.logger.info("Did not retrieve any solution_by_cell_ids using the specified merge id: " + mergeModel.getId());
			      		  
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
		
		return solutionByCellIdUsingCellCoordsAsHashMap;	
	}

	public HashMap<String, String> retrieveConstantUsingCellCoordsAsHashMapUsingMergeModel(
			MergeModel mergeModel) {

		HashMap<String, String> constantUsingCellCoordsAsHashMap = new HashMap<String, String>();

		try {
			
			Connection connection = this.getDataModel().getNewDatabaseConnection();
			 
			if (connection != null) {		
		
			      try{
			          PreparedStatement preparedStatement = connection.prepareStatement("SELECT joined_keytable_id, column_number, constant FROM `resolution` WHERE merge_id = ? ORDER BY joined_keytable_id, column_number;");
			          preparedStatement.setInt(1, mergeModel.getId());
			          preparedStatement.executeQuery();
			          ResultSet resultSet = preparedStatement.getResultSet();
			          
			          if (resultSet.next()) {
			        	  
			        	  resultSet.beforeFirst();
			        	  
			        	  while(resultSet.next()){
		
			        		 
			        		  Integer joinedKeytableId = resultSet.getInt("joined_keytable_id");
			        		  Integer columnNumber = resultSet.getInt("column_number");
			        		  String cellCoords = joinedKeytableId.toString() + "," + columnNumber.toString();
			        		  
			        		  constantUsingCellCoordsAsHashMap.put(cellCoords, resultSet.getString("constant"));
			        	  
			        	  }
		
			      	  } else {
			      		  
			      		  //There may be no unresolved conflicts
			      		  //this.logger.info("Did not retrieve any constants using the specified merge id: " + mergeModel.getId());
			      		  
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
		
		return constantUsingCellCoordsAsHashMap;
		
	}

	public HashMap<Integer, Boolean> retrieveNullOrConstantSolutionUsingColumnNumberAsHashMapUsingMergeModel(
			MergeModel mergeModel) {
		
		HashMap<Integer, Boolean> nullOrConstantSolutionUsingColumnNumberAsHashMap = new HashMap<Integer, Boolean>();
		
		try {
			
			Connection connection = this.getDataModel().getNewDatabaseConnection();
			 
			if (connection != null) {		
		
			      try{
			          PreparedStatement preparedStatement = connection.prepareStatement(
			        		  "SELECT column_number FROM `resolution` WHERE merge_id = ? " +
			          			"AND (solution_by_column_id = 3 OR solution_by_column_id = 4 OR solution_by_row_id = 3 OR solution_by_row_id = 4 OR solution_by_cell_id = 3 OR solution_by_cell_id = 4) " +
			          			"ORDER BY column_number" +
			          			";");
			          preparedStatement.setInt(1, mergeModel.getId());
			          preparedStatement.executeQuery();
			          ResultSet resultSet = preparedStatement.getResultSet();
			          
			          if (resultSet.next()) {
			        	  
			        	  resultSet.beforeFirst();
			        	  
			        	  while(resultSet.next()){
		
			        		  nullOrConstantSolutionUsingColumnNumberAsHashMap.put(resultSet.getInt("column_number"), true);
			        	  
			        	  }
		
			      	  } else {
			      		  
			      		  //There may be no unresolved conflicts
			      		  //this.logger.info("Did not retrieve any null/constant counts using the specified merge id: " + mergeModel.getId());
			      		  
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
		
		return nullOrConstantSolutionUsingColumnNumberAsHashMap;
	}

	public CachedRowSet retrieveResolutionsAsCachedRowSetUsingMergeId(Integer mergeId, Connection connection) {

		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);
		
        Class<?> cachedRowSetImplClass = null;
		try {
			cachedRowSetImplClass = Class.forName("com.sun.rowset.CachedRowSetImpl");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		CachedRowSet resolutionsAsCachedRowSet = null;
		try {
			resolutionsAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `resolution` WHERE merge_id = ? ORDER BY joined_keytable_id, column_number;");
	          preparedStatement.setInt(1, mergeModel.getId());
	          preparedStatement.executeQuery();
	         
	          resolutionsAsCachedRowSet.populate(preparedStatement.getResultSet());
	          
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
		return resolutionsAsCachedRowSet;
		
	}

	public void setExportRepositoryFilepath(String exportRepositoryFilepath) {
		this.exportRepositoryFilepath = exportRepositoryFilepath;
	}

	public void setExportSuccessful(boolean exportSuccessful) {
		this.exportSuccessful = exportSuccessful;
	}

	public boolean getExportSuccessful() {
		return this.exportSuccessful;
	}

	public String getExportRepositoryFilepath() {
		return this.exportRepositoryFilepath;
	}


	
	
	
	
}
