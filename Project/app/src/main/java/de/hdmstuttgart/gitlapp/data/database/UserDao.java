package de.hdmstuttgart.gitlapp.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.hdmstuttgart.gitlapp.models.User;

@Dao
public interface UserDao {

    @Insert
    void insertUsers(User ... users);

    @Update
    int updateUsers(User ... users);

    @Delete
    int deleteUsers(User ... users);

    @Query("SELECT * FROM users")
    List<User> getAllUsers();
}
