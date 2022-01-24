package de.hdmstuttgart.gitlapp.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import de.hdmstuttgart.gitlapp.models.Project;

@Dao
public interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProjects(List<Project> projects);

    @Query("SELECT * FROM projects")
    List<Project> getAllProjects();

    @Query("DELETE FROM projects")
    void clearProjectsTable();
}

