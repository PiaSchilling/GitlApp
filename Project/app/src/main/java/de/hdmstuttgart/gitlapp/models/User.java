package de.hdmstuttgart.gitlapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    private final int id;
    private final String username;
    private final String name;
    @ColumnInfo(name = "avatar_url")
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
