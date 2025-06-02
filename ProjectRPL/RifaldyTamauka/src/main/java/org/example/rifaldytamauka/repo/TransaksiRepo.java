package org.example.rifaldytamauka.repo;

import org.example.rifaldytamauka.util.DBConnector;
import org.example.rifaldytamauka.data.Transaksi;

import java.sql.*;
import java.util.ArrayList;

public class TransaksiRepo {

    // Menambahkan transaksi baru ke database
    public static boolean insertTransaksi(Transaksi transaksi) {
        String sql = "INSERT INTO Transaksi (waktu, kategori, jumlah, catatan, jenis) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnector.getInstance().getConnection();

            // Cek apakah connection masih aktif
            if (conn == null || conn.isClosed()) {
                System.err.println("Database connection is null or closed");
                return false;
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, transaksi.getWaktu());
            pstmt.setString(2, transaksi.getKategori());
            pstmt.setInt(3, transaksi.getJumlah());
            pstmt.setString(4, transaksi.getCatatan());

            // Handle jenis field - berdasarkan database schema harus 'pemasukan' atau 'pengeluaran'
            String jenis = transaksi.getJenis();
            if (jenis == null || jenis.trim().isEmpty()) {
                // Default ke pengeluaran jika tidak diisi
                jenis = "pengeluaran";
            }
            // Validasi jenis sesuai dengan CHECK constraint
            if (!jenis.equals("pemasukan") && !jenis.equals("pengeluaran")) {
                jenis = "pengeluaran"; // Default fallback
            }
            pstmt.setString(5, jenis);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Insert transaksi - rows affected: " + rowsAffected);
            return rowsAffected > 0; // Return true jika berhasil insert

        } catch (SQLException e) {
            System.err.println("Error inserting transaksi: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            // Tutup PreparedStatement tapi jangan tutup Connection (karena digunakan di tempat lain)
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Mengambil semua transaksi dari database
    public static ArrayList<Transaksi> getAllTransaksi() {
        ArrayList<Transaksi> transactions = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnector.getInstance().getConnection();

            if (conn == null || conn.isClosed()) {
                System.err.println("Database connection is null or closed");
                return transactions;
            }

            stmt = conn.prepareStatement("SELECT * FROM Transaksi ORDER BY id DESC");
            rs = stmt.executeQuery();

            while (rs.next()) {
                String jenis = rs.getString("jenis");
                if (jenis == null) jenis = "pengeluaran"; // Handle null jenis dengan default yang valid

                Transaksi transaksi = new Transaksi(
                        rs.getString("waktu"),
                        rs.getInt("id"),
                        rs.getString("kategori"),
                        rs.getInt("jumlah"),
                        rs.getString("catatan"),
                        jenis
                );
                transactions.add(transaksi);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all transaksi: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Tutup resources
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return transactions;
    }

    // Mengambil semua transaksi berdasarkan kategori
    public static ArrayList<Transaksi> getAllTransaksiByKategori(String kategori) {
        ArrayList<Transaksi> transactions = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnector.getInstance().getConnection();

            if (conn == null || conn.isClosed()) {
                System.err.println("Database connection is null or closed");
                return transactions;
            }

            stmt = conn.prepareStatement("SELECT * FROM Transaksi WHERE kategori = ? ORDER BY id DESC");
            stmt.setString(1, kategori);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String jenis = rs.getString("jenis");
                if (jenis == null) jenis = "pengeluaran"; // Handle null jenis dengan default yang valid

                Transaksi transaksi = new Transaksi(
                        rs.getString("waktu"),
                        rs.getInt("id"),
                        rs.getString("kategori"),
                        rs.getInt("jumlah"),
                        rs.getString("catatan"),
                        jenis
                );
                transactions.add(transaksi);
            }
        } catch (SQLException e) {
            System.err.println("Error getting transaksi by kategori: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Tutup resources
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return transactions;
    }

    // Method untuk delete transaksi (jika diperlukan)
    public static boolean deleteTransaksi(int id) {
        String sql = "DELETE FROM Transaksi WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnector.getInstance().getConnection();

            if (conn == null || conn.isClosed()) {
                System.err.println("Database connection is null or closed");
                return false;
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting transaksi: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}