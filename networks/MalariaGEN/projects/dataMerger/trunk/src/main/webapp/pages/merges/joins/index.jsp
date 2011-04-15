<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="../../shared/jsp/prepage.jsp" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
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

DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");




%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>dataMerger - Merge - Joins</title>

	<link rel="stylesheet" type="text/css" href="../../shared/css/shared.css" />

	<link rel="stylesheet" href="css/joins.css" type="text/css" />

	<script type="text/javascript" src="../../shared/js/jquery.min.js"></script>
	<script type="text/javascript" src="../../shared/js/jquery.json.min.js"></script>
	
	<script type="text/javascript" src="../../shared/js/shared.js"></script>
	
	<script type="text/javascript" src="js/joins.js"></script>

	<script type="text/javascript">
	
		$(document).ready(function(){
			
			initSharedFunctions();
			initJoinsFunctions();

		});
	
	</script>


</head>
<body>
	<div class="page">
		<%@ include file="../../shared/jsp/header.jsp" %>
		<h2 class="page-title">Edit join</h2>

		<div class="status">
		</div>	
		<div class="error">
		</div>
		
		<div class="merge-info">
			<table>
			
				<tr><th>Merge ID:</th><td><%=mergeModel.getId() %></td></tr>
				
				<tr><th>Merge created:</th><td><%=dateFormat.format(mergeModel.getCreatedDatetime()) %></td></tr>
				
				<tr><th>Merge updated:</th><td><%=dateFormat.format(mergeModel.getUpdatedDatetime()) %></td></tr>
				
			</table>
		</div>
		
		<div class="problem-messages-container">
			
			<% if (mergeModel.getJoinsModel().getKeysCount() == null || mergeModel.getJoinsModel().getKeysCount() == 0) { %>
			<p>A key is required.
			</p>
			<% } %>
			
			<% if (null != mergeModel.getTotalDuplicateKeysCount() && mergeModel.getTotalDuplicateKeysCount() > 0) { %>
			<p>There are duplicate keys.
			</p>
			<% } %>
		
		</div>
		
		<div class="buttons">
		
			<%-- TODO: v2 detect whether there are changes that need saving. Alert choice to save if move off page. --%>
			<%-- TODO: v2 detect whether resolutions need recalculating (e.g. new column, removed column). Alert choice to abort (operation could remove work done). --%>

			<div class="item"><img class="saving-indicator" src="../../shared/gif/loading.gif" style="display:none" title="Saving..."/><button class="save-join">Save Join</button></div>
		
			<% if (mergeModel.getJoinsModel().getKeysCount() > 0 && mergeModel.getTotalDuplicateKeysCount() == 0) { %>
			<div class="item"><button class="edit-resolutions-by-column">Edit Resolutions</button></div>
			<% } %>
			
			<div class="item"><a href="<%= headerBasePathURL %>pages/merges/">Cancel &amp; Return to Merges</a></div>
			
		</div>
		
		<div class="forms">
			
			<!-- Turning off autocomplete prevents FF3 from remembering form values after refresh, which would not be helpful here. -->
			<form class="joins-form" onsubmit="return false;" autocomplete="off">
			
				<%=mergesModel.retrieveJoinsAsDecoratedXHTMLTableUsingMergeModel(mergeModel) %>	
	
			</form>
			
			<form class="new-join-form" onsubmit="return false;" autocomplete="off">
				<h3 class="new-join-heading-container">New column:</h3>
				<div class="new-join-table-container">
				<%=mergesModel.retrieveNewJoinAsDecoratedXHTMLTableUsingMergeModel(mergeModel) %>
				</div>
			</form>
	
			<%--
	
			<p>FIXME: Changing the join, just the column order or column name, shouldn't have to trigger a recalculation. (Possibly re-introduce join_id and amend code to simply update if the join_id already exists? Note: This would alter the schema of resolution_by_column, which should then use join_id instead of column_number. Although this approach would require a more complex solution to determine whether or not to recalculate.)
			</p>	
	
			<p>FIXME: Added join to beginning or end breaks up/down buttons disabled-ness.
			</p>		
			
			<p>FIXME: Saving join does not update updatedDate and Key counts. (build and trigger an AJAX GET service.)
			</p>
			
			 --%>
			
		</div>
		
	</div>
</body>
</html>