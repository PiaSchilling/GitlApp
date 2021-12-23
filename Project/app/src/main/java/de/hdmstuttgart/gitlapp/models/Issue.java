package de.hdmstuttgart.gitlapp.models;

import java.util.Date;
import java.util.List;

public class Issue {

    private final int id;
    private final int weight;
    private final User author;
    private final State state;
    private final Date createDate;
    private final Date dueDate;
    private final String title;
    private final String description;
    private final int thumbsUp;
    private final int thumbsDown;
    //private final Milestone milestone; //todo not sure if this is needed
    private final List<Label> issueLabels;
    private final List<Note> issueComments;

    public Issue(int id, int weight, User author, State state, Date createDate, Date dueDate, String title, String description, int thumbsUp, int thumbsDown, List<Label> issueLabels, List<Note> issueComments) {
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
    }
}
