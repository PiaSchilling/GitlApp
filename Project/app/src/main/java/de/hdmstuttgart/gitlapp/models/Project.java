package de.hdmstuttgart.gitlapp.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "projects")
public class Project {

    @PrimaryKey
    private int id;
    private String name;
    private String description;
    private  int owner_id;
    private String avatar_url;
    private String name_with_namespace;
    private String owner_name;

    @Ignore
    public Project(int id, String name, String description, int owner_id, String avatar_url) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner_id = owner_id;
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

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public String getName_with_namespace() {
        return name_with_namespace;
    }

    public void setName_with_namespace(String name_with_namespace) {
        this.name_with_namespace = name_with_namespace;
    }

    public String getOwner_name() {
        int index = name_with_namespace.indexOf('/');
        return name_with_namespace.substring(0,index);
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }
}

