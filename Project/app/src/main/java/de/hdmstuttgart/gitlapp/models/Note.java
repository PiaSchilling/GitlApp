package de.hdmstuttgart.gitlapp.models;

import java.util.Date;

public class Note {

    private final int id;
    private final User author;
    private final String body;
    private final Date createDate;

    public Note(int id, User author, String body, Date createDate) {
        this.id = id;
        this.author = author;
        this.body = body;
        this.createDate = createDate;
    }
}
