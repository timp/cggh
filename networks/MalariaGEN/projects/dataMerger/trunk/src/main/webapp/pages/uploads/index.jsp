<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*" %>
<jsp:useBean id="uploads" class="org.cggh.tools.dataMerger.data.uploads.controller.UploadsController" scope="page"/>
<jsp:setProperty name="uploads" property="middleName" value="Jim" />    
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

		<!-- using it as a bean -->

		<p>middleName: <jsp:getProperty name="uploads" property="middleName"/></p>
		
		
<%

  //If we were using it as a servlet...

  List middleNames = (List) request.getAttribute("middleNames");

  if (middleNames != null) {
	  Iterator it = middleNames.iterator();
	  while(it.hasNext()) {
	    out.print("<br>middleName: " + it.next());
	  }
  } else {
	  
	  out.print("<br>middleNames is null");
	  
  }
%>		
		
	</div>	

</body>
</html>