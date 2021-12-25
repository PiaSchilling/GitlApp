package de.hdmstuttgart.gitlapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;

@Entity(tableName = "issues")
public class Issue {

    @PrimaryKey
    private final int id;
    private final int weight;
    private final User author;
    private final State state;
    @ColumnInfo(name = "create_date")
    private final Date createDate;
    @ColumnInfo(name = "due_date")
    private final Date dueDate;
    private final String title;
    private final String description;
    @ColumnInfo(name = "thumbs_up")
    private final int thumbsUp;
    @ColumnInfo(name = "thumbs_down")
    private final int thumbsDown;
    @ColumnInfo(name = "project_id")
    private final int projectId;
    //private final Milestone milestone; //todo not sure if this is needed

    @Ignore
    private final List<Label> issueLabels;
    @Ignore
    private final List<Note> issueComments;

    public Issue(int id, int weight, User author, State state, Date createDate, Date dueDate, String title, String description, int thumbsUp, int thumbsDown, List<Label> issueLabels, List<Note> issueComments, int projectId) {
        this.id = id;
        this.weight = weight;
        this.author = author;
        this.state = state;
        this.createDate = createDate;
        this.dueDate = dueDate;
        this.title = title;
        this.description = description;
        this.thumbsUp = thumbsUp;
        this.thumbsDown = thumbsDown;
        this.issueLabels = issueLabels;
        this.issueComments = issueComments;

        this.projectId = projectId;
    }
}
