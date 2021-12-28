package de.hdmstuttgart.gitlapp.data.repositories;


import java.util.ArrayList;
import java.util.List;

import de.hdmstuttgart.gitlapp.data.database.AppDatabase;
import de.hdmstuttgart.gitlapp.models.Issue;
import de.hdmstuttgart.gitlapp.models.Label;
import de.hdmstuttgart.gitlapp.models.Milestone;
import de.hdmstuttgart.gitlapp.models.User;

/**
 * class holds methods which map nested objects in the issueObject to and from the according database table
 */
public class ORM {

    /**
     * gets the nested objects of issue objects and inserts them into to the according database table
     * @param issues list of issues which should be saved/ inserted to the db
     * @param appDatabase database in which should be inserted
     */
    public static void mapAndInsertIssues(List<Issue> issues, AppDatabase appDatabase){
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

    /**
     * selects objects from db and inserts them to issueObjects as nested objects
     * @param projectID the id of the project which the issues should be builded for
     * @param appDatabase the database which contains the according tables
     * @return a list of completely built issues
     */
    public static List<Issue> mapIssueObjectsFromDb(int projectID, AppDatabase appDatabase){
        List<Issue> projectIssues = appDatabase.issueDao().getProjectIssues(projectID);
        List<Issue> completeIssueObjects = new ArrayList<>();

        for (Issue issue : projectIssues){

            User author = appDatabase.userDao().getUserById(issue.getAuthor_id());
            List<Label> issueLabels = appDatabase.labelDao().getIssueLabels(issue.getId());

            issue.setAuthor(author);
            issue.setLabels(issueLabels);

            completeIssueObjects.add(issue);
        }
        return completeIssueObjects;
    }
}
