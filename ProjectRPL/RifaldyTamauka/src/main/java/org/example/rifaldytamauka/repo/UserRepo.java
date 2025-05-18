package org.example.rifaldytamauka.repo;

import org.example.rifaldytamauka.DBManager;
import org.example.rifaldytamauka.data.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepo {
    public static void insertUser(User user) {
        try {
            Connection conn = DBManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO User (username, password)" +
                    "VALUES (?, ?)");
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPass());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static User login(String username, String pass) {
        User user = null;
        try {
            Connection conn = DBManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * From  User where username = ? and password = ?");
            stmt.setString(1, username);
            stmt.setString(2, pass);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(username, rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
