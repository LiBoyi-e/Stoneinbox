package util.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Provides helper methods for controllers.
 */
public class ControllerHelper {
    private static final Logger logger = LoggerFactory.getLogger(ControllerHelper.class);

    /**
     * load and show fxml.
     *
     * @param fxmlLoader   the loader
     * @param resourceName the resource name
     * @param stage        the stage
     * @throws IOException when io exception
     */
    public static void loadAndShowFXML(FXMLLoader fxmlLoader, String resourceName, Stage stage) throws IOException {
        logger.trace("Loading FXML resource {}", resourceName);
        fxmlLoader.setLocation(fxmlLoader.getClass().getResource(resourceName));
        Parent root = fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
