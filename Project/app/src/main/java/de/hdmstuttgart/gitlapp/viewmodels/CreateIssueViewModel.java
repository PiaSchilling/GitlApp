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

    /**
     * modifies the user input that it fits for the api call
     */
    public void postNewIssue(int projectId, String issueTitle, String issueDescription, String dueDate, String weight, List<String> labels, String milestoneName){

        //build label string
        StringBuilder labelString = new StringBuilder();
        for (String label : labels){
            labelString.append(label).append(",");
        }

        //try to cast weight string to int
        int weightInt = 0;
        try{
            weightInt = Integer.parseInt(weight);
        } catch (NumberFormatException e){
            Log.e("Api","weight not numeric");
        }

        //get milestone by name if not null
        int milestoneId = 0;
        if(milestoneName != null){
            Optional<Milestone> result = projectRepository.getProjectMilestones(projectId).stream()
                    .filter(milestone -> milestone.getTitle().equals(milestoneName)).findAny();
            if (result.isPresent()){
                milestoneId = result.get().getId();
            }
        }

        Log.d("Label", "try to post issue: " + projectId + "," + issueTitle + "," + issueDescription + "," + dueDate + "," +weightInt + "," + labelString.toString() + "," + milestoneId);
        issueRepository.postNewIssue(projectId,issueTitle,issueDescription,dueDate,weightInt,labelString.toString(),milestoneId);
    }

    public List<Label> getProjectLabels(int projectId){
        return projectRepository.getProjectLabels(projectId);
    }

    public List<Milestone> getProjectMilestones(int projectId){
        return projectRepository.getProjectMilestones(projectId);
    }

    public MutableLiveData<String> getMessage() {
        return issueRepository.getNetworkCallMessage();
    }
}
