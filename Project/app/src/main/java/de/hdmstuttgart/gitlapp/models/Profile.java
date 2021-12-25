package de.hdmstuttgart.gitlapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * keeps the information of the logged in user including the access token for the gitlab api //todo this should be a singleton (only one logged in user)
 */
@Entity(tableName = "profile")
public class Profile {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "logged_in_user")
    private final User loggedInUser;
    @ColumnInfo(name = "access_token")
    private final String accessToken;

    public Profile(User loggedInUser, String accessToken) {
        this.loggedInUser = loggedInUser;
        this.accessToken = accessToken;
    }
}
