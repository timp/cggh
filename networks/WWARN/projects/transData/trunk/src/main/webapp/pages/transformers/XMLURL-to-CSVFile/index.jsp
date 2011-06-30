<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
	<title>transData - Transformers - XML URL to CSV File</title>
	
	<script type="text/javascript" src="../../shared/js/jquery.min.js"></script>
	<script type="text/javascript" src="../../shared/js/jquery.json.min.js"></script>
	<script type="text/javascript" src="../../shared/js/shared.js"></script>
	
	<script type="text/javascript" src="js/XMLURL-to-CSVFile.js"></script>
	<script type="text/javascript">

		$(document).ready(function(){
			
			initSharedFunctions();
			initXMLURL_to_CSVFileFunctions();

		});
	
	</script>
</head>
<body>

	<h1>transData</h1>
	
	<h2>Transformers</h2>
	
	<h3>XML URL to CSV File</h3>

	<div class="error"></div>

	<form class="new-transformation-form" onsubmit="return false;" autocomplete="off">
		<h3>New transformation:</h3>
		<div>
		<label>URL:</label>
			<input type="text" name="URL" />
			<button class="transformXMLURL-to-CSVRowsFileButton">Rows</button>
			<img class="transforming-indicator" src="../../shared/gif/loading.gif" style="display:none" title="Tranforming..."/>
		</div>
	</form>

</body>
</html>