package org.example.rifaldytamauka.util;

import org.example.rifaldytamauka.data.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * @editor David.Seay-71220909
 */
public class DBConnector {
    private final String DB_URL = "jdbc:sqlite:SAMbenking.sqlite";
    private Connection connection;

    private static volatile DBConnector instance = null;

    private DBConnector() {
    }

    public static DBConnector getInstance() {
        if (instance == null) {
            synchronized (DBConnector.class) {
                if (instance == null) {
                    instance = new DBConnector();
                    instance.getConnection();
                    instance.createTable();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DB_URL);
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database connection error
            }
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database connection closure error
            }
        }
    }


    public void createTable() {
        // Create database tables if they don't exist
        String dbUSER = "CREATE TABLE IF NOT EXISTS users ("
                + "id TEXT PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT NOT NULL,"
                + "email TEXT NOT NULL,"
                + "password TEXT NOT NULL"
                + ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(dbUSER);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle table creation error
        }
    }

    public List<User> getAllDataUsers() {
        String query = "SELECT * FROM users";
        List<User> usersList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");

                String username = resultSet.getString("username");
                String pass = resultSet.getString("password");
                User user = new User(id, username, pass);
                usersList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database query error
        }
        return usersList;
    }


}