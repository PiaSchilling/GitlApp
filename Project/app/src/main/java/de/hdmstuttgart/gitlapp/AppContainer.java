package de.hdmstuttgart.gitlapp;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hdmstuttgart.gitlapp.data.database.AppDatabase;
import de.hdmstuttgart.gitlapp.data.repositories.IssueRepository;

//Container of objects shared across the whole app (dependency injection)
public class AppContainer {

    private Context applicationContext;

    public AppContainer(Context context){
        this.applicationContext = context;
    }

    public ExecutorService executorService = Executors.newFixedThreadPool(2);

    public AppDatabase appDatabase = AppDatabase.getDatabaseInstance(applicationContext);
    public IssueRepository issueRepository = new IssueRepository(appDatabase);

}
