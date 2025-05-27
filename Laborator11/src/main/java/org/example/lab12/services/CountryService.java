package org.example.lab12.services;

import org.example.lab12.entities.Country;
import org.example.lab12.repositories.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {
    private final CountryRepository countryRepository;
    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    public boolean existsByName(String name) {
        return countryRepository.existsByName(name);
    }

    public void save(Country country) {
        countryRepository.save(country);
    }

}
