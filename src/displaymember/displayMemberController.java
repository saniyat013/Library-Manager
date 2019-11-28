
package displaymember;

import database.DatabaseHandler;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class displayMemberController implements Initializable {

    @FXML
    private AnchorPane displayMemberPane;
    @FXML
    private TableView<DisplayMemberTableModel> displayMemberTable;
    @FXML
    private TableColumn<DisplayMemberTableModel, String> nameCol;
    @FXML
    private TableColumn<DisplayMemberTableModel, String> idCol;
    @FXML
    private TableColumn<DisplayMemberTableModel, String> mobileCol;
    @FXML
    private TableColumn<DisplayMemberTableModel, String> emailCol;
    
    ObservableList<DisplayMemberTableModel> oblist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try{
            Connection conn = DatabaseHandler.getMySqlConnection();
            
            ResultSet rs = conn.createStatement().executeQuery("select * from member");
            
            while(rs.next()){
                oblist.add(new DisplayMemberTableModel(rs.getString("name"), rs.getString("id"),
                rs.getString("mobile"), rs.getString("email")));
            }
            
        } catch (Exception ex) {
            Logger.getLogger(displayMemberController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        mobileCol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        displayMemberTable.setItems(oblist);
    }    
    
}
