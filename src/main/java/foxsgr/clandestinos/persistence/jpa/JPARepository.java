package foxsgr.clandestinos.persistence.jpa;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An abstract JPA repository.
 *
 * @param <T> the type of the entity.
 * @param <I> the type of the primary key.
 */
@SuppressWarnings({"unused", "WeakerAccess", "unchecked"})
public class JPARepository<T, I extends Serializable> {

    /**
     * The entity manager.
     */
    private EntityManager entityManager;

    /**
     * The class of the entity.
     */
    private final Class<T> entityClass;

    /**
     * The entity manager factory.
     */
    @PersistenceUnit
    private static EntityManagerFactory entityManagerFactory;

    /**
     * The name of the persistence unit.
     */
    private static final String PERSISTENCE_UNIT_NAME = "JPA";

    /**
     * Creates the repository.
     *
     * @param entityClass the class of the entity.
     */
    public JPARepository(final Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Finds an entity by its identifier.
     *
     * @param id the identifier of the entity.
     * @return the found entity or null if it wasn't found.
     */
    public T find(final I id) {
        return entityManager().find(entityClass, id);
    }

    /**
     * Finds all of the entities of the entity type.
     *
     * @return all of the entities found.
     */
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        Query query = entityManager().createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e");
        return query.getResultList();
    }

    /**
     * Stores an entity.
     *
     * @param entity the entity to store.
     * @return the stored entity.
     */
    public T save(final T entity) {
        EntityManager em = entityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        T obj = em.merge(entity);

        em.flush();
        em.refresh(obj);
        tx.commit();
        em.close();

        return obj;
    }

    /**
     * Stores a collection of entities.
     *
     * @param entities the entities to store.
     * @return the stored entities.
     */
    public Set<T> saveAll(Collection<T> entities) {
        Set<T> persisted = new HashSet<>();

        EntityManager em = entityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        for (T entity : entities) {
            T obj = em.merge(entity);
            persisted.add(obj);
        }
        tx.commit();
        em.close();

        return persisted;
    }

    /**
     * Deletes an entity.
     *
     * @param key the key of the entity to delete.
     */
    public boolean delete(I key) {
        T entity = find(key);
        if (entity != null) {
            entityManager().remove(entity);
            return true;
        }

        return false;
    }

    /**
     * Counts the persisted entities.
     *
     * @return the amount of persisted entities.
     */
    public long count() {
        Query query = entityManager().createQuery("SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e");
        return (Long) query.getSingleResult();
    }

    /**
     * Creates the entity manager if it hasn't already been created.
     *
     * @return the entity manager.
     */
    protected EntityManager entityManager() {
        if (entityManager == null || !entityManager.isOpen()) {
            entityManager = entityManagerFactory().createEntityManager();
        }

        return entityManager;
    }

    /**
     * Finds the result of a single result query.
     *
     * @param query the query to execute.
     * @return the result of the query or null if none was found.
     */
    protected static Object singleResult(Query query) {
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Creates the entity manager factory if it hasn't already been created.
     *
     * @return the entity manager factory.
     */
    private static EntityManagerFactory entityManagerFactory() {
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }

        return entityManagerFactory;
    }
}
