package rollingcubes.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;



/**
 * welcome controller.
 */
public class OpeningController {
    private final Logger logger = LoggerFactory.getLogger(OpeningController.class);
    @FXML
    private TextField playerANameTextField;

    @FXML
    private TextField playerBNameTextField;

    @Inject
    private FXMLLoader fxmlLoader;

    @FXML
    private Label errorLabel;

    /**
     * start action call back.
     * @param actionEvent the event
     * @throws IOException io exception
     */
    public void startAction(ActionEvent actionEvent) throws IOException {
        if (playerANameTextField.getText().isEmpty()) {
            errorLabel.setText("Please enter player A name!");
        } else if (playerBNameTextField.getText().isEmpty()) {
            errorLabel.setText("Please enter player B name!");
        } else {
            fxmlLoader.setLocation(getClass().getResource("/fxml/game.fxml"));
            Parent root = fxmlLoader.load();
            final GameController controller = fxmlLoader.<GameController>getController();
            controller.setPlayerAName(playerANameTextField.getText());
            controller.setPlayerBName(playerBNameTextField.getText());

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            logger.info("The Player A's name is set to {}, The Player B's name is set to {}, loading game scene",
                    playerANameTextField.getText(), playerBNameTextField.getText());
        }
    }

}
