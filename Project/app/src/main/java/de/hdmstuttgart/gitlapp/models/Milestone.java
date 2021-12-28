
package de.hdmstuttgart.gitlapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "milestones")
public class Milestone {

    @PrimaryKey
    private int id;
    private int iid;
    @ColumnInfo(name = "project_id")
    private int projectId;
    private String title;

    @Ignore
    public Milestone(int id, int iid, int projectId, String title, String description, State state, Date createDate, Date dueDate) {
        this.id = id;
        this.iid = iid;
        this.projectId = projectId;
        this.title = title;
    }

    public Milestone(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIid() {
        return iid;
    }

    public void setIid(int iid) {
        this.iid = iid;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

