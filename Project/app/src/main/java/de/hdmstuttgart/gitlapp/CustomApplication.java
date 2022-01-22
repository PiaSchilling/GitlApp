
package de.hdmstuttgart.gitlapp;

import android.app.Application;
import android.content.Context;

import de.hdmstuttgart.gitlapp.dependencies.AppContainer;
import de.hdmstuttgart.gitlapp.dependencies.LoginContainer;

public class CustomApplication extends Application {

    private AppContainer container;
    private LoginContainer loginContainer;

    //dependency container
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

