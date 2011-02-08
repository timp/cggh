<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<jsp:useBean id="usersModel" class="org.cggh.tools.dataMerger.data.users.UsersModel" scope="session"/>
<%
//TODO: Move this to a controller.
usersModel.setHttpServletRequest(request);

/*
 if (!usersModel.getCurrentUserUsername().isEmpty() && !usersModel.isCurrentUserRegistered()) {
	 usersModel.createUser(usersModel.getCurrentUserUsername());
 }
*/
%>
<%
String contextPath = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
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
<%-- 
<span>Username: <%=usersModel.getCurrentUserUsername() %></span>
<span>Registered: <%=usersModel.isCurrentUserRegistered() %></span>
--%>