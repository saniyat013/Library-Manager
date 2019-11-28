
package addmember;

import static addbook.addBookController.conn;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import database.DatabaseHandler;
import static java.lang.System.exit;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class addMemberController implements Initializable {

    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField mobile;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private AnchorPane addMemberPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try{
            conn = DatabaseHandler.getMySqlConnection();
        }catch(Exception e){
            Alert dbconnectionfailed = new Alert(Alert.AlertType.ERROR);
            dbconnectionfailed.setHeaderText(null);
            dbconnectionfailed.setContentText("Couldn't Connect to Database.\nProgram is exiting..");
            dbconnectionfailed.showAndWait();
            exit(0);
        }
    }    

    @FXML
    private void addMember(ActionEvent event) {
        String mName = name.getText();
        String mID   = id.getText();
        String mMobile = mobile.getText();
        String mEmail = email.getText();
        
        if(mName.isEmpty() || mID.isEmpty() || mMobile.isEmpty() || mEmail.isEmpty()){
            Alert emptyFields = new Alert(Alert.AlertType.ERROR);
            emptyFields.setHeaderText(null);
            emptyFields.setContentText("Please fill up all the fields");
            emptyFields.showAndWait();
            return;
        }
        
        try {          
            PreparedStatement pst = conn.prepareStatement("insert into member (name, id, mobile, email) values (?,?,?,?)");
            pst.setString(1, mName);
            pst.setString(2, mID);
            pst.setString(3, mMobile);
            pst.setString(4, mEmail);
            pst.execute();
            pst.close();
            
            Alert bookAdded = new Alert(Alert.AlertType.INFORMATION);
            bookAdded.setHeaderText(null);
            bookAdded.setContentText("Member Added Successfully");
            bookAdded.showAndWait();
        } catch (Exception e) {
            Alert failed = new Alert(Alert.AlertType.ERROR);
            failed.setHeaderText(null);
            failed.setContentText("Failed. Couldn't Add Member");
            failed.showAndWait();
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage addMemberStage = (Stage) addMemberPane.getScene().getWindow();
        addMemberStage.close();
    }
    
}
