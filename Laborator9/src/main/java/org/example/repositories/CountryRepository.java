package org.example.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.example.entities.Country;
import org.example.utils.EntityManagerFactorySingleton;

import java.util.List;
public class CountryRepository extends AbstractRepository<Country, Long> {
    public CountryRepository() {
        super(Country.class);
    }

    public List<Country> findByName(String namePattern) {
        return executeNamedQuery("Country.findByName", query -> query.setParameter("name", "%" + namePattern + "%"));
    }

    public Country findByNameExact(String name) {
        return executeNamedQuery("Country.findByName", query -> query.setParameter("name", name))
                .stream()
                .findFirst()
                .orElse(null);
    }
}
/*
public class CountryRepository {
    private final EntityManagerFactory emf;

    public CountryRepository() {
        this.emf = EntityManagerFactorySingleton.getInstance();
    }

    public void create(Country country) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(country);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Country findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Country.class, id);
        } finally {
            em.close();
        }
    }

    public List<Country> findByName(String namePattern) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Country> query = em.createNamedQuery("Country.findByName", Country.class);
            query.setParameter("name", "%" + namePattern + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Country findByNameExact(String name) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Country> query = em.createQuery(
                    "SELECT c FROM Country c WHERE c.name = :name", Country.class);
            query.setParameter("name", name);
            return query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }
}

 */