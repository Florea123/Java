package org.example.lab12.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.lab12.entities.City;
import org.example.lab12.services.CityService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cities")
@Tag(name = "City Operations", description = "Operations related to cities")
public class CityController {
    private final CityService cityService;
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public String html() {
        return "cities";
    }

    @GetMapping("/list")
    @Operation(summary = "Get all cities", description = "Returns a list of all users")
    public ResponseEntity<List<City>> getAllCities() {
        return ResponseEntity.ok(cityService.getAllCities());
    }

    @PostMapping("/add")
    @Operation(summary = "Add city", description = "Add a city to the city database.")
    public ResponseEntity<String> addCity(@RequestBody City city) {
        Optional<City> auxCity = cityService.getCityByName(city.getName());
        if(auxCity.isPresent()) {
            return ResponseEntity.badRequest().body("City with name: " + city.getName() + " already exists");
        }
        else cityService.addCity(city);
        return ResponseEntity.ok().body("City with name: " + city.getName() + " has been added");
    }

    @PutMapping("/update")
    @Operation(summary = "Update city", description = "Updates the name of a city with the given name")
    public ResponseEntity<String> changeCity(@RequestParam String oldName, @RequestParam String newName) {
        Optional<City> city = cityService.getCityByName(oldName);
        if(city.isEmpty()) {
            return ResponseEntity.badRequest().body("City with name: " + oldName + " does not exist");
        }
        else cityService.updateCity(oldName, newName);
        return ResponseEntity.ok().body("City changed successfully");
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete city", description = "Deletes a city with the given name")
    public ResponseEntity<String> deleteCity(@RequestParam String name) {
        Optional<City> city = cityService.getCityByName(name);
        if(city.isEmpty()) {
            return ResponseEntity.badRequest().body("City with name: " + name + " does not exist");
        }
        else cityService.deleteCity(city.get());
        return ResponseEntity.ok().body("City deleted successfully");
    }
}
