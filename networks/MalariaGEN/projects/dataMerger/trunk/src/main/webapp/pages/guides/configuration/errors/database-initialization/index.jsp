<%-- Note: This file should not require any database interaction because it is also used on database installation pages, etc. --%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>dataMerger - Guides - Configuration - Errors - Database initialization</title>
		
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
					
					<h5>Database initialization</h5>
					
					<p>The database has not been initialized.</p>
					
					<h6>Possible causes:</h6>
					<ul>
						<li>The database has not been properly installed.</li>
					</ul>	
					
					<p>For application administrators, the database can be initialized using the <a href="/dataMerger//pages/settings/">settings section</a>.
					</p>
		
				</div>
				
			</div>
			
		</div>
	</body>
</html>