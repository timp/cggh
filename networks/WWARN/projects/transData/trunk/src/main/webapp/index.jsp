<% 
	String webappBaseURLAsString = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
	
	response.sendRedirect(webappBaseURLAsString + "pages/transformers/XMLURL-to-CSVFile/");
%>