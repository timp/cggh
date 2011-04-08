<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="../../../shared/jsp/prepage.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>dataMerger - Home - Welcome</title>
		
		<link rel="stylesheet" type="text/css" href="../../../shared/css/shared.css" />
		<link rel="stylesheet" type="text/css" href="../../css/home.css" />
		<script type="text/javascript" src="../../../shared/js/shared.js"></script>
		
	</head>
	<body>
		<div class="page">
			<%@ include file="../../../shared/jsp/header.jsp" %>
			<h2 class="page-title">Home</h2>
	
			<div class="guides-container">
			
				<%@ include file="../../jsp/guides-menu.jsp" %>
				
				<div class="guide">
					
	
		
					<h3>Usage:</h3>
					
					<p>The file format for the source files ("uploads") is expected to be the same as the "calculated data" downloaded from Topheno.
					</p>
					
					<p>Specifically:
						<ul>
							<li>ISO-8859-1 character-set encoding (a.k.a. Latin-1) 
							</li>
							<li>The Unix-style end-of-line character (i.e. \n, Line Feed, LF)
							</li>
							<li>Comma-separated values
							</li>
						</ul>
					</p>
					
					<p>This also describes the file-type of the "export" files produced using this application. (So they too can be used in merges.) Exported files may contain \N characters (NULL), whereas Topheno exports currently only use empty strings to represent NULL (both are treated as equivalent).
					</p>
					
					<p><em>Note:</em> Saving such as file in Microsoft Excel will result in a different (as yet unsupported file type) 
					</p>
					
					<p>Not yet supported (when saved by Excel):
						<ul>
							<li>ANSI and UTF8 character sets. (Ideally, Topheno should export as UTF8.)
							</li>
							<li>Windows-style end-of-line character (i.e. \n\r, Carriage Return with Line Feed, CR+LF)
							</li>
						</ul>
					</p>
					<p>Symptoms:	
						<ul>
							<li>Unsupported character-encoding: Odd characters appearing in column headings. Potential application failures.
							</li>
							<li>Unsupported end-of-line character: Right-most cells contain an unexpected last character (i.e. \r). Potential application failures, esp. conflict counting and resolution.
							</li>
						</ul>
					</p>
					
					<p>Current work-arounds:
						<ul>
							<li>Use Notepad++ to change the character encoding or end-of-line characters.
							</li>
						</ul>
					</p>
					
					<p>Supported:
						<ul>
							<li>Values (and column headings) optionally enclosed by double-quotes.
							</li>
						</ul>
					</p>
		
							<h3>Start</h3>
							<p><a href="../uploads">Uploads</a> [requires authentication]
							</p>
		
				
				
				</div>
				
			</div>
			
		</div>
	</body>
</html>