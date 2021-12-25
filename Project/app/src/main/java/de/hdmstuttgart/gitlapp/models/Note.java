package de.hdmstuttgart.gitlapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "notes")
public class Note {

    @PrimaryKey
    private final int id;
    private final User author;
    private final String body;
    @ColumnInfo(name = "create_date")
    private final Date createDate;
    @ColumnInfo(name = "issue_id")
    private final int issueId;

    public Note(int id, User author, String body, Date createDate, int issueId) {
        this.id = id;
        this.author = author;
        this.body = body;
        this.createDate = createDate;
        this.issueId = issueId;
    }
}
