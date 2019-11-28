
package displaybook;

import database.DatabaseHandler;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class displayBookController implements Initializable {

    @FXML
    private AnchorPane displayBookPane;
    @FXML
    private TableView<DisplayBookTableModel> displayTable;
    @FXML
    private TableColumn<DisplayBookTableModel, String> idCol;
    @FXML
    private TableColumn<DisplayBookTableModel, String> titleCol;
    @FXML
    private TableColumn<DisplayBookTableModel, String> authorCol;
    @FXML
    private TableColumn<DisplayBookTableModel, String> publisherCol;
    @FXML
    private TableColumn<DisplayBookTableModel, Boolean> availabilityCol;

    ObservableList<DisplayBookTableModel> oblist = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try{
            Connection conn = DatabaseHandler.getMySqlConnection();
        
            ResultSet rs = conn.createStatement().executeQuery("select * from book");
            
            while(rs.next()){
                oblist.add(new DisplayBookTableModel(rs.getString("id"), rs.getString("title"),
                        rs.getString("author"), rs.getString("publisher"),
                        rs.getBoolean("isAvail")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(displayBookController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(displayBookController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        availabilityCol.setCellValueFactory(new PropertyValueFactory<>("isAvail"));
        
        displayTable.setItems(oblist);
    }    
    
}
