<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%-- Note: Shouldn't have the prepage include. --%>
<%@ page import="org.cggh.tools.dataMerger.data.databases.DatabasesCRUD" %>
<%@ page import="org.cggh.tools.dataMerger.data.databases.DatabaseModel" %>
<%
	DatabasesCRUD databasesCRUD = new DatabasesCRUD();
	DatabaseModel databaseModel = databasesCRUD.retrieveDatabaseAsDatabaseModelUsingServletContext(getServletContext());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>dataMerger - Settings - Database</title>
		
		<link rel="stylesheet" type="text/css" href="../../shared/css/shared.css" />
		<link rel="stylesheet" type="text/css" href="../css/settings.css" />
		
		<script type="text/javascript" src="../../shared/js/jquery.min.js"></script>	
		<script type="text/javascript" src="../../shared/js/shared.js"></script>
		<script type="text/javascript" src="js/database.js"></script>
		<script type="text/javascript">
		
			$(document).ready(function(){
				
				initSharedFunctions();
				initDatabaseFunctions();
	
			});
		
		</script>
		
	</head>
	<body>
		<div class="page">
			<%@ include file="../../shared/jsp/header.jsp" %>
			<h2 class="page-title">Settings</h2>
			
			<div class="settings-container">
			
				<%@ include file="../jsp/settings-sections-menu.jsp" %>
				
				<div class="settings-section">
					
					<h3>Database</h3>


					<h3>System check:</h3>
					<ul class="systemCheckList">
					
						<% if (databaseModel.isServerConnectable()) { %>
						<li class="pass">Database server is connectable.</li>
						<% } else { %>
						<li class="fail">Database server is not connectable.</li>
						<% } %>
					
						<% if (databaseModel.isConnectable()) { %>
						<li class="pass">Database is connectable.</li>
						<% } else { %>
						<li class="fail">Database is not connectable.</li>
						<% } %>
						
						<% if (databaseModel.getTablesAsCachedRowSet().size() >= 13) { %>
						<li class="pass">Database tables count &gt;= 13.</li>
						<% } else { %>
						<li class="fail">Database tables count &lt; 13.</li>
						<% } %>
								
						
					</ul>
				
					<h3>System information:</h3>
					<table class="systemInformationTable">

						<tr>	
							<th>Database version:</th>
							<% if (databaseModel.getVersionAsString() != null) { %>
							<td class="pass"><%=databaseModel.getVersionAsString() %></td>
							<% } else { %>
							<td class="fail"><%=databaseModel.getVersionAsString() %></td>
							<% } %>
						</tr>
						<tr>
							<th>Database tables count:</th>
							<% if (databaseModel.getTablesAsCachedRowSet().size() >= 13) { %>
							<td class="pass"><%=databaseModel.getTablesAsCachedRowSet().size() %></td>
							<% } else { %>
							<td class="fail"><%=databaseModel.getTablesAsCachedRowSet().size() %></td>
							<% } %>
						</tr>
					
					</table>
					
				
					<h3>web.xml configuration:</h3>
					<table class="systemInformationTable"> 
						<tr><th>databaseDriverFullyQualifiedClassName:</th><td><%=application.getInitParameter("databaseDriverFullyQualifiedClassName") %></td></tr>
						<tr><th>databaseServerPath:</th><td><%=application.getInitParameter("databaseServerPath") %></td></tr>
						<tr><th>databaseName:</th><td><%=application.getInitParameter("databaseName") %></td></tr>
						<tr><th>databaseUsername:</th><td><%=application.getInitParameter("databaseUsername") %></td></tr>
					</table>
				
				
					<p class="refreshButtonContainer"><button onclick="window.location.reload(true)">Refresh</button>
					</p>
		
					<div class="ajaxResponse">
					</div>
					
					<div class="ajaxError">
					</div>	
		
					<h3>Database:</h3>
					<p>
						<img class="creating-database-indicator" src="../../shared/gif/loading.gif" style="display:none" title="Creating..."/>
						<% if (databaseModel.isServerConnectable() && !databaseModel.isConnectable()) { %>
							<button class="createDatabaseButton">Create</button>
						<% } else { %>
							<button disabled="disabled">Create</button>
						<% } %>
						<% if (databaseModel.isConnectable()) { %>
							<button class="deleteDatabaseButton">Delete</button>
						<% } else { %>
							<button disabled="disabled">Delete</button>
						<% } %>
						<img class="deleting-database-indicator" src="../../shared/gif/loading.gif" style="display:none" title="Deleting..."/>
					</p>
					<div class="divider-space"></div>
				
				
					<h3>Database tables and data:</h3>	
					<p>
						<img class="creating-database-tables-indicator" src="../../shared/gif/loading.gif" style="display:none" title="Creating..."/>
						<% if (databaseModel.isConnectable() && (databaseModel.getTableNamesAsStringArrayList() == null || databaseModel.getTableNamesAsStringArrayList().size() == 0)) { %>
							<button class="createAndInitializeDatabaseTablesButton">Create and Initialize</button>
						<% } else { %>
							<button disabled="disabled">Create and Initialize</button>
						<% } %>
						<% if (databaseModel.isConnectable() && databaseModel.getTableNamesAsStringArrayList() != null && databaseModel.getTableNamesAsStringArrayList().size() > 0) { %>
							<button class="deleteDatabaseTablesButton">Delete</button>
						<% } else { %>
							<button disabled="disabled">Delete</button>
						<% } %>
						
						<img class="deleting-database-tables-indicator" src="../../shared/gif/loading.gif" style="display:none" title="Deleting..."/>
					</p>
					<div class="divider-space"></div>
				
				
				</div>
				
			</div>
			

			
		</div>
	</body>
</html>