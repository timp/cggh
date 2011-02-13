package org.cggh.tools.dataMerger.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javax.servlet.ServletException;
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

		//TODO:
        System.out.println("doing get");
	    
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
        String realPath = getServletContext().getInitParameter("fileRepositoryBasePath");
        
        try {
            writer = response.getWriter();
        } catch (IOException ex) {
            log(FilesController.class.getName() + "has thrown an exception: " + ex.getMessage());
        }

        //TODO
        log("test");
        
        String filename = request.getHeader("X-File-Name");
        
        //Register the upload, get the file id
        Integer user_id = null;
        Integer file_id = null;
        
        Boolean successful = null;
        
		try {
			
			Class.forName("com.mysql.jdbc.Driver").newInstance(); 
			Connection connection = DriverManager.getConnection(getServletContext().getInitParameter("dbBasePath") + getServletContext().getInitParameter("dbName"), getServletContext().getInitParameter("dbUsername"), getServletContext().getInitParameter("dbPassword"));
			 
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
			        	  user_id = resultSet.getInt("id");
			          } else {
			        	  System.out.println("Did not find user in user table. This user is not registered. Db query gives !resultSet.next()");
			          }

			          resultSet.close();
			          preparedStatement.close();
			          
			        }
			        catch(SQLException sqlException){
				    	sqlException.printStackTrace();
			        } 					
				
		      try {
		          PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO upload (original_filename, created_by_user_id, created_on_datetime) VALUES (?, ?, NOW());");
		          preparedStatement.setString(1, filename);
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
			        	  
			        	  file_id = resultSet.getInt(1);
			        	  
			          } else {
			        	  
			        	  
			        	  System.out.println("Unexpected: !resultSet.next()");
			          }
			          
			          preparedStatement.close();

			        }
			        catch(SQLException sqlException){
				    	sqlException.printStackTrace();
			        }		   
			        
			        
				      try{
				          PreparedStatement preparedStatement = connection.prepareStatement("UPDATE upload SET repository_path=? WHERE id=?;");
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
				            writer.print("{success: true}");
				            successful = true;
				        } catch (FileNotFoundException ex) {
				            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				            writer.print("{success: false}");
				            log(FilesController.class.getName() + "has thrown an exception: " + ex.getMessage());
				            successful = false;
				        } catch (IOException ex) {
				            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				            writer.print("{success: false}");
				            log(FilesController.class.getName() + "has thrown an exception: " + ex.getMessage());
				            successful = false;
				        } finally {
				            try {
				                fos.close();
				                is.close();
				            } catch (IOException ignored) {
				            }
				        }				        
				     
					      try{
					          PreparedStatement preparedStatement = connection.prepareStatement("UPDATE upload SET repository_path=?, successful=? WHERE id=?;");
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
				System.out.println("Done.");
				
			} else {
				System.out.println("connection.isClosed");
			}
				
		} 
		catch (Exception exception) {
			System.out.println("Failed to connect to database server.");
			exception.printStackTrace();
		}
   
        
        


        
        
        
        
        
        
        
        
        
        writer.flush();
        writer.close();
		
        
        
        
		
	}

}
