package de.hdmstuttgart.gitlapp.viewmodels;

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
     * calls repo to load data from local data or network
     */
    public void initIssueLiveData(int projectId) {
        issueRepository.initProjectIssues(projectId);
    }

    /**
     * calling repo which tries to update data from network
     */
    public void updateIssueLiveData(int projectId) {
        issueRepository.fetchProjectIssues(projectId);
    }

    /**
     * filters a list of issues by a given state
     * @param state issue state (open, closed, all)
     * @return the filtered list
     */
    public List<Issue> filterIssuesByState(String state) {
        List<Issue> temp = issueRepository.getIssueListLiveData().getValue();
        return Objects.requireNonNull(temp)
                .stream()
                .filter(issue -> issue.getState().equals(state))
                .collect(Collectors.toList());
    }

    public MutableLiveData<List<Issue>> getMutableLiveData() {
        return issueRepository.getIssueListLiveData();
    }

    public MutableLiveData<String> getMessage() {
        return issueRepository.getNetworkCallMessage();
    }

}
