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
								<li>Files cannot be undeleted.</li>
							</ul>
						</li>
						<li>Pressing the Merge button
							<ul>
								<li>After selecting two files and pressing the Merge button, a spinning icon will appear to indicate that the application is waiting for a process to complete.</li>
								<li>After pressing the Merge button, the application loads the files into the database and attempts to guess how the two sets of data should be joined based on the column names, and then guesses which columns are the keys (uniquely identify each record) based on the uniqueness of the values.</li>
								<li>After the application has guessed the join and keys, it presents the join for editing.</li>
							</ul>
						</li>
						<li>Editing the join
							<ul>
								<li></li>
								<li></li>
							</ul>
						</li>
						<li>Editing the conflict resolutions</li>
						<li>Exporting a merged file</li>
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
							<li>ANSI and UTF8 character sets. (Ideally, Topheno should export as UTF8.)
							</li>
							<li>Windows-style end-of-line character, i.e. \n\r, Carriage Return with Line Feed, CR+LF
							</li>
						</ul>
					
					<h5>Symptoms:</h5>
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
							<li>In v1.0, the following values (as they appear in the uploaded CSV file) are interpreted as NULL:
							
								<ul>
									<li>NULL</li>
									<li>\N</li>
									<li>"\N"</li>
								</ul>
							
							</li>
							<li>As of v1.1, the following values are also interpreted as NULL:
							
								<ul>
									<li>"NULL"</li>
									<li>Quoted and unquoted empty strings</li>
								</ul>
							
							</li>
							<li>In v1.0, all NULL values were exported as (and appear in the exported CSV file as) \N</li>
							<li>As of v1.1, all NULL values are exported as (and appear in the exported CSV file as) "NULL"</li>
							<li>As of v1.1, the import and export of NULL values is controlled by values in the database's setting table.</li>
							
						</ul>
					
				
				</div>
				
			</div>
			
		</div>
	</body>
</html>