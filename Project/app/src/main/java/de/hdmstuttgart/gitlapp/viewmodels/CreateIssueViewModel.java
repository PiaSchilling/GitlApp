package de.hdmstuttgart.gitlapp.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.stream.Collectors;

import de.hdmstuttgart.gitlapp.data.repositories.IssueRepository;
import de.hdmstuttgart.gitlapp.data.repositories.ProjectRepository;
import de.hdmstuttgart.gitlapp.models.Label;
import de.hdmstuttgart.gitlapp.models.Milestone;

public class CreateIssueViewModel extends ViewModel {

    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;

    public CreateIssueViewModel(IssueRepository issueRepository,ProjectRepository projectRepository){
        this.issueRepository = issueRepository;
        this.projectRepository = projectRepository;
    }

    public void postNewIssue(int projectId, String issueTitle, String issueDescription, String dueDate, int weight, int milestoneId, String labels){
        issueRepository.postNewIssue(projectId,issueTitle,issueDescription, dueDate, weight, milestoneId, labels);
    }

    /**
     * gets the names from all labels of a project and puts them into a list
     * @param projectId the project to get the labels for
     * @return a list of strings containing only the names of the labels
     */
    public List<String> getProjectLabelsNames(int projectId){
         return projectRepository.getProjectLabels(projectId)
                 .stream()
                 .map(Label::getName)
                 .collect(Collectors.toList());
    }

    /**
     * gets the names from all milestones of a project and puts them into a list
     * @param projectId the project to get the milestones for
     * @return a list of strings containing only the names of the milestones
     */
    public List<String> getProjectMilestoneNames(int projectId){
        return projectRepository.getProjectMilestones(projectId)
                .stream()
                .map(Milestone::getTitle)
                .collect(Collectors.toList());
    }
}
