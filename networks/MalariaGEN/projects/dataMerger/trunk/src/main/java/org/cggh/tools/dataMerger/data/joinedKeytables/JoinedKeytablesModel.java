package org.cggh.tools.dataMerger.data.joinedKeytables;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

import org.cggh.tools.dataMerger.data.merges.MergeModel;

public class JoinedKeytablesModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4036224766935438609L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.data.joinedKeytables");

	public JoinedKeytableModel retrieveJoinedKeytableAsJoinedKeytableModelUsingMergeId(
			Integer mergeId, Connection connection) {
		
		MergeModel mergeModel = new MergeModel();
		mergeModel.setId(mergeId);
		
		JoinedKeytableModel joinedKeytableModel = new JoinedKeytableModel();
		
	      try{
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT joined_keytable_name FROM `merge` WHERE id = ?;");
	          preparedStatement.setInt(1, mergeModel.getId());
	          preparedStatement.executeQuery();
	          ResultSet resultSet = preparedStatement.getResultSet();
	          
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();

	        	  //Set the joinedKeytable properties
	        	  joinedKeytableModel.setName(resultSet.getString("joined_keytable_name"));
	        	  
	        	  if (joinedKeytableModel.getName() != null) {
	        		  //Retrieve the datatable data
	        		  joinedKeytableModel.setDataAsCachedRowSet(this.retrieveDataAsCachedRowSetByJoinedKeytableName(joinedKeytableModel.getName(), connection));
	        	  }
	        	  
	      	  } else {
	      		  
	      		  //This is not necessarily an error, since might just be checking for existence.
	      		  //this.logger.info("Did not retrieve joined datatable with the specified merge Id.");
	      		  
	      		  
	      	  }
	          
	          resultSet.close();
	          
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
		
		return joinedKeytableModel;
	}

	public CachedRowSet retrieveDataAsCachedRowSetByJoinedKeytableName(String joinedKeytableName,
			Connection connection) {
		
		JoinedKeytableModel joinedKeytableModel = new JoinedKeytableModel();
		joinedKeytableModel.setName(joinedKeytableName);
		
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
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `" + joinedKeytableModel.getName() + "`;");
	          preparedStatement.executeQuery();
	         
	          dataAsCachedRowSet.populate(preparedStatement.getResultSet());
	          
	          preparedStatement.close();

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
		return dataAsCachedRowSet;
	}

	public JoinedKeytableModel retrieveJoinedKeytableAsJoinedKeytableModelThroughCreatingJoinedKeytableUsingMergeModel(
			MergeModel mergeModel, Connection connection) {


		JoinedKeytableModel joinedKeytableModel = new JoinedKeytableModel();

		joinedKeytableModel.setName("joined_keytable_" + mergeModel.getId());
		
		// See whether a table by this name already exists
		//TODO: This is a bit convoluted. Consider using something like DataModel.isTableExistingByName(name)
		JoinedKeytableModel testJoinedKeytableModel = this.retrieveJoinedKeytableAsJoinedKeytableModelUsingName(joinedKeytableModel.getName(), connection);
		
		if (testJoinedKeytableModel.getDataAsCachedRowSet() == null) {
		
			
	     try {
	    	 
	    	 
	    	 //FIXME: Should this only be done when a table is created successfully?
	    	 // Update the merge
	         PreparedStatement preparedStatement = connection.prepareStatement("UPDATE merge SET joined_keytable_name = ? WHERE id = ?;");
	         preparedStatement.setString(1, joinedKeytableModel.getName());
	         preparedStatement.setInt(2, mergeModel.getId());	          
	         preparedStatement.executeUpdate();
	         preparedStatement.close();
	         
	         

		 		CachedRowSet keyJoinsAsCachedRowSet = mergeModel.getJoinsModel().getKeyJoinsAsCachedRowSet();

	         
	         
		 		String keyColumnDefinitionsForCreateTableSQL = "id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, ";
		 		
				String joinForSelectIntoTableSQL = "";
				String orderForSelectIntoTableSQL = "";
				

		 		String keyColumnNamesAsCSVForInsertIntoJoinedKeytableSQL = "";
		        String datatable1KeyColumnNamesAsCSVForSelectFromDatatable1 = "";
				
				try {
					
					keyJoinsAsCachedRowSet.beforeFirst();
					
					if (keyJoinsAsCachedRowSet.next()) {
						
						keyJoinsAsCachedRowSet.first();
						
						
						keyColumnDefinitionsForCreateTableSQL = keyColumnDefinitionsForCreateTableSQL.concat("`key_column_" + keyJoinsAsCachedRowSet.getString("column_number") + "` VARCHAR(255) NOT NULL ");
						keyColumnNamesAsCSVForInsertIntoJoinedKeytableSQL = keyColumnNamesAsCSVForInsertIntoJoinedKeytableSQL.concat("`key_column_" + keyJoinsAsCachedRowSet.getString("column_number") + "` ");
						
						
						joinForSelectIntoTableSQL = joinForSelectIntoTableSQL.concat("`" + mergeModel.getDatatable1Model().getName() + "`.`" + keyJoinsAsCachedRowSet.getString("datatable_1_column_name") + "` = `" + mergeModel.getDatatable2Model().getName() + "`.`" + keyJoinsAsCachedRowSet.getString("datatable_2_column_name") + "` ");
						orderForSelectIntoTableSQL = orderForSelectIntoTableSQL.concat("`" + mergeModel.getDatatable1Model().getName() + "`.`" + keyJoinsAsCachedRowSet.getString("datatable_1_column_name") + "` ");
						datatable1KeyColumnNamesAsCSVForSelectFromDatatable1 = datatable1KeyColumnNamesAsCSVForSelectFromDatatable1.concat("`" + mergeModel.getDatatable1Model().getName() + "`.`" + keyJoinsAsCachedRowSet.getString("datatable_1_column_name") + "` ");
						
						while (keyJoinsAsCachedRowSet.next()) {
							
							keyColumnDefinitionsForCreateTableSQL = keyColumnDefinitionsForCreateTableSQL.concat(", `key_column_" + keyJoinsAsCachedRowSet.getString("column_number") + "` VARCHAR(255) NOT NULL ");
							keyColumnNamesAsCSVForInsertIntoJoinedKeytableSQL = keyColumnNamesAsCSVForInsertIntoJoinedKeytableSQL.concat(", `key_column_" + keyJoinsAsCachedRowSet.getString("column_number") + "` ");
							
							joinForSelectIntoTableSQL = joinForSelectIntoTableSQL.concat("AND `" + mergeModel.getDatatable1Model().getName() + "`.`" + keyJoinsAsCachedRowSet.getString("datatable_1_column_name") + "` = `" + mergeModel.getDatatable2Model().getName() + "`.`" + keyJoinsAsCachedRowSet.getString("datatable_2_column_name") + "` ");
							orderForSelectIntoTableSQL = orderForSelectIntoTableSQL.concat(", `" + mergeModel.getDatatable1Model().getName() + "`.`" + keyJoinsAsCachedRowSet.getString("datatable_1_column_name") + "` ");
							datatable1KeyColumnNamesAsCSVForSelectFromDatatable1 = datatable1KeyColumnNamesAsCSVForSelectFromDatatable1.concat(", `" + mergeModel.getDatatable1Model().getName() + "`.`" + keyJoinsAsCachedRowSet.getString("datatable_1_column_name") + "` ");
						}
						
						
						
					} else {
						
						this.logger.severe("Have not got any key joins for the merge.");
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}    
	         

    	    
		      try {
		    	  
		    	  // Create the table
		    	  // Note: Cannot reliably make the keys primary keys because there is  a max key length (3072 bytes)
		    	  String createJoinedKeytableSQL = "CREATE TABLE `" + joinedKeytableModel.getName() + "` (" + 
		    	  									keyColumnDefinitionsForCreateTableSQL + 
									        		  ", PRIMARY KEY (id) " + 
									        		  ") ENGINE=InnoDB;";
		    	  
		    	  //this.logger.info("createJoinedKeytableSQL=" + createJoinedKeytableSQL);
		    	  
		          Statement statement = connection.createStatement();
		          statement.executeUpdate(createJoinedKeytableSQL);
		          statement.close();

		          
		          //TODO
		          //FIXME
		         

	          
     	         // Load the data using the datatables and the joins
    		      try {
    		    	  
    		    	  String selectIntoTableSQL = "INSERT INTO `" + joinedKeytableModel.getName() + "` (" + keyColumnNamesAsCSVForInsertIntoJoinedKeytableSQL + ") " +
									      		  		"SELECT " + datatable1KeyColumnNamesAsCSVForSelectFromDatatable1 + " " +
									    		  		"FROM `" + mergeModel.getDatatable1Model().getName() + "` " +
									    		  		"JOIN `" + mergeModel.getDatatable2Model().getName() + "` " +
									    		  		"ON " + joinForSelectIntoTableSQL + 
									    		  		"ORDER BY " + orderForSelectIntoTableSQL + ";";
    		    	  
    		    	  //this.logger.info("selectIntoTableSQL=" + selectIntoTableSQL);
    		    	  
    		          PreparedStatement preparedStatement2 = connection.prepareStatement(selectIntoTableSQL);

    		          preparedStatement2.executeUpdate();
    		          preparedStatement2.close();
    		          
    		          
    		          //TODO: Retrieve?
    		          
    		        }
    		        catch(SQLException sqlException){
    			    	sqlException.printStackTrace();
    		        } 

		          
		          
		        }
		        catch(SQLException sqlException){
			    	sqlException.printStackTrace();
		        } 
	         
	
	       }
	       catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	       } 	


		} else {
			
			// Table already exists
			this.logger.severe("A joined datatable of the intended name has already been recorded.");
			
		}	       
	       
		
		return joinedKeytableModel;
	}

	public JoinedKeytableModel retrieveJoinedKeytableAsJoinedKeytableModelUsingName(
			String name, Connection connection) {
		
		JoinedKeytableModel joinedKeytableModel = new JoinedKeytableModel();
		
		joinedKeytableModel.setName(name);


	      try {
	          PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, joined_keytable_name FROM `merge` WHERE joined_keytable_name = ?;");
	          preparedStatement.setString(1, joinedKeytableModel.getName());				          
	          preparedStatement.executeQuery();

	          ResultSet resultSet = preparedStatement.getResultSet();

	          // There may be no such datatable.
	          if (resultSet.next()) {
	        	  
	        	  resultSet.first();
	        	  
	        	  MergeModel mergeModel = new MergeModel();
	        	  mergeModel.setId(resultSet.getInt("id"));

	        	  joinedKeytableModel = this.retrieveJoinedKeytableAsJoinedKeytableModelUsingMergeId(mergeModel.getId(), connection);

	          } else {
	        	  //this.logger.info("Did not find a joined datatable with the name: " + joinedKeytableModel.getName());
	          }

	          resultSet.close();
	          preparedStatement.close();
	          

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
		
		return joinedKeytableModel;
		
	}

	public void dropJoinedKeytableUsingName(String joinedKeytableName, Connection connection) {

		JoinedKeytableModel joinedKeytableModel = new JoinedKeytableModel();
		joinedKeytableModel.setName(joinedKeytableName);
		
		//TODO: Check security
		try {
	          PreparedStatement preparedStatement = connection.prepareStatement("DROP TABLE `" + joinedKeytableModel.getName() + "`;");				          
	          preparedStatement.executeUpdate();
	          preparedStatement.close();
	          

	        }
	        catch(SQLException sqlException){
		    	sqlException.printStackTrace();
	        } 
	}

}
