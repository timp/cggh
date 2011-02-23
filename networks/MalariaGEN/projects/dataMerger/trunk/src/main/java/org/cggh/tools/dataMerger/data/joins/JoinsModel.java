package org.cggh.tools.dataMerger.data.joins;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.merges.MergeModel;


public class JoinsModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2278497615197327793L;
	private Integer nextColumnNumber;
	private CachedRowSet joinsAsCachedRowSet;
	private Integer keysCount;
	private CachedRowSet crossDatatableJoinsAsCachedRowSet;

	//Must not have a joinModel, because joinModel has a mergeModel, which has a joinsModel, causes StackOverflowError
	//private JoinModel joinModel;
	
	
	public JoinsModel() {

	}

    
	public void setJoinsAsCachedRowSet(CachedRowSet joinsAsCachedRowSet) {
		this.joinsAsCachedRowSet = joinsAsCachedRowSet;	
	}   
    public CachedRowSet getJoinsAsCachedRowSet () {
        return this.joinsAsCachedRowSet;
    }

    

	public void setKeysCount(Integer keysCount) {

		this.keysCount = keysCount;
		
	}    
    public Integer getKeysCount () {
        return this.keysCount;
    }   
    
	public Integer getNextColumnNumberByMergeId(Integer mergeId,
			Connection connection) {
		
		//Get-Bys must only set and return the item between get and By (and its children).
		//So none of this stuff here: this.getJoinModel().getMergeModel().setId(mergeId);
		
		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);
		
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(column_number) AS maxColumnNumber FROM `join` WHERE merge_id = ?;");
	          preparedStatement.setInt(1, mergeModel.getId());
	          preparedStatement.executeQuery();
	          ResultSet resultSet = preparedStatement.getResultSet();
	          
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();

	        	  Integer maxColumnNumber = resultSet.getInt("maxColumnNumber");
	        	  
	        	  if (maxColumnNumber != null) {
	        		  
	        		  this.setNextColumnNumber(maxColumnNumber + 1);
	        		  
	        	  } else {
	        		  
	        		  this.setNextColumnNumber(1);
	        	  }

	        	  
	        	  
	      	  } else {
	      		  
	      		  this.setNextColumnNumber(1);
	      		  
	      	  }
	          
	          resultSet.close();
	          
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
		return this.getNextColumnNumber();
	}


	public void setNextColumnNumber(Integer nextColumnNumber) {
		this.nextColumnNumber = nextColumnNumber;
	}


	public Integer getNextColumnNumber() {
		return this.nextColumnNumber;
	}


	public void createJoin(Integer mergeId, Integer columnNumber, Boolean key,
			String datatable1ColumnName, String datatable2ColumnName,
			String columnName, Connection connection) {
		
		// Creates must not affect exterior models.
		// So don't use this.joinModel or this.setJoinModel().
		
		JoinModel joinModel = new JoinModel();
		
		joinModel.getMergeModel().setId(mergeId);
		joinModel.setColumnNumber(columnNumber);
		joinModel.setKey(key);
		joinModel.setDatatable1ColumnName(datatable1ColumnName);
		joinModel.setDatatable2ColumnName(datatable2ColumnName);
		joinModel.setColumnName(columnName);
		
		
	      try {

	          PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `join` (merge_id, column_number, `key`, datatable_1_column_name, datatable_2_column_name, column_name) VALUES (?, ?, ?, ?, ?, ?);");
	          preparedStatement.setInt(1, joinModel.getMergeModel().getId());
	          preparedStatement.setInt(2, joinModel.getColumnNumber());
	          preparedStatement.setBoolean(3, joinModel.getKey());
	          preparedStatement.setString(4, joinModel.getDatatable1ColumnName());
	          preparedStatement.setString(5, joinModel.getDatatable2ColumnName());
	          preparedStatement.setString(6, joinModel.getColumnName());
	          preparedStatement.executeUpdate();
	          preparedStatement.close();
	          
	
	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 	
	}


	public void setJoinsModelByMergeId(Integer mergeId, Connection connection) {

		// Set-bys must only set the set*By item and all its children.
		// Note that mergeModel is a child of JoinModel, not JoinsModel.
		MergeModel mergeModel = new MergeModel();
		
		mergeModel.setId(mergeId);
		
		//TODO: Nullify the set*By item properties
		this.setNextColumnNumber(null);
		this.setJoinsAsCachedRowSet(null);
		this.setKeysCount(null);
		   
		
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, merge_id, column_number, `key`, datatable_1_column_name, datatable_2_column_name, constant_1, constant_2, column_name FROM `join` WHERE merge_id = ?;");
	          preparedStatement.setInt(1, mergeModel.getId());
	          preparedStatement.executeQuery();
	          ResultSet resultSet = preparedStatement.getResultSet();
	          
	          String CACHED_ROW_SET_IMPL_CLASS = "com.sun.rowset.CachedRowSetImpl"; 
	          Class<?> cachedRowSetImplClass = Class.forName(CACHED_ROW_SET_IMPL_CLASS);
	          CachedRowSet joinsAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
	          joinsAsCachedRowSet.populate(preparedStatement.getResultSet());
	          
	          resultSet.close();
	          preparedStatement.close();

	          this.setJoinsAsCachedRowSet(joinsAsCachedRowSet);
	          

	          
		      try{
		          PreparedStatement preparedStatement2 = connection.prepareStatement(
		        		  	"SELECT COUNT(`key`) AS keysCount FROM `join` " + 
		        		  	"WHERE merge_id = ? AND `key` = ?;"
		        		  	);
		          preparedStatement2.setInt(1, mergeModel.getId());
		          preparedStatement2.setBoolean(2, true);
		          preparedStatement2.executeQuery();
		          ResultSet resultSet2 = preparedStatement2.getResultSet();
		          
		          // There may be no such record
		          if (resultSet2.next()) {
		        	  
		        	  resultSet2.first();
		        	  
		        	  Integer keysCount = resultSet2.getInt("keysCount");
		        	  
		        	  if (keysCount != null) {
		        		  
		        		  this.setKeysCount(resultSet2.getInt("keysCount"));
		        	  
		        	  } else {
		        		  
		        		  //TODO: Or should this stay as null?
		        		  this.setKeysCount(0);
		        	  }
		        	  
		          } else {
		        	  this.setKeysCount(0);
		          }
		          
		          resultSet2.close();
		          preparedStatement2.close();
		          
		          
			      try{
			          PreparedStatement preparedStatement3 = connection.prepareStatement(
			        		  "SELECT id, merge_id, column_number, `key`, datatable_1_column_name, " + 
			        		  "datatable_2_column_name, constant_1, constant_2, column_name FROM `join` WHERE merge_id = ? " +
			        		  "AND (datatable_1_column_name IS NOT NULL AND datatable_2_column_name IS NOT NULL);");
			          preparedStatement3.setInt(1, mergeModel.getId());
			          preparedStatement3.executeQuery();
			          ResultSet resultSet3 = preparedStatement3.getResultSet();
			          
			          CachedRowSet crossDatatableJoinsAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
			          crossDatatableJoinsAsCachedRowSet.populate(preparedStatement3.getResultSet());
			          
			          resultSet3.close();
			          preparedStatement3.close();

			          this.setCrossDatatableJoinsAsCachedRowSet(crossDatatableJoinsAsCachedRowSet);
		          
			        }
			        catch(SQLException sqlException){
				    	sqlException.printStackTrace();
			        } catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
		          

		        }
		        catch(SQLException sqlException){
		        	System.out.println("<p>" + sqlException + "</p>");
			    	sqlException.printStackTrace();
		        } 
	          
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
		
	}


	public void setCrossDatatableJoinsAsCachedRowSet(
			final CachedRowSet crossDatatableJoinsAsCachedRowSet) {
		this.crossDatatableJoinsAsCachedRowSet = crossDatatableJoinsAsCachedRowSet;
	}



	public void setJoinsModelByMergeModel(MergeModel mergeModel, Connection connection) {

		// Set-bys must only set the set*By item and all its children.
		// Note that mergeModel is a child of JoinModel, not JoinsModel.
		
		this.setJoinsModelByMergeId(mergeModel.getId(), connection);
		
	}



	public CachedRowSet getCrossDatatableJoinsAsCachedRowSet() {
		return this.crossDatatableJoinsAsCachedRowSet;
	}




	public void updateJoinByJoinModel(JoinModel joinModel, Connection connection) {
		
	      try {

	          PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `join` SET merge_id = ?, column_number = ?, `key` = ?, datatable_1_column_name = ?, datatable_2_column_name = ?, column_name = ? WHERE id = ?;");
	          preparedStatement.setInt(1, joinModel.getMergeModel().getId());
	          preparedStatement.setInt(2, joinModel.getColumnNumber());
	          preparedStatement.setBoolean(3, joinModel.getKey());
	          preparedStatement.setString(4, joinModel.getDatatable1ColumnName());
	          preparedStatement.setString(5, joinModel.getDatatable2ColumnName());
	          preparedStatement.setString(6, joinModel.getColumnName());
	          preparedStatement.setInt(7, joinModel.getId());
	          preparedStatement.executeUpdate();
	          preparedStatement.close();
	          
	
	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
	}











 
}
