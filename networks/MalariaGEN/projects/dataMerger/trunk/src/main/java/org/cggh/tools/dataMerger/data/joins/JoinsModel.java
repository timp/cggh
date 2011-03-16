package org.cggh.tools.dataMerger.data.joins;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.DataModel;
import org.cggh.tools.dataMerger.data.merges.MergeModel;
import org.cggh.tools.dataMerger.data.merges.MergesModel;
import org.cggh.tools.dataMerger.scripts.merges.MergeScriptsModel;
import org.json.JSONArray;
import org.json.JSONObject;


public class JoinsModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2278497615197327793L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.joins");
	
	private Integer nextColumnNumber;
	private CachedRowSet joinsAsCachedRowSet;
	private Integer keysCount;
	private CachedRowSet crossDatatableJoinsAsCachedRowSet;
	private DataModel dataModel;

	private CachedRowSet keyJoinsAsCachedRowset;
	private CachedRowSet nonKeyCrossDatatableJoinsAsCachedRowset;
	private CachedRowSet nonCrossDatatableJoinsAsCachedRowset;

	//Must not have a joinModel, because joinModel has a mergeModel, which has a joinsModel, causes StackOverflowError
	//private JoinModel joinModel;
	
	
	public JoinsModel() {
		
		
		//this.setDataModel(new DataModel());
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
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `join` WHERE merge_id = ? ORDER BY column_number;");
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





	public JoinsModel retrieveJoinsAsJoinsModelByMergeId(Integer mergeId,
			Connection connection) {

		JoinsModel joinsModel = new JoinsModel();
		
		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);

		
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT merge_id, column_number, `key`, datatable_1_column_name, datatable_2_column_name, constant_1, constant_2, column_name FROM `join` " + 
	        	"WHERE merge_id = ? ORDER BY column_number;");
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
			        		  "AND (datatable_1_column_name IS NOT NULL AND datatable_2_column_name IS NOT NULL) " +
			        		  "ORDER BY column_number;");
			          preparedStatement3.setInt(1, mergeModel.getId());
			          preparedStatement3.executeQuery();
			          ResultSet resultSet3 = preparedStatement3.getResultSet();
			          
			          CachedRowSet crossDatatableJoinsAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
			          crossDatatableJoinsAsCachedRowSet.populate(preparedStatement3.getResultSet());
			          
			          resultSet3.close();
			          preparedStatement3.close();

			          joinsModel.setCrossDatatableJoinsAsCachedRowSet(crossDatatableJoinsAsCachedRowSet);
			          
			          
			          //FIXME: I think the naming is causing some architectural non-sense here.
			          
			          joinsModel.setNonKeyCrossDatatableJoinsAsCachedRowSet(this.retrieveNonKeyCrossDatatableJoinsAsCachedRowsetByMergeId(mergeModel.getId(), connection));
			          joinsModel.setKeyJoinsAsCachedRowSet(this.retrieveKeyJoinsAsCachedRowsetByMergeId(mergeModel.getId(), connection));
			          
			          
		          
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
		          
		          
		          //Need a complete, up-to-date mergeModel
		          MergesModel mergesModel = new MergesModel();
		          mergeModel = mergesModel.retrieveMergeAsMergeModelByMergeId(mergeModel.getId(), connection);
		          
		          //Recount the duplicate keys
		          mergeModel = mergesModel.retrieveMergeAsMergeModelThroughCountingDuplicateKeysUsingMergeModel(mergeModel, connection);
		          
		          //FIXME
		          //Recount the data conflicts (should also re-create the joined_keytable)
		          MergeScriptsModel mergeScriptsModel = new MergeScriptsModel();
		          mergeModel = mergeScriptsModel.retrieveMergeAsMergeModelThroughDeterminingDataConflictsUsingMergeModel(mergeModel, connection);
		          
					
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


	public CachedRowSet retrieveKeyJoinsAsCachedRowsetByMergeId(
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
		
		CachedRowSet keyJoinsAsCachedRowSet = null;
		try {
			keyJoinsAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `join` WHERE merge_id = ? AND `key` = TRUE AND datatable_1_column_name IS NOT NULL AND datatable_2_column_name IS NOT NULL ORDER BY column_number;");
	          preparedStatement.setInt(1, mergeModel.getId());
	          preparedStatement.executeQuery();
	         
	          keyJoinsAsCachedRowSet.populate(preparedStatement.getResultSet());
	          
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
		return keyJoinsAsCachedRowSet;
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
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `join` WHERE merge_id = ? AND `key` != TRUE AND datatable_1_column_name IS NOT NULL AND datatable_2_column_name IS NOT NULL ORDER BY column_number;");
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


	public void setKeyJoinsAsCachedRowSet(
			CachedRowSet keyJoinsAsCachedRowset) {
		this.keyJoinsAsCachedRowset = keyJoinsAsCachedRowset;
	}


	public void setNonKeyCrossDatatableJoinsAsCachedRowSet(
			CachedRowSet nonKeyCrossDatatableJoinsAsCachedRowset) {
		this.nonKeyCrossDatatableJoinsAsCachedRowset = nonKeyCrossDatatableJoinsAsCachedRowset;
	}


	public CachedRowSet getKeyJoinsAsCachedRowSet() {
		
		return this.keyJoinsAsCachedRowset;
	}


	public CachedRowSet getNonKeyCrossDatatableJoinsAsCachedRowSet() {

		return this.nonKeyCrossDatatableJoinsAsCachedRowset;
	}


	public List<String> retrieveDatatable1KeyColumnNamesAsStringListByMergeId(
			Integer mergeId, Connection connection) {
		
		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);
		
		List<String> datatable1KeyColumnNamesAsStringList = new ArrayList<String>();
		
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT datatable_1_column_name FROM `join` WHERE `key` = true AND merge_id = ? ORDER BY column_number;");
	          preparedStatement.setInt(1, mergeModel.getId());
	          preparedStatement.executeQuery();
	          ResultSet resultSet = preparedStatement.getResultSet();
	          
	          if (resultSet.next()) {
	        	  
	        	  resultSet.beforeFirst();
	        	  
	        	  while(resultSet.next()){

	        		  datatable1KeyColumnNamesAsStringList.add(resultSet.getString("datatable_1_column_name"));
	        	  
	        	  }

	      	  } else {
	      		  
	      		  //This is not necessarily an error, because there may be no keys specified in the join.
	      		  this.logger.info("Did not retrieve any key datatable 1 column names for the specified merge.");
	      		  
	      	  }
	          
	          resultSet.close();
	          
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
	        
		return datatable1KeyColumnNamesAsStringList;
	}

	//TODO: Refactor, merge this with the datatable 1 query.
	public List<String> retrieveDatatable2KeyColumnNamesAsStringListByMergeId(
			Integer mergeId, Connection connection) {
		
		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);
		
		List<String> datatable2KeyColumnNamesAsStringList = new ArrayList<String>();
		
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT datatable_2_column_name FROM `join` WHERE `key` = true AND merge_id = ? ORDER BY column_number;");
	          preparedStatement.setInt(1, mergeModel.getId());
	          preparedStatement.executeQuery();
	          ResultSet resultSet = preparedStatement.getResultSet();
	          
	          if (resultSet.next()) {
	        	  
	        	  resultSet.beforeFirst();
	        	  
	        	  while(resultSet.next()){

	        		  datatable2KeyColumnNamesAsStringList.add(resultSet.getString("datatable_2_column_name"));
	        	  
	        	  }

	      	  } else {
	      		  
	      		  //This is not necessarily an error, because there may be no keys specified in the join.
	      		  this.logger.info("Did not retrieve any key datatable 2 column names for the specified merge.");
	      		  
	      	  }
	          
	          resultSet.close();
	          
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
	        
		return datatable2KeyColumnNamesAsStringList;
	}


	//TODO: Combine these somehow
	public HashMap<Integer, String> retrieveDatatable1ColumnNamesByColumnNumberAsHashMapUsingMergeId(
			Integer mergeId, Connection connection) {
		
		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);

		HashMap<Integer, String> datatable1ColumnNamesByColumnNumberAsHashMap = new HashMap<Integer, String>();
		
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT column_number, datatable_1_column_name FROM `join` WHERE merge_id = ? ORDER BY column_number;");
	          preparedStatement.setInt(1, mergeModel.getId());
	          preparedStatement.executeQuery();
	          ResultSet resultSet = preparedStatement.getResultSet();
	          
	          if (resultSet.next()) {
	        	  
	        	  resultSet.beforeFirst();
	        	  
	        	  while(resultSet.next()){

	        		  datatable1ColumnNamesByColumnNumberAsHashMap.put(resultSet.getInt("column_number"), resultSet.getString("datatable_1_column_name"));
	        	  
	        	  }

	      	  } else {
	      		  
	      		  this.logger.severe("Did not retrieve any datatable 1 column names for the specified merge.");
	      		  
	      	  }
	          
	          resultSet.close();
	          
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
		return datatable1ColumnNamesByColumnNumberAsHashMap;
	}


	public HashMap<Integer, String> retrieveDatatable2ColumnNamesByColumnNumberAsHashMapUsingMergeId(
			Integer mergeId, Connection connection) {

		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);

		HashMap<Integer, String> datatable2ColumnNamesByColumnNumberAsHashMap = new HashMap<Integer, String>();
				
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT column_number, datatable_2_column_name FROM `join` WHERE merge_id = ? ORDER BY column_number;");
	          preparedStatement.setInt(1, mergeModel.getId());
	          preparedStatement.executeQuery();
	          ResultSet resultSet = preparedStatement.getResultSet();
	          
	          if (resultSet.next()) {
	        	  
	        	  resultSet.beforeFirst();
	        	  
	        	  while(resultSet.next()){

	        		  datatable2ColumnNamesByColumnNumberAsHashMap.put(resultSet.getInt("column_number"), resultSet.getString("datatable_2_column_name"));
	        	  
	        	  }

	      	  } else {
	      		  
	      		  this.logger.severe("Did not retrieve any datatable 2 column names for the specified merge.");
	      		  
	      	  }
	          
	          resultSet.close();
	          
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
		return datatable2ColumnNamesByColumnNumberAsHashMap;
	}


	public HashMap<Integer, String> retrieveJoinColumnNamesByColumnNumberAsHashMapUsingMergeModel(
			MergeModel mergeModel) {

		HashMap<Integer, String> joinColumnNamesByColumnNumberAsHashMap = new HashMap<Integer, String>();

		try {
			
			Connection connection = this.getDataModel().getNewConnection();
			 
			if (!connection.isClosed()) {		
		
				joinColumnNamesByColumnNumberAsHashMap = retrieveJoinColumnNamesByColumnNumberAsHashMapUsingMergeModel(mergeModel, connection);
	        
			} else {
				
				this.logger.severe("connection.isClosed");
			}
				
		} 
		catch (Exception e) {
			
			this.logger.severe(e.getMessage());
			e.printStackTrace();
		}
		
		return joinColumnNamesByColumnNumberAsHashMap;
	}

	public HashMap<Integer, String> retrieveJoinColumnNamesByColumnNumberAsHashMapUsingMergeModel(
			MergeModel mergeModel, Connection connection) {
		
		HashMap<Integer, String> joinColumnNamesByColumnNumberAsHashMap = new HashMap<Integer, String>();

	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT column_number, column_name FROM `join` WHERE merge_id = ? ORDER BY column_number;");
	          preparedStatement.setInt(1, mergeModel.getId());
	          preparedStatement.executeQuery();
	          ResultSet resultSet = preparedStatement.getResultSet();
	          
	          if (resultSet.next()) {
	        	  
	        	  resultSet.beforeFirst();
	        	  
	        	  while(resultSet.next()){

	        		  joinColumnNamesByColumnNumberAsHashMap.put(resultSet.getInt("column_number"), resultSet.getString("column_name"));
	        	  
	        	  }

	      	  } else {
	      		  
	      		  this.logger.severe("Did not retrieve any column names for the specified merge id: " + mergeModel.getId());
	      		  
	      	  }
	          
	          resultSet.close();
	          
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 	
		
		return joinColumnNamesByColumnNumberAsHashMap;
	}
	

	public CachedRowSet retrieveNonCrossDatatableJoinsAsCachedRowsetByMergeId(
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
		
		CachedRowSet nonCrossDatatableJoinsAsCachedRowSet = null;
		try {
			nonCrossDatatableJoinsAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `join` WHERE merge_id = ? AND (datatable_1_column_name IS NULL OR datatable_2_column_name IS NULL) ORDER BY column_number;");
	          preparedStatement.setInt(1, mergeModel.getId());
	          preparedStatement.executeQuery();
	         
	          nonCrossDatatableJoinsAsCachedRowSet.populate(preparedStatement.getResultSet());
	          
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
		return nonCrossDatatableJoinsAsCachedRowSet;
	}


	public void setNonCrossDatatableJoinsAsCachedRowSet(
			CachedRowSet nonCrossDatatableJoinsAsCachedRowset) {
		this.nonCrossDatatableJoinsAsCachedRowset = nonCrossDatatableJoinsAsCachedRowset;
	}


	public CachedRowSet getNonCrossDatatableJoinsAsCachedRowSet() {
		return this.nonCrossDatatableJoinsAsCachedRowset;
	}











 
}
