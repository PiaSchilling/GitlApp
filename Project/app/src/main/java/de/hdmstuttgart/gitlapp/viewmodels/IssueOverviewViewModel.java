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

    private final IssueRepository issueRepository;
    private int pageNumber;

    public IssueOverviewViewModel(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
        this.pageNumber = 1;
    }

    /**
     * calls repo to load data from local data or network
     * but only if the issues for this project aren't currently stored in the live data object (cached)
     */
    public void initIssueLiveData(int projectId) {
        issueRepository.initProjectIssues(projectId);
    }

    /**
     * calling repo which tries to update data from network
     */
    public void updateIssueLiveData(int projectId) {
        issueRepository.fetchProjectIssues(projectId, pageNumber);
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

    public void incrementPageNumber(){
        this.pageNumber++;
    }

    public MutableLiveData<List<Issue>> getMutableLiveData() {
        return issueRepository.getIssueListLiveData();
    }

    public MutableLiveData<String> getMessage() {
        return issueRepository.getNetworkCallMessage();
    }


}
