package filedownloadfacility.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by IntelliJ IDEA.
 * User: lee
 * Date: 08-Sep-2009
 * Time: 15:40:54
 * To change this template use File | Settings | File Templates.
 */
public interface FileDownloadFacilityServiceAsync {
    void grabListOfUploadedFiles(AsyncCallback<String[]> async);
}
