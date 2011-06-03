<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="../shared/jsp/prepage.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="org.cggh.tools.dataMerger.data.uploads.UploadsCRUD" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>dataMerger - Uploads</title>

	<!-- NOTE: This makes no difference to the problem of back-button amnesia. -->
	<meta http-equiv="Cache-Control" content="no-cache" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Expires" content="-1" />
	
	<link rel="stylesheet" type="text/css" href="../shared/css/fileuploader.css" />
	<link rel="stylesheet" type="text/css" href="../shared/css/shared.css" />
	
	<link rel="stylesheet" type="text/css" href="css/uploads.css" />
	
	<script type="text/javascript" src="../shared/js/jquery.min.js"></script>
	<script type="text/javascript" src="../shared/js/jquery.json.min.js"></script>
	<script type="text/javascript" src="../shared/js/shared.js"></script>
	
	<script type="text/javascript" src="js/fileuploader.js"></script>
	<script type="text/javascript" src="js/uploads.js"></script>
	
	<script type="text/javascript">

		$(document).ready(function(){
			
			//FIXME: Moved calls to load, in an attempt to fix FF back-button amnesia.
			//http://support.mozilla.com/en-US/questions/779617

		});
		
		//NOTE: This makes no difference to the problem of back-button amnesia.
		
		$(window).load(function() {
			
            $.ajaxSetup({cache: false});

            initSharedFunctions();
			initUploadsFunctions();
            
        });
	
	</script>
	
</head>
<body>
	<div class="page">
	
		<%@ include file="../shared/jsp/header.jsp" %>
		
		<h2 class="page-title">Uploads</h2>


		<div class="status">
		</div>	
		<div class="error">
		</div>

		<form class="uploads-form" onsubmit="return false;">
		
		<div class="uploads">
		
			<% 
			
			if (databaseModel.isInitialized()) {
				
				UploadsCRUD uploadsCRUD = new UploadsCRUD();
				uploadsCRUD.setDatabaseModel(databaseModel);
			
				out.print(uploadsCRUD.retrieveUploadsAsDecoratedXHTMLTableUsingUserId(userModel.getId()));
			
			} else {
				//NOTE: This message will not be displayed because the page will be redirected by the prepage logic.
				//The redirect will not work if there is a NULL pointer exception when CRUD is attempted without a database connection.
				out.print("Database is not initialized");
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