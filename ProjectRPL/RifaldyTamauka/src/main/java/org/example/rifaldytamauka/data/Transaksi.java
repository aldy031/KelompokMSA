package org.example.rifaldytamauka.data;

import java.time.LocalDate;

public class Transaksi {
    private int id;
    private String waktu;
    private String kategori;
    private int jumlah;
    private String catatan; // Tambahan properti catatan

    // Konstruktor lengkap
    public Transaksi(String waktu, int id, String kategori, int jumlah, String catatan) {
        this.waktu = waktu;
        this.id = id;
        this.kategori = kategori;
        this.jumlah = jumlah;
        this.catatan = catatan;
    }

    // Konstruktor tanpa ID
    public Transaksi(String waktu, String kategori, int jumlah, String catatan) {
        this.waktu = waktu;
        this.kategori = kategori;
        this.jumlah = jumlah;
        this.catatan = catatan;
    }

    // Konstruktor yang sering dipakai saat insert
    public Transaksi(String waktu, int id, String kategori, int jumlah) {
        this.waktu = waktu;
        this.id = id;
        this.kategori = kategori;
        this.jumlah = jumlah;
    }

    // Konstruktor default
    public Transaksi(String kategori, int jumlah) {
        this.waktu = LocalDate.now().toString();
        this.kategori = kategori;
        this.jumlah = jumlah;
    }

    // Getter dan Setter
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

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }
}
