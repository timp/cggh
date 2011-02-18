package org.cggh.tools.dataMerger.data.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.cggh.tools.dataMerger.data.DataModel;

public class UserModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 60066823075291273L;
	private DataModel dataModel;
	
	private Integer id = null;
	private String username = null;
	
	private Boolean registered = null;
	
	public UserModel() {
		this.setDataModel(new DataModel());
	}

	public void setUserModelByUsername (final String username) {
		
		this.setUsername(username);
		
		try {
			
			Connection connection = this.getDataModel().getConnection();
			 
			if (!connection.isClosed()) {

			      try{
			          PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM user WHERE username = ?;");
			          preparedStatement.setString(1, this.getUsername());
			          preparedStatement.executeQuery();
			          ResultSet resultSet = preparedStatement.getResultSet();

			          // There may be no user in the user table.
			          if (resultSet.next()) {
			        	  
			        	  resultSet.first();
			        	  this.setRegistered(true);
			        	  this.setId(resultSet.getInt("id"));
			        	  
			          } else {
			        	  //TODO: proper logging and error handling
			        	  System.out.println("Did not find user in user table. This user is not registered. Db query gives !resultSet.next()");
			          }

			          resultSet.close();
			          preparedStatement.close();
			          
			        }
			        catch(SQLException sqlException){
			        	System.out.println(sqlException);
				    	sqlException.printStackTrace();
			        } 				
				
			
				connection.close();
				
			} else {
				
				System.out.println("connection.isClosed");
			}
				
		} 
		catch (Exception e) {
			System.out.println("Exception from setUserModelByUsername.");
			e.printStackTrace();
		}
		
	}

    public void setDataModel (final DataModel dataModel) {
        this.dataModel  = dataModel;
    }
    public DataModel getDataModel () {
        return this.dataModel;
    } 	

    
	public void setId (final Integer id) {
		this.id = id;
	}
	public Integer getId () {
		return this.id;
	}    
    
	public void setUsername (final String username) {
		this.username = username;
	}
	public String getUsername () {
		return this.username;
	}	
	
	public boolean isRegistered () {
		return this.registered;
	}

	public void setRegistered (final Boolean registered) {
		
		this.registered = registered;
	}
	
}