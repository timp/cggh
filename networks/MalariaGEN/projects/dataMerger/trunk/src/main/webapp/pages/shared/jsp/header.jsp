<%-- Note: This file should not require any database interaction because it is also used on database installation pages, etc. --%>
<div class="header">

	<%
	String headerBasePathURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
	%>

	<h1 class="title">dataMerger</h1>

	<p class="user-menu">
		<% if (request.getRemoteUser() != null) { %>
		<span class="user"><%=request.getRemoteUser() %></span>
		<span class="logout"><a href="javascript:logout();">logout</a></span>
		<% } else { %>
		<span class="user">Anonymous</span>
		<% } %>
	</p>

	<ul class="apps-menu">
	
		<!-- TODO: Adaptive interface (based on real ability to GET the page) rather than role-based. -->
		
		<% if (request.getServletPath().startsWith("/pages/home/")) { %>
			<li class="item"><a class="link selected" href="<%= headerBasePathURL %>pages/home/">Home</a></li>
		<% } else { %>
			<li class="item"><a class="link" href="<%= headerBasePathURL %>pages/home/">Home</a></li>
		<% } %>
		
		<% if (request.isUserInRole("uploader")) { %>
			<% if (request.getServletPath().startsWith("/pages/uploads/")) { %>
				<li class="item"><a class="link selected" href="<%= headerBasePathURL %>pages/uploads/">Uploads</a></li>
			<% } else { %>
				<li class="item"><a class="link" href="<%= headerBasePathURL %>pages/uploads/">Uploads</a></li>
			<% } %>
		<% } %>
	
		<% if (request.isUserInRole("merger")) { %>
			<% if (request.getServletPath().startsWith("/pages/merges/")) { %>
				<li class="item"><a class="link selected" href="<%= headerBasePathURL %>pages/merges/">Merges</a></li>
			<% } else { %>
				<li class="item"><a class="link" href="<%= headerBasePathURL %>pages/merges/">Merges</a></li>
			<% } %>
		<% } %>
	
		<% if (request.isUserInRole("exporter")) { %>
			<% if (request.getServletPath().startsWith("/pages/exports/")) { %>
				<li class="item"><a class="link selected" href="<%= headerBasePathURL %>pages/exports/">Exports</a></li>
			<% } else { %>
				<li class="item"><a class="link" href="<%= headerBasePathURL %>pages/exports/">Exports</a></li>
			<% } %>
		<% } %>
	
		<% if (request.isUserInRole("non-specific")) { %>
			<% if (request.getServletPath().startsWith("/pages/admin/")) { %>
				<li class="item"><a class="link selected" href="<%= headerBasePathURL %>pages/admin/">Admin</a></li>
			<% } else { %>
				<li class="item"><a class="link" href="<%= headerBasePathURL %>pages/admin/">Admin</a></li>
			<% } %>
		<% } %>
	
	</ul>



</div>