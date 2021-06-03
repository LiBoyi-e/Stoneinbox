package rollingcubes.javafx.controller;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.inject.Inject;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rollingcubes.results.GameResult;
import rollingcubes.results.GameResultDao;
import rollingcubes.state.BoxGameModel;
import rollingcubes.state.BoxState;
import util.javafx.ControllerHelper;
import util.javafx.Stopwatch;

/**
 * Game controller.
 */
public class GameController {
    private final Logger logger = LoggerFactory.getLogger(GameController.class);
    @FXML
    private Label nextLabel;

    @FXML
    private Label messageLabel;


    @FXML
    private HBox board;

    @FXML
    private Label stepsLabel;

    @FXML
    private Label stopwatchLabel;

    @FXML
    private Button resetButton;

    @FXML
    private Button giveUpFinishButton;

    @Inject
    private FXMLLoader fxmlLoader;

    @Inject
    private GameResultDao gameResultDao;


    private Stopwatch stopwatch = new Stopwatch();


    private IntegerProperty steps = new SimpleIntegerProperty();

    private Instant startTime;

    private String playerAName;
    private String playerBName;
    private BoxGameModel model;
    private Image emptyImage;
    private Image stoneImage;
    private boolean havePlayerWin = false;


    private void handleWeHaveAWinner(ObservableValue<? extends String> observableValue, String s, String t1) {
        final String playerName = observableValue.getValue();
        logger.info("Player {} has solved the game in {} steps", playerName, steps.get());
        stopwatch.stop();
        messageLabel.setText(String.format("Congratulations, %s!", playerName));
        resetButton.setDisable(true);
        giveUpFinishButton.setText("Finish");
        havePlayerWin = true;
    }

    /**
     * set player a name.
     *
     * @param playerAName player name
     */
    public void setPlayerAName(String playerAName) {
        this.playerAName = playerAName;
        model.playerANameProperty().set(playerAName);
    }

    /**
     * set player b name.
     *
     * @param playerBName the name
     */
    public void setPlayerBName(String playerBName) {
        this.playerBName = playerBName;
        model.playerBNameProperty().set(playerBName);
    }

    /**
     * initialize the controller.
     */
    @FXML
    public void initialize() {
        emptyImage = new Image(getClass().getResource("/images/box_empty.png").toExternalForm());
        stoneImage = new Image(getClass().getResource("/images/box_stone.png").toExternalForm());

        stepsLabel.textProperty().bind(steps.asString());
        stopwatchLabel.textProperty().bind(stopwatch.hhmmssProperty());
        Platform.runLater(() -> messageLabel.setText(String.format("Good luck, %s, %s!", playerAName, playerBName)));
        resetGame();
    }

    private void resetGame() {

        model = new BoxGameModel(playerAName, playerBName);
        havePlayerWin = false;
        bindGameStateToUI();
        steps.set(0);
        startTime = Instant.now();
        if (stopwatch.getStatus() == Animation.Status.PAUSED) {
            stopwatch.reset();
        }
        stopwatch.start();
    }

    private void bindGameStateToUI() {
        for (int i = 0; i < board.getChildren().size(); i++) {
            final ImageView imageView = (ImageView) board.getChildren().get(i);
            final int location = i;
            imageView.imageProperty().bind(new ObjectBinding<Image>() {
                {
                    bind(model.boxStateReadOnlyObjectWrapper(location));
                }

                @Override
                protected Image computeValue() {
                    if (model.boxStateReadOnlyObjectWrapper(location).getValue() == BoxState.EMPTY) {
                        return emptyImage;
                    } else {
                        return stoneImage;
                    }
                }
            });
        }

        this.nextLabel.textProperty().bind(model.currentPlayerProperty());
        model.winnerProperty().addListener(this::handleWeHaveAWinner);

    }

    /**
     * handle box clicked . call back.
     *
     * @param mouseEvent the mouse event
     */
    public void handleBoxClicked(MouseEvent mouseEvent) {
        if (!havePlayerWin) {
            final Object source = mouseEvent.getSource();
            if (source instanceof ImageView) {
                final ImageView imageView = (ImageView) source;
                final int index = getBoxIndex(imageView);
                takeout(new int[]{index});
            }
        }
    }

    /**
     * handle box drag detected event.
     *
     * @param event the event
     */
    public void handleBoxDragDetected(MouseEvent event) {
        final Object source = event.getSource();
        if (source instanceof ImageView) {
            final ImageView imageView = (ImageView) source;
            final int index = getBoxIndex(imageView);
            if (!model.isEmptyInBox(index)) {
                final Dragboard dragboard = imageView.startDragAndDrop(TransferMode.COPY);
                final ClipboardContent clipboardContent = new ClipboardContent();
                clipboardContent.putString(Integer.toString(index));
                dragboard.setContent(clipboardContent);
            }
            event.consume();
        }
    }

    /**
     * handle box drag over event.
     *
     * @param event the event
     */
    public void handleBoxDragOver(DragEvent event) {
        final Object gestureSource = event.getGestureSource();
        if (gestureSource != event.getSource()) {
            final Dragboard dragboard = event.getDragboard();
            if (dragboard.hasString()) {
                final String string = dragboard.getString();
                try {
                    final int sourceIndex = Integer.parseInt(string);
                    final Object self = event.getSource();
                    if (self instanceof ImageView) {
                        final ImageView imageView = (ImageView) self;
                        final int currentIndex = getBoxIndex(imageView);
                        if (sourceIndex + 1 == currentIndex || sourceIndex == currentIndex + 1) {
                            if (!model.isEmptyInBox(currentIndex)) {
                                event.acceptTransferModes(TransferMode.ANY);
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                }
            }
        }
        event.consume();
    }

    /**
     * handle box drag drop event.
     *
     * @param event the event
     */
    public void handleBoxDragDrop(DragEvent event) {
        final Object gestureSource = event.getGestureSource();
        final Object gestureTarget = event.getGestureTarget();
        final int i = getBoxIndex(((ImageView) gestureSource));
        final int j = getBoxIndex(((ImageView) gestureTarget));
        takeout(new int[]{i, j});
    }

    /**
     * take action .
     *
     * @param indexes box indexes
     */
    public void takeout(int[] indexes) {
        try {
            final String currentPlayer = model.getCurrentPlayer();
            model.takeStoneOutofBox(indexes);
            steps.set(steps.get() + 1);
            logger.info("Player {} take action on {}", currentPlayer, Arrays.toString(indexes));
        } catch (Exception e) {
            logger.info(e.getMessage());
            new Alert(Alert.AlertType.INFORMATION, e.getMessage()).show();
        }
    }

    private int getBoxIndex(ImageView imageView) {
        return board.getChildren().indexOf(imageView);
    }

    /**
     * handle reset button call back.
     *
     * @param actionEvent the event
     */
    public void handleResetButton(ActionEvent actionEvent) {
        logger.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        logger.info("Resetting game");
        stopwatch.stop();
        resetGame();
    }

    /**
     * handle give up game .
     *
     * @param actionEvent the event
     * @throws IOException when io exception
     */
    public void handleGiveUpFinishButton(ActionEvent actionEvent) throws IOException {
        var buttonText = ((Button) actionEvent.getSource()).getText();
        logger.debug("{} is pressed", buttonText);
        if (buttonText.equals("Give Up")) {
            stopwatch.stop();
            logger.info("The game has been given up");
        }else {
            // finish
            logger.debug("Saving result");
            final GameResult gameResult = createGameResult();
            gameResultDao.persist(gameResult);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            ControllerHelper.loadAndShowFXML(fxmlLoader, "/fxml/highscores.fxml", stage);
        }
    }

    private GameResult createGameResult() {
        String current = model.currentPlayerProperty().get();
        return GameResult.builder()
                .playerA(model.getPlayerAName())
                .playerB(model.getPlayerBName())
                .winner(current)
                .duration(Duration.between(startTime, Instant.now()))
                .steps(steps.get())
                .build();
    }

}
