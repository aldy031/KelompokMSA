package org.example.rifaldytamauka;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import org.example.rifaldytamauka.data.Ringkasan;
import org.example.rifaldytamauka.data.Transaksi;
import org.example.rifaldytamauka.repo.RingkasanRepo;
import org.example.rifaldytamauka.util.DBConnector;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class LihatRingkasanController implements Initializable {

    @FXML private TableView<Ringkasan> totalPemasukanTable;
    @FXML private TableColumn<Ringkasan, String> totalPemasukanKolom;
    @FXML private TableColumn<Ringkasan, String> totalPengeluaranKolom;
    @FXML private TableColumn<Ringkasan, String> saldo;


    private Connection connection;

    private final String DB_URL = "jdbc:sqlite:SAMbenking.sqlite";

    private FilteredList<Ringkasan> ringkasanFilteredList;

    // Formatter untuk mata uang Rupiah
    private final NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ringkasanFilteredList = new FilteredList<>(FXCollections.observableArrayList());
        totalPemasukanTable.setItems(ringkasanFilteredList);

        // Set cell value factory untuk mengambil nilai double

        totalPemasukanKolom.setCellValueFactory(cellData -> {
            Ringkasan ringkasan = cellData.getValue();
            double value = ringkasan.getTotalPemasukan(); // Gunakan method yang return double
            return new javafx.beans.property.SimpleStringProperty(formatRupiah(value));
        });

        totalPengeluaranKolom.setCellValueFactory(cellData -> {
            Ringkasan ringkasan = cellData.getValue();
            double value = ringkasan.getTotalPengeluaran(); // Gunakan method yang return double
            return new javafx.beans.property.SimpleStringProperty(formatRupiah(value));
        });

        saldo.setCellValueFactory(cellData -> {
            Ringkasan ringkasan = cellData.getValue();
            double value = ringkasan.getSaldo(); // Gunakan method yang return double
            return new javafx.beans.property.SimpleStringProperty(formatRupiah(value));
        });

//        totalPemasukanKolom.setCellValueFactory(new PropertyValueFactory<>("totalPemasukan"));
//        totalPengeluaranKolom.setCellValueFactory(new PropertyValueFactory<>("totalPengeluaran"));
//        saldo.setCellValueFactory(new PropertyValueFactory<>("saldo"));

        connection = DBConnector.getInstance().getConnection();
        getAllData();
    }

    // Method untuk format mata uang Rupiah
    private String formatRupiah(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return "Rp " + decimalFormat.format(amount);
    }

    private ObservableList<Ringkasan> getObservableList() {
        return (ObservableList<Ringkasan>) ringkasanFilteredList.getSource();
    }

    private void getAllData() {
        String query = "SELECT * FROM ringkasan";
        try (var statement = connection.createStatement();
             var resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                double totalPemasukan = resultSet.getDouble("total_pemasukan");
                double totalPengeluaran = resultSet.getDouble("total_pengeluaran");
                double saldo = resultSet.getDouble("saldo");

                Ringkasan ringkasan = new Ringkasan(totalPemasukan, totalPengeluaran, saldo);
                getObservableList().add(ringkasan);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Gagal mengambil data dari tabel ringkasan");
        }
    }

    // Method alternatif untuk update ringkasan dengan format yang benar
    private void updateRingkasan() {
        // Uncomment dan sesuaikan jika diperlukan
        /*
        int totalPemasukan = ringkasanRepo.getTotal("Pemasukan");
        int totalPengeluaran = ringkasanRepo.getTotal("Pengeluaran");
        int saldo = ringkasanRepo.getSaldo();

        totalPengeluaranLabel.setText(formatRupiah(totalPengeluaran));
        saldoLabel.setText(formatRupiah(saldo));
        */
    }

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
            System.err.println("Gagal memuat halaman Kelola Transaksi");
        }
    }

    @FXML
    private void navigateToKelolaKategori(MouseEvent event) {
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
}