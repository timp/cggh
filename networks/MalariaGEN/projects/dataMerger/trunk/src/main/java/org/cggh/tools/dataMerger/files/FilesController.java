package org.cggh.tools.dataMerger.files;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;



/**
 * Servlet implementation class uploads
 */
public class FilesController extends HttpServlet {


    /**
	 * 
	 */
	private static final long serialVersionUID = 6016934494099664659L;

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
				
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				
				Connection connection = DriverManager.getConnection(getServletContext().getInitParameter("databaseBasePath") + getServletContext().getInitParameter("databaseName"), getServletContext().getInitParameter("databaseUsername"), getServletContext().getInitParameter("databasePassword"));
				 
				if (!connection.isClosed()) {

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
				
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				
				Connection connection = DriverManager.getConnection(getServletContext().getInitParameter("databaseBasePath") + getServletContext().getInitParameter("databaseName"), getServletContext().getInitParameter("databaseUsername"), getServletContext().getInitParameter("databasePassword"));
				 
				if (!connection.isClosed()) {

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

        PrintWriter writer = null;
        InputStream is = null;
        FileOutputStream fos = null;

        //final String DESTINATION_DIR_PATH = "files";
        //String realPath = getServletContext().getRealPath(DESTINATION_DIR_PATH) + "/";
        
        if (request.getPathInfo().equals("/uploads")) {
	        
        	//TODO: Proper content-negotiation
        	response.setContentType("application/json");
        	
	        String realPath = getServletContext().getInitParameter("uploadsFileRepositoryBasePath");
	        
	        try {
	            writer = response.getWriter();
	        } catch (IOException ex) {
	            //this.log(FilesController.class.getName() + "has thrown an exception: " + ex.getMessage());
	        }
	
	        String filename = request.getHeader("X-File-Name");
	        
	        //Register the upload, get the file id
	        Integer user_id = null;
	        Integer file_id = null;
	        
	        Boolean successful = null;
	        
			try {
				
				Class.forName("com.mysql.jdbc.Driver").newInstance(); 
				Connection connection = DriverManager.getConnection(getServletContext().getInitParameter("databaseBasePath") + getServletContext().getInitParameter("databaseName"), getServletContext().getInitParameter("databaseUsername"), getServletContext().getInitParameter("databasePassword"));
				 
				if (!connection.isClosed()) {
	
					// Get the user_id
					
					
				      try {
				          PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM user WHERE username = ?;");
				          preparedStatement.setString(1, request.getRemoteUser());
				          preparedStatement.executeQuery();
				          ResultSet resultSet = preparedStatement.getResultSet();
	
				          // There may be no user in the user table.
				          if (resultSet.next()) {
				        	  //TODO: Is it worth checking for id is null?
				        	  resultSet.first();
				        	  user_id = resultSet.getInt("id");
				          } else {
				        	  //System.out.println("Did not find user in user table. This user is not registered. Db query gives !resultSet.next()");
				          }
	
				          resultSet.close();
				          preparedStatement.close();
				          
				        }
				        catch(SQLException sqlException){
					    	sqlException.printStackTrace();
				        } 					
					
				        
				        //TODO: Shouldn't this be in the UploadsModel? Or this should be a generic file table.
			      try {
			          PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO upload (original_filename, created_by_user_id, created_datetime) VALUES (?, ?, NOW());");
			          preparedStatement.setString(1, URLDecoder.decode(filename, "UTF-8"));
			          preparedStatement.setInt(2, user_id);
			          preparedStatement.executeUpdate();
			          preparedStatement.close();
	
			        }
			        catch(SQLException sqlException){
				    	sqlException.printStackTrace();
			        } 					        
				        
			        
				      try{
				    	  //TODO: Is this cross-db compatible? ref: @@IDENTITY
				          PreparedStatement preparedStatement = connection.prepareStatement("SELECT LAST_INSERT_ID();");
				          preparedStatement.executeQuery();
				          
				          ResultSet resultSet = preparedStatement.getResultSet();
				          if (resultSet.next()) {
				        	  
				        	  resultSet.first();
				        	  file_id = resultSet.getInt(1);
				        	  
				          } else {
				        	  
				        	  
				        	  //System.out.println("Unexpected: !resultSet.next()");
				          }
				          
				          preparedStatement.close();
	
				        }
				        catch(SQLException sqlException){
					    	sqlException.printStackTrace();
				        }		   
				        
				        
					      try{
					          PreparedStatement preparedStatement = connection.prepareStatement("UPDATE upload SET repository_filepath=? WHERE id=?;");
					          preparedStatement.setString(1, realPath + file_id);
					          preparedStatement.setInt(2, file_id);
					          preparedStatement.executeUpdate();
					          preparedStatement.close();
	
					        }
					        catch(SQLException sqlException){
						    	sqlException.printStackTrace();
					        } 			        
	
					        //TODO: if file_id is not null
					        
					        try {
					        	is = request.getInputStream();
					            fos = new FileOutputStream(new File(realPath + file_id));
					            IOUtils.copy(is, fos);
					            response.setStatus(HttpServletResponse.SC_OK);
					            writer.print("{\"success\": \"true\"}");
					            successful = true;
					        } catch (FileNotFoundException ex) {
					            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					            writer.print("{\"success\": \"false\"}");
					            //this.log(FilesController.class.getName() + "has thrown an exception: " + ex.getMessage());
					            successful = false;
					        } catch (IOException ex) {
					            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					            writer.print("{\"success\": \"false\"}");
					            //this.log(FilesController.class.getName() + "has thrown an exception: " + ex.getMessage());
					            successful = false;
					        } finally {
					            try {
					                fos.close();
					                is.close();
					            } catch (IOException ignored) {
					            }
					        }				        
					     
						      try{
						          PreparedStatement preparedStatement = connection.prepareStatement("UPDATE upload SET repository_filepath=?, successful=? WHERE id=?;");
						          preparedStatement.setString(1, realPath + file_id);
						          preparedStatement.setBoolean(2, successful);
						          preparedStatement.setInt(3, file_id);
						          preparedStatement.executeUpdate();
						          preparedStatement.close();
	
						        }
						        catch(SQLException sqlException){
							    	sqlException.printStackTrace();
						        } 				        
					        
					connection.close();
					
				} else {
					//System.out.println("connection.isClosed");
				}
					
			} 
			catch (Exception exception) {
				//System.out.println("Failed to connect to database server.");
				exception.printStackTrace();
			}

        } else {
        	
        	//System.out.println("Unhandled pathInfo.");
        }
        
        writer.flush();
        writer.close();

		
	}

	
	
}
