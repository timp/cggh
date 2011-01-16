<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*" %> 
<%@ page import="java.io.*" %>     
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Downgrade db v0.1 to v0</title>
	</head>
	<body>
		<h1>Downgrade db v0.1 to v0</h1>
		
		<p>WARNING: This will completely delete the dataMerger database.</p>
<% 

if (request.getMethod().equals("POST")) {
	
	try {
		
		Class.forName("com.mysql.jdbc.Driver").newInstance(); 
		Connection connection = DriverManager.getConnection(application.getInitParameter("dbPath"), application.getInitParameter("dbUsername"), application.getInitParameter("dbPassword"));
		 
		if(!connection.isClosed())
	
			out.println("<p>Connected to database server.</p>");
	
			out.println("<p>Using one database transaction, which should either succeed and be committed or fail and be rolled back.</p>");
	
			PreparedStatement preparedStatement = null;
			boolean wasSuccessful = false;
			
			try {
			    connection.setAutoCommit( false );
	
	
				String transactionSQL = "";
				
				transactionSQL += "DROP DATABASE `dataMerger`;";
			    
			    preparedStatement = connection.prepareStatement(transactionSQL);
			    preparedStatement.executeUpdate();
			    preparedStatement.close();
			    wasSuccessful = true;
			}
			finally {
			    try {
			        try {
			            if (wasSuccessful && connection != null) {
			                connection.commit();
			            	out.println("<p>Committed.</p>");
			        	} 
			            else if (!wasSuccessful && connection != null) {
			                connection.rollback();
			                out.println("<p>Rolled back.</p>");
			        	}
			        }
			        catch(SQLException SQLException) {
			            throw new RuntimeException((wasSuccessful ? "Commit " : "Rollback ") + "failed.");
			        }
			    }
			    finally {
			    	
			        if(preparedStatement != null)
			        	
			            try {preparedStatement.close();} catch ( SQLException SQLException) {};
			            
			        if(connection != null) {
			            try {
			                connection.setAutoCommit( true );
			            } 
			            catch (SQLException SQLException) {
							throw new RuntimeException("Unable to enable autocommit", SQLException);
			            } finally {
			                try {connection.close();} catch (SQLException SQLException) {};
			            }
			        }
			    }
			}
			
	
	
	
			
	
			connection.close();
			
	} 
	catch (Exception e) {
		System.out.println("Failed to connect to database server.");
		out.println("<p>" + e + "</p>");
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
			<input type="submit" value="Downgrade" />
		</form>
<% 
} 
%>
	</body>
</html>