package org.example.lab12.controllers;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.example.lab12.entities.Country;
import org.example.lab12.services.CountryService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Controller
@RequestMapping("/countries")
public class CountryController {
    private final CountryService countryService;
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public String html() {
        return "countries";
    }

    @PostMapping("/add")
    public void createCountry(@RequestBody Country country) {
        if(countryService.existsByName(country.getName())) {
            System.out.println("Country with name " + country.getName() + " already exists.");
        }
        else{
            countryService.save(country);
            System.out.println("Country with name " + country.getName() + " created.");
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<Country>> showCountries(){
        return ResponseEntity.ok(countryService.getAllCountries());
    }

    @GetMapping("/colors")
    public ResponseEntity<Map<Country, String>>  assignColors(){
        List<Country> countries = countryService.getAllCountries();
        Map<String, List<String>> adjacency = new HashMap<>();
        ClassPathResource resource = new ClassPathResource("GEODATASOURCE-COUNTRY-BORDERS.CSV");
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                if(values[3] != null && !values[3].isEmpty()){
                    if(!adjacency.containsKey(values[1]))
                        adjacency.put(values[1], new ArrayList<>());
                    adjacency.get(values[1]).add(values[3]);
                }
            }
        } catch (IOException | CsvValidationException e) {
            return ResponseEntity.badRequest().build();
        }

        Map<String, Integer> colorMap = new HashMap<>();
        for (String countryName : adjacency.keySet()) {
            List<Integer> neighborColors = new LinkedList<>();
            for (String neighbor : adjacency.get(countryName)) {
                if (colorMap.containsKey(neighbor)) {
                    neighborColors.add(colorMap.get(neighbor));
                }
            }
            int color = 0;
            while (neighborColors.contains(color)) {
                color++;
            }
            colorMap.put(countryName, color);
        }

        String[] colorNames = {"Red", "Green", "Blue", "Yellow", "Orange", "Purple"};
        Map<Country, String> result = new HashMap<>();
        for (Country country : countries) {
            Integer colorIdx = colorMap.get(country.getName());
            String colorName = (colorIdx != null && colorIdx < colorNames.length) ? colorNames[colorIdx] : "Red";
            result.put(country, colorName);
        }

        return ResponseEntity.ok().body(result);
    }

}
