package org.cggh.tools.dataMerger.files;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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

		PrintWriter out = response.getWriter();		
        //response.setContentType("application/json");
		response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        //Return
        //{"success":true} when upload was successful
        //{"error":"error message to display"} in case of error
        
        
        //TODO:
        System.out.println("doing post");
        
        //TODO: Factor these out into separate classes. Use interfaces?
        
		if (request.getPathInfo().equals("/upload")) {
			
			//InstallDb_0_0_1 installDb_0_0_1 = new InstallDb_0_0_1();
			//installDb_0_0_1.run();
			
			//ScriptController scriptController = new ScriptController("install-db-v0.0.1");
			//scriptController.run();
			
			//dataMerger.scripts.getScript("install-db-v0.0.1").run();
			
			//dataMerger.scripts.runScript("install-db-v0.0.1");
			
			System.out.println("uploading...");
			
			out.println("{\"error\":\"error message to display\"}");
			
		} else {
			
//	     	{
//	  	  "foo": "The quick brown fox jumps over the lazy dog.",
//	  	  "bar": "ABCDEFG",
//	  	  "baz": [52, 97]
//	  	}
	   		
			
	        JSONArray list = new JSONArray();
	        list.put(52);
	        list.put("er2... " + request.getPathInfo());		
			
			 JSONObject json = new JSONObject();
			 
	         try {
	 
	             json.put("foo", "The quick brown fox jumps over the lazy dog.");
	 
	             json.put("bar", "ABCDEFG");

	             json.put("baz", list);
	 
	 
	         } catch (JSONException e) {
	 
	             e.printStackTrace();
	 
	         }

	        

	         
	         response.setContentType("application/json");
	         response.setCharacterEncoding("UTF-8");
	         out.println(json);

	         out.close();			
			
		}


		
		
	}

}
