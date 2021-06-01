package util.guice;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersistenceModuleTest {

    @Test
    void configure() {
        final PersistenceModule persistenceModule = new PersistenceModule("rolling-cubes");
        assertThrows(Exception.class, () -> {
            persistenceModule.configure();
        });
    }
}