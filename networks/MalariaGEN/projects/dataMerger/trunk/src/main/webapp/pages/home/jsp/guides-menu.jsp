	<%
	//This is normally a duplicate of the basePath provided in the header-include, but of course cannot be guaranteed out of context. 
	String guideMenuBasePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
	%>
			<ul class="guides-menu">

				<% if (request.getServletPath().startsWith("/pages/home/guides/welcome")) { %>
					<li class="item"><a class="link selected" href="<%= guideMenuBasePath %>pages/home/guides/welcome">Welcome</a></li>
				<% } else { %>
					<li class="item"><a class="link" href="<%= guideMenuBasePath %>pages/home/guides/welcome">Welcome</a></li>
				<% } %>

				<% if (request.getServletPath().startsWith("/pages/home/guides/installation")) { %>
					<li class="item"><a class="link selected" href="<%= guideMenuBasePath %>pages/home/guides/installation">Installation</a></li>
				<% } else { %>
					<li class="item"><a class="link" href="<%= guideMenuBasePath %>pages/home/guides/installation">Installation</a></li>
				<% } %>
				
				<% if (request.getServletPath().startsWith("/pages/home/guides/configuration")) { %>
					<li class="item"><a class="link selected" href="<%= guideMenuBasePath %>pages/home/guides/configuration">Configuration</a></li>
				<% } else { %>
					<li class="item"><a class="link" href="<%= guideMenuBasePath %>pages/home/guides/configuration">Configuration</a></li>
				<% } %>
				
				<% if (request.getServletPath().startsWith("/pages/home/guides/usage")) { %>
					<li class="item"><a class="link selected" href="<%= guideMenuBasePath %>pages/home/guides/usage">Usage</a></li>
				<% } else { %>
					<li class="item"><a class="link" href="<%= guideMenuBasePath %>pages/home/guides/usage">Usage</a></li>
				<% } %>

			</ul>