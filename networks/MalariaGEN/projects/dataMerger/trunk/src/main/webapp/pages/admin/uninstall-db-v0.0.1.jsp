<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:useBean id="dataController" class="org.cggh.tools.dataMerger.scripts.ScriptsModel" scope="page"/>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Uninstall database v0.0.1</title>
<!-- <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.5/jquery.min.js"></script> -->
	<script type="text/javascript" src="../shared/js/jquery.min.js"></script>
</head>
<body>
	<p><a href="../admin/">Admin menu</a></p>
	<h1>Uninstall database v0.0.1</h1>
	<button class="uninstallButton">Uninstall</button>
	<button onclick="location.reload(true);">Refresh</button>
	<div class="response">
	</div>
	<div class="status">
	</div>	
	<div class="error">
	</div>
	<script type="text/javascript">
	$('.uninstallButton').click(
		function () {

			
			$.ajax({
				  type: "POST",
				  url: "/dataMerger/scripts/uninstall-db-v0.0.1",
				  //data: { tags: "cat", tagmode: "any", format: "json" },
				   
				  dataType: "json",
				  success: function(data, textStatus, jqXHR) {
						$('.response').html("Response: " + data.baz[1]);
						$('.status').html("textStatus: " + textStatus);
					},
					error:function (jqXHR, textStatus, errorThrown){
		                    $('.error').html("errorThrown: " + errorThrown);
		                    $('.status').html("textStatus: " + textStatus);
		                } 
				});
		}
		
	);

	
	</script>
</body>
</html>