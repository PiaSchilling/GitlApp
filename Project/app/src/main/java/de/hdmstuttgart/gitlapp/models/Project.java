package de.hdmstuttgart.gitlapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "projects")
public class Project {

    @PrimaryKey
    private int id;
    private String name;
    private String description;
    private  int ownerId;
    @ColumnInfo(name = "avatar_url")
    private String avatarUrl;


    public Project(int id, String name, String description, int ownerId, String avatarUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.avatarUrl = avatarUrl;

    }

    public Project(){

    }
}

