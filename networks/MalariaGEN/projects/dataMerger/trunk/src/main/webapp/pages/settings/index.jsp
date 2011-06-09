<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="../shared/jsp/prepage.jsp" %>
<%@ page import="org.cggh.tools.dataMerger.files.filebases.FilebasesCRUD" %>
<%@ page import="org.cggh.tools.dataMerger.files.filebases.FilebaseModel" %>
<%@ page import="org.cggh.tools.dataMerger.code.codebases.CodebaseModel" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>dataMerger - Admin</title>
		
		<link rel="stylesheet" type="text/css" href="../shared/css/shared.css" />
		<link rel="stylesheet" type="text/css" href="css/settings.css" />
		<link rel="stylesheet" type="text/css" href="../guides/css/guides.css" />
		
		<script type="text/javascript" src="../shared/js/jquery.min.js"></script>		
		<script type="text/javascript" src="../shared/js/shared.js"></script>

	</head>
	<body>
		<div class="page">

			<%@ include file="../shared/jsp/header.jsp"%>
			
			<h2 class="page-title">Settings</h2>
			
			
			<p>To view and edit settings, click on one of the tabs below.
			</p> 
			
			<div class="divider-space"></div>
			

				<div class="settings-container">
				
					<%@ include file="jsp/settings-sections-menu.jsp" %>
					
					<div class="settings-section">
						
										

							
					</div>
					
				</div>
			


			

			
			
			
			
		</div>

	</body>
</html>