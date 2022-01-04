package de.hdmstuttgart.gitlapp;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hdmstuttgart.gitlapp.data.database.AppDatabase;
import de.hdmstuttgart.gitlapp.data.network.GitLabClient;
import de.hdmstuttgart.gitlapp.data.network.ServiceGenerator;
import de.hdmstuttgart.gitlapp.data.repositories.IssueRepository;
import de.hdmstuttgart.gitlapp.data.repositories.ProfileRepository;
import de.hdmstuttgart.gitlapp.data.repositories.ProjectRepository;
import de.hdmstuttgart.gitlapp.viewmodels.IssueDetailViewModelFactory;

//Container of objects shared across the whole app (dependency injection)
public class AppContainer {

    private final Context applicationContext;

    // - - - - - -  Room local database - - - - - - - - -
    public AppDatabase appDatabase;

    // - - - - - - Retrofit api - - - - - - - - - - - - -
    private String baseUrl = ""; //todo make not hardcoded

    public GitLabClient gitLabClient;

    // - - - - - - Repositories - - - - - - - - - - - - -
    public IssueRepository issueRepository;
    public ProjectRepository projectRepository;
    public ProfileRepository profileRepository;

    // - - - - - - Background threading - - - - - - - - -
    public ExecutorService executorService = Executors.newFixedThreadPool(2);

    public AppContainer(Context context) {
        this.applicationContext = context;

        appDatabase = AppDatabase.getDatabaseInstance(applicationContext); //needs the context this is why its instanced in the constructor
        issueRepository = new IssueRepository(appDatabase, gitLabClient);
        projectRepository = new ProjectRepository(appDatabase, gitLabClient);
        profileRepository = new ProfileRepository(appDatabase, gitLabClient);
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl + "/api/v4/";

        ServiceGenerator serviceGenerator = new ServiceGenerator(this.baseUrl); //can only be instanced after the base url is set
        gitLabClient = serviceGenerator.getGitLabClient();
    }

}
