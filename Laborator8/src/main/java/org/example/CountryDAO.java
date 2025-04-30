package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CountryDAO {
    public void create(String name, int continentId) throws SQLException {
        Connection con = Database.getConnection();
        try (PreparedStatement pstmt = con.prepareStatement(
                "INSERT INTO countries (name, continent) VALUES (?, ?)")) {
            pstmt.setString(1, name);
            pstmt.setInt(2, continentId);
            pstmt.executeUpdate();
        }
    }

    public Integer findByName(String name) throws SQLException {
        Connection con = Database.getConnection();
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT id FROM countries WHERE name='" + name + "'")) {
            return rs.next() ? rs.getInt(1) : null;
        }
    }

    public String findById(int id) throws SQLException {
        Connection con = Database.getConnection();
        try (PreparedStatement pstmt = con.prepareStatement(
                "SELECT name FROM countries WHERE id = ?")) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getString(1) : null;
        }
    }
    public ArrayList<String> findByContinent(int continentId) throws SQLException {
        Connection con = Database.getConnection();
        ArrayList<String> countries = new ArrayList<>();
        try (PreparedStatement pstmt = con.prepareStatement(
                "SELECT name FROM countries WHERE continent = ?")) {
            pstmt.setInt(1, continentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                countries.add(rs.getString("name"));
            }
        }
        return countries;
    }
}