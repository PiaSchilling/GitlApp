
package de.hdmstuttgart.gitlapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


/**
 * keeps the information of the logged in user including the access token for the gitlab api //todo this should be a singleton (only one logged in user)
 */

@Entity(tableName = "profile")
public class Profile {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int loggedInUserId;
    @ColumnInfo(name = "access_token")
    private String accessToken;

    @Ignore
    public Profile(int loggedInUserId, String accessToken) {
        this.loggedInUserId = loggedInUserId;
        this.accessToken = accessToken;
    }

    public Profile(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLoggedInUserId() {
        return loggedInUserId;
    }

    public void setLoggedInUserId(int loggedInUserId) {
        this.loggedInUserId = loggedInUserId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}

