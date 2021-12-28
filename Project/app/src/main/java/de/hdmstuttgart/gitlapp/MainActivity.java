package de.hdmstuttgart.gitlapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;

import de.hdmstuttgart.gitlapp.viewmodels.IssueViewModel;
import de.hdmstuttgart.gitlapp.viewmodels.ProjectViewModel;

public class MainActivity extends AppCompatActivity {

    private IssueViewModel issueViewModel;
    private ProjectViewModel projectViewModel;
    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // - - - - - get the di container for getting the global dependencies - - - - - - - (for now the container could be stored in the mainActivity but there might me more activities in the future)
        CustomApplication customApplication = new CustomApplication();
        AppContainer container = customApplication.getContainer(getApplicationContext());

        // - - - - - - instance view model - - - - - - - -
        this.issueViewModel = new IssueViewModel(container.issueRepository);
        this.projectViewModel = new ProjectViewModel(container.projectRepository);

        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //clickActionIssue();
                clickActionProject();
            }
        });
    }

    private void clickActionIssue(){
        issueViewModel.refresh();
       textView.setText(Arrays.toString(issueViewModel.showList().toArray()));
    }

    public void clickActionProject(){
        projectViewModel.refresh();
        textView.setText(Arrays.toString(projectViewModel.showList().getValue().toArray()));
    }


}