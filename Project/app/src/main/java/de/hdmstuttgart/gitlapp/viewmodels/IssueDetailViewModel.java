package de.hdmstuttgart.gitlapp.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import de.hdmstuttgart.gitlapp.data.repositories.IssueRepository;
import de.hdmstuttgart.gitlapp.models.Issue;
import de.hdmstuttgart.gitlapp.models.User;

/**
 * ViewModel for IssueDetail view (only one issue)
 */
public class IssueDetailViewModel extends ViewModel {

    private final IssueRepository issueRepository;


    /**
     * view model is always instanced for a specific issueId
     * @param issueRepository for fetching data
     */
    public IssueDetailViewModel(IssueRepository issueRepository){
        this.issueRepository = issueRepository;
    }

    /**
     * gets a single issue from the repo
     * if the issue with the issue id does not exist a fall back issue will be returned to prevent the app from crashing
     * @return the issue with the issueId or a fall back issue with dummy values
     */
    public MutableLiveData<Issue> getIssueDetailLiveData(int issueId) {
        try {
            return issueRepository.getSingleIssueLiveData(issueId);
        } catch (Exception e) {
            Log.e("Api",e.getMessage() + " | returning fallBackIssue");
            Issue fallBackIssue = new Issue(0,0,0,"-","-",new User(),0,0,0,0,0, "closed");
            MutableLiveData<Issue> fallBackLiveData = new MutableLiveData<>();
            fallBackLiveData.setValue(fallBackIssue);
            return fallBackLiveData;
        }
    }

    public void refreshData(){
        issueRepository.refreshProjectIssues(7124);
    }
}
