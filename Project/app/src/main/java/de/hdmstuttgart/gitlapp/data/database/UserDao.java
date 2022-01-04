package de.hdmstuttgart.gitlapp.data.database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.hdmstuttgart.gitlapp.models.User;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) //todo implement more efficent method
    void insertUsers(User ... users);

    @Insert(onConflict = OnConflictStrategy.REPLACE) //todo implement more efficent method
    void insertUsers(List<User> users);

    @Update
    int updateUsers(User ... users);

    @Delete
    int deleteUsers(User ... users);

    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    @Query("SELECT * from users WHERE id = :userId")
    User getUserById(int userId);
}
