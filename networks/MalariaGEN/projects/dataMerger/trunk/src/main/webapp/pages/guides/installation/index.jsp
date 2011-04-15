<%-- Note: This file should not require any database interaction because it is also used on database installation pages, etc. --%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>dataMerger - Guides - Installation</title>
		
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
					
					<h3>Installation</h3>
		
					<p>This application is available from the Google Code repository as a WAR file, which can be deployed as a web application to Apache Tomcat. 
					</p>
					
		
		
					<h4>Requirements</h4>
					
					<ul>
						<li>MySQL needs to be able to write to the configured export file path.</li>
						<li>The configured database user needs the FILE permission, which is not database-specific.</li>
					</ul>
				
				
				
				
					<h5>More info:</h5>
					<ul>
						<li><a target="_blank" href="http://en.wikipedia.org/wiki/WAR_file_format_(Sun)">WAR file format (Sun)</a>
						</li>
						<li><a target="_blank" href="http://code.google.com/">Google Code</a>
						</li>
						<li><a target="_blank" href="http://code.google.com/p/cggh/">Google Code project for this application</a>
						</li>
						<li><a target="_blank" href="http://tomcat.apache.org/">Apache Tomcat</a>
						</li>
					</ul>
						
				</div>
				
			</div>
			
		</div>
	</body>
</html>