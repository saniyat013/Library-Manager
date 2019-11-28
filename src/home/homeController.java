
package home;

import static addbook.addBookController.conn;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import database.DatabaseHandler;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utility.LibraryManagerUtil;

public class homeController implements Initializable {

    @FXML
    private HBox book_info;
    @FXML
    private HBox member_info;
    @FXML
    private TextField bookIDInput;
    @FXML
    private Text bookName;
    @FXML
    private Text bookAuthor;
    @FXML
    private Text bookStatus;
    @FXML
    private TextField memberIDInput;
    @FXML
    private Text memberName;
    @FXML
    private Text memberContact;       
    @FXML
    private JFXTextField bookID;
    
    private ResultSet rs, rs1;
    private Connection conn;
    @FXML
    private ListView<String> issueDataList;

    Boolean isReadyForSubmission = false;
    @FXML
    private StackPane homePane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JFXDepthManager.setDepth(book_info, 1);
        JFXDepthManager.setDepth(member_info, 1);  
        
        try {
            conn = DatabaseHandler.getMySqlConnection();           
        } catch (Exception ex) {
            Logger.getLogger(homeController.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }    

    @FXML
    private void loadAddMember(ActionEvent event) {
        loadWindow("/addmember/addMember.fxml", "Add New Member");
    }

    @FXML
    private void loadAddBook(ActionEvent event) {
        loadWindow("/addbook/addBook.fxml", "Add New Book");
    }

    @FXML
    private void loadViewMember(ActionEvent event) {
        loadWindow("/displaymember/displayMember.fxml", "Member List");
    }

    @FXML
    private void loadViewBook(ActionEvent event) {
        loadWindow("/displaybook/displayBook.fxml", "Book List");
    }

    @FXML
    private void loadSettings(ActionEvent event) {
        loadWindow("/settings/settings.fxml", "Settings");
    }
    
    void loadWindow(String loc, String title){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
            LibraryManagerUtil.setStageIcon(stage);
        } catch (IOException ex) {
            Logger.getLogger(homeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void loadBookInfo(ActionEvent event) throws SQLException {
        clearBookCache();
        String id = bookIDInput.getText();
        String q = "select * from book where id = '" + id + "'";
        rs = conn.createStatement().executeQuery(q);
        Boolean flag = false;
        while(rs.next()){
            String bName = rs.getString("title");
            String bAuthor = rs.getString("author");
            Boolean bStatus = rs.getBoolean("isAvail");
            flag = true;

            bookName.setText(bName);
            bookAuthor.setText(bAuthor);
            String status = (bStatus)? "Available" : "Not Available";
            bookStatus.setText(status);

        }
        if(!flag){
            bookName.setText("Book Not Found");
        } 
    }
    
    void clearBookCache(){
        bookName.setText("");
        bookAuthor.setText("");
        bookStatus.setText("");
        
    }

    @FXML
    private void loadMemberInfo(ActionEvent event) throws SQLException {
        clearMemberCache();
        String id = memberIDInput.getText();
        String q = "select * from member where id = '" + id + "'";
        rs = conn.createStatement().executeQuery(q);
        Boolean flag = false;
        while(rs.next()){
            String mName = rs.getString("name");
            String mContact = rs.getString("mobile");
            flag = true;

            memberName.setText(mName);
            memberContact.setText(mContact);
        }
        if(!flag){
            bookName.setText("Member Not Found");
        }
    }
    
    void clearMemberCache(){
        memberName.setText("");
        memberContact.setText("");
    }

    @FXML
    private void loadIssueOperation(ActionEvent event) throws SQLException {
        String memberID = memberIDInput.getText();
        String bookID = bookIDInput.getText();
        
        if(memberID.equals("") || bookID.equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Book Not Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please Select Book and Member First..");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Issue Operation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure to issue the book" + bookName.getText() + 
                    "\nto " + memberName.getText() + " ?");

            Optional<ButtonType> response = alert.showAndWait();
            
            if(response.get() == ButtonType.OK){
                try{
                    PreparedStatement pst1 = conn.prepareStatement("insert into issue (memberID, bookID) values (?,?)");
                    pst1.setString(1, memberID);
                    pst1.setString(2, bookID);
                    pst1.execute();

                    PreparedStatement pst2 = conn.prepareStatement("update book set isAvail = ? where id = ?");
                    pst2.setBoolean(1, false);
                    pst2.setString(2, bookID);   

                    if(pst2.executeUpdate() == 1){
                        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                        alert1.setTitle("Success");
                        alert1.setHeaderText(null);
                        alert1.setContentText("Book Issue Complete");
                        alert1.showAndWait();
                    }else{
                        Alert alert1 = new Alert(Alert.AlertType.ERROR);
                        alert1.setTitle("Failed");
                        alert1.setHeaderText(null);
                        alert1.setContentText("Issue Operation Failed");
                        alert1.showAndWait();
                    }
                    pst1.close();
                    pst2.close();
                }catch(Exception e){
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("ATTENTION");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Book Already Issued");
                    alert1.showAndWait();
                }
            }else{
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Cancelled");
                alert1.setHeaderText(null);
                alert1.setContentText("Issue Operation Cancelled");
                alert1.showAndWait();
            }
        }
    }

    @FXML
    private void loadBookInfo2(ActionEvent event) throws SQLException {
        ObservableList<String> issueData = FXCollections.observableArrayList();
        isReadyForSubmission = false;
        String id = bookID.getText();
        String q = "select * from issue where bookID = '" + id + "'";
        rs = conn.createStatement().executeQuery(q);
        while(rs.next()){
            String mBookID = rs.getString("bookID");
            String mMemberID = rs.getString("memberID");
            Timestamp mIssueTime = rs.getTimestamp("issueTime");
            int mRenewCount = rs.getInt("renewCount");
            
            issueData.add("Issue Date and Time: " + mIssueTime.toLocalDateTime());
            issueData.add("Renew Count: " + mRenewCount);
            
            String q1 = "select * from book where id = '" + mBookID + "'";
            rs1 = conn.createStatement().executeQuery(q1);
            issueData.add("Book Information:");
            while(rs1.next()){
                issueData.add("\tBook Name: " + rs1.getString("title"));
                issueData.add("\tBook ID: " + rs1.getString("id"));
                issueData.add("\tBook Author: " + rs1.getString("author"));
                issueData.add("\tBook Publisher: " + rs1.getString("publisher"));
            }
            
            String q2 = "select * from member where id = '" + mMemberID + "'";
            rs1 = conn.createStatement().executeQuery(q2);
            issueData.add("Member Information:");
            while(rs1.next()){
                issueData.add("\tName: " + rs1.getString("name"));
                issueData.add("\tMobile: " + rs1.getString("mobile"));
                issueData.add("\tEmail: " + rs1.getString("email"));
            }
            isReadyForSubmission = true;
        }
        issueDataList.getItems().setAll(issueData);
    }

    @FXML
    private void loadSubmissionOp(ActionEvent event) throws SQLException {
        if(!isReadyForSubmission){
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Failed");
            alert1.setHeaderText(null);
            alert1.setContentText("Please Select a Book to Submit");
            alert1.showAndWait();
            return;
        }
        String id = bookID.getText();
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Issue Operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to submit the book?");
        
        Optional<ButtonType> response = alert.showAndWait();
        if(response.get() == ButtonType.OK){
            PreparedStatement pst1 = conn.prepareStatement("delete from issue where bookID = ?");
            pst1.setString(1, id);
            pst1.execute();

            PreparedStatement pst2 = conn.prepareStatement("update book set isAvail = ? where id = ?");
            pst2.setBoolean(1, true);
            pst2.setString(2, id);   

            if(pst2.executeUpdate() == 1){
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Success");
                alert1.setHeaderText(null);
                alert1.setContentText("Book has been Submitted");
                alert1.showAndWait();
            }else{
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Failed");
                alert1.setHeaderText(null);
                alert1.setContentText("Submission has been Failed");
                alert1.showAndWait();
            }

            pst1.close();
            pst2.close();
        }else{
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Cancelled");
            alert1.setHeaderText(null);
            alert1.setContentText("Submission Cancelled");
            alert1.showAndWait();
        }
    }

    @FXML
    private void loadRenewOp(ActionEvent event) throws SQLException {
        if(!isReadyForSubmission){
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Failed");
            alert1.setHeaderText(null);
            alert1.setContentText("Please Select a Book to Renew");
            alert1.showAndWait();
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Renew Operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to renew the book?");
        
        Optional<ButtonType> response = alert.showAndWait();
        if(response.get() == ButtonType.OK){
            PreparedStatement pst = conn.prepareStatement("update issue set issueTime = ?, renewCount = renewCount + 1 where bookID = ?");
            
            Timestamp current_timestamp = new Timestamp(System.currentTimeMillis());
            
            pst.setTimestamp(1, current_timestamp);
            pst.setString(2, bookID.getText());
            
            if(pst.executeUpdate() == 1){
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Success");
                alert1.setHeaderText(null);
                alert1.setContentText("Book has been Renewd");
                alert1.showAndWait();
            }else{
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Failed");
                alert1.setHeaderText(null);
                alert1.setContentText("Renew has been Failed");
                alert1.showAndWait();
            }
            pst.close();
        }else{
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Cancelled");
            alert1.setHeaderText(null);
            alert1.setContentText("Renew Cancelled");
            alert1.showAndWait();
        }
    }

    @FXML
    private void menuCloseAction(ActionEvent event) {
        ((Stage)homePane.getScene().getWindow()).close();
    }

    @FXML
    private void menuAddBookAction(ActionEvent event) {
        loadWindow("/addbook/addBook.fxml", "Add New Book");
    }

    @FXML
    private void menuAddMemberAction(ActionEvent event) {
        loadWindow("/addmember/addMember.fxml", "Add New Member");
    }

    @FXML
    private void menuViewBookAction(ActionEvent event) {
        loadWindow("/displaybook/displayBook.fxml", "Book List");        
    }

    @FXML
    private void menuViewMemberAction(ActionEvent event) {
        loadWindow("/displaymember/displayMember.fxml", "Member List");
    }

    @FXML
    private void menuViewFullScreen(ActionEvent event) {
        Stage stage = ((Stage)homePane.getScene().getWindow());
        stage.setFullScreen(!stage.isFullScreen());
    }

}
