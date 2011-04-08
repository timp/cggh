<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="../shared/jsp/prepage.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="org.cggh.tools.dataMerger.data.uploads.UploadsModel" %>
<%

	UploadsModel uploadsModel = new UploadsModel();

	uploadsModel.setDataModel(dataModel);
	uploadsModel.setUserModel(userModel);

%>	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>dataMerger - Uploads</title>

	
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
			<%=uploadsModel.retrieveUploadsAsDecoratedXHTMLTableUsingUploadsModel(uploadsModel) %>		
		</div>

		</form>
		
		<hr class="divider-space"/>


		<h3 class="field-title">New uploads:</h3>

		<div id="file-uploader">
		       
        	<noscript>          
            	<p>Please enable JavaScript to use the file uploader.</p>
            	<%-- Put a simple form for upload here --%>
        	</noscript>
        	
        	         
    	</div>

		<%--

		<p>FIXME: Fix re-merge datatabled uploads.
		</p>

		<p>FIXME: Add processing animation upon pressing the merge button.
		</p>

		<p>FIXME: Fix JavaScript error: Unexpected token in attribute selector: '!'.
		</p>
		
 		--%>
	</div>	

</body>
</html>