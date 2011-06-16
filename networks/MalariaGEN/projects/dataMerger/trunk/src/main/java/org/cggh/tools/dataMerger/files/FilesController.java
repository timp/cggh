package org.cggh.tools.dataMerger.files;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.sql.Connection;

import java.sql.SQLException;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.cggh.tools.dataMerger.data.databases.DatabaseModel;
import org.cggh.tools.dataMerger.data.databases.DatabasesCRUD;
import org.cggh.tools.dataMerger.data.exports.ExportModel;
import org.cggh.tools.dataMerger.data.exports.ExportsCRUD;
import org.cggh.tools.dataMerger.data.files.FileModel;
import org.cggh.tools.dataMerger.data.files.FileOriginModel;
import org.cggh.tools.dataMerger.data.files.FilesCRUD;
import org.cggh.tools.dataMerger.data.users.UserModel;
import org.cggh.tools.dataMerger.data.users.UsersCRUD;
import org.cggh.tools.dataMerger.files.filebases.FilebaseModel;
import org.cggh.tools.dataMerger.files.filebases.FilebasesCRUD;



/**
 * Servlet implementation class uploads
 */
public class FilesController extends HttpServlet {


    /**
	 * 
	 */
	private static final long serialVersionUID = 6016934494099664659L;
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.files");

	/**
     * @see HttpServlet#HttpServlet()
     */
    public FilesController() {
        super();

        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//TODO: See DataController for better content-negotiation pattern
		
        //Should be able to access files using a GET request to /dataMerger/files/[id]
	

		DatabasesCRUD databasesCRUD = new DatabasesCRUD();
		DatabaseModel databaseModel = databasesCRUD.retrieveDatabaseAsDatabaseModelUsingServletContext(getServletContext());
	
		
		//TODO: Bail out if user_id is null
		UsersCRUD usersCRUD = new UsersCRUD();
		usersCRUD.setDatabaseModel(databaseModel);
		UserModel userModel = usersCRUD.retrieveUserAsUserModelUsingUsername((String)request.getSession().getAttribute("username"));
				
		
		Pattern exportRecordFilesURLPattern = Pattern.compile("^/exports/(\\d+)/(\\w+)$");
		 Matcher exportRecordFilesURLPatternMatcher = exportRecordFilesURLPattern.matcher(request.getPathInfo());
		
		Pattern filesURLPattern = Pattern.compile("^/(\\d+)$");
		 Matcher filesURLPatternMatcher = filesURLPattern.matcher(request.getPathInfo());
		
		 //Not used.
		 //String[] headerAcceptsAsStringArray = request.getHeader("Accept").split(",");
		 // List<String> headerAcceptsAsStringList = Arrays.asList(headerAcceptsAsStringArray);
		 
		
		if (exportRecordFilesURLPatternMatcher.find()) {
			
	    	  ExportsCRUD exportsCRUD = new ExportsCRUD();
	    	  exportsCRUD.setDatabaseModel(databaseModel);
	    	  
	    	  ExportModel exportModel = exportsCRUD.retrieveExportAsExportModelUsingExportIdAndUserId(Integer.parseInt(exportRecordFilesURLPatternMatcher.group(1)), userModel.getId());
			
	    	  String reportFileRequest = exportRecordFilesURLPatternMatcher.group(2);
	    	  
	    	  String reportFileFilepath = null;
	    	  
	    	  if (reportFileRequest.equals("joins")) {
	    		  reportFileFilepath = exportModel.getJoinsRecordFilepath();
	    	  }
	    	  else if (reportFileRequest.equals("resolutions")) {
	    		  reportFileFilepath = exportModel.getResolutionsRecordFilepath();
	    	  }
	    	  else if (reportFileRequest.equals("settings")) {
	    		  reportFileFilepath = exportModel.getSettingsRecordFilepath();
	    	  }
	    	  
	    	  if (reportFileFilepath != null) {
	    	  
	              File                file        = new File(reportFileFilepath);
	              int                 length   = 0;
	              ServletOutputStream op       = response.getOutputStream();
	              ServletContext      context  = getServletConfig().getServletContext();
	              String              mimetype = context.getMimeType(file.getName());
	
	
	              response.setContentType( (mimetype != null) ? mimetype : "application/octet-stream" );
	              response.setContentLength( (int)file.length() );
	              response.setHeader( "Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" );
	
	           
	
	              byte[] bbuf = new byte[2048]; //8192
	              DataInputStream in = new DataInputStream(new FileInputStream(file));
	
	              while ((in != null) && ((length = in.read(bbuf)) != -1))
	              {
	                  op.write(bbuf,0,length);
	              }
	
	              in.close();
	              op.flush();
	              op.close();	
	              
	    	  } else {
	    		  response.setContentType("text/plain");
	          	response.getWriter().print("Unhandled request.");
	    	  }
              
		}
		else if (filesURLPatternMatcher.find()) {
		

				
				
		    	  FilesCRUD filesCRUD = new FilesCRUD();
		    	  filesCRUD.setDatabaseModel(databaseModel);
		    	  FileModel fileModel = filesCRUD.retrieveFileCreatedByUserAsFileModelUsingFileIdAndUserId(Integer.parseInt(filesURLPatternMatcher.group(1)), userModel.getId());
	
		          // There may be no file or the file may not belong to this user (created_by).
		          if (fileModel.getFilename() != null) {
		        	  
		              File                f        = new File(fileModel.getFilepath());
		              int                 length   = 0;
		              ServletOutputStream op       = response.getOutputStream();
		              ServletContext      context  = getServletConfig().getServletContext();
		              String              mimetype = context.getMimeType(fileModel.getFilepath());
	
	
		              response.setContentType( (mimetype != null) ? mimetype : "application/octet-stream" );
		              response.setContentLength( (int)f.length() );
		              response.setHeader( "Content-Disposition", "attachment; filename=\"" + fileModel.getFilename() + "\"" );
	
		           
	
		              byte[] bbuf = new byte[2048]; //8192
		              DataInputStream in = new DataInputStream(new FileInputStream(f));
	
		              while ((in != null) && ((length = in.read(bbuf)) != -1))
		              {
		                  op.write(bbuf,0,length);
		              }
	
		              in.close();
		              op.flush();
		              op.close();					        	  
		        	  
		        	  
		        	  
		        	  
		        	  
		        	  
		          } else {
		        	  logger.warning("fileModel.getFilename() is null");
		          }

			
		} else {
			
			response.setContentType("text/plain");
        	response.getWriter().print("Unhandled pathInfo.");
		}
		

	    
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getPathInfo().equals("/uploads")) {
	        
        	FilebasesCRUD filebasesCRUD = new FilebasesCRUD();
        	FilebaseModel filebaseModel = filebasesCRUD.retrieveFilebaseAsFilebaseModelUsingServletContext(getServletContext());
        	
        	if (filebaseModel.isExistent() && filebaseModel.isWritable()) {
        		
        		 
        		 
        		 //TODO: request.getHeader("X-File-Name") works for FF but not IE
 			 	 //Could add qqfile the onSubmit of the upload.js and use request.getParameter("qqfile") instead, but this also causes unresolved problems:
        		 //Problem 1: File does not upload properly 
        		 //Problem 2: JSON response gets interpreted as a file download.
        		 
        		 
        		
        		 if (request.getHeader("X-File-Name") != null && request.getHeader("X-File-Name") != "") {
        			 
        			 DatabasesCRUD databasesCRUD = new DatabasesCRUD();
        			 DatabaseModel databaseModel = databasesCRUD.retrieveDatabaseAsDatabaseModelUsingServletContext(getServletContext());
        			 
        			 Connection connection = databaseModel.getNewConnection(); 
        			
        			 if (connection != null) {
        			 
        				 UsersCRUD usersCRUD = new UsersCRUD();
        				 usersCRUD.setDatabaseModel(databaseModel);
        				 UserModel userModel = usersCRUD.retrieveUserAsUserModelUsingUsername((String)request.getSession().getAttribute("username"));
        		     
        				 FileOriginModel fileOriginModel = new FileOriginModel();
        				 // Short-cut
        				 fileOriginModel.setId(1);
        				 
        				 FileModel fileModel = new FileModel();
            			 fileModel.setFilename(URLDecoder.decode(request.getHeader("X-File-Name"), "UTF-8"));
            			 fileModel.setCreatedByUserModel(userModel);
            			 fileModel.setFileOriginModel(fileOriginModel);
        				 
	        			 FilesCRUD filesCRUD = new FilesCRUD();
	        			 filesCRUD.createFileUsingFileModel(fileModel, connection);
	        			 
	        			 
	        			 fileModel.setId(databasesCRUD.retrieveLastInsertIdAsIntegerUsingConnection(connection));
	            		 fileModel.setFilepath(getServletContext().getInitParameter("fileRepositoryBasePath") + "uploads" + filebaseModel.getFilepathSeparator() + fileModel.getId());
	            		 
	            		 
	            		 
	            	     InputStream inputStream = null;
	            	     FileOutputStream fileOutputStream = null;
	            		 
	            	     //TODO: improve this
	            	     Boolean successful = null;
	            	     
	            	     try {
	            			 
	            			 	inputStream = request.getInputStream();
	            			 	fileOutputStream = new FileOutputStream(new File(fileModel.getFilepath()));
					            IOUtils.copy(inputStream, fileOutputStream);
					            
					            File uploadFile = new File(fileModel.getFilepath());
					            
					            if (uploadFile.exists()) {
					            	
					            	fileModel.setFileSizeInBytes(uploadFile.length());
					            	
					            	filesCRUD.updateFileFilepathAndFileSizeInBytesUsingFileModel(fileModel, connection);
					            	successful = true;
					            	
					            } else {
					            	
					            	successful = false;
					            }
					            
					            
					            
					            
					            
	            	     } catch (FileNotFoundException ex) {
					        
	            	    	 successful = false;
					         ex.printStackTrace();
					         
					        	
	            	     } catch (IOException ex) {
					        	
	            	    	 successful = false;
					        ex.printStackTrace();

					     } finally {
					    	 
					    	 try {
					    		 fileOutputStream.close();
					             inputStream.close();
					         } catch (IOException ex) {
					        	 ex.printStackTrace();
					         }
					     }	
	            		 
	            		 
	            		 
	            		 
	            		 try {
	            			//FIXME: Re-organize try-catch and introduce a finally block.
							connection.close();
	            		 } catch (SQLException e) {
							e.printStackTrace();
	            		 }
	            		 
	            		//TODO: content negotiation
	            		 if (successful) {
	            			 
	            	         response.setContentType("application/json");
	            			 response.setStatus(HttpServletResponse.SC_OK);
	            			 response.getWriter().print("{\"success\": \"true\"}");
					         
	            		 } else {
	            			 
	            			 response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            			 response.setContentType("application/json");
	            			 response.getWriter().print("{\"success\": \"false\"}");
	            		 }
	            		 
	            		
        			 } else {
        				 
        				 logger.severe("connection is null");
        			 }
        			 
        		 } else {
        			 
        			 logger.severe("request header X-File-Name is null or empty");
        			 
        		 }
        		
        	}
        	
        	

        } else {
        	
        	//
        	
        	//TODO: content negotiation
        	response.setContentType("text/plain");
        	response.getWriter().print("Unhandled pathInfo.");
        	
        }

	}

	
	
}
