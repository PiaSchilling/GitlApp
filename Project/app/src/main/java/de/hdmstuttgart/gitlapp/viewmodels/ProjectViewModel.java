package de.hdmstuttgart.gitlapp.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import de.hdmstuttgart.gitlapp.data.repositories.ProjectRepository;
import de.hdmstuttgart.gitlapp.models.Project;

public class ProjectViewModel extends ViewModel {

    ProjectRepository projectRepository;

    public ProjectViewModel(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }

    /**
     * make an api call to update the data
     */
    public void updateProjectIssues(){
        projectRepository.refreshData();
    }

    public MutableLiveData<List<Project>> getProjectIssueLiveData(){
        return projectRepository.getProjectLiveData();
    }
}
