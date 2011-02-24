<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="../shared/jsp/prepage.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<jsp:useBean id="uploadsModel" class="org.cggh.tools.dataMerger.data.uploads.UploadsModel" scope="page"/>
<jsp:useBean id="uploadsFunctionsModel" class="org.cggh.tools.dataMerger.functions.uploads.UploadsFunctionsModel" scope="page"/>
<%

uploadsModel.setDataModel(dataModel);
uploadsModel.setUserModel(userModel);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Uploads</title>
	<link rel="stylesheet" type="text/css" href="../shared/css/fileuploader.css" />
	
	<script type="text/javascript" src="../shared/js/jquery.min.js"></script>
	<script type="text/javascript" src="../shared/js/fileuploader.js"></script>
	<script type="text/javascript" src="../shared/js/jquery.json.min.js"></script>
	<script type="text/javascript" src="../shared/js/uploads.js"></script>
	<script type="text/javascript" src="../shared/js/merges.js"></script>
	
	<script type="text/javascript">
	
		window.onload = init;
		
		function init () {
			
			initUploadsScripts();
			
			var uploader = new qq.FileUploader({

				// pass the dom node (ex. $(selector)[0] for jQuery users)
		        element: document.getElementById('file-uploader'),
		        
		            // url of the server-side upload script, should be on the same domain
		            action: '/dataMerger/files/uploads',
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
		            onSubmit: function(id, fileName){},
		            onProgress: function(id, fileName, loaded, total){},
		            onComplete: function(id, fileName, responseJSON){ getUploads(); },
		            onCancel: function(id, fileName){},
		            
		            messages: {
		                // error messages, see qq.FileUploaderBasic for content            
		            },
		            showMessage: function(message){ alert(message); }    
		        
		        
		    }); 			

		
			
			
			
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

		<form class="uploads-form">
		<div class="uploads">
<%

  CachedRowSet uploadsAsCachedRowSet = uploadsModel.getUploadsAsCachedRowSet();

  if (uploadsAsCachedRowSet != null) {

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

		<p>TODO: Fix non-response on merge button first press.
		</p>

	</div>	

</body>
</html>