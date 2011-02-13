<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<jsp:useBean id="usersModel" class="org.cggh.tools.dataMerger.data.users.UsersModel" scope="session"/>
<%

usersModel.setHttpServletRequest(request);

if (!usersModel.isUsernameCreated(request.getRemoteUser())) {
	
	usersModel.createUser(request.getRemoteUser());

}

usersModel.getCurrentUser().setUsername(request.getRemoteUser());

%>
<%
String contextPath = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<h1>MalariaGEN dataMerger</h1>
<div><%=usersModel.getCurrentUser().getUsername() %></div>
<div><a href="TODO:logout">logout</a></div>
<ul>
	<!-- TODO: Adaptive interface (based on ability to GET) -->
	
	<li><a href="<%= basePath %>pages/home/">Welcome</a></li>
	<li><a href="<%= basePath %>pages/uploads/">Uploads</a></li>
	<li><a href="<%= basePath %>pages/merges/">Merges</a></li>
	<li><a href="<%= basePath %>pages/exports/">Exports</a></li>
	
	<!--
	<li><a href="<%= basePath %>pages/admin/">Admin</a></li>
	-->
</ul>



