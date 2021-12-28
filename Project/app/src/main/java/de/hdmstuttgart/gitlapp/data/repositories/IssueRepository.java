package de.hdmstuttgart.gitlapp.data.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;


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

    //holding response of api call or when this fails the "response" of the db
    private List<Issue> responseList = new ArrayList<>();

    //present data to the viewModel
    private MutableLiveData<List<Issue>> issuesLiveData = new MutableLiveData<>();

    public IssueRepository(AppDatabase appDatabase, GitLabClient gitLabClient){
        this.appDatabase = appDatabase;
        this.gitLabClient = gitLabClient;
    }

    /**
     * reloads the data by making api calls
     * if call fails, data from the local database will be loaded
     */
    public void refreshData(){
        //todo make background thread for this (threadpool)
        //todo implement a callback

        Call<List<Issue>> call = gitLabClient.getProjectIssues("glpat-im7xUxYLmQv1LnKnvesr"); //todo make access token not hardcoded
        call.enqueue(new Callback<List<Issue>>() {
            @Override
            public void onResponse(Call<List<Issue>> call, Response<List<Issue>> response) {
                responseList = response.body();
                Log.d("Api","IssueCall SUCCESS " + responseList.toString());
                ORM.mapAndInsertIssues(responseList,appDatabase);
            }

            @Override
            public void onFailure(Call<List<Issue>> call, Throwable t) {
                Log.d("Api","Oh no " + t.getMessage() + ", loading data form database");//todo make toast (make refresh data throw exception)
                responseList = ORM.mapIssueObjectsFromDb(6414,appDatabase);
                Log.d("Api","Loaded: " + responseList.toString());
            }
        });
        issuesLiveData.setValue(responseList);
    }

    public MutableLiveData<List<Issue>> getIssueLiveData(){
        return this.issuesLiveData;
    }

}
