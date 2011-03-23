<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*" %> 
<%@ page import="java.io.*" %>     
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Connect to database</title>
	</head>
	<body>
		<h1>Connect to database</h1>
		
<% 

if (request.getMethod().equals("POST")) {
	
	try {
		
		Class.forName("com.mysql.jdbc.Driver").newInstance(); 
		Connection connection = DriverManager.getConnection(application.getInitParameter("dbPath"), application.getInitParameter("databaseUsername"), application.getInitParameter("databasePassword"));
		 
		if(!connection.isClosed())
	
			out.println("<p>Connected to database server.</p>");
	
			connection.close();
			
	} 
	catch (Exception e) {
		System.out.println("Failed to connect to database server. Using path " + application.getInitParameter("dbPath") + ", user " + application.getInitParameter("databaseUsername"));
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
			<input type="submit" value="Connect" />
		</form>
<% 
} 
%>
	</body>
</html>