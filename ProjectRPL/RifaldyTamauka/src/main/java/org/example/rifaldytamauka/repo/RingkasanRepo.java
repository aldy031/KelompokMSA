package org.example.rifaldytamauka.repo;

import org.example.rifaldytamauka.util.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RingkasanRepo {

    public void tambahTransaksi(String kategori, int saldo) {
        String query = "INSERT INTO ringkasan (kategori, saldo) VALUES (?, ?)";
        try (Connection conn = DBConnector.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, kategori);
            stmt.setInt(2, saldo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRingkasan(String kategori, int saldo) {
        String updateQuery = "UPDATE ringkasan SET saldo = saldo + ? WHERE kategori = ?";
        try (Connection conn = DBConnector.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setInt(1, saldo);
            stmt.setString(2, kategori);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated == 0) {
                tambahTransaksi(kategori, saldo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getTotal(String kategori) {
        String query = "SELECT saldo FROM ringkasan WHERE kategori = ?";
        try (Connection conn = DBConnector.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, kategori);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("saldo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getSaldo() {
        int pemasukan = getTotal("pemasukan");
        int pengeluaran = getTotal("pengeluaran");
        return pemasukan - pengeluaran;
    }
}
