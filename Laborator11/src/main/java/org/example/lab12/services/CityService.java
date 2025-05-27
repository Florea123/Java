package org.example.lab12.services;

import org.example.lab12.entities.City;
import org.example.lab12.repositories.CityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityService {
    private final CityRepository cityRepository;
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public void addCity(City city) {
        cityRepository.save(city);
    }

    public void deleteCity(City city) {
        cityRepository.delete(city);
    }

    public void updateCity(String oldName, String newName) {
        Optional<City> city = cityRepository.findByName(oldName);
        if (city.isPresent()) {
            city.get().setName(newName);
            cityRepository.save(city.get());
        }
        else{
            System.out.println("No such city");
        }
    }

    public Optional<City> getCityByName(String name) {
        return cityRepository.findByName(name);
    }

}