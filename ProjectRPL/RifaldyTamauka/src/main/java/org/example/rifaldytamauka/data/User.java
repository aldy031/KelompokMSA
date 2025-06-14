package org.example.rifaldytamauka.data;

public class user {
    private int id;

    private String username;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // konstruktor untuk Register (tanpa id)
    public user(String username, String password) {

        this.username = username;
        this.password = password;
    }
    // konstruktor full (dibaca dari DB)
    public user(int id, String username, String password) {
        this( username, password);
        this.id = id;
    }

    /* getter-setter */
}
