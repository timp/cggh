<%-- Note: This file should not require any database interaction because it is also used on database installation pages, etc. --%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>dataMerger - Guides - Installation</title>
		
		<link rel="stylesheet" type="text/css" href="../shared/css/shared.css" />
		<link rel="stylesheet" type="text/css" href="css/guides.css" />
		
		<script type="text/javascript" src="../shared/js/jquery.min.js"></script>
		<script type="text/javascript" src="../shared/js/shared.js"></script>
		<script type="text/javascript">
	
			$(document).ready(function(){
				initSharedFunctions();
			});
		
		</script>
		
	</head>
	<body>
		<div class="page">
			<%@ include file="../shared/jsp/header.jsp" %>
			<h2 class="page-title">Guides</h2>

			<p>To view a guide, click on one of the tabs below.
			</p>
			
			<div class="divider-space"></div>

			<div class="intro">

				<div class="guides-container">
				
					<%@ include file="jsp/guides-menu.jsp" %>
					
					<div class="guide">
						
						
							
					</div>
					
				</div>
				
			</div>
			
		</div>
	</body>
</html>