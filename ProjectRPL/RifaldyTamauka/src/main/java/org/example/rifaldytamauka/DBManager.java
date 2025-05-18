package org.example.rifaldytamauka;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBManager {
    private Connection conn;
    private static DBManager instance;
    private static  String DB_URL = "jdbc:sqlite:mahasiswa.db";
    private DBManager() {}

    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
            instance.getConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(DB_URL);
            } catch (Exception e) {
                e.printStackTrace();
                conn = null;
            }
        }
        return conn;
    }

    public static void main(String[] args) {
        Connection conn =  DBManager.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM User");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String username = rs.getString("username");
                String pass = rs.getString("password");
                System.out.println(username);
                System.out.println(pass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
