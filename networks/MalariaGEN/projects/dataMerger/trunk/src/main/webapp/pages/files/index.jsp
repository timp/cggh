<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ include file="../shared/jsp/prepage.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="org.cggh.tools.dataMerger.data.files.FilesCRUD" %>
<%@ page import="org.cggh.tools.dataMerger.data.files.FileModel" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>dataMerger - Files</title>

	<link rel="stylesheet" type="text/css" href="../shared/css/fileuploader.css" />
	<link rel="stylesheet" type="text/css" href="../shared/css/shared.css" />
	
	<link rel="stylesheet" type="text/css" href="css/files.css" />
	
	<script type="text/javascript" src="../shared/js/jquery.min.js"></script>
	<script type="text/javascript" src="../shared/js/jquery.json.min.js"></script>
	<script type="text/javascript" src="../shared/js/shared.js"></script>
	
	<script type="text/javascript" src="js/fileuploader.js"></script>
	<script type="text/javascript" src="js/files.js"></script>
	
	<script type="text/javascript">

		$(document).ready(function(){
			
			initSharedFunctions();
			initFilesFunctions();

		});
	
	</script>
	
</head>
<body>
	<div class="page">
	
		<%@ include file="../shared/jsp/header.jsp" %>
		
		<h2 class="page-title">Files</h2>

		<div class="status">
		</div>	
		<div class="error">
		</div>

		<form class="files-form" onsubmit="return false;">
		
		<div class="files">
			<%
			
				if (databaseModel.isInitialized() && userModel.getId() != null) {
					FilesCRUD filesCRUD = new FilesCRUD();
					filesCRUD.setDatabaseModel(databaseModel);
					out.print(filesCRUD.retrieveFilesAsDecoratedXHTMLTableUsingUserId(userModel.getId()));
				} else {
					out.print("Code error.");
				}
				
			%>	
		</div>

		</form>

		<hr class="divider-space"/>

		<h3 class="field-title">New uploads:</h3>
		<% if (request.getHeader("User-Agent").indexOf("MSIE") > 0) { %>
		
		<p>Sorry, the upload feature is not currently supported for the Internet Explorer browser.
		</p>
		
		<div id="file-uploader" style="display: none;"></div>
		
		<% } else { %>
		

		<div id="file-uploader">
		       
        	<noscript>          
            	<p>Please enable JavaScript to use the file uploader.</p>
            	<%-- TODO: Put a simple form for upload here --%>
        	</noscript>
        	
        	         
    	</div>
    	<% } %>
	
	</div>	

</body>
</html>