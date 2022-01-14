package de.hdmstuttgart.gitlapp.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Optional;
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

    public void postNewIssue(int projectId, String issueTitle, String issueDescription, String dueDate, int weight, int milestoneId, List<String> labels){
        StringBuilder labelString = new StringBuilder();
        for (String label : labels){
            labelString.append(label).append(",");
        }
        Log.d("Label", labelString.toString());
        issueRepository.postNewIssue(projectId,issueTitle,issueDescription, dueDate, weight, milestoneId, labelString.toString());
    }

    /**
     * gets the names from all labels of a project and puts them into a list
     * @param projectId the project to get the labels for
     * @return a list of strings containing only the names of the labels
     */
    public List<String> getProjectLabelsNames(int projectId){
        List<String> names = projectRepository.getProjectLabels(projectId)
                .stream()
                .map(Label::getName)
                .collect(Collectors.toList());
        names.add("-"); //to also provide the option to select no label at all
         return names;
    }

    public List<Label> getProjectLabels(int projectId){
        return projectRepository.getProjectLabels(projectId);
    }

    public Label getLabelByName(int projectId, String name){
        List<Label> projectLabels = projectRepository.getProjectLabels(projectId);

        Optional<Label> result = projectLabels.stream().filter(label -> label.getName().equals(name)).findAny();

        if(result.isPresent()){
            return result.get();
        }else{
            Log.e("Api","No label with name " + name + " in project " + projectId + " found");
            return null; //todo dangerous
        }
    }

    /**
     * gets the names from all milestones of a project and puts them into a list
     * @param projectId the project to get the milestones for
     * @return a list of strings containing only the names of the milestones
     */
    public List<String> getProjectMilestoneNames(int projectId){
        List<String> names = projectRepository.getProjectMilestones(projectId)
                .stream()
                .map(Milestone::getTitle)
                .collect(Collectors.toList());
        names.add("-"); //to also provide the option to select no milestone at all
        return names;
    }
}
