package de.hdmstuttgart.gitlapp.models;

public class User {

    private final int id;
    private final String username;
    private final String name;
    private final String avatarUrl;
    private final String email;

    public User(int id, String username, String name, String avatarUrl, String email) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.email = email;
    }
}
