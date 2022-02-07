package de.hdmstuttgart.gitlapp.data.network;

/**
 * network status indicators which are set to live data objects by the repos
 */
public enum NetworkStatus {

    //general
    LOADING("-"),
    DEFAULT("-"),
    NETWORK_ERROR("Oh no check your wifi connection"),
    FAIL("Oh no there went something wrong"),


    //issues, Projects
    SUCCESS("Call successful"),
    PROJECT_NOT_FOUND("Not found"),
    ISSUE_POST_SUCCESS("Add issue successful"),

    //profile
    PROFILE_CREATED("Profile created"),
    AUTHENTICATION_ERROR("Oops not able to authorize, check access token"),
    PROFILE_NOT_FOUND("_"),
    MALFORMED_URL("Oops, the url seems to be incorrect");

    public String message;

    NetworkStatus(String message){
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
