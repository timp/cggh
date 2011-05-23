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
			
			<div class="divider-space"></div>
			
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
				
				<% if (databaseModel.getTablesAsCachedRowSet().size() >= 11) { %>
				<li class="pass">Database tables count &gt;= 11.</li>
				<% } else { %>
				<li class="fail">Database tables count &lt; 11.</li>
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

			
			<div class="divider-space"></div>
			
			<h3>System information:</h3>
			<table class="systemInformationTable">
				<tr>
					<th>Codebase version:</th>
					<td><%=codebaseModel.getVersionAsString() %><td>
				</tr>
				<tr>	
					<th>Database version:</th>
					<% if (databaseModel.getVersionAsString() != null) { %>
					<td class="pass"><%=databaseModel.getVersionAsString() %><td>
					<% } else { %>
					<td class="fail"><%=databaseModel.getVersionAsString() %><td>
					<% } %>
				</tr>
				<tr>
					<th>Database tables count:</th>
					<% if (databaseModel.getTablesAsCachedRowSet().size() >= 11) { %>
					<td class="pass"><%=databaseModel.getTablesAsCachedRowSet().size() %><td>
					<% } else { %>
					<td class="fail"><%=databaseModel.getTablesAsCachedRowSet().size() %><td>
					<% } %>
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
			
			<div class="divider-space"></div>
			
			<h3>Database:</h3>
			
			<p>
				<img class="creating-database-indicator" src="../shared/gif/loading.gif" style="display:none" title="Creating..."/>
				<button class="createDatabaseButton">Create</button>
				<button class="deleteDatabaseButton">Delete</button>
				<img class="deleting-database-indicator" src="../shared/gif/loading.gif" style="display:none" title="Deleting..."/>
			</p>
			<div class="divider-space"></div>
		
			<h3>Database Tables:</h3>
			
			<p>
				<img class="creating-database-tables-indicator" src="../shared/gif/loading.gif" style="display:none" title="Creating..."/>
				<button class="createAndInitializeTablesButton">Create and Initialize</button>
				<button class="deleteDatabaseTablesButton">Delete</button>
				<img class="deleting-database-tables-indicator" src="../shared/gif/loading.gif" style="display:none" title="Deleting..."/>
			</p>
			<div class="divider-space"></div>
			
			<h3>Filebase:</h3>
			
			<p>
				<img class="creating-filebase-indicator" src="../shared/gif/loading.gif" style="display:none" title="Creating..."/>
				<button class="createFilebaseButton">Create</button>
				<button class="deleteFilebaseButton">Delete</button>
				<img class="deleting-filebase-indicator" src="../shared/gif/loading.gif" style="display:none" title="Deleting..."/>
			</p>
			<div class="divider-space"></div>
			
		</div>

	</body>
</html>