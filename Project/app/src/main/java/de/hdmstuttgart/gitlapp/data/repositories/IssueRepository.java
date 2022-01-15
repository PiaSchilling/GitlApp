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

        try{
            this.accessToken = appDatabase.profileDao().getProfile().getAccessToken();
        }catch (NullPointerException e){
            Log.e("Api","Access token is null or empty");
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

        Call<List<Issue>> call = gitLabClient.getProjectIssues(projectId, accessToken);
        call.enqueue(new Callback<List<Issue>>() {
            @Override
            public void onResponse(Call<List<Issue>> call, Response<List<Issue>> response) {
                if (response.isSuccessful()) {
                    responseList = response.body();
                    Log.d("Api", "IssueCall SUCCESS " + responseList.toString());
                    ORM.mapAndInsertIssues(responseList, appDatabase);
                    issuesLiveData.postValue(responseList);
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
                Log.d("Api", "Oh no " + t.getMessage() + ", data not updated");
                Log.d("Api",t.toString());
                t.printStackTrace();
                networkCallMessage.setValue("Oh no, check your wifi connection");
            }
        });
    }

    public void postNewIssue(int projectId, String issueTitle, String issueDescription, String dueDate, int weight,  String labels, int milestoneId){
        Call<Void> call = gitLabClient.postNewIssue(projectId,accessToken,issueTitle,issueDescription,dueDate,weight,milestoneId,labels);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.d("Api","IssuePost SUCCESS");
                    networkCallMessage.setValue("Add issue successful");
                    refreshProjectIssues(projectId); //todo its not efficient to make two network calls!
                }else{
                    Log.e("Api","IssuePost FAIL, " + response.code());
                    networkCallMessage.setValue("Add issue failed, " + response.code()); //todo implement more meaningful message
                    Log.e("Api", String.valueOf(response.errorBody()));
                    Log.e("Api",response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                networkCallMessage.setValue("Add issue failed, check wifi connection");
                Log.e("Api","IssuePost FAIL, " + t.getMessage());
            }
        });
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
