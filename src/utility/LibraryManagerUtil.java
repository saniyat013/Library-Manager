
package utility;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public class LibraryManagerUtil {
    private static final String IMAGE_LOC = "/icons/appicon.png";
    
    public static void setStageIcon(Stage stage){
        stage.getIcons().add(new Image(IMAGE_LOC));
    }
}
