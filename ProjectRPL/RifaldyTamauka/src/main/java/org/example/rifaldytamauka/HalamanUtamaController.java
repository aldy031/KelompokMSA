package org.example.rifaldytamauka;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class HalamanUtamaController {

    @FXML
    private void Logout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Sampai Jumpa Setan");
        alert.setContentText("Logout success!!");
        alert.showAndWait();
        try {
            // Ganti "login.fxml" sesuai lokasi file login kamu
            Parent loginRoot = FXMLLoader.load(getClass().getResource("login.fxml"));
            Scene loginScene = new Scene(loginRoot);

            // Ambil stage sekarang lalu set scene baru
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(loginScene);

            // Pastikan tampilan tetap di tengah layar
            stage.centerOnScreen();

            // Tampilkan kembali stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat login.fxml: " + e.getMessage());
        }
    }
}
