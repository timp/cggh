<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="../../shared/jsp/prepage.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>dataMerger - Settings - Userbase</title>
		
		<link rel="stylesheet" type="text/css" href="../../shared/css/shared.css" />
		<link rel="stylesheet" type="text/css" href="../css/settings.css" />
		
		<script type="text/javascript" src="../../shared/js/shared.js"></script>
		
	</head>
	<body>
		<div class="page">
			<%@ include file="../../shared/jsp/header.jsp" %>
			<h2 class="page-title">Settings</h2>
			
			<div class="settings-container">
			
				<%@ include file="../jsp/settings-sections-menu.jsp" %>
				
				<div class="settings-section">
					
					<h3>Userbase</h3>
		
					<h3>System check:</h3>
					<ul class="systemCheckList">
						
						<%--
					
						<% if (userbaseModel.isServerConnectable()) { %>
						<li class="pass">Userbase server is connectable.</li>
						<% } else { %>
						<li class="fail">Userbase server is not connectable.</li>
						<% } %>
					
						<% if (userbaseModel.isConnectable()) { %>
						<li class="pass">Userbase is connectable.</li>
						<% } else { %>
						<li class="fail">Userbase is not connectable.</li>
						<% } %>
						
						<% if (userbaseModel.getTablesAsCachedRowSet().size() >= 1) { %>
						<li class="pass">Userbase tables count &gt;= 1.</li>
						<% } else { %>
						<li class="fail">Userbase tables count &lt; 1.</li>
						<% } %>
							
						 --%>	
						
					</ul>
					
					<ul>
						<li>Add user, list users, edit user (if poss), username/password/permissions. TODO: Add permissions + user_permissions tables. userbase password hash algorithm (select, auto-determine).</li>
					</ul>
				
				</div>
				
			</div>
			

			
		</div>
	</body>
</html>