package rollingcubes.state;

import javafx.beans.property.ReadOnlyObjectWrapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoxGameModelTest {

    @Test
    void takeAction() {
        final BoxGameModel model = prepareMode(0);
        assertThrows(RuntimeException.class, () -> {
            model.takeStoneOutofBox(new int[0]);
        });
        assertThrows(RuntimeException.class, () -> {
            model.takeStoneOutofBox(new int[]{1, 2, 3});
        });
        assertThrows(RuntimeException.class, () -> {
            model.takeStoneOutofBox(new int[]{2, 4});
        });
        assertThrows(RuntimeException.class, () -> {
            model.takeStoneOutofBox(new int[]{0, 1});
        });
        final BoxGameModel model1 = prepareMode(2);
        assertThrows(RuntimeException.class, () -> {
            model1.takeStoneOutofBox(new int[]{1, 2});
        });

        assertThrows(RuntimeException.class, () -> {
            model1.takeStoneOutofBox(new int[]{2});
        });

        for (int i = 1; i < 15; i++) {
            model.takeStoneOutofBox(new int[]{i});
        }
    }

    private BoxGameModel prepareMode(int index) {
        final BoxGameModel model = new BoxGameModel("a", "b");
        final ReadOnlyObjectWrapper<BoxState>[] boxes = model.getBoxes();
        for (int i = 0; i < boxes.length; i++) {
            boxes[i].set(BoxState.HAVE_STONE);
        }
        boxes[index].set(BoxState.EMPTY);
        return model;
    }

    private BoxGameModel prepareMode() {
        return prepareMode(0);
    }

    @Test
    void checkIfWeHaveAWinner() {
    }

    @Test
    void boxStateReadOnlyObjectWrapper() {
        final BoxGameModel model = prepareMode();
        assertEquals(BoxState.EMPTY, model.boxStateReadOnlyObjectWrapper(0).get());
    }

    @Test
    void getWinner() {
        final BoxGameModel model = prepareMode();
        assertNull(model.getWinner());
    }

    @Test
    void winnerProperty() {
        final BoxGameModel model = prepareMode();
        assertNotNull(model.winnerProperty());
    }

    @Test
    void getNextPlayer() {
        final BoxGameModel model = prepareMode();
        assertEquals("a", model.getCurrentPlayer());
    }

    @Test
    void nextPlayerProperty() {
        final BoxGameModel model = prepareMode();
        assertNotNull(model.currentPlayerProperty());
    }

    @Test
    void getPlayerAName() {
        final BoxGameModel model = prepareMode();
        assertEquals("a", model.getPlayerAName());
    }

    @Test
    void playerANameProperty() {
        final BoxGameModel model = prepareMode();
        assertEquals("a", model.playerANameProperty().get());
    }

    @Test
    void getPlayerBName() {
        final BoxGameModel model = prepareMode();
        assertEquals("b", model.getPlayerBName());
    }

    @Test
    void playerBNameProperty() {
        final BoxGameModel model = prepareMode();
        assertEquals("b", model.playerBNameProperty().get());
    }

    @Test
    void isEmptyInBox() {
        final BoxGameModel model = prepareMode();
        assertTrue(model.isEmptyInBox(0));
    }
}