package org.cggh.tools.dataMerger.data.uploads;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class UploadsModel implements java.io.Serializable {

	
	public UploadsModel() {
		
		//TODO: Get all the uploads?
		
	}

	public UploadsModel(String user) {
		
		//TODO: Get the uploads for a user (need username and roles(rolenames) )
		
	}
	
   public ResultSet getUploadsAsResultSet() {

	   ResultSet uploadsAsResultSet = null;
	   
		try {
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			//TODO: Centralize db connections in config file.
			
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dataMerger", "root", "root");
			 
			if (!connection.isClosed()) {
		
				
			
				connection.close();
				
			} else {
				
				System.out.println("connection.isClosed");
			}
				
		} 
		catch (Exception e) {
			System.out.println("Failed to connect to the database.");
			e.printStackTrace();
		}



     return(uploadsAsResultSet);
   }
}
