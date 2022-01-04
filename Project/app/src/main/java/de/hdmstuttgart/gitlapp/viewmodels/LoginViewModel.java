package de.hdmstuttgart.gitlapp.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.hdmstuttgart.gitlapp.data.repositories.ProfileRepository;

public class LoginViewModel extends ViewModel {


    private final ProfileRepository profileRepository;

    public LoginViewModel(ProfileRepository profileRepository){
        this.profileRepository = profileRepository;
    }

    public void createUserProfile(String hostUrl, int userId, String accessToken){
        profileRepository.setProfileInformation(userId,hostUrl,accessToken);
    }

    public MutableLiveData<String> getMessageLiveData(){
        return profileRepository.getMessageLiveData();
    }
}
