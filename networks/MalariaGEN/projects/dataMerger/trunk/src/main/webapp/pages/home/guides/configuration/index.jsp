<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="../../../shared/jsp/prepage.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>dataMerger - Home - Welcome</title>
		
		<link rel="stylesheet" type="text/css" href="../../../shared/css/shared.css" />
		<link rel="stylesheet" type="text/css" href="../../css/home.css" />
		<script type="text/javascript" src="../../../shared/js/shared.js"></script>
		
	</head>
	<body>
		<div class="page">
			<%@ include file="../../../shared/jsp/header.jsp" %>
			<h2 class="page-title">Home</h2>
			
			<div class="guides-container">
			
				<%@ include file="../../jsp/guides-menu.jsp" %>
				
				<div class="guide">
					
					<h3>Configuration:</h3>
		
		
								<h4>[dataMerger-webapp]/WEB-INF/web.xml:</h4>
								<ul> 
									<li>databaseBasePath (e.g. jdbc:mysql://localhost:3306/)</li>
									<li>databaseName (e.g. datamerger)</li>
									<li>databaseUsername (e.g. root)</li>
									<li>databasePassword (e.g. root)</li>
									<li>uploadsFileRepositoryBasePath (e.g. C:\Lee\Work\dataMerger\files\uploads\)</li>
									<li>exportsFileRepositoryBasePath (e.g. C:\Lee\Work\dataMerger\files\exports\)</li>
								</ul>
		
		
								<h4>[tomcat-server]/tomcat-users.xml:</h4>
								<pre style="margin-left: 25px;">
		&lt;-- Required roles, referred to in web.xml --&gt;
		&lt;role rolename="non-specific"/&gt;
		&lt;role rolename="user"/&gt;
		&lt;role rolename="uploader"/&gt;
		&lt;role rolename="merger"/&gt;
		&lt;role rolename="exporter"/&gt;
		
		&lt;-- Optional users --&gt;
		&lt;user username="A Dmin" password="admin" roles="user,uploader,merger,exporter,non-specific" /&gt;
		&lt;user username="U Ploader" password="uploader" roles="user,uploader" /&gt;
		&lt;user username="M Erger" password="merger" roles="user,merger" /&gt;
		&lt;user username="E Xporter" password="exporter" roles="user,exporter" /&gt;
		&lt;user username="lee" password="test" roles="user,uploader,merger,exporter" /&gt;
								</pre>
				
				</div>
				
			</div>
			
		</div>
	</body>
</html>