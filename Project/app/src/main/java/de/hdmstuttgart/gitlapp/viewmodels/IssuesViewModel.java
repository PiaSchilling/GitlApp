package de.hdmstuttgart.gitlapp.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import de.hdmstuttgart.gitlapp.data.repositories.IssueRepository;
import de.hdmstuttgart.gitlapp.models.Issue;

/***
 * ViewModel for handling issue lists from certain projects
 */
public class IssuesViewModel extends ViewModel {

    IssueRepository issueRepository;
    int projectId;

    public IssuesViewModel(IssueRepository issueRepository, int projectId) {
        this.issueRepository = issueRepository;
        this.projectId = projectId;
        issueRepository.initProjectIssues(projectId);
    }

    /**
     * triggers refresh method in repo for re-fetching data from api (if possible)
     */
    public void refreshProjectIssues() {
        issueRepository.refreshProjectIssues(projectId);
    }

    public MutableLiveData<List<Issue>> getIssueListLiveData(){
        return issueRepository.getIssueListLiveData();
    }

    public MutableLiveData<String> getMessage(){
        return issueRepository.getNetworkCallMessage();
    }

}
