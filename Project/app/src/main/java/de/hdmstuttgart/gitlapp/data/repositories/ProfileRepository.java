package de.hdmstuttgart.gitlapp.data.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
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

    LiveData<Profile> profileLiveData = new MutableLiveData<>();
    MutableLiveData<String> messageLiveData = new MutableLiveData<>();

    // - - - - - Profile attributes - - - -
    User loggedIdUser;
    int userId;
    String accessToken;
    String hostUrl;


    public ProfileRepository(AppDatabase appDatabase, GitLabClient gitLabClient) {
        this.appDatabase = appDatabase;
        this.gitLabClient = gitLabClient;
    }

    private void initProfile(){
        Profile profile = new Profile(loggedIdUser.getId(), accessToken, hostUrl);
        Log.d("ProfileRepo","Profile created " + profile.toString());
        appDatabase.profileDao().insertProfile(profile);
    }

    private void fetchUser(int userId){

        Log.d("ProfileRepo","FetchUser called");

        Call<User> call = gitLabClient.getSingleUser(userId, this.accessToken);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        loggedIdUser = response.body();
                        loggedIdUser.setLoggedInUser(true);
                        appDatabase.userDao().insertUsers(loggedIdUser);
                        initProfile();

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

        fetchUser(userId);
    }

    public MutableLiveData<String> getMessageLiveData() {
        return messageLiveData;
    }
}
