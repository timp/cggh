package filedownloadfacility.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.Map;

public class FileDownloadFacility implements EntryPoint {


    MyConstants constants = GWT.create(MyConstants.class);

    public void onModuleLoad() {

        // Create a panel for the page.
        final VerticalPanel page = new VerticalPanel();

        // Create a panel for the required input portion of the page.
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

        // Create a panel for the file selector.
        final HorizontalPanel fileSelectorPanel = new HorizontalPanel();

        // Add a drop box with the list types
        final ListBox fileSelector = new ListBox(false);
        fileSelector.setVisibleItemCount(10);

        // Set up the get files async call.
        final AsyncCallback<String[]> asyncCallback = new AsyncCallback<String[]>() {

            public void onFailure(Throwable caught) {
                fileSelectorPanel.clear();
                fileSelectorPanel.add(new HTML(constants.failedToGetFilesMessage()));
            }

            public void onSuccess(String[] listOfFiles) {

                fileSelectorPanel.clear();
                fileSelector.clear();

                for (int i = 0; i < listOfFiles.length; i++) {
                    fileSelector.addItem(listOfFiles[i]);
                }
                fileSelectorPanel.add(fileSelector);

            }


        };

        // Create a panel for the download buttons
        HorizontalPanel downloadButtonsPanel = new HorizontalPanel();

        // Create a download file button
        Button downloadFileButton = new Button(constants.downloadFileButtonText());

        downloadFileButton.addClickHandler(

                new ClickHandler() {

                    public void onClick(ClickEvent event) {

                        if (fileSelector.getSelectedIndex() != -1) {

                            Window.open(GWT.getModuleBaseURL() + "FileDownloadFacilityServlet" + "?file=" + fileSelector.getItemText(fileSelector.getSelectedIndex()), "downloadWindow", "scrollbar, resizable");

                        } else {
                            Window.alert(constants.selectAFilePrompt());
                        }

                    }
                }
        );

        downloadButtonsPanel.add(downloadFileButton);

        // Create a refresh button
        Button refreshButton = new Button(constants.refreshButtonText());

        refreshButton.addClickHandler(

                new ClickHandler() {

                    public void onClick(ClickEvent event) {

                        fileSelector.setStyleName("refreshing");

                        FileDownloadFacilityService.App.getInstance().grabListOfUploadedFiles(asyncCallback);

                        fileSelector.setStyleName("refreshed");

                        Window.alert(constants.fileSelectorListHasBeenRefreshed());
                    }
                }
        );

        // Get the files on load.
        FileDownloadFacilityService.App.getInstance().grabListOfUploadedFiles(asyncCallback);


        // Add the styles.
        page.addStyleName("page");
        aboutThisFacilityDisclosurePanel.addStyleName("aboutFacility");
        aboutFacilityPanel.addStyleName("aboutFacilityPanel");
        fileSelectorPanel.addStyleName("fileSelectorPanel");
        downloadButtonsPanel.addStyleName("downloadButtonsPanel");
        refreshButton.addStyleName("refreshButton");


        // Assemble the required input panel.
        // If there is info to show, add the disclosure panel.
        if (facilityInfoItemsMap.size() > 0) {
            requiredInputPanel.add(aboutThisFacilityDisclosurePanel);
        }

        requiredInputPanel.add(new HTML("<div class=\"step\">" + constants.downloadFileIntro() + "</div>"));
        requiredInputPanel.add(fileSelectorPanel);
        requiredInputPanel.add(downloadButtonsPanel);
        requiredInputPanel.add(new HTML("<div class=\"step\">" + constants.refreshButtonIntro() + "</div>"));
        requiredInputPanel.add(refreshButton);


        // Assemble the page
        page.add(requiredInputPanel);

        Image phamacologyIcon = new Image("images/pharmacology_module_50pxsq.gif");

        HorizontalPanel titlePanel = new HorizontalPanel();
        titlePanel.add(phamacologyIcon);
        titlePanel.add(new HTML(constants.pageTitle()));

        DecoratedStackPanel decoratedStackPanel = new DecoratedStackPanel();
        decoratedStackPanel.add(page, titlePanel.getElement().getString(), true);

        Image wwarnLogo = new Image("images/wwarn_logo.gif");

        RootPanel.get().add(wwarnLogo);
        RootPanel.get().add(decoratedStackPanel);

    }


}
