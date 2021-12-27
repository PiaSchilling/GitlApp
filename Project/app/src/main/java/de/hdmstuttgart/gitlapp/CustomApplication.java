
package de.hdmstuttgart.gitlapp;

import android.app.Application;
import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomApplication extends Application {

    private AppContainer container;

    //dependency container (singleton access)
    public AppContainer getContainer(Context context){
        if(container == null){
            container = new AppContainer(context);
            return container;
        }
        return container;
    }
}

