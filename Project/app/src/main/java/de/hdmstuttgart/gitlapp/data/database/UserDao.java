package de.hdmstuttgart.gitlapp.data.database;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import de.hdmstuttgart.gitlapp.models.User;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) //todo implement more efficient method
    void insertUsers(User ... users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(List<User> users);

    @Query("SELECT * from users WHERE id = :userId")
    User getUserById(int userId);

    @Query("DELETE FROM users")
    void clearUserTable();
}
