package org.cggh.tools.dataMerger.data.joins;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.DataModel;
import org.cggh.tools.dataMerger.data.merges.MergeModel;
import org.cggh.tools.dataMerger.data.users.UserModel;
import org.json.JSONArray;
import org.json.JSONObject;


public class JoinsModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2278497615197327793L;
	private Integer nextColumnNumber;
	private CachedRowSet joinsAsCachedRowSet;
	private Integer keysCount;
	private CachedRowSet crossDatatableJoinsAsCachedRowSet;
	private DataModel dataModel;
	private UserModel userModel;
	private CachedRowSet keyCrossDatatableJoinsAsCachedRowsetByMergeId;
	private CachedRowSet nonKeyCrossDatatableJoinsAsCachedRowsetByMergeId;

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


	public void createJoinUsingJoinModel(JoinModel joinModel, Connection connection) {
		
		
	      try {

	          PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `join` (merge_id, column_number, `key`, datatable_1_column_name, datatable_2_column_name, constant_1, constant_2, column_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
	          preparedStatement.setInt(1, joinModel.getMergeModel().getId());
	          preparedStatement.setInt(2, joinModel.getColumnNumber());
	          preparedStatement.setBoolean(3, joinModel.getKey());
	          
	          preparedStatement.setString(4, joinModel.getDatatable1ColumnName());
	          preparedStatement.setString(5, joinModel.getDatatable2ColumnName());
	          
	          preparedStatement.setString(6, joinModel.getConstant1());
	          preparedStatement.setString(7, joinModel.getConstant2());
	          
	          preparedStatement.setString(8, joinModel.getColumnName());
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

	          PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `join` SET `key` = ?, datatable_1_column_name = ?, datatable_2_column_name = ?, column_name = ? WHERE merge_id = ? AND column_number = ? ;");
	          
	          preparedStatement.setBoolean(1, joinModel.getKey());
	          preparedStatement.setString(2, joinModel.getDatatable1ColumnName());
	          preparedStatement.setString(3, joinModel.getDatatable2ColumnName());
	          preparedStatement.setString(4, joinModel.getColumnName());
	          
	          preparedStatement.setInt(5, joinModel.getMergeModel().getId());
	          preparedStatement.setInt(6, joinModel.getColumnNumber());
	          
	          preparedStatement.executeUpdate();
	          preparedStatement.close();
	          
	
	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
	        //TODO: This will have to trigger a recalculation of conflicts, but may be done a level up (by the referrer/caller of this method)
	        
	}


	public CachedRowSet retrieveJoinsAsCachedRowSetByMergeId(Integer mergeId,
			Connection connection) {

		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);
		
        Class<?> cachedRowSetImplClass = null;
		try {
			cachedRowSetImplClass = Class.forName("com.sun.rowset.CachedRowSetImpl");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		CachedRowSet joinsAsCachedRowSet = null;
		try {
			joinsAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `join` WHERE merge_id = ?;");
	          preparedStatement.setInt(1, mergeModel.getId());
	          preparedStatement.executeQuery();
	         
	          joinsAsCachedRowSet.populate(preparedStatement.getResultSet());
	          
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
		return joinsAsCachedRowSet;
	}


	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
	}

	public DataModel getDataModel() {

		return this.dataModel;
	}



	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}


	public JoinsModel retrieveJoinsAsJoinsModelByMergeId(Integer mergeId,
			Connection connection) {

		JoinsModel joinsModel = new JoinsModel();
		
		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);

		
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT merge_id, column_number, `key`, datatable_1_column_name, datatable_2_column_name, constant_1, constant_2, column_name FROM `join` " + 
	        	"WHERE merge_id = ?;");
	          preparedStatement.setInt(1, mergeModel.getId());
	          preparedStatement.executeQuery();
	          ResultSet resultSet = preparedStatement.getResultSet();
	          
	          String CACHED_ROW_SET_IMPL_CLASS = "com.sun.rowset.CachedRowSetImpl"; 
	          Class<?> cachedRowSetImplClass = Class.forName(CACHED_ROW_SET_IMPL_CLASS);
	          CachedRowSet joinsAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
	          joinsAsCachedRowSet.populate(preparedStatement.getResultSet());
	          
	          resultSet.close();
	          preparedStatement.close();

	          joinsModel.setJoinsAsCachedRowSet(joinsAsCachedRowSet);
	          

	          
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
		        		  
		        		  joinsModel.setKeysCount(resultSet2.getInt("keysCount"));
		        	  
		        	  } else {
		        		  
		        		  //TODO: Or should this stay as null?
		        		  joinsModel.setKeysCount(0);
		        	  }
		        	  
		          } else {
		        	  joinsModel.setKeysCount(0);
		          }
		          
		          resultSet2.close();
		          preparedStatement2.close();
		          
		          
			      try{
			          PreparedStatement preparedStatement3 = connection.prepareStatement(
			        		  "SELECT merge_id, column_number, `key`, datatable_1_column_name, " + 
			        		  "datatable_2_column_name, constant_1, constant_2, column_name FROM `join` WHERE merge_id = ? " +
			        		  "AND (datatable_1_column_name IS NOT NULL AND datatable_2_column_name IS NOT NULL);");
			          preparedStatement3.setInt(1, mergeModel.getId());
			          preparedStatement3.executeQuery();
			          ResultSet resultSet3 = preparedStatement3.getResultSet();
			          
			          CachedRowSet crossDatatableJoinsAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
			          crossDatatableJoinsAsCachedRowSet.populate(preparedStatement3.getResultSet());
			          
			          resultSet3.close();
			          preparedStatement3.close();

			          joinsModel.setCrossDatatableJoinsAsCachedRowSet(crossDatatableJoinsAsCachedRowSet);
		          
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
			
			
		return joinsModel;
		
	}


	public void updateJoinsByMergeIdUsingJoinsAsJSONObject(Integer mergeId,
			JSONObject joinsAsJsonObject) {
		
		//FIXME: This is cutting a corner (replacing rather than updating).
		
		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);

		try {
			
			Connection connection = this.getDataModel().getNewConnection();
			 
			if (!connection.isClosed()) {
					
				  //Remove all the joins for this mergeId
		          PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `join` WHERE merge_id = ?;");
		          preparedStatement.setInt(1, mergeModel.getId());
		          preparedStatement.executeUpdate();
		          preparedStatement.close();
					
		          //Insert all the joins from this JSON Object
		          JSONArray columnNumbers = joinsAsJsonObject.getJSONArray("column_number");
		          JSONArray columnNames = joinsAsJsonObject.getJSONArray("column_name");

		          for (int i = 0; i < columnNumbers.length(); i++) {
		        	  
		        	  JoinModel joinModel = new JoinModel();
		        	  
		        	  joinModel.setMergeModel(mergeModel);
		        	  
		        	  joinModel.setColumnNumber(columnNumbers.getInt(i));
		        	  
		        	  if (joinsAsJsonObject.has("key-" + columnNumbers.getInt(i))) {
		        	  
		        		  joinModel.setKey(true);
		        		  
		        	  } else {
		        		  
		        		  joinModel.setKey(false);
		        	  }

		        	  if (joinsAsJsonObject.has("datatable_1_column_name-" + columnNumbers.getInt(i))) { 
		        		  joinModel.setDatatable1ColumnName(joinsAsJsonObject.getString("datatable_1_column_name-" + columnNumbers.getInt(i)));  		  
		        	  } else {
		        		  joinModel.setDatatable1ColumnName(null);
		        	  }

		        	  if (joinsAsJsonObject.has("datatable_2_column_name-" + columnNumbers.getInt(i))) { 
		        		  joinModel.setDatatable2ColumnName(joinsAsJsonObject.getString("datatable_2_column_name-" + columnNumbers.getInt(i)));  		  
		        	  } else {
		        		  joinModel.setDatatable2ColumnName(null);
		        	  }

		        	  if (joinsAsJsonObject.has("constant_1-" + columnNumbers.getInt(i))) { 
		        		  joinModel.setConstant1(joinsAsJsonObject.getString("constant_1-" + columnNumbers.getInt(i)));  		  
		        	  } else {
		        		  joinModel.setConstant1(null);
		        	  }

		        	  if (joinsAsJsonObject.has("constant_2-" + columnNumbers.getInt(i))) { 
		        		  joinModel.setConstant2(joinsAsJsonObject.getString("constant_2-" + columnNumbers.getInt(i)));  		  
		        	  } else {
		        		  joinModel.setConstant2(null);
		        	  }    	  
		        	  
		        	  joinModel.setColumnName(columnNames.getString(i));
		        	  
		        	  this.createJoinUsingJoinModel(joinModel, connection);
		        	  
		          }
		          
		          
		          //TODO: This should then trigger a recalculation of conflicts
		          //(If/when transfer to a column_id/join_id method, would need to determine whether or not to calculate.)
					
				connection.close();
				
			} else {
				
				System.out.println("connection.isClosed");
			}
				
		} 
		catch (Exception e) {
			
			//TODO:
			System.out.println("Exception in updateJoinsByMergeIdUsingJoinsAsJSONObject");
			e.printStackTrace();
		}
		
	}


	public CachedRowSet retrieveJoinsAsCachedRowSetByMergeId(Integer mergeId) {

		
		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);

		CachedRowSet joinsAsCachedRowSet = null;
		
		try {
			
			Connection connection = this.getDataModel().getNewConnection();
			 
			if (!connection.isClosed()) {
		
				joinsAsCachedRowSet = this.retrieveJoinsAsCachedRowSetByMergeId(mergeId, connection);
				
				connection.close();
				
			} else {
				
				System.out.println("connection.isClosed");
			}
				
		} 
		catch (Exception e) {
			
			//TODO:
			System.out.println("Exception in updateJoinsByMergeIdUsingJoinsAsJSONObject");
			e.printStackTrace();
		}				
				
		return joinsAsCachedRowSet;
	}


	public CachedRowSet retrieveKeyCrossDatatableJoinsAsCachedRowsetByMergeId(
			Integer mergeId, Connection connection) {

		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);
		
        Class<?> cachedRowSetImplClass = null;
		try {
			cachedRowSetImplClass = Class.forName("com.sun.rowset.CachedRowSetImpl");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		CachedRowSet keyCrossDatatableJoinsAsCachedRowSet = null;
		try {
			keyCrossDatatableJoinsAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `join` WHERE merge_id = ? AND `key` = TRUE AND datatable_1_column_name IS NOT NULL AND datatable_2_column_name IS NOT NULL;");
	          preparedStatement.setInt(1, mergeModel.getId());
	          preparedStatement.executeQuery();
	         
	          keyCrossDatatableJoinsAsCachedRowSet.populate(preparedStatement.getResultSet());
	          
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
		return keyCrossDatatableJoinsAsCachedRowSet;
	}


	public CachedRowSet retrieveNonKeyCrossDatatableJoinsAsCachedRowsetByMergeId(
			Integer mergeId, Connection connection) {

		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);
		
        Class<?> cachedRowSetImplClass = null;
		try {
			cachedRowSetImplClass = Class.forName("com.sun.rowset.CachedRowSetImpl");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		CachedRowSet nonKeyCrossDatatableJoinsAsCachedRowSet = null;
		try {
			nonKeyCrossDatatableJoinsAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `join` WHERE merge_id = ? AND `key` != TRUE AND datatable_1_column_name IS NOT NULL AND datatable_2_column_name IS NOT NULL;");
	          preparedStatement.setInt(1, mergeModel.getId());
	          preparedStatement.executeQuery();
	         
	          nonKeyCrossDatatableJoinsAsCachedRowSet.populate(preparedStatement.getResultSet());
	          
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
		return nonKeyCrossDatatableJoinsAsCachedRowSet;
	}


	public void setKeyCrossDatatableJoinsAsCachedRowSet(
			CachedRowSet keyCrossDatatableJoinsAsCachedRowsetByMergeId) {
		this.keyCrossDatatableJoinsAsCachedRowsetByMergeId = keyCrossDatatableJoinsAsCachedRowsetByMergeId;
	}


	public void setNonKeyCrossDatatableJoinsAsCachedRowSet(
			CachedRowSet nonKeyCrossDatatableJoinsAsCachedRowsetByMergeId) {
		this.nonKeyCrossDatatableJoinsAsCachedRowsetByMergeId = nonKeyCrossDatatableJoinsAsCachedRowsetByMergeId;
	}


	public CachedRowSet getKeyCrossDatatableJoinsAsCachedRowSet() {
		
		return this.keyCrossDatatableJoinsAsCachedRowsetByMergeId;
	}


	public CachedRowSet getNonKeyCrossDatatableJoinsAsCachedRowSet() {

		return this.nonKeyCrossDatatableJoinsAsCachedRowsetByMergeId;
	}











 
}
