
package login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import home.homeController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import settings.Preferences;
import utility.LibraryManagerUtil;

public class loginController implements Initializable {

    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;

    Preferences preference;
    @FXML
    private Label loginTitle;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        preference = Preferences.getPreferences();
       
    }    

    @FXML
    private void loginButtonAction(ActionEvent event) {
        loginTitle.setText("Library Manager Login");
        loginTitle.setStyle("-fx-background-color: #0D47A1; -fx-text-fill: white");
        String uname = username.getText();
        String pass = password.getText();
        
        if(uname.equals(preference.getUsername()) && pass.equals(preference.getPassword())){
            closeStage();
            loadHome();
        }else{
            loginTitle.setText("Invalid Credentials");
            loginTitle.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white");
        }
    }

    @FXML
    private void cancelButtonAction(ActionEvent event) {
        System.exit(0);
    }
    
    void closeStage(){
        ((Stage)username.getScene().getWindow()).close();
    }
    
    void loadHome(){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/home/home.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Library Manager");
            stage.setScene(new Scene(parent));
            stage.show();
            
            LibraryManagerUtil.setStageIcon(stage);
            
        } catch (IOException ex) {
            Logger.getLogger(homeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
