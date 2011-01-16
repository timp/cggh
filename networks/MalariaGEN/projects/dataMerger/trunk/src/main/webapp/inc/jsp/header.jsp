<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%
String contextPath = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<ul>
	<li><a href="<%= basePath %>home/">Welcome</a></li>
	<li><a href="<%= basePath %>uploads/">Uploads</a></li>
	<li><a href="<%= basePath %>merges/">Merges</a></li>
	<li><a href="<%= basePath %>exports/">Exports</a></li>
</ul>