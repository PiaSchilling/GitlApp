
package de.hdmstuttgart.gitlapp.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import de.hdmstuttgart.gitlapp.models.Label;

@Dao
public interface LabelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLabels(List<Label> labels);

    @Query("SELECT * FROM labels WHERE issue_id = :issueId") //get all labels of an specific issue
    List<Label> getIssueLabels(int issueId);

    @Query("UPDATE labels SET project_id = :projectId WHERE issue_id = 0 AND project_id = 0")
    void setProjectIdForIssue(int projectId);

    @Query("DELETE FROM labels")
    void clearLabelsTable();
}

