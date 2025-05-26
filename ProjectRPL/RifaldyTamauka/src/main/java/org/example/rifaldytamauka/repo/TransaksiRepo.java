package org.example.rifaldytamauka.repo;

import org.example.rifaldytamauka.util.DBConnector;
import org.example.rifaldytamauka.data.Transaksi;

import java.sql.*;
import java.util.ArrayList;

public class TransaksiRepo {

    // Menambahkan transaksi baru ke database
    public static void insertTransaksi(Transaksi transaksi) {
        String sql = "INSERT INTO Transaksi (waktu, kategori, jumlah, catatan, jenis) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnector.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, transaksi.getWaktu());
            pstmt.setString(2, transaksi.getKategori());
            pstmt.setInt(3, transaksi.getJumlah());
            pstmt.setString(4, transaksi.getCatatan());
            pstmt.setString(5, transaksi.getJenis()); // ✅ Menambahkan jenis

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Mengambil semua transaksi dari database
    public static ArrayList<Transaksi> getAllTransaksi() {
        ArrayList<Transaksi> transactions = new ArrayList<>();
        try {
            Connection conn = DBConnector.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Transaksi");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Transaksi transaksi = new Transaksi(
                        rs.getString("waktu"),
                        rs.getInt("id"),
                        rs.getString("kategori"),
                        rs.getInt("jumlah"),
                        rs.getString("catatan"),
                        rs.getString("jenis") // ✅ Ambil jenis dari database
                );
                transactions.add(transaksi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    // Mengambil semua transaksi berdasarkan kategori
    public static ArrayList<Transaksi> getAllTransaksiByKategori(String kategori) {
        ArrayList<Transaksi> transactions = new ArrayList<>();
        try {
            Connection conn = DBConnector.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Transaksi WHERE kategori = ?");
            stmt.setString(1, kategori);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Transaksi transaksi = new Transaksi(
                        rs.getString("waktu"),
                        rs.getInt("id"),
                        rs.getString("kategori"),
                        rs.getInt("jumlah"),
                        rs.getString("catatan"),
                        rs.getString("jenis") // ✅ Ambil jenis juga
                );
                transactions.add(transaksi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
