<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="../shared/jsp/prepage.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="org.cggh.tools.dataMerger.data.uploads.UploadsModel" %>
<%@ page import="org.cggh.tools.dataMerger.functions.uploads.UploadsFunctionsModel" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Uploads</title>
	<link rel="stylesheet" type="text/css" href="../shared/css/fileuploader.css" />
	
	<script type="text/javascript" src="../shared/js/jquery.min.js"></script>
	<script type="text/javascript" src="../shared/js/fileuploader.js"></script>
	<script type="text/javascript" src="../shared/js/jquery.json.min.js"></script>
	
	<script type="text/javascript" src="../shared/js/shared.js"></script>
	<script type="text/javascript" src="../shared/js/uploads.js"></script>
	<script type="text/javascript" src="../shared/js/merges.js"></script>
	
	<script type="text/javascript">
	
		window.onload = init;
		
		function init () {
			
			initSharedFunctions();
			initUploadsFunctions();
			initMergesFunctions();

		}
	
	</script>
	
</head>
<body>
	<div class="page">
		<%@ include file="../shared/jsp/header.jsp" %>
		<h2>Uploads</h2>


		<div class="status">
		</div>	
		<div class="error">
		</div>

		<form class="uploads-form" onsubmit="return false;">
		<div class="uploads">
<%

	UploadsModel uploadsModel = new UploadsModel();

	uploadsModel.setDataModel(dataModel);

  CachedRowSet uploadsAsCachedRowSet = uploadsModel.retrieveUploadsAsCachedRowSetUsingUserId(userModel.getId());

  if (uploadsAsCachedRowSet != null) {

	  	UploadsFunctionsModel uploadsFunctionsModel = new UploadsFunctionsModel();
	    uploadsFunctionsModel.setCachedRowSet(uploadsAsCachedRowSet);
	    uploadsFunctionsModel.setDecoratedXHTMLTableByCachedRowSet();
	    out.print(uploadsFunctionsModel.getDecoratedXHTMLTable());
	    
  } else {
	  
	  //TODO: Error handling
	  out.print("<p>Error: uploadsAsCachedRowSet is null</p>");
	  
  }
%>		
		</div>

		</form>
		

		<h3>New upload:</h3>

		<div id="file-uploader">
		       
        	<noscript>          
            	<p>Please enable JavaScript to use the file uploader.</p>
            	<%-- Put a simple form for upload here --%>
        	</noscript>
        	
        	         
    	</div>

		<p>TODO: Fix JavaScript error: Unexpected token in attribute selector: '!'.
		</p>
		
		<p>TODO: Add processing animation upon pressing the merge button.
		</p>

	</div>	

</body>
</html>