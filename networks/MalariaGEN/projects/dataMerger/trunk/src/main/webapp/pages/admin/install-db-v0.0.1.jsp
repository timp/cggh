<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Install database v0.0.1</title>
	<script type="text/javascript" src="../shared/js/jquery.min.js"></script>
</head>
<body>
	<p><a href="../admin/">Admin menu</a></p>
	<h1>Install database v0.0.1</h1>
	<button class="installButton">Install</button>
	<button onclick="location.reload(true);">Refresh</button>
	<div class="response">
	</div>
	<div class="status">
	</div>	
	<div class="error">
	</div>
	<script type="text/javascript">
	$('.installButton').click(
		function () {

			
			$.ajax({
				  type: "POST",
				  url: "/dataMerger/scripts/install-db-v0.0.1",
				  //data: { tags: "cat", tagmode: "any", format: "json" },
				   
				  dataType: "text",
				  success: function(data, textStatus, jqXHR) {
						$('.response').html("Response: " + data);
						$('.status').html("textStatus: " + textStatus);
					},
					error:function (jqXHR, textStatus, errorThrown){
							$('.response').html("Response: " + data);
		                    $('.error').html("errorThrown: " + errorThrown);
		                    $('.status').html("textStatus: " + textStatus);
		                } 
				});
		}
		
	);

	
	</script>
</body>
</html>