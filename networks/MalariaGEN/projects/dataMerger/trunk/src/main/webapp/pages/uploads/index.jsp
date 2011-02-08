<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.ResultSet" %>
<jsp:useBean id="uploadsModel" class="org.cggh.tools.dataMerger.data.uploads.UploadsModel" scope="page"/>
<%
uploadsModel.setHttpServletRequest(request);
%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Uploads</title>
</head>
<body>
	<div class="page">
		<%@ include file="../shared/jsp/header.jsp" %>
		<h1>Uploads</h1>

<%

  ResultSet uploadsAsResultSet = uploadsModel.getUploadsAsResultSet();

  if (uploadsAsResultSet != null) {
	  
	    out.print("TODO: Send the uploadsAsResultSet to representation functions to get a decorated HTML table plus sort+page javascript, then print here.");
	  
  } else {
	  
	  out.print("<p>Error: uploadsAsResultSet is null</p>");
	  
  }
%>		
		
	</div>	

</body>
</html>