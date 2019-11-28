
package addbook;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.mysql.jdbc.Connection;
import static java.lang.System.exit;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import database.DatabaseHandler;

public class addBookController implements Initializable {

    @FXML
    private JFXTextField title;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField author;
    @FXML
    private JFXTextField publisher;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    
    @FXML
    private AnchorPane addBookPane;
    
    public static Connection conn;
    
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
    private void addBook(ActionEvent event) {
        String bookId = id.getText();
        String bookTitle = title.getText();
        String bookAuthor = author.getText();     
        String bookPublisher = publisher.getText();
        
        if(bookId.isEmpty() || bookAuthor.isEmpty() || bookTitle.isEmpty() || bookPublisher.isEmpty()){
            Alert emptyFields = new Alert(Alert.AlertType.ERROR);
            emptyFields.setHeaderText(null);
            emptyFields.setContentText("Please fill up all the fields");
            emptyFields.showAndWait();
            return;
        }
        
        try {          
            PreparedStatement pst = conn.prepareStatement("insert into book (id, title, author, publisher, isAvail) values (?,?,?,?,?)");
            pst.setString(1, bookId);
            pst.setString(2, bookTitle);
            pst.setString(3, bookAuthor);
            pst.setString(4, bookPublisher);
            pst.setBoolean(5, true);
            pst.execute();
            pst.close();
            
            Alert bookAdded = new Alert(Alert.AlertType.INFORMATION);
            bookAdded.setHeaderText(null);
            bookAdded.setContentText("Book Added Successfully");
            bookAdded.showAndWait();
        } catch (Exception e) {
            Alert failed = new Alert(Alert.AlertType.ERROR);
            failed.setHeaderText(null);
            failed.setContentText("Failed. Couldn't Add Book");
            failed.showAndWait();
        }
    }
    
    @FXML
    private void cancel(ActionEvent event) {
        Stage addBookStage = (Stage) addBookPane.getScene().getWindow();
        addBookStage.close();
    }
    
}
