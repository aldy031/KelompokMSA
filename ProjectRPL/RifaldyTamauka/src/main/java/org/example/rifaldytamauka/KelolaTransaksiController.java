package org.example.rifaldytamauka;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Node;
import org.example.rifaldytamauka.repo.TransaksiRepo;
import org.example.rifaldytamauka.data.Transaksi;
import org.example.rifaldytamauka.util.DBConnector;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class KelolaTransaksiController implements Initializable {

    @FXML private TextField tanggalField;
    @FXML private TextField jumlahField;
    @FXML private TextField kategoriField;
    @FXML private TextField catatanField;

    @FXML private  TextField searchField;
    @FXML private TableView<Transaksi> transaksiTable;
    @FXML private TableColumn<Transaksi, String> tanggalColumn;
    @FXML private TableColumn<Transaksi, String> kategoriColumn;
    @FXML private TableColumn<Transaksi, String> catatanColumn;
    @FXML private TableColumn<Transaksi, Integer> jumlahColumn;

    private FilteredList<Transaksi> transaksiFilteredList;
    Transaksi selectedTransaksi;
    private Connection connection;
    private final String DB_URL = "jdbc:sqlite:SAMbenking.sqlite";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        transaksiFilteredList = new FilteredList<>(FXCollections.observableList(FXCollections.observableArrayList()));
        transaksiTable.setItems(transaksiFilteredList);
        searchField.textProperty().addListener(
                (observableValue, oldValue, newValue) -> transaksiFilteredList.setPredicate(createPredicate(newValue))
        );
        tanggalColumn.setCellValueFactory(new PropertyValueFactory<>("waktu"));
        kategoriColumn.setCellValueFactory(new PropertyValueFactory<>("kategori"));
        catatanColumn.setCellValueFactory(new PropertyValueFactory<>("catatan"));
        jumlahColumn.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        connection = DBConnector.getInstance().getConnection();
        getAllData();
        transaksiTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Transaksi>() {
            @Override
            public void changed(ObservableValue<? extends Transaksi> observableValue, Transaksi course, Transaksi t1) {
                if (observableValue.getValue() != null) {
                    selectedTransaksi = observableValue.getValue();
                    tanggalColumn.setText(observableValue.getValue().getWaktu());
                    kategoriColumn.setText(observableValue.getValue().getKategori());
                    catatanColumn.setText(observableValue.getValue().getCatatan());
                    jumlahColumn.setText(Integer.toString(observableValue.getValue().getJumlah()));
                }
            }
        });

        //setup combo box
//        cbJenisPaket.getItems().add(Membership.JENIS_PAKET_BRONZE);
//        cbJenisPaket.getItems().add(Membership.JENIS_PAKET_SILVER);
//        cbJenisPaket.getItems().add(Membership.JENIS_PAKET_GOLD);
//        bersihkan();
    }

    private void loadTransaksiFromDatabase() {
        List<Transaksi> list = TransaksiRepo.getAllTransaksi();
        transaksiTable.getItems().setAll(list);
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
                Transaksi transaki = new Transaksi(
                        waktu, id, kategori,
                        jumlah, catatan);
                getObservableList().add(transaki);
            }
        } catch (SQLException e) {
            // Handle database query error
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
//            transaksi.setCatatan(catatan); // jika ada field catatan di objek
            TransaksiRepo.insertTransaksi(transaksi);

            transaksiTable.getItems().add(transaksi);
            clearForm();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearForm() {
        tanggalField.clear();
        jumlahField.clear();
        kategoriField.clear();
        catatanField.clear();
    }

    private boolean searchFindsTransaksi(Transaksi transaksi, String searchText) {
        return (transaksi.getKategori().toLowerCase().contains(searchText.toLowerCase()));
    }

    private Predicate<Transaksi> createPredicate(String searchText) {
        return transaksi -> {
            if (searchText == null || searchText.isEmpty()) return true;
            return searchFindsTransaksi(transaksi, searchText);
        };
    }

    @FXML
    private void switchToPemasukan() {
        // opsional logika mode
    }

    @FXML
    private void switchToPengeluaran() {
        // opsional logika mode
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
    private void Logout() {
        // logout logic
    }

    @FXML
    private void simpanTransaksi(ActionEvent event) {
        // Logika untuk menyimpan transaksi
        System.out.println("Tombol Simpan ditekan");
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
