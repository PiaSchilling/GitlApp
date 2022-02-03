package de.hdmstuttgart.gitlapp.data.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdmstuttgart.gitlapp.data.database.AppDatabase;
import de.hdmstuttgart.gitlapp.data.network.GitLabClient;
import de.hdmstuttgart.gitlapp.data.network.NetworkStatus;
import de.hdmstuttgart.gitlapp.models.Label;
import de.hdmstuttgart.gitlapp.models.Milestone;
import de.hdmstuttgart.gitlapp.models.Project;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectRepository implements IProjectRepository {

    // - - - - -  Sources - - - - - -
    private final AppDatabase appDatabase;
    private final GitLabClient gitLabClient;

    private String accessToken;

    // holding response of api call or when this fails the "response" of the db
    private List<Project> responseList = new ArrayList<>();

    // present data to the view model
    private final MutableLiveData<List<Project>> projectsLiveData = new MutableLiveData<>();
    private final MutableLiveData<NetworkStatus> networkCallMessage = new MutableLiveData<>();
    private final MutableLiveData<List<Label>> labelsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Milestone>> milestoneLiveData = new MutableLiveData<>();


    public ProjectRepository(AppDatabase appDatabase, GitLabClient gitLabClient) {
        this.appDatabase = appDatabase;
        this.gitLabClient = gitLabClient;

        try {
            this.accessToken = appDatabase.profileDao().getProfile().getAccessToken();
        } catch (NullPointerException e) {
            Log.e("Api", "Access token is null or empty");
        }
    }

    // - - - - projects overview - - - - -

    /**
     * loads the locally stored project (local cache) and tries to update
     * should be called on app start, in case there is no network connection data is still displayed
     */
    @Override
    public void initProjects() {
        responseList = appDatabase.projectDao().getAllProjects();
        fetchProjects();
        projectsLiveData.setValue(responseList);
        Log.d("Api", "Loaded local data " + responseList.toString());
    }


    /**
     * reloads the data by making api calls
     * if call fails, data from the local database will be loaded
     */
    @Override
    public void fetchProjects() {
        Call<List<Project>> call = gitLabClient.getMemberProjects(accessToken);
        call.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(@NonNull Call<List<Project>> call, @NonNull Response<List<Project>> response) {
                if (response.isSuccessful()) {
                    responseList = response.body();
                    appDatabase.projectDao().insertProjects(responseList);
                    projectsLiveData.setValue(responseList);
                    networkCallMessage.setValue(NetworkStatus.SUCCESS);
                    Log.d("Api", "ProjectCall SUCCESS " + responseList.toString());
                } else {
                    Log.d("Api", "ProjectCall FAIL, code " + response.code());
                    if (response.code() == 401) {
                        networkCallMessage.setValue(NetworkStatus.AUTHENTICATION_ERROR);
                    } else if (response.code() == 404) {
                        networkCallMessage.setValue(NetworkStatus.PROJECT_NOT_FOUND);
                    } else {
                        Log.e("Api","FetchProjects FAIL, " + response.code());
                        NetworkStatus.FAIL.setMessage("Code " + response.code());
                        networkCallMessage.setValue(NetworkStatus.FAIL);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Project>> call, @NonNull Throwable t) {
                Log.e("Api", "Oh no " + t.getMessage() + ", loading data form database");
                networkCallMessage.setValue(NetworkStatus.NETWORK_ERROR);
            }
        });
    }

    @Override
    public MutableLiveData<List<Project>> getProjectsLiveData() {
        return this.projectsLiveData;
    }

    @Override
    public MutableLiveData<NetworkStatus> getNetworkCallMessage() {
        return networkCallMessage;
    }


    // - - - - - single project details - - - - - -

    /**
     * make an api call to get all labels by a specific project
     *
     * @param projectId id of the project for which the data should be fetched for
     */
    @Override
    public void fetchProjectLabels(int projectId) {
        Call<List<Label>> call = gitLabClient.getProjectLabels(projectId, accessToken);
        call.enqueue(new Callback<List<Label>>() {
            @Override
            public void onResponse(@NonNull Call<List<Label>> call, @NonNull Response<List<Label>> response) {
                if (response.isSuccessful()) {
                    List<Label> temp = response.body();
                    if (!Objects.equals(temp, labelsLiveData.getValue())) {
                        labelsLiveData.setValue(temp);
                        appDatabase.labelDao().insertLabels(temp);
                        appDatabase.labelDao().setProjectIdForIssue(projectId);
                    }
                } else {
                    Log.e("Api", "fetchProjectLabels call NOT SUCCESSFUL, code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Label>> call, @NonNull Throwable t) {
                Log.e("Api", "FAIL, code: " + t.getMessage());
            }
        });
    }

    /**
     * make an api call to get all milestones by a specific project
     *
     * @param projectId id of the project for which the data should be fetched for
     */
    @Override
    public void fetchProjectMilestones(int projectId) {
        Call<List<Milestone>> call = gitLabClient.getProjectMilestones(projectId, accessToken);
        call.enqueue(new Callback<List<Milestone>>() {
            @Override
            public void onResponse(@NonNull Call<List<Milestone>> call, @NonNull Response<List<Milestone>> response) {
                if (response.isSuccessful()) {
                    List<Milestone> temp = response.body();
                    if (!Objects.equals(temp, milestoneLiveData.getValue())) { //update live data only if something changed
                        milestoneLiveData.setValue(temp);
                        appDatabase.milestoneDao().insertMilestones(temp);
                    }
                } else {
                    Log.e("Api", "fetchProjectMilestones call NOT SUCCESSFUL, code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Milestone>> call, @NonNull Throwable t) {
                Log.e("Api", "fetchProjectMilestones call FAIL, code: " + t.getMessage());
            }
        });
    }

    @Override
    public MutableLiveData<List<Label>> getLabelsLiveData(int projectId) {
        fetchProjectLabels(projectId);
        return labelsLiveData;
    }

    @Override
    public MutableLiveData<List<Milestone>> getMilestoneLiveData(int projectId) {
        fetchProjectMilestones(projectId);
        return milestoneLiveData;
    }
}
