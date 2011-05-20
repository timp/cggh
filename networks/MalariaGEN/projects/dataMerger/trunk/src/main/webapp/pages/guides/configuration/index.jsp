<%-- Note: This file should not require any database interaction because it is also used on database installation pages, etc. --%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
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
		
		
					<h4>Catchable errors and known remedies:</h4>
					<ul>
						<li><a href="errors/database-connection">Database connection</a></li>
					</ul>
		
		
								<h4>[tomcat-server]/webapps/[application-name]/WEB-INF/web.xml:</h4>
								<ul> 
										<li>    databaseDriverFullyQualifiedClassName: com.mysql.jdbc.Driver</li>
										<li>    databaseServerPath: jdbc:mysql://localhost:3306/</li>
										<li>    databaseName: datamerger</li>
										<li>    databaseUsername: root</li>
										<li>    filebaseServerPath: C:\Lee\Work\dataMerger\files\</li>
										<li>    filebaseInstallationLogAsCSVFilePathRelativeToFilebaseServerPath: installation\log.csv</li>
										<li>    stringsToNullifyAsCSV: ,,NULL</li>
										<li>    stringToExportInsteadOfNull: NULL</li>
								</ul>
								
								<%-- TODO: switch to if-can-get rather than if-has-role --%>
								<% if (request.isUserInRole("non-specific")) { %>
								<h5>Current configuration:</h5>
								<ul> 
									<li>databaseDriverFullyQualifiedClassName: <%=application.getInitParameter("databaseDriverFullyQualifiedClassName") %></li>
									<li>databaseServerPath: <%=application.getInitParameter("databaseServerPath") %></li>
									<li>databaseName: <%=application.getInitParameter("databaseName") %></li>
									<li>databaseUsername: <%=application.getInitParameter("databaseUsername") %></li>
									<li>filebaseServerPath: <%=application.getInitParameter("filebaseServerPath") %></li>
									<li>filebaseInstallationLogAsCSVFilePathRelativeToFilebaseServerPath: <%=application.getInitParameter("filebaseInstallationLogAsCSVFilePathRelativeToFilebaseServerPath") %></li>
									<li>stringsToNullifyAsCSV: <%=application.getInitParameter("stringsToNullifyAsCSV") %></li>
									<li>stringToExportInsteadOfNull: <%=application.getInitParameter("stringToExportInsteadOfNull") %></li>
								</ul>
								<% } %>
		
		
								<h4>[tomcat-server]/tomcat-users.xml:</h4>
								<pre style="margin-left: 25px;">
&lt;-- Required roles, referred to in web.xml --&gt;
&lt;role rolename="non-specific"/&gt;
&lt;role rolename="user"/&gt;
&lt;role rolename="uploader"/&gt;
&lt;role rolename="merger"/&gt;
&lt;role rolename="exporter"/&gt;

&lt;-- Optional users --&gt;
&lt;user username="admin" password="admin" roles="user,uploader,merger,exporter,non-specific" /&gt;
&lt;user username="uploader" password="uploader" roles="user,uploader" /&gt;
&lt;user username="merger" password="merger" roles="user,merger" /&gt;
&lt;user username="exporter" password="exporter" roles="user,exporter" /&gt;
								</pre>
				
				</div>
				
			</div>
			
		</div>
	</body>
</html>