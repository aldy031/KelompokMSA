package org.example.rifaldytamauka.repo;

import org.example.rifaldytamauka.util.DBConnector;
import org.example.rifaldytamauka.data.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepo {


    public static boolean register(String username, String password, String p1) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DBConnector.getInstance().getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, username);
            st.setString(2, password);
            return st.executeUpdate() == 1;

        } catch (SQLException e) {
            return false;
        }
    }


    public static user login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DBConnector.getInstance().getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, username);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();

//            if (rs.next()) {
//                return new User(rs.getString("username"),
//                        rs.getString("password"));
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;    // gagal
    }
}
