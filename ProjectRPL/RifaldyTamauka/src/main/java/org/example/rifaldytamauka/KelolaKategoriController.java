package org.example.rifaldytamauka;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.example.rifaldytamauka.data.Ringkasan;
import org.example.rifaldytamauka.data.Transaksi;
import org.example.rifaldytamauka.util.DBConnector;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class KelolaKategoriController implements Initializable {

    @FXML
    private TextField txtNamaKategori;

    @FXML
    private TextField txtJumlah;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField namaKategoriField;

    @FXML
    private Button btn;

    @FXML
    private Button btnSimpan;

    @FXML
    private Button btnHapus;

    @FXML
    private Button btnTambah; // Tambahan untuk tombol tambah baru

    @FXML
    private TableColumn<Ringkasan, Integer> id;

    @FXML
    private TableColumn<Ringkasan, String> jenisKategori;

    @FXML
    private TableColumn<Ringkasan, Double> kolomJumlah; // Ubah ke Double

    @FXML
    private TableColumn<Ringkasan, String> kolomDate;

    @FXML
    private TableView<Ringkasan> table;

    private String selectedColor = "#5B9BD5"; // Default
    private Circle selectedColorCircle; // Untuk tracking circle yang dipilih

    private FilteredList<Ringkasan> kategoriFilteredList;
    private Ringkasan selectedKategori;
    private Connection connection;
    private final String DB_URL = "jdbc:sqlite:SAMbenking.sqlite";

    private ObservableList<Ringkasan> getObservableList() {
        return (ObservableList<Ringkasan>) kategoriFilteredList.getSource();
    }

    // -------------------------------
    // Navigation Methods
    // -------------------------------
    @FXML
    private void navigateToKelolaTransaksi(MouseEvent event) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/rifaldytamauka/KelolaTransaksi.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Kelola Transaksi");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("Navigation Error", "Gagal memuat halaman Kelola Transaksi: " + e.getMessage());
        }
    }

    @FXML
    private void navigateToLihatRingkasan(MouseEvent event) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/rifaldytamauka/LihatRingkasan.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Lihat Ringkasan");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("Navigation Error", "Gagal memuat halaman Lihat Ringkasan: " + e.getMessage());
        }
    }

    // -------------------------------
    // Kategori Tab Switch
    // -------------------------------
    @FXML
    private void switchToKategoriPemasukan() {
        System.out.println("Pindah ke tab Kategori Pemasukan");
        // Implementasi filter untuk kategori pemasukan
        filterByType("PEMASUKAN");
    }

    @FXML
    private void switchToKategoriPengeluaran() {
        System.out.println("Pindah ke tab Kategori Pengeluaran");
        // Implementasi filter untuk kategori pengeluaran
        filterByType("PENGELUARAN");
    }

    private void filterByType(String type) {
        // Implementasi filter berdasarkan tipe jika diperlukan
        // Untuk saat ini, tampilkan semua data
        getAllData();
    }

    // -------------------------------
    // Warna kategori - DIPERBAIKI
    // -------------------------------
    @FXML
    private void selectColor(MouseEvent event) {
        Circle clicked = (Circle) event.getSource();

        // Reset stroke dari circle sebelumnya
        if (selectedColorCircle != null) {
            selectedColorCircle.setStroke(Color.TRANSPARENT);
            selectedColorCircle.setStrokeWidth(0);
        }

        // Set stroke untuk circle yang baru dipilih
        selectedColor = toHex((Color) clicked.getFill());
        selectedColorCircle = clicked;
        clicked.setStroke(Color.web("#42404d"));
        clicked.setStrokeWidth(3);

        System.out.println("Warna dipilih: " + selectedColor);
    }

    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    // -------------------------------
    // CRUD Operations - DIPERBAIKI LENGKAP
    // -------------------------------

    @FXML
    private void onBtnTambah(ActionEvent event) {
        // Reset form untuk mode tambah baru
        bersihkan();
        selectedKategori = null;
        txtNamaKategori.requestFocus();
        System.out.println("Mode: Tambah Kategori Baru");
    }

    @FXML
    void onBtnSimpan(ActionEvent event) {
        try {
            // Ambil dan validasi input
            String kategori = txtNamaKategori.getText().trim();
            String jumlahText = txtJumlah.getText().trim();

            // VALIDASI: Nama kategori
            if (kategori.isEmpty()) {
                showErrorDialog("Validasi Error", "Nama kategori tidak boleh kosong!");
                txtNamaKategori.requestFocus();
                return;
            }

            // VALIDASI: Jumlah saldo
            if (jumlahText.isEmpty()) {
                jumlahText = "0"; // Default ke 0 jika kosong
            }

            double saldo;
            try {
                saldo = Double.parseDouble(jumlahText);
            } catch (NumberFormatException e) {
                showErrorDialog("Validasi Error", "Jumlah saldo harus berupa angka yang valid!");
                txtJumlah.requestFocus();
                return;
            }

            // Cek apakah kategori sudah ada (untuk INSERT)
            if (selectedKategori == null && isKategoriExists(kategori)) {
                showErrorDialog("Kategori Sudah Ada", "Kategori dengan nama '" + kategori + "' sudah ada!");
                txtNamaKategori.requestFocus();
                return;
            }

            boolean success = false;
            String message = "";

            if (selectedKategori != null) {
                // Mode UPDATE - Edit kategori yang sudah ada
                if (isKategoriUpdated()) {
                    Ringkasan ringkasanBaru = new Ringkasan(selectedKategori.getId(), kategori, saldo);
                    success = updateKategori(selectedKategori, ringkasanBaru);
                    message = success ? "Kategori berhasil diperbarui!" : "Gagal memperbarui kategori!";
                } else {
                    showInfoDialog("Tidak Ada Perubahan", "Tidak ada perubahan yang perlu disimpan.");
                    return;
                }
            } else {
                // Mode INSERT - Tambah kategori baru
                success = insertKategori(kategori, saldo);
                message = success ? "Kategori baru berhasil ditambahkan!" : "Gagal menambahkan kategori baru!";
            }

            if (success) {
                showSuccessDialog("Berhasil", message);
                getAllData(); // Refresh data
                bersihkan();
            } else {
                showErrorDialog("Gagal", message);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("System Error", "Terjadi kesalahan sistem: " + e.getMessage());
        }
    }

    @FXML
    void onBtnHapus(ActionEvent event) {
        // VALIDASI: Cek apakah ada kategori yang dipilih
        if (selectedKategori == null) {
            showErrorDialog("Tidak Ada Data", "Silakan pilih kategori dari tabel terlebih dahulu sebelum menghapus.");
            return;
        }

        // Konfirmasi penghapusan
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi Hapus");
        confirmAlert.setHeaderText("Hapus Kategori");
        confirmAlert.setContentText("Apakah Anda yakin ingin menghapus kategori '" +
                selectedKategori.getKategori() + "'?\n\nTindakan ini tidak dapat dibatalkan!");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (deleteKategori(selectedKategori)) {
                    showSuccessDialog("Berhasil", "Kategori berhasil dihapus!");
                    getAllData(); // Refresh data
                    bersihkan();
                } else {
                    showErrorDialog("Gagal", "Kategori gagal dihapus!");
                }
            }
        });
    }

    // -------------------------------
    // Tambah Kategori (alternatif method)
    // -------------------------------
    @FXML
    private void tambahKategori() {
        String nama = namaKategoriField != null ? namaKategoriField.getText().trim() : "";
        if (nama.isEmpty()) {
            showAlert("Nama kategori tidak boleh kosong!");
            return;
        }

        // Cek apakah kategori sudah ada
        if (isKategoriExists(nama)) {
            showErrorDialog("Kategori Sudah Ada", "Kategori dengan nama '" + nama + "' sudah ada!");
            return;
        }

        // Default saldo 0
        double saldo = 0.0;

        // Insert ke database
        if (insertKategori(nama, saldo)) {
            showSuccessDialog("Berhasil", "Kategori '" + nama + "' berhasil ditambahkan!");
            getAllData(); // Refresh data
            bersihkan();
            if (namaKategoriField != null) {
                namaKategoriField.clear();
            }
        } else {
            showErrorDialog("Gagal", "Kategori gagal ditambahkan!");
        }

        System.out.println("Menambahkan kategori: " + nama + " dengan warna " + selectedColor);
    }

    // -------------------------------
    // Database Operations - DIPERBAIKI
    // -------------------------------

    private boolean insertKategori(String kategori, double saldo) {
        String query = "INSERT INTO ringkasan (kategori, saldo) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, kategori);
            preparedStatement.setDouble(2, saldo);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error inserting kategori: " + e.getMessage());
            return false;
        }
    }

    private boolean updateKategori(Ringkasan oldKategori, Ringkasan newKategori) {
        String query = "UPDATE ringkasan SET kategori = ?, saldo = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newKategori.getKategori());
            preparedStatement.setDouble(2, newKategori.getSaldo());
            preparedStatement.setInt(3, oldKategori.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error updating kategori: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteKategori(Ringkasan ringkasan) {
        String query = "DELETE FROM ringkasan WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, ringkasan.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error deleting kategori: " + e.getMessage());
            return false;
        }
    }

    private boolean isKategoriExists(String kategori) {
        String query = "SELECT COUNT(*) FROM ringkasan WHERE LOWER(kategori) = LOWER(?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, kategori);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error checking kategori existence: " + e.getMessage());
        }
        return false;
    }

    private void getAllData() {
        String query = "SELECT * FROM ringkasan ORDER BY id ASC";
        getObservableList().clear();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String kategori = rs.getString("kategori");
                double saldo = rs.getDouble("saldo"); // Ubah ke getDouble

                Ringkasan ringkasan = new Ringkasan(id, kategori, saldo);
                getObservableList().add(ringkasan);
            }
            System.out.println("Data loaded: " + getObservableList().size() + " kategori");
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorDialog("Database Error", "Gagal memuat data: " + e.getMessage());
        }
    }

    // -------------------------------
    // Validation Methods
    // -------------------------------

    private boolean isKategoriUpdated() {
        if (selectedKategori == null) {
            return false;
        }

        // Cek perubahan nama kategori
        if (!selectedKategori.getKategori().equalsIgnoreCase(txtNamaKategori.getText().trim())) {
            return true;
        }

        // Cek perubahan saldo
        try {
            double currentSaldo = Double.parseDouble(txtJumlah.getText().trim());
            if (Math.abs(selectedKategori.getSaldo() - currentSaldo) > 0.001) {
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return false;
    }

    // -------------------------------
    // Initialization and Setup
    // -------------------------------

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Initialize database connection
            connection = DBConnector.getInstance().getConnection();
            if (connection == null) {
                showErrorDialog("Database Error", "Gagal menghubungkan ke database!");
                return;
            }

            // Initialize table
            setupTable();

            // Initialize search functionality
            setupSearch();

            // Load initial data
            getAllData();

            // Setup table selection listener
            setupTableSelectionListener();

            // Clear form initially
            bersihkan();

            System.out.println("KelolaKategoriController initialized successfully");

        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Initialization Error", "Gagal menginisialisasi controller: " + e.getMessage());
        }
    }

    private void setupTable() {
        kategoriFilteredList = new FilteredList<>(FXCollections.observableArrayList());
        table.setItems(kategoriFilteredList);

        // Setup table columns
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        jenisKategori.setCellValueFactory(new PropertyValueFactory<>("kategori"));
        kolomJumlah.setCellValueFactory(new PropertyValueFactory<>("saldo"));

        // Format kolom saldo untuk menampilkan currency
        kolomJumlah.setCellFactory(column -> new TableCell<Ringkasan, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("Rp %.2f", item));
                }
            }
        });
    }

    private void setupSearch() {
        if (txtSearch != null) {
            txtSearch.textProperty().addListener(
                    (observableValue, oldValue, newValue) -> {
                        kategoriFilteredList.setPredicate(createPredicate(newValue));
                    }
            );
        }
    }

    private void setupTableSelectionListener() {
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Ringkasan>() {
            @Override
            public void changed(ObservableValue<? extends Ringkasan> observableValue, Ringkasan oldRingkasan, Ringkasan newRingkasan) {
                if (newRingkasan != null) {
                    selectedKategori = newRingkasan;
                    txtNamaKategori.setText(newRingkasan.getKategori());
                    txtJumlah.setText(String.format("%.2f", newRingkasan.getSaldo()));
                    System.out.println("Selected kategori: " + newRingkasan.getKategori());
                }
            }
        });
    }

    // -------------------------------
    // Utility Methods
    // -------------------------------

    private void bersihkan() {
        if (txtNamaKategori != null) txtNamaKategori.clear();
        if (txtDate != null) txtDate.clear();
        if (txtJumlah != null) txtJumlah.clear();
        if (txtSearch != null) txtSearch.clear();

        table.getSelectionModel().clearSelection();
        selectedKategori = null;

        // Reset color selection
        if (selectedColorCircle != null) {
            selectedColorCircle.setStroke(Color.TRANSPARENT);
            selectedColorCircle.setStrokeWidth(0);
            selectedColorCircle = null;
        }
        selectedColor = "#5B9BD5"; // Reset to default

        // Focus to search field
        if (txtSearch != null) {
            txtSearch.requestFocus();
        }
    }

    private Predicate<Ringkasan> createPredicate(String searchText) {
        return ringkasan -> {
            if (searchText == null || searchText.isEmpty()) return true;
            return searchFindsKategori(ringkasan, searchText);
        };
    }

    private boolean searchFindsKategori(Ringkasan ringkasan, String searchText) {
        return ringkasan.getKategori().toLowerCase().contains(searchText.toLowerCase());
    }

    // -------------------------------
    // Logout
    // -------------------------------
    @FXML
    private void Logout(ActionEvent event) {
        // Konfirmasi logout
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi Logout");
        confirmAlert.setHeaderText("Keluar dari Aplikasi");
        confirmAlert.setContentText("Apakah Anda yakin ingin keluar?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                    Parent root = FXMLLoader.load(getClass().getResource("/org/example/rifaldytamauka/Login.fxml"));
                    stage.setScene(new Scene(root));
                    stage.setTitle("Login");
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    showErrorDialog("Navigation Error", "Gagal memuat halaman Login: " + e.getMessage());
                }
            }
        });
    }

    // -------------------------------
    // Alert Helper Methods
    // -------------------------------

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Peringatan");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}