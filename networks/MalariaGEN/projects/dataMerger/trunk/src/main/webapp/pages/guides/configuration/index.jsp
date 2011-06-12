<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%-- Note: This file should not require any database interaction because it is also used on database installation pages, etc. --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>dataMerger - Guides - Configuration</title>
		
		<link rel="stylesheet" type="text/css" href="../../shared/css/shared.css" />
		<link rel="stylesheet" type="text/css" href="../css/guides.css" />
		<script type="text/javascript" src="../../shared/js/shared.js"></script>
		
	</head>
	<body>
		<div class="page">
			<%@ include file="../../shared/jsp/header.jsp" %>
			<h2 class="page-title">Guides</h2>
			
			<div class="guides-container">
			
				<%@ include file="../jsp/guides-menu.jsp" %>
				
				<div class="guide">
					
					<h3>Configuration</h3>
					
		<p><a href="http://techtracer.com/2007/09/12/setting-up-ssl-on-tomcat-in-3-easy-steps/" target="_blank">Setting up SSL on Tomcat</a></p>
		
<pre style="margin-left: 25px;">		
&lt;Connector port="8443" protocol="HTTP/1.1" SSLEnabled="true"
               maxThreads="150" scheme="https" secure="true"
               clientAuth="false" sslProtocol="TLS"
               keystoreFile="C:\Lee\localssl.bin" keystorePass="testtest"
               enableLookups="true" disableUploadTimeout="true" /&gt;
</pre>
		
		
								<h4>Example: [tomcat-server]/webapps/[application-name]/WEB-INF/web.xml:</h4>
								<ul> 
										<li>    databaseDriverFullyQualifiedClassName: com.mysql.jdbc.Driver</li>
										<li>    databaseServerPath: jdbc:mysql://localhost:3306/</li>
										<li>    databaseName: datamerger</li>
										<li>    databaseUsername: root</li>
										<li>    fileRepositoryBasePath: C:\Lee\Work\dataMerger\files\</li>
										<li>    fileRepositoryInstallationLogPathRelativeToRepositoryBasePath: installation\log.csv</li>
										<li>    stringsToNullifyAsCSV: ,,NULL</li>
										<li>    stringToExportInsteadOfNull: NULL</li>
								</ul>
								
								<%-- TODO: switch to if-can-get rather than if-has-role --%>
								<% if (request.isUserInRole("non-specific")) { %>
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
&lt;role rolename="non-specific"/&gt;
&lt;role rolename="user"/&gt;
&lt;role rolename="uploader"/&gt;
&lt;role rolename="merger"/&gt;
&lt;role rolename="exporter"/&gt;

&lt;-- Optional users --&gt;
&lt;user username="setter" password="setter" roles="user,uploader,merger,exporter,non-specific" /&gt;
&lt;user username="uploader" password="uploader" roles="user,uploader" /&gt;
&lt;user username="merger" password="merger" roles="user,merger" /&gt;
&lt;user username="exporter" password="exporter" roles="user,exporter" /&gt;
								</pre>

					<h4>Catchable configuration errors with known remedies:</h4>
					<ul>
						<li><a href="errors/database-connection">Database connection</a></li>
						<li><a href="errors/database-initialization">Database initialization</a></li>
					</ul>	

				
				</div>
			
			</div>
			

			
		</div>
	</body>
</html>