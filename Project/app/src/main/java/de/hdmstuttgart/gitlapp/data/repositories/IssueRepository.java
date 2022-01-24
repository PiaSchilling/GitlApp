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

public class IssueRepository {

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
    public void initProjectIssues(int projectId) {
        responseList = ORM.completeIssueObjects(projectId, appDatabase);
        fetchProjectIssues(projectId,1);
        issuesLiveData.setValue(responseList);
        Log.d("Api", "Loaded local data " + responseList.toString());
    }

    /**
     * fetches all issues by a specific project
     */
    public void fetchProjectIssues(int projectId, int page) {
        networkCallMessage.postValue("loading");
        //todo make background thread for this (thread pool)
        Call<List<Issue>> call = gitLabClient.getProjectIssues(projectId, accessToken, page);
        call.enqueue(new Callback<List<Issue>>() {
            @Override
            public void onResponse(Call<List<Issue>> call, Response<List<Issue>> response) {
                if (response.isSuccessful()) {
                    responseList = response.body();

                    ORM.mapAndInsertIssues(responseList, appDatabase);
                    List<Issue> issues = ORM.completeIssueObjects(projectId,appDatabase);
                    issues.sort(Comparator.comparingInt(Issue::getIid).reversed());
                    issuesLiveData.postValue(issues);

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
    public void closeIssue(int projectId, int issueIid){
        Call<Void> call = gitLabClient.closeIssue(projectId,issueIid,accessToken);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    getSingleProjectIssue(projectId,issueIid); //if close was successful make call to update only the updated issue
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

    /**
     * fetch a single project issue by its iid
     * @param projectId the project the issue belongs to
     * @param issueIid the issueIid of the issue which should be fetched
     */
    public void getSingleProjectIssue(int projectId, int issueIid){ //todo not working needs to be fixed (label bug)
        Call<Issue> call = gitLabClient.getProjectIssueByIid(projectId,issueIid,accessToken);
        call.enqueue(new Callback<Issue>() {
            @Override
            public void onResponse(Call<Issue> call, Response<Issue> response) {
                if(response.isSuccessful()){
                    Issue responseIssue = response.body();
                    appDatabase.issueDao().updateIssues(responseIssue);

                    //update list with new issue
                    singleIssueLiveData.setValue(responseIssue);
                    Optional<Issue> temp = issuesLiveData.getValue()
                            .stream()
                            .filter(issue -> issue.getIid() == issueIid).findAny();
                    if(temp.isPresent()){
                        int index = issuesLiveData.getValue().indexOf(temp.get());
                        issuesLiveData.getValue().set(index,responseIssue);
                    }
                    Log.d("Api","SingleIssueCall SUCCESS, updated issue #" + Objects.requireNonNull(responseIssue).getIid());
                }else{
                    Log.e("Api", "SingleIssueCall FAIL, " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Issue> call, Throwable t) {
                Log.e("Api","SingleIssueCall FAIL, " + t.getMessage());
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
    public MutableLiveData<Issue> setSingleIssueLiveData(int issueId) throws Exception {
       // MutableLiveData<Issue> singleIssueLiveData = new MutableLiveData<>();
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

    public MutableLiveData<Issue> getSingleIssueLiveData(){
        return singleIssueLiveData;
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
