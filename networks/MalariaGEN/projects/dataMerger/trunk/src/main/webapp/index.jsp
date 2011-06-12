<% 
	String rootBasePathURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
	
	if (request.getSession().getAttribute("userAuthentic") == null || !request.getSession().getAttribute("userAuthentic").equals(Boolean.TRUE)) {
		response.sendRedirect(rootBasePathURL + "pages/shared/login/");
	} else {
		response.sendRedirect(rootBasePathURL + "pages/guides/");
	}
%>