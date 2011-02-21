<jsp:useBean id="headerDataModel" class="org.cggh.tools.dataMerger.data.DataModel" scope="session"/>
<jsp:useBean id="headerUserModel" class="org.cggh.tools.dataMerger.data.users.UserModel" scope="session"/>
<jsp:useBean id="headerUsersModel" class="org.cggh.tools.dataMerger.data.users.UsersModel" scope="session"/>
<%

headerDataModel.setDataModelByServletContext(request.getSession().getServletContext());
headerUserModel.setDataModel(headerDataModel);
headerUserModel.setUserModelByUsername(request.getRemoteUser());

headerUsersModel.setDataModel(headerDataModel);
headerUsersModel.setUserModel(headerUserModel);

if (!headerUsersModel.isUsernameCreated(headerUsersModel.getUserModel().getUsername())) {
	
	headerUsersModel.createUserByUsername(headerUsersModel.getUserModel().getUsername());

}

%>
<%
String contextPath = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<h1>MalariaGEN dataMerger</h1>
<div><%=headerUsersModel.getUserModel().getUsername() %></div>
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



