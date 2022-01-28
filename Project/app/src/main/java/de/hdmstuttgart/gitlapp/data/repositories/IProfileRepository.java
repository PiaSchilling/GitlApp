package de.hdmstuttgart.gitlapp.data.repositories;

import androidx.lifecycle.MutableLiveData;

import de.hdmstuttgart.gitlapp.models.User;

public interface IProfileRepository {

    void fetchUser(String url);

    void  createProfile();

    void setProfileInformation(int userId, String hostUrl, String accessToken);

    User getLoggedIdUser();

    int getLoggedInUserId();

    String getHostUrl();

    MutableLiveData<String> getMessageLiveData();

    void clearAppData();

}
