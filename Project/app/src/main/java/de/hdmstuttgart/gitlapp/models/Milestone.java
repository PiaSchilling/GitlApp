package de.hdmstuttgart.gitlapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "milestones")
public class Milestone {

    @PrimaryKey
    private final int id;
    private final int iid;
    @ColumnInfo(name = "project_id")
    private final int projectId;
    private final String title;
    //private final String description; //todo those fields will not be displayed, keep them?
    //private final State state;
    //private final Date createDate;
    //private final Date dueDate;

    public Milestone(int id, int iid, int projectId, String title, String description, State state, Date createDate, Date dueDate) {
        this.id = id;
        this.iid = iid;
        this.projectId = projectId;
        this.title = title;
        /*this.description = description;
        this.state = state;
        this.createDate = createDate;
        this.dueDate = dueDate;*/
    }
}
