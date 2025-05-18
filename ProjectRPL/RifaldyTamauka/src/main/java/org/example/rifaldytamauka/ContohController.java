package org.example.rifaldytamauka;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.rifaldytamauka.data.Transaksi;
import org.example.rifaldytamauka.repo.TransaksiRepo;


import java.io.IOException;
import java.util.ArrayList;

public class ContohController {
    @FXML
    TextField jumlah;
    @FXML
    TextField kategori;
    @FXML
    VBox list;

    @FXML
    public void onButtonSubmit(ActionEvent event) throws IOException {
        String jumlahText = jumlah.getText();
        String kategoriText = kategori.getText();
        int jumlah =  Integer.parseInt(jumlahText);
        Transaksi t = new Transaksi(kategoriText, jumlah);
        TransaksiRepo.insertTransaksi(t);
    }

    @FXML
    public void onButtonShow(ActionEvent event) throws IOException {
        ArrayList<Transaksi> transaksis = TransaksiRepo.getAllTransaksi();
        HBox hBox = new HBox();
        Label label = new Label(transaksis.get(0).getKategori());
        hBox.getChildren().add(label);
        label = new Label(transaksis.get(0).getWaktu());
        hBox.getChildren().add(label);
        list.getChildren().add(hBox);
    }
}
