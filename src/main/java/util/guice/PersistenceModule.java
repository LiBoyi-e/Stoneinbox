package util.guice;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;

/**
 * the persistence module.
 */
public class PersistenceModule extends AbstractModule {

    private String jpaUnit;

    /**
     * constructor .
     *
     * @param jpaUnit the jpa unit.
     */
    public PersistenceModule(String jpaUnit) {
        this.jpaUnit = jpaUnit;
    }

    /**
     * configure the PersistenceModule.
     */
    @Override
    protected void configure() {
        install(new JpaPersistModule(jpaUnit));
        bind(JpaInitializer.class).asEagerSingleton();
    }

}
