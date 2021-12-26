package de.hdmstuttgart.gitlapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "labels")
public class Label {

    @PrimaryKey
    private int id;
    private String name;
    private String color;

    @ColumnInfo(name = "issue_id")
    private int issueId;
    @ColumnInfo(name = "project_id")
    private int projectId;

    public Label(int id, String name, String color, int issueId, int projectId) {
        this.id = id;
        this.name = name;
        this.color = color;

        this.issueId = issueId;
        this.projectId = projectId;
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

    public int getIssueId() {
        return issueId;
    }

    public void setIssueId(int issueId) {
        this.issueId = issueId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}

