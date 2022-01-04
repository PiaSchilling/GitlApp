package de.hdmstuttgart.gitlapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "labels" , primaryKeys = {"id", "issue_id"})
public class Label {

    //@PrimaryKey
    private int id;
    private String name;
    private String color;


    private int issue_id;
    @ColumnInfo(name = "project_id")
    private int projectId;

   @Ignore
    public Label(int id, String name, String color, int issue_id, int projectId) {
        this.id = id;
        this.name = name;
        this.color = color;

        this.issue_id = issue_id;
        this.projectId = projectId;
    }

    public Label(){
    }

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getIssue_id() {
        return issue_id;
    }

    public void setIssue_id(int issue_id) {
        this.issue_id = issue_id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}

