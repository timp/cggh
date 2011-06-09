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
					
					<p>The application cannot connect to the specified database using the specified parameters.</p>
					
					<h6>Possible causes:</h6>
					<ul>
						<li>The values specified in the web.xml are incorrect (/webapps/dataMerger/WEB-INF/web.xml).</li>
						<li>The database has not been installed.</li>
					</ul>	
					
					<p>The required configuration parameters are databaseDriverFullyQualifiedClassName, databaseServerPath, databaseName, databaseUsername, databasePassword.
					</p>
					
					<p>The required values can be seen on the <a href="../../">configuration page</a>.
					</p>
					
					<p>For application administrators, the current values can be seen on the <a href="../../">configuration page</a> and the database can be installed using the <a href="<%=databaseConnectionErrorBasePathURL %>pages/settings/">settings section</a>.
					</p>
		
				</div>
				
			</div>
			
		</div>
	</body>
</html>