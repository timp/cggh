<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
	//For the time being, this redirects to the home page.
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
    response.sendRedirect(basePath + "pages/home/");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>dataMerger</title>
</head>
<body>
	<!-- TODO: Adaptive based on permissions (or anonymous) -->
	<ul>
		<li><a href="pages">Pages</a>
		</li>
		<li><a href="data">Data</a>
		</li>
		<li><a href="files">Files</a>
		</li>
		<li><a href="functions">Functions</a>
		</li>
		<li><a href="scripts">Scripts</a>
		</li>
	</ul>
</body>
</html>