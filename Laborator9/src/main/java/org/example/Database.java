package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class Database {
    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource dataSource;

    static {
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/java");
        config.setUsername("postgres");
        config.setPassword("123456");
        config.setMaximumPoolSize(50);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setMaxLifetime(1800000);
        config.setConnectionTimeout(30000);
        config.setLeakDetectionThreshold(2000);
        config.setAutoCommit(false);
        dataSource = new HikariDataSource(config);
    }

    private Database() {}

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void closeConnection() {
        if(dataSource!=null)
            dataSource.close();
    }

    public static void rollback(Connection connection) {
        try {
            if (connection != null && !getConnection().getAutoCommit()) {
                connection.rollback();
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
    }
}