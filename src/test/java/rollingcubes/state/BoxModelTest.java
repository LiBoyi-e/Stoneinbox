package rollingcubes.state;

import javafx.beans.property.ReadOnlyObjectWrapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoxModelTest {

    @Test
    void takeAction() {
        final BoxModel model = prepareMode(0);
        assertThrows(RuntimeException.class, () -> {
            model.takeAction(new int[0]);
        });
        assertThrows(RuntimeException.class, () -> {
            model.takeAction(new int[]{1, 2, 3});
        });
        assertThrows(RuntimeException.class, () -> {
            model.takeAction(new int[]{2, 4});
        });
        assertThrows(RuntimeException.class, () -> {
            model.takeAction(new int[]{0, 1});
        });
        final BoxModel model1 = prepareMode(2);
        assertThrows(RuntimeException.class, () -> {
            model1.takeAction(new int[]{1, 2});
        });

        assertThrows(RuntimeException.class, () -> {
            model1.takeAction(new int[]{2});
        });

        for (int i = 1; i < 15; i++) {
            model.takeAction(new int[]{i});
        }
    }

    private BoxModel prepareMode(int index) {
        final BoxModel model = new BoxModel("a", "b");
        final ReadOnlyObjectWrapper<BoxState>[] boxes = model.getBoxes();
        for (int i = 0; i < boxes.length; i++) {
            boxes[i].set(BoxState.HAVE_STONE);
        }
        boxes[index].set(BoxState.EMPTY);
        return model;
    }

    private BoxModel prepareMode() {
        return prepareMode(0);
    }

    @Test
    void checkIfWeHaveAWinner() {
    }

    @Test
    void boxStateReadOnlyObjectWrapper() {
        final BoxModel model = prepareMode();
        assertEquals(BoxState.EMPTY, model.boxStateReadOnlyObjectWrapper(0).get());
    }

    @Test
    void getWinner() {
        final BoxModel model = prepareMode();
        assertNull(model.getWinner());
    }

    @Test
    void winnerProperty() {
        final BoxModel model = prepareMode();
        assertNotNull(model.winnerProperty());
    }

    @Test
    void getNextPlayer() {
        final BoxModel model = prepareMode();
        assertEquals("a", model.getNextPlayer());
    }

    @Test
    void nextPlayerProperty() {
        final BoxModel model = prepareMode();
        assertNotNull(model.nextPlayerProperty());
    }

    @Test
    void getPlayerAName() {
        final BoxModel model = prepareMode();
        assertEquals("a", model.getPlayerAName());
    }

    @Test
    void playerANameProperty() {
        final BoxModel model = prepareMode();
        assertEquals("a", model.playerANameProperty().get());
    }

    @Test
    void getPlayerBName() {
        final BoxModel model = prepareMode();
        assertEquals("b", model.getPlayerBName());
    }

    @Test
    void playerBNameProperty() {
        final BoxModel model = prepareMode();
        assertEquals("b", model.playerBNameProperty().get());
    }

    @Test
    void isEmptyInBox() {
        final BoxModel model = prepareMode();
        assertTrue(model.isEmptyInBox(0));
    }
}