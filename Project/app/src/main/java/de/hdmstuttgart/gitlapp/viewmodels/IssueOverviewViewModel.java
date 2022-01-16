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


    public IssueOverviewViewModel(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    /**
     * calls repo to load local data (if present)
     * then tries to update data from network
     * @return the initialized live data
     */
    public MutableLiveData<List<Issue>> initIssueLiveData(int projectId) {
        issueRepository.initProjectIssues(projectId);
        issueRepository.refreshProjectIssues(projectId);
        return issueRepository.getIssueListLiveData();
    }

    /**
     * tries to update data from network
     * @return updated live data
     */
    public MutableLiveData<List<Issue>> updateIssueLiveData(int projectId) {
        issueRepository.refreshProjectIssues(projectId);
        return issueRepository.getIssueListLiveData();
    }


    public List<Issue> filterIssuesByState(String state) {
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
