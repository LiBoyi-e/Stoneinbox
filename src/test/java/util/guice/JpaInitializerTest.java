package util.guice;

import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;

class JpaInitializerTest {
    @Test
    public void JpaInitializer(){
        assertThrows(Exception.class, () -> {
            new JpaInitializer(null);
        });
    }
}