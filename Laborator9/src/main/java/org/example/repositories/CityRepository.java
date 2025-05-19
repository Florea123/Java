package org.example.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.example.entities.City;
import org.example.entities.Continent;
import org.example.utils.EntityManagerFactorySingleton;

import java.util.List;
public class CityRepository extends AbstractRepository<City, Long> {
    public CityRepository() {
        super(City.class);
    }

    public List<City> findByName(String namePattern) {
        return executeNamedQuery("City.findByName", query -> query.setParameter("name", "%" + namePattern + "%"));
    }
    public City findByNameExact(String name) {
        return executeNamedQuery("City.findByName", query -> query.setParameter("name", name))
                .stream()
                .findFirst()
                .orElse(null);
    }
}
/*
public class CityRepository {
    private final EntityManagerFactory emf;

    public CityRepository() {
        this.emf = EntityManagerFactorySingleton.getInstance();
    }

    public void create(City city) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(city);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public City findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(City.class, id);
        } finally {
            em.close();
        }
    }

    public List<City> findByName(String namePattern) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<City> query = em.createNamedQuery("City.findByName", City.class);
            query.setParameter("name", "%" + namePattern + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public City findByNameExact(String name) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<City> query = em.createQuery("SELECT c FROM City c WHERE c.name = :name", City.class);
            query.setParameter("name", name);
            return query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }
}

 */