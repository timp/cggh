<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*" %> 
<%@ page import="java.io.*" %>
<%@ page import="java.security.MessageDigest" %>     
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Upgrade db v0 to v0.1</title>
	</head>
	<body>
		<h1>Upgrade db v0 to v0.1</h1>
		
		<p>WARNING: Some statements, such as CREATE TABLE, cause an implicit commit. They cannot be rolled back and commit all previous statements. See http://dev.mysql.com/doc/refman/5.1/en/implicit-commit.html</p>
<% 

if (request.getMethod().equals("POST")) {
	
	try {
		
		Class.forName("com.mysql.jdbc.Driver").newInstance(); 
		Connection connection = DriverManager.getConnection(application.getInitParameter("dbPath"), application.getInitParameter("dbUsername"), application.getInitParameter("dbPassword"));
		 
		if (!connection.isClosed()) {
	
			out.println("<p>Connected to database server.</p>");
	
			out.println("<p>Using one database transaction, which should either succeed and be committed, or fail and be rolled back.</p>");

		    connection.setAutoCommit(false);

		    //Implicit committing statements
		    
			String createDataMergerDbSQL = "CREATE DATABASE `dataMerger` DEFAULT CHARSET=utf8 DEFAULT COLLATE utf8_bin; ";

			String useDataMergerDbSQL = "USE `dataMerger`; ";
			
			String createUserTableSQL = "CREATE TABLE `user` (" +
			"`id` INT NOT NULL AUTO_INCREMENT, " +
			"`username` VARCHAR(256) NOT NULL, " +
			"`password_hash` VARCHAR(256) NOT NULL, " +
			"PRIMARY KEY (`id`)" +
			") ENGINE=InnoDB; ";
			
			String createDatafileTableSQL = "CREATE TABLE `datafile` (" +
			"`id` INT NOT NULL AUTO_INCREMENT, " +
			"`filename` VARCHAR(256) NOT NULL, " +
			"`path` VARCHAR(256) NOT NULL, " +
			"`created_datetime` DATETIME NOT NULL, " +
			"`created_by_user_id` INT NOT NULL, " +
			"PRIMARY KEY (`id`)" +
			") ENGINE=InnoDB; ";		
			
			String createMergeTableSQL = "CREATE TABLE `merge` (" +
			"`id` INT NOT NULL AUTO_INCREMENT, " +
			"`datafile_id_1` INT NOT NULL, " +
			"`datafile_id_2` INT NOT NULL, " +
			"`path` VARCHAR(256) NOT NULL, " +
			"`created_datetime` DATETIME NOT NULL, " +
			"`updated_datetime` DATETIME NOT NULL, " +
			"`created_by_user_id` INT NOT NULL, " +
			"INDEX `datafile_id_1_index` (`datafile_id_1`), " +
			"INDEX `datafile_id_2_index` (`datafile_id_2`), " +
			"INDEX `created_by_user_id_index` (`created_by_user_id`), " +
			"FOREIGN KEY (datafile_id_1) REFERENCES `datafile`(`id`) ON DELETE CASCADE, " +
			"FOREIGN KEY (datafile_id_2) REFERENCES `datafile`(`id`) ON DELETE CASCADE, " +
			"FOREIGN KEY (created_by_user_id) REFERENCES `user`(`id`) ON DELETE CASCADE, " +
			"PRIMARY KEY (`id`)" +
			") ENGINE=InnoDB; ";

			String createDatatableTableSQL = "CREATE TABLE `datatable` (" +
			"`id` INT NOT NULL AUTO_INCREMENT, " +
			"`datatable_name` VARCHAR(256) NOT NULL, " +
			"`datafile_id` INT NOT NULL, " +
			"`created_datetime` DATETIME NOT NULL, " +
			"INDEX `datafile_id_index` (`datafile_id`), " +
			"FOREIGN KEY (`datafile_id`) REFERENCES `datafile`(`id`) ON DELETE CASCADE, " +			
			"PRIMARY KEY (`id`)" +
			") ENGINE=InnoDB; ";
			
			String createJoinTableSQL = "CREATE TABLE `join` (" +
			"`id` INT NOT NULL AUTO_INCREMENT, " +
			"`merge_id` INT NOT NULL, " +
			"`datatable_1_duplicate_keys_count` INT, " +
			"`datatable_2_duplicate_keys_count` INT, " +
			"`total_duplicate_keys_count` INT, " +
			"INDEX `merge_id_index` (`merge_id`), " +
			"FOREIGN KEY (`merge_id`) REFERENCES `merge`(`id`) ON DELETE CASCADE, " +				
			"PRIMARY KEY (`id`)" +
			") ENGINE=InnoDB; ";	
			
			String createJoinColumnTableSQL = "CREATE TABLE `join_column` (" +
			"`id` INT NOT NULL AUTO_INCREMENT, " +
			"`join_id` INT NOT NULL, " +
			"`datatable_1_duplicate_keys_count` INT, " +
			"`datatable_2_duplicate_keys_count` INT, " +
			"`total_duplicate_keys_count` INT, " +
			"INDEX `join_id_index` (`join_id`), " +
			"FOREIGN KEY (`join_id`) REFERENCES `join`(`id`) ON DELETE CASCADE, " +
			"PRIMARY KEY (`id`)" +
			") ENGINE=InnoDB; ";
			
			String createResolutionTableSQL = "CREATE TABLE `resolution` (" +
			"`id` INT NOT NULL AUTO_INCREMENT, " +
			"`merge_id` INT NOT NULL, " +
			"`conflicts_count` INT, " +
			"INDEX `merge_id_index` (`merge_id`), " +
			"FOREIGN KEY (`merge_id`) REFERENCES `merge`(`id`) ON DELETE CASCADE, " +
			"PRIMARY KEY (`id`)" +
			") ENGINE=InnoDB; ";				
			
			String createExportTableSQL = "CREATE TABLE `export` (" +
			"`id` INT NOT NULL AUTO_INCREMENT, " +
			"PRIMARY KEY (`id`)" +
			") ENGINE=InnoDB; ";			
			
			// Transactable statements
			
			String insertAdminUserSQL = "INSERT INTO `user` (`username`, `password_hash`) VALUES (?, ?);";
			
			Statement statement = connection.createStatement();

			//TODO: Refactor this into a function.
			MessageDigest mdInstance = MessageDigest.getInstance("SHA-256");
			byte[] defaultBytes = application.getInitParameter("adminPassword").getBytes();
			mdInstance.reset();
			mdInstance.update(defaultBytes);
			byte mdBytes[] = mdInstance.digest();
			StringBuffer hexStringBuffer = new StringBuffer();
			for (int i = 0; i < mdBytes.length; i++) {
			    String hexString = Integer.toHexString(0xFF & mdBytes[i]);
			    if (hexString.length() == 1) {
			    	hexStringBuffer.append('0');
			    }
			    hexStringBuffer.append(hexString);
			}
			String adminPasswordHash = hexStringBuffer.toString();

			PreparedStatement insertAdminUserPreparedStatement = connection.prepareStatement(insertAdminUserSQL);
			insertAdminUserPreparedStatement.setString(1, application.getInitParameter("adminUsername"));
			insertAdminUserPreparedStatement.setString(2, adminPasswordHash);
			
			
			
			try {
				// Implicit commits
		    	statement.executeUpdate(createDataMergerDbSQL);
		    	statement.executeUpdate(useDataMergerDbSQL);
		    	statement.executeUpdate(createUserTableSQL);
		    	statement.executeUpdate(createDatafileTableSQL);
		    	statement.executeUpdate(createMergeTableSQL);
		    	statement.executeUpdate(createDatatableTableSQL);
		    	statement.executeUpdate(createJoinTableSQL);
		    	statement.executeUpdate(createJoinColumnTableSQL);
		    	statement.executeUpdate(createResolutionTableSQL);
		    	statement.executeUpdate(createExportTableSQL);
		    	
		    	// Transactables
		    	insertAdminUserPreparedStatement.executeUpdate();
		    	connection.commit();
		    	out.println("<p>Committed.</p>");
			}
			catch (SQLException SQLException) {
				connection.rollback();
				out.println("<p>Rolled back.</p>");
				out.println("<p>SQL Exception:</p><p>" + SQLException + "</p>");
				SQLException.printStackTrace();
			}
		    finally {
			    statement.close();
				connection.close();
		    }
		
		} else {
			
			//Connection is closed.
			out.println("<p>Failed to open a connection to the database server.</p>");
			
		}
		
	} 
	catch (Exception e) {
		out.println("<p>Exception:</p><p>" + e + "</p>");
		e.printStackTrace();
	}
%>	
		<form action="<%= request.getRequestURI() %>" method="POST" >
			<input type="submit" value="Try again" />
		</form>
<%
} else {
%>

		<form action="<%= request.getRequestURI() %>" method="POST" >
			<input type="submit" value="Upgrade" />
		</form>
<% 
} 
%>
	</body>
</html>