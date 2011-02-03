package org.cggh.tools.dataMerger.data.uploads.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class UploadsModel {

	
	public UploadsModel() {
	}


	
   public ResultSet getUploadsAsResultSet() {

	   ResultSet uploadsASResultSet = null;
	   
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



     return(uploadsASResultSet);
   }
}
