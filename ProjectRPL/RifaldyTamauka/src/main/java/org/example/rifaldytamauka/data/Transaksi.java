package org.example.rifaldytamauka.data;

import java.time.LocalDate;

public class Transaksi {
    private int id;
    private String waktu;
    private String kategori;
    private int jumlah;
    private String catatan;
    private String jenis; // ✅ Tambahan properti jenis

    // Konstruktor lengkap
    public Transaksi(String waktu, int id, String kategori, int jumlah, String catatan, String jenis) {
        this.waktu = waktu;
        this.id = id;
        this.kategori = kategori;
        this.jumlah = jumlah;
        this.catatan = catatan;
        this.jenis = jenis;
    }

    // Konstruktor tanpa ID
    public Transaksi(String waktu, String kategori, int jumlah, String catatan, String jenis) {
        this.waktu = waktu;
        this.kategori = kategori;
        this.jumlah = jumlah;
        this.catatan = catatan;
        this.jenis = jenis;
    }

    // Konstruktor saat insert (tanpa jenis)
    public Transaksi(String waktu, int id, String kategori, int jumlah, String catatan) {
        this.waktu = waktu;
        this.id = id;
        this.kategori = kategori;
        this.jumlah = jumlah;
        this.catatan = catatan;
    }

    // Konstruktor default untuk testing atau cepat input
    public Transaksi(String kategori, int jumlah, String jenis) {
        this.waktu = LocalDate.now().toString();
        this.kategori = kategori;
        this.jumlah = jumlah;
        this.jenis = jenis;
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

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }
}
