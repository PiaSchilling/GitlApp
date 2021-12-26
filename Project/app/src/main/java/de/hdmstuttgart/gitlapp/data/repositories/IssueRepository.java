package de.hdmstuttgart.gitlapp.data.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;


import de.hdmstuttgart.gitlapp.data.database.AppDatabase;
import de.hdmstuttgart.gitlapp.data.network.ApiResponse;
import de.hdmstuttgart.gitlapp.data.network.DummyAPI;
import de.hdmstuttgart.gitlapp.data.network.IssueApi;
import de.hdmstuttgart.gitlapp.data.network.ServiceGenerator;
import de.hdmstuttgart.gitlapp.models.Issue;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IssueRepository {

    private AppDatabase database;
    private List<Issue> issues = new ArrayList<>();
    private List<Issue> issuesAPI = new ArrayList<>();
    private MutableLiveData<List<Issue>> issuesLiveData = new MutableLiveData<>();
    private DummyAPI dummyAPI = new DummyAPI();
    private ServiceGenerator serviceGenerator = new ServiceGenerator("https://gitlab.mi.hdm-stuttgart.de/api/v4/");
    private IssueApi issueApi = serviceGenerator.getIssueApi();

    public IssueRepository(AppDatabase database){
        this.database = database;
    }

    /**
     * refresh the data saved in the room db by making an api call and insert the result in the room db
     */
    public void refreshData(){ //todo cleanup
        //todo make background thread for this (threadpool)
        //todo implement a callback

        Call<List<Issue>> call = issueApi.getSearchResult("glpat-im7xUxYLmQv1LnKnvesr"); //todo make access token not hardcoded
        call.enqueue(new Callback<List<Issue>>() {
            @Override
            public void onResponse(Call<List<Issue>> call, Response<List<Issue>> response) {
                List<Issue> issues = response.body();
                Log.d("Api","Success" + issues.toString());

                issuesAPI.addAll(issues);
            }

            @Override
            public void onFailure(Call<List<Issue>> call, Throwable t) {
                Log.d("Api","FAIL");
            }
        });

        //issuesAPI = dummyAPI.getIssues();                                   //1. fetch data from network
        //database.issueDao().insertIssues(issuesAPI.toArray(new Issue[0]));  //2. save them into room
        database.issueDao().insertOrUpdate(issuesAPI);
        issues = database.issueDao().getAllIssues();                        //3. get all issues from room
        issuesLiveData.setValue(issues);                                    //3. set them to the live data object

    }

    public List<Issue> getIssues(){
        return this.issues;
    }
}
