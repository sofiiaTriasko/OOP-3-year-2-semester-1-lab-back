package com.example.semester_6_oop_lab_1_back.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AbstractDAO {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/db";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";

    private static final Connection connection;

    static {
        try {
            connection = getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return connection;
        }

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }
}
