package filedownloadfacility.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import filedownloadfacility.client.FileDownloadFacilityService;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.Comparator;

/**
 * User: lee
 * Date: 08-Sep-2009
 * Time: 15:40:54
 */
public class FileDownloadFacilityServiceImpl extends RemoteServiceServlet implements FileDownloadFacilityService {

    public String[] grabListOfUploadedFiles () {


        ServletContext context = getServletContext();
        String sourcePath = context.getInitParameter("sourcePath");
        File myFiles = new File(sourcePath);

        String[] uploadedFiles = myFiles.list();

        ReverseStringComparator reverseStringComparator = new ReverseStringComparator();

        java.util.Arrays.sort(uploadedFiles, reverseStringComparator);

        return uploadedFiles;
    }




    
}

