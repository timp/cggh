package fileuploadfacility.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Created by IntelliJ IDEA.
 * User: lee
 * Date: 27-Aug-2009
 * Time: 11:38:38
 * To change this template use File | Settings | File Templates.
 */
public interface FileUploadFacilityService extends RemoteService {
    // Sample interface method of remote interface
    Boolean processUploadFileAndForm(FormWithMetaDataForUploadedDataFile formWithMetaDataForUploadedDataFile);

    /**
     * Utility/Convenience class.
     * Use FileUploadFacilityService.App.getInstance () to access static instance of FileUploadFacilityServiceAsync
     */
    public static class App {
        private static FileUploadFacilityServiceAsync app = null;

        public static synchronized FileUploadFacilityServiceAsync getInstance() {
            if (app == null) {
                app = (FileUploadFacilityServiceAsync) GWT.create(FileUploadFacilityService.class);
                ((ServiceDefTarget) app).setServiceEntryPoint(GWT.getModuleBaseURL() + "fileuploadfacility.FileUploadFacility/FileUploadFacilityService");
            }
            return app;
        }
    }
}
