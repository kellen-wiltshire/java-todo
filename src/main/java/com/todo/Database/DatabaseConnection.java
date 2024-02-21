package com.todo.Database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection getConnection() throws SQLException {
        System.out.println("Establishing database connection");
        try {
            DriverManager.setLoginTimeout(5); // Set a timeout of 5 seconds
            String url = "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=new_password";
            System.out.println(url);
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
            throw e;
        }
    }
}
