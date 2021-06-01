package util.jpa;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.google.inject.persist.Transactional;

/**
 * Generic JPA DAO class that provides JPA support for the entity class specified.
 *
 * @param <T> the type of the entity class
 */
public abstract class GenericJpaDao<T> {

    protected Class<T> entityClass;
    protected EntityManager entityManager;

    /**
     * Creates a {@code GenericJpaDao} object.
     *
     * @param entityClass the {@link Class} object that represents the entity class
     */
    public GenericJpaDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Sets the underlying {@link EntityManager} instance.
     *
     * @param entityManager the underlying {@link EntityManager} instance
     */
    @Inject
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Persists the specified entity instance in the database.
     *
     * @param entity the entity instance to be persisted in the database
     */
    @Transactional
    public void persist(T entity) {
        entityManager.persist(entity);
    }

}
