package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CityDAO {
    public void create(String name, int countryId, boolean isCapital, int population, double latitude, double longitude) throws SQLException {
        Connection con = Database.getConnection();
        try (PreparedStatement pstmt = con.prepareStatement(
                "INSERT INTO cities (name, country, capital, population, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?)")) {
            pstmt.setString(1, name);
            pstmt.setInt(2, countryId);
            pstmt.setBoolean(3, isCapital);
            pstmt.setInt(4, population);
            pstmt.setDouble(5, latitude);
            pstmt.setDouble(6, longitude);
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            con.rollback();
            throw e;
        }
    }

    public Integer findByName(String name) throws SQLException {
        Connection con = Database.getConnection();
        try (PreparedStatement pstmt = con.prepareStatement(
                "SELECT id FROM cities WHERE name = ?")) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getInt(1) : null;
        }
    }

    public String findById(int id) throws SQLException {
        Connection con = Database.getConnection();
        try (PreparedStatement pstmt = con.prepareStatement(
                "SELECT name FROM cities WHERE id = ?")) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getString(1) : null;
        }
    }

    public ArrayList<String> findByCountry(int countryId) throws SQLException {
        Connection con = Database.getConnection();
        ArrayList<String> cities = new ArrayList<>();
        try (PreparedStatement pstmt = con.prepareStatement(
                "SELECT name FROM cities WHERE country_id = ?")) {
            pstmt.setInt(1, countryId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                cities.add(rs.getString("name"));
            }
        }
        return cities;
    }
    public ArrayList<City> findAll() throws SQLException {
        Connection con = Database.getConnection();
        ArrayList<City> cities = new ArrayList<>();
        try (ResultSet rs = con.createStatement().executeQuery("SELECT * FROM cities")) {
            while (rs.next()) {
                cities.add(new City(
                        rs.getInt("id"),
                        rs.getInt("country"),
                        rs.getString("name"),
                        rs.getBoolean("capital"),
                        rs.getInt("population"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")
                ));
            }
        }
        return cities;
    }
}
