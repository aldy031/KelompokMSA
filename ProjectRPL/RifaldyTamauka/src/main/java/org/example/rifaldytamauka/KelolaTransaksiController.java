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
import javafx.stage.Stage;
import javafx.scene.Node;
import org.example.rifaldytamauka.data.Transaksi;
import org.example.rifaldytamauka.repo.TransaksiRepo;
import org.example.rifaldytamauka.RingkasanService;
import org.example.rifaldytamauka.util.DBConnector;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class KelolaTransaksiController implements Initializable {

    @FXML private TextField tanggalField;
    @FXML private TextField jumlahField;
    @FXML private TextField kategoriField;
    @FXML private TextField catatanField;
    @FXML private TextField searchField;
    @FXML private Button BtnHapus;

    @FXML private TableView<Transaksi> transaksiTable;
    @FXML private TableColumn<Transaksi, String> tanggalColumn;
    @FXML private TableColumn<Transaksi, String> kategoriColumn;
    @FXML private TableColumn<Transaksi, String> catatanColumn;
    @FXML private TableColumn<Transaksi, Integer> jumlahColumn;

    private FilteredList<Transaksi> transaksiFilteredList;
    private Connection connection;
    private Transaksi selectedTransaksi;
    private RingkasanService ringkasanService;

    private final String DB_URL = "jdbc:sqlite:SAMbenking.sqlite";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inisialisasi RingkasanService
        ringkasanService = new RingkasanService();

        transaksiFilteredList = new FilteredList<>(FXCollections.observableArrayList());
        transaksiTable.setItems(transaksiFilteredList);

        tanggalColumn.setCellValueFactory(new PropertyValueFactory<>("waktu"));
        kategoriColumn.setCellValueFactory(new PropertyValueFactory<>("kategori"));
        catatanColumn.setCellValueFactory(new PropertyValueFactory<>("catatan"));
        jumlahColumn.setCellValueFactory(new PropertyValueFactory<>("jumlah"));

        searchField.textProperty().addListener((observable, oldValue, newValue) ->
                transaksiFilteredList.setPredicate(createPredicate(newValue)));

        connection = DBConnector.getInstance().getConnection();
        getAllData();

        transaksiTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Transaksi>() {
            @Override
            public void changed(ObservableValue<? extends Transaksi> observableValue, Transaksi oldVal, Transaksi newVal) {
                if (newVal != null) {
                    selectedTransaksi = newVal;
                    tanggalField.setText(newVal.getWaktu());
                    kategoriField.setText(newVal.getKategori());
                    catatanField.setText(newVal.getCatatan());
                    jumlahField.setText(String.valueOf(newVal.getJumlah()));
                }
            }
        });
    }

    private ObservableList<Transaksi> getObservableList() {
        return (ObservableList<Transaksi>) transaksiFilteredList.getSource();
    }

    private void getAllData() {
        String query = "SELECT * FROM Transaksi";
        getObservableList().clear();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String waktu = rs.getString("waktu");
                int id = rs.getInt("id");
                String kategori = rs.getString("kategori");
                int jumlah = rs.getInt("jumlah");
                String catatan = rs.getString("catatan");

                Transaksi transaksi = new Transaksi(waktu, id, kategori, jumlah, catatan);
                getObservableList().add(transaksi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void simpanTransaksi() {
        try {
            String tanggal = tanggalField.getText();
            String kategori = kategoriField.getText();
            String catatan = catatanField.getText();
            int jumlah = Integer.parseInt(jumlahField.getText());

            Transaksi transaksi = new Transaksi(tanggal, 0, kategori, jumlah, catatan);

            // Insert transaksi
            if (TransaksiRepo.insertTransaksi(transaksi)) {
                // Update ringkasan untuk kategori yang bersangkutan
                ringkasanService.updateRingkasanByKategori(kategori);

                // Refresh data dari database untuk mendapatkan ID yang benar
                getAllData();
                clearForm();

                // Tampilkan pesan sukses
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Transaksi berhasil disimpan!");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Gagal menyimpan transaksi!");
                alert.show();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Jumlah harus berupa angka!");
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Terjadi kesalahan: " + e.getMessage());
            alert.show();
        }
    }

    @FXML
    private void updateTransaksi() {
        if (selectedTransaksi == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Pilih transaksi yang ingin diupdate terlebih dahulu!");
            alert.show();
            return;
        }

        try {
            String tanggal = tanggalField.getText();
            String kategori = kategoriField.getText();
            String catatan = catatanField.getText();
            int jumlah = Integer.parseInt(jumlahField.getText());

            String oldKategori = selectedTransaksi.getKategori();

            // Update transaksi
            String query = "UPDATE Transaksi SET waktu = ?, kategori = ?, jumlah = ?, catatan = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, tanggal);
                stmt.setString(2, kategori);
                stmt.setInt(3, jumlah);
                stmt.setString(4, catatan);
                stmt.setInt(5, selectedTransaksi.getId());

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    // Update ringkasan untuk kategori lama dan baru
                    ringkasanService.updateRingkasanByKategori(oldKategori);
                    if (!oldKategori.equals(kategori)) {
                        ringkasanService.updateRingkasanByKategori(kategori);
                    }

                    getAllData();
                    clearForm();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Transaksi berhasil diupdate!");
                    alert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Gagal mengupdate transaksi!");
                    alert.show();
                }
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Jumlah harus berupa angka!");
            alert.show();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error database: " + e.getMessage());
            alert.show();
        }
    }

    private void clearForm() {
        tanggalField.clear();
        jumlahField.clear();
        kategoriField.clear();
        catatanField.clear();
        selectedTransaksi = null;
    }

    private boolean searchFindsTransaksi(Transaksi transaksi, String searchText) {
        return transaksi.getKategori().toLowerCase().contains(searchText.toLowerCase());
    }

    private Predicate<Transaksi> createPredicate(String searchText) {
        return transaksi -> {
            if (searchText == null || searchText.isEmpty()) return true;
            return searchFindsTransaksi(transaksi, searchText);
        };
    }

    @FXML
    void onBtnHapus(ActionEvent event) {
        if (selectedTransaksi == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Pilih transaksi yang ingin dihapus terlebih dahulu!");
            alert.show();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION,
                "Apakah Anda yakin ingin menghapus transaksi ini?",
                ButtonType.YES, ButtonType.NO);
        confirmAlert.setTitle("Konfirmasi Hapus");

        if (confirmAlert.showAndWait().get() == ButtonType.YES) {
            String kategori = selectedTransaksi.getKategori();

            if (deletetransaksi(selectedTransaksi)) {
                // Update ringkasan setelah menghapus
                ringkasanService.updateRingkasanByKategori(kategori);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Berhasil Dihapus!");
                alert.show();
                clearForm();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Gagal menghapus transaksi!");
                alert.show();
            }
        }
    }

    public boolean deletetransaksi(Transaksi transaksi) {
        String query = "DELETE FROM Transaksi WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, transaksi.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                getObservableList().remove(transaksi);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error database: " + e.getMessage());
            alert.show();
        }
        return false;
    }

    @FXML
    private void switchToPemasukan() {
        // Logika mode pemasukan
    }

    @FXML
    private void switchToPengeluaran(MouseEvent event) {
        navigate(event, "Pengeluaran.fxml");
    }

    @FXML
    private void navigateToKelolaKategori(MouseEvent event) {
        navigate(event, "KelolaKategori.fxml");
    }

    @FXML
    private void navigateToLihatRingkasan(MouseEvent event) {
        navigate(event, "LihatRingkasan.fxml");
    }

    @FXML
    private void navigateToLihatTransaksi(MouseEvent event) {
        navigate(event, "LihatTransaksi.fxml");
    }

    @FXML
    private void Logout(ActionEvent event) {
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

    private void navigate(MouseEvent event, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/rifaldytamauka/" + fxmlFile));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}