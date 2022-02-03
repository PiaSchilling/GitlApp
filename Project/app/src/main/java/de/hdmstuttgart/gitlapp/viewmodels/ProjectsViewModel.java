package de.hdmstuttgart.gitlapp.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import de.hdmstuttgart.gitlapp.data.network.NetworkStatus;
import de.hdmstuttgart.gitlapp.data.repositories.IProfileRepository;
import de.hdmstuttgart.gitlapp.data.repositories.IProjectRepository;
import de.hdmstuttgart.gitlapp.models.Project;

public class ProjectsViewModel extends ViewModel {

    IProjectRepository projectRepository;
    IProfileRepository profileRepository;

    public ProjectsViewModel(IProjectRepository projectRepository, IProfileRepository profileRepository){
        this.projectRepository = projectRepository;
        this.profileRepository = profileRepository;
    }

    /**
     * calls repo to load data from local data or network
     */
    public void initProjectsLiveData(){
        projectRepository.initProjects();
    }

    /**
     * calling repo which tries to update data from network
     */
    public void refreshProjects(){
        projectRepository.fetchProjects();
    }

    public MutableLiveData<List<Project>> getMutableLiveData(){
        return projectRepository.getProjectsLiveData();
    }

    public MutableLiveData<NetworkStatus> getMessage() {
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

