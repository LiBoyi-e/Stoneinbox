package rollingcubes.results;

import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class GameResultDaoTest {

    @Test
    void findBest() {
        assertDoesNotThrow(() -> {
            final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rolling-cubes");
            final GameResultDao gameResultDao = new GameResultDao();
            gameResultDao.setEntityManager(entityManagerFactory.createEntityManager());
            final List<GameResult> best = gameResultDao.findBest(1);
        });
    }

    @Test
    void persist() {
        assertDoesNotThrow(() -> {
            final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rolling-cubes");
            final GameResultDao gameResultDao = new GameResultDao();
            gameResultDao.setEntityManager(entityManagerFactory.createEntityManager());
            assertThrows(Exception.class, () -> {
                gameResultDao.persist(null);
            });

        });
    }
}