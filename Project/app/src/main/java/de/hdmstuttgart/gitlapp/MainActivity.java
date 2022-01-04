package de.hdmstuttgart.gitlapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import de.hdmstuttgart.gitlapp.models.Issue;
import de.hdmstuttgart.gitlapp.viewmodels.IssueDetailViewModel;
import de.hdmstuttgart.gitlapp.viewmodels.IssueDetailViewModelFactory;
import de.hdmstuttgart.gitlapp.viewmodels.ProjectViewModel;

public class MainActivity extends AppCompatActivity {

    private IssueDetailViewModel issuesViewModel;
    private ProjectViewModel projectViewModel;
    private Button button;
    private Button button2;
    private TextView textView;
    private TextView textView2;

    private IssueDetailViewModelFactory factory;
    private MutableLiveData<Issue> issueMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Issue>> listMutableLiveData = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_issue_detail);

        // - - - - - get the di container for getting the global dependencies - - - - - - - (for now the container could be stored in the mainActivity but there might me more activities in the future)

        AppContainer container = ((CustomApplication) getApplication()).getContainer(getApplicationContext());

        // - - - - - - instance view model - - - - - - - -

        container.issueRepository.initData(7124); //todo move to issuesViewModel

        this.projectViewModel = new ProjectViewModel(container.projectRepository);

        this.factory = new IssueDetailViewModelFactory(container.issueRepository,12603);
        this.issuesViewModel = new ViewModelProvider(this, factory).get(IssueDetailViewModel.class);

        this.issueMutableLiveData.setValue(issuesViewModel.getIssueDetailLiveData().getValue());

        textView = findViewById(R.id.issue_title);
        textView.setText(issueMutableLiveData.getValue().getTitle());




      /*  button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        button2 = findViewById(R.id.button2);
        textView2 = findViewById(R.id.textView2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickActionProject();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickActionIssue();
            }
        });

        //make a toast to inform user if update was successful
        Observer<String> toastObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getApplicationContext(), issueViewModel.getMessage().getValue(), Toast.LENGTH_SHORT).show();
            }
        };


        issueViewModel.getMessage().observe(this, toastObserver)*/;
    }

 /*   private void clickActionIssue() {
        issuesViewModel.refresh();
        textView2.setText(Arrays.toString(issuesViewModel.showList().getValue().toArray()));
    }

    public void clickActionProject() {
        projectViewModel.refresh();
        textView.setText(Arrays.toString(projectViewModel.showList().getValue().toArray()));
    }*/


}