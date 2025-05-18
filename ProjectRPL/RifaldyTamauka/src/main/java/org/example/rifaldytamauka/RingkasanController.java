package org.example.rifaldytamauka;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

import java.sql.Connection;

public class RingkasanController {
    @FXML
    private PieChart pieChart;
    private Connection connection;

    private final String DB_URL = "jdbc:sqlite:mahasiswa.db";

    public void initialize() {
        getConnection();
        prepareData();
    }
    private void getConnection() {
        if ( connection == null){
            try {
                connection = java.sql.DriverManager.getConnection(DB_URL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Connection is already established");
        }
    }
    private void prepareData() {
        String sql = "SELECT katergori, COUNT(*) FROM mahasiswa GROUP BY katergori";
        try {
            java.sql.Statement stmt = connection.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String kategori = rs.getString("katergori");
                int jumlah = rs.getInt("COUNT(*)");
                pieChart.getData().add(new PieChart.Data(kategori, jumlah));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



