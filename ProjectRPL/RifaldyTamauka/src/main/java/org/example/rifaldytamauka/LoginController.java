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
import org.example.rifaldytamauka.util.DBConnector;
import org.example.rifaldytamauka.util.SessionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.io.IOException;

public class LoginController {
//    private static final String CORRECT_USERNAME = "admin";
//    private static final String CORRECT_PASSWORD = "admin";

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;

    @FXML
    private void onClickLogin(ActionEvent event) throws SQLException {
        doLogin(event);
    }

    @FXML
    private void onKeyPress(KeyEvent event) throws SQLException {
        if (event.getCode() == KeyCode.ENTER) {
            doLogin(null); // bisa null karena tidak selalu lewat tombol
        }
    }

    private void doLogin(ActionEvent event) throws SQLException {
        Alert alert;
        if (txtUsername.getText().isBlank() && txtPassword.getText().isBlank()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Login failed!! Please insert your username and password");
            alert.showAndWait();
            txtUsername.requestFocus();
        } else {
            validateLogin();
        }
    }

    private void validateLogin() throws SQLException {
        Connection connNEW = DBConnector.getInstance().getConnection();

        String verif = "SELECT * FROM users WHERE username = '" + txtUsername.getText() + "' AND password = '" + txtPassword.getText() + "'";
        Statement ps = connNEW.createStatement();
        ResultSet rs = ps.executeQuery(verif);
        try {
            while (rs.next()) {
                int userId = rs.getInt("id_user");
                String username = rs.getString("username");
                String password = rs.getString("password");
                User loggedInUser = new User(userId, username, password);
                Alert alert;
                if (loggedInUser != null) {
                    if(txtUsername.getText().equals("admin") && txtPassword.getText().equals("admin")) {
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("Information");
                        alert.setContentText("Login success!!");
                        alert.showAndWait();
                        goToMainPage(null);
                    }
                    else {
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("Information");
                        alert.setContentText("Login success!!");
                        SessionManager.getInstance().setUserInfo(userId, username);
                        SessionManager.getInstance().login();
                        alert.showAndWait();
                        goToMainPage(null);

                    }
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error");
                    alert.setContentText("Invalid Login!! Please check again.");
                    alert.showAndWait();
                    txtUsername.requestFocus();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
