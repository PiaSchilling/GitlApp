package de.hdmstuttgart.gitlapp.data.database;
import de.hdmstuttgart.gitlapp.models.Profile;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


@Dao
public interface ProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProfile(Profile profile);

    @Query("SELECT * FROM profile")
    Profile getProfile();

    @Query("DELETE FROM profile")
    void clearProfileTable();
}

