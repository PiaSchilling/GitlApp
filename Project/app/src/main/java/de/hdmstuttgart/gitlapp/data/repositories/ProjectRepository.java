package de.hdmstuttgart.gitlapp.data.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import de.hdmstuttgart.gitlapp.data.database.AppDatabase;
import de.hdmstuttgart.gitlapp.data.network.GitLabClient;
import de.hdmstuttgart.gitlapp.models.Label;
import de.hdmstuttgart.gitlapp.models.Milestone;
import de.hdmstuttgart.gitlapp.models.Project;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectRepository {

    // - - - - -  Sources - - - - - -
    private AppDatabase appDatabase;
    private GitLabClient gitLabClient;
    private String accessToken;

    //holding response of api call or when this fails the "response" of the db
    private List<Project> responseList = new ArrayList<>();
    //present data to the view model
    private MutableLiveData<List<Project>> projectsLiveData = new MutableLiveData<>();
    private MutableLiveData<String> networkCallMessage = new MutableLiveData<>();


    public ProjectRepository(AppDatabase appDatabase, GitLabClient gitLabClient){
        this.appDatabase = appDatabase;
        this.gitLabClient = gitLabClient;


       try{
            this.accessToken = appDatabase.profileDao().getProfile().getAccessToken();
        }catch (NullPointerException e){
            Log.e("Api","Access token is null or empty");
        }
    }

        // Zweck dieser Methode?
        // init --> initiieren --> anstoß geben

    public void initProjects() {
        Log.d("ST-","Loaded local data 1 " + responseList.toString());
        responseList = appDatabase.projectDao().getAllProjects();
        projectsLiveData.setValue(responseList);
        Log.d("Api", "Loaded local data " + responseList.toString());
        Log.d("ST-","Loaded local data 2 " + responseList.toString());
    }


    /**
     * reloads the data by making api calls
     * if call fails, data from the local database will be loaded
     */
    public void refreshProjects(){

        Call<List<Project>> call = gitLabClient.getMemberProjects(accessToken);
        call.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                if(response.isSuccessful()){
                    responseList = response.body();
                    Log.d("Api","ProjectCall SUCCESS " + responseList.toString());

                    // response of api call is now in local database and in Live Data
                    appDatabase.projectDao().insertProjects(responseList);
                    Log.e("ST-", " appDatabase in onResponse " + appDatabase.projectDao().getAllProjects().toString());

                    projectsLiveData.setValue(responseList);
                    Log.e("ST-", "projectsLiveData in onResponse " + projectsLiveData.getValue().toString());
                    Log.e("ST-", "responseList in onResponse " + responseList.toString());

                    networkCallMessage.setValue("Update successful");
                }else{
                    Log.d("Api", "ProjectCall FAIL, code " + response.code());
                    if(response.code() == 401){
                        networkCallMessage.setValue("Problem with authentication");
                    }else if(response.code() == 404){
                        networkCallMessage.setValue("Projects not found");
                    }else{
                        networkCallMessage.setValue("Error, code " + response.code());
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                Log.d("Api","Oh no " + t.getMessage() + ", loading data form database");
                responseList = appDatabase.projectDao().getAllProjects();
            }
        });
    }

    public void fetchProjectLabels(int projectId){
        Call<List<Label>> call = gitLabClient.getProjectLabels(projectId,accessToken);
        call.enqueue(new Callback<List<Label>>() {
            @Override
            public void onResponse(Call<List<Label>> call, Response<List<Label>> response) {

                if(response.isSuccessful()){
                    List<Label> temp = response.body();
                    appDatabase.labelDao().insertLabels(temp);
                    appDatabase.labelDao().setProjectIdForIssue(projectId);
                }else{
                    Log.e("Api","FAIL, code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Label>> call, Throwable t) {
                Log.e("Api","FAIL, code: " + t.getMessage());
            }
        });
    }

    public List<Label> getProjectLabels(int projectId){
        fetchProjectLabels(projectId);
        return appDatabase.labelDao().getProjectLabels(projectId); //todo implement api call, only already by issue used labels are already in the db
    }

    public List<Milestone> getProjectMilestones(int projectId){
        return appDatabase.milestoneDao().getProjectMilestones(projectId); //todo implement api call, only already by issue used milestones are already in the db
    }

    public MutableLiveData<List<Project>> getProjectLiveData(){
        return this.projectsLiveData;
    }

    public MutableLiveData<String> getNetworkCallMessage() {
        return networkCallMessage;
    }

}
