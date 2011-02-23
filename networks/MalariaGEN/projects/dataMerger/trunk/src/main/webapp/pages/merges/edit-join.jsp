<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="../shared/jsp/prepage.jsp" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<jsp:useBean id="mergeModel" class="org.cggh.tools.dataMerger.data.merges.MergeModel" scope="page"/>
<jsp:useBean id="joinsFunctionsModel" class="org.cggh.tools.dataMerger.functions.joins.JoinsFunctionsModel" scope="page"/>
<%

mergeModel.setDataModel(dataModel);
mergeModel.setUserModel(userModel);

mergeModel.setMergeModelById(Integer.parseInt(request.getParameter("merge_id")));

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
		
			<dt>Merge ID:</dt><dd><%=mergeModel.getId() %></dd>
			
			<dt>Merge created:</dt><dd><%=mergeModel.getCreatedDatetime() %></dd>
			
			<dt>Merge updated:</dt><dd><%=mergeModel.getUpdatedDatetime() %></dd>
			
		</dl>
		
		<div>
			
			<% if (mergeModel.getJoinsModel().getKeysCount() == null || mergeModel.getJoinsModel().getKeysCount() == 0) { %>
			<p>[error: A key is required]
			</p>
			<% } %>
			<p>TODO: Keys count: <%=mergeModel.getJoinsModel().getKeysCount() %></p>
			
			<% if (null != mergeModel.getTotalDuplicateKeysCount() && mergeModel.getTotalDuplicateKeysCount() > 0) { %>
			<p>[error: There are duplicate keys]
			</p>
			<% } %>
			<p>TODO: Duplicate Keys count: <%=mergeModel.getTotalDuplicateKeysCount() %></p>
		
		</div>
		
		<div>
			<button>Save Join</button>
		
			<button>Edit Resolutions</button>
			
			<a href="">Cancel &amp; Return to Merges</a>
		</div>
		
		
		<form class="join-form">
		<div class="join">
<%

  CachedRowSet joinsAsCachedRowSet = mergeModel.getJoinsModel().getJoinsAsCachedRowSet();

  if (joinsAsCachedRowSet != null) {

	  	//TODO: This seems wrong because a set of joins can relate to more than one merge.
	    joinsFunctionsModel.setMergeModel(mergeModel);
	    
	    joinsFunctionsModel.setCachedRowSet(joinsAsCachedRowSet);
	    joinsFunctionsModel.setDecoratedXHTMLTableByCachedRowSet();
	    out.print(joinsFunctionsModel.getDecoratedXHTMLTable());
	    
  } else {
	  
	  out.print("<p>Error: joinsAsCachedRowSet is null</p>");
	  
  }
%>		
		</div>

		</form>
		
		<form class="new-join">
			<h3>New column:</h3>
			<div>TODO: New column UI</div>
		</form>
		
	</div>
</body>
</html>