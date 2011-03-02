<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="../shared/jsp/prepage.jsp" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="org.cggh.tools.dataMerger.data.merges.MergesModel" %>
<%@ page import="org.cggh.tools.dataMerger.data.merges.MergeModel" %>
<%@ page import="org.cggh.tools.dataMerger.functions.merges.MergeFunctionsModel" %>
<%@ page import="org.cggh.tools.dataMerger.data.joins.JoinModel" %>
<%@ page import="org.cggh.tools.dataMerger.functions.joins.JoinFunctionsModel" %>
<%

MergesModel mergesModel = new MergesModel();
mergesModel.setDataModel(dataModel);
mergesModel.setUserModel(userModel);

MergeModel mergeModel = new MergeModel();
mergeModel = mergesModel.retrieveMergeAsMergeModelByMergeId(Integer.parseInt(request.getParameter("merge_id")));

MergeFunctionsModel mergeFunctionsModel = new MergeFunctionsModel();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Merges</title>
<link rel="stylesheet" href="../shared/css/merges.css" type="text/css" />

	<script type="text/javascript" src="../shared/js/jquery.min.js"></script>
	<script type="text/javascript" src="../shared/js/jquery.json.min.js"></script>
	
	<script type="text/javascript" src="../shared/js/shared.js"></script>
	
	<script type="text/javascript" src="js/merges.js"></script>

	<script type="text/javascript">
	
		$(document).ready(function(){
			
			initSharedFunctions();
			initMergesFunctions();

		});
	
	</script>


</head>
<body>
	<div class="page">
		<%@ include file="../shared/jsp/header.jsp" %>
		<h2>Edit join</h2>

		<div class="status">
		</div>	
		<div class="error">
		</div>
		
		<dl>
		
			<dt>Merge ID:</dt><dd><%=mergeModel.getId() %></dd>
			
			<dt>Merge created:</dt><dd><%=mergeModel.getCreatedDatetime() %></dd>
			
			<dt>Merge updated:</dt><dd><%=mergeModel.getUpdatedDatetime() %></dd>
			
		</dl>
		
		<div>
			
			<% if (mergeModel.getJoinsModel().getKeysCount() == null || mergeModel.getJoinsModel().getKeysCount() == 0) { %>
			<p>A key is required.
			</p>
			<% } %>
			
			<% if (null != mergeModel.getTotalDuplicateKeysCount() && mergeModel.getTotalDuplicateKeysCount() > 0) { %>
			<p>There are duplicate keys.
			</p>
			<% } %>
		
		</div>
		
		<div>
			<%-- TODO: v2 detect whether there are changes that need saving. Alert choice to save if move off page. --%>
			<button class="save join">Save Join</button>
		
			<% if (mergeModel.getJoinsModel().getKeysCount() > 0 && mergeModel.getTotalDuplicateKeysCount() == 0) { %>
			<button class="edit resolutions-by-column">Edit Resolutions</button>
			<% } %>
			
			<a href="<%= basePath %>pages/merges/">Cancel &amp; Return to Merges</a>
		</div>
		
		<!-- Turning off autocomplete prevents FF3 from remembering form values after refresh, which would not be helpful here. -->
		<form class="joins-form" onsubmit="return false;" autocomplete="off">
		<div class="joins">
<%

	//TODO: Update this to use same as uploads and edit-resolutions-by-column

  CachedRowSet joinsAsCachedRowSet = mergeModel.getJoinsModel().getJoinsAsCachedRowSet();

  if (joinsAsCachedRowSet != null) {

	    mergeFunctionsModel.setMergeModel(mergeModel);	    
	    mergeFunctionsModel.setJoinsAsCachedRowSet(joinsAsCachedRowSet);
	    mergeFunctionsModel.setJoinsAsDecoratedXHTMLTableByJoinsAsCachedRowSet();
	    out.print(mergeFunctionsModel.getJoinsAsDecoratedXHTMLTable());
	    
  } else {
	  
	  out.print("<p>Error: joinsAsCachedRowSet is null</p>");
	  
  }
%>		
		</div>

		</form>
		
		<form class="new-join-form" onsubmit="return false;" autocomplete="off">
			<h3>New column:</h3>
			<%-- TODO: Get this from a service. --%>
<%

	JoinModel joinModel = new JoinModel();
	JoinFunctionsModel joinFunctionsModel = new JoinFunctionsModel();

	
	joinFunctionsModel.setJoinModel(joinModel);
	joinFunctionsModel.setMergeModel(mergeModel);
	joinFunctionsModel.setJoinAsDecoratedXHTMLTableByJoinModel();
	out.print(joinFunctionsModel.getJoinAsDecoratedXHTMLTable());

%>
		</form>
		
		
		<p>FIXME: Added join cannot be moved. (poss. related to css binding. Use direct js call instead?)
		</p>

		<p>FIXME: Added join to beginning or end breaks up/down buttons. (poss. related to css binding. Use direct js call instead?)
		</p>		
		
		<p>FIXME: Saving join does not update updatedDate and Key counts. (build and trigger an AJAX GET service.)
		</p>
		
		<p>FIXME: Saving join breaks up/down buttons. (poss. related to css binding. Use direct js call instead?)
		</p>
		
	</div>
</body>
</html>