package com.example.project2.DAO;

import java.sql.*;

public class DB_Logger {
    private static String username = "project2app";
    private static String password = "project2app";
    private static String dbURL = "jdbc:mysql://localhost:3306/operationslog";
    private static Connection connection;

    public static void connect() {
        try {
             connection = DriverManager.getConnection(dbURL, username, password);
            System.out.println("Connected to Logger Database");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void totalOperations() throws SQLException {
        // Retrieve the current value from the database
        String selectSql = "SELECT num_queries FROM operationslog.operationscount";
        ResultSet resultSet = connection.createStatement().executeQuery(selectSql);

        int currentValue = 0;
        try {
            if (resultSet.next()) {
                currentValue = resultSet.getInt("num_queries");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return; // Exit the method if an exception occurs
        }

        // Increment the current value by one
        int updatedValue = currentValue + 1;

        // Update the database with the new value
        String updateSql = "UPDATE operationslog.operationscount SET num_queries = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
            updateStatement.setInt(1, updatedValue);
            updateStatement.executeUpdate();
            totalUpdates();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void totalUpdates() throws SQLException {
        // Retrieve the current value from the database
        String selectSql = "SELECT num_updates FROM operationslog.operationscount";
        ResultSet resultSet = connection.createStatement().executeQuery(selectSql);
        System.out.println(resultSet);

        int currentValue = 0;
        try {
            if (resultSet.next()) {
                currentValue = resultSet.getInt("num_updates");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return; // Exit the method if an exception occurs
        }

        // Increment the current value by one
        int updatedValue = currentValue + 1;

        // Update the database with the new value
        String updateSql = "UPDATE operationslog.operationscount SET num_updates = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
            updateStatement.setInt(1, updatedValue);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
