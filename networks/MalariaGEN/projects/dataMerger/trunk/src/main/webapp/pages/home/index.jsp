<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="../shared/jsp/prepage.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>dataMerger - Home</title>
		
		<link rel="stylesheet" type="text/css" href="../shared/css/shared.css" />
		<script type="text/javascript" src="../shared/js/shared.js"></script>
		
	</head>
	<body>
		<div class="page">
			<%@ include file="../shared/jsp/header.jsp" %>
			<h2 class="page-title">Welcome</h2>
			
			<p>This is the dataMerger application, designed to help MalariaGEN merge clinical data files downloaded from Topheno.
			</p>
			
			<h3>Configuration:</h3>


						<h4>[dataMerger-webapp]/WEB-INF/web.xml:</h4>
						<ul> 
							<li>dbBasePath (e.g. jdbc:mysql://localhost:3306/)</li>
							<li>dbName (e.g. datamerger)</li>
							<li>dbUsername (e.g. root)</li>
							<li>dbPassword (e.g. root)</li>
							<li>uploadsFileRepositoryBasePath (e.g. C:\Lee\Work\dataMerger\files\uploads\)</li>
							<li>exportsFileRepositoryBasePath (e.g. C:\Lee\Work\dataMerger\files\exports\)</li>
						</ul>


						<h4>[tomcat-server]/tomcat-users.xml:</h4>
						<pre style="margin-left: 25px;">
&lt;-- Required roles, referred to in web.xml --&gt;
&lt;role rolename="non-specific"/&gt;
&lt;role rolename="user"/&gt;
&lt;role rolename="uploader"/&gt;
&lt;role rolename="merger"/&gt;
&lt;role rolename="exporter"/&gt;

&lt;-- Optional users --&gt;
&lt;user username="A Dmin" password="admin" roles="user,uploader,merger,exporter,non-specific" /&gt;
&lt;user username="U Ploader" password="uploader" roles="user,uploader" /&gt;
&lt;user username="M Erger" password="merger" roles="user,merger" /&gt;
&lt;user username="E Xporter" password="exporter" roles="user,exporter" /&gt;
&lt;user username="lee" password="test" roles="user,uploader,merger,exporter" /&gt;
						</pre>


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
			
			<p>This also describes the file-type of the "export" files produced using this application. (So they too can be used in merges.)
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
	</body>
</html>