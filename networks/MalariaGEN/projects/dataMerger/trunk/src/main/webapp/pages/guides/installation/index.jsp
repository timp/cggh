<%-- Note: This file should not require any database interaction because it is also used on database installation pages, etc. --%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%
String installationGuideBasePathURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>dataMerger - Guides - Installation</title>
		
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
					
					<h3>Installation</h3>
		
					<p>This application is available from the Google Code repository as a WAR file, which can be deployed as a web application onto Apache Tomcat. 
					</p>
					
					<h4>Steps</h4>
					<ol>
						<li>Deploy the WAR file to Tomcat.</li>
						<li>Configure the web.xml and tomcat-users.xml files. (See the <a href="<%=installationGuideBasePathURL %>pages/guides/configuration">Configuration</a> guide for details.)</li>
						<li>Use the <a href="<%=installationGuideBasePathURL %>pages/settings">Settings</a> page to create the database, database tables, filebase and filebase directories.</li>
					</ol>
		
					<h4>System requirements</h4>
					
					<ul>
						<li>Tomcat</li>
						<li>MySQL</li>
						<li>Tomcat and MySQL need read and write access to the file repository path, which can be set in the web.xml.</li>
						<li>The MySQL database user needs the FILE permission, which is not database-specific.</li>
					</ul>
				
				
				
				
					<h4>External references</h4>
					<ul>
						<li><a target="_blank" href="http://code.google.com/p/cggh/">Google Code project for this application</a>
						</li>
						<li><a target="_blank" href="http://tomcat.apache.org/">Apache Tomcat</a>
						</li>
						<li><a target="_blank" href="http://www.mysql.com/">MySQL</a>
						</li>
						<li><a target="_blank" href="http://en.wikipedia.org/wiki/WAR_file_format_(Sun)">WAR file format</a>
						</li>
					</ul>
						
				</div>
				
			</div>
			
		</div>
	</body>
</html>