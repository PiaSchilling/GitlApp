package de.hdmstuttgart.gitlapp.data.repositories;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;


import de.hdmstuttgart.gitlapp.data.database.AppDatabase;
import de.hdmstuttgart.gitlapp.data.network.DummyAPI;
import de.hdmstuttgart.gitlapp.models.Issue;

public class IssueRepository {

    private AppDatabase database;
    private List<Issue> issues = new ArrayList<>();
    private List<Issue> issuesAPI = new ArrayList<>();
    private MutableLiveData<List<Issue>> issuesLiveData = new MutableLiveData<>();
    private DummyAPI dummyAPI = new DummyAPI();

    public IssueRepository(AppDatabase database){
        this.database = database;
    }

    /**
     * refresh the data saved in the room db by making an api call and insert the result in the room db
     */
    public void refreshData(){
        //todo make background thread for this (threadpool)
        //todo implement a callback


        issuesAPI = dummyAPI.getIssues();                                   //1. fetch data from network
        //database.issueDao().insertIssues(issuesAPI.toArray(new Issue[0]));  //2. save them into room
        database.issueDao().insertOrUpdate(issuesAPI);
        issues = database.issueDao().getAllIssues();                        //3. get all issues from room
        issuesLiveData.setValue(issues);                                    //3. set them to the live data object

    }

    public List<Issue> getIssues(){
        return this.issues;
    }
}
