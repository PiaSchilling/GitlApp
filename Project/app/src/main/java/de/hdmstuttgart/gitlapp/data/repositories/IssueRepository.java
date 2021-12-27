package de.hdmstuttgart.gitlapp.data.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;


import de.hdmstuttgart.gitlapp.data.database.AppDatabase;
import de.hdmstuttgart.gitlapp.data.network.GitLabClient;
import de.hdmstuttgart.gitlapp.models.Issue;
import de.hdmstuttgart.gitlapp.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IssueRepository {

    private final AppDatabase database;
    private final GitLabClient gitLabClient;

    private List<Issue> responseList = new ArrayList<>();
    private List<Issue> issues = new ArrayList<>();
    private List<Issue> issuesAPI = new ArrayList<>();
    private MutableLiveData<List<Issue>> issuesLiveData = new MutableLiveData<>();


    public IssueRepository(AppDatabase database, GitLabClient gitLabClient){
        this.database = database;
        this.gitLabClient = gitLabClient;
    }

    /**
     * refresh the data saved in the room db by making an api call and insert the result in the room db
     */
    public void refreshData(){ //todo cleanup
        //todo make background thread for this (threadpool)
        //todo implement a callback

        Call<List<Issue>> call = gitLabClient.getSearchResult("glpat-im7xUxYLmQv1LnKnvesr"); //todo make access token not hardcoded
        call.enqueue(new Callback<List<Issue>>() {
            @Override
            public void onResponse(Call<List<Issue>> call, Response<List<Issue>> response) {
                responseList = response.body();
                Log.d("Api","Success" + responseList.toString());

                issuesAPI.addAll(responseList);
            }

            @Override
            public void onFailure(Call<List<Issue>> call, Throwable t) {
                Log.d("Api","FAIL");
            }
        });

        //issuesAPI = dummyAPI.getIssues();                                   //1. fetch data from network
        //database.issueDao().insertIssues(issuesAPI.toArray(new Issue[0]));  //2. save them into room

        splitResult();
        updateDatabase();
        issues = database.issueDao().getAllIssues();//3. get all issues from room

        issuesLiveData.setValue(issues);                                    //3. set them to the live data object

    }

    public List<Issue> getIssues(){
        return this.issues;
    }

    private void updateDatabase(){
        database.issueDao().insertOrUpdate(issuesAPI);
    }

    private void splitResult(){
        List<User> authors = new ArrayList<>();

        for(Issue issue : responseList){
            User author = issue.getAuthor();
            Log.d("Api","LOOP" + author.toString());
            authors.add(author);
            issue.setAuthor_id(author.getId());
        }

        database.userDao().insertUsers(authors);

        Log.d("Api","AUTHORS " + authors.toString());
        Log.d("Api","DATABSE USER " + database.userDao().getAllUsers());

        //get list of all
        //get the users
        //save the users to room
        //save the user id to issue author_id
    }
}
