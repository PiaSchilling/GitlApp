package de.hdmstuttgart.gitlapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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

    // - - - - - -  Room local database - - - - - - - - -
    public AppDatabase appDatabase;

    // - - - - - - Retrofit api - - - - - - - - - - - - -
    private final String baseUrl;
    public GitLabClient gitLabClient;

    // - - - - - - Repositories - - - - - - - - - - - - -
    public IssueRepository issueRepository;
    public ProjectRepository projectRepository;
    public ProfileRepository profileRepository;


    // - - - - - - view model provider factories - - - - -
    public IssueDetailViewModelFactory issueDetailViewModelFactory;


    // - - - - - - Background threading - - - - - - - - -
    public ExecutorService executorService = Executors.newFixedThreadPool(2);


    public AppContainer(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("profileInformation", Context.MODE_PRIVATE);
        baseUrl = sharedPref.getString("baseUrl", "default");

        if (!baseUrl.equals("default")) {
            //can only be instanced after the base url is set
            ServiceGenerator serviceGenerator = new ServiceGenerator(Objects.requireNonNull(this.baseUrl)); //can only be instanced after the base url is set
            gitLabClient = serviceGenerator.getGitLabClient();

            appDatabase = AppDatabase.getDatabaseInstance(context); //needs the context this is why its instanced in the constructor

            issueRepository = new IssueRepository(Objects.requireNonNull(appDatabase), Objects.requireNonNull(gitLabClient));
            projectRepository = new ProjectRepository(Objects.requireNonNull(appDatabase), Objects.requireNonNull(gitLabClient));
            profileRepository = new ProfileRepository(Objects.requireNonNull(appDatabase), Objects.requireNonNull(gitLabClient));

            issueDetailViewModelFactory = new IssueDetailViewModelFactory(issueRepository);

        } else {
            Log.e("Api", "Can not find base url in shard preferences"); //can never happen
        }

    }
}
