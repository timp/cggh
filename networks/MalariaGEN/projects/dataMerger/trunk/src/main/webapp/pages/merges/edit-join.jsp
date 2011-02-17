<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:useBean id="mergesModel" class="org.cggh.tools.dataMerger.data.merges.MergesModel" scope="page"/>
<%

mergesModel.setHttpServletRequest(request);

mergesModel.getCurrentMerge().setId(Integer.parseInt(request.getParameter("id")));

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Merges</title>
</head>
<body>
	<div class="page">
		<%@ include file="../shared/jsp/header.jsp" %>
		<h2>Edit join</h2>
		
		<dl>
		
			<dt>Merge ID:</dt><dd><%=mergesModel.getCurrentMerge().getId() %></dd>
			
			<dt>Merge created:</dt><dd><%=mergesModel.getCurrentMerge().getCreatedDatetime() %></dd>
			
			<dt>Merge updated:</dt><dd><%=mergesModel.getCurrentMerge().getUpdatedDatetime() %></dd>
			
		</dl>
		
		<div>
		
			<p>[error: A key is required]
			</p>
			
			<p>[error: There are duplicate keys]
			</p>
		
		</div>
		
		<div>
			<button>Save Join</button>
		
			<button>Edit Resolutions</button>
			
			<a href="">Cancel &amp; Return to Merges</a>
		</div>
		
	</div>
</body>
</html>