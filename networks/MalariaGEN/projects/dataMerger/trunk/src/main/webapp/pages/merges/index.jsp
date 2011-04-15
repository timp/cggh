<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ include file="../shared/jsp/prepage.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="org.cggh.tools.dataMerger.data.merges.MergesModel" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>dataMerger - Merges</title>

	<link rel="stylesheet" type="text/css" href="../shared/css/shared.css" />

	<link rel="stylesheet" type="text/css" href="css/merges.css" />
	
	<script type="text/javascript" src="../shared/js/jquery.min.js"></script>
	<script type="text/javascript" src="../shared/js/jquery.json.min.js"></script>
	<script type="text/javascript" src="../shared/js/shared.js"></script>
	
	<script type="text/javascript" src="js/merges.js"></script>
	
	<script type="text/javascript">
	
		$(document).ready(function(){
			
			initSharedFunctions();
			initMergesFunctions();
	
		});
		
	</script>
	
</head>
<body>
	<div class="page">
		<%@ include file="../shared/jsp/header.jsp" %>
		
		
		<h2 class="page-title">Merges</h2>


		<div class="status">
		</div>	
		<div class="error">
		</div>

		<form class="merges-form" onsubmit="return false;">
		<div class="merges">
			<%
			
				MergesModel mergesModel = new MergesModel();
			
				mergesModel.setDataModel(dataModel);
				mergesModel.setUserModel(userModel);
				
				out.print(mergesModel.retrieveMergesAsDecoratedXHTMLTableUsingMergesModel(mergesModel));
				
			%>		
		</div>

		</form>

	</div>	

</body>
</html>