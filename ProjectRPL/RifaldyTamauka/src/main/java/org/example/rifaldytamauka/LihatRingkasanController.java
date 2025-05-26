package org.example.rifaldytamauka;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LihatRingkasanController {

    @FXML
    private Label totalPemasukanLabel;

    @FXML
    private Label totalPengeluaranLabel;

    @FXML
    private Label saldoLabel;

    private static final String DATABASE_URL = "jdbc:sqlite:database.db"; // Ganti dengan path database Anda

    @FXML
    public void initialize() {
        // Panggil metode untuk memperbarui ringkasan saat halaman dimuat
        updateRingkasan();
    }

    @FXML
    private void navigateToKelolaTransaksi(MouseEvent event) {
        // Navigasi ke halaman Kelola Transaksi
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/rifaldytamauka/KelolaTransaksi.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Kelola Transaksi");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat halaman Kelola Transaksi");
        }
    }

    @FXML
    private void navigateToKelolaKategori(MouseEvent event) {
        // Navigasi ke halaman Kelola Kategori
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/rifaldytamauka/KelolaKategori.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Kelola Kategori");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat halaman Kelola Kategori");
        }
    }

    @FXML
    private void Logout(ActionEvent event) {
        // Logika logout: Navigasi ke halaman login
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/rifaldytamauka/Login.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat halaman Login");
        }
    }

    private void updateRingkasan() {
        // Koneksi ke database dan hitung ringkasan
        try (Connection conn = DriverManager.getConnection(DATABASE_URL)) {
            // Hitung total pemasukan
            String pemasukanQuery = "SELECT COALESCE(SUM(jumlah), 0) AS total FROM ringkasan WHERE kategori = 'Pemasukan'";
            try (PreparedStatement stmt = conn.prepareStatement(pemasukanQuery)) {
                ResultSet rs = stmt.executeQuery();
                int totalPemasukan = rs.getInt("total");
                totalPemasukanLabel.setText("Rp " + totalPemasukan);
            }

            // Hitung total pengeluaran
            String pengeluaranQuery = "SELECT COALESCE(SUM(jumlah), 0) AS total FROM ringkasan WHERE kategori = 'Pengeluaran'";
            try (PreparedStatement stmt = conn.prepareStatement(pengeluaranQuery)) {
                ResultSet rs = stmt.executeQuery();
                int totalPengeluaran = rs.getInt("total");
                totalPengeluaranLabel.setText("Rp " + totalPengeluaran);
            }

            // Hitung saldo
            String saldoQuery = """
                SELECT
                    (SELECT COALESCE(SUM(jumlah), 0) FROM ringkasan WHERE kategori = 'Pemasukan') -
                    (SELECT COALESCE(SUM(jumlah), 0) FROM ringkasan WHERE kategori = 'Pengeluaran') AS saldo
                """;
            try (PreparedStatement stmt = conn.prepareStatement(saldoQuery)) {
                ResultSet rs = stmt.executeQuery();
                int saldo = rs.getInt("saldo");
                saldoLabel.setText("Rp " + saldo);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Gagal memperbarui ringkasan");
        }
    }
}
