package org.example.rifaldytamauka.data;


import java.time.LocalDateTime;

public class Ringkasan {
    private int id;
    private String kategori;
    private double totalPemasukan;
    private double totalPengeluaran;
    private double saldo;
    private double persenPerubahanPemasukan;
    private double persenPerubahanPengeluaran;
    private double persenPerubahanSaldo;
    private LocalDateTime lastUpdated;

    // Constructor default
    public Ringkasan() {
    }

    // Constructor dengan id dan kategori saja
    public Ringkasan(int id, String kategori, double saldo) {
        this.id = id;
        this.kategori = kategori;
        this.totalPemasukan = 0.0;
        this.totalPengeluaran = 0.0;
        this.saldo = saldo;
        this.persenPerubahanPemasukan = 0.0;
        this.persenPerubahanPengeluaran = 0.0;
        this.persenPerubahanSaldo = 0.0;
        this.lastUpdated = LocalDateTime.now();
    }

    // Constructor dengan kategori dan data keuangan dasar
    public Ringkasan(double totalPemasukan, double totalPengeluaran, double saldo) {
        this.totalPemasukan = totalPemasukan;
        this.totalPengeluaran = totalPengeluaran;
        this.saldo = totalPemasukan - totalPengeluaran;
        this.persenPerubahanPemasukan = 0.0;
        this.persenPerubahanPengeluaran = 0.0;
        this.persenPerubahanSaldo = 0.0;
        this.lastUpdated = LocalDateTime.now();
    }

    // Constructor dengan data keuangan lengkap (tanpa id)
    public Ringkasan(String kategori, double totalPemasukan, double totalPengeluaran,
                     double persenPerubahanPemasukan, double persenPerubahanPengeluaran) {
        this.kategori = kategori;
        this.totalPemasukan = totalPemasukan;
        this.totalPengeluaran = totalPengeluaran;
        this.saldo = totalPemasukan - totalPengeluaran;
        this.persenPerubahanPemasukan = persenPerubahanPemasukan;
        this.persenPerubahanPengeluaran = persenPerubahanPengeluaran;
        this.persenPerubahanSaldo = calculatePersenPerubahanSaldo();
        this.lastUpdated = LocalDateTime.now();
    }

    // Constructor dengan semua parameter kecuali id (untuk insert baru)
    public Ringkasan(String kategori, double totalPemasukan, double totalPengeluaran,
                     double saldo, double persenPerubahanPemasukan, double persenPerubahanPengeluaran,
                     double persenPerubahanSaldo) {
        this.kategori = kategori;
        this.totalPemasukan = totalPemasukan;
        this.totalPengeluaran = totalPengeluaran;
        this.saldo = saldo;
        this.persenPerubahanPemasukan = persenPerubahanPemasukan;
        this.persenPerubahanPengeluaran = persenPerubahanPengeluaran;
        this.persenPerubahanSaldo = persenPerubahanSaldo;
        this.lastUpdated = LocalDateTime.now();
    }

    // Constructor dengan semua parameter
    public Ringkasan(int id, String kategori, double totalPemasukan, double totalPengeluaran,
                     double saldo, double persenPerubahanPemasukan, double persenPerubahanPengeluaran,
                     double persenPerubahanSaldo, LocalDateTime lastUpdated) {
        this.id = id;
        this.kategori = kategori;
        this.totalPemasukan = totalPemasukan;
        this.totalPengeluaran = totalPengeluaran;
        this.saldo = saldo;
        this.persenPerubahanPemasukan = persenPerubahanPemasukan;
        this.persenPerubahanPengeluaran = persenPerubahanPengeluaran;
        this.persenPerubahanSaldo = persenPerubahanSaldo;
        this.lastUpdated = lastUpdated;
    }

    // Getter dan Setter untuk id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter dan Setter untuk kategori
    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    // Getter dan Setter untuk totalPemasukan
    public double getTotalPemasukan() {
        return totalPemasukan;
    }

    public void setTotalPemasukan(double totalPemasukan) {
        this.totalPemasukan = totalPemasukan;
    }

    // Getter dan Setter untuk totalPengeluaran
    public double getTotalPengeluaran() {
        return totalPengeluaran;
    }

    public void setTotalPengeluaran(double totalPengeluaran) {
        this.totalPengeluaran = totalPengeluaran;
    }

    // Getter dan Setter untuk saldo
    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    // Getter dan Setter untuk persenPerubahanPemasukan
    public double getPersenPerubahanPemasukan() {
        return persenPerubahanPemasukan;
    }

    public void setPersenPerubahanPemasukan(double persenPerubahanPemasukan) {
        this.persenPerubahanPemasukan = persenPerubahanPemasukan;
    }

    // Getter dan Setter untuk persenPerubahanPengeluaran
    public double getPersenPerubahanPengeluaran() {
        return persenPerubahanPengeluaran;
    }

    public void setPersenPerubahanPengeluaran(double persenPerubahanPengeluaran) {
        this.persenPerubahanPengeluaran = persenPerubahanPengeluaran;
    }

    // Getter dan Setter untuk persenPerubahanSaldo
    public double getPersenPerubahanSaldo() {
        return persenPerubahanSaldo;
    }

    public void setPersenPerubahanSaldo(double persenPerubahanSaldo) {
        this.persenPerubahanSaldo = persenPerubahanSaldo;
    }

    // Getter dan Setter untuk lastUpdated
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    // Method toString untuk debugging
    @Override
    public String toString() {
        return "Ringkasan{" +
                "id=" + id +
                ", kategori='" + kategori + '\'' +
                ", totalPemasukan=" + totalPemasukan +
                ", totalPengeluaran=" + totalPengeluaran +
                ", saldo=" + saldo +
                ", persenPerubahanPemasukan=" + persenPerubahanPemasukan +
                ", persenPerubahanPengeluaran=" + persenPerubahanPengeluaran +
                ", persenPerubahanSaldo=" + persenPerubahanSaldo +
                ", lastUpdated=" + lastUpdated +
                '}';
    }

    // Method untuk menghitung saldo otomatis
    public void hitungSaldo() {
        this.saldo = this.totalPemasukan - this.totalPengeluaran;
    }

    // Method overloading untuk update data keuangan
    public void updateData(double totalPemasukan, double totalPengeluaran) {
        this.totalPemasukan = totalPemasukan;
        this.totalPengeluaran = totalPengeluaran;
        hitungSaldo();
        this.lastUpdated = LocalDateTime.now();
    }

    // Method overloading untuk update data lengkap
    public void updateData(double totalPemasukan, double totalPengeluaran,
                           double persenPerubahanPemasukan, double persenPerubahanPengeluaran) {
        this.totalPemasukan = totalPemasukan;
        this.totalPengeluaran = totalPengeluaran;
        this.persenPerubahanPemasukan = persenPerubahanPemasukan;
        this.persenPerubahanPengeluaran = persenPerubahanPengeluaran;
        this.persenPerubahanSaldo = calculatePersenPerubahanSaldo();
        hitungSaldo();
        this.lastUpdated = LocalDateTime.now();
    }

    // Method overloading untuk update semua data keuangan
    public void updateData(double totalPemasukan, double totalPengeluaran, double saldo,
                           double persenPerubahanPemasukan, double persenPerubahanPengeluaran,
                           double persenPerubahanSaldo) {
        this.totalPemasukan = totalPemasukan;
        this.totalPengeluaran = totalPengeluaran;
        this.saldo = saldo;
        this.persenPerubahanPemasukan = persenPerubahanPemasukan;
        this.persenPerubahanPengeluaran = persenPerubahanPengeluaran;
        this.persenPerubahanSaldo = persenPerubahanSaldo;
        this.lastUpdated = LocalDateTime.now();
    }

    // Method untuk menghitung persentase perubahan saldo
    private double calculatePersenPerubahanSaldo() {
        if (totalPemasukan == 0 && totalPengeluaran == 0) {
            return 0.0;
        }
        double saldoBaru = totalPemasukan - totalPengeluaran;
        if (saldo == 0) {
            return saldoBaru > 0 ? 100.0 : (saldoBaru < 0 ? -100.0 : 0.0);
        }
        return ((saldoBaru - saldo) / Math.abs(saldo)) * 100;
    }

    // Method untuk menambah pemasukan
    public void tambahPemasukan(double jumlah) {
        if (jumlah > 0) {
            this.totalPemasukan += jumlah;
            hitungSaldo();
            this.lastUpdated = LocalDateTime.now();
        }
    }

    // Method overloading untuk menambah pemasukan dengan persentase
    public void tambahPemasukan(double jumlah, double persenPerubahan) {
        if (jumlah > 0) {
            this.totalPemasukan += jumlah;
            this.persenPerubahanPemasukan = persenPerubahan;
            hitungSaldo();
            this.lastUpdated = LocalDateTime.now();
        }
    }

    // Method untuk menambah pengeluaran
    public void tambahPengeluaran(double jumlah) {
        if (jumlah > 0) {
            this.totalPengeluaran += jumlah;
            hitungSaldo();
            this.lastUpdated = LocalDateTime.now();
        }
    }

    // Method overloading untuk menambah pengeluaran dengan persentase
    public void tambahPengeluaran(double jumlah, double persenPerubahan) {
        if (jumlah > 0) {
            this.totalPengeluaran += jumlah;
            this.persenPerubahanPengeluaran = persenPerubahan;
            hitungSaldo();
            this.lastUpdated = LocalDateTime.now();
        }
    }

    // Method untuk reset semua data keuangan
    public void resetData() {
        this.totalPemasukan = 0.0;
        this.totalPengeluaran = 0.0;
        this.saldo = 0.0;
        this.persenPerubahanPemasukan = 0.0;
        this.persenPerubahanPengeluaran = 0.0;
        this.persenPerubahanSaldo = 0.0;
        this.lastUpdated = LocalDateTime.now();
    }

    // Method untuk cek apakah dalam kondisi surplus atau defisit
    public String getStatusKeuangan() {
        if (saldo > 0) {
            return "SURPLUS";
        } else if (saldo < 0) {
            return "DEFISIT";
        } else {
            return "SEIMBANG";
        }
    }

    // Method untuk mendapatkan ringkasan singkat
    public String getRingkasanSingkat() {
        return String.format("Kategori: %s | Saldo: %.2f | Status: %s",
                kategori, saldo, getStatusKeuangan());
    }

    // Method overloading untuk format ringkasan dengan detail berbeda
    public String getRingkasanSingkat(boolean includePersentase) {
        if (includePersentase) {
            return String.format("Kategori: %s | Saldo: %.2f (%.1f%%) | Status: %s",
                    kategori, saldo, persenPerubahanSaldo, getStatusKeuangan());
        }
        return getRingkasanSingkat();
    }
}