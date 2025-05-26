package org.example.rifaldytamauka;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class HalamanUtamaController {

    @FXML
    private void Logout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Sampai Jumpa!");
        alert.setContentText("Logout berhasil!");
        alert.showAndWait();
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/org/example/rifaldytamauka/login.fxml"));
            Scene loginScene = new Scene(loginRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(loginScene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat login.fxml: " + e.getMessage());
        }
    }


    @FXML
    private void bukaKelolaTransaksi(MouseEvent event) {
        pindahScene(event, "KelolaTransaksi.fxml");
    }


    @FXML
    private void bukaKelolaKategori(MouseEvent event) {
        pindahScene(event, "KelolaKategori.fxml");
    }

    @FXML
    private void bukaLihatRingkasan(MouseEvent event) {
        pindahScene(event, "LihatRingkasan.fxml");
    }

    private void pindahScene(MouseEvent event, String namaFXML) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/rifaldytamauka/" + namaFXML));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat file FXML: " + namaFXML + " - " + e.getMessage());
        }
    }
}
