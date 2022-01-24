package de.hdmstuttgart.gitlapp.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.hdmstuttgart.gitlapp.models.Note;

@Dao
public interface NoteDao { //todo can be removed if we kick the comment feature

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNotes(Note ... notes);

    @Update
    int updateNote(Note ... notes);

    @Delete
    int deleteNotes(Note ... notes);

    @Query("SELECT * FROM notes")
    List<Note> getAllNotes();

    @Query("DELETE FROM notes")
    void clearNotesTable();

}

