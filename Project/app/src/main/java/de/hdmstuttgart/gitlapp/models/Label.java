package de.hdmstuttgart.gitlapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "labels")
public class Label {

    @PrimaryKey
    private final int id;
    private final String name;
    private final String color;

    @ColumnInfo(name = "issue_id")
    private final int issueId;
    @ColumnInfo(name = "project_id")
    private final int projectId;

    public Label(int id, String name, String color, int issueId, int projectId) {
        this.id = id;
        this.name = name;
        this.color = color;

        this.issueId = issueId;
        this.projectId = projectId;
    }
}
