package org.example.rifaldytamauka;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.rifaldytamauka.util.DBConnector;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUpController {

    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtUser;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtPass2;

    @FXML
    private void onClickRegister(ActionEvent e) {
        String email = txtEmail.getText().trim();
        String username = txtUser.getText().trim();
        String pass1 = txtPassword.getText();
        String pass2 = txtPass2.getText();

        // Validasi input
        if (email.isBlank() || username.isBlank() || pass1.isBlank() || pass2.isBlank()) {
            showAlert(Alert.AlertType.ERROR, "Semua field wajib diisi!", "Tidak bisa membuka Login:\n" + e.getClass());
            return;
        }

        if (!pass1.equals(pass2)) {
            showAlert(Alert.AlertType.ERROR, "Password tidak sama!", "Tidak bisa membuka Login:\n" + e.getClass());
            txtPassword.clear();
            txtPass2.clear();
            txtPassword.requestFocus();
            return;
        }

        // Koneksi database
        try (Connection conn = DBConnector.getInstance().getConnection()) {
            // Cek apakah username/email sudah ada
            String checkQuery = "SELECT * FROM users WHERE username = ? OR email = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, username);
                checkStmt.setString(2, email);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    showAlert(Alert.AlertType.ERROR, "Username atau email sudah dipakai.", "Tidak bisa membuka Login:\n" + e.getClass());
                    txtUser.requestFocus();
                    return;
                }
            }

            // Simpan data baru ke tabel users
            String insertQuery = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, email);
                insertStmt.setString(3, pass1); // Simpan plain password (gunakan hash untuk keamanan)
                int rowsInserted = insertStmt.executeUpdate();

                if (rowsInserted > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Registrasi berhasil! Silakan login.", "Tidak bisa membuka Login:\n" + e.getClass());
                    gotoLogin(e);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Registrasi gagal. Coba lagi.", "Tidak bisa membuka Login:\n" + e.getClass());
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Terjadi kesalahan: " + ex.getMessage(), "Tidak bisa membuka Login:\n" + ex.getMessage());
        }
    }

    @FXML
    private void gotoLogin(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/org/example/rifaldytamauka/Login.fxml"));
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Sign-Up Page");
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Tidak bisa membuka Login:\n" + ex.getMessage());
        }
    }

    @FXML
    private void navigateToLogin(MouseEvent event) {
        navigate(event, "Login.fxml");
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

    private void showAlert(Alert.AlertType alertType, String message, String s) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void gotoSignUp(ActionEvent actionEvent) {
    }
}