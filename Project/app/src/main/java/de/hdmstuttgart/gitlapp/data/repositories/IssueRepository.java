package de.hdmstuttgart.gitlapp.data.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import de.hdmstuttgart.gitlapp.data.database.AppDatabase;
import de.hdmstuttgart.gitlapp.data.network.GitLabClient;
import de.hdmstuttgart.gitlapp.models.Issue;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IssueRepository {

    // - - - - - Sources - - - - - - - -
    private final AppDatabase appDatabase;
    private final GitLabClient gitLabClient;

    private String accessToken;

    //holding response of api call or when this fails the "response" of the db
    private List<Issue> responseList = new ArrayList<>();

    //present data to the viewModel
    private MutableLiveData<List<Issue>> issuesLiveData = new MutableLiveData<>();
    private MutableLiveData<String> networkCallMessage = new MutableLiveData<>();


    public IssueRepository(AppDatabase appDatabase, GitLabClient gitLabClient) {
        this.appDatabase = appDatabase;
        this.gitLabClient = gitLabClient;

        this.accessToken = appDatabase.profileDao().getProfile().getAccessToken();
        if(accessToken == null || accessToken.isEmpty()){
            Log.e("Api","Access token is null or empty");
            this.accessToken = "token"; // to prevent the app from crashing (maybe find a better solution)
        }
    }

    /**
     * loads data from local source
     *
     * @param projectId the id of the project the issues should be loaded for
     */
    public void initProjectIssues(int projectId) {
        responseList = ORM.completeIssueObjects(projectId, appDatabase);
        issuesLiveData.setValue(responseList);
        Log.d("Api", "Loaded local data " + responseList.toString());
    }

    /**
     * reloads the data by making api calls
     * if call fails, data from the local database will be loaded
     */
    public void refreshProjectIssues(int projectId) {
        //todo make background thread for this (threadpool)
        //todo implement a callback
        Log.d("Api", "Called refresh data");

        Call<List<Issue>> call = gitLabClient.getProjectIssues(projectId, accessToken); //todo make access token not hardcoded
        call.enqueue(new Callback<List<Issue>>() {
            @Override
            public void onResponse(Call<List<Issue>> call, Response<List<Issue>> response) {
                if (response.isSuccessful()) {
                    responseList = response.body();
                    Log.d("Api", "IssueCall SUCCESS " + responseList.toString());
                    ORM.mapAndInsertIssues(responseList, appDatabase);
                    issuesLiveData.setValue(responseList);
                    networkCallMessage.postValue("Update successful");
                } else {
                    if(response.code() == 401){
                        networkCallMessage.setValue("Problem with authentication");
                    }else if(response.code() == 404){
                        networkCallMessage.setValue("Project with id " + projectId + " not found");
                    }else{
                        networkCallMessage.setValue("Error, code " + response.code());
                    }
                    Log.e("Api", "IssueCall FAIL, code " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Issue>> call, Throwable t) {
                Log.d("Api", "Oh no " + t.getMessage() + ", data not updated");//todo make toast
               // responseList = ORM.completeIssueObjects(projectId, appDatabase); //todo remove (is not necessary bc of initData method)
                //Log.d("Api", "Loaded: " + responseList.toString());
                networkCallMessage.setValue("Oh no, check your wifi connection");
                //issuesLiveData.postValue(responseList);
            }
        });
        //issuesLiveData.setValue(responseList);
    }

    public MutableLiveData<List<Issue>> getIssueListLiveData() {
        Log.d("Api", "Called get issueLiveData" + issuesLiveData.getValue());
        return this.issuesLiveData;
    }

    /**
     * get a single issue from the already fetched list of issues
     * @param issueId the id of the issue which should be returned
     * @return a live data object containing a single issue
     * @throws Exception if no issue with the issueId can be found
     */
    public MutableLiveData<Issue> getSingleIssueLiveData(int issueId) throws Exception {
        MutableLiveData<Issue> singleIssueLiveData = new MutableLiveData<>();
        List<Issue> temp = responseList
                .stream()
                .filter(issue -> issue.getId() == issueId)
                .collect(Collectors.toList());

        if (temp.size() == 1) {
            singleIssueLiveData.setValue(temp.get(0));
            Log.d("Api","GetSingleIssueLiveData returns " + temp.get(0).toString());
            return singleIssueLiveData;
        }
        Log.d("Api","GetSingleIssueLiveData throws exception, list size: " + temp.size());
        throw new Exception("Issue with id " + issueId + " exists " + temp.size() + " times");
    }

    /**
     * for displaying a message whether data update via api call was successful or not
     *
     * @return LiveData object containing the message
     */
    public MutableLiveData<String> getNetworkCallMessage() {
        return networkCallMessage;
    }
}
