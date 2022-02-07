package de.hdmstuttgart.gitlapp.viewmodels;

import org.junit.Before;
import org.junit.Test;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

import de.hdmstuttgart.gitlapp.TestIssueRepository;
import de.hdmstuttgart.gitlapp.models.Issue;
import de.hdmstuttgart.gitlapp.models.User;

/**
 * very simple test, just to demonstrate repo replacement with mock repo (to test viewModel independent from api and db)
 *
 * wont run without TestRule! (we were not sure if we are allowed to use the dependency which is required for InstantTaskExecutorRule()
 * this is why its commented out)
 */
public class IssueOverviewViewModelTest {

    private final TestIssueRepository testRepo = new TestIssueRepository();
    private final IssueOverviewViewModel viewModel = new IssueOverviewViewModel(testRepo);

    private List<Issue> closedIssues;
    private List<Issue> openedIssues;
    private List<Issue> allIssues;

    // @Rule
   // public TestRule rule = new InstantTaskExecutorRule();

    @Before
    public void initTestData(){
        User author = new User(1,"username", "name", "url","mail@mail.de");
        Issue issue1 = new Issue(1,1,1,"TestIssue One","Issue description one",author,1,1,1,1,"opened");
        Issue issue2 = new Issue(2,2,1,"TestIssue Two","Issue description two",author,1,1,1,1,"opened");
        Issue issue3 = new Issue(3,3,1,"TestIssue Three","Issue description three",author,1,1,1,1,"opened");
        Issue issue4 = new Issue(4,4,1,"TestIssue Four","Issue description four",author,1,1,1,1,"closed");
        Issue issue5 = new Issue(5,5,1,"TestIssue Five","Issue description five",author,1,1,1,1,"closed");

        allIssues = new ArrayList<>();
        allIssues.add(issue1);
        allIssues.add(issue2);
        allIssues.add(issue3);
        allIssues.add(issue4);
        allIssues.add(issue5);

        closedIssues = new ArrayList<>();
        closedIssues.add(issue4);
        closedIssues.add(issue5);

        openedIssues = new ArrayList<>();
        openedIssues.add(issue1);
        openedIssues.add(issue2);
        openedIssues.add(issue3);
    }

    @Test
    public void filterIssuesByState() {
        Assert.assertArrayEquals(closedIssues.toArray(),viewModel.filterIssuesByState("closed").toArray());
        Assert.assertArrayEquals(openedIssues.toArray(),viewModel.filterIssuesByState("opened").toArray());
        Assert.assertArrayEquals(allIssues.toArray(),viewModel.getMutableLiveData().getValue().toArray(new Issue[0]));
    }
}