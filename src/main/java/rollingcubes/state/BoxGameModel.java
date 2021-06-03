package rollingcubes.state;

import javafx.beans.binding.When;
import javafx.beans.property.*;

import java.util.Arrays;
import java.util.Random;

/**
 * represent  box mode.
 */
public class BoxGameModel {
    private ReadOnlyObjectWrapper<BoxState>[] boxes = new ReadOnlyObjectWrapper[15];
    private StringProperty playerAName = new SimpleStringProperty();
    private StringProperty playerBName = new SimpleStringProperty();

    private StringProperty winner = new SimpleStringProperty();
    private StringProperty currentPlayer = new SimpleStringProperty();
    private BooleanProperty isPlayerANext = new SimpleBooleanProperty();

    /**
     * constructor.
     *
     * @param playerAName player a name
     * @param playerBName player b name
     */
    public BoxGameModel(String playerAName, String playerBName) {
        this.playerAName.set(playerAName);
        this.playerBName.set(playerBName);
        shuffleBoxes();
        isPlayerANext.set(true);
        currentPlayer.bind(new When(isPlayerANext).then(this.playerAName).otherwise(this.playerBName));

    }

    private void shuffleBoxes() {
        for (int i = 0; i < boxes.length; i++) {
            boxes[i] = new ReadOnlyObjectWrapper<>();
        }
        for (ReadOnlyObjectWrapper<BoxState> box : boxes) {
            box.set(BoxState.HAVE_STONE);
        }
        final Random random = new Random(System.currentTimeMillis());
        final int randomIndex = random.nextInt(boxes.length);
        boxes[randomIndex].set(BoxState.EMPTY);
    }

    /**
     * take action.
     *
     * @param positions positions
     */
    public void takeStoneOutofBox(int[] positions) {
        if (positions.length == 0) {
            throw new RuntimeException("no box select");
        } else if (positions.length > 2) {
            throw new RuntimeException("user should select only at most 2 box adjacent.");
        } else {
            Arrays.sort(positions);
            if (positions.length != 1) {
                //assert length=2
                if (positions[0] + 1 != positions[1]) {
                    throw new RuntimeException("two box should adjacent");
                }
                if (boxes[positions[1]].get() == BoxState.EMPTY) {
                    throw new RuntimeException("box already empty");
                }
            }
            if (boxes[positions[0]].get() == BoxState.EMPTY) {
                throw new RuntimeException("box already empty");
            }
            for (int position : positions) {
                boxes[position].set(BoxState.EMPTY);
            }
            if (checkIfWeHaveAWinner()) {
                winner.set(currentPlayer.get());
            } else {
                isPlayerANext.set(!isPlayerANext.get());
            }
        }
    }

    /**
     * check if have winner.
     *
     * @return true if have.
     */
    public boolean checkIfWeHaveAWinner() {
        return Arrays.stream(boxes).allMatch(w -> w.get() == BoxState.EMPTY);
    }

    /**
     * get box state at some location.
     *
     * @param location the location
     * @return the box state
     */
    public ReadOnlyObjectWrapper<BoxState> boxStateReadOnlyObjectWrapper(int location) {
        return boxes[location];
    }

    /**
     * get winner.
     *
     * @return the winner name
     */
    public String getWinner() {
        return winner.get();
    }

    /**
     * get winner property.
     *
     * @return the property
     */
    public StringProperty winnerProperty() {
        return winner;
    }

    /**
     * get next player.
     *
     * @return the player name
     */
    public String getCurrentPlayer() {
        return currentPlayer.get();
    }

    /**
     * get next player property.
     *
     * @return the property
     */
    public StringProperty currentPlayerProperty() {
        return currentPlayer;
    }

    /**
     * get player a name.
     *
     * @return the name
     */
    public String getPlayerAName() {
        return playerAName.get();
    }

    /**
     * get player a name property.
     *
     * @return the property
     */
    public StringProperty playerANameProperty() {
        return playerAName;
    }

    /**
     * get player b name.
     *
     * @return the name
     */
    public String getPlayerBName() {
        return playerBName.get();
    }

    /**
     * get player b name property.
     *
     * @return the property
     */
    public StringProperty playerBNameProperty() {
        return playerBName;
    }

    /**
     * check if is empty in box at pos.
     * @param i the index
     * @return true if empty
     */
    public boolean isEmptyInBox(int i) {
        return boxes[i].get() == BoxState.EMPTY;
    }

    ReadOnlyObjectWrapper<BoxState>[] getBoxes() {
        return boxes;
    }
}
