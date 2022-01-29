
package de.hdmstuttgart.gitlapp.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "milestones")
public class Milestone {

    @PrimaryKey
    private int id;
    private int iid;
    private int project_id;
    private String title;

    @Ignore
    public Milestone(int id, int iid, int projectId, String title) {
        this.id = id;
        this.iid = iid;
        this.project_id = projectId;
        this.title = title;
    }

    public Milestone(){

    }

    @NonNull
    @Override
    public String toString() {
        return "Milestone{" +
                "id=" + id +
                ", iid=" + iid +
                ", project_id=" + project_id +
                ", title='" + title + '\'' +
                '}';
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

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

