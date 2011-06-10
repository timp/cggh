<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="../../shared/jsp/prepage.jsp" %>
<%@ page import="org.cggh.tools.dataMerger.files.filebases.FilebasesCRUD" %>
<%@ page import="org.cggh.tools.dataMerger.files.filebases.FilebaseModel" %>
<%

	FilebasesCRUD filebasesCRUD = new FilebasesCRUD();
	FilebaseModel filebaseModel = filebasesCRUD.retrieveFilebaseAsFilebaseModelUsingServletContext(request.getSession().getServletContext());
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>dataMerger - Settings - Filebase</title>
		
		<link rel="stylesheet" type="text/css" href="../../shared/css/shared.css" />
		<link rel="stylesheet" type="text/css" href="../css/settings.css" />
		
		<script type="text/javascript" src="../../shared/js/jquery.min.js"></script>	
		<script type="text/javascript" src="../../shared/js/shared.js"></script>
		<script type="text/javascript" src="js/filebase.js"></script>
		<script type="text/javascript">
		
			$(document).ready(function(){
				
				initSharedFunctions();
				initFilebaseFunctions();
	
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
					
					<h3>Filebase</h3>
				
					<h3>System check:</h3>
					<ul class="systemCheckList">
					
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
									
					<h3>System information:</h3>
					<table class="systemInformationTable">
					
						<tr>
							<th>Filebase version:</th>
							<td><%=filebaseModel.getVersionAsString() %></td>
						</tr>
						<tr>
							<th>Filebase file count:</th>
							<td><%=filebaseModel.getFilesAsStringArrayList().size() %></td>
						</tr>
						<tr>
							<th>Filebase directories count:</th>
							<% if (filebaseModel.getFilesAsStringArrayList().size() == 3) { %>
							<td class="pass"><%=filebaseModel.getFilesAsStringArrayList().size() %></td>
							<% } else { %>
							<td class="fail"><%=filebaseModel.getFilesAsStringArrayList().size() %></td>
							<% } %>
						</tr>
					</table>
					
					<h3>web.xml configuration:</h3>
					<table class="systemInformationTable"> 
						<tr><th>fileRepositoryBasePath:</th><td><%=application.getInitParameter("fileRepositoryBasePath") %></td></tr>
						<tr><th>fileRepositoryInstallationLogPathRelativeToRepositoryBasePath:</th><td><%=application.getInitParameter("fileRepositoryInstallationLogPathRelativeToRepositoryBasePath") %></td></tr>
					</table>
					
					<p class="refreshButtonContainer"><button onclick="window.location.reload(true)">Refresh</button>
					</p>
		
					<div class="ajaxResponse">
					</div>
					
					<div class="ajaxError">
					</div>	
		
		
					<h3>Filebase:</h3>
					<p>
						<img class="creating-filebase-indicator" src="../../shared/gif/loading.gif" style="display:none" title="Creating..."/>
						
						<% if (filebaseModel.isExistent()) { %>
							<button disabled="disabled">Create</button>
							<button class="deleteFilebaseButton">Delete</button>
						<% } else { %>
							<button class="createFilebaseButton">Create</button>
							<button disabled="disabled">Delete</button>
						<% } %>
						
						<img class="deleting-filebase-indicator" src="../../shared/gif/loading.gif" style="display:none" title="Deleting..."/>
					</p>
					<div class="divider-space"></div>
					
					
					<h3>Filebase directories and files:</h3>
					<p>
						<img class="creating-filebase-directories-indicator" src="../../shared/gif/loading.gif" style="display:none" title="Creating..."/>
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
						
						<img class="deleting-filebase-directories-indicator" src="../../shared/gif/loading.gif" style="display:none" title="Deleting..."/>
					</p>
					<div class="divider-space"></div>
				
				</div>
				
			</div>
			

			
		</div>
	</body>
</html>