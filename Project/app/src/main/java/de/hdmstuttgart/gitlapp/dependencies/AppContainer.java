package de.hdmstuttgart.gitlapp.dependencies;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Objects;

import de.hdmstuttgart.gitlapp.data.database.AppDatabase;
import de.hdmstuttgart.gitlapp.data.network.GitLabClient;
import de.hdmstuttgart.gitlapp.data.network.ServiceGenerator;
import de.hdmstuttgart.gitlapp.data.repositories.IIssueRepository;
import de.hdmstuttgart.gitlapp.data.repositories.IProfileRepository;
import de.hdmstuttgart.gitlapp.data.repositories.IProjectRepository;
import de.hdmstuttgart.gitlapp.data.repositories.IssueRepository;
import de.hdmstuttgart.gitlapp.data.repositories.ProfileRepository;
import de.hdmstuttgart.gitlapp.data.repositories.ProjectRepository;
import de.hdmstuttgart.gitlapp.viewmodels.vmpFactories.ViewModelFactory;

// Container of objects shared across the whole app (dependency injection)
public class AppContainer {

    // - - - - - -  Room local database - - - - - - - - -
    public AppDatabase appDatabase;

    public GitLabClient gitLabClient;

    // - - - - - - Repositories - - - - - - - - - - - - -
    public IIssueRepository issueRepository;
    public IProjectRepository projectRepository;
    public IProfileRepository profileRepository;


    // - - - - - - view model provider factories - - - - -
    public ViewModelFactory viewModelFactory;



    public AppContainer(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("profileInformation", Context.MODE_PRIVATE);
        // - - - - - - Retrofit api - - - - - - - - - - - - -
        String baseUrl = sharedPref.getString("baseUrl", "default");

        if (!baseUrl.equals("default")) {
            //can only be instanced after the base url is set
            ServiceGenerator serviceGenerator = new ServiceGenerator(Objects.requireNonNull(baseUrl)); //can only be instanced after the base url is set
            gitLabClient = serviceGenerator.getGitLabClient();

            appDatabase = AppDatabase.getDatabaseInstance(context); //needs the context this is why its instanced in the constructor

            issueRepository = new IssueRepository(Objects.requireNonNull(appDatabase), Objects.requireNonNull(gitLabClient));
            projectRepository = new ProjectRepository(Objects.requireNonNull(appDatabase), Objects.requireNonNull(gitLabClient));
            profileRepository = new ProfileRepository(Objects.requireNonNull(appDatabase), Objects.requireNonNull(gitLabClient));

            viewModelFactory = new ViewModelFactory(issueRepository, projectRepository, profileRepository);

        } else {
            Log.e("Api", "Can not find base url in shared preferences"); //can never happen
        }

    }
}
