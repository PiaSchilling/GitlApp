package de.hdmstuttgart.gitlapp.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import de.hdmstuttgart.gitlapp.data.repositories.ProjectRepository;
import de.hdmstuttgart.gitlapp.models.Project;

public class ProjectsViewModel extends ViewModel {

    ProjectRepository projectRepository;

    // MutableLiveData<List<Project>> mutableProjectLiveData = new MutableLiveData<>();


    public ProjectsViewModel(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }



    /**
     * refresh method in repo for re-fetching data from api if available
     */
    public void refreshProjects(){
        projectRepository.refreshProjects();
    }


    // reine durch-reiche Methode
    public MutableLiveData<List<Project>> getMutableLiveData(){
        return projectRepository.getProjectLiveData();
    }

    
    /**
     * calls repo to load data from local data or network 
     */
    
    public void initProjectsLiveData(){
        projectRepository.initProjects();
        projectRepository.refreshProjects();
    }


    public MutableLiveData<String> getMessage() {
        return projectRepository.getNetworkCallMessage();
    }
}
