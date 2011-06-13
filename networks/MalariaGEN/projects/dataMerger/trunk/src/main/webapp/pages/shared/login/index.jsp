<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>dataMerger - Login</title>
		
		<link rel="stylesheet" type="text/css" href="../../shared/css/shared.css" />
		<link rel="stylesheet" type="text/css" href="css/login.css" />
		
		<script type="text/javascript" src="../../shared/js/jquery.min.js"></script>
		<script type="text/javascript" src="../../shared/js/jquery.json.min.js"></script>
		<script type="text/javascript" src="../../shared/js/shared.js"></script>
		
		<script type="text/javascript" src="js/login.js"></script>
	
		<script type="text/javascript">
	
			$(document).ready(function(){
				
				initSharedFunctions();
				initLoginFunctions();
	
				document.getElementById("loginForm").username.focus();
			});
		
		</script>
		
	</head>
	<body>
		<div class="page">
		
			<%@ include file="../../shared/jsp/header.jsp" %>
		
			<h2 class="page-title">Login</h2>

			<div class="status">
			</div>	
			<div class="error">
			</div>
			
			<div class="badLoginMessage" style="display:none">
				<p class="error">Access denied.
				</p>
			
			</div>

			<p>To enter, please complete the form below.
			</p>
			
			<form id="loginForm" class="loginForm" onsubmit="return false;">
				
				<table>
				
				<tr><th><label>Username:</label></th><td><input type="text" name="username" /></td>
				</tr>
				
				<tr><th><label>Password:</label></th><td><input type="password" name="password" /></td>
				</tr>
			
				<tr><td></td><td><button class="loginButton">Login</button><img class="authenticating-indicator" src="../../shared/gif/loading.gif" style="display:none" title="Authenticating..."/></td>
				</tr>
				
				</table>
			
			</form>
			
			
		</div>
	</body>
</html>