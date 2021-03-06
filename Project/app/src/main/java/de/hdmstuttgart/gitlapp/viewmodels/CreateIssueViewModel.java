package de.hdmstuttgart.gitlapp.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import de.hdmstuttgart.gitlapp.data.network.NetworkStatus;
import de.hdmstuttgart.gitlapp.data.repositories.IIssueRepository;
import de.hdmstuttgart.gitlapp.data.repositories.IProjectRepository;
import de.hdmstuttgart.gitlapp.models.Label;
import de.hdmstuttgart.gitlapp.models.Milestone;

public class CreateIssueViewModel extends ViewModel {

    private final IIssueRepository issueRepository;
    private final IProjectRepository projectRepository;

    public CreateIssueViewModel(IIssueRepository issueRepository,IProjectRepository projectRepository){
        this.issueRepository = issueRepository;
        this.projectRepository = projectRepository;
    }

    /**
     * modifies the user input that it fits for the api call
     */
    public void postNewIssue(int projectId, String issueTitle, String issueDescription, String dueDate, String weight, List<String> labels, String milestoneName){

        //build label string from list
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
            Optional<Milestone> result = Objects.requireNonNull(projectRepository.getMilestoneLiveData(projectId).getValue())
                    .stream()
                    .filter(milestone -> milestone.getTitle().equals(milestoneName))
                    .findAny();
            if (result.isPresent()){
                milestoneId = result.get().getId();
            }
        }

        Log.d("Label", "try to post issue: " + projectId + "," + issueTitle + "," + issueDescription + "," + dueDate + "," +weightInt + "," + labelString.toString() + "," + milestoneId);
        issueRepository.postNewIssue(projectId,issueTitle,issueDescription,dueDate,weightInt,labelString.toString(),milestoneId);
    }

    public MutableLiveData<List<Label>> getProjectLabels(int projectId){
        return projectRepository.getLabelsLiveData(projectId);
    }

    public MutableLiveData<List<Milestone>>getProjectMilestones(int projectId){
        return projectRepository.getMilestoneLiveData(projectId);
    }

    public MutableLiveData<NetworkStatus> getMessage() {
        return issueRepository.getNetworkCallMessage();
    }
}
