package org.example.rifaldytamauka;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.example.rifaldytamauka.data.User;
import org.example.rifaldytamauka.repo.UserRepo;

import java.io.IOException;

public class LoginController {
    private static final String CORRECT_USERNAME = "admin";
    private static final String CORRECT_PASSWORD = "admin";

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;

    @FXML
    private void onClickLogin(ActionEvent event) {
        doLogin(event);
    }

    @FXML
    private void onKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            doLogin(null); // bisa null karena tidak selalu lewat tombol
        }
    }

    private void doLogin(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        Alert alert;

        // Cek hardcoded admin
        if (username.equals(CORRECT_USERNAME) && password.equals(CORRECT_PASSWORD)) {
            UserManager.currentUser = new User(0, "admin@example.com", "admin", "admin");
            showAlert(Alert.AlertType.INFORMATION, "Information", "Login success!!");

            goToMainPage(event);
        } else {
            // Cek ke repository
            User user = UserRepo.login(username, password);
            if (user != null) {
                UserManager.currentUser = user;
                showAlert(Alert.AlertType.INFORMATION, "Information", "Login success!!");

                goToMainPage(event);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Login failed!! Please check again.");
                txtUsername.requestFocus();
            }
        }
    }

    private void goToMainPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HalamanUtama.fxml"));
            Parent root = loader.load();
            Stage stage;
            if (event != null) {
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            } else {
                // fallback jika event null (misalnya tekan Enter)
                stage = (Stage) txtUsername.getScene().getWindow();
            }
            stage.setScene(new Scene(root));
            stage.setTitle("Halaman Utama");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuka halaman utama:\n" + e.getMessage());
        }
    }

    @FXML
    private void gotoSignUp(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/org/example/rifaldytamauka/SignUp.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Sign-Up Page");
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Tidak bisa membuka Sign-Up:\n" + ex.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String header, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
