<% 
	String rootBasePathURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
	response.sendRedirect(rootBasePathURL + "pages/guides"); 
%>