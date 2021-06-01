package util.guice;

import com.google.inject.persist.PersistService;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * represent jpa initializer.
 */
@Singleton
public class JpaInitializer {

    /**
     * a constructor .
     *
     * @param persistService the persistence service.
     */
    @Inject
    public JpaInitializer(PersistService persistService) {
        persistService.start();
    }
}
