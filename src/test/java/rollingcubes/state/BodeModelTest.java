package rollingcubes.state;

import javafx.beans.property.ReadOnlyObjectWrapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BodeModelTest {

    @Test
    void takeAction() {
        final BodeModel model = prepareMode(0);
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
        final BodeModel model1 = prepareMode(2);
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

    private BodeModel prepareMode(int index) {
        final BodeModel model = new BodeModel("a", "b");
        final ReadOnlyObjectWrapper<BoxState>[] boxes = model.getBoxes();
        for (int i = 0; i < boxes.length; i++) {
            boxes[i].set(BoxState.HAVE_STONE);
        }
        boxes[index].set(BoxState.EMPTY);
        return model;
    }

    private BodeModel prepareMode() {
        return prepareMode(0);
    }

    @Test
    void checkIfWeHaveAWinner() {
    }

    @Test
    void boxStateReadOnlyObjectWrapper() {
        final BodeModel model = prepareMode();
        assertEquals(BoxState.EMPTY, model.boxStateReadOnlyObjectWrapper(0).get());
    }

    @Test
    void getWinner() {
        final BodeModel model = prepareMode();
        assertNull(model.getWinner());
    }

    @Test
    void winnerProperty() {
        final BodeModel model = prepareMode();
        assertNotNull(model.winnerProperty());
    }

    @Test
    void getNextPlayer() {
        final BodeModel model = prepareMode();
        assertEquals("a", model.getNextPlayer());
    }

    @Test
    void nextPlayerProperty() {
        final BodeModel model = prepareMode();
        assertNotNull(model.nextPlayerProperty());
    }

    @Test
    void getPlayerAName() {
        final BodeModel model = prepareMode();
        assertEquals("a", model.getPlayerAName());
    }

    @Test
    void playerANameProperty() {
        final BodeModel model = prepareMode();
        assertEquals("a", model.playerANameProperty().get());
    }

    @Test
    void getPlayerBName() {
        final BodeModel model = prepareMode();
        assertEquals("b", model.getPlayerBName());
    }

    @Test
    void playerBNameProperty() {
        final BodeModel model = prepareMode();
        assertEquals("b", model.playerBNameProperty().get());
    }

    @Test
    void isEmptyInBox() {
        final BodeModel model = prepareMode();
        assertTrue(model.isEmptyInBox(0));
    }
}