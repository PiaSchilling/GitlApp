package de.hdmstuttgart.gitlapp.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import de.hdmstuttgart.gitlapp.data.repositories.IssueRepository;
import de.hdmstuttgart.gitlapp.models.Issue;

/***
 * ViewModel for handling issue lists from certain projects
 */
public class IssueOverviewViewModel extends ViewModel {

    IssueRepository issueRepository;
    int projectId;

    MutableLiveData<List<Issue>> mutableLiveData = new MutableLiveData<>();

    public IssueOverviewViewModel(IssueRepository issueRepository, int projectId) {
        this.issueRepository = issueRepository;
        this.projectId = projectId;
        issueRepository.initProjectIssues(projectId);

        this.mutableLiveData.setValue(issueRepository.getIssueListLiveData().getValue());
    }

    /**
     * triggers refresh method in repo for re-fetching data from api (if possible)
     */
    public void refreshProjectIssues() {
        issueRepository.refreshProjectIssues(projectId);
    }

    public MutableLiveData<List<Issue>> getIssueListLiveData(){
        Log.d("Api","ProjectId in viewModel " + projectId);
        issueRepository.initProjectIssues(projectId);//todo remove
        issueRepository.refreshProjectIssues(projectId);//todo remove
        return this.mutableLiveData;
    }

    public void clearIssueFilter(){
        this.mutableLiveData.setValue(issueRepository.getIssueListLiveData().getValue());
        Log.d("WAAS","clear filter " + mutableLiveData.getValue().toString());
        Log.d("WAAS", "issue repo " + issueRepository.getIssueListLiveData());
    }

    public void filterIssuesByState(String state){
        List<Issue> temp = issueRepository.getIssueListLiveData().getValue();

        try{
            List<Issue> result = Objects.requireNonNull(temp)
                    .stream()
                    .filter(issue -> issue.getState().equals(state))
                    .collect(Collectors.toList());
            mutableLiveData.setValue(result);
        }catch (NullPointerException e){
            Log.e("Api","Issue list empty");
        }
    }

    public MutableLiveData<String> getMessage(){
        return issueRepository.getNetworkCallMessage();
    }

    public MutableLiveData<List<Issue>> getMutableLiveData() {
        return mutableLiveData;
    }
}
