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


    public ProjectRepository(AppDatabase appDatabase, GitLabClient gitLabClient) {
        this.appDatabase = appDatabase;
        this.gitLabClient = gitLabClient;

        try {
            this.accessToken = appDatabase.profileDao().getProfile().getAccessToken();
        } catch (NullPointerException e) {
            Log.e("Api", "Access token is null or empty");
        }
    }


    /**
     * loads the locally stored project (local cache)
     * should be called on app start, in case there is no network connection data is still displayed
     */
    public void initProjects() {
        responseList = appDatabase.projectDao().getAllProjects();
        projectsLiveData.setValue(responseList);
        Log.d("Api", "Loaded local data " + responseList.toString());
    }


    /**
     * reloads the data by making api calls
     * if call fails, data from the local database will be loaded
     */
    public void refreshProjects() {

        Call<List<Project>> call = gitLabClient.getMemberProjects(accessToken);
        call.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                if (response.isSuccessful()) {
                    responseList = response.body();
                    appDatabase.projectDao().insertProjects(responseList);
                    projectsLiveData.setValue(responseList);
                    networkCallMessage.setValue("Update successful");
                    Log.d("Api", "ProjectCall SUCCESS " + responseList.toString());
                } else {
                    Log.d("Api", "ProjectCall FAIL, code " + response.code());
                    if (response.code() == 401) {
                        networkCallMessage.setValue("Problem with authentication");
                    } else if (response.code() == 404) {
                        networkCallMessage.setValue("Projects not found");
                    } else {
                        networkCallMessage.setValue("Error, code " + response.code());
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                Log.d("Api", "Oh no " + t.getMessage() + ", loading data form database");
                responseList = appDatabase.projectDao().getAllProjects();
            }
        });
    }


    /**
     * make an api call to get all labels by a specific project
     *
     * @param projectId id of the project for which the data should be fetched for
     */
    public void fetchProjectLabels(int projectId) {
        Call<List<Label>> call = gitLabClient.getProjectLabels(projectId, accessToken);
        call.enqueue(new Callback<List<Label>>() {
            @Override
            public void onResponse(Call<List<Label>> call, Response<List<Label>> response) {

                if (response.isSuccessful()) {
                    List<Label> temp = response.body();
                    appDatabase.labelDao().insertLabels(temp);
                    appDatabase.labelDao().setProjectIdForIssue(projectId);
                } else {
                    Log.e("Api", "fetchProjectLabels call NOT SUCCESSFUL, code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Label>> call, Throwable t) {
                Log.e("Api", "FAIL, code: " + t.getMessage());
            }
        });
    }

    /**
     * make an api call to get all milestones by a specific project
     *
     * @param projectId id of the project for which the data should be fetched for
     */
    public void fetchProjectMilestones(int projectId) {
        Call<List<Milestone>> call = gitLabClient.getProjectMilestones(projectId, accessToken);
        call.enqueue(new Callback<List<Milestone>>() {
            @Override
            public void onResponse(Call<List<Milestone>> call, Response<List<Milestone>> response) {
                if (response.isSuccessful()) {
                    List<Milestone> temp = response.body();
                    Log.e("Milestone", temp.toString());
                    appDatabase.milestoneDao().insertMilestones(temp);
                } else {
                    Log.e("Api", "fetchProjectMilestones call NOT SUCCESSFUL, code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Milestone>> call, Throwable t) {
                Log.e("Api", "fetchProjectMilestones call FAIL, code: " + t.getMessage());

            }
        });
    }

    /**
     * get all labels defined in a project
     *
     * @param projectId the id of the project the labels should be returned for
     * @return a list of labels
     */
    public List<Label> getProjectLabels(int projectId) {
        fetchProjectLabels(projectId);
        return appDatabase.labelDao().getProjectLabels(projectId); //todo implement api call, only already by issue used labels are already in the db
    }

    /**
     * get all milestones defined in a project
     *
     * @param projectId the id of the project the milestones should be returned for
     * @return a list of milestones
     */
    public List<Milestone> getProjectMilestones(int projectId) {
        fetchProjectMilestones(projectId);
        return appDatabase.milestoneDao().getProjectMilestones(projectId); //todo implement api call, only already by issue used milestones are already in the db
    }

    public MutableLiveData<List<Project>> getProjectLiveData() {
        return this.projectsLiveData;
    }

    public MutableLiveData<String> getNetworkCallMessage() {
        return networkCallMessage;
    }

}
