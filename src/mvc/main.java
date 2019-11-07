
package mvc;

import java.io.IOException;
import java.util.Optional;
import javafx.application.Application;
import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import mvc.View.View;

/**
 *
 * @author Administrator
 */
public class main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        View p=new View();
        p.start();
        Pane pane=p.pane;
        Scene scene = new Scene(pane, 1080, 650);
        stage.setTitle("Paint");
        stage.setScene(scene);
         stage.setOnCloseRequest(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit Paint");
            alert.setHeaderText("Exit Paint");
            alert.setContentText("Are you sure you want to quit Paint?\nUnsaved changes will be lost.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() != ButtonType.OK) {
                e.consume();
            }
        });
        stage.show();

    }

   
    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
        launch(args);
        
    }
}

