package filedownloadfacility.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Created by IntelliJ IDEA.
 * User: lee
 * Date: 08-Sep-2009
 * Time: 15:40:54
 * To change this template use File | Settings | File Templates.
 */
public interface FileDownloadFacilityService extends RemoteService {

    String[] grabListOfUploadedFiles();

    /**
     * Utility/Convenience class.
     * Use FileDownloadFacilityService.App.getInstance () to access static instance of FileDownloadFacilityServiceAsync
     */
    public static class App {
        private static FileDownloadFacilityServiceAsync app = null;

        public static synchronized FileDownloadFacilityServiceAsync getInstance() {
            if (app == null) {
                app = (FileDownloadFacilityServiceAsync) GWT.create(FileDownloadFacilityService.class);
                ((ServiceDefTarget) app).setServiceEntryPoint(GWT.getModuleBaseURL() + "filedownloadfacility.FileDownloadFacility/FileDownloadFacilityService");
            }
            return app;
        }
    }
}
