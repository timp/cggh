package org.cggh.tools.dataMerger.scripts.merges;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.cggh.tools.dataMerger.data.merges.MergeModel;
import org.cggh.tools.dataMerger.data.merges.MergesModel;



public class MergeScriptsModel implements java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3828652891254659545L;



	public MergeScriptsModel() {
		
	}



	
	public MergeModel retrieveMergeAsMergeModelThroughDeterminingDatatable1DuplicateKeysCountUsingMergeModel(MergeModel mergeModel, Connection connection) {
		
		String Datatable1KeyColumnNamesAsSQLCSV = null;

	      StringBuffer stringBuffer   = new StringBuffer(2 * (mergeModel.getDatatable1Model().getKeyColumnNamesAsStringList().size() + 1));

	      stringBuffer.append('`');
	      for (int i = 0; i < mergeModel.getDatatable1Model().getKeyColumnNamesAsStringList().size(); i++) {
	    	  
	    	  stringBuffer.append(mergeModel.getDatatable1Model().getKeyColumnNamesAsStringList().get(i));

	          if (i != mergeModel.getDatatable1Model().getKeyColumnNamesAsStringList().size() - 1) {
	        	  stringBuffer.append('`');
	        	  stringBuffer.append(',');
	        	  stringBuffer.append('`');
	          }
	      }
	      stringBuffer.append('`');


	      Datatable1KeyColumnNamesAsSQLCSV = stringBuffer.toString();
	      
	      //TODO:
	      System.out.println("Datatable1KeyColumnNamesAsSQLCSV: " + Datatable1KeyColumnNamesAsSQLCSV);
		
	      try{
	    	  //SELECT SUM(duplicateValuesCount) AS totalDuplicateValuesCount FROM (SELECT COUNT(CONCAT(`Row`,`ID`)) AS duplicateValuesCount FROM `datatable_3` GROUP BY CONCAT(`Row`,`ID`) HAVING duplicateValuesCount > 1) AS duplicateValuesCounts;
	    	  PreparedStatement preparedStatement = connection.prepareStatement("SELECT SUM(duplicateValuesCount) AS totalDuplicateValuesCount FROM (SELECT COUNT(CONCAT(" + Datatable1KeyColumnNamesAsSQLCSV +  ")) AS duplicateValuesCount FROM `" + mergeModel.getDatatable1Model().getName()+ "` GROUP BY CONCAT(" + Datatable1KeyColumnNamesAsSQLCSV +  ") HAVING duplicateValuesCount > 1) AS duplicateValuesCounts;");
	          preparedStatement.executeQuery();
	          ResultSet resultSet = preparedStatement.getResultSet();
	          
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();

	        	  Integer totalDuplicateValuesCount = resultSet.getInt("totalDuplicateValuesCount");
	        	  
	        	  if (totalDuplicateValuesCount != null) {
	        		  
	        		  mergeModel.getDatatable1Model().setDuplicateKeysCount(totalDuplicateValuesCount);
	        		  
	        	  } else {
	        		  
	        		  mergeModel.getDatatable1Model().setDuplicateKeysCount(0);
	        	  }
	        	  
	      	  } else {
	      		  
	      		  mergeModel.getDatatable1Model().setDuplicateKeysCount(0);
	      	  }
	          
	          resultSet.close();
	          preparedStatement.close();
	          
	          
	          //TODO:
	          System.out.println("Setting dt1 duplicate keys count to: " + mergeModel.getDatatable1Model().getDuplicateKeysCount());
	          
	          MergesModel mergesModel = new MergesModel();
	          
	          mergesModel.updateMergeUsingMergeModel(mergeModel, connection);
	          

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 

	        
	        return mergeModel;
	}

	//TODO: Condense these two identical methods into one.
	public MergeModel retrieveMergeAsMergeModelThroughDeterminingDatatable2DuplicateKeysCountUsingMergeModel(MergeModel mergeModel, Connection connection) {
		
		String Datatable2KeyColumnNamesAsSQLCSV = null;

	      StringBuffer stringBuffer   = new StringBuffer(2 * (mergeModel.getDatatable2Model().getKeyColumnNamesAsStringList().size() + 1));

	      stringBuffer.append('`');
	      for (int i = 0; i < mergeModel.getDatatable2Model().getKeyColumnNamesAsStringList().size(); i++) {
	    	  
	    	  stringBuffer.append(mergeModel.getDatatable2Model().getKeyColumnNamesAsStringList().get(i));

	          if (i != mergeModel.getDatatable2Model().getKeyColumnNamesAsStringList().size() - 1) {
	        	  stringBuffer.append('`');
	        	  stringBuffer.append(',');
	        	  stringBuffer.append('`');
	          }
	      }
	      stringBuffer.append('`');


	      Datatable2KeyColumnNamesAsSQLCSV = stringBuffer.toString();
	      
	      //TODO:
	      System.out.println("Datatable2KeyColumnNamesAsSQLCSV: " + Datatable2KeyColumnNamesAsSQLCSV);
		
	      try{
	    	  //SELECT SUM(duplicateValuesCount) AS totalDuplicateValuesCount FROM (SELECT COUNT(CONCAT(`Row`,`ID`)) AS duplicateValuesCount FROM `datatable_3` GROUP BY CONCAT(`Row`,`ID`) HAVING duplicateValuesCount > 1) AS duplicateValuesCounts;
	    	  PreparedStatement preparedStatement = connection.prepareStatement("SELECT SUM(duplicateValuesCount) AS totalDuplicateValuesCount FROM (SELECT COUNT(CONCAT(" + Datatable2KeyColumnNamesAsSQLCSV +  ")) AS duplicateValuesCount FROM `" + mergeModel.getDatatable2Model().getName()+ "` GROUP BY CONCAT(" + Datatable2KeyColumnNamesAsSQLCSV +  ") HAVING duplicateValuesCount > 1) AS duplicateValuesCounts;");
	          preparedStatement.executeQuery();
	          ResultSet resultSet = preparedStatement.getResultSet();
	          
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();

	        	  Integer totalDuplicateValuesCount = resultSet.getInt("totalDuplicateValuesCount");
	        	  
	        	  if (totalDuplicateValuesCount != null) {
	        		  
	        		  mergeModel.getDatatable2Model().setDuplicateKeysCount(totalDuplicateValuesCount);
	        		  
	        	  } else {
	        		  
	        		  mergeModel.getDatatable2Model().setDuplicateKeysCount(0);
	        	  }
	        	  
	      	  } else {
	      		  
	      		  mergeModel.getDatatable2Model().setDuplicateKeysCount(0);
	      	  }
	          
	          resultSet.close();
	          preparedStatement.close();
	          
	          //TODO:
	          System.out.println("Setting dt2 duplicate keys count to: " + mergeModel.getDatatable2Model().getDuplicateKeysCount());
	          
	          
	          MergesModel mergesModel = new MergesModel();
	          
	          mergesModel.updateMergeUsingMergeModel(mergeModel, connection);
	          

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 

	        
	       return mergeModel;
	}

	
}
