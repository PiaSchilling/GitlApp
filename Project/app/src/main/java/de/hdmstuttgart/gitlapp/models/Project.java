package de.hdmstuttgart.gitlapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.PrimaryKey;

import java.util.List;


@Entity(tableName = "projects")
public class Project {

    @PrimaryKey
    private int id;
    private String name;
    private String description;
    private  int ownerId;
    private String avatar_url;

    @Ignore
    private List<Label> projectLabels;
    @Ignore
    private List<Milestone> projectMilestones;


    @Ignore
    public Project(int id, String name, String description, int ownerId, String avatar_url) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.avatar_url = avatar_url;
    }

    public Project(){

    }


    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", avatar_url='" + avatar_url + '\'' +
                '}';
    }

    // - - - - - - getter and setter - - - - - -
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
}

