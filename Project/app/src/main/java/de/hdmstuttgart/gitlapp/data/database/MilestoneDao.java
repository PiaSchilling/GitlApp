
package de.hdmstuttgart.gitlapp.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.hdmstuttgart.gitlapp.models.Milestone;

@Dao
public interface MilestoneDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMilestones(Milestone ... milestones);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMilestones(List<Milestone> milestones);

    @Update
    int updateMilestones(Milestone ... milestones);

    @Delete
    int deleteMilestones(Milestone ... milestones);

    @Query("SELECT * FROM milestones")
    List<Milestone> getAllMilestones();

    @Query("SELECT * FROM milestones WHERE project_id = :projectId")
    List<Milestone> getProjectMilestones(int projectId);

    @Query("SELECT * FROM milestones WHERE id = :milestoneId")
    Milestone getMilestoneById(int milestoneId);
}
