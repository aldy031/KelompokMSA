package org.example.rifaldytamauka.data;

import java.time.LocalDate;
import java.util.Date;

public class Transaksi {
    private String waktu;
    private int id;
    private String kategori;
    private int jumlah;

    public Transaksi(String waktu, int id, String kategori, int jumlah) {
        this.waktu = waktu;
        this.id = id;
        this.kategori = kategori;
        this.jumlah = jumlah;
    }


    public Transaksi(String waktu, String kategori, int jumlah) {
        this.waktu = waktu;
        this.kategori = kategori;
        this.jumlah = jumlah;
    }

    public Transaksi(String kategori, int jumlah) {
        this.waktu = LocalDate.now().toString();
        this.kategori = kategori;
        this.jumlah = jumlah;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}
