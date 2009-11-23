package fileuploadfacility.client;

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

    String freelyAccessible();

    String notDirectlyLinked();

    String requiredConfirmation();

    String allowsFileAndFormSubmission();

    String providesConfirmationOfSubmission();

    String requiresInformation();

    String facilityInfoIntro();

    Map<String, String>  facilityInfoItems();

    String formIntro();

    String callbackFailureResponse();

    String fileCopyError();

    String fileCopySuccess();

    String agreementText();

    String nameFieldLabel();

    String emailFieldLabel();

    String phoneFieldLabel();

    String agreementCheckboxLabel();

    String formSubmitButtonText();

    String refreshButtonText();

    String fileUploaderNotFinishedPrompt();

    String supplyNamePrompt();

    String agreeWithAgreementPrompt();

    String uploadedFilePathFieldLabel();

    String locationFieldLabel();

    String studySiteFieldLabel();

    String PIFieldLabel();

    String supplyLocationPrompt();

    String supplyStudySitePrompt();

    String supplyPIPrompt();

    String supplyEmailPrompt();

    String supplyPhonePrompt();

    String uploadFileIntro();

    String formSubmitButtonIntro();

    String agreementIntro();

    String refreshButtonIntro();


    String fileHasUploaded();

    String limitsFileUploadSize();

    String fileUploadNotSuccesssfulPrompt();

    String introLink();

    String introText();
}
