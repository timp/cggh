<%-- Note: This file should not require any database interaction because it is also used on database installation pages, etc. --%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%
String databaseConnectionErrorBasePathURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>dataMerger - Guides - Configuration - Errors - Database connection</title>
		
		<link rel="stylesheet" type="text/css" href="../../../../shared/css/shared.css" />
		<link rel="stylesheet" type="text/css" href="../../../css/guides.css" />
		<script type="text/javascript" src="../../../../shared/js/shared.js"></script>
		
	</head>
	<body>
		<div class="page">
			<%@ include file="../../../../shared/jsp/header.jsp" %>
			<h2 class="page-title">Guides</h2>
			
			<div class="guides-container">
			
				<%@ include file="../../../jsp/guides-menu.jsp" %>
				
				<div class="guide">
					
					<h3>Configuration</h3>
		
					<h4>Errors</h4>
					
					<h5>Database connection</h5>
					
					<p>The application cannot connect to the database using the specified parameters.</p>
					
					<h6>Possible causes:</h6>
					<ul>
						<li>The values specified in the web.xml are incorrect. (See the <a href="<%=databaseConnectionErrorBasePathURL %>pages/guides/configuration">Configuration Guide</a>.)</li>
						<li>The database has not been installed. (See the <a href="<%=databaseConnectionErrorBasePathURL %>pages/guides/installation">Installation Guide</a>.)</li>
					</ul>	
					
					<h6>The required configuration parameters in the web.xml are:</h6>
					<ul>
					 	<li>databaseDriverFullyQualifiedClassName</li>
						<li>databaseServerPath</li>
						<li>databaseName</li>
						<li>databaseUsername</li>
						<li>databasePassword</li>
					</ul>
					
				</div>
				
			</div>
			
		</div>
	</body>
</html>