
package de.hdmstuttgart.gitlapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


/**
 * keeps the information of the logged in user including the access token for the gitlab api
 */

@Entity(tableName = "profile")
public class Profile {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int loggedInUserId;
    @ColumnInfo(name = "access_token")
    private String accessToken;
    private String hostUrl;

    @Ignore
    public Profile(int loggedInUserId, String accessToken, String hostUrl) {
        this.loggedInUserId = loggedInUserId;
        this.accessToken = accessToken;
        this.hostUrl = hostUrl;
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

    public String getHostUrl() {
        return hostUrl;
    }

    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }
}

