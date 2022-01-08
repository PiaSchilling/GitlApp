
package de.hdmstuttgart.gitlapp;

import android.app.Application;
import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomApplication extends Application {

    private AppContainer container;
    private LoginContainer loginContainer;

    //dependency container (singleton access)
    public AppContainer getAppContainer(Context context){
        if(container == null){
            container = new AppContainer(context);
            return container;
        }
        return container;
    }

    public LoginContainer getLoginContainer(Context context){
        if(loginContainer == null){
            loginContainer = new LoginContainer(context);
            return loginContainer;
        }
        return loginContainer;
    }
}

