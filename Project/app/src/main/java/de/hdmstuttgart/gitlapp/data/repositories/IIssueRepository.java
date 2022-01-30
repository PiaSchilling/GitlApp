package de.hdmstuttgart.gitlapp.data.repositories;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import de.hdmstuttgart.gitlapp.data.network.NetworkStatus;
import de.hdmstuttgart.gitlapp.models.Issue;

public interface IIssueRepository {

    void initProjectIssues(int projectId);

    void fetchProjectIssues(int projectId, int page);

    void postNewIssue(int projectId, String issueTitle, String issueDescription, String dueDate, int weight, String labels, int milestoneId);

    void closeIssue(int projectId, int issueIid);

    MutableLiveData<List<Issue>> getIssueListLiveData();

    MutableLiveData<Issue> getSingleIssueLiveData(int issueId) throws Exception;

    MutableLiveData<NetworkStatus> getNetworkCallMessage();
}

