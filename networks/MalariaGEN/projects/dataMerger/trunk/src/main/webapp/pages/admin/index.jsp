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
				
				<% if (databaseModel.getTablesAsCachedRowSet().size() >= 15) { %>
				<li class="pass">Database tables count &gt;= 15.</li>
				<% } else { %>
				<li class="fail">Database tables count &lt; 15.</li>
				<% } %>
				
				<% if (filebaseModel.isExistent()) { %>
				<li class="pass">Filebase exists.</li>
				<% } else { %>
				<li class="fail">Filebase does not exist.</li>
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
				
				<% if (filebaseModel.getFilesAsStringArrayList().size() == 3) { %>
				<li class="pass">Filebase directories count == 3.</li>
				<% } else { %>
				<li class="fail">Filebase directories count != 3.</li>
				<% } %>				
				
			</ul>

			<p class="refreshButtonContainer"><button onclick="window.location.reload(true)">Refresh</button>
			</p>

			<div class="ajaxResponse">
			</div>
			
			<div class="ajaxError">
			</div>		
			
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
					<% if (databaseModel.getTablesAsCachedRowSet().size() >= 15) { %>
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
					<td><%=filebaseModel.getFilesAsStringArrayList().size() %><td>
				</tr>
				<tr>
					<th>Filebase directories count:</th>
					<% if (filebaseModel.getFilesAsStringArrayList().size() == 3) { %>
					<td class="pass"><%=filebaseModel.getFilesAsStringArrayList().size() %><td>
					<% } else { %>
					<td class="fail"><%=filebaseModel.getFilesAsStringArrayList().size() %><td>
					<% } %>
				</tr>
			</table>
			
			<div class="clearBoth"></div>
			
			<div class="divider-space"></div>
			
			<h3>Database:</h3>
			
			<p>
				<img class="creating-database-indicator" src="../shared/gif/loading.gif" style="display:none" title="Creating..."/>
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
				<img class="deleting-database-indicator" src="../shared/gif/loading.gif" style="display:none" title="Deleting..."/>
			</p>
			<div class="divider-space"></div>
		
			<h3>Database tables and data:</h3>
			
			<p>
				<img class="creating-database-tables-indicator" src="../shared/gif/loading.gif" style="display:none" title="Creating..."/>
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
				
				<img class="deleting-database-tables-indicator" src="../shared/gif/loading.gif" style="display:none" title="Deleting..."/>
			</p>
			<div class="divider-space"></div>
			
			<h3>Filebase:</h3>
			
			<p>
				<img class="creating-filebase-indicator" src="../shared/gif/loading.gif" style="display:none" title="Creating..."/>
				
				<% if (filebaseModel.isExistent()) { %>
					<button disabled="disabled">Create</button>
					<button class="deleteFilebaseButton">Delete</button>
				<% } else { %>
					<button class="createFilebaseButton">Create</button>
					<button disabled="disabled">Delete</button>
				<% } %>
				
				<img class="deleting-filebase-indicator" src="../shared/gif/loading.gif" style="display:none" title="Deleting..."/>
			</p>
			<div class="divider-space"></div>
			
			<!-- TODO: code -->
			
			<h3>Filebase directories and files:</h3>
			
			<p>
				<img class="creating-filebase-directories-indicator" src="../shared/gif/loading.gif" style="display:none" title="Creating..."/>
				<% if (databaseModel.isInitialized() && filebaseModel.isExistent() && filebaseModel.isWritable() && filebaseModel.getFilesAsStringArrayList().size() == 0) { %>
					<button class="createFilebaseDirectoriesButton">Create and Initialize</button>
				<% } else { %>
					<button disabled="disabled">Create and Initialize</button>
				<% } %>
				<% if (filebaseModel.isExistent() && filebaseModel.isWritable() && filebaseModel.getFilesAsStringArrayList().size() > 0) { %>
					<button class="deleteFilebaseDirectoriesButton">Delete</button>
				<% } else { %>
					<button disabled="disabled">Delete</button>
				<% } %>
				
				<img class="deleting-filebase-directories-indicator" src="../shared/gif/loading.gif" style="display:none" title="Deleting..."/>
			</p>
			<div class="divider-space"></div>
			
		</div>

	</body>
</html>