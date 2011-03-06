<div class="header">

	<%
	String contextPath = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
	%>

	<h1 class="title">MalariaGEN dataMerger</h1>

	<p class="user-menu">
		<span class="user"><%=request.getRemoteUser() %></span>
		<span class="logout"><a href="TODO:logout">logout</a></span>
	</p>

	<ul class="apps-menu">
	
		<!-- TODO: Adaptive interface (based on real ability to GET the page) rather than role-based. -->
		
		<!-- TODO: Add selected class to link for current app -->
		
		<li class="item"><a class="link " href="<%= basePath %>pages/home/">Welcome</a></li>
		
		<% if (request.isUserInRole("uploader")) { %>
		<li class="item"><a class="link " href="<%= basePath %>pages/uploads/">Uploads</a></li>
		<% } %>
	
		<% if (request.isUserInRole("merger")) { %>
		<li class="item"><a class="link " href="<%= basePath %>pages/merges/">Merges</a></li>
		<% } %>
	
		<% if (request.isUserInRole("exporter")) { %>
		<li class="item"><a class="link " href="<%= basePath %>pages/exports/">Exports</a></li>
		<% } %>
	
		<% if (request.isUserInRole("non-specific")) { %>
		<li class="item"><a class="link " href="<%= basePath %>pages/admin/">Admin</a></li>
		<% } %>
	
	</ul>



</div>