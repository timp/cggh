<% 
	String webappBaseURLAsString = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
	
	if (session.getAttribute("userAuthenticated") == null || !(Boolean)session.getAttribute("userAuthenticated")) {
		response.sendRedirect(webappBaseURLAsString + "pages/shared/login/");
	} else {
		response.sendRedirect(webappBaseURLAsString + "pages/guides/");
	}
%>