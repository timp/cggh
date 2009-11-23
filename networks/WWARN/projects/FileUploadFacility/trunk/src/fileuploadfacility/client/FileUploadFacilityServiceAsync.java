package fileuploadfacility.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by IntelliJ IDEA.
 * User: lee
 * Date: 27-Aug-2009
 * Time: 11:38:39
 * To change this template use File | Settings | File Templates.
 */
public interface FileUploadFacilityServiceAsync {
    // Sample interface method of remote interface
    void processUploadFileAndForm(FormWithMetaDataForUploadedDataFile formWithMetaDataForUploadedDataFile, AsyncCallback<Boolean> async);
}
