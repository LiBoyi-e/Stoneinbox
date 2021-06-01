package rollingcubes.state;

import javafx.beans.binding.When;
import javafx.beans.property.*;

import java.util.Arrays;
import java.util.Random;

public class BodeModel {
    private ReadOnlyObjectWrapper<BoxState>[] boxes = new ReadOnlyObjectWrapper[15];
    private StringProperty playerAName = new SimpleStringProperty();
    private StringProperty playerBName = new SimpleStringProperty();

    private StringProperty winner = new SimpleStringProperty();
    private StringProperty nextPlayer = new SimpleStringProperty();
    private BooleanProperty isPlayerANext = new SimpleBooleanProperty();

    public BodeModel(String playerAName, String playerBName) {
        this.playerAName.set(playerAName);
        this.playerBName.set(playerBName);
        shuffleBoxes();
        isPlayerANext.set(true);
        nextPlayer.bind(new When(isPlayerANext).then(this.playerAName).otherwise(this.playerBName));

    }

    private void shuffleBoxes() {
        for (int i = 0; i < boxes.length; i++) {
            boxes[i] = new ReadOnlyObjectWrapper<>();
        }
        final Random random = new Random(System.currentTimeMillis());
        for (ReadOnlyObjectWrapper<BoxState> box : boxes) {
            box.set(BoxState.HAVE_STONE);
        }
        final int randomIndex = random.nextInt(boxes.length);
        boxes[randomIndex].set(BoxState.EMPTY);
    }

    public void takeAction(int[] positions) {
        if (positions.length == 0) {
            throw new RuntimeException("no box select");
        } else if (positions.length > 2) {
            throw new RuntimeException("user should select only at most 2 box adjacent.");
        } else {
            Arrays.sort(positions);
            if (positions.length != 1) {
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
                winner.set(nextPlayer.get());
            } else {
                isPlayerANext.set(!isPlayerANext.get());
            }
        }
    }

    public boolean checkIfWeHaveAWinner() {
        return Arrays.stream(boxes).allMatch(w -> w.get() == BoxState.EMPTY);
    }

    public ReadOnlyObjectWrapper<BoxState> boxStateReadOnlyObjectWrapper(int location) {
        return boxes[location];
    }

    public String getWinner() {
        return winner.get();
    }

    public StringProperty winnerProperty() {
        return winner;
    }

    public String getNextPlayer() {
        return nextPlayer.get();
    }

    public StringProperty nextPlayerProperty() {
        return nextPlayer;
    }

    public String getPlayerAName() {
        return playerAName.get();
    }

    public StringProperty playerANameProperty() {
        return playerAName;
    }

    public String getPlayerBName() {
        return playerBName.get();
    }

    public StringProperty playerBNameProperty() {
        return playerBName;
    }

    public boolean isEmptyInBox(int i){
        return boxes[i].get() == BoxState.EMPTY;
    }

     ReadOnlyObjectWrapper<BoxState>[] getBoxes() {
        return boxes;
    }
}
