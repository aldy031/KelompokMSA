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
import javafx.stage.Stage;
import org.example.rifaldytamauka.repo.UserRepo;
import org.example.rifaldytamauka.DBManager;

import java.io.IOException;

public class SignUpController {
    private static final String CORRECT_USERNAME = "admin";
    private static final String CORRECT_PASSWORD = "admin";
    private static final String CORRECT_EMAIL = "admin";

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPass1;
    @FXML private PasswordField txtPass2;


    @FXML
    private void onClickRegister(ActionEvent e) {
        String u  = txtUsername.getText().trim();
        String p1 = txtPass1.getText();
        String p2 = txtPass2.getText();

        if (u.isBlank() || p1.isBlank() || p2.isBlank()) {
            alert(Alert.AlertType.ERROR, "Semua field wajib diisi!");
            return;
        }

        if (!p1.equals(p2)) {
            alert(Alert.AlertType.ERROR, "Password tidak sama!");
            txtPass1.clear(); txtPass2.clear(); txtPass1.requestFocus();
            return;
        }

        boolean ok = UserRepo.register(u, p1);
        if (ok) {
            alert(Alert.AlertType.INFORMATION, "Registrasi berhasil!");
            load(e, "/view/Login.fxml", "Login");
        } else {
            alert(Alert.AlertType.ERROR, "Username sudah dipakai.");
            txtUsername.requestFocus();
        }
    }


    @FXML
    private void handleBack(ActionEvent e) {
        load(e, "/view/Login.fxml", "Login");
    }


    private void load(ActionEvent e, String fxml, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage st = (Stage) ((Node) e.getSource()).getScene().getWindow();
            st.setScene(new Scene(root));
            st.setTitle(title);
        } catch (IOException ex) { ex.printStackTrace(); }
    }
    private void alert(Alert.AlertType t, String m) {
        new Alert(t, m).showAndWait();
    }

}
