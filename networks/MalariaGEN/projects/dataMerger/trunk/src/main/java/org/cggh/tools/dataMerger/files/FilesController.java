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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import org.cggh.tools.dataMerger.data.uploads.UploadModel;
import org.cggh.tools.dataMerger.data.uploads.UploadsCRUD;
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
		
        //Should be able to access files using a GET request to /dataMerger/files/uploads/[id]
		//request.getPathInfo().equals("/1")
		
		//Should be able to access files using a GET request to /dataMerger/files/uploads/1
		//request.getParameter("id");
		
		

		Pattern uploadsURLPattern = Pattern.compile("^/uploads/(\\d+)$");
		 Matcher uploadsURLPatternMatcher = uploadsURLPattern.matcher(request.getPathInfo());
		 
		 Pattern exportsURLPattern = Pattern.compile("^/exports/(\\d+)/(.+)$");
		 Matcher exportsURLPatternMatcher = exportsURLPattern.matcher(request.getPathInfo());
		
		
		if (uploadsURLPatternMatcher.find()) {
			
			
			
			
			try {
				
				//FIXME: Use DataModel
				
				Class.forName(getServletContext().getInitParameter("databaseDriverFullyQualifiedClassName")).newInstance();
				
				Connection connection = DriverManager.getConnection(getServletContext().getInitParameter("databaseServerPath") + getServletContext().getInitParameter("databaseName"), getServletContext().getInitParameter("databaseUsername"), getServletContext().getInitParameter("databasePassword"));
				 
				if (connection != null) {

					// Get the user_id
					//TODO: convert to UserModel.setId
					Integer user_id = null;
					
				      try{
				          PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM user WHERE username = ?;");
				          preparedStatement.setString(1, request.getRemoteUser());
				          preparedStatement.executeQuery();
				          ResultSet resultSet = preparedStatement.getResultSet();

				          // There may be no user in the user table.
				          if (resultSet.next()) {
				        	  resultSet.first();
				        	  user_id = resultSet.getInt("id");
				          } else {
				        	  //System.out.println("Did not find user in user table. This user is not registered. Db query gives !resultSet.next()");
				          }

				          resultSet.close();
				          preparedStatement.close();
				          
				        }
				        catch(SQLException sqlException){
				        	//System.out.println("<p>" + sqlException + "</p>");
					    	sqlException.printStackTrace();
				        } 					
					
				        //TODO: Bail out if user_id is null
				        
				      try{
				    	  
				    	//TODO: convert to UploadModel.setId
				    	  Integer upload_id = Integer.parseInt(uploadsURLPatternMatcher.group(1));
				    	  
				    	  
				          PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, repository_filepath, original_filename FROM upload WHERE id = ? AND created_by_user_id = ?;");
				          preparedStatement.setInt(1, upload_id);
				          preparedStatement.setInt(2, user_id);
				          preparedStatement.executeQuery();
				          ResultSet resultSet = preparedStatement.getResultSet();
	
				          // There may be no file in the uploads table.
				          if (resultSet.next()) {
				        	  resultSet.first();
				        	  
				        	  String repository_filepath = null;
				      		String original_filename = null;
				        	  
				        	  repository_filepath = resultSet.getString("repository_filepath");
				        	  original_filename = resultSet.getString("original_filename");
				        	  
				        	  
				        	  
				        	  
				        	  
				              File                f        = new File(repository_filepath);
				              int                 length   = 0;
				              ServletOutputStream op       = response.getOutputStream();
				              ServletContext      context  = getServletConfig().getServletContext();
				              String              mimetype = context.getMimeType( repository_filepath );


				              response.setContentType( (mimetype != null) ? mimetype : "application/octet-stream" );
				              response.setContentLength( (int)f.length() );
				              response.setHeader( "Content-Disposition", "attachment; filename=\"" + original_filename + "\"" );

				           
				              //res.setContentType("application/x-download");
				              //res.setHeader("Content-Disposition", "attachment; filename=" + filename);
				              

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
				        	  //System.out.println("Db query gives !resultSet.next()");
				          }
	
				          resultSet.close();
				          preparedStatement.close();
				          
				        }
				        catch(SQLException sqlException){
				        	//System.out.println("<p>" + sqlException + "</p>");
					    	sqlException.printStackTrace();
				        } 				
					
				        //FIXME: Re-organize try-catch and introduce a finally block.
					connection.close();
					
				} else {
					
					//System.out.println("connection.isClosed");
				}
					
			} 
			catch (Exception e) {
				//System.out.println(e.getMessage().toString());
				e.printStackTrace();
			}		
		
			
			
		}
		
		else if (exportsURLPatternMatcher.find()) {
			
			
			
			
			try {
				
				//FIXME: Use DataModel
				
				Class.forName(getServletContext().getInitParameter("databaseDriverFullyQualifiedClassName")).newInstance();
				
				Connection connection = DriverManager.getConnection(getServletContext().getInitParameter("databaseServerPath") + getServletContext().getInitParameter("databaseName"), getServletContext().getInitParameter("databaseUsername"), getServletContext().getInitParameter("databasePassword"));
				 
				if (connection != null) {

					// Get the user_id
					//TODO: convert to UserModel.setId
					Integer user_id = null;
					
				      try{
				          PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM user WHERE username = ?;");
				          preparedStatement.setString(1, request.getRemoteUser());
				          preparedStatement.executeQuery();
				          ResultSet resultSet = preparedStatement.getResultSet();

				          // There may be no user in the user table.
				          if (resultSet.next()) {
				        	  
				        	  resultSet.first();
				        	  
				        	  user_id = resultSet.getInt("id");
				        	  
				        	  
						      try{
						    	  
						    	//TODO: use Models
						    	  
						    	  Integer export_id = Integer.parseInt(exportsURLPatternMatcher.group(1));
						    	  
						    	  String resourceName = exportsURLPatternMatcher.group(2);
							    
						    	  String repository_filepath = null;
						    	  
						    	  if (resourceName.equals("joins")) {
							    	  
							          PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT joins_export_repository_filepath FROM export WHERE id = ? AND created_by_user_id = ?;");
							          preparedStatement2.setInt(1, export_id);
							          preparedStatement2.setInt(2, user_id);
							          preparedStatement2.executeQuery();
							          ResultSet resultSet2 = preparedStatement2.getResultSet();
				
							          // There may be no such export.
							          if (resultSet2.next()) {
							        	  resultSet2.first();
							        	  
							        	  
							        	  repository_filepath = resultSet2.getString("joins_export_repository_filepath");
							        	  
							          } else {
							        	  //System.out.println("Db query gives !resultSet.next()");
							          }
				
							          resultSet2.close();
							          preparedStatement2.close();
				        	  
						    	  } 
						    	  else if (resourceName.equals("resolutions")) {
						    		
							          PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT resolutions_export_repository_filepath FROM export WHERE id = ? AND created_by_user_id = ?;");
							          preparedStatement2.setInt(1, export_id);
							          preparedStatement2.setInt(2, user_id);
							          preparedStatement2.executeQuery();
							          ResultSet resultSet2 = preparedStatement2.getResultSet();
				
							          // There may be no such export.
							          if (resultSet2.next()) {
							        	  resultSet2.first();
							        	  
							        	  
							        	  repository_filepath = resultSet2.getString("resolutions_export_repository_filepath");
							        	  
							          } else {
							        	  //System.out.println("Db query gives !resultSet.next()");
							          }
				
							          resultSet2.close();
							          preparedStatement2.close();
						    	  } 
						    	  else if (resourceName.equals("merged")) {
						    		  
							          PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT merged_datatable_export_repository_filepath FROM export WHERE id = ? AND created_by_user_id = ?;");
							          preparedStatement2.setInt(1, export_id);
							          preparedStatement2.setInt(2, user_id);
							          preparedStatement2.executeQuery();
							          ResultSet resultSet2 = preparedStatement2.getResultSet();
				
							          // There may be no such export.
							          if (resultSet2.next()) {
							        	  resultSet2.first();
							        	  
							        	  
							        	  repository_filepath = resultSet2.getString("merged_datatable_export_repository_filepath");
							        	  
							          } else {
							        	  //System.out.println("Db query gives !resultSet.next()");
							          }
				
							          resultSet2.close();
							          preparedStatement2.close(); 
 
						    	  } else {
							    	  //TODO
							    	  //this.log("Unrecognised resource name:" + resourceName);
							      }
							          
							          
						    	  if (repository_filepath != null) {
						        	  
						              File                f        = new File(repository_filepath);
						              int                 length   = 0;
						              ServletOutputStream op       = response.getOutputStream();
						              ServletContext      context  = getServletConfig().getServletContext();
						              String              mimetype = context.getMimeType( repository_filepath );
		
		
						              response.setContentType( (mimetype != null) ? mimetype : "application/octet-stream" );
						              response.setContentLength( (int)f.length() );
						              response.setHeader( "Content-Disposition", "attachment; filename=\"" + f.getName() + "\"" );
		
						           
						              //res.setContentType("application/x-download");
						              //res.setHeader("Content-Disposition", "attachment; filename=" + filename);
						              
		
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
					        		  //this.log("Unexpected: repository_filepath is null");
					        	  }

						      
						          
						        }
						        catch(SQLException sqlException){
						        	//System.out.println("<p>" + sqlException + "</p>");
							    	sqlException.printStackTrace();
						        } 					
							
				        //TODO: Bail out if user_id is null
				      
				          } else {
				        	  //System.out.println("Did not find user in user table. This user is not registered. Db query gives !resultSet.next()");
				          }    
					          
				          resultSet.close();
				          preparedStatement.close();
				          
				        }
				        catch(SQLException sqlException){
				        	//System.out.println("<p>" + sqlException + "</p>");
					    	sqlException.printStackTrace();
				        } 				
					
				      //FIXME: Re-organize try-catch and introduce a finally block.
					connection.close();
					
				} else {
					
					//System.out.println("connection.isClosed");
				}
					
			} 
			catch (Exception e) {
				//System.out.println(e.getMessage().toString());
				e.printStackTrace();
			}	
			
			
			
			
		} else {
			
			//System.out.println("Unhandled pathInfo.");
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
        		
        		 UploadModel uploadModel = new UploadModel();
        		 
        		 //TODO: request.getHeader("X-File-Name") works for FF but not IE
 			 	 //Could add qqfile the onSubmit of the upload.js and use request.getParameter("qqfile") instead, but this also causes unresolved problems:
        		 //Problem 1: File does not upload properly 
        		 //Problem 2: JSON response gets interpreted as a file download.
        		 
        		 
        		
        		 if (request.getHeader("X-File-Name") != null && request.getHeader("X-File-Name") != "") {
        			 
        			 uploadModel.setOriginalFilename(URLDecoder.decode(request.getHeader("X-File-Name"), "UTF-8"));
        			 
        			 DatabasesCRUD databasesCRUD = new DatabasesCRUD();
        			 DatabaseModel databaseModel = databasesCRUD.retrieveDatabaseAsDatabaseModelUsingServletContext(getServletContext());
        			 
        			 Connection connection = databaseModel.getNewConnection(); 
        			
        			 if (connection != null) {
        			 
        				 UsersCRUD usersCRUD = new UsersCRUD();
        				 usersCRUD.setDatabaseModel(databaseModel);
        				 UserModel userModel = usersCRUD.retrieveUserAsUserModelUsingUsername(request.getRemoteUser());
        		        
	        			 UploadsCRUD uploadsCRUD = new UploadsCRUD();
	        			 uploadsCRUD.createUploadUsingUploadModelAndUserModel(uploadModel, userModel, connection);
	        			 
	        			 uploadModel.setId(databasesCRUD.retrieveLastInsertIdAsIntegerUsingConnection(connection));
	        			 
	            		 uploadModel.setRepositoryFilepath(getServletContext().getInitParameter("filebaseServerPath") + "uploads" + filebaseModel.getFilepathSeparator() + uploadModel.getId());
	            		 
	            	     InputStream inputStream = null;
	            	     FileOutputStream fileOutputStream = null;
	            		 
	            	     //TODO: improve this
	            	     Boolean successful = null;
	            	     
	            	     try {
	            			 
	            			 	inputStream = request.getInputStream();
	            			 	fileOutputStream = new FileOutputStream(new File(uploadModel.getRepositoryFilepath()));
					            IOUtils.copy(inputStream, fileOutputStream);
					            
					            File uploadFile = new File(uploadModel.getRepositoryFilepath());
					            
					            if (uploadFile.exists()) {
					            	
					            	uploadModel.setFileSizeInBytes(uploadFile.length());
					            	
					            	uploadsCRUD.updateUploadRepositoryFilepathAndFileSizeInBytesUsingUploadModel(uploadModel, connection);
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
