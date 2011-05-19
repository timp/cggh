<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="../shared/jsp/prepage.jsp" %>
<%@ page import="org.cggh.tools.dataMerger.files.filebases.FilebasesCRUD" %>
<%@ page import="org.cggh.tools.dataMerger.files.filebases.FilebaseModel" %>
<%@ page import="org.cggh.tools.dataMerger.code.codebases.CodebaseModel" %>
<%

	FilebasesCRUD filebasesCRUD = new FilebasesCRUD();
	FilebaseModel filebaseModel = filebasesCRUD.retrieveFilebaseAsFilebaseModelUsingServletContext(request.getSession().getServletContext());
	
	CodebaseModel codebaseModel = new CodebaseModel();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>dataMerger - Admin</title>
		
		<link rel="stylesheet" type="text/css" href="../shared/css/shared.css" />
		<link rel="stylesheet" type="text/css" href="css/admin.css" />
		<link rel="stylesheet" type="text/css" href="../guides/css/guides.css" />
		
		<script type="text/javascript" src="../shared/js/jquery.min.js"></script>		
		<script type="text/javascript" src="../shared/js/shared.js"></script>
		<script type="text/javascript" src="js/admin.js"></script>
		<script type="text/javascript">
		
			$(document).ready(function(){
				
				initSharedFunctions();
				initAdminFunctions();
	
			});
		
		</script>
	</head>
	<body>
		<div class="page">

			<%@ include file="../shared/jsp/header.jsp"%>
			
			<h2 class="page-title">Admin</h2>
			
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
				
				<% if (filebaseModel.isReadable()) { %>
				<li class="pass">Filebase is readable.</li>
				<% } else { %>
				<li class="fail">Filebase is not readable.</li>
				<% } %>
				
				<% if (filebaseModel.isWritable()) { %>
				<li class="pass">Filebase is writable.</li>
				<% } else { %>
				<li class="fail">Filebase is not writable.</li>
				<% } %>
			</ul>


			<div class="ajaxResponse">
			</div>
			
			<div class="ajaxError">
			</div>		
			
			<% if (databaseModel.isServerConnectable() && !databaseModel.isConnectable()) { %>
			<div><button class="create-database-button">Create database</button><img class="creating-database-indicator" src="../shared/gif/loading.gif" style="display:none" title="Processing..."/>
			</div>
			<% } %>

			<%-- TODO: code --%>
			<%-- If filebase some condition, show button to create filebase --%>
			<% if (!filebaseModel.isExistent() && filebaseModel.isWritable()) { %>
			<div><button class="create-filebase-button">Create filebase</button><img class="creating-filebase-indicator" src="../shared/gif/loading.gif" style="display:none" title="Processing..."/>
			</div>
			<% } %>

			
			<h3>System information:</h3>
			<table class="systemInformationTable">
				<tr>
					<th>Codebase version:</th>
					<td><%=codebaseModel.getVersionAsString() %><td>
				</tr>
				<tr>	
					<th>Database version:</th>
					<td><%=databaseModel.getVersionAsString() %><td>
				</tr>
				<tr>
					<th>Database tables count:</th>
					<td><%=databaseModel.getTablesAsCachedRowSet().size() %><td>
				</tr>
				<tr>
					<th>Filebase version:</th>
					<td><%=filebaseModel.getVersionAsString() %><td>
				</tr>
				<tr>
					<th>Filebase file count:</th>
					<td><%=filebaseModel.getFilesAsStringArray().length %><td>
				</tr>
			</table>
			
			<div class="clearBoth"></div>
			
			<h3>Version management:</h3>
			
			<h4>v1.0</h4>
			<p>
				<button class="install-1_0-button">Install</button>
				<button class="uninstall-1_0-button">Uninstall</button>
				
			</p>
			
			<h4>v1.1</h4>
			<p>
				<button class="upgrade-1_0-to-1_1-button">Upgrade from v1.0</button>
				<button class="install-1_1-button">Install</button>
				<button class="uninstall-1_1-button">Uninstall</button>
			</p>
		
			
		</div>

	</body>
</html>