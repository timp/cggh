	<%
	//This is normally a duplicate of the headerBasePathURL provided in the header-include, but of course cannot be guaranteed out of context. 
	String guidesMenuBasePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
	%>
			<ul class="guides-menu">

				<% if (request.getServletPath().startsWith("/pages/guides/installation")) { %>
					<li class="item"><a class="link selected" href="<%= guidesMenuBasePath %>pages/guides/installation">Installation</a></li>
				<% } else { %>
					<li class="item"><a class="link" href="<%= guidesMenuBasePath %>pages/guides/installation">Installation</a></li>
				<% } %>
				
				<% if (request.getServletPath().startsWith("/pages/guides/configuration")) { %>
					<li class="item"><a class="link selected" href="<%= guidesMenuBasePath %>pages/guides/configuration">Configuration</a></li>
				<% } else { %>
					<li class="item"><a class="link" href="<%= guidesMenuBasePath %>pages/guides/configuration">Configuration</a></li>
				<% } %>
				
				<% if (request.getServletPath().startsWith("/pages/guides/usage")) { %>
					<li class="item"><a class="link selected" href="<%= guidesMenuBasePath %>pages/guides/usage">Usage</a></li>
				<% } else { %>
					<li class="item"><a class="link" href="<%= guidesMenuBasePath %>pages/guides/usage">Usage</a></li>
				<% } %>

			</ul>