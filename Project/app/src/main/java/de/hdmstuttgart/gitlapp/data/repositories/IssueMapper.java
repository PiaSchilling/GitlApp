package de.hdmstuttgart.gitlapp.data.repositories;


import java.util.ArrayList;
import java.util.List;

import de.hdmstuttgart.gitlapp.data.database.AppDatabase;
import de.hdmstuttgart.gitlapp.models.Issue;
import de.hdmstuttgart.gitlapp.models.Label;
import de.hdmstuttgart.gitlapp.models.User;

/**
 * maps the response body of the api call to the according classes and
 */
public class IssueMapper {

    //die objekte extrahieren
    //Issue
    //  Author -> user
    //  Labels -> Label (list)


    //die objekte in die db speichern

    //map<Issue, author>
    //map <Issue, List<Label>

    private AppDatabase appDatabase;

    public IssueMapper(AppDatabase appDatabase){
        this.appDatabase = appDatabase;
    }

    /**
     * gets the nested objects of issue objects and inserts them into to the according database table
     * @param issues list of issues which should be saved
     */
    public void mapIssues(List<Issue> issues){
        List<User> authors = new ArrayList<>();
        List<Label> labels = new ArrayList<>();

        for (Issue issue : issues){
            User author = issue.getAuthor();
            List<Label> issueLabels = issue.getLabels();

            for(Label issueLabel : issueLabels){
                issueLabel.setIssueId(issue.getId()); //set the issue id for all labels
            }

            authors.add(author);
            labels.addAll(issueLabels);

            issue.setAuthor_id(author.getId());
        }
        appDatabase.issueDao().insertOrUpdate(issues);
        appDatabase.userDao().insertUsers(authors);
        appDatabase.labelDao().insertLabels(labels);
    }
}
