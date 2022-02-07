package de.hdmstuttgart.gitlapp.data.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.Objects;

import de.hdmstuttgart.gitlapp.data.database.AppDatabase;
import de.hdmstuttgart.gitlapp.data.network.GitLabClient;
import de.hdmstuttgart.gitlapp.data.network.NetworkStatus;
import de.hdmstuttgart.gitlapp.models.Profile;
import de.hdmstuttgart.gitlapp.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository implements IProfileRepository{

    // - - - - - Sources - - - - - - - -
    private final AppDatabase appDatabase;
    private final GitLabClient gitLabClient;

    private final MutableLiveData<NetworkStatus> messageLiveData = new MutableLiveData<>();

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
    @Override
    public void createProfile(){
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
    @Override
    public void fetchUser(String url){
        Call<User> call = gitLabClient.getSingleUserWithWholeUrl(url, this.accessToken);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        loggedIdUser = response.body();
                        appDatabase.userDao().insertUsers(loggedIdUser);
                        createProfile();
                        messageLiveData.setValue(NetworkStatus.PROFILE_CREATED);
                        Log.d("ProfileRepo","SUCCESS" + loggedIdUser.toString());
                    }
                }else{
                    Log.e("ProfileRepo","call not successful, code" + response.code());
                    if(response.code() == 401){
                        messageLiveData.setValue(NetworkStatus.AUTHENTICATION_ERROR);
                    }else if(response.code() == 404){
                        NetworkStatus.PROFILE_NOT_FOUND.setMessage("Oops cannot find user with id " + userId + ", check user id");
                        messageLiveData.setValue(NetworkStatus.PROFILE_NOT_FOUND);
                    }else{
                        messageLiveData.setValue(NetworkStatus.FAIL);
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                try{
                    Log.e("ProfileRepo","FAIL " + t.getMessage());
                    if(Objects.requireNonNull(t.getMessage()).contains("Malformed URL")){
                        messageLiveData.setValue(NetworkStatus.MALFORMED_URL);
                    }else{
                        NetworkStatus.NETWORK_ERROR.setMessage("Not able to create profile, check internet connection");
                        messageLiveData.setValue(NetworkStatus.NETWORK_ERROR);
                    }
                }catch (NullPointerException e){
                    Log.e("Api",e.getMessage());
                }
            }
        });
    }

    @Override
    public void setProfileInformation(int userId, String hostUrl, String accessToken){
        this.userId = userId;
        this.accessToken = "Bearer " + accessToken;
        this.hostUrl = hostUrl;
        String url = hostUrl + "/api/v4/users/" + userId;
        fetchUser(url);
    }

    @Override
    public User getLoggedIdUser(){
        int localUserId = appDatabase.profileDao().getProfile().getLoggedInUserId();
        return appDatabase.userDao().getUserById(localUserId);
    }


    @Override
    public int getLoggedInUserId(){
        return appDatabase.profileDao().getProfile().getLoggedInUserId();
    }

    @Override
    public String getHostUrl(){
        return appDatabase.profileDao().getProfile().getHostUrl();
    }

    @Override
    public MutableLiveData<NetworkStatus> getMessageLiveData() {
        return messageLiveData;
    }



    // - - - - - clear data at log out - - - -
    @Override
    public void clearAppData(){
        appDatabase.issueDao().clearIssuesTable();
        appDatabase.labelDao().clearLabelsTable();
        appDatabase.milestoneDao().clearMilestonesTable();
        appDatabase.profileDao().clearProfileTable();
        appDatabase.projectDao().clearProjectsTable();
        appDatabase.userDao().clearUserTable();
    }

}
