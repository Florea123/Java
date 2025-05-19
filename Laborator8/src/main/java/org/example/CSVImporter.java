package org.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CSVImporter {
    public void importData(String filePath) {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> rows = reader.readAll();

            rows.remove(0);

            Connection connection = Database.getConnection();

            for (String[] row : rows) {
                String countryName = row[0];
                String capitalName = row[1];
                double latitude = parseDoubleOrDefault(row[2], 0.0);
                double longitude = parseDoubleOrDefault(row[3], 0.0);
                String countryCode = row[4];
                String continentName = row[5];

                int continentId = insertContinent(connection, continentName);

                int countryId = insertCountry(connection, countryName, countryCode, continentId);

                insertCity(connection, capitalName, countryId, latitude, longitude);
            }

            connection.commit();
            System.out.println("Data imported successfully!");
        } catch (IOException | CsvException | SQLException e) {
            e.printStackTrace();
        }
    }

    private double parseDoubleOrDefault(String value, double defaultValue) {
        try {
            return value == null || value.equalsIgnoreCase("NULL") || value.isEmpty()
                    ? defaultValue
                    : Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private int insertContinent(Connection connection, String name) throws SQLException {
        var continentDAO = new ContinentDAO();
        Integer id = continentDAO.findByName(name);
        if (id == null) {
            continentDAO.create(name);
            id = continentDAO.findByName(name);
            if(id==null)
                throw new SQLException("Failed to insert continent: " + name);
        }
        return id;
    }

    private int insertCountry(Connection connection, String name, String code, int continentId) throws SQLException {
        var countryDAO = new CountryDAO();
        Integer id = countryDAO.findByName(name);
        if (id == null) {
            countryDAO.create(name, code, continentId);
            id = countryDAO.findByName(name);
        }
        return id;
    }

    private void insertCity(Connection connection, String name, int countryId, double latitude, double longitude) throws SQLException {
        var cityDAO = new CityDAO();
        if (cityDAO.findByName(name) == null) {
            cityDAO.create(name, countryId, true, 0, latitude, longitude);
        }
    }
}