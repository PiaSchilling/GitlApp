package de.hdmstuttgart.gitlapp.data.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import de.hdmstuttgart.gitlapp.data.database.AppDatabase;
import de.hdmstuttgart.gitlapp.data.network.GitLabClient;
import de.hdmstuttgart.gitlapp.models.Profile;
import de.hdmstuttgart.gitlapp.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {

    // - - - - - Sources - - - - - - - -
    private final AppDatabase appDatabase;
    private final GitLabClient gitLabClient;

    MutableLiveData<String> messageLiveData = new MutableLiveData<>();

    // - - - - - Profile attributes - - - -
    private User loggedIdUser;
    private int userId;
    private String accessToken;
    private String hostUrl;


    public ProfileRepository(AppDatabase appDatabase, GitLabClient gitLabClient) {
        this.appDatabase = appDatabase;
        this.gitLabClient = gitLabClient;
    }

    /**
     * creates a profile and inserts in the database
     */
    private void createProfile(){
        Log.d("Bug","createProfile triggered");

        Profile profile = new Profile(loggedIdUser.getId(), accessToken, hostUrl);
        Log.d("ProfileRepo","Profile created " + profile.toString());
        appDatabase.profileDao().insertProfile(profile);
    }

    /**
     * fetches user from network -> only if the call is successful the user exists
     * only if the user exists a profile is created
     * @param url the whole request url (including the base url bc this is the only way making api calls with dynamic base url)
     */
    private void fetchUser(String url){
        Log.d("Bug","fetchUser triggered");
        Call<User> call = gitLabClient.getSingleUserWithWholeUrl(url, this.accessToken);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        loggedIdUser = response.body();
                        appDatabase.userDao().insertUsers(loggedIdUser);
                        createProfile();
                        messageLiveData.setValue("Call successful, profile created");
                        Log.d("ProfileRepo","SUCCESS" + loggedIdUser.toString());
                    }
                }else{
                    Log.e("ProfileRepo","call not successful, code" + response.code());
                    if(response.code() == 401){
                        messageLiveData.setValue("Not able to authorize, check access token");
                    }else if(response.code() == 404){
                        messageLiveData.setValue("Cannot find user with id " + userId + " ,check user id");
                    }else{
                        messageLiveData.setValue("Error, code " + response.code());
                    }

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("ProfileRepo","FAIL " + t.getMessage());
                messageLiveData.setValue("Not able to create profile, check internet connection");
            }
        });
    }

    public void setProfileInformation(int userId, String hostUrl, String accessToken){
        this.userId = userId;
        this.accessToken = "Bearer " + accessToken;
        this.hostUrl = hostUrl;
        String url = hostUrl + "/api/v4/users/" + userId;
        fetchUser(url);
    }

    public User getLoggedIdUser(){
        int localUserId = appDatabase.profileDao().getProfile().getLoggedInUserId();
        return appDatabase.userDao().getUserById(localUserId);
    }

    public Profile getUserProfile(){
        return appDatabase.profileDao().getProfile();
    }

    public MutableLiveData<String> getMessageLiveData() {
        return messageLiveData;
    }


    public int getLoggedInUserId(){
        return appDatabase.profileDao().getProfile().getLoggedInUserId();
    }

    public String getHostUrl(){
        return appDatabase.profileDao().getProfile().getHostUrl();
    }


    // - - - - - clear data at log out - - - -
    public void clearAppData(){
        appDatabase.issueDao().clearIssuesTable();
        appDatabase.labelDao().clearLabelsTable();
        appDatabase.milestoneDao().clearMilestonesTable();
        appDatabase.noteDao().clearNotesTable();
        appDatabase.profileDao().clearProfileTable();
        appDatabase.projectDao().clearProjectsTable();
        appDatabase.userDao().clearUserTable();
    }

}
