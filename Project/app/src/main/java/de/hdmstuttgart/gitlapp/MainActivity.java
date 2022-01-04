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
import de.hdmstuttgart.gitlapp.models.Issue;
import de.hdmstuttgart.gitlapp.viewmodels.IssueDetailViewModel;
import de.hdmstuttgart.gitlapp.viewmodels.IssueDetailViewModelFactory;
import de.hdmstuttgart.gitlapp.viewmodels.ProjectViewModel;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // - - - - - get the di container for getting the global dependencies - - - - - - - (for now the container could be stored in the mainActivity but there might me more activities in the future)

        AppContainer container = ((CustomApplication) getApplication()).getContainer(getApplicationContext());

        // - - - - - - instance view model - - - - - - - -

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, IssueDetailFragment.newInstance(12655),null)
                    .commit();
        }

    }
}