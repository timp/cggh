<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%-- Note: This file should not require any database interaction because it is also used on database installation pages, etc. --%>
<%
String sessionLossErrorBasePathURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>dataMerger - Guides - Usage - Errors - Valid session required</title>
		
		<link rel="stylesheet" type="text/css" href="../../../../shared/css/shared.css" />
		<link rel="stylesheet" type="text/css" href="../../../css/guides.css" />
		<script type="text/javascript" src="../../../../shared/js/shared.js"></script>
		
	</head>
	<body>
		<div class="page">
			<%@ include file="../../../../shared/jsp/header.jsp" %>
			<h2 class="page-title">Guides</h2>
			
			<div class="guides-container">
			
				<%@ include file="../../../jsp/guides-menu.jsp" %>
				
				<div class="guide">
					
					<h3>Usage</h3>
		
					<h4>Errors</h4>
					
					<h5>Valid session required</h5>
					
					<p>You are not logged in.</p>
					
					<p>You need to be logged in before you can access the requested page.</p>
					
					<p>If you already logged in before, then your session has now timed-out or become otherwise invalidated.</p>
					
					<p>To access the page you requested, you will need to <a href="<%=sessionLossErrorBasePathURL %>pages/shared/login/">log in</a> and try again.</p>
		
				</div>
				
			</div>
			
		</div>
	</body>
</html>