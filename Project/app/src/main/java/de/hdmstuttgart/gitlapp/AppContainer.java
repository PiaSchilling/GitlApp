package de.hdmstuttgart.gitlapp;

import android.content.Context;

import java.util.Objects;
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
    private String baseUrl = "https://gitlab.mi.hdm-stuttgart.de/api/v4/"; //todo make not hardcoded
    ServiceGenerator serviceGenerator = new ServiceGenerator(this.baseUrl); //can only be instanced after the base url is set
    public GitLabClient gitLabClient = serviceGenerator.getGitLabClient();



    // - - - - - - Repositories - - - - - - - - - - - - -
    public IssueRepository issueRepository;
    public ProjectRepository projectRepository;
    public ProfileRepository profileRepository;

    // - - - - - - Background threading - - - - - - - - -
    public ExecutorService executorService = Executors.newFixedThreadPool(2);

    public AppContainer(Context context) {
        this.applicationContext = context;

        appDatabase = AppDatabase.getDatabaseInstance(applicationContext); //needs the context this is why its instanced in the constructor
        issueRepository = new IssueRepository(Objects.requireNonNull(appDatabase),Objects.requireNonNull(gitLabClient));
        projectRepository = new ProjectRepository(Objects.requireNonNull(appDatabase),Objects.requireNonNull(gitLabClient));
        profileRepository = new ProfileRepository(Objects.requireNonNull(appDatabase), Objects.requireNonNull(gitLabClient));
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl + "/api/v4/";

    }

}
