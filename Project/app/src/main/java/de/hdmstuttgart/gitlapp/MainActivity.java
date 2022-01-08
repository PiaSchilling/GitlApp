package de.hdmstuttgart.gitlapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import de.hdmstuttgart.gitlapp.fragments.IssueOverviewFragment;
import de.hdmstuttgart.gitlapp.fragments.LoginFragment;
import de.hdmstuttgart.gitlapp.fragments.ProjectsFragment;

public class MainActivity extends AppCompatActivity {

    public String baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // show login screen if no profile info is saved otherwise show no login screen
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("profileInformation", Context.MODE_PRIVATE);
        baseUrl = sharedPref.getString("baseUrl", "default");

        Log.d("Base", baseUrl);

        if (baseUrl.equals("default")) {
            Log.d("Login", "No profile saved, show login screen");
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, LoginFragment.class, null)
                        .commit();
            }
        } else {
            Log.d("Login", "Base url " + baseUrl);
            Log.d("Login", "Profile saved, show login screen");
            if (savedInstanceState == null) {

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, ProjectsFragment.class, null)
                        .commit();
            }
        }
    }
}