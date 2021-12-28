package de.hdmstuttgart.gitlapp.data.repositories;

import android.accounts.NetworkErrorException;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import de.hdmstuttgart.gitlapp.data.database.AppDatabase;
import de.hdmstuttgart.gitlapp.data.network.GitLabClient;
import de.hdmstuttgart.gitlapp.models.Project;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectRepository {

    private AppDatabase appDatabase;
    private GitLabClient gitLabClient;

    private List<Project> responseList = new ArrayList<>();
    private MutableLiveData<List<Project>> liveData = new MutableLiveData<>();

    public ProjectRepository(AppDatabase appDatabase, GitLabClient gitLabClient){
        this.appDatabase = appDatabase;
        this.gitLabClient = gitLabClient;
    }

    /**
     * reloads the data by making api calls
     * if call fails, data from the local database will be loaded
     */
    public void refreshData(){

        Call<List<Project>> call = gitLabClient.getMemberProjects("Bearer glpat-im7xUxYLmQv1LnKnvesr");
        call.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                responseList = response.body();
                Log.d("Api","ProjectCall SUCCESS " + responseList.toString());
                appDatabase.projectDao().insertProjects(responseList);
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                if(t instanceof UnknownHostException){
                    Log.d("Api","Check wifi connection");
                }
                Log.d("Api","Oh no " + t.getMessage() + ", loading data form database");//todo make toast
                responseList = appDatabase.projectDao().getAllProjects();
            }
        });
        liveData.setValue(responseList);
    }

    public List<Project> getResponseList(){
        return this.responseList;
    }

    public MutableLiveData<List<Project>> getProjectLiveData(){
        return this.liveData;
    }
}
