package filedownloadfacility.client;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.ui.TabBar;

import java.util.Map;


/**
 * User: lee
 * Date: 07-Sep-2009
 * Time: 14:23:52
 */

public interface MyConstants extends com.google.gwt.i18n.client.Constants {


    String pageTitle();

    String aboutLink();


    String facilityInfoIntro();

    Map<String, String>  facilityInfoItems();

    String refreshButtonText();

    String refreshButtonIntro();

    String downloadFileIntro();

    String failedToGetFilesMessage();

    String fileSelectorListHasBeenRefreshed();

    String downloadFileButtonText();

    String selectAFilePrompt();

    String availableToPermittedWebAuthUsers();

    String providesAccessToSubmittedFiles();

    String doesNotAllowServerFileModification();
}
