<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%-- Note: This file should not require any database interaction because it should stand alone as a document. --%>
<%
String usageGuideBasePathURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>dataMerger - Guides - Usage</title>
		
		<link rel="stylesheet" type="text/css" href="../../shared/css/shared.css" />
		<link rel="stylesheet" type="text/css" href="../css/guides.css" />
		
		<script type="text/javascript" src="../../shared/js/jquery.min.js"></script>
		<script type="text/javascript" src="../../shared/js/shared.js"></script>
		<script type="text/javascript">
	
			$(document).ready(function(){
				initSharedFunctions();
			});
		
		</script>
		
	</head>
	<body>
		<div class="page">
			<%@ include file="../../shared/jsp/header.jsp" %>
			<h2 class="page-title">Guides</h2>
	
			<div class="guides-container">
			
				<%@ include file="../jsp/guides-menu.jsp" %>
				
				<div class="guide">
					
	
		
					<h3>Usage</h3>
					
					<h4>Catchable usage errors with known remedies:</h4>
					<ul>
						<li><a href="errors/accept-header">Accept header</a></li>
						<li><a href="errors/valid-session-required">Session loss</a></li>
					</ul>
					

					
					
					<h4>Steps required to merge two files (with some notes on the side)</h4>
					<ol>
						<li>Use the Firefox web browser to navigate to the URL http://yourhost:8080/dataMerger
							<ul>
								<li>You will be redirected to a secure connection, https://yourhost:8443/dataMerger/pages/shared/login/</li>
							</ul>
						</li>
						<li>Logging in
							<ul>
								<li>Administration area (<a href="<%=usageGuideBasePathURL %>/pages/settings">Settings</a>)
									<ul>
										<li>Available via the URL https://yourhost:8443/dataMerger/pages/settings</li>
										<li>You will be prompted to provide a username password, as specified in the tomcat-users.xml configuration file.</li>
									</ul>
								</li>
								<li>Application area (<a href="<%=usageGuideBasePathURL %>/pages/files">Files</a>, <a href="<%=usageGuideBasePathURL %>/pages/merges">Merges</a>, <a href="<%=usageGuideBasePathURL %>/pages/exports">Exports</a>)
									<ul>
										<li>Log in using the username and password set by application administrator.</li>
										<li>Application administrators can add users (username and password) using the Administration area (<a href="<%=usageGuideBasePathURL %>/pages/settings">Settings</a>)</li>
									</ul>
								</li>
							</ul>
						</li>
						<li>Uploading files
							<ul>
								<li>The upload feature is currently only available using the Firefox web browser. (To be fixed.)</li>
								<li>See notes on file-type limitations below.</li>
								<li>Use the Upload files facility at the bottom of the <a href="<%=usageGuideBasePathURL %>/pages/files">Files page</a> to upload files to the application.</li>
								<li>You can queue multiple files for upload, either by pressing the Upload button and selecting multiple files from the file browser, or by dragging and dropping files onto the Upload button.</li>
								<li>Files that have been uploaded will appear in the list of files, alongside exported files.</li>
								<li>After uploading, files can be hidden or deleted.</li>
							</ul>
						</li>
						<li>Selecting files
							<ul>
								<li>To merge files, select two files (using the checkboxes) that appear next to each file in the list.</li>
								<li>To hide files, select the files (using the checkboxes) and then press the Hide button.</li>
								<li>To see the list of hidden files, press the Show hidden button.
								<li>To unhide files, press the Show hidden button, select the files you want to unhide (using the checkboxes) and then press the Unhide button.</li>
								<li>To see the list of files that are not hidden, press the Show unhidden button</li>
								<li>To sort the list of unhidden files by column, click on the column heading link.</li>
								<li>Currently, columns can only be sorted one at a time and in ascending order.</li>
								<li>Currently, the list of hidden files cannot be sorted by column.</li>
								<li>To search for a file in the list, use the browser's own Find feature.</li>
								<li>To delete files, select the files (using the checkboxes) and then press the Delete button.</li>
								<li>Files can only be deleted when they are not referred to by an existing merge or export.</li>
								<li>Files cannot be undeleted.</li>
							</ul>
						</li>
						<li>Pressing the Merge button
							<ul>
								<li>After selecting two files and pressing the Merge button, a spinning icon will appear to indicate that the application is waiting for a process to complete.</li>
								<li>After pressing the Merge button, the application loads the files into the database and attempts to guess how the two sets of data should be joined based on the column names, and then guesses which columns are the keys (uniquely identify each record) based on the uniqueness of the values.</li>
								<li>After the application has guessed the join and keys, it presents the join for editing.</li>
								<li>Pressing the Merge button creates a new "merge", which ties together the information required for carrying out the merge.</li>
								<li>Merges are listed on the Merges page and can be deleted.</li>
								<li>Deleting a merge also deletes all of the join and resolution information associated with that merge.</li>
								<li>Deleting a merge does not delete the two source files associated with it, which may be used for other merges.</li>
							</ul>
						</li>
						<li>Editing the join
							<ul>
								<li>For each merge created, the edit page for the join can be accessed by clicking on the Edit join link on the Merges page.</li>
								<li>Columns can be added to the join by selecting a column from one file and a column from the other file, and then pressing the Add button.</li>								
								<li>Columns can also be added that derive their values from only one column (from only one of the files) and use a specified constant value instead (e.g. NULL) for uncommon records appearing in the other file.</li>
								<li>A joined pair of columns between the two files can be designated as a key, or part of a composite key, which is used to identify unique records between the two files.</li>
								<li>Each record must be identifiable by a unique key, so there must be no duplicate key values present in either of the two files.</li>
								<li>If no key has been selected, or if duplicate key values are found, the merge cannot take place and a warning will appear.</li>
								<li>Joined columns can be added to any place in the existing order.</li>
								<li>Joined columns can be moved up or down, or removed. The column nearest the top will appear first in the final merged data table.</li>
								<li>Joined columns can be renamed.</li>
								<li>Joined columns for (the final merged data table) must have unique names before the join can be saved.</li>
								<li>If the join is valid and produces a merge that has no data conflicts between the two sources, the merge can be exported.</li>
								<li>If the join produces a merge that has data conflicts, then resolutions for those conflicts must be specified before the merge can be exported.</li>
							</ul>
						</li>
						<li>Editing the conflict resolutions
							<ul>
								<li>To edit the resolutions for a merge that has conflicts, press the Edit Resolutions button on the Edit join page, or click on the appropriate Edit Resolutions link on the Merges page.</li>
								<li>Conflicts can be resolved by providing solutions either by column, row or individual cell.</li>
								<li>A drop-down list of possible solutions for each data conflict (or group of data conflicts) is presented.</li>
								<li>Choice of solutions operates on a first-come-first-serve basis, so solutions will not overwrite or contradict each other. For example, if a data conflict exists for a particular cells in column A and this is resolved by choosing "Prefer source 1", then a subsequent choice to resolve all other conflicts in column A by "Prefer source 2" will not apply to that particular cell.</li>
								<li>When solutions are saved, the conflict count is updated until there are no more conflicts without a proposed solution.</li>
								<li>When all conflicts have been resolved, the option to export the merge will appear.</li>
								<li>When all conflicts have been resolved, a merge can also be exported from the Merges page, or the Edit join page.</li>
							</ul>
						</li>
						<li>Exporting a merged file
							<ul>
								<li>The name of the merged file can be specified using the text-box provided.</li>
								<li>The default name for the merged file (created via export) is merged_file_#.csv, where # is the merge ID.</li>
								<li>To create an export, choose a name for the merged file and then press the Export button (on either the Merges, Edit Join or Edit Resolutions pages).</li>
								<li>A record of the export's merged file, source files, joins and conflict resolutions is made.</li>
								<li>A record of application settings is also made for provenance purposes, e.g. the list of values regarded as null, and the value exported instead of null in the merged file.</li>
								<li>The settings, joins and resolutions record files are CSV files.</li>
								<li>To download the merged file, the original source files, or any of the provenance records, click on the appropriate link on the Exports page.</li>
								<li>Exports can be deleted.</li>
								<li>Deleting an export does not delete the two source files associated with it, or the merged file produced, which may be used for other merges.</li>
								<li>Deleting an export does not currently delete any of the provenance record files associated with it.</li>
							</ul>
						</li>
					</ol>
					
					
					<h4>File-type limitations</h4>
					<p>Currently, the application will only work with text files that have:</p>
					  
						<ul>
							<li>Comma-separated values
							</li>
							<li>Values optionally enclosed by the double quote character, i.e. &quot;
							</li>
							<li>Special characters escaped using the back-slash character, i.e. \
							</li>
							<li>ISO-8859-1 character-set encoding, a.k.a. Latin-1
							</li>
							<li>The Unix-style end-of-line character, i.e. \n, Line Feed, LF
							</li>
						</ul>
								
					<p><em>Note:</em> Saving such as file in Microsoft Excel will result in a different (as yet unsupported file type). 
					</p>					
					
					<h5>Not yet supported (when saved using Excel):</h5>
						<ul>
							<li>ANSI and UTF8 character sets.
							</li>
							<li>Windows-style end-of-line character, i.e. \n\r, Carriage Return with Line Feed, CR+LF
							</li>
						</ul>
					
					<h5>Symptoms of using the wrong file-type:</h5>
						<ul>
							<li>Unsupported character-encoding: 
								<ul>
									<li>Odd characters appearing in column headings. Potential application failures.
									</li>
								</ul>
							</li>
							<li>Unsupported end-of-line character: 
								<ul>
									<li>Right-most cells contain an unexpected last character (i.e. \r). Potential application failures, esp. conflict counting and resolution.
									</li>
								</ul>
							</li>
						</ul>
					
					
					<h5>Current work-arounds:</h5>
						<ul>
							<li>Use Notepad++ to change the character encoding or end-of-line characters.
							</li>
						</ul>
					
		
					<h4>Handling of NULL values:</h4>
						<ul>
							<li>The import and export of NULL values is controlled by values in the database's setting table. The following values are currently interpreted as NULL: 
							
								<ul>
									<li>NULL</li>
									<li>\N</li>
									<li>"\N"</li>
									<li>"NULL"</li>
									<li>Quoted and unquoted empty strings</li>
								</ul>
							
							</li>
							<li>All NULL values are exported as (and appear in the exported CSV file as) "NULL"</li>
						</ul>
					
				
				</div>
				
			</div>
			
		</div>
	</body>
</html>