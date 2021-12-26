
package de.hdmstuttgart.gitlapp;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomApplication extends Application {

    //dependency container which can be accessed by all classes bc its in a custom application class
    public AppContainer container = new AppContainer(getApplicationContext());

}

