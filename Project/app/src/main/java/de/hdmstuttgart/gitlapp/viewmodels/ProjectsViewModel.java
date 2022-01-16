package de.hdmstuttgart.gitlapp.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import de.hdmstuttgart.gitlapp.data.repositories.ProfileRepository;
import de.hdmstuttgart.gitlapp.data.repositories.ProjectRepository;
import de.hdmstuttgart.gitlapp.models.Project;

public class ProjectsViewModel extends ViewModel {

    ProjectRepository projectRepository;
    ProfileRepository profileRepository;

    // MutableLiveData<List<Project>> mutableProjectLiveData = new MutableLiveData<>();


    public ProjectsViewModel(ProjectRepository projectRepository, ProfileRepository profileRepository){
        this.projectRepository = projectRepository;
        this.profileRepository = profileRepository;
    }

    /**
     * refresh method in repo for re-fetching data from api if available
     */
    public void refreshProjects(){
        projectRepository.refreshProjects();
    }


    // reine durch-reiche Methode
    public MutableLiveData<List<Project>> getMutableLiveData(){
        return projectRepository.getProjectsLiveData();
    }

    
    /**
     * calls repo to load data from local data or network 
     */
    public void initProjectsLiveData(){
        projectRepository.initProjects();
    }


    public MutableLiveData<String> getMessage() {
        return projectRepository.getNetworkCallMessage();
    }


    // - - - - profile methods - - - - - - - - - -

    public String getLoggedInUserName(){
        return profileRepository.getLoggedIdUser().getName();
    }

    public String getLoggedInUserAvatar(){
        return profileRepository.getLoggedIdUser().getAvatar_url();
    }


}

