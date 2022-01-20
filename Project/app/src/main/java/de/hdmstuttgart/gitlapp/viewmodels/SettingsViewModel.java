package de.hdmstuttgart.gitlapp.viewmodels;

import androidx.lifecycle.ViewModel;

import de.hdmstuttgart.gitlapp.data.repositories.ProfileRepository;
import de.hdmstuttgart.gitlapp.data.repositories.ProjectRepository;

public class SettingsViewModel extends ViewModel {


    private final ProfileRepository profileRepository;
    private final ProjectRepository projectRepository;

    public SettingsViewModel(ProfileRepository profileRepository, ProjectRepository projectRepository) {
        this.profileRepository = profileRepository;
        this.projectRepository = projectRepository;
    }


    public String getGitlabUrl(){
         return profileRepository.getHostUrl();
    }

    public String getUserId(){
          return String.valueOf(profileRepository.getLoggedInUserId());
    }

    public String getLoggedInUserName(){
        return profileRepository.getLoggedIdUser().getName();
    }

    public String getLoggedInUserAvatar(){
        return profileRepository.getLoggedIdUser().getAvatar_url();
    }

    public void clearDatabase(){
        profileRepository.clearAppData();
    }
}
