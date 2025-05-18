package org.example.rifaldytamauka.data;

public class User {
    private int id;
    private String email;
    private String username;
    private String password;

    // konstruktor untuk Register (tanpa id)
    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
    // konstruktor full (dibaca dari DB)
    public User(int id, String email, String username, String password) {
        this(email, username, password);
        this.id = id;
    }

    /* getter-setter */
}
