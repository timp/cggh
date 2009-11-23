package fileuploadfacility.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: lee
 * Date: 02-Sep-2009
 * Time: 15:40:39
 */
public class FormWithMetaDataForUploadedDataFile implements IsSerializable {

    String originalFileName;
    String location;
    String studySite;
    String PI;
    String name;
    String email;
    String phone;

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setUploadedFilename(String uploadedFilePath) {
        this.originalFileName = uploadedFilePath;
    }


    public void setLocation(String location) {
        this.location = location;
    }

    public void setStudySite(String studySite) {
        this.studySite = studySite;
    }

    public void setPI(String PI) {
        this.PI = PI;
    }

    public String getLocation() {
        return location;
    }

    public String getStudySite() {
        return studySite;
    }

    public String getPI() {
        return PI;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String summarize() {

        //MyConstants constants = GWT.create(MyConstants.class);
        //threw an unexpected exception: java.lang.UnsupportedOperationException: ERROR: GWT.create() is only usable in client code!

        return "Original File Name: " + " " + originalFileName + "\r\n"
                + "Location: " + " " + location + "\r\n"
                + "Study Site: " + " " + studySite + "\r\n"
                + "PI: " + " " + PI + "\r\n"
                + "Name: " + " " + name + "\r\n"
                + "E-mail: " + " " + email + "\r\n"
                + "Telephone: " + " " + phone + "\r\n"
                ;


    }

}


