package de.hdmstuttgart.gitlapp.data.database;
import de.hdmstuttgart.gitlapp.models.Profile;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


@Dao
public interface ProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProfile(Profile profile);

    @Update
    int updateProfile(Profile profile);

    @Delete
    int deleteProfile(Profile profile);

    @Query("SELECT * FROM profile") //todo refactor so its only returning one profile
    Profile getProfile();
}

