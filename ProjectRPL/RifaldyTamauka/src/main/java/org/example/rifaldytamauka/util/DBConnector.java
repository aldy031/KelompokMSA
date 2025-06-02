package org.example.rifaldytamauka.util;

import org.example.rifaldytamauka.data.user;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
            }
        }
    }

    public void createTable() {
        // Create users table
        String dbUSER = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT NOT NULL,"
                + "email TEXT NOT NULL,"
                + "password TEXT NOT NULL"
                + ")";

        // Create transaksi table - ini yang utama untuk perhitungan
        String dbTRANSAKSI = "CREATE TABLE IF NOT EXISTS Transaksi ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "waktu TEXT NOT NULL,"
                + "kategori TEXT NOT NULL,"
                + "jumlah INTEGER NOT NULL,"
                + "catatan TEXT,"
                + "jenis TEXT NOT NULL CHECK (jenis IN ('pemasukan', 'pengeluaran'))"
                + ")";

        // Create kategori table untuk referensi
        String dbKATEGORI = "CREATE TABLE IF NOT EXISTS kategori ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nama TEXT NOT NULL UNIQUE,"
                + "jenis TEXT NOT NULL CHECK (jenis IN ('pemasukan', 'pengeluaran'))"
                + ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(dbUSER);
            stmt.execute(dbTRANSAKSI);
            stmt.execute(dbKATEGORI);

            // Insert default categories
            insertDefaultKategori();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertDefaultKategori() {
        String insertKategori = "INSERT OR IGNORE INTO kategori (nama, jenis) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(insertKategori)) {
            // Kategori Pemasukan
            String[] pemasukanKategori = {"Gaji", "Bonus", "Investasi", "Lainnya"};
            for (String kategori : pemasukanKategori) {
                pstmt.setString(1, kategori);
                pstmt.setString(2, "pemasukan");
                pstmt.executeUpdate();
            }

            // Kategori Pengeluaran
            String[] pengeluaranKategori = {"Makanan", "Transportasi", "Belanja", "Hiburan", "Kesehatan", "Lainnya"};
            for (String kategori : pengeluaranKategori) {
                pstmt.setString(1, kategori);
                pstmt.setString(2, "pengeluaran");
                pstmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<user> getAllDataUsers() {
        String query = "SELECT * FROM users";
        List<user> usersList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String pass = resultSet.getString("password");
                user user = new user(id, username, pass);
                usersList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usersList;
    }
}