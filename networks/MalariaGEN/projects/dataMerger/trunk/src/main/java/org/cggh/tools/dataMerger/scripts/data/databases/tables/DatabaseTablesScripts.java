package org.cggh.tools.dataMerger.scripts.data.databases.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import org.cggh.tools.dataMerger.data.databases.DatabaseModel;

public class DatabaseTablesScripts {

	//INFO: What is a "script"?
	//A script is not a function, not an elemental Model, CRUD or Controller.
	//It is usually a chain of functions or CRUD calls.
	
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.scripts.data.tables");
	
	public Boolean createInitialTablesUsingDatabaseModel (DatabaseModel databaseModel) {

		Connection connection = databaseModel.getNewConnection();
		
		if (connection != null) {
		
        
				try {
		          Statement statement = connection.createStatement();
		          statement.executeUpdate("CREATE TABLE user (" +
		          		"id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
		          		"username VARCHAR(255) NOT NULL, " +
		          		"password_hash VARCHAR(255) NOT NULL, " +
		          		"PRIMARY KEY (id), " +
		          		"CONSTRAINT unique_username_constraint UNIQUE (username) " +
		          		") ENGINE=InnoDB;");
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
		          Statement statement = connection.createStatement();
		          statement.executeUpdate("CREATE TABLE `data_installation` (" +  
		        		  "id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
		        		  "major_version_number BIGINT(255) UNSIGNED NOT NULL, " +
		        		  "minor_version_number BIGINT(255) UNSIGNED NOT NULL, " +
		        		  "revision_version_number BIGINT(255) UNSIGNED NOT NULL, " + 
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
			    	  
			          
			          PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `data_installation` (" +
			        		  "major_version_number, minor_version_number, revision_version_number, created_datetime " +
			        		  ") VALUES (1, 1, 0, NOW());");
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
			          statement.executeUpdate("CREATE TABLE `file_origin` (" +  
			        		  "id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
			        		  "origin VARCHAR(255) NOT NULL, " +
			        		  "PRIMARY KEY (id)," +
			        		  "CONSTRAINT unique_origin_constraint UNIQUE (origin) " +
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
				    	  
				          
				          PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `file_origin` (" +
				        		  "origin" +
				        		  ") VALUES ('upload'), ('export');");
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
	          statement.executeUpdate("CREATE TABLE file (" + 
	        		  "id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
	        		  "filename VARCHAR(255) NOT NULL, " + 
	        		  "filepath VARCHAR(255) NULL, " + 
	        		  "file_size_in_bytes BIGINT(255) UNSIGNED NULL, " +
	        		  "file_origin_id BIGINT(255) UNSIGNED NULL, " +
	        		  "created_by_user_id BIGINT(255) UNSIGNED NOT NULL, " + 
	        		  "created_datetime DATETIME NOT NULL, " +
	        		  
	        		  "datatable_name VARCHAR(255) NULL, " +
	        		  
	        		  "rows_count BIGINT(255) NULL, " +
	        		  "columns_count BIGINT(255) NULL, " +
	        		  
	        		  "hidden BOOLEAN NULL, " +
	        		  
	        		  "PRIMARY KEY (id), " +
	        		  "CONSTRAINT unique_path_constraint UNIQUE (filepath), " +
	        		  "CONSTRAINT unique_datatable_name_constraint UNIQUE (datatable_name), " +
	        		  "INDEX created_by_user_id_index (created_by_user_id), " + 
	        		  "FOREIGN KEY (created_by_user_id) REFERENCES user(id) " + 
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
		          statement.executeUpdate("CREATE TABLE merge (" + 
		        		  "id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
		        		  "file_1_id BIGINT(255) UNSIGNED NOT NULL, " + 
		        		  "file_2_id BIGINT(255) UNSIGNED NOT NULL, " + 
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
		        		  "INDEX file_1_id_index (file_1_id), " + 
		        		  "FOREIGN KEY (file_1_id) REFERENCES file(id), " +
		        		  "INDEX file_2_id_index (file_2_id), " + 
		        		  "FOREIGN KEY (file_2_id) REFERENCES file(id) " + 
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
		        		  "CONSTRAINT unique_column_name_per_merge_constraint UNIQUE (merge_id, column_name), " +
		        		  "INDEX merge_id_index (merge_id), " + 
		        		  "FOREIGN KEY (merge_id) REFERENCES merge(id) ON DELETE CASCADE" +
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
			        		  "FOREIGN KEY (merge_id) REFERENCES merge(id) ON DELETE CASCADE" +
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
				          statement.executeUpdate("CREATE TABLE export (" + 
				        		  "id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
				        		  
				        		  "source_file_1_id BIGINT(255) UNSIGNED NOT NULL, " +
				        		  "source_file_2_id BIGINT(255) UNSIGNED NOT NULL, " +
				        		  "merged_file_id BIGINT(255) UNSIGNED NULL, " +
				        		  
				        		  "source_file_1_filepath VARCHAR(255) NOT NULL, " +
				        		  "source_file_2_filepath VARCHAR(255) NOT NULL, " +
				        		  "merged_file_filepath VARCHAR(255) NULL, " +
				        		  
				        		  "joins_record_filepath VARCHAR(255) NULL, " +
				        		  "resolutions_record_filepath VARCHAR(255) NULL, " +
				        		  "settings_record_filepath VARCHAR(255) NULL, " +
				        		  
				        		  "created_by_user_id BIGINT(255) UNSIGNED NOT NULL, " + 
				        		  "created_datetime DATETIME NOT NULL, " +
				        		  
				        		  
				        		  "PRIMARY KEY (id), " +
				        		  "CONSTRAINT unique_merged_file_filepath_constraint UNIQUE (merged_file_filepath), " +
				        		  "CONSTRAINT unique_joins_record_filepath_constraint UNIQUE (joins_record_filepath), " +
				        		  "CONSTRAINT unique_resolutions_record_filepath_constraint UNIQUE (resolutions_record_filepath), " +
				        		  "CONSTRAINT unique_settings_record_filepath_constraint UNIQUE (settings_record_filepath), " +
				        		  
				        		  "INDEX created_by_user_id_index (created_by_user_id), " + 
				        		  "FOREIGN KEY (created_by_user_id) REFERENCES user(id), " +
				        		  
				        		  "INDEX source_file_1_id_index (source_file_1_id), " + 
				        		  "FOREIGN KEY (source_file_1_id) REFERENCES file(id), " +
				        		  
				        		  "INDEX source_file_2_id_index (source_file_2_id), " + 
				        		  "FOREIGN KEY (source_file_2_id) REFERENCES file(id), " +
				        		  
				        		  "INDEX merged_file_id_index (merged_file_id), " + 
				        		  "FOREIGN KEY (merged_file_id) REFERENCES file(id) " + 
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
				          statement.executeUpdate("CREATE TABLE setting (" +
				        		  "id BIGINT(255) UNSIGNED NOT NULL AUTO_INCREMENT, " +
				        		  "name VARCHAR(255) NOT NULL, " +
				        		  "value VARCHAR(255) NULL, " +
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
				      
				      // fileRepositoryBasePath:  Tomcat and MySQL must have read and write access to this directory.
				      // stringToExportInsteadOfNull: Without this parameter the default behaviour is to export NULLs as \N (unquoted)
				      
				      try{
				    	  
				          Statement statement = connection.createStatement();
				          statement.executeUpdate("INSERT INTO `setting` (" +
				        		  "name, value" +
				        		  ") VALUES " +
				        		  "('stringsToNullifyAsCSV', ',NULL'), " +
				        		  "('stringToExportInsteadOfNull', 'NULL') " +
				        		  ";");
				          
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
				      
				      ///////////
				      
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

	public Boolean deleteTablesUsingDatabaseModel(DatabaseModel databaseModel) {

		Connection connection = databaseModel.getNewConnection();
		
		if (connection != null) {
		
		      try{
		          Statement statement = connection.createStatement();
		          statement.executeUpdate("DROP TABLE setting;");
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
					          statement.executeUpdate("DROP TABLE file;");
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
					          statement.executeUpdate("DROP TABLE file_origin;");
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
					          statement.executeUpdate("DROP TABLE `data_installation`;");
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
					      
			      
				    ///////////////////////////
			      
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
