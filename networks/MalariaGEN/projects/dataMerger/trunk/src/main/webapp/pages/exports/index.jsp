<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ include file="../shared/jsp/prepage.jsp" %>
<% if (userModel != null && userModel.getId() != null) { %>
<%@ page import="org.cggh.tools.dataMerger.data.exports.ExportsCRUD" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>dataMerger - Exports</title>

	<link rel="stylesheet" type="text/css" href="../shared/css/shared.css" />
	
	<link rel="stylesheet" type="text/css" href="css/exports.css" />
	
	<script type="text/javascript" src="../shared/js/jquery.min.js"></script>
	<script type="text/javascript" src="../shared/js/jquery.json.min.js"></script>
	<script type="text/javascript" src="../shared/js/shared.js"></script>
	
	<script type="text/javascript" src="js/exports.js"></script>
	
	<script type="text/javascript">

		$(document).ready(function(){
			
			initSharedFunctions();
			initExportsFunctions();

		});
	
	</script>
	
</head>
<body>
	<div class="page">
	
		<%@ include file="../shared/jsp/header.jsp" %>
		
		<h2 class="page-title">Exports</h2>


		<div class="status">
		</div>	
		<div class="error">
		</div>

		<form class="exports-form" onsubmit="return false;">
		
			<div class="exports">
				<%

					//NOTE: redirects in the prepage file to not stop this code executing.
					if (userModel != null && userModel.getId() != null) {
				
						ExportsCRUD exportsCRUD = new ExportsCRUD();
				
						exportsCRUD.setDatabaseModel(databaseModel);
					
						out.print(exportsCRUD.retrieveExportsAsDecoratedXHTMLTableUsingUserId(userModel.getId()));
						
					}
				%>	
			</div>

		</form>
	
	</div>	

</body>
</html>
<% } %>