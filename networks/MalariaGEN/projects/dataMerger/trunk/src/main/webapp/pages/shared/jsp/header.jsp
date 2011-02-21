<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%--TODO: The data and user models are being loaded twice, i.e. in the index page and in the header. --%>
<jsp:useBean id="headerDataModel" class="org.cggh.tools.dataMerger.data.DataModel" scope="session"/>
<jsp:useBean id="headerUserModel" class="org.cggh.tools.dataMerger.data.users.UserModel" scope="session"/>
<jsp:useBean id="usersModel" class="org.cggh.tools.dataMerger.data.users.UsersModel" scope="session"/>
<%

headerDataModel.setDataModelByServletContext(request.getSession().getServletContext());
headerUserModel.setDataModel(headerDataModel);
headerUserModel.setUserModelByUsername(request.getRemoteUser());

usersModel.setDataModel(headerDataModel);
usersModel.setUserModel(headerUserModel);

if (!usersModel.isUsernameCreated(usersModel.getUserModel().getUsername())) {
	
	usersModel.createUserByUsername(usersModel.getUserModel().getUsername());

}

%>
<%
String contextPath = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<h1>MalariaGEN dataMerger</h1>
<div><%=usersModel.getUserModel().getUsername() %></div>
<div><a href="TODO:logout">logout</a></div>
<ul>
	<!-- TODO: Adaptive interface (based on real ability to GET the page) rather than role-based. -->
	
	<li><a href="<%= basePath %>pages/home/">Welcome</a></li>
	
	<% if (request.isUserInRole("uploader")) { %>
	<li><a href="<%= basePath %>pages/uploads/">Uploads</a></li>
	<% } %>

	<% if (request.isUserInRole("merger")) { %>
	<li><a href="<%= basePath %>pages/merges/">Merges</a></li>
	<% } %>

	<% if (request.isUserInRole("exporter")) { %>
	<li><a href="<%= basePath %>pages/exports/">Exports</a></li>
	<% } %>

	<% if (request.isUserInRole("non-specific")) { %>
	<li><a href="<%= basePath %>pages/admin/">Admin</a></li>
	<% } %>

</ul>



