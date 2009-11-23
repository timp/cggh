package fileuploadfacility.client;

/**
 * Created by IntelliJ IDEA.
 * User: lee
 * Date: 03-Sep-2009
 * Time: 10:47:03
 * To change this template use File | Settings | File Templates.
 */
public class Utils {
    public static String extractFilenameFromPath(String uploadedFilename) {
        return uploadedFilename.substring(uploadedFilename.lastIndexOf("\\") + 1);
    }
}
