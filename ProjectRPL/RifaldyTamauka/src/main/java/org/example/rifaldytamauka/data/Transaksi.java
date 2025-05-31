package org.example.rifaldytamauka.data;

import java.time.LocalDate;

public class Transaksi {
    private String waktu;
    private int id;
    private String kategori;
    private int jumlah;
    private String catatan;
    private String jenis; // Tambahan untuk membedakan Pemasukan/Pengeluaran

    // Constructor dengan jenis
    public Transaksi(String waktu, int id, String kategori, int jumlah, String catatan, String jenis) {
        this.waktu = waktu;
        this.id = id;
        this.kategori = kategori;
        this.jumlah = jumlah;
        this.catatan = catatan;
        this.jenis = jenis;
    }

    // Constructor tanpa jenis (backward compatibility)
    public Transaksi(String waktu, int id, String kategori, int jumlah, String catatan) {
        this.waktu = waktu;
        this.id = id;
        this.kategori = kategori;
        this.jumlah = jumlah;
        this.catatan = catatan;
        this.jenis = "Pengeluaran"; // default
    }

    // Getters and Setters
    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Transaksi{" +
                "waktu='" + waktu + '\'' +
                ", id=" + id +
                ", kategori='" + kategori + '\'' +
                ", jumlah=" + jumlah +
                ", catatan='" + catatan + '\'' +
                ", jenis='" + jenis + '\'' +
                '}';
    }
}
