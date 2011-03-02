package org.cggh.tools.dataMerger.data.resolutionsByColumn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.DataModel;
import org.cggh.tools.dataMerger.data.joins.JoinModel;
import org.cggh.tools.dataMerger.data.joins.JoinsModel;
import org.cggh.tools.dataMerger.data.merges.MergeModel;
import org.cggh.tools.dataMerger.data.users.UserModel;
import org.cggh.tools.dataMerger.functions.merges.MergesFunctionsModel;
import org.cggh.tools.dataMerger.functions.resolutionsByColumn.ResolutionsByColumnFunctionsModel;
import org.json.JSONArray;
import org.json.JSONObject;


public class ResolutionsByColumnModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2346858760723955545L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.resolutions");

	private DataModel dataModel;
	
	public ResolutionsByColumnModel() {

		this.setDataModel(new DataModel());
		
	}

    public void setDataModel (final DataModel dataModel) {
        this.dataModel  = dataModel;
    }
    public DataModel getDataModel () {
        return this.dataModel;
    } 
	
	public void createResolutionByColumnUsingResolutionByColumnModel(
			ResolutionByColumnModel resolutionByColumnModel,
			Connection connection) {
		
	      try {

	          PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `resolution_by_column` (merge_id, column_number, problem_by_column_id, conflicts_count, solution_by_column_id, constant) VALUES (?, ?, ?, ?, ?, ?);");
	          preparedStatement.setInt(1, resolutionByColumnModel.getMergeModel().getId());
	          preparedStatement.setInt(2, resolutionByColumnModel.getColumnNumber());
	          preparedStatement.setInt(3, resolutionByColumnModel.getProblemByColumnModel().getId());
	          preparedStatement.setInt(4, resolutionByColumnModel.getConflictsCount());
	          if (resolutionByColumnModel.getSolutionByColumnModel().getId() != null) {
	        	  preparedStatement.setInt(5, resolutionByColumnModel.getSolutionByColumnModel().getId());
	          } else {
	        	  preparedStatement.setNull(5, java.sql.Types.INTEGER);
	          }
	          if (resolutionByColumnModel.getConstant() != null) {
	        	  preparedStatement.setString(6, resolutionByColumnModel.getConstant());
	          } else {
	        	  preparedStatement.setNull(6, java.sql.Types.VARCHAR);
	          }
	        

	          preparedStatement.executeUpdate();
	          preparedStatement.close();
	          
	
	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 	
		
		
	}

	public String retrieveResolutionsByColumnAsDecoratedXHTMLTableUsingMergeModel (MergeModel mergeModel) {
		
		String resolutionsByColumnAsDecoratedXHTMLTable = null;
		
		  CachedRowSet resolutionsByColumnAsCachedRowSet = this.retrieveResolutionsByColumnAsCachedRowSetUsingMergeId(mergeModel.getId());

		  if (resolutionsByColumnAsCachedRowSet != null) {
			  

			  	CachedRowSet solutionsByColumnAsCachedRowSet = this.retrieveSolutionsByColumnAsCachedRowSet();

			  	ResolutionsByColumnFunctionsModel resolutionsByColumnFunctionsModel = new ResolutionsByColumnFunctionsModel();
			  	resolutionsByColumnFunctionsModel.setResolutionsByColumnAsCachedRowSet(resolutionsByColumnAsCachedRowSet);
			  	resolutionsByColumnFunctionsModel.setSolutionsByColumnAsCachedRowSet(solutionsByColumnAsCachedRowSet);
			  	resolutionsByColumnFunctionsModel.setResolutionsByColumnAsDecoratedXHTMLTableUsingResolutionsByColumnAsCachedRowSet();
			  	resolutionsByColumnAsDecoratedXHTMLTable = resolutionsByColumnFunctionsModel.getResolutionsByColumnAsDecoratedXHTMLTable();
			    
		  } else {
			  
			  //TODO: Error handling
			  this.logger.warning("Error: resolutionsByColumnAsCachedRowSet is null");
			  resolutionsByColumnAsDecoratedXHTMLTable = "<p>Error: resolutionsByColumnAsCachedRowSet is null</p>";
			  
		  }
		
		return resolutionsByColumnAsDecoratedXHTMLTable;
	}


	public CachedRowSet retrieveSolutionsByColumnAsCachedRowSet() {
		
		CachedRowSet solutionsByColumnAsCachedRowSet = null;

		   String CACHED_ROW_SET_IMPL_CLASS = "com.sun.rowset.CachedRowSetImpl";
		   
			try {
				
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				
				Connection connection = this.getDataModel().getNewConnection();
				 
				if (!connection.isClosed()) {
				
					 //FIXME: Apparently a bug in CachedRowSet using getX('columnAlias') aka columnLabel, which actually only works with getX('columnName'), so using getX('columnIndex').
					 
					
				      try{
				          PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from solution_by_column;");
				          preparedStatement.executeQuery();
				          Class<?> cachedRowSetImplClass = Class.forName(CACHED_ROW_SET_IMPL_CLASS);
				          solutionsByColumnAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
				          solutionsByColumnAsCachedRowSet.populate(preparedStatement.getResultSet());
				          preparedStatement.close();
	
				        }
				        catch(SQLException sqlException){
				        	this.logger.severe(sqlException.toString());
					    	sqlException.printStackTrace();
				        } 	
				
					connection.close();
					
				} else {
					this.logger.severe("connection.isClosed");
				}
					
			} 
			catch (Exception e) {
				this.logger.severe(e.toString());
				e.printStackTrace();
			}
		
		return solutionsByColumnAsCachedRowSet;
	}

	public CachedRowSet retrieveResolutionsByColumnAsCachedRowSetUsingMergeId(
			Integer mergeId) {
		
		CachedRowSet resolutionsByColumnAsCachedRowSet = null;
		
		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);
		
		   String CACHED_ROW_SET_IMPL_CLASS = "com.sun.rowset.CachedRowSetImpl";
		   
			try {
				
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				
				Connection connection = this.getDataModel().getNewConnection();
				 
				if (!connection.isClosed()) {
				
					 //FIXME: Apparently a bug in CachedRowSet using getX('columnAlias') aka columnLabel, which actually only works with getX('columnName'), so using getX('columnIndex').
					 
					
				      try{
				          PreparedStatement preparedStatement = connection.prepareStatement(
				        		  "SELECT resolution_by_column.column_number, `join`.column_name, resolution_by_column.conflicts_count, resolution_by_column.problem_by_column_id, problem_by_column.description, resolution_by_column.solution_by_column_id, resolution_by_column.constant " + 
				        		  "FROM resolution_by_column " +
				        		  "JOIN `join` ON `join`.column_number = resolution_by_column.column_number " +
				        		  "JOIN problem_by_column ON problem_by_column.id = resolution_by_column.problem_by_column_id " +
				        		  "WHERE resolution_by_column.merge_id = ?" + 
				        		  ";");
				          preparedStatement.setInt(1, mergeModel.getId());
				          preparedStatement.executeQuery();
				          Class<?> cachedRowSetImplClass = Class.forName(CACHED_ROW_SET_IMPL_CLASS);
				          resolutionsByColumnAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
				          resolutionsByColumnAsCachedRowSet.populate(preparedStatement.getResultSet());
				          preparedStatement.close();
	
				        }
				        catch(SQLException sqlException){
				        	this.logger.severe(sqlException.toString());
					    	sqlException.printStackTrace();
				        } 	
				
					connection.close();
					
				} else {
					
					System.out.println("connection.isClosed");
				}
					
			} 
			catch (Exception e) {
				System.out.println("Exception from getMergesAsCachedRowSet.");
				e.printStackTrace();
			}
	
	
	
	     return resolutionsByColumnAsCachedRowSet;
	}

	public void updateResolutionsByColumnByMergeIdUsingResolutionsByColumnAsJSONObject(
			Integer mergeId, JSONObject resolutionsByColumnAsJsonObject) {
		// TODO Auto-generated method stub
		
		//for each index in column_number, update the fields present (except for keys)
		
		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);

		try {
			
			Connection connection = this.getDataModel().getNewConnection();
			 
			if (!connection.isClosed()) {
					
		          //Insert all the joins from this JSON Object
		          JSONArray columnNumbers = resolutionsByColumnAsJsonObject.getJSONArray("column_number");
		          JSONArray problemByColumnIds = resolutionsByColumnAsJsonObject.getJSONArray("problem_by_column_id");
		          JSONArray solutionByColumnIds = resolutionsByColumnAsJsonObject.getJSONArray("solution_by_column_id");
		          JSONArray constants = resolutionsByColumnAsJsonObject.getJSONArray("constant");

		          for (int i = 0; i < columnNumbers.length(); i++) {
		        	  
		        	  ResolutionByColumnModel resolutionByColumnModel = new ResolutionByColumnModel();
		        	  
		        	  resolutionByColumnModel.setMergeModel(mergeModel);
		        	  resolutionByColumnModel.setColumnNumber(columnNumbers.getInt(i));
		        	  
		        	  ProblemByColumnModel problemByColumnModel = new ProblemByColumnModel();
		        	  problemByColumnModel.setId(problemByColumnIds.getInt(i));
		        	  resolutionByColumnModel.setProblemByColumnModel(problemByColumnModel);
		        	  
		        	  //FIXME: Don't want to overwrite conflicts_count with null. Or maybe set to 0 if we now have a solution?
		        	  
		        	  SolutionByColumnModel solutionByColumnModel = new SolutionByColumnModel();
		        	  solutionByColumnModel.setId(solutionByColumnIds.getInt(i));
		        	  resolutionByColumnModel.setSolutionByColumnModel(solutionByColumnModel);
		        	  
		        	  resolutionByColumnModel.setConstant(constants.getString(i));
		        	  
		        	  
		        	  //TODO
		        	  
		        	  this.updateResolutionByColumnUsingResolutionByColumnModel(resolutionByColumnModel, connection);
		        	  
		          }
		          
					
				connection.close();
				
			} else {
				
				this.logger.severe("connection.isClosed");
			}
				
		} 
		catch (Exception e) {
			
			this.logger.severe(e.toString());
			e.printStackTrace();
		}
		
	}

	public void updateResolutionByColumnUsingResolutionByColumnModel(
			ResolutionByColumnModel resolutionByColumnModel,
			Connection connection) {
		
		
	      try {

	          PreparedStatement preparedStatement = connection.prepareStatement("UPDATE resolution_by_column SET solution_by_column_id = ?, constant = ? WHERE merge_id = ? AND column_number = ? AND problem_by_column_id = ?;");
	          
	          
	          preparedStatement.setInt(1, resolutionByColumnModel.getSolutionByColumnModel().getId());
	          preparedStatement.setString(2, resolutionByColumnModel.getConstant());
	          
	          preparedStatement.setInt(3, resolutionByColumnModel.getMergeModel().getId());
	          preparedStatement.setInt(4, resolutionByColumnModel.getColumnNumber());
	          preparedStatement.setInt(5, resolutionByColumnModel.getProblemByColumnModel().getId());
	          
	          preparedStatement.executeUpdate();
	          preparedStatement.close();
	          
	
	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 	
		
	}
}
