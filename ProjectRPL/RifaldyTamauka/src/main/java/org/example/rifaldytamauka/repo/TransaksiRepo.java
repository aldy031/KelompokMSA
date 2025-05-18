package org.example.rifaldytamauka.repo;

import org.example.rifaldytamauka.DBManager;
import org.example.rifaldytamauka.data.Transaksi;
import org.example.rifaldytamauka.data.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class TransaksiRepo {
    public static void insertTransaksi(Transaksi transaksi) {
        try {
            Connection conn = DBManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Transaksi (kategori, jumlah, waktu)" +
                    "VALUES (?, ?, ?)");
            stmt.setString(1, transaksi.getKategori());
            stmt.setInt(2, transaksi.getJumlah());
            stmt.setString(3, transaksi.getWaktu());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Transaksi> getAllTransaksi() {
        ArrayList<Transaksi> transactions = new ArrayList<>();
        try {
            Connection conn = DBManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Transaksi");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Transaksi transaksi = new Transaksi(rs.getString("waktu"), rs.getInt("id"), rs.getString("kategori"), rs.getInt("jumlah"));
                transactions.add(transaksi);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public static ArrayList<Transaksi> getAllTransaksiByKategori(String kategori) {
        ArrayList<Transaksi> transactions = new ArrayList<>();
        try {
            Connection conn = DBManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Transaksi WHERE kategori = ?");
            stmt.setString(1, kategori);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Transaksi transaksi = new Transaksi(rs.getString("waktu"), rs.getInt("id"), rs.getString("kategori"), rs.getInt("jumlah"));
                transactions.add(transaksi);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

}
