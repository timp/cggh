<%-- Note: This file should not require any database interaction because it is also used on database installation pages, etc. Take everything from the request/session. --%>
<%
	String headerBasePathURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/"; 

%>
<div class="header">

	

	<h1 class="title">dataMerger</h1>

	<p class="user-menu">
		<%
			if (request.getRemoteUser() != null) {
		%>
				<span class="user"><%=request.getRemoteUser() %></span>
		<%
			}
		%>
		<% 
			if (session.getAttribute("userAuthenticated") != null && (Boolean)session.getAttribute("userAuthenticated")) { 
		%>
			<span class="user"><%=session.getAttribute("username") %></span>
			<span class="logout"><a class="logoutLink">logout</a></span>
		<%
			} else {
		%>
				<span class="user">Anonymous</span>
		<%				
			}
		%>


	</p>

	<ul class="apps-menu">
	
		<% if (session.getAttribute("userAuthenticated") == null || !(Boolean)session.getAttribute("userAuthenticated")) { %>
	
			<% if (request.getServletPath().startsWith("/pages/shared/login/")) { %>
				<li class="item"><a class="link selected" href="<%= headerBasePathURL %>pages/shared/login/">Login</a></li>
			<% } else { %>
				<li class="item"><a class="link" href="<%= headerBasePathURL %>pages/shared/login/">Login</a></li>
			<% } %>
			
		<% } %>
		
		
		
		





		
		<% if (session.getAttribute("userAuthenticated") != null && (Boolean)session.getAttribute("userAuthenticated")) { %>
			<% if (request.getServletPath().startsWith("/pages/files/")) { %>
				<li class="item"><a class="link selected" href="<%= headerBasePathURL %>pages/files/">Files</a></li>
			<% } else { %>
				<li class="item"><a class="link" href="<%= headerBasePathURL %>pages/files/">Files</a></li>
			<% } %>
		<% } %>

	
		<% if (session.getAttribute("userAuthenticated") != null && (Boolean)session.getAttribute("userAuthenticated")) { %>
			<% if (request.getServletPath().startsWith("/pages/merges/")) { %>
				<li class="item"><a class="link selected" href="<%= headerBasePathURL %>pages/merges/">Merges</a></li>
			<% } else { %>
				<li class="item"><a class="link" href="<%= headerBasePathURL %>pages/merges/">Merges</a></li>
			<% } %>
		<% } %>
		
		<% if (session.getAttribute("userAuthenticated") != null && (Boolean)session.getAttribute("userAuthenticated")) { %>
			<% if (request.getServletPath().startsWith("/pages/exports/")) { %>
				<li class="item"><a class="link selected" href="<%= headerBasePathURL %>pages/exports/">Exports</a></li>
			<% } else { %>
				<li class="item"><a class="link" href="<%= headerBasePathURL %>pages/exports/">Exports</a></li>
			<% } %>
		<% } %>		


		<% if (request.getServletPath().startsWith("/pages/guides/")) { %>
			<li class="item"><a class="link selected" href="<%= headerBasePathURL %>pages/guides/">Guides</a></li>
		<% } else { %>
			<li class="item"><a class="link" href="<%= headerBasePathURL %>pages/guides/">Guides</a></li>
		<% } %>


		
	<% if (request.isUserInRole("administrator")) { %>
			<% if (request.getServletPath().startsWith("/pages/settings/")) { %>
				<li class="item"><a class="link selected" href="<%= headerBasePathURL %>pages/settings/">Settings</a></li>
			<% } else { %>
				<li class="item"><a class="link" href="<%= headerBasePathURL %>pages/settings/">Settings</a></li>
			<% } %>
	<% } %>
	
	</ul>
</div>