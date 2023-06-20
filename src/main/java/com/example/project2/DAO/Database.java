package com.example.project2.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
    static Connection conn;

    public static void connect(String url, String userName, String pwd){
        try {
            conn = DriverManager.getConnection(url, userName, pwd);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet executeQuery(String sql){
        try {
            return conn.createStatement().executeQuery(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void disconnect() {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
