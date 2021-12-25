package de.hdmstuttgart.gitlapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;

@Entity(tableName = "projects")
public class Project {

    @PrimaryKey
    private final int id;
    private final String name;
    private final String description;
    private final Date createDate;
    private final User owner;
    @ColumnInfo(name = "avatar_url")
    private final String avatarUrl;

    @Ignore
    private final List<Label> projectLabels;
    @Ignore
    private final List<Milestone> projectMilestones;
    @Ignore
    private final List<Issue> projectIssues;

    public Project(int id, String name, String description, Date createDate, User owner, String avatarUrl, List<Label> projectLabels, List<Milestone> projectMilestones, List<Issue> projectIssues) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createDate = createDate;
        this.owner = owner;
        this.avatarUrl = avatarUrl;
        this.projectLabels = projectLabels;
        this.projectMilestones = projectMilestones;
        this.projectIssues = projectIssues;
    }
}
