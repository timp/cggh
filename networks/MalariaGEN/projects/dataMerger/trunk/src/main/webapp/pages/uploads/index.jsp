<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<jsp:useBean id="uploadsModel" class="org.cggh.tools.dataMerger.data.uploads.UploadsModel" scope="page"/>
<jsp:useBean id="functionsModel" class="org.cggh.tools.dataMerger.functions.FunctionsModel" scope="page"/>
<%
uploadsModel.setHttpServletRequest(request);
%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Uploads</title>
	<link rel="stylesheet" type="text/css" href="../shared/css/fileuploader.css" />
	
	<script type="text/javascript" src="../shared/js/jquery.min.js"></script>
	<script type="text/javascript" src="../shared/js/fileuploader.js"></script>
	
</head>
<body>
	<div class="page">
		<%@ include file="../shared/jsp/header.jsp" %>
		<h1>Uploads</h1>

<%

//RowSetDynaClass rsdc = new RowSetDynaClass(rs);

  CachedRowSet uploadsAsCachedRowSet = uploadsModel.getUploadsAsCachedRowSet();

  if (uploadsAsCachedRowSet != null) {

	    functionsModel.setCachedRowSet(uploadsAsCachedRowSet);
	    functionsModel.transformCachedRowSetIntoXHTMLTable();
	    out.print(functionsModel.getXHTMLTable());
	    
	    
	    //dataMerger.scripts.run("install-db-v0.0.1");
	    //dataMerger.scripts.getScript(x).run();
	    
  } else {
	  
	  out.print("<p>Error: uploadsAsCachedRowSet is null</p>");
	  
  }
%>		

		<div id="file-uploader">
		       
        	<noscript>          
            	<p>Please enable JavaScript to use file uploader.</p>
            	<!-- or put a simple form for upload here -->
        	</noscript>
        	
        	         
    	</div>

	</div>	
	<script type="text/javascript">
		var uploader = new qq.FileUploader({

			// pass the dom node (ex. $(selector)[0] for jQuery users)
	        element: document.getElementById('file-uploader'),
	        
	            // url of the server-side upload script, should be on the same domain
	            action: '/dataMerger/files/',
	            // additional data to send, name-value pairs
	            params: {},
	            
	            // validation    
	            // ex. ['jpg', 'jpeg', 'png', 'gif'] or []
	            allowedExtensions: [],        
	            // each file size limit in bytes
	            // this option isn't supported in all browsers
	            sizeLimit: 0, // max size   
	            minSizeLimit: 0, // min size
	            
	            // set to true to output server response to console
	            debug: true,
	            
	            // events         
	            // you can return false to abort submit
	            onSubmit: function(id, fileName){ alert("onSubmit"); },
	            onProgress: function(id, fileName, loaded, total){ alert("onProgress. Loaded: " + loaded); },
	            onComplete: function(id, fileName, responseJSON){ alert("onComplete. Response: " + responseJSON.error); },
	            onCancel: function(id, fileName){ alert("onCancel"); },
	            
	            messages: {
	                // error messages, see qq.FileUploaderBasic for content            
	            },
	            showMessage: function(message){ alert(message); }    
	        
	        
	    }); 
	</script>
</body>
</html>