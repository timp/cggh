package org.cggh.tools.dataMerger.data.resolutions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class ResolutionsModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2346858760723955545L;

	public ResolutionsModel() {

		
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

	
}
