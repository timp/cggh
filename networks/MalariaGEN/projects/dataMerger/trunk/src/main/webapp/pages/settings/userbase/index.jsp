<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%-- Note: Shouldn't have the prepage include. --%>
<%@ page import="org.cggh.tools.dataMerger.data.users.userbases.UserbasesCRUD" %>
<%@ page import="org.cggh.tools.dataMerger.data.users.userbases.UserbaseModel" %>
<%
	UserbasesCRUD userbasesCRUD = new UserbasesCRUD();
	UserbaseModel userbaseModel = userbasesCRUD.retrieveUserbaseAsUserbaseModelUsingServletContext(getServletContext());
%>
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
						
						<% if (userbaseModel.isDatabaseServerConnectable()) { %>
						<li class="pass">Userbase server is connectable.</li>
						<% } else { %>
						<li class="fail">Userbase server is not connectable.</li>
						<% } %>
					
						<% if (userbaseModel.isDatabaseConnectable()) { %>
						<li class="pass">Userbase is connectable.</li>
						<% } else { %>
						<li class="fail">Userbase is not connectable.</li>
						<% } %>
						
						<% if (userbaseModel.isDatabaseDataRetrievable()) { %>
						<li class="pass">Userbase data is retrievable.</li>
						<% } else { %>
						<li class="fail">Userbase data is not retrievable.</li>
						<% } %>
							
					</ul>
					
					<p>TODO: list of users
					</p>
					
					<p>TODO: add user (skip edit/delete user)
					</p>
				
				</div>
				
			</div>
			

			
		</div>
	</body>
</html>