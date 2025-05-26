package org.example.rifaldytamauka;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class KelolaKategoriController {

    @FXML
    private TextField searchField;

    @FXML
    private TextField namaKategoriField;

    @FXML
    private Button kategoriPemasukanButton;

    @FXML
    private Button kategoriPengeluaranButton;

    @FXML
    private Button tambahKategoriButton;

    @FXML
    private Button LogoutButton;

    @FXML
    private VBox kategoriListContainer;

    @FXML
    private HBox lihatRingkasanMenu, kelolaTransaksiMenu;

    @FXML
    private Circle colorBlue, colorGreen, colorRed, colorYellow, colorPurple, colorOrange;

    private String selectedColor = "#5B9BD5"; // Default

    // -------------------------------
    // Navigasi
    // -------------------------------
    @FXML
    private void navigateToLihatRingkasan(MouseEvent event) {
        System.out.println("Navigasi ke Lihat Ringkasan");
        // Tambahkan logika navigasi jika diperlukan
    }

    @FXML
    private void navigateToKelolaTransaksi(MouseEvent event) {
        System.out.println("Navigasi ke Kelola Transaksi");
        // Tambahkan logika navigasi jika diperlukan
    }

    // -------------------------------
    // Kategori Tab Switch
    // -------------------------------
    @FXML
    private void switchToKategoriPemasukan() {
        System.out.println("Pindah ke tab Kategori Pemasukan");
        // Logika untuk menampilkan daftar pemasukan
    }

    @FXML
    private void switchToKategoriPengeluaran() {
        System.out.println("Pindah ke tab Kategori Pengeluaran");
        // Logika untuk menampilkan daftar pengeluaran
    }

    // -------------------------------
    // Tambah Kategori
    // -------------------------------
    @FXML
    private void tambahKategori() {
        String nama = namaKategoriField.getText().trim();
        if (nama.isEmpty()) {
            showAlert("Nama kategori tidak boleh kosong!");
            return;
        }

        System.out.println("Menambahkan kategori: " + nama + " dengan warna " + selectedColor);
        // Tambahkan logika penyimpanan ke database atau daftar
    }

    // -------------------------------
    // Warna kategori
    // -------------------------------
    @FXML
    private void selectColor(MouseEvent event) {
        Circle clicked = (Circle) event.getSource();
        selectedColor = toHex((Color) clicked.getFill());
        System.out.println("Warna dipilih: " + selectedColor);

        // Reset semua border stroke
        colorBlue.setStroke(Color.TRANSPARENT);
        colorGreen.setStroke(Color.TRANSPARENT);
        colorRed.setStroke(Color.TRANSPARENT);
        colorYellow.setStroke(Color.TRANSPARENT);
        colorPurple.setStroke(Color.TRANSPARENT);
        colorOrange.setStroke(Color.TRANSPARENT);

        clicked.setStroke(Color.web("#42404d"));
        clicked.setStrokeWidth(3);
    }

    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    // -------------------------------
    // Edit dan Delete Placeholder
    // -------------------------------
    @FXML
    private void editKategori() {
        System.out.println("Edit kategori dipanggil.");
        // Tambahkan logika edit
    }

    @FXML
    private void deleteKategori() {
        System.out.println("Delete kategori dipanggil.");
        // Tambahkan konfirmasi dan logika delete
    }

    // -------------------------------
    // Logout
    // -------------------------------
    @FXML
    private void Logout() {
        System.out.println("Logout user...");
        // Tambahkan logika logout
    }

    // -------------------------------
    // Helper
    // -------------------------------
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Peringatan");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
