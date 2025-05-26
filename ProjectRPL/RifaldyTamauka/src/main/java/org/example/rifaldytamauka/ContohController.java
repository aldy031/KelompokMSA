//package org.example.rifaldytamauka;
//
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import org.example.rifaldytamauka.data.Transaksi;
//import org.example.rifaldytamauka.repo.TransaksiRepo;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//public class ContohController {
//    @FXML
//    private TextField jumlah;
//    @FXML
//    private TextField kategori;
//    @FXML
//    private VBox list;
//
//    @FXML
//    public void onButtonSubmit(ActionEvent event) {
//        try {
//            String jumlahText = jumlah.getText();
//            String kategoriText = kategori.getText();
//            int jumlahInt = Integer.parseInt(jumlahText); // ✅ Rename
//            Transaksi t = new Transaksi(kategoriText, jumlahInt);
//            TransaksiRepo.insertTransaksi(t);
//        } catch (NumberFormatException e) {
//            System.out.println("Input jumlah tidak valid: " + e.getMessage());
//        }
//    }
//
//    @FXML
//    public void onButtonShow(ActionEvent event) {
//        list.getChildren().clear(); // ✅ Bersihkan list dulu
//        ArrayList<Transaksi> transaksis = TransaksiRepo.getAllTransaksi();
//        for (Transaksi t : transaksis) {
//            HBox hBox = new HBox();
//            Label kategoriLabel = new Label(t.getKategori());
//            Label waktuLabel = new Label(" - " + t.getWaktu());
//            hBox.getChildren().addAll(kategoriLabel, waktuLabel);
//            list.getChildren().add(hBox);
//        }
//    }
//}
