package de.hdmstuttgart.gitlapp.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.hdmstuttgart.gitlapp.models.Issue;
import de.hdmstuttgart.gitlapp.models.Note;

@Dao
public interface NoteDao {

    @Insert
    void insertNotes(Note ... notes);

    @Update
    int updateNote(Note ... notes);

    @Delete
    int deleteNotes(Note ... notes);

    @Query("SELECT * FROM notes")
    List<Note> getAllNotes();

    @Query("SELECT * FROM notes WHERE issue_id = :issueId")
    List<Issue> getIssueComments(int issueId);
}
