package org.example.rifaldytamauka.repo;

import org.example.rifaldytamauka.util.DBConnector;
import org.example.rifaldytamauka.data.Transaksi;

import java.sql.*;
import java.util.ArrayList;

public class TransaksiRepo {

    // Menambahkan transaksi baru ke database
    public static void insertTransaksi(Transaksi transaksi) {
        try {
            Connection conn = DBConnector.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO Transaksi (kategori, jumlah, waktu) VALUES (?, ?, ?)"
            );

            // Logging
            System.out.println("Menambahkan transaksi ke database:");
            System.out.println("Kategori: " + transaksi.getKategori());
            System.out.println("Jumlah: " + transaksi.getJumlah());
            System.out.println("Waktu: " + transaksi.getWaktu());

            stmt.setString(1, transaksi.getKategori());
            stmt.setInt(2, transaksi.getJumlah());
            stmt.setString(3, transaksi.getWaktu());
            stmt.executeUpdate();

            System.out.println("Transaksi berhasil disimpan.");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Gagal menambahkan transaksi: " + e.getMessage());
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
                // Membuat objek Transaksi dari hasil query
                Transaksi transaksi = new Transaksi(
                        rs.getString("waktu"),   // Mengambil data kolom "waktu"
                        rs.getInt("id"),        // Mengambil data kolom "id"
                        rs.getString("kategori"), // Mengambil data kolom "kategori"
                        rs.getInt("jumlah"),    // Mengambil data kolom "jumlah"
                        rs.getString("catatan") // Mengambil data kolom "catatan"
                );
                // Menambahkan objek Transaksi ke dalam ArrayList
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
                        rs.getString("catatan")
                );
                transactions.add(transaksi);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
