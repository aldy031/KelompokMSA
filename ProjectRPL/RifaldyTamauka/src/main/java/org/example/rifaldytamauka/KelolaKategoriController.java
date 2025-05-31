package org.example.rifaldytamauka;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.example.rifaldytamauka.data.Ringkasan;
import org.example.rifaldytamauka.data.Transaksi;
import org.example.rifaldytamauka.util.DBConnector;

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
    private Button btnSimpan;

    @FXML
    private Button btnHapus;

    @FXML
    private TableColumn<Ringkasan, Integer> id;

    @FXML
    private TableColumn<Ringkasan, String> jenisKategori;

    @FXML
    private TableColumn<Ringkasan, Integer> kolomJumlah;

    @FXML
    private TableColumn<Ringkasan, String> kolomDate;

    @FXML
    private TableView<Ringkasan> table;

    private String selectedColor = "#5B9BD5"; // Default

    private FilteredList<Ringkasan> kategoriFilteredList;
    private Ringkasan selectedKategori;
    private Connection connection;
    private final String DB_URL = "jdbc:sqlite:SAMbenking.sqlite";

    private ObservableList<Ringkasan> getObservableList() {
        return (ObservableList<Ringkasan>) kategoriFilteredList.getSource();
    }

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
    void onBtnSimpan(ActionEvent event) {
        try {
            // Ambil nilai dari text fields
            int id = selectedKategori.getId();
            String kategori = txtNamaKategori.getText();
            double saldo = Double.parseDouble(txtJumlah.getText()); // Konversi String ke double
            // Atur waktu sekarang sebagai default

            // Buat objek Ringkasan baru
            Ringkasan ringkasanBaru = new Ringkasan(id, kategori, saldo);

            // Periksa dan simpan perubahan
            if (isKategoriUpdated()) {
                if (updateCatatan(selectedKategori, ringkasanBaru)) {
                    new Alert(Alert.AlertType.INFORMATION, "Kategori berhasil dirubah!").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Kategori gagal dirubah!").show();
                }
            }
            bersihkan();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Saldo harus berupa angka!").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Terjadi kesalahan: " + e.getMessage()).show();
        }
    }


    @FXML
    void onBtnHapus(ActionEvent event) {
        if (selectedKategori != null && deleteKategori(selectedKategori)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Membership Dihapus!");
            alert.show();
            bersihkan();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        kategoriFilteredList = new FilteredList<>(FXCollections.observableList(FXCollections.observableArrayList()));
        table.setItems(kategoriFilteredList);
        txtSearch.textProperty().addListener(
                (observableValue, oldValue, newValue) -> kategoriFilteredList.setPredicate(createPredicate(newValue))
        );
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        jenisKategori.setCellValueFactory(new PropertyValueFactory<>("kategori"));
        kolomJumlah.setCellValueFactory(new PropertyValueFactory<>("saldo"));
        connection = DBConnector.getInstance().getConnection();
        getAllData();
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Ringkasan>() {
            @Override
            public void changed(ObservableValue<? extends Ringkasan> observableValue, Ringkasan course, Ringkasan t1) {
                if (observableValue.getValue() != null) {
                    selectedKategori = observableValue.getValue();
                    txtNamaKategori.setText(observableValue.getValue().getKategori());
                    txtJumlah.setText(String.format("%.2f", observableValue.getValue().getSaldo()));

                }
            }
        });
        bersihkan();
    }

    private void bersihkan() {
        txtNamaKategori.clear();
        txtDate.clear();
        txtJumlah.clear();
        txtSearch.clear();
        txtSearch.requestFocus();
        table.getSelectionModel().clearSelection();
        selectedKategori = null;
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

    private void getAllData() {
        String query = "SELECT * FROM ringkasan";
        getObservableList().clear();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String kategori = rs.getString("kategori");
                int saldo = rs.getInt("saldo");

                Ringkasan ringkasan = new Ringkasan(id, kategori, saldo);
                getObservableList().add(ringkasan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isKategoriUpdated() {
        if (selectedKategori == null) {
            return false;
        }
        if (!selectedKategori.getKategori().equalsIgnoreCase(txtNamaKategori.getText())){
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteKategori(Ringkasan ringkasan) {
        String query = "DELETE FROM ringkasan WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, ringkasan.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                getObservableList().remove(ringkasan);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean updateCatatan(Ringkasan oldKategori, Ringkasan newKategori) {
        String query = "UPDATE ringkasan SET kategori = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newKategori.getKategori());
            preparedStatement.setInt(2, oldKategori.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                int iOldKategori = getObservableList().indexOf(oldKategori);
                getObservableList().set(iOldKategori, newKategori);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database query error
        }
        return false;
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
