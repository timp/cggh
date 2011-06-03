package org.cggh.tools.dataMerger.scripts.data.databases.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import org.cggh.tools.dataMerger.data.databases.DatabaseModel;
import org.cggh.tools.dataMerger.data.users.UserModel;

public class DatabaseTablesScripts {

	//INFO: What is a "script"?
	//A script is not a function, not an elemental Model, CRUD or Controller.
	//It is usually a chain of functions or CRUD calls.
	
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.scripts.data.tables");
	
	public Boolean createInitialTablesUsingDatabaseModelAndUsername (DatabaseModel databaseModel, String Username) {

		Connection connection = databaseModel.getNewConnection();
		
		if (connection != null) {
		
        
				try {
		          Statement statement = connection.createStatement();
		          statement.executeUpdate("CREATE TABLE user (id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, username VARCHAR(255) NOT NULL, PRIMARY KEY (id), CONSTRAINT unique_username_constraint UNIQUE (username)) ENGINE=InnoDB;");
		          statement.close();
	
		        }
		        catch(SQLException sqlException){
			    	sqlException.printStackTrace();
			    	try {
			    		//FIXME: Re-organize try-catches and introduce a finally blocks in all of these.
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			    	return false;
		        }	
		     
	        
		      try{
		    	  
		          PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `user` (username) VALUES (?)");
		          preparedStatement.setString(1, Username);
		          preparedStatement.executeUpdate();
		          preparedStatement.close();

		        }
		      catch(SQLException sqlException){
			    	sqlException.printStackTrace();
			    	try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			    	return false;
		        }
		      
				//Short-cut
		        UserModel userModel = new UserModel();
		        userModel.setUsername(Username);
				userModel.setId(1);
						
	        
		      try{
		          Statement statement = connection.createStatement();
		          statement.executeUpdate("CREATE TABLE `installation` (" +  
		        		  "id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
		        		  "major_version_number BIGINT(255) UNSIGNED NOT NULL, " +
		        		  "minor_version_number BIGINT(255) UNSIGNED NOT NULL, " +
		        		  "revision_version_number BIGINT(255) UNSIGNED NOT NULL, " + 
		        		  "created_by_user_id BIGINT(255) UNSIGNED NOT NULL, " + 
		        		  "created_datetime DATETIME NOT NULL, " +
		        		  "PRIMARY KEY (id) " +
		        		  ") ENGINE=InnoDB;");
		          statement.close();

		        }
		      catch(SQLException sqlException){
			    	sqlException.printStackTrace();
			    	try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			    	return false;
		        }
		      
		        
		        
			      try{
			    	  
			          
			          PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `installation` (" +
			        		  "major_version_number, minor_version_number, revision_version_number, created_by_user_id, created_datetime " +
			        		  ") VALUES (1, 1, 0, ?, NOW());");
			          preparedStatement.setInt(1, userModel.getId());
			          preparedStatement.executeUpdate();
			          preparedStatement.close();

			        }
			      catch(SQLException sqlException){
				    	sqlException.printStackTrace();
				    	try {
							connection.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
				    	return false;
			        }
			      
	        
	      try{
	          Statement statement = connection.createStatement();
	          statement.executeUpdate("CREATE TABLE upload (" + 
	        		  "id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
	        		  "original_filename VARCHAR(255) NOT NULL, " + 
	        		  "repository_filepath VARCHAR(255) NULL, " + 
	        		  "file_size_in_bytes BIGINT(255) UNSIGNED NULL, " +
	        		  "created_by_user_id BIGINT(255) UNSIGNED NOT NULL, " + 
	        		  "created_datetime DATETIME NOT NULL, " +
	        		  "datatable_name VARCHAR(255) NULL, " + 
	        		  "PRIMARY KEY (id), " +
	        		  "CONSTRAINT unique_path_constraint UNIQUE (repository_filepath), " +
	        		  "CONSTRAINT unique_datatable_name_constraint UNIQUE (datatable_name), " +
	        		  "INDEX created_by_user_id_index (created_by_user_id), " + 
	        		  "FOREIGN KEY (created_by_user_id) REFERENCES user(id) " + 
	        		  ") ENGINE=InnoDB;");
	          
	          //NOTE: Took out ON DELETE CASCADE ON UPDATE CASCADE for safety
	          
	          statement.close();

	        }
	      catch(SQLException sqlException){
		    	sqlException.printStackTrace();
		    	try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		    	return false;
	        }

	      try{
	          Statement statement = connection.createStatement();
	          statement.executeUpdate("CREATE TABLE label (" + 
	        		  "id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
	        		  "name VARCHAR(255) NOT NULL, " +  
	        		  "PRIMARY KEY (id), " +
	        		  "CONSTRAINT unique_name_constraint UNIQUE (name) " + 
	        		  ") ENGINE=InnoDB;");
	          
	          //NOTE: Took out ON DELETE CASCADE ON UPDATE CASCADE for safety
	          
	          statement.close();

	        }
	      catch(SQLException sqlException){
		    	sqlException.printStackTrace();
		    	try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		    	return false;
	        }	      

	      
	      try{
	    	  
	          Statement statement = connection.createStatement();
	          statement.executeUpdate("INSERT INTO `label` (" +
	        		  "name " +
	        		  ") VALUES ('hidden'), ('removed');");
	          statement.close();

	        }
	      catch(SQLException sqlException){
		    	sqlException.printStackTrace();
		    	try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		    	return false;
	        }	      
	      

	      try{
	          Statement statement = connection.createStatement();
	          statement.executeUpdate("CREATE TABLE upload_label (" + 
	        		  "upload_id BIGINT(255) UNSIGNED NOT NULL, " +
	        		  "label_id BIGINT(255) UNSIGNED NOT NULL, " +
	        		  "PRIMARY KEY (upload_id, label_id), " +
	        		  "INDEX upload_id_index (upload_id), " + 
	        		  "FOREIGN KEY (upload_id) REFERENCES upload(id), " +
	        		  "INDEX label_id_index (label_id), " +
	        		  "FOREIGN KEY (label_id) REFERENCES label(id) " +
	        		  ") ENGINE=InnoDB;");
	          
	          //NOTE: Took out ON DELETE CASCADE ON UPDATE CASCADE for safety
	          
	          statement.close();

	        }
	      catch(SQLException sqlException){
		    	sqlException.printStackTrace();
		    	try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
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
		        		  "FOREIGN KEY (created_by_user_id) REFERENCES user(id), " +
		        		  "INDEX upload_1_id_index (upload_1_id), " + 
		        		  "FOREIGN KEY (upload_1_id) REFERENCES upload(id), " +
		        		  "INDEX upload_2_id_index (upload_2_id), " + 
		        		  "FOREIGN KEY (upload_2_id) REFERENCES upload(id) " + 
		        		  ") ENGINE=InnoDB;");
		          
		        //NOTE: Took out ON DELETE CASCADE ON UPDATE CASCADE for safety
		          
		          statement.close();

		        }
		      catch(SQLException sqlException){
			    	sqlException.printStackTrace();
			    	try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			    	return false;
		        }
		      

		      try{
		          Statement statement = connection.createStatement();
		          statement.executeUpdate("CREATE TABLE merge_label (" + 
		        		  "merge_id BIGINT(255) UNSIGNED NOT NULL, " +
		        		  "label_id BIGINT(255) UNSIGNED NOT NULL, " +
		        		  "PRIMARY KEY (merge_id, label_id), " +
		        		  "INDEX merge_id_index (merge_id), " + 
		        		  "FOREIGN KEY (merge_id) REFERENCES merge(id), " +
		        		  "INDEX label_id_index (label_id), " +
		        		  "FOREIGN KEY (label_id) REFERENCES label(id) " +
		        		  ") ENGINE=InnoDB;");
		          
		          statement.close();

		        }
		      catch(SQLException sqlException){
			    	sqlException.printStackTrace();
			    	try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
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
		        		  "FOREIGN KEY (merge_id) REFERENCES merge(id) " +
		        		  ") ENGINE=InnoDB;");
		          
		        //NOTE: Took out ON DELETE CASCADE ON UPDATE CASCADE for safety
		          
		          statement.close();

		        }
		      catch(SQLException sqlException){
			    	sqlException.printStackTrace();
			    	try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
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
				    	try {
							connection.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
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
				    	try {
							connection.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
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
		    	try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
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
		    	try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
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
			    	try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
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
			    	try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
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
				    	try {
							connection.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
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
					    	try {
								connection.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
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
			        		  "FOREIGN KEY (merge_id) REFERENCES merge(id) " +
			        		  ") ENGINE=InnoDB;");
			          
			        //NOTE: Took out ON DELETE CASCADE ON UPDATE CASCADE for safety
			          
			          statement.close();

			        }
			      catch(SQLException sqlException){
				    	sqlException.printStackTrace();
				    	try {
							connection.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
				    	return false;
			        }
			      
			        
				      try{
				          Statement statement = connection.createStatement();
				          statement.executeUpdate("CREATE TABLE export (" + 
				        		  "id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
				        		  "filename VARCHAR(255) NOT NULL, " + 
				        		  "upload_1_id BIGINT(255) UNSIGNED NOT NULL, " +
				        		  "upload_2_id BIGINT(255) UNSIGNED NOT NULL, " +
				        		  "created_by_user_id BIGINT(255) UNSIGNED NOT NULL, " + 
				        		  "created_datetime DATETIME NOT NULL, " +
				        		  "merged_datatable_name VARCHAR(255) NULL, " + 
				        		  "merged_datatable_export_repository_filepath VARCHAR(255) NULL, " +
				        		  "merged_datatable_export_file_size_in_bytes BIGINT(255) UNSIGNED NULL, " +
				        		  "joins_export_repository_filepath VARCHAR(255) NULL, " +
				        		  "resolutions_export_repository_filepath VARCHAR(255) NULL, " +
				        		  "PRIMARY KEY (id), " +
				        		  "CONSTRAINT unique_merged_datatable_export_repository_filepath_constraint UNIQUE (merged_datatable_export_repository_filepath), " +
				        		  "CONSTRAINT unique_joins_export_repository_filepath_constraint UNIQUE (joins_export_repository_filepath), " +
				        		  "CONSTRAINT unique_resolutions_export_repository_filepath_constraint UNIQUE (resolutions_export_repository_filepath), " +
				        		  "INDEX created_by_user_id_index (created_by_user_id), " + 
				        		  "FOREIGN KEY (created_by_user_id) REFERENCES user(id), " +
				        		  "INDEX upload_1_id_index (upload_1_id), " + 
				        		  "FOREIGN KEY (upload_1_id) REFERENCES upload(id), " +
				        		  "INDEX upload_2_id_index (upload_2_id), " + 
				        		  "FOREIGN KEY (upload_2_id) REFERENCES upload(id) " + 
				        		  ") ENGINE=InnoDB;");
				          
				        //NOTE: Took out ON DELETE CASCADE ON UPDATE CASCADE for safety
				          
				          statement.close();
	
				        }
				      catch(SQLException sqlException){
					    	sqlException.printStackTrace();
					    	try {
								connection.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
					    	return false;
				        }
				      
		
				      
				      
				      
				      try{
				          Statement statement = connection.createStatement();
				          statement.executeUpdate("CREATE TABLE export_label (" + 
				        		  "export_id BIGINT(255) UNSIGNED NOT NULL, " +
				        		  "label_id BIGINT(255) UNSIGNED NOT NULL, " +
				        		  "PRIMARY KEY (export_id, label_id), " +
				        		  "INDEX export_id_index (export_id), " + 
				        		  "FOREIGN KEY (export_id) REFERENCES export(id), " +
				        		  "INDEX label_id_index (label_id), " +
				        		  "FOREIGN KEY (label_id) REFERENCES label(id) " +
				        		  ") ENGINE=InnoDB;");
				          
				          //NOTE: Took out ON DELETE CASCADE ON UPDATE CASCADE for safety
				          
				          statement.close();

				        }
				      catch(SQLException sqlException){
					    	sqlException.printStackTrace();
					    	try {
								connection.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
					    	return false;
				        }					      
				      
				      
				      
				      
				        try {
							connection.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
				        
						return true;

		
		} else {
			
			//CRUD should not have been attempted unless isConnectable
			this.logger.severe("connection is null");
			return false;
		}
		
		
		
	}

	public boolean deleteTablesUsingDatabaseModel(DatabaseModel databaseModel) {

		Connection connection = databaseModel.getNewConnection();
		
		if (connection != null) {
		
		      try{
		          Statement statement = connection.createStatement();
		          statement.executeUpdate("DROP TABLE export_label;");
		          statement.close();

		        }
		      catch(SQLException sqlException){
			    	sqlException.printStackTrace();
			    	try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			    	return false;
		        }			      
			        
			      try{
			          Statement statement = connection.createStatement();
			          statement.executeUpdate("DROP TABLE export;");
			          statement.close();

			        }
			      catch(SQLException sqlException){
				    	sqlException.printStackTrace();
				    	try {
							connection.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
				    	return false;
			        }
			      
			      try{
			    	  
			          Statement statement = connection.createStatement();
			          statement.executeUpdate("DROP TABLE `resolution`;");
			          statement.close();

			        }
			      catch(SQLException sqlException){
				    	sqlException.printStackTrace();
				    	try {
							connection.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
				    	return false;
			        }	
			      
			      try{
			    	  
			          Statement statement = connection.createStatement();
			          statement.executeUpdate("DROP TABLE `conflict`;");
			          statement.close();

			        }
			      catch(SQLException sqlException){
				    	sqlException.printStackTrace();
				    	try {
							connection.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
				    	return false;
			        }			      
			      
			      try{
			    	  
			          Statement statement = connection.createStatement();
			          statement.executeUpdate("DROP TABLE `solution_by_cell`;");
			          statement.close();

			        }
			      catch(SQLException sqlException){
				    	sqlException.printStackTrace();
				    	try {
							connection.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
				    	return false;
			        }			      
			      
					try{
				    	  
				          Statement statement = connection.createStatement();
				          statement.executeUpdate("DROP TABLE `solution_by_row`;");
				          statement.close();

				        }
						catch(SQLException sqlException){
					    	sqlException.printStackTrace();
					    	try {
								connection.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
					    	return false;
				        }			      
			      
					      try{
					    	  
					          Statement statement = connection.createStatement();
					          statement.executeUpdate("DROP TABLE `solution_by_column`;");
					          statement.close();

					        }
					      catch(SQLException sqlException){
						    	sqlException.printStackTrace();
						    	try {
									connection.close();
								} catch (SQLException e) {
									e.printStackTrace();
								}
						    	return false;
					        }			      
			      
					      try{
					    	  
					    	  //Note: (255) in BIGINT(255) is only the display width, not the capacity.
					    	  
					          Statement statement = connection.createStatement();
					          statement.executeUpdate("DROP TABLE `join`;");
					          statement.close();

					        }
					      catch(SQLException sqlException){
						    	sqlException.printStackTrace();
						    	try {
									connection.close();
								} catch (SQLException e) {
									e.printStackTrace();
								}
						    	return false;
					        }

					      
					      try{
					          Statement statement = connection.createStatement();
					          statement.executeUpdate("DROP TABLE merge_label;");
					          statement.close();

					        }
					      catch(SQLException sqlException){
						    	sqlException.printStackTrace();
						    	try {
									connection.close();
								} catch (SQLException e) {
									e.printStackTrace();
								}
						    	return false;
					        }					      
					      
					      try{
					          Statement statement = connection.createStatement();
					          statement.executeUpdate("DROP TABLE merge;");
					          statement.close();

					        }
					      catch(SQLException sqlException){
						    	sqlException.printStackTrace();
						    	try {
									connection.close();
								} catch (SQLException e) {
									e.printStackTrace();
								}
						    	return false;
					        }					      

					      try{
					          Statement statement = connection.createStatement();
					          statement.executeUpdate("DROP TABLE upload_label;");
					          statement.close();

					        }
					      catch(SQLException sqlException){
						    	sqlException.printStackTrace();
						    	try {
									connection.close();
								} catch (SQLException e) {
									e.printStackTrace();
								}
						    	return false;
					        }
					      
					      try{
					          Statement statement = connection.createStatement();
					          statement.executeUpdate("DROP TABLE label;");
					          statement.close();

					        }
					      catch(SQLException sqlException){
						    	sqlException.printStackTrace();
						    	try {
									connection.close();
								} catch (SQLException e) {
									e.printStackTrace();
								}
						    	return false;
					        }					      
					      
					      try{
					          Statement statement = connection.createStatement();
					          statement.executeUpdate("DROP TABLE upload;");
					          statement.close();

					        }
					      catch(SQLException sqlException){
						    	sqlException.printStackTrace();
						    	try {
									connection.close();
								} catch (SQLException e) {
									e.printStackTrace();
								}
						    	return false;
					        }
					      
					      try{
					          Statement statement = connection.createStatement();
					          statement.executeUpdate("DROP TABLE `installation`;");
					          statement.close();

					        }
					      catch(SQLException sqlException){
						    	sqlException.printStackTrace();
						    	try {
									connection.close();
								} catch (SQLException e) {
									e.printStackTrace();
								}
						    	return false;
					        }					      
					      
							try {
						          Statement statement = connection.createStatement();
						          statement.executeUpdate("DROP TABLE user;");
						          statement.close();
					
						        }
						        catch(SQLException sqlException){
							    	sqlException.printStackTrace();
							    	try {
										connection.close();
									} catch (SQLException e) {
										e.printStackTrace();
									}
							    	return false;
						        }					      
					      
			      
			      
			        try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				        
						return true;

		
		} else {
			
			//CRUD should not have been attempted unless isConnectable
			this.logger.severe("connection is null");
			return false;
		}
		
	}
	
}
