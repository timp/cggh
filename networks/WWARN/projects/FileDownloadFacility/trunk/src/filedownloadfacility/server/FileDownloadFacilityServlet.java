package filedownloadfacility.server;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.File;
import java.io.DataInputStream;
import java.io.FileInputStream;

/**
 * User: lee
 * Date: 09-Sep-2009
 * Time: 10:19:36
 */
public class FileDownloadFacilityServlet extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doDownload(request, response, request.getParameter("file"));

    }



    protected void doDownload(HttpServletRequest request, HttpServletResponse response, String filename) throws ServletException, IOException {

        // Only serve files from the source folder.
        filename = filename.substring(filename.lastIndexOf("\\") + 1);
        filename = filename.substring(filename.lastIndexOf("/") + 1);

        final int BUFFER_SIZE = 8192;

        try {

            ServletContext context = getServletContext();
            String sourcePath = context.getInitParameter("sourcePath");

            File file = new File(sourcePath + filename);
            int length = 0;
            ServletOutputStream outputStream = response.getOutputStream();
            String mimetype = context.getMimeType(filename);

            response.setContentType((mimetype != null) ? mimetype : "application/octet-stream");
            response.setContentLength((int) file.length());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

            byte[] bytes = new byte[BUFFER_SIZE];
            DataInputStream in = new DataInputStream(new FileInputStream(file));

            while ((length = in.read(bytes)) != -1) {
                outputStream.write(bytes, 0, length);
            }

            in.close();
            outputStream.flush();
            outputStream.close();

        }
        catch (IOException e) {

            //TODO: Handle this exception better.

            throw new ServletException("Error retrieving file.");

        }

    }


}
