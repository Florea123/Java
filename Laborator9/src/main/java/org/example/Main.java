package org.example;

import org.example.entities.Continent;
import org.example.entities.Country;
import org.example.entities.City;
import org.example.repositories.ContinentRepository;
import org.example.repositories.CountryRepository;
import org.example.repositories.CityRepository;
import org.example.utils.EntityManagerFactorySingleton;

public class Main {
    public static void main(String[] args) {
        ContinentRepository continentRepository = new ContinentRepository();
        CountryRepository countryRepository = new CountryRepository();
        CityRepository cityRepository = new CityRepository();

        Continent continent = continentRepository.findByNameExact("Test Continent");
        if (continent == null) {
            continent = new Continent();
            continent.setName("Test Continent");
            continentRepository.create(continent);
        }

        Country country = countryRepository.findByNameExact("Test Country");
        if (country == null) {
            country = new Country();
            country.setName("Test Country");

            country.setCode("TC");
            country.setContinent(continent);
            countryRepository.create(country);
        }

        City city = cityRepository.findByNameExact("Test City");
        if (city == null) {
            city = new City();
            city.setName("Test City");
            city.setCountry(country);
            city.setCapital(true);
            cityRepository.create(city);
        }

        City foundCity = cityRepository.findById(city.getId()).orElse(null);
        if (foundCity != null) {
            System.out.println("Found by ID: " + foundCity.getName());
        }
        cityRepository.findByName("Test").forEach(c -> System.out.println("Found by name: " + c.getName()));
        EntityManagerFactorySingleton.close();
    }
}