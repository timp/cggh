<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Admin</title>
</head>
<body>
	<div class="page">
		<!-- TODO: Can't use header if there is no database! Currently tries to use user info, etc. -->
		<%--@ include file="../shared/jsp/header.jsp" --%>
		<h2>Admin</h2>
		<ul>
			<li><a href="install-db-v0.0.1.jsp">Install database v0.0.1</a>
			</li>
			<li><a href="uninstall-db-v0.0.1.jsp">Uninstall database v0.0.1</a>
			</li>
		</ul>
	</div>
</body>
</html>