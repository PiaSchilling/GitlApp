package de.hdmstuttgart.gitlapp.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.hdmstuttgart.gitlapp.models.Issue;

@Dao
public interface IssueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIssues(List<Issue> issues);

    @Update
    int updateIssues(Issue... issues);

    @Query("SELECT * FROM issues WHERE project_id = :projectId")
    List<Issue> getProjectIssues(int projectId);

    @Query("DELETE FROM issues")
    void clearIssuesTable();

}
