<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%-- Note: This file should not require any database interaction because it is also used on database installation pages, etc. --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>dataMerger - Guides - Configuration</title>
		
		<link rel="stylesheet" type="text/css" href="../../shared/css/shared.css" />
		<link rel="stylesheet" type="text/css" href="../css/guides.css" />
		
		<script type="text/javascript" src="../../shared/js/jquery.min.js"></script>
		<script type="text/javascript" src="../../shared/js/shared.js"></script>
		<script type="text/javascript">
	
			$(document).ready(function(){
				initSharedFunctions();
			});
		
		</script>
		
	</head>
	<body>
		<div class="page">
			<%@ include file="../../shared/jsp/header.jsp" %>
			<h2 class="page-title">Guides</h2>
			
			<div class="guides-container">
			
				<%@ include file="../jsp/guides-menu.jsp" %>
				
				<div class="guide">
					
					<h3>Configuration</h3>
					
					<h4>Catchable configuration errors with known remedies:</h4>
					<ul>
						<li><a href="errors/database-connection">Database connection</a></li>
						<li><a href="errors/database-initialization">Database initialization</a></li>
					</ul>	

		
		<h4>Example [tomcat-server]/server.xml:</h4>
		
<pre style="margin-left: 25px;">		
&lt;Connector port="8443" protocol="HTTP/1.1" SSLEnabled="true"
               maxThreads="150" scheme="https" secure="true"
               clientAuth="false" sslProtocol="TLS"
               keystoreFile="C:\Lee\localssl.bin" keystorePass="testtest"
               enableLookups="true" disableUploadTimeout="true" /&gt;
</pre>
		
		
								<h4>Example: [tomcat-server]/webapps/[application-name]/WEB-INF/web.xml:</h4>
<pre style="margin-left: 25px;">	
	&lt;!-- Database connections --&gt;
	&lt;context-param&gt;
		&lt;param-name&gt;databaseDriverFullyQualifiedClassName&lt;/param-name&gt;
		&lt;param-value&gt;com.mysql.jdbc.Driver&lt;/param-value&gt;
	&lt;/context-param&gt;	
	&lt;context-param&gt;
		&lt;param-name&gt;databaseServerPath&lt;/param-name&gt;
		&lt;param-value&gt;jdbc:mysql://localhost:3306/&lt;/param-value&gt;
	&lt;/context-param&gt;	
	&lt;context-param&gt;
		&lt;param-name&gt;databaseName&lt;/param-name&gt;
		&lt;param-value&gt;datamerger&lt;/param-value&gt;
	&lt;/context-param&gt;
	&lt;context-param&gt;
		&lt;param-name&gt;databaseUsername&lt;/param-name&gt;
		&lt;param-value&gt;root&lt;/param-value&gt;
	&lt;/context-param&gt;
	&lt;context-param&gt;
		&lt;param-name&gt;databasePassword&lt;/param-name&gt;
		&lt;param-value&gt;root&lt;/param-value&gt;
	&lt;/context-param&gt;

	&lt;!-- File Repository settings --&gt;
	&lt;context-param&gt;
		&lt;param-name&gt;fileRepositoryBasePath&lt;/param-name&gt;
		&lt;param-value&gt;C:\Lee\Work\dataMerger\files\&lt;/param-value&gt;
	&lt;/context-param&gt;
	&lt;context-param&gt;
		&lt;param-name&gt;fileRepositoryInstallationLogPathRelativeToRepositoryBasePath&lt;/param-name&gt;
		&lt;param-value&gt;file-repository-installation\log.csv&lt;/param-value&gt;
	&lt;/context-param&gt;

	&lt;!-- User Database connections --&gt;
	&lt;context-param&gt;
		&lt;param-name&gt;userDatabaseDriverFullyQualifiedClassName&lt;/param-name&gt;
		&lt;param-value&gt;com.mysql.jdbc.Driver&lt;/param-value&gt;
	&lt;/context-param&gt;
	&lt;context-param&gt;
		&lt;param-name&gt;userDatabaseServerPath&lt;/param-name&gt;
		&lt;param-value&gt;jdbc:mysql://localhost:3306/&lt;/param-value&gt;
	&lt;/context-param&gt;
	&lt;context-param&gt;
		&lt;param-name&gt;userDatabaseName&lt;/param-name&gt;
		&lt;param-value&gt;datamerger&lt;/param-value&gt;
	&lt;/context-param&gt;
	&lt;context-param&gt;
		&lt;param-name&gt;userDatabaseUsername&lt;/param-name&gt;
		&lt;param-value&gt;root&lt;/param-value&gt;
	&lt;/context-param&gt;
	&lt;context-param&gt;
		&lt;param-name&gt;userDatabasePassword&lt;/param-name&gt;
		&lt;param-value&gt;root&lt;/param-value&gt;
	&lt;/context-param&gt;
	&lt;context-param&gt;
		&lt;param-name&gt;userDatabaseTableName&lt;/param-name&gt;
		&lt;param-value&gt;user&lt;/param-value&gt;
	&lt;/context-param&gt;
	&lt;context-param&gt;
		&lt;param-name&gt;userUsernameColumnName&lt;/param-name&gt;
		&lt;param-value&gt;username&lt;/param-value&gt;
	&lt;/context-param&gt;
	&lt;context-param&gt;
		&lt;param-name&gt;userbasePasswordHashColumnName&lt;/param-name&gt;
		&lt;param-value&gt;password_hash&lt;/param-value&gt;
	&lt;/context-param&gt;
	&lt;context-param&gt;
		&lt;param-name&gt;userbasePasswordHashFunctionName&lt;/param-name&gt;
		&lt;param-value&gt;SHA-256&lt;/param-value&gt;
	&lt;/context-param&gt;
</pre>
								
								<% if (request.isUserInRole("administrator")) { %>
								<h5>Current web.xml configuration:</h5>
								<ul> 
									<li>databaseDriverFullyQualifiedClassName: <%=application.getInitParameter("databaseDriverFullyQualifiedClassName") %></li>
									<li>databaseServerPath: <%=application.getInitParameter("databaseServerPath") %></li>
									<li>databaseName: <%=application.getInitParameter("databaseName") %></li>
									<li>databaseUsername: <%=application.getInitParameter("databaseUsername") %></li>
									<li>fileRepositoryBasePath: <%=application.getInitParameter("fileRepositoryBasePath") %></li>
									<li>fileRepositoryInstallationLogPathRelativeToRepositoryBasePath: <%=application.getInitParameter("fileRepositoryInstallationLogPathRelativeToRepositoryBasePath") %></li>
									<li>stringsToNullifyAsCSV: <%=application.getInitParameter("stringsToNullifyAsCSV") %></li>
									<li>stringToExportInsteadOfNull: <%=application.getInitParameter("stringToExportInsteadOfNull") %></li>
								</ul>
								<% } %>
		
		
								<h4>Example [tomcat-server]/tomcat-users.xml:</h4>
								<pre style="margin-left: 25px;">
&lt;-- Required roles, referred to in web.xml --&gt;
&lt;role rolename="administrator"/&gt;

&lt;-- Users --&gt;
&lt;user username="admin" password="test" roles="administrator" /&gt;
								</pre>



				
				</div>
			
			</div>
			

			
		</div>
	</body>
</html>