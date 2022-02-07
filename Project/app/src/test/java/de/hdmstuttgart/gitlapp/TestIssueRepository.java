package de.hdmstuttgart.gitlapp;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import de.hdmstuttgart.gitlapp.data.network.NetworkStatus;
import de.hdmstuttgart.gitlapp.data.repositories.IIssueRepository;
import de.hdmstuttgart.gitlapp.models.Issue;
import de.hdmstuttgart.gitlapp.models.User;

/**
 * mock repo which contains hardcoded data -> allows us to test the viewModel decoupled from the api and db
 */
public class TestIssueRepository implements IIssueRepository {

    @Override
    public void initProjectIssues(int projectId) {
        //not needed/tested
    }

    @Override
    public void fetchProjectIssues(int projectId, int page) {
        //not needed/tested
    }

    @Override
    public void postNewIssue(int projectId, String issueTitle, String issueDescription, String dueDate, int weight, String labels, int milestoneId) {
        //not needed/tested
    }

    @Override
    public void closeIssue(int projectId, int issueIid) {
        //not needed/tested
    }

    @Override
    public MutableLiveData<List<Issue>> getIssueListLiveData() {
        User author = new User(1,"username", "name", "url","mail@mail.de");
        Issue issue1 = new Issue(1,1,1,"TestIssue One","Issue description one",author,1,1,1,1,"opened");
        Issue issue2 = new Issue(2,2,1,"TestIssue Two","Issue description two",author,1,1,1,1,"opened");
        Issue issue3 = new Issue(3,3,1,"TestIssue Three","Issue description three",author,1,1,1,1,"opened");
        Issue issue4 = new Issue(4,4,1,"TestIssue Four","Issue description four",author,1,1,1,1,"closed");
        Issue issue5 = new Issue(5,5,1,"TestIssue Five","Issue description five",author,1,1,1,1,"closed");

        List<Issue> issues = new ArrayList<>();
        issues.add(issue1);
        issues.add(issue2);
        issues.add(issue3);
        issues.add(issue4);
        issues.add(issue5);

        MutableLiveData<List<Issue>> liveData = new MutableLiveData<>();
        liveData.setValue(issues);

        return liveData;
    }

    @Override
    public MutableLiveData<Issue> getSingleIssueLiveData(int issueId) throws Exception {
        //not needed/tested
        return null;
    }

    @Override
    public MutableLiveData<NetworkStatus> getNetworkCallMessage() {
        //not needed/tested
        return null;
    }
}
