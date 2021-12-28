package de.hdmstuttgart.gitlapp.viewmodels;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import de.hdmstuttgart.gitlapp.data.repositories.ProjectRepository;
import de.hdmstuttgart.gitlapp.models.Project;

public class ProjectViewModel {

    ProjectRepository projectRepository;

    public ProjectViewModel(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }

    public void refresh(){
        projectRepository.refreshData();
    }

    public MutableLiveData<List<Project>> showList(){
        return projectRepository.getProjectLiveData();
    }
}
