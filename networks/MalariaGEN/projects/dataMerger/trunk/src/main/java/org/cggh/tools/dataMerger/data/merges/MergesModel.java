package org.cggh.tools.dataMerger.data.merges;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.CachedRowSet;


public class MergesModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1917678012559065867L;
	
	
	//TODO: Update this to use User and Data Models.
	private HttpServletRequest httpServletRequest;
	private MergeModel currentMerge;
	
	public MergesModel() {
		
		this.setCurrentMerge(new MergeModel());
	}



    public void setHttpServletRequest (final HttpServletRequest  httpServletRequest ) {
        this.httpServletRequest  = httpServletRequest;
    }
    public HttpServletRequest getHttpServletRequest () {
        return this.httpServletRequest;
    }    

	public void setCurrentMerge (final MergeModel merge) {
		
		this.currentMerge = merge;
	}
	public MergeModel getCurrentMerge () {
		
		return this.currentMerge;
	}	    
    

    public Integer createMerge (Integer upload1Id, Integer upload2Id) {
    	
    	this.currentMerge.setUpload1Id(upload1Id);
    	this.currentMerge.setUpload2Id(upload2Id);
    	
	   ServletContext servletContext = this.getHttpServletRequest().getSession().getServletContext();
	   
		try {
			
			//TODO: Upgrade this to use Data Model.
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection connection = DriverManager.getConnection(servletContext.getInitParameter("dbBasePath") + servletContext.getInitParameter("dbName"), servletContext.getInitParameter("dbUsername"), servletContext.getInitParameter("dbPassword"));
			 
			if (!connection.isClosed()) {
		
				//TODO: Upgrade this to use User Model.
				// Get the user_id
				Integer user_id = null;
				
			      try{
			          PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM user WHERE username = ?;");
			          preparedStatement.setString(1, this.getHttpServletRequest().getRemoteUser());
			          preparedStatement.executeQuery();
			          ResultSet resultSet = preparedStatement.getResultSet();

			          // There may be no user in the user table.
			          if (resultSet.next()) {
			        	  resultSet.first();
			        	  user_id = resultSet.getInt("id");
			          } else {
			        	  System.out.println("Did not find user in user table. This user is not registered. Db query gives !resultSet.next()");
			          }

			          resultSet.close();
			          preparedStatement.close();
			          
			        }
			        catch(SQLException sqlException){
			        	System.out.println("<p>" + sqlException + "</p>");
				    	sqlException.printStackTrace();
			        } 				
				
			      try {
			          PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO merge (upload_1_id, upload_2_id, created_by_user_id, created_datetime, updated_datetime) VALUES (?, ?, ?, NOW(), NOW());");
			          preparedStatement.setInt(1, this.currentMerge.getUpload1Id());
			          preparedStatement.setInt(2, this.currentMerge.getUpload2Id());
			          //TODO: update to use UserModel.
			          preparedStatement.setInt(3, user_id);				          
			          preparedStatement.executeUpdate();
			          preparedStatement.close();
	
			        }
			        catch(SQLException sqlException){
				    	sqlException.printStackTrace();
			        } 			        

			      try{
			    	  //TODO: Is this cross-db compatible? ref: @@IDENTITY
			          PreparedStatement preparedStatement = connection.prepareStatement("SELECT LAST_INSERT_ID();");
			          preparedStatement.executeQuery();
			          
			          ResultSet resultSet = preparedStatement.getResultSet();
			          if (resultSet.next()) {
			        	  
			        	  resultSet.first();
			        	  this.currentMerge.setId(resultSet.getInt(1));
			        	  
			          } else {
			        	  
			        	  
			        	  System.out.println("Unexpected: !resultSet.next()");
			          }
			          
			          preparedStatement.close();

			        }
			        catch(SQLException sqlException){
				    	sqlException.printStackTrace();
			        }					        
				        
			        //TODO: see if the datatables have already been loaded in.
			        
					// Get the datatable_for_upload_1_id
					Integer datatable_id_for_upload_1_id = null;
					
				      try{
				          PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM datatable WHERE upload_id = ?;");
				          preparedStatement.setInt(1, this.currentMerge.getUpload1Id());
				          preparedStatement.executeQuery();
				          ResultSet resultSet = preparedStatement.getResultSet();

				          // There may be no user in the user table.
				          if (resultSet.next()) {
				        	  resultSet.first();
				        	  datatable_id_for_upload_1_id = resultSet.getInt("id");
				          } else {
				        	  System.out.println("Did not find upload in datatable table. This upload is not imported. Db query gives !resultSet.next()");
				          }

				          resultSet.close();
				          preparedStatement.close();
				          
				        }
				        catch(SQLException sqlException){
				        	System.out.println("<p>" + sqlException + "</p>");
					    	sqlException.printStackTrace();
				        } 					        

						// Get the datatable_for_upload_2_id
						Integer datatable_id_for_upload_2_id = null;
						
					      try{
					          PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM datatable WHERE upload_id = ?;");
					          preparedStatement.setInt(1, this.currentMerge.getUpload2Id());
					          preparedStatement.executeQuery();
					          ResultSet resultSet = preparedStatement.getResultSet();

					          // There may be no user in the user table.
					          if (resultSet.next()) {
					        	  resultSet.first();
					        	  datatable_id_for_upload_2_id = resultSet.getInt("id");
					          } else {
					        	  System.out.println("Did not find upload in datatable table. This upload is not imported. Db query gives !resultSet.next()");
					          }

					          resultSet.close();
					          preparedStatement.close();
					          
					        }
					        catch(SQLException sqlException){
					        	System.out.println("<p>" + sqlException + "</p>");
						    	sqlException.printStackTrace();
					        } 					        
					        
				        //TODO: load the data into tables
					       //TODO: Merge these identical twin branches.
					    if (datatable_id_for_upload_1_id != null) {
					    	
					    		//TODO: Create the datatable record.
					    	
					    		//TODO: Load the datatable 
						      try {
						          PreparedStatement preparedStatement = connection.prepareStatement("LOAD DATA INFILE ? INTO TABLE `datatable_" + this.currentMerge.getDatatable1Model().getId().toString() + "`;");
						          preparedStatement.setString(1, this.currentMerge.getUpload1Model().getRepositoryFilepath());
						          preparedStatement.executeUpdate();
						          preparedStatement.close();
				
						        }
						        catch(SQLException sqlException){
							    	sqlException.printStackTrace();
						        } 
					    }
					    if (datatable_id_for_upload_2_id != null) {
					    	
				    		//TODO: Create the datatable record.
				    	
				    		//TODO: Load the datatable 
					      try {
					          PreparedStatement preparedStatement = connection.prepareStatement("LOAD DATA INFILE ? INTO TABLE `datatable_" + this.currentMerge.getDatatable2Model().getId().toString() + "`;");
					          preparedStatement.setString(1, this.currentMerge.getUpload2Model().getRepositoryFilepath());
					          preparedStatement.executeUpdate();
					          preparedStatement.close();
			
					        }
					        catch(SQLException sqlException){
						    	sqlException.printStackTrace();
					        } 
				    }
						
						        
			
				connection.close();
				
			} else {
				
				System.out.println("connection.isClosed");
			}
				
		} 
		catch (Exception e) {
			System.out.println("Exception from createMerge.");
			e.printStackTrace();
		}
    	
		
		return this.currentMerge.getId();
    }
    
   public CachedRowSet getMergesAsCachedRowSet() {

	   ServletContext servletContext = this.getHttpServletRequest().getSession().getServletContext();
	   
	   String CACHED_ROW_SET_IMPL_CLASS = "com.sun.rowset.CachedRowSetImpl";
	   
	   CachedRowSet mergesAsCachedRowSet = null;
	   
		try {
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection connection = DriverManager.getConnection(servletContext.getInitParameter("dbBasePath") + servletContext.getInitParameter("dbName"), servletContext.getInitParameter("dbUsername"), servletContext.getInitParameter("dbPassword"));
			 
			if (!connection.isClosed()) {
		
				// Get the user_id
				Integer user_id = null;
				
			      try{
			          PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM user WHERE username = ?;");
			          preparedStatement.setString(1, this.getHttpServletRequest().getRemoteUser());
			          preparedStatement.executeQuery();
			          ResultSet resultSet = preparedStatement.getResultSet();

			          // There may be no user in the user table.
			          if (resultSet.next()) {
			        	  resultSet.first();
			        	  user_id = resultSet.getInt("id");
			          } else {
			        	  System.out.println("Did not find user in user table. This user is not registered. Db query gives !resultSet.next()");
			          }

			          resultSet.close();
			          preparedStatement.close();
			          
			        }
			        catch(SQLException sqlException){
			        	System.out.println("<p>" + sqlException + "</p>");
				    	sqlException.printStackTrace();
			        } 				
				
			      try{
			          PreparedStatement preparedStatement = connection.prepareStatement("SELECT merge.id, upload_1.original_filename, upload_2.original_filename, created_datetime, updated_datetime FROM upload INNER JOIN upload AS upload_1 ON upload_1.id = merge.upload_1_id INNER JOIN upload AS upload_2 ON upload_2.id = merge.upload_2_id WHERE created_by_user_id = ?;");
			          preparedStatement.setInt(1, user_id);
			          preparedStatement.executeQuery();
			          Class<?> cachedRowSetImplClass = Class.forName(CACHED_ROW_SET_IMPL_CLASS);
			          mergesAsCachedRowSet = (CachedRowSet) cachedRowSetImplClass.newInstance();
			          mergesAsCachedRowSet.populate(preparedStatement.getResultSet());
			          preparedStatement.close();

			        }
			        catch(SQLException sqlException){
			        	System.out.println("<p>" + sqlException + "</p>");
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



     return(mergesAsCachedRowSet);
   }
}
