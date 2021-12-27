package de.hdmstuttgart.gitlapp;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hdmstuttgart.gitlapp.data.database.AppDatabase;
import de.hdmstuttgart.gitlapp.data.network.GitLabClient;
import de.hdmstuttgart.gitlapp.data.network.ServiceGenerator;
import de.hdmstuttgart.gitlapp.data.repositories.IssueRepository;

//Container of objects shared across the whole app (dependency injection)
public class AppContainer {

    private final Context applicationContext;

    // - - - - - -  Room local database - - - - - - - - -
    public AppDatabase appDatabase;

    // - - - - - - Retrofit api - - - - - - - - - - - - -
    private final String baseUrl = "https://gitlab.mi.hdm-stuttgart.de/api/v4/"; //todo make not hardcoded
    private final String accessToken = "-"; //todo find a place to store the token

    private final ServiceGenerator serviceGenerator = new ServiceGenerator(baseUrl);

    public final GitLabClient gitLabClient = serviceGenerator.getGitLabClient();

    // - - - - - - Repositories - - - - - - - - - - - - -
    public IssueRepository issueRepository;

    // - - - - - - Background threading - - - - - - - - -
    public ExecutorService executorService = Executors.newFixedThreadPool(2);

    public AppContainer(Context context){
        this.applicationContext = context;

        appDatabase = AppDatabase.getDatabaseInstance(applicationContext); //needs the context this is why its instanced in the constructor
        issueRepository = new IssueRepository(appDatabase, gitLabClient);
    }








}
