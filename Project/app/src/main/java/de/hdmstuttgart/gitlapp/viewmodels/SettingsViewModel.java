package de.hdmstuttgart.gitlapp.viewmodels;

import androidx.lifecycle.ViewModel;

import de.hdmstuttgart.gitlapp.data.repositories.IProfileRepository;

public class SettingsViewModel extends ViewModel {


    private final IProfileRepository profileRepository;

    public SettingsViewModel(IProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
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
