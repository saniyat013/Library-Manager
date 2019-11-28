
package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utility.LibraryManagerUtil;

public class LibraryAssistant extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/login/login.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setTitle("Library Manager Login");
        stage.setScene(scene);
        stage.show();
        
        LibraryManagerUtil.setStageIcon(stage);
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
