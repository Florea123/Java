package org.example.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "continents")
@NamedQuery(name = "Continent.findByName", query = "SELECT c FROM Continent c WHERE c.name LIKE :name")
public class Continent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "continent", cascade = CascadeType.ALL)
    private List<Country> countries;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }
}