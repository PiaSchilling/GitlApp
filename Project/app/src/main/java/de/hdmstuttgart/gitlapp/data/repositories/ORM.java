package de.hdmstuttgart.gitlapp.data.repositories;


import java.util.ArrayList;
import java.util.List;

import de.hdmstuttgart.gitlapp.data.database.AppDatabase;
import de.hdmstuttgart.gitlapp.models.Issue;
import de.hdmstuttgart.gitlapp.models.Label;
import de.hdmstuttgart.gitlapp.models.Milestone;
import de.hdmstuttgart.gitlapp.models.User;

/**
 * class holds methods which map nested objects to and from the according database table (we did not like the solutions for this problem provided by room)
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
        List<Milestone> milestones = new ArrayList<>();

        for (Issue issue : issues){
            User author = issue.getAuthor();
            List<Label> issueLabels = issue.getLabels();
            Milestone milestone = issue.getMilestone();

            for(Label issueLabel : issueLabels){
                issueLabel.setIssue_id(issue.getId()); //set the issue id for all labels
            }
            labels.addAll(issueLabels);

            authors.add(author);
            issue.setAuthor_id(author.getId());

            if(milestone != null){
                milestones.add(milestone);
                issue.setMilestone_id(milestone.getId());
            }
        }
        appDatabase.issueDao().insertIssues(issues);
        appDatabase.userDao().insertUsers(authors);
        appDatabase.labelDao().insertLabels(labels);
        appDatabase.milestoneDao().insertMilestones(milestones);
    }

    /**
     * queries all related data tables and inserts this data into (incomplete) issueObjects as nested objects
     * @param projectID the id of the project which the issues should be completed for
     * @param appDatabase the database which contains the according tables
     * @return a list of completely built issue objects (contains nested objects now)
     */
    public static List<Issue> completeIssueObjects(int projectID, AppDatabase appDatabase){
        List<Issue> projectIssues = appDatabase.issueDao().getProjectIssues(projectID);
        List<Issue> completedIssueObjects = new ArrayList<>();

        for (Issue issue : projectIssues){

            User author = appDatabase.userDao().getUserById(issue.getAuthor_id());
            List<Label> issueLabels = appDatabase.labelDao().getIssueLabels(issue.getId());
            Milestone milestone = appDatabase.milestoneDao().getMilestoneById(issue.getMilestone_id());

            issue.setAuthor(author);
            issue.setLabels(issueLabels);
            issue.setMilestone(milestone);

            completedIssueObjects.add(issue);
        }
        return completedIssueObjects;
    }
}
