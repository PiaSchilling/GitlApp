
package de.hdmstuttgart.gitlapp.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import de.hdmstuttgart.gitlapp.models.Milestone;

@Dao
public interface MilestoneDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMilestones(List<Milestone> milestones);

    @Query("SELECT * FROM milestones WHERE id = :milestoneId")
    Milestone getMilestoneById(int milestoneId);

    @Query("DELETE FROM milestones")
    void clearMilestonesTable();
}
