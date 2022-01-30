package de.hdmstuttgart.gitlapp.data.network;

public enum NetworkStatus {

    LOADING("-"),
    NETWORK_ERROR("Oh no check your wifi connection"),
    SUCCESS("Call successful"),
    AUTHENTICATION_ERROR("Problem with authentication"),
    NOT_FOUND("Not found"),
    FAIL("Oh no there went something wrong"),
    POST_SUCCESS("Add issue successful");


    public String message;

    NetworkStatus(String message){
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
