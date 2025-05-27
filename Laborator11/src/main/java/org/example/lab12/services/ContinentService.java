package org.example.lab12.services;

import org.example.lab12.entities.Continent;
import org.example.lab12.repositories.ContinentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContinentService {
    private final ContinentRepository continentRepository;
    public ContinentService(ContinentRepository continentRepository) {
        this.continentRepository = continentRepository;
    }

    public List<Continent> getAllContinents() {
        return continentRepository.findAll();
    }


}
