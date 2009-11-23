package fileuploadfacility.server;

import gwtupload.server.UploadAction;

import java.io.*;
import java.util.Vector;

import org.apache.commons.fileupload.FileItem;
import fileuploadfacility.client.Utils;

import javax.servlet.ServletContext;

/**
 * User: lee
 * Date: 27-Aug-2009
 * Time: 14:06:00
 */
public class FileUploadFacilityServlet extends UploadAction {
    public String doAction(Vector<FileItem> sessionFiles) throws IOException {
        for (FileItem fileItem : sessionFiles) {

            String uploadedFilename;
            uploadedFilename = fileItem.getName();

            ServletContext context = getServletContext();
            String tmpPath = context.getInitParameter("tmpPath");

            File tmpPathDir = new File(tmpPath);
            if (tmpPathDir.exists() && tmpPathDir.canWrite()) {
                try {
                    writeFileToFolder(fileItem, uploadedFilename, tmpPath);
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            } else {
                  return "Error: Could not upload file to the server.";
            }
        }

        return "";
    }

    void writeFileToFolder(FileItem fileItem, String uploadedFilename, String tmpPath) throws Exception {
        fileItem.write(new File(tmpPath + Utils.extractFilenameFromPath(uploadedFilename)));
    }
}
