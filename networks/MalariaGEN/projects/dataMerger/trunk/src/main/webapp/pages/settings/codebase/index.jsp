<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%-- Note: Shouldn't have the prepage include. --%>
<%@ page import="org.cggh.tools.dataMerger.code.codebases.CodebaseModel" %>
<%@ page import="org.cggh.tools.dataMerger.data.databases.DatabasesCRUD" %>
<%@ page import="org.cggh.tools.dataMerger.data.databases.DatabaseModel" %>
<%@ page import="org.cggh.tools.dataMerger.code.settings.SettingsCRUD" %>
<%@ page import="org.cggh.tools.dataMerger.code.settings.SettingsModel" %>
<%
	CodebaseModel codebaseModel = new CodebaseModel();

	DatabasesCRUD databasesCRUD = new DatabasesCRUD();
	DatabaseModel databaseModel = databasesCRUD.retrieveDatabaseAsDatabaseModelUsingServletContext(getServletContext());
	
	SettingsCRUD settingsCRUD = new SettingsCRUD();
	settingsCRUD.setDatabaseModel(databaseModel);
	SettingsModel settingsModel = settingsCRUD.retrieveSettingsAsSettingsModel();
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>dataMerger - Settings - Codebase</title>
		
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
					
					<h3>Codebase</h3>
		
					<h4>System check:</h4>
					<ul class="systemCheckList">
						
						<% if (databaseModel.isInitialized()) { %>
						<li class="pass">Database is initialized.</li>
						<% } else { %>
						<li class="fail">Database is not initialized.</li>
						<% } %>
						
					</ul>
		
		
						<h4>System information:</h4>
						<table class="systemInformationTable">
							<tr>
								<th>Codebase version:</th>
								<td><%=codebaseModel.getVersionAsString() %><td>
							</tr>
						</table>
						
					
						
					<h4>Settings</h4>
					<% if (databaseModel.isInitialized()) { %>
					<table class="systemInformationTable"> 
						<tr><th>stringsToNullifyAsCSV:</th><td><%=settingsModel.getSettingsAsHashMap().get("stringsToNullifyAsCSV") %></td></tr>
						<tr><th>stringToExportInsteadOfNull:</th><td><%=settingsModel.getSettingsAsHashMap().get("stringToExportInsteadOfNull") %></td></tr>
					</table>
					<% } else { %>
					<p>Settings data are not retrievable.</p>
					<% } %>
							
				
				</div>
				
			</div>
			

			
		</div>
	</body>
</html>