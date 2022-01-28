package de.hdmstuttgart.gitlapp.data.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


import de.hdmstuttgart.gitlapp.data.database.AppDatabase;
import de.hdmstuttgart.gitlapp.data.network.GitLabClient;
import de.hdmstuttgart.gitlapp.models.Issue;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IssueRepository implements IIssueRepository{

    // - - - - - Sources - - - - - - - -
    private final AppDatabase appDatabase;
    private final GitLabClient gitLabClient;

    private String accessToken;

    //holding response of api call or when this fails the "response" of the db
    private List<Issue> responseList = new ArrayList<>();

    //present data to the viewModel
    private final MutableLiveData<List<Issue>> issuesLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> networkCallMessage = new MutableLiveData<>();
    private final MutableLiveData<Issue> singleIssueLiveData = new MutableLiveData<>();



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
     * loads data from local source and tries to update this data
     *
     * @param projectId the id of the project the issues should be loaded for
     */
    @Override
    public void initProjectIssues(int projectId) {
        responseList = ORM.completeIssueObjects(projectId, appDatabase);
        fetchProjectIssues(projectId,1);
        issuesLiveData.setValue(responseList);
        Log.d("Api", "Loaded local data " + responseList.toString());
    }

    /**
     * fetches all issues by a specific project
     */
    @Override
    public void fetchProjectIssues(int projectId, int page) {
        networkCallMessage.postValue("loading");
        //todo make background thread for this (thread pool)
        Call<List<Issue>> call = gitLabClient.getProjectIssues(projectId, accessToken, page);
        call.enqueue(new Callback<List<Issue>>() {
            @Override
            public void onResponse(Call<List<Issue>> call, Response<List<Issue>> response) {
                if (response.isSuccessful()) {
                    responseList = response.body();
                    Log.e("Api","onResponse " + responseList.toString());
                    ORM.mapAndInsertIssues(responseList, appDatabase);
                    responseList = ORM.completeIssueObjects(projectId,appDatabase);
                    Log.e("Api","issuese" + responseList.toString());
                    responseList.sort(Comparator.comparingInt(Issue::getIid).reversed());
                    issuesLiveData.postValue(responseList);

                    Log.d("Api", "IssueCall SUCCESS " + responseList.toString());
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
                Log.e("Api", "Oh no " + t.getMessage() + ", data not updated");
                networkCallMessage.setValue("Oh no, check your wifi connection");
            }
        });
    }

    /**
     * makes an api call to post a new issue with parameters set by the user
     */
    @Override
    public void postNewIssue(int projectId, String issueTitle, String issueDescription, String dueDate, int weight,  String labels, int milestoneId){
        Call<Void> call = gitLabClient.postNewIssue(projectId,accessToken,issueTitle,issueDescription,dueDate,weight,milestoneId,labels);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.d("Api","IssuePost SUCCESS");
                    networkCallMessage.setValue("Add issue successful");
                    fetchProjectIssues(projectId,1); //todo its not efficient to make two network calls!
                }else{
                    Log.e("Api","IssuePost FAIL, " + response.code());
                    networkCallMessage.setValue("Add issue failed, " + response.code()); //todo implement more meaningful message
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                networkCallMessage.setValue("Add issue failed, check wifi connection");
                Log.e("Api","IssuePost FAIL, " + t.getMessage());
            }
        });
    }

    /**
     * change the state of an issue from open to closed by making the corresponding api call
     * @param projectId the id of the project the issue belongs to
     * @param issueIid the iid of the issue which should be closed
     */
    @Override
    public void closeIssue(int projectId, int issueIid){
        Call<Void> call = gitLabClient.closeIssue(projectId,issueIid,accessToken);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Optional<Issue> updatedIssue = responseList.stream().filter(issue -> issue.getIid() == issueIid).findAny();
                    updatedIssue.ifPresent(issue -> issue.setState("closed"));
                    updatedIssue.ifPresent(singleIssueLiveData::setValue);
                    Log.d("Api","IssueClose SUCCESS");
                    networkCallMessage.setValue("Close issue successful");
                }else{
                    Log.e("Api","IssueClose FAIL, " + response.code());
                    networkCallMessage.setValue("Close issue failed, " + response.code());
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                networkCallMessage.setValue("Close issue failed, check wifi connection");
                Log.e("Api","IssueClose FAIL, " + t.getMessage());
            }
        });
    }

    @Override
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
    @Override
    public MutableLiveData<Issue> getSingleIssueLiveData(int issueId) throws Exception {
        Log.e("Api",responseList.toString());
        List<Issue> temp = responseList
                .stream()
                .filter(issue -> issue.getId() == issueId)
                .collect(Collectors.toList());

        if (temp.size() == 1) {
            singleIssueLiveData.setValue(temp.get(0));
            Log.d("Api","GetSingleIssueLiveData returns " + temp.get(0).toString());
            return singleIssueLiveData;
        }
        Log.e("Api","GetSingleIssueLiveData throws exception, list size: " + temp.size());
        throw new Exception("Issue with id " + issueId + " exists " + temp.size() + " times");
    }

    /**
     * for displaying a message whether data update via api call was successful or not
     *
     * @return LiveData object containing the message
     */
    @Override
    public MutableLiveData<String> getNetworkCallMessage() {
        return networkCallMessage;
    }
}
