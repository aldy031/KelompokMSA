package org.example.rifaldytamauka;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.rifaldytamauka.repo.UserRepo;

import java.io.IOException;

public class SignUpController {

    /* ---------- Field yang di-bind dari SignUp.fxml ---------- */
    @FXML private TextField txtEmail;      // ← pastikan ada di FXML
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPass1;
    @FXML private PasswordField txtPass2;

    /* ----------------- REGISTER ----------------- */
    @FXML
    private void onClickRegister(ActionEvent e) {
        String email = txtEmail.getText().trim();
        String user  = txtUsername.getText().trim();
        String p1    = txtPass1.getText();
        String p2    = txtPass2.getText();

        /* --- validasi form --- */
        if (email.isBlank() || user.isBlank() || p1.isBlank() || p2.isBlank()) {
            alert(Alert.AlertType.ERROR, "Semua field wajib diisi!");
            return;
        }
        if (!p1.equals(p2)) {
            alert(Alert.AlertType.ERROR, "Password tidak sama!");
            txtPass1.clear(); txtPass2.clear(); txtPass1.requestFocus();
            return;
        }

        /* --- simpan ke database --- */
        boolean ok = UserRepo.register(email, user, p1);
        if (ok) {
            alert(Alert.AlertType.INFORMATION, "Registrasi berhasil, silakan login.");
            gotoLogin(e);                         // kembali ke halaman Login
        } else {
            alert(Alert.AlertType.ERROR, "Username sudah dipakai.");
            txtUsername.requestFocus();
        }
    }

    /* -------------- BACK TO LOGIN -------------- */
    @FXML
    private void gotoLogin(ActionEvent e) {
        loadScene(e, "/view/Login.fxml", "Login");
    }

    /* -------------- UTILITIES -------------- */
    private void alert(Alert.AlertType type, String msg) {
        new Alert(type, msg).showAndWait();
    }

    private void loadScene(ActionEvent e, String fxml, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
