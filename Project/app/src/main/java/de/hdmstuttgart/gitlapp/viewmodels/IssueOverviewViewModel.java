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

    MutableLiveData<List<Issue>> mutableLiveData = new MutableLiveData<>();

    public IssueOverviewViewModel(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    /**
     * triggers refresh method in repo for re-fetching data from api (if possible)
     */
    public void refreshProjectIssues(int projectId) {
        issueRepository.refreshProjectIssues(projectId);
    }

    public MutableLiveData<List<Issue>> getIssueListLiveData() {
        Log.d("Api", "ProjectId in viewModel ");
        return this.mutableLiveData;
    }

    /**
     * calls repo to load local data (if present)
     * then tries to update data from network
     * @return
     */
    public MutableLiveData<List<Issue>> initIssueLiveData(int projectId) {
        issueRepository.initProjectIssues(projectId);
        issueRepository.refreshProjectIssues(projectId);
       // mutableLiveData.setValue(issueRepository.getIssueListLiveData().getValue());
        return issueRepository.getIssueListLiveData();
    }

    /**
     * tries to update data from network
     * @return
     */
    public MutableLiveData<List<Issue>> updateIssueLiveData(int projectId) {
        issueRepository.refreshProjectIssues(projectId);
       // mutableLiveData.setValue(issueRepository.getIssueListLiveData().getValue());
        return issueRepository.getIssueListLiveData();
    }

    public MutableLiveData<List<Issue>> clearIssueFilter() {
        //this.mutableLiveData.setValue(issueRepository.getIssueListLiveData().getValue());
        return issueRepository.getIssueListLiveData();
    }

    public List<Issue> filterIssuesByState(String state) {
       /* List<Issue> temp = issueRepository.getIssueListLiveData().getValue();

        try {
            List<Issue> result = Objects.requireNonNull(temp)
                    .stream()
                    .filter(issue -> issue.getState().equals(state))
                    .collect(Collectors.toList());
            mutableLiveData.setValue(result);
        } catch (NullPointerException e) {
            Log.e("Api", "Issue list empty");
        }*/
        List<Issue> temp = issueRepository.getIssueListLiveData().getValue();
        List<Issue> result = Objects.requireNonNull(temp)
                .stream()
                .filter(issue -> issue.getState().equals(state))
                .collect(Collectors.toList());
        return result;
    }

    public MutableLiveData<String> getMessage() {
        return issueRepository.getNetworkCallMessage();
    }

    public MutableLiveData<List<Issue>> getMutableLiveData() {
        return issueRepository.getIssueListLiveData();
    }
}
