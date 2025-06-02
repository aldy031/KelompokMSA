package org.example.rifaldytamauka.data;

public class Transaksi extends Ringkasan {
    private String waktu;
    private int id;
    private String kategori;
    private int jumlah;
    private String catatan;
    private String jenis;

    public Transaksi(int jumlah) {
        this.jumlah = jumlah;
    }

    // Constructor dengan jenis
    public Transaksi(String waktu, int id, String kategori, int jumlah, String catatan, String jenis) {
        this.waktu = waktu;
        this.id = id;
        this.kategori = kategori;
        this.jumlah = jumlah;
        this.catatan = catatan;
        this.jenis = jenis;
    }

    // Constructor tanpa jenis (untuk kompatibilitas dengan controller yang ada)
    public Transaksi(String waktu, int id, String kategori, int jumlah, String catatan) {
        this.waktu = waktu;
        this.id = id;
        this.kategori = kategori;
        this.jumlah = jumlah;
        this.catatan = catatan;
        this.jenis = "umum"; // Default value
    }

    // Getters
    public String getWaktu() {
        return waktu;
    }

    public int getId() {
        return id;
    }

    public String getKategori() {
        return kategori;
    }

    public int getJumlah() {
        return jumlah;
    }

    public String getCatatan() {
        return catatan;
    }

    public String getJenis() {
        return jenis;
    }

    // Setters
    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
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