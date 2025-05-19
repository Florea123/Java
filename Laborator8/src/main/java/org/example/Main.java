package org.example;

import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    public static void main(String args[]) {
        /*
        String filePath = Main.class.getClassLoader().getResource("concap.csv").getPath();
        CSVImporter importer = new CSVImporter();
        importer.importData(filePath);
        */
        try{
            CityDAO cityDAO = new CityDAO();
            ArrayList<City> cities = cityDAO.findAll();
            for(int i=0;i<cities.size();i++){
                for(int j=i+1;j<cities.size();j++){
                    City city1 =cities.get(i);
                    City city2 =cities.get(j);
                    double distance = DistanceCalculator.haversine(city1.getLatitude(), city1.getLongitude(),
                            city2.getLatitude(), city2.getLongitude());
                    System.out.printf("Distance between %s and %s: %.2f km%n",
                            city1.getName(), city2.getName(), distance);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}