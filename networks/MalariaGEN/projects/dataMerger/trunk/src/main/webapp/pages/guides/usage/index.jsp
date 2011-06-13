<%-- Note: This file should not require any database interaction because it should stand alone as a document. --%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
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
					
					
					<h4>Steps</h4>
					
					<ol>
						<li>Login
							<ul>
								<li>Currently using HTTP Basic. Waiting for SSO developments. Possible temporary move to custom db.</li>
							</ul>
						</li>
						<li>Upload files</li>
						<li>Select files</li>
						<li>Press Merge</li>
						<li>Edit Joins</li>
						<li>Edit Resolutions</li>
						<li>Export merged file</li>
					</ol>
					
					
					<p>The file format for the source files ("uploads") is expected to be the same as the "calculated data" downloaded from Topheno.
					</p>
					
					<h4>Specifically:</h4>
						<ul>
							<li>ISO-8859-1 character-set encoding (a.k.a. Latin-1) 
							</li>
							<li>The Unix-style end-of-line character (i.e. \n, Line Feed, LF)
							</li>
							<li>Comma-separated values
							</li>
						</ul>
					
					
					<p>This also describes the file-type of the "export" files produced using this application. (So they too can be used in merges.) Exported files may contain \N characters (NULL), whereas Topheno exports currently only use empty strings to represent NULL (both are treated as equivalent).
					</p>
					
					<p><em>Note:</em> Saving such as file in Microsoft Excel will result in a different (as yet unsupported file type) 
					</p>
					
					<h4>Not yet supported (when saved by Excel):</h4>
						<ul>
							<li>ANSI and UTF8 character sets. (Ideally, Topheno should export as UTF8.)
							</li>
							<li>Windows-style end-of-line character (i.e. \n\r, Carriage Return with Line Feed, CR+LF)
							</li>
						</ul>
					
					<h4>Symptoms:	</h4>
						<ul>
							<li>Unsupported character-encoding: Odd characters appearing in column headings. Potential application failures.
							</li>
							<li>Unsupported end-of-line character: Right-most cells contain an unexpected last character (i.e. \r). Potential application failures, esp. conflict counting and resolution.
							</li>
						</ul>
					
					
					<h4>Current work-arounds:</h4>
						<ul>
							<li>Use Notepad++ to change the character encoding or end-of-line characters.
							</li>
						</ul>
					
					
					<h4>Supported:</h4>
						<ul>
							<li>Values (and column headings) optionally enclosed by double-quotes.
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
							
						</ul>
					
		
							<h3>Start</h3>
							<p><a href="../uploads">Uploads</a> [requires authentication]
							</p>
		
				
				
				</div>
				
			</div>
			
		</div>
	</body>
</html>