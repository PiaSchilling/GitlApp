package de.hdmstuttgart.gitlapp.viewmodels;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import de.hdmstuttgart.gitlapp.data.repositories.IssueRepository;
import de.hdmstuttgart.gitlapp.models.Issue;

public class IssueViewModel {

    IssueRepository issueRepository;

    public IssueViewModel(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    public void refresh(){
        issueRepository.refreshData();
    }

    public MutableLiveData<List<Issue>> showList(){
        return issueRepository.getIssueLiveData();
    }
}
