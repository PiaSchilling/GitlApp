package de.hdmstuttgart.gitlapp.data.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import de.hdmstuttgart.gitlapp.data.database.AppDatabase;
import de.hdmstuttgart.gitlapp.data.network.GitLabClient;
import de.hdmstuttgart.gitlapp.models.Project;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectRepository {

    // - - - - -  Sources - - - - - -
    private AppDatabase appDatabase;
    private GitLabClient gitLabClient;

    //holding response of api call or when this fails the "response" of the db
    private List<Project> responseList = new ArrayList<>();
    //present data to the view model
    private MutableLiveData<List<Project>> projectsLiveData = new MutableLiveData<>();

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
                if(response.isSuccessful()){
                    responseList = response.body();
                    Log.d("Api","ProjectCall SUCCESS " + responseList.toString());
                    appDatabase.projectDao().insertProjects(responseList);
                }else{
                    Log.d("Api", "ProjectCall FAIL, code " + response.code());
                }

            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                Log.d("Api","Oh no " + t.getMessage() + ", loading data form database");//todo make toast (make refresh data throw exception)
                responseList = appDatabase.projectDao().getAllProjects();
            }
        });
        projectsLiveData.setValue(responseList);
    }

    public MutableLiveData<List<Project>> getProjectLiveData(){
        return this.projectsLiveData;
    }
}
