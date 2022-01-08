package de.hdmstuttgart.gitlapp.dependencies;

import android.content.Context;

import java.util.Objects;

import de.hdmstuttgart.gitlapp.data.database.AppDatabase;
import de.hdmstuttgart.gitlapp.data.network.GitLabClient;
import de.hdmstuttgart.gitlapp.data.network.ServiceGenerator;
import de.hdmstuttgart.gitlapp.data.repositories.ProfileRepository;
import de.hdmstuttgart.gitlapp.viewmodels.vmpFactories.LoginViewModelFactory;

public class LoginContainer {

    // - - - - - -  Room local database - - - - - - - - -
    public AppDatabase appDatabase;

    // - - - - - - Retrofit api - - - - - - - - - - - - -
    private final String baseUrl = "https://gitlab.com/api/v4/"; //just a placeholder (will be overridden by the user input)
    ServiceGenerator serviceGenerator = new ServiceGenerator(this.baseUrl); //can only be instanced after the base url is set
    public GitLabClient gitLabClient = serviceGenerator.getGitLabClient();

    // - - - - -  Repository - - - - - - - - -
    public ProfileRepository profileRepository;

    // - - - - -  ViewModel provider - - - - -
    public LoginViewModelFactory loginViewModelFactory;


    public LoginContainer(Context context) {
        appDatabase = AppDatabase.getDatabaseInstance(context); //needs the context this is why its instanced in the constructor
        profileRepository = new ProfileRepository(Objects.requireNonNull(appDatabase), Objects.requireNonNull(gitLabClient));
        loginViewModelFactory =  new LoginViewModelFactory(Objects.requireNonNull(profileRepository));
    }


}
