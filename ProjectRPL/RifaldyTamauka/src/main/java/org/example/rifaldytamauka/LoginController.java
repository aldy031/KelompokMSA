
package org.example.rifaldytamauka;

import javafx.scene.control.PasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


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
        Alert alert;
        if (txtUsername.getText().equals(CORRECT_USERNAME) && txtPassword.getText().equals(CORRECT_PASSWORD)) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Information");
            alert.setContentText("Login success!!");
            alert.showAndWait();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("HalamanUtama.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Halaman Utama");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Login failed!! Please check again.");
            alert.showAndWait();
            txtUsername.requestFocus();
        }
    }
}



//package org.example.rifaldytamauka;
//
//import javafx.fxml.FXML;
//import javafx.scene.control.Alert;
//import javafx.scene.control.PasswordField;
//import javafx.scene.control.TextField;
//import javafx.scene.input.KeyCode;
//import javafx.scene.input.KeyEvent;
//
//import java.io.IOException;
//
//public class LoginController {
//    private static final String CORRECT_USERNAME = "admin";
//    private static final String CORRECT_PASSWORD = "admin";
//
//    @FXML private TextField txtUsername;
//    @FXML private PasswordField txtPassword;
//
//    @FXML
//    protected void onKeyPressEvent(KeyEvent event) throws IOException {
//        if( event.getCode() == KeyCode.ENTER ) {
//            btnClickLogin();
//        }
//    }
//    @FXML
//    protected void btnClickLogin() throws IOException {
//        Alert alert;
//        if (txtUsername.getText().equals(CORRECT_USERNAME) && txtPassword.getText().equals(CORRECT_PASSWORD)) {
//            alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setHeaderText("Information");
//            alert.setContentText("Login success!!");
//            alert.showAndWait();
//            HelloApplication.setRoot("HalamanUtama", "",false);
//        } else {
//            alert = new Alert(Alert.AlertType.ERROR);
//            alert.setHeaderText("Error");
//            alert.setContentText("Login failed!! Please check again.");
//            alert.showAndWait();
//            txtUsername.requestFocus();
//        }
//    }
//}





