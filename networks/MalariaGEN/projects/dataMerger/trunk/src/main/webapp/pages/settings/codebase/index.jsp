<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="../../shared/jsp/prepage.jsp" %>
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
		
						<h3>System information:</h3>
						<table class="systemInformationTable">
							<tr>
								<th>Codebase version:</th>
								<td><%=codebaseModel.getVersionAsString() %><td>
							</tr>
						</table>
						
						<div class="clearBoth"></div>
							
				
				</div>
				
			</div>
			

			
		</div>
	</body>
</html>