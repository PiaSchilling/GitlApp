package de.hdmstuttgart.gitlapp.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.hdmstuttgart.gitlapp.models.Project;

@Dao
public interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) //todo check for changes
    void insertProjects(Project ... projects);

    @Insert(onConflict = OnConflictStrategy.REPLACE) //todo check for changes
    void insertProjects(List<Project> projects);

    @Update
    int updateProjects(Project ... projects);

    @Delete
    int deleteProjects(Project ... projects);

    @Query("SELECT * FROM projects")
    List<Project> getAllProjects();

    @Query("DELETE FROM projects")
    void clearProjectsTable();
}

