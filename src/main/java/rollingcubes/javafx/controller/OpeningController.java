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

import javax.inject.Inject;
import java.io.IOException;

import org.tinylog.Logger;

/**
 * welcome controller.
 */
public class OpeningController {
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
            Logger.info("The Player A's name is set to {}, The Player B's name is set to {}, loading game scene",
                    playerANameTextField.getText(), playerBNameTextField.getText());
        }
    }

}
