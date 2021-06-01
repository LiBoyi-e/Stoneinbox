package util.javafx;

import javafx.animation.Animation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StopwatchTest {

    @Test
    void secondsProperty() {
        final Stopwatch stopwatch = new Stopwatch();
        assertNotNull(stopwatch.secondsProperty());
    }

    @Test
    void hhmmssProperty() {
        final Stopwatch stopwatch = new Stopwatch();
        assertNotNull(stopwatch.hhmmssProperty());
    }

    @Test
    void start() {
        final Stopwatch stopwatch = new Stopwatch();
        assertThrows(Exception.class, () -> {
            stopwatch.start();
        });
        stopwatch.stop();
        assertEquals(Animation.Status.PAUSED, stopwatch.getStatus());
        stopwatch.reset();
    }


}