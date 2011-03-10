<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="../../../shared/jsp/prepage.jsp" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="org.cggh.tools.dataMerger.data.merges.MergesModel" %>
<%@ page import="org.cggh.tools.dataMerger.data.merges.MergeModel" %>
<%@ page import="org.cggh.tools.dataMerger.data.resolutionsByColumn.ResolutionsByColumnModel" %>
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
<title>Merges</title>

	
	<link rel="stylesheet" type="text/css" href="../../../shared/css/shared.css" />
	
	<link rel="stylesheet" href="css/resolutions-by-column.css" type="text/css" />

	<script type="text/javascript" src="../../../shared/js/jquery.min.js"></script>
	<script type="text/javascript" src="../../../shared/js/jquery.json.min.js"></script>
	
	<script type="text/javascript" src="../../../shared/js/shared.js"></script>
	
	<script type="text/javascript" src="js/resolutions-by-column.js"></script>

	<script type="text/javascript">
	
		$(document).ready(function(){
			
			initSharedFunctions();
			initResolutionsByColumnFunctions();

		});
	
	</script>


</head>
<body>
	<div class="page">
		<%@ include file="../../../shared/jsp/header.jsp" %>
		
		<h2 class="page-title">Edit Resolutions</h2>

		<div class="status">
		</div>	
		<div class="error">
		</div>
		

		<div class="merge-info">
			<table>
			
				<tr><th>Merge ID:</th><td><%=mergeModel.getId() %></td></tr>
				
				<tr><th>Merge created:</th><td><%=dateFormat.format(mergeModel.getCreatedDatetime()) %></td></tr>
				
				<tr><th>Merge updated:</th><td><%=dateFormat.format(mergeModel.getUpdatedDatetime()) %></td></tr>
				
				<tr>
					<th>Sources:</th>
					<td>
				<ol>
					<%-- TODO: This url should be more dynamic --%>
					<li><a href="/dataMerger/files/uploads/<%=mergeModel.getUpload1Model().getId()%>"><%=mergeModel.getUpload1Model().getOriginalFilename()%></a></li>
					<li><a href="/dataMerger/files/uploads/<%=mergeModel.getUpload2Model().getId()%>"><%=mergeModel.getUpload2Model().getOriginalFilename()%></a></li>
				</ol>
					</td>
				</tr>
				
			</table>
		</div>
		
		<div class="problem-messages-container">
			
			<% if (mergeModel.getJoinsModel().getKeysCount() == null || mergeModel.getJoinsModel().getKeysCount() == 0 || mergeModel.getTotalDuplicateKeysCount() == null || mergeModel.getTotalDuplicateKeysCount() > 0) {%>
			<p>A valid join is required.
			</p>
			<% } %>
			
			<% if (mergeModel.getTotalConflictsCount() == 1) { %>
			<p><%=mergeModel.getTotalConflictsCount() %> conflict
			</p>
			<% } else if (mergeModel.getTotalConflictsCount() > 1) { %>
			<p><%=mergeModel.getTotalConflictsCount() %> conflicts
			</p>
			<% } %>
		
		</div>
		
		<div class="buttons">
			<div class="item"><img class="saving-indicator" src="../../../shared/gif/loading.gif" style="display:none" title="Saving..."/><button class="save-resolutions-by-column">Save Resolutions</button></div>
		
			<div class="item"><button class="edit-join">Edit Join</button></div>
			
			<!-- TODO: If there are no unsolved problems. -->
			<div class="item"><button>Export</button></div>
			
			<div class="item"><a href="<%= basePath %>pages/merges/">Cancel &amp; Return to Merges</a></div>
		</div>
		
		<div class="resolutions-container">
			<ul>
				<!-- TODO: soft-code these link urls -->
				<li><a href="/dataMerger/pages/merges/resolutions/by-column?merge_id=<%=mergeModel.getId()%>">By Column</a></li>
				<li><a href="/dataMerger/pages/merges/edit-resolutions-by-row.jsp?merge_id=<%=mergeModel.getId()%>">By Row</a></li>
				<li><a href="/dataMerger/pages/merges/edit-resolutions-by-cell.jsp?merge_id=<%=mergeModel.getId()%>">By Cell</a></li>
			</ul>
			
			<div class="resolutions-by-column">
			<form class="resolutions-by-column-form" onsubmit="return false;" autocomplete="off">
	<%
		
		ResolutionsByColumnModel resolutionsByColumnModel = new ResolutionsByColumnModel();
		resolutionsByColumnModel.setDataModel(dataModel);
		
		//FIXME
		//resolutionsByColumnModel.setUserModel(userModel);
	
		out.print(resolutionsByColumnModel.retrieveResolutionsByColumnAsDecoratedXHTMLTableUsingMergeModel(mergeModel));
		
	%>		
			</form>
			</div>
			
			<%-- 
			<p>FIXME: Saving resolutions doesn't update the conflicts count.
			</p>
			--%>
		</div>
	</div>
</body>
</html>