<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="../shared/jsp/prepage.jsp" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="org.cggh.tools.dataMerger.data.merges.MergesModel" %>
<%@ page import="org.cggh.tools.dataMerger.data.merges.MergeModel" %>
<%@ page import="org.cggh.tools.dataMerger.data.resolutionsByCell.ResolutionsByCellModel" %>
<%

MergesModel mergesModel = new MergesModel();
mergesModel.setDataModel(dataModel);
mergesModel.setUserModel(userModel);

MergeModel mergeModel = new MergeModel();
mergeModel = mergesModel.retrieveMergeAsMergeModelByMergeId(Integer.parseInt(request.getParameter("merge_id")));

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>dataMerger - Merges</title>
<link rel="stylesheet" href="css/merges.css" type="text/css" />

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
		<h2>Edit Resolutions</h2>

		<div class="status">
		</div>	
		<div class="error">
		</div>
		
		<dl>
		
			<dt>Merge ID:</dt><dd><%=mergeModel.getId() %></dd>
			
			<dt>Merge created:</dt><dd><%=mergeModel.getCreatedDatetime() %></dd>
			
			<dt>Merge updated:</dt><dd><%=mergeModel.getUpdatedDatetime() %></dd>
			
			<dt>Sources:</dt>
			<dd>
				<ol>
					<%-- TODO: This url should be more dynamic --%>
					<li><a href="/dataMerger/files/uploads/<%=mergeModel.getUpload1Model().getId()%>"><%=mergeModel.getUpload1Model().getOriginalFilename()%></a></li>
					<li><a href="/dataMerger/files/uploads/<%=mergeModel.getUpload2Model().getId()%>"><%=mergeModel.getUpload2Model().getOriginalFilename()%></a></li>
				</ol>
			</dd>
			
		</dl>
		
		<div>
			
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
		
		<div>
			<button class="save-resolutions-by-cell">Save Resolutions</button>
		
			<button class="edit-join">Edit Join</button>
			
			<!-- TODO: If there are no unsolved problems. -->
			<button>Export</button>
			
			<a href="<%= basePath %>pages/merges/">Cancel &amp; Return to Merges</a>
		</div>
		
		<ul>
			<!-- TODO: soft-code these link urls -->
			<li><a href="<%= basePath %>pages/merges/edit-resolutions-by-column.jsp?merge_id=<%=mergeModel.getId()%>">By Column</a></li>
			<li><a href="<%= basePath %>pages/merges/edit-resolutions-by-row.jsp?merge_id=<%=mergeModel.getId()%>">By Row</a></li>
			<li><a href="<%= basePath %>pages/merges/edit-resolutions-by-cell.jsp?merge_id=<%=mergeModel.getId()%>">By Cell</a></li>
		</ul>
		
		<div class="resolutions-by-cell">
		<form class="resolutions-by-cell-form" onsubmit="return false;" autocomplete="off">
<%
	
	ResolutionsByCellModel resolutionsByCellModel = new ResolutionsByCellModel();
	resolutionsByCellModel.setDataModel(dataModel);
	
	//FIXME
	//resolutionsByColumnModel.setUserModel(userModel);

	out.print(resolutionsByCellModel.retrieveResolutionsByCellAsDecoratedXHTMLTableUsingMergeModel(mergeModel));
	
%>		
		</form>
		</div>
		
	</div>
</body>
</html>