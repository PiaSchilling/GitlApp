package de.hdmstuttgart.gitlapp.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.hdmstuttgart.gitlapp.models.Issue;

@Dao
public interface IssueDao {

    @Insert
    void insertIssues(Issue ... issues);

    @Update
    int updateIssues(Issue ... issues);

    @Delete
    int deleteIssues(Issue ... issues);

    @Query("SELECT * FROM issues")
    List<Issue> getAllIssues();

    @Query("SELECT * FROM issues WHERE project_id = :projectId")
    List<Issue> getProjectIssues(int projectId);
}
