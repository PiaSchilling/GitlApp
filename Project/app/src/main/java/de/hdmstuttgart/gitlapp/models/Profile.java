package de.hdmstuttgart.gitlapp.models;

/**
 * keeps the information of the logged in user including the access token for the gitlab api
 */
public class Profile {

    private final User loggedInUser;
    private final String accessToken;

    public Profile(User loggedInUser, String accessToken) {
        this.loggedInUser = loggedInUser;
        this.accessToken = accessToken;
    }
}
