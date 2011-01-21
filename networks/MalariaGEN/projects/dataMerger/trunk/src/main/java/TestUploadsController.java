import org.cggh.tools.dataMerger.data.uploads.controller.*;

public class TestUploadsController {

    public static void main(String[] args) {
        UploadsController uploadsController = new UploadsController();
        uploadsController.setMiddleName("Bob");
 
        System.out.print(uploadsController.getMiddleName());
    }
}