package org.example.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.example.utils.EntityManagerFactorySingleton;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.*;

public abstract class AbstractRepository<T, ID> {
    private static final Logger LOGGER = Logger.getLogger(AbstractRepository.class.getName());
    private final EntityManagerFactory emf;
    private final Class<T> entityClass;

    static {
        try {
            LogManager.getLogManager().reset();
            LOGGER.setLevel(Level.ALL);

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);
            LOGGER.addHandler(consoleHandler);

            FileHandler fileHandler = new FileHandler("application.log", true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AbstractRepository(Class<T> entityClass) {
        this.emf = EntityManagerFactorySingleton.getInstance();
        this.entityClass = entityClass;
    }

    public void create(T entity) {
        EntityManager em = emf.createEntityManager();
        try {
            long startTime = System.currentTimeMillis();
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            long endTime = System.currentTimeMillis();
            LOGGER.info("Executed create() in " + (endTime - startTime) + " ms");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in create(): " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public Optional<T> findById(ID id) {
        EntityManager em = emf.createEntityManager();
        try {
            long startTime = System.currentTimeMillis();
            Optional<T> result = Optional.ofNullable(em.find(entityClass, id));
            long endTime = System.currentTimeMillis();
            LOGGER.info("Executed findById() in " + (endTime - startTime) + " ms");
            return result;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in findById(): " + e.getMessage(), e);
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    public List<T> executeNamedQuery(String queryName, QueryParameterSetter<T> parameterSetter) {
        EntityManager em = emf.createEntityManager();
        try {
            long startTime = System.currentTimeMillis();
            TypedQuery<T> query = em.createNamedQuery(queryName, entityClass);
            parameterSetter.setParameters(query);
            List<T> result = query.getResultList();
            long endTime = System.currentTimeMillis();
            LOGGER.info("Executed executeNamedQuery() [" + queryName + "] in " + (endTime - startTime) + " ms");
            return result;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in executeNamedQuery(): " + e.getMessage(), e);
            return List.of();
        } finally {
            em.close();
        }
    }

    @FunctionalInterface
    public interface QueryParameterSetter<T> {
        void setParameters(TypedQuery<T> query);
    }
}