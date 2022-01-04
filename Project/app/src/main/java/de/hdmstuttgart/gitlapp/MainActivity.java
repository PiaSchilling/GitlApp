package de.hdmstuttgart.gitlapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import de.hdmstuttgart.gitlapp.fragments.IssueDetailFragment;
import de.hdmstuttgart.gitlapp.fragments.LoginFragment;
import de.hdmstuttgart.gitlapp.models.Issue;
import de.hdmstuttgart.gitlapp.viewmodels.IssueDetailViewModel;
import de.hdmstuttgart.gitlapp.viewmodels.IssueDetailViewModelFactory;
import de.hdmstuttgart.gitlapp.viewmodels.ProjectViewModel;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // - - - - - get the di container  - - - - - - -
        AppContainer container = ((CustomApplication) getApplication()).getContainer(getApplicationContext());

        // - - - - - check if a profile is saved if so no login screen will be showed - - - - -
        try {
            Log.d("Login", "Profile saved, show no login screen");
            String accessToken = container.appDatabase.profileDao().getProfile().getAccessToken();
            String baseUrl = container.appDatabase.profileDao().getProfile().getHostUrl();

            container.setBaseUrl(baseUrl);
        } catch (NullPointerException e) {
            Log.d("Login", "No profile saved, show login screen");
            //todo show login screen
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, LoginFragment.class, null)
                    .commit();
        }

    }
}