
package de.hdmstuttgart.gitlapp.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.Map;

import de.hdmstuttgart.gitlapp.models.Label;

@Dao
public interface LabelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLabels(Label... labels);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLabels(List<Label> labels);

    @Update
    int updateLabels(Label... labels); //returns an int to indicate how many rows were been deleted

    @Delete
    int deleteLabels(Label... labels); //returns an int to indicate how many rows were been deleted

    @Query("SELECT * FROM labels")
    List<Label> getAllLabels();

    @Query("SELECT * FROM labels WHERE project_id = :projectId") //get all labels of a specific project
    List<Label> getProjectLabels(int projectId);

    @Query("SELECT * FROM labels WHERE issue_id = :issueId") //get all labels of an specific issue
    List<Label> getIssueLabels(int issueId);



}

