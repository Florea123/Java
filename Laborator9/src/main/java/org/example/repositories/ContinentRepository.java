package org.example.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.example.entities.Continent;
import org.example.entities.Country;
import org.example.utils.EntityManagerFactorySingleton;

import java.util.List;
public class ContinentRepository extends AbstractRepository<Continent, Long> {
    public ContinentRepository() {
        super(Continent.class);
    }

    public List<Continent> findByName(String namePattern) {
        return executeNamedQuery("Continent.findByName", query -> query.setParameter("name", "%" + namePattern + "%"));
    }

    public Continent findByNameExact(String name) {
        return executeNamedQuery("Continent.findByName", query -> query.setParameter("name", name))
                .stream()
                .findFirst()
                .orElse(null);
    }
}
/*
public class ContinentRepository {
    private final EntityManagerFactory emf;

    public ContinentRepository() {
        this.emf = EntityManagerFactorySingleton.getInstance();
    }

    public void create(Continent continent) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(continent);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Continent findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Continent.class, id);
        } finally {
            em.close();
        }
    }

    public List<Continent> findByName(String namePattern) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Continent> query = em.createNamedQuery("Continent.findByName", Continent.class);
            query.setParameter("name", "%" + namePattern + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Continent findByNameExact(String name) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Continent> query = em.createQuery("SELECT c FROM Continent c WHERE c.name = :name", Continent.class);
            query.setParameter("name", name);
            return query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }
}

 */