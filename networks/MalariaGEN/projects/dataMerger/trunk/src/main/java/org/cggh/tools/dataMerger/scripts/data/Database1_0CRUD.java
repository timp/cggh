package org.cggh.tools.dataMerger.scripts.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import org.cggh.tools.dataMerger.data.DataModel;
import org.cggh.tools.dataMerger.data.databases.DatabasesCRUD;
import org.cggh.tools.dataMerger.data.tables.TablesCRUD;
import org.cggh.tools.dataMerger.data.users.UserModel;

public class Database1_0CRUD {
	
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.scripts.data");
	
	private DataModel dataModel = null;
	private UserModel userModel = null;
	
	public Database1_0CRUD () {
		
		this.setDataModel(new DataModel());
		this.setUserModel(new UserModel());
		
	}
	
	public Boolean create () {
		
		//TODO: No longer create database here (done by POST to databasesController)
		
		Connection databaseServerConnection = this.getDataModel().getNewDatabaseServerConnection();
		
		if (databaseServerConnection != null) {
			
			DatabasesCRUD databasesCRUD = new DatabasesCRUD();
			databasesCRUD.setDataModel(this.getDataModel());
			databasesCRUD.setUserModel(this.getUserModel());
			
			databasesCRUD.createDatabase(databaseServerConnection);
			
			try {
				databaseServerConnection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			
			this.getDataModel().setDatabaseConnectableUsingDataModel();
			
			if (this.getDataModel().isDatabaseConnectable()) {
				
				Connection connection = this.getDataModel().getNewDatabaseConnection();
				
				TablesCRUD tablesCRUD = new TablesCRUD();
				tablesCRUD.setDataModel(this.getDataModel());
				tablesCRUD.setUserModel(this.getUserModel());
				
				//TODO: Objectify table creation. Perhaps in v2.
				//tablesCRUD.createTable
				///////////////////////////////////////////
				
			        
			        
			        
			        
			      try{
			          Statement statement = connection.createStatement();
			          statement.executeUpdate("CREATE TABLE user (id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, username VARCHAR(255) NOT NULL, PRIMARY KEY (id), CONSTRAINT unique_username_constraint UNIQUE (username)) ENGINE=InnoDB;");
			          statement.close();

			        }
			        catch(SQLException sqlException){
				    	sqlException.printStackTrace();
				    	return false;
			        }	
			        
			        
				      try{
				    	  
				          Statement statement = connection.createStatement();
				          statement.executeUpdate("INSERT INTO `user` (" +
				        		  "username " +
				        		  ") VALUES ('" + this.getUserModel().getUsername() + "';");
				          statement.close();
	
				        }
				        catch(SQLException sqlException){
				        	
					    	sqlException.printStackTrace();
					    	return false;
				        }				        
			        
			        
				        this.getUserModel().setId(1);
				        
			        

				      try{
				          Statement statement = connection.createStatement();
				          statement.executeUpdate("CREATE TABLE `installation` (" +  
				        		  "id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
				        		  "major_version_number BIGINT(255) UNSIGNED NOT NULL, " +
				        		  "minor_version_number BIGINT(255) UNSIGNED NOT NULL, " +
				        		  "revision_version_number BIGINT(255) UNSIGNED NOT NULL, " + 
				        		  "created_by_user_id BIGINT(255) UNSIGNED NOT NULL, " + 
				        		  "created_datetime DATETIME NOT NULL " +
				        		  ") ENGINE=InnoDB;");
				          statement.close();

				        }
				        catch(SQLException sqlException){
				        	
					    	sqlException.printStackTrace();
					    	return false;
				        } 				

				        
				        
					      try{
					    	  
					          Statement statement = connection.createStatement();
					          statement.executeUpdate("INSERT INTO `installation` (" +
					        		  "major_version_number, minor_version_number, revision_version_number, created_by_user_id, created_datetime " +
					        		  ") VALUES (1, 0, 0, " + this.getUserModel().getId() + ", NOW());");
					          statement.close();
		
					        }
					        catch(SQLException sqlException){
					        	
						    	sqlException.printStackTrace();
						    	return false;
					        }			        
			        
			        
			      try{
			          Statement statement = connection.createStatement();
			          statement.executeUpdate("CREATE TABLE upload (" + 
			        		  "id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
			        		  "original_filename VARCHAR(255) NOT NULL, " + 
			        		  "repository_filepath VARCHAR(255) NULL, " + 
			        		  "successful BOOLEAN NULL, " + 
			        		  "created_by_user_id BIGINT(255) UNSIGNED NOT NULL, " + 
			        		  "created_datetime DATETIME NOT NULL, " +
			        		  "datatable_name VARCHAR(255) NULL, " + 
			        		  "PRIMARY KEY (id), " +
			        		  "CONSTRAINT unique_path_constraint UNIQUE (repository_filepath), " +
			        		  "CONSTRAINT unique_datatable_name_constraint UNIQUE (datatable_name), " +
			        		  "INDEX created_by_user_id_index (created_by_user_id), " + 
			        		  "FOREIGN KEY (created_by_user_id) REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE " + 
			        		  ") ENGINE=InnoDB;");
			          statement.close();

			        }
			        catch(SQLException sqlException){
			        	
				    	sqlException.printStackTrace();
				    	return false;
			        } 				
				
				
			        
				      try{
				          Statement statement = connection.createStatement();
				          statement.executeUpdate("CREATE TABLE merge (" + 
				        		  "id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
				        		  "upload_1_id BIGINT(255) UNSIGNED NOT NULL, " + 
				        		  "upload_2_id BIGINT(255) UNSIGNED NOT NULL, " + 
				        		  "created_by_user_id BIGINT(255) UNSIGNED NOT NULL, " + 
				        		  "created_datetime DATETIME NOT NULL, " +
				        		  "updated_datetime DATETIME NOT NULL, " +
				        		  "datatable_1_duplicate_keys_count BIGINT(255) UNSIGNED NULL, " + 
				        		  "datatable_2_duplicate_keys_count BIGINT(255) UNSIGNED NULL, " + 
				        		  "total_duplicate_keys_count BIGINT(255) UNSIGNED NULL, " + 
				        		  "total_conflicts_count BIGINT(255) UNSIGNED NULL, " +
				        		  "joined_keytable_name VARCHAR(255) NULL, " + 
				        		  "joined_datatable_name VARCHAR(255) NULL, " + 
				        		  "PRIMARY KEY (id), " +
				        		  "CONSTRAINT unique_joined_keytable_name_constraint UNIQUE (joined_keytable_name), " +
				        		  "INDEX created_by_user_id_index (created_by_user_id), " + 
				        		  "FOREIGN KEY (created_by_user_id) REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE, " +
				        		  "INDEX upload_1_id_index (upload_1_id), " + 
				        		  "FOREIGN KEY (upload_1_id) REFERENCES upload(id) ON DELETE CASCADE ON UPDATE CASCADE, " +
				        		  "INDEX upload_2_id_index (upload_2_id), " + 
				        		  "FOREIGN KEY (upload_2_id) REFERENCES upload(id) ON DELETE CASCADE ON UPDATE CASCADE " + 
				        		  ") ENGINE=InnoDB;");
				          statement.close();
		
				        }
				        catch(SQLException sqlException){
				        	
					    	sqlException.printStackTrace();
					    	return false;
				        } 
			    
			        
			        
				      try{
				    	  
				    	  //Note: (255) in BIGINT(255) is only the display width, not the capacity.
				    	  
				          Statement statement = connection.createStatement();
				          statement.executeUpdate("CREATE TABLE `join` (" + 
				        		  "merge_id BIGINT(255) UNSIGNED NOT NULL, " + 
				        		  "column_number INT(255) UNSIGNED NOT NULL, " +
				        		  "`key` BOOLEAN NULL, " + 
				        		  "datatable_1_column_name VARCHAR(255) NULL, " +
				        		  "datatable_2_column_name VARCHAR(255) NULL, " +
				        		  "constant_1 VARCHAR(255) NULL, " +
				        		  "constant_2 VARCHAR(255) NULL, " +
				        		  "column_name VARCHAR(255) NULL, " +
				        		  "PRIMARY KEY (merge_id, column_number), " +
				        		  "INDEX merge_id_index (merge_id), " + 
				        		  "FOREIGN KEY (merge_id) REFERENCES merge(id) ON DELETE CASCADE ON UPDATE CASCADE " +
				        		  ") ENGINE=InnoDB;");
				          statement.close();

				        }
				        catch(SQLException sqlException){
				        	
					    	sqlException.printStackTrace();
					    	return false;
				        } 

					        
					      try{
					    	  
					          Statement statement = connection.createStatement();
					          statement.executeUpdate("CREATE TABLE `solution_by_column` (" + 
					        		  "id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
					        		  "description VARCHAR(255) NOT NULL, " +
					        		  "PRIMARY KEY (id) " +
					        		  ") ENGINE=InnoDB;");
					          statement.close();
		
					        }
					        catch(SQLException sqlException){
					        	
						    	sqlException.printStackTrace();
						    	return false;
					        } 
				        
					      try{
					    	  
					          Statement statement = connection.createStatement();
					          statement.executeUpdate("INSERT INTO `solution_by_column` (" +
					        		  "description " +
					        		  ") VALUES ('Prefer source 1'), ('Prefer source 2'), ('Use NULL'), ('Use CONSTANT');");
					          statement.close();
		
					        }
					        catch(SQLException sqlException){
					        	
						    	sqlException.printStackTrace();
						    	return false;
					        }						        
						   
				

					try{
			    	  
			          Statement statement = connection.createStatement();
			          statement.executeUpdate("CREATE TABLE `solution_by_row` (" + 
			        		  "id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
			        		  "description VARCHAR(255) NOT NULL, " +
			        		  "PRIMARY KEY (id) " +
			        		  ") ENGINE=InnoDB;");
			          statement.close();

			        }
			        catch(SQLException sqlException){
			        	
				    	sqlException.printStackTrace();
				    	return false;
			        } 
		        
			        try{
			    	  
			          Statement statement = connection.createStatement();
			          statement.executeUpdate("INSERT INTO `solution_by_row` (" +
			        		  "description " +
			        		  ") VALUES ('Prefer source 1'), ('Prefer source 2'), ('Use NULL'), ('Use CONSTANT'), ('Remove entire row');");
			          statement.close();

			        }
			        catch(SQLException sqlException){
			        	
				    	sqlException.printStackTrace();
				    	return false;
			        }
					   


			        
			        
				      try{
				    	  
				          Statement statement = connection.createStatement();
				          statement.executeUpdate("CREATE TABLE `solution_by_cell` (" + 
				        		  "id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
				        		  "description VARCHAR(255) NOT NULL, " +
				        		  "PRIMARY KEY (id) " +
				        		  ") ENGINE=InnoDB;");
				          statement.close();

				        }
				        catch(SQLException sqlException){
				        	
					    	sqlException.printStackTrace();
					    	return false;
				        } 
			        
				      try{
				    	  
				          Statement statement = connection.createStatement();
				          statement.executeUpdate("INSERT INTO `solution_by_cell` (" +
				        		  "description " +
				        		  ") VALUES ('Prefer source 1'), ('Prefer source 2'), ('Use NULL'), ('Use CONSTANT');");
				          statement.close();

				        }
				        catch(SQLException sqlException){
				        	
					    	sqlException.printStackTrace();
					    	return false;
				        }
			        

			       
				        
					      try{
					    	  
					          Statement statement = connection.createStatement();
					          statement.executeUpdate("CREATE TABLE `conflict` (" + 
					        		  "id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
					        		  "description VARCHAR(255) NOT NULL, " +
					        		  "PRIMARY KEY (id) " +
					        		  ") ENGINE=InnoDB;");
					          statement.close();
		
					        }
					        catch(SQLException sqlException){
					        	
						    	sqlException.printStackTrace();
						    	return false;
					        }

						      try{
						    	  
						          Statement statement = connection.createStatement();
						          statement.executeUpdate("INSERT INTO `conflict` (" +
						        		  "description " +
						        		  ") VALUES ('conflicting values'), ('NULL in source 1 versus value in source 2'), ('NULL in source 2 versus value in source 1');");
						          statement.close();
			
						        }
						        catch(SQLException sqlException){
						        	
							    	sqlException.printStackTrace();
							    	return false;
						        }				        

					      try{
					    	  
					          Statement statement = connection.createStatement();
					          statement.executeUpdate("CREATE TABLE `resolution` (" + 
					        		  "merge_id BIGINT(255) UNSIGNED NOT NULL, " +
					        		  "joined_keytable_id BIGINT(255) UNSIGNED NOT NULL, " +
					        		  "column_number INT(255) UNSIGNED NOT NULL, " +
					        		  "conflict_id BIGINT(255) NOT NULL, " +
					        		  "solution_by_column_id BIGINT(255) NULL, " +
					        		  "solution_by_row_id BIGINT(255) NULL, " +
					        		  "solution_by_cell_id BIGINT(255) NULL, " +
					        		  "constant VARCHAR(255) NULL, " +
					        		  "PRIMARY KEY (merge_id, joined_keytable_id, column_number), " +
					        		  "INDEX merge_id_index (merge_id), " +
					        		  "INDEX joined_keytable_id_index (joined_keytable_id), " + 
					        		  "INDEX column_number_index (column_number), " + 
					        		  "FOREIGN KEY (merge_id) REFERENCES merge(id) ON DELETE CASCADE ON UPDATE CASCADE " +
					        		  ") ENGINE=InnoDB;");
					          statement.close();

					        }
					        catch(SQLException sqlException){
					        	
						    	sqlException.printStackTrace();
						    	return false;
					        } 				        
				        
					        
						      try{
						          Statement statement = connection.createStatement();
						          statement.executeUpdate("CREATE TABLE export (" + 
						        		  "id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
						        		  "upload_1_id BIGINT(255) UNSIGNED NOT NULL, " +
						        		  "upload_2_id BIGINT(255) UNSIGNED NOT NULL, " +
						        		  "created_by_user_id BIGINT(255) UNSIGNED NOT NULL, " + 
						        		  "created_datetime DATETIME NOT NULL, " +
						        		  "merged_datatable_name VARCHAR(255) NULL, " + 
						        		  "merged_datatable_export_repository_filepath VARCHAR(255) NULL, " +
						        		  "joins_export_repository_filepath VARCHAR(255) NULL, " +
						        		  "resolutions_export_repository_filepath VARCHAR(255) NULL, " +
						        		  "merged_datatable_export_successful BOOLEAN NULL, " +
						        		  "joins_export_successful BOOLEAN NULL, " +
						        		  "resolutions_export_successful BOOLEAN NULL, " +
						        		  "PRIMARY KEY (id), " +
						        		  "CONSTRAINT unique_merged_datatable_export_repository_filepath_constraint UNIQUE (merged_datatable_export_repository_filepath), " +
						        		  "CONSTRAINT unique_joins_export_repository_filepath_constraint UNIQUE (joins_export_repository_filepath), " +
						        		  "CONSTRAINT unique_resolutions_export_repository_filepath_constraint UNIQUE (resolutions_export_repository_filepath), " +
						        		  "INDEX created_by_user_id_index (created_by_user_id), " + 
						        		  "FOREIGN KEY (created_by_user_id) REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE, " +
						        		  "INDEX upload_1_id_index (upload_1_id), " + 
						        		  "FOREIGN KEY (upload_1_id) REFERENCES upload(id) ON DELETE CASCADE ON UPDATE CASCADE, " +
						        		  "INDEX upload_2_id_index (upload_2_id), " + 
						        		  "FOREIGN KEY (upload_2_id) REFERENCES upload(id) ON DELETE CASCADE ON UPDATE CASCADE " + 
						        		  ") ENGINE=InnoDB;");
						          statement.close();
			
						        }
						        catch(SQLException sqlException){
						        	
							    	sqlException.printStackTrace();
							    	return false;
						        }				
				
				
				
				
				///////////////////////////////////////////
				
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
				
			
				
				
			} else {
				
				this.logger.severe("database is not connectable");
				return false;
			}
			
		} else {
			
			this.logger.severe("databaseServerConnection is null");
			return false;
		}
		
		return true;
		
	}
	
	
	public void delete () {
		
		
	}

	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
	}

	public DataModel getDataModel() {
		return dataModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}

	public UserModel getUserModel() {
		return userModel;
	}

	
}