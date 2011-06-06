	<%
	//This is normally a duplicate of the headerBasePathURL provided in the header-include, but of course cannot be guaranteed out of context. 
	String settingsSectionsMenuBasePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
	%>
			<ul class="settings-sections-menu">

				<% if (request.getServletPath().startsWith("/pages/settings/database")) { %>
					<li class="item"><a class="link selected" href="<%= settingsSectionsMenuBasePath %>pages/settings/database">Database</a></li>
				<% } else { %>
					<li class="item"><a class="link" href="<%= settingsSectionsMenuBasePath %>pages/settings/database">Database</a></li>
				<% } %>
				
				<% if (request.getServletPath().startsWith("/pages/settings/filebase")) { %>
					<li class="item"><a class="link selected" href="<%= settingsSectionsMenuBasePath %>pages/settings/filebase">Filebase</a></li>
				<% } else { %>
					<li class="item"><a class="link" href="<%= settingsSectionsMenuBasePath %>pages/settings/filebase">Filebase</a></li>
				<% } %>
				
				<% if (request.getServletPath().startsWith("/pages/settings/userbase")) { %>
					<li class="item"><a class="link selected" href="<%= settingsSectionsMenuBasePath %>pages/settings/userbase">Userbase</a></li>
				<% } else { %>
					<li class="item"><a class="link" href="<%= settingsSectionsMenuBasePath %>pages/settings/userbase">Userbase</a></li>
				<% } %>
				
				<% if (request.getServletPath().startsWith("/pages/settings/codebase")) { %>
					<li class="item"><a class="link selected" href="<%= settingsSectionsMenuBasePath %>pages/settings/codebase">Codebase</a></li>
				<% } else { %>
					<li class="item"><a class="link" href="<%= settingsSectionsMenuBasePath %>pages/settings/codebase">Codebase</a></li>
				<% } %>

			</ul>