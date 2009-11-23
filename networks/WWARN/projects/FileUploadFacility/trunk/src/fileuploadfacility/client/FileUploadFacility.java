package fileuploadfacility.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import gwtupload.client.*;

import java.util.Map;

public class FileUploadFacility implements EntryPoint {


    private MyConstants constants = GWT.create(MyConstants.class);


     private Boolean dataFileUploaderHasFinished = false;
     private Boolean dataFileUploadWasSuccessful = false;


     private FormWithMetaDataForUploadedDataFile formWithMetaDataForUploadedDataFile = new FormWithMetaDataForUploadedDataFile();

    public void onModuleLoad() {

        RootPanel.get().clear();
        RootPanel.get().add(WebpageAsWidget());


    }

    public VerticalPanel WebpageAsWidget() {

        // Create a panel for the webpageWithoutDecoration.
        final VerticalPanel webpageWithoutDecoration = new VerticalPanel();

        // Create a panel for the required input portion of the webpageWithoutDecoration.
        final VerticalPanel requiredInputPanel = new VerticalPanel();

        // Create a panel for the about facility info.
        VerticalPanel aboutFacilityPanel = new VerticalPanel();

        // Get the facility info map hash from the constants interface.
        Map<String, String> facilityInfoItemsMap = constants.facilityInfoItems();

        // If there are items, add them to the panel as a HTML list.
        if (facilityInfoItemsMap.size() > 0) {

            String facilityInfoList = "";

            facilityInfoList += constants.facilityInfoIntro();

            facilityInfoList += "<ul>";

            for (Map.Entry<String, String> entry : facilityInfoItemsMap.entrySet()) {
                facilityInfoList += "<li>" + entry.getValue() + "</li>";
            }
            facilityInfoList += "</ul>";

            aboutFacilityPanel.add(new HTML(facilityInfoList));
        }

        // Add a disclosure panel to the about facility info.
        DisclosurePanel aboutThisFacilityDisclosurePanel = new DisclosurePanel(constants.aboutLink());
        aboutThisFacilityDisclosurePanel.setAnimationEnabled(true);
        aboutThisFacilityDisclosurePanel.setContent(aboutFacilityPanel);

        // Create a panel for the intro/disclaimer.
        VerticalPanel introPanel = new VerticalPanel();
        introPanel.add(new HTML(constants.introText()));

        // Add a disclosure panel to the intro/disclaimer.
        DisclosurePanel introDisclosurePanel = new DisclosurePanel(constants.introLink());
        introDisclosurePanel.setAnimationEnabled(true);
        introDisclosurePanel.setContent(introPanel);
        introDisclosurePanel.setOpen(true);

        // Create a refresh button
        final Button refreshButton = new Button(constants.refreshButtonText());
        MyReloadButtonClickListener myReloadButtonClickListener = new MyReloadButtonClickListener();
        refreshButton.addClickHandler(myReloadButtonClickListener);

        // Create a panel for the uploader.
        VerticalPanel fileUploaderPanel = new VerticalPanel();

        BasicProgress progressBar = new BasicProgress();
        final SingleUploader singleFileUploader = new SingleUploader(progressBar);
        singleFileUploader.setAutoSubmit(true);

        final Label callbackResponse = new Label();


        final AsyncCallback<Boolean> asyncCallback = new AsyncCallback<Boolean>() {
            public void onFailure(Throwable caught) {
                callbackResponse.setText(constants.callbackFailureResponse());
            }

            public void onSuccess(Boolean fileOperationSuccessful) {

                webpageWithoutDecoration.remove(requiredInputPanel);
                webpageWithoutDecoration.add(refreshButton);

                if (fileOperationSuccessful) {
                    callbackResponse.setText(constants.fileCopySuccess());
                } else {
                    callbackResponse.setText(constants.fileCopyError());
                }


            }

        };

        ValueChangeHandler<IUploader> onFinishHandler = new ValueChangeHandler<IUploader>() {
            public void onValueChange(ValueChangeEvent<IUploader> event) {

                formWithMetaDataForUploadedDataFile = new FormWithMetaDataForUploadedDataFile();

                formWithMetaDataForUploadedDataFile.setUploadedFilename(Utils.extractFilenameFromPath(singleFileUploader.getFilePath()));

                dataFileUploaderHasFinished = true;

                  /**
                   * Get the process status
                   * status possible values are:
                   *     UNINITIALIZED = 0;
                   *     QUEUED = 1;
                   *     INPROGRESS = 2;
                   *     FINISHED = 3;
                   *     ERROR = 4;
                   */

                if (singleFileUploader.getStatusWidget().getStatus() == 3) {
                    dataFileUploadWasSuccessful = true;
                }


                //Window.alert(constants.fileHasUploaded());

                event.toString();

            }
        };
        singleFileUploader.setOnFinishHandler(onFinishHandler);

        fileUploaderPanel.add(singleFileUploader);


        // Create a flex table to layout the form
        FlexTable formLayout = new FlexTable();
        formLayout.setCellPadding(5);

        FlexTable.FlexCellFormatter formLayoutCellFormatter = formLayout.getFlexCellFormatter();

        formLayoutCellFormatter.setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
        formLayoutCellFormatter.setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
        formLayoutCellFormatter.setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
        formLayoutCellFormatter.setHorizontalAlignment(4, 0, HasHorizontalAlignment.ALIGN_RIGHT);
        formLayoutCellFormatter.setHorizontalAlignment(5, 0, HasHorizontalAlignment.ALIGN_RIGHT);
        formLayoutCellFormatter.setHorizontalAlignment(6, 0, HasHorizontalAlignment.ALIGN_RIGHT);

        // Add form fields and their labels
        final TextBox locationTextBox = new TextBox();
        final TextBox studySiteTextBox = new TextBox();
        final TextBox PITextBox = new TextBox();
        final TextBox nameTextBox = new TextBox();
        final TextBox emailTextBox = new TextBox();
        final TextBox phoneTextBox = new TextBox();


        formLayout.setHTML(1, 0, constants.locationFieldLabel());
        formLayout.setHTML(2, 0, constants.studySiteFieldLabel());
        formLayout.setHTML(3, 0, constants.PIFieldLabel());
        formLayout.setHTML(4, 0, constants.nameFieldLabel());
        formLayout.setHTML(5, 0, constants.emailFieldLabel());
        formLayout.setHTML(6, 0, constants.phoneFieldLabel());

        formLayout.setWidget(1, 1, locationTextBox);
        formLayout.setWidget(2, 1, studySiteTextBox);
        formLayout.setWidget(3, 1, PITextBox);
        formLayout.setWidget(4, 1, nameTextBox);
        formLayout.setWidget(5, 1, emailTextBox);
        formLayout.setWidget(6, 1, phoneTextBox);


        // make an agreement scroller
        HTML agreementText = new HTML(constants.agreementText());
        ScrollPanel agreementScroller = new ScrollPanel(agreementText);


        // make an agreement checkbox
        final CheckBox agreementCheckBoxAndLabel = new CheckBox(constants.agreementCheckboxLabel());

        // make a submit button
        Button submitButton = new Button(constants.formSubmitButtonText());


        submitButton.addClickHandler(

                new ClickHandler() {

                    public void onClick(ClickEvent event) {

                        // Get the form field values.

                        formWithMetaDataForUploadedDataFile.setLocation(locationTextBox.getText());
                        formWithMetaDataForUploadedDataFile.setStudySite(studySiteTextBox.getText());
                        formWithMetaDataForUploadedDataFile.setPI(PITextBox.getText());
                        formWithMetaDataForUploadedDataFile.setName(nameTextBox.getText());
                        formWithMetaDataForUploadedDataFile.setEmail(emailTextBox.getText());
                        formWithMetaDataForUploadedDataFile.setPhone(phoneTextBox.getText());

                        Boolean agreedTerms = agreementCheckBoxAndLabel.getValue();


                        // Validate the form.

                        if (!dataFileUploaderHasFinished) {
                            Window.alert(constants.fileUploaderNotFinishedPrompt());
                        } else if (!dataFileUploadWasSuccessful) {
                            Window.alert(constants.fileUploadNotSuccesssfulPrompt());
                        } else if (locationTextBox.getText().equals("")) {
                            Window.alert(constants.supplyLocationPrompt());
                        } else if (studySiteTextBox.getText().equals("")) {
                            Window.alert(constants.supplyStudySitePrompt());
                        } else if (PITextBox.getText().equals("")) {
                            Window.alert(constants.supplyPIPrompt());
                        } else if (nameTextBox.getText().equals("")) {
                            Window.alert(constants.supplyNamePrompt());
                        } else if (emailTextBox.getText().equals("")) {
                            Window.alert(constants.supplyEmailPrompt());
                        } else if (phoneTextBox.getText().equals("")) {
                            Window.alert(constants.supplyPhonePrompt());
                        } else if (!agreedTerms) {
                            Window.alert(constants.agreeWithAgreementPrompt());
                        } else {

                            // Send the form field values to the upload facility service.

                            if (callbackResponse.getText().equals("")) {
                                FileUploadFacilityService.App.getInstance().processUploadFileAndForm(formWithMetaDataForUploadedDataFile, asyncCallback);
                            } else {
                                callbackResponse.setText("");
                            }

                        }
                    }
                }
        );


        // Add the styles.
        webpageWithoutDecoration.addStyleName("page");
        aboutThisFacilityDisclosurePanel.addStyleName("aboutFacility");
        aboutFacilityPanel.addStyleName("aboutFacilityPanel");
        introDisclosurePanel.addStyleName("intro");
        introPanel.addStyleName("introPanel");
        fileUploaderPanel.addStyleName("uploaderPanel");
        formLayout.addStyleName("form");
        agreementScroller.addStyleName("agreement");
        agreementCheckBoxAndLabel.addStyleName("agreementCheckboxAndLabel");
        submitButton.addStyleName("submitButton");
        callbackResponse.addStyleName("callbackResponse");
        refreshButton.addStyleName("refreshButton");

        // Other programmatic styles
        agreementScroller.setSize("auto", "100px");

        // Assemble the required input panel.
        // If there is info to show, add the disclosure panel.
        if (facilityInfoItemsMap.size() > 0) {
            requiredInputPanel.add(aboutThisFacilityDisclosurePanel);
        }


        requiredInputPanel.add(introDisclosurePanel);

        requiredInputPanel.add(new HTML("<div class=\"step\">" + constants.refreshButtonIntro() + "</div>"));
        requiredInputPanel.add(refreshButton);
        requiredInputPanel.add(new HTML("<div class=\"step\">" + constants.uploadFileIntro() + "</div>"));
        requiredInputPanel.add(fileUploaderPanel);
        requiredInputPanel.add(new HTML("<div class=\"step\">" + constants.formIntro() + "</div>"));
        requiredInputPanel.add(formLayout);
        requiredInputPanel.add(new HTML("<div class=\"step\">" + constants.agreementIntro() + "</div>"));
        requiredInputPanel.add(agreementScroller);
        requiredInputPanel.add(agreementCheckBoxAndLabel);
        requiredInputPanel.add(new HTML("<div class=\"step\">" + constants.formSubmitButtonIntro() + "</div>"));
        requiredInputPanel.add(submitButton);

        // Assemble the webpageWithoutDecoration
        //webpageWithoutDecoration.add(new HTML("<h1>" + constants.pageTitle() + "</h1>"));

        webpageWithoutDecoration.add(requiredInputPanel);
        webpageWithoutDecoration.add(callbackResponse);


        Image phamacologyIcon = new Image("images/pharmacology_module_50pxsq.gif");

        HorizontalPanel titlePanel = new HorizontalPanel();
        titlePanel.add(phamacologyIcon);
        titlePanel.add(new HTML(constants.pageTitle()));

        DecoratedStackPanel decoratedStackPanel = new DecoratedStackPanel();
        decoratedStackPanel.add(webpageWithoutDecoration, titlePanel.getElement().getString(), true);


        VerticalPanel headedDecoratedWebpage = new VerticalPanel();
        Image wwarnLogo = new Image("images/wwarn_logo.gif");
        headedDecoratedWebpage.add(wwarnLogo);

        headedDecoratedWebpage.add(decoratedStackPanel);

        return headedDecoratedWebpage;
    }

    private class MyReloadButtonClickListener implements ClickHandler {


        public void onClick(ClickEvent event) {
            reload();//To change body of implemented methods use File | Settings | File Templates.
        }
    }


    private native void reload() /*-{
    $wnd.location.reload();
   }-*/;


}


