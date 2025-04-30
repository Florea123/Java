package org.example;

import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    public static void main(String args[]) {
        try {
            var continents = new ContinentDAO();
            continents.create("Europe");
            Database.getConnection().commit();

            var countries = new CountryDAO();
            int europeId = continents.findByName("Europe");
            countries.create("Romania", europeId);
            countries.create("Ukraine", europeId);
            Database.getConnection().commit();

            ArrayList<String> europeanCountries = countries.findByContinent(europeId);
            System.out.println("Countries in Europe:");
            europeanCountries.forEach(System.out::println);

            Database.closeConnection();
        } catch (SQLException e) {
            System.err.println(e);
            Database.rollback();
        }
    }
}