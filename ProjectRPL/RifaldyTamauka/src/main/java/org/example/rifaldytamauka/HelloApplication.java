package org.example.rifaldytamauka;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Tampilkan halaman login pertama kali
        showLoginPage(stage);
    }

    // Method untuk menampilkan halaman login dengan ukuran 700x500
    private void showLoginPage(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500); // Ukuran untuk halaman login
        stage.setTitle("Login Page");
        stage.setScene(scene);
        stage.centerOnScreen(); // Menjaga posisi tetap di tengah
        stage.show();
    }

    // Method untuk menampilkan halaman utama dengan ukuran 1059x694
    public static void showMainPage(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("HalamanUtama.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1059, 694); // Ukuran untuk halaman utama
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.centerOnScreen(); // Menjaga posisi tetap di tengah
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
