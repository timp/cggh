package fileuploadfacility.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import fileuploadfacility.client.FileUploadFacilityService;
import fileuploadfacility.client.FormWithMetaDataForUploadedDataFile;
import fileuploadfacility.client.Utils;

import javax.servlet.ServletContext;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * User: lee
 * Date: 27-Aug-2009
 * Time: 11:38:39
 */
public class FileUploadFacilityServiceImpl extends RemoteServiceServlet implements FileUploadFacilityService {


    public Boolean processUploadFileAndForm(FormWithMetaDataForUploadedDataFile formWithMetaDataForUploadedDataFile) {

        ServletContext context = getServletContext();
        String tmpPath = context.getInitParameter("tmpPath");
        String targetPath = context.getInitParameter("targetPath");


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String dateStamp = format.format(new Date());

        if (!writeMetaFileAndMoveDataFileIntoUploadFolder(formWithMetaDataForUploadedDataFile, tmpPath, targetPath, dateStamp))
            return false;

        return true;
    }

    boolean writeMetaFileAndMoveDataFileIntoUploadFolder(FormWithMetaDataForUploadedDataFile formWithMetaDataForUploadedDataFile, String tmpPath, String targetPath, String dateStamp) {
        FileWriter formFile;
        String uploadedFilePath = formWithMetaDataForUploadedDataFile.getOriginalFileName();
        try {
            formFile = new FileWriter(targetPath + dateStamp + "_" + Utils.extractFilenameFromPath(uploadedFilePath) + ".txt");
            formFile.write(formWithMetaDataForUploadedDataFile.summarize());
            formFile.close();

            File dataFile = new File(tmpPath + Utils.extractFilenameFromPath(uploadedFilePath));

            File targetPathDir = new File(targetPath);

            // Move file to new directory
            boolean dataFileRenameSuccess = dataFile.renameTo(new File(targetPathDir, dateStamp + "_" + dataFile.getName()));
            if (!dataFileRenameSuccess) {
                // File was not successfully moved
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return false;
        }
        return true;
    }
}
