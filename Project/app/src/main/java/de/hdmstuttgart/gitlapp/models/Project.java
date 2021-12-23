package de.hdmstuttgart.gitlapp.models;

import java.util.Date;
import java.util.List;

public class Project {

    private final int id;
    private final String name;
    private final String description;
    private final Date createDate;
    private final User owner;
    private final String avatarUrl;
    private final List<Label> projectLabels;
    private final List<Milestone> projectMilestones;
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
