package de.hdmstuttgart.gitlapp.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.hdmstuttgart.gitlapp.data.network.NetworkStatus;
import de.hdmstuttgart.gitlapp.data.repositories.IProfileRepository;

public class LoginViewModel extends ViewModel {


    private final IProfileRepository profileRepository;

    public LoginViewModel(IProfileRepository profileRepository){
        this.profileRepository = profileRepository;
    }

    public void createUserProfile(String hostUrl, int userId, String accessToken){
        profileRepository.setProfileInformation(userId,hostUrl,accessToken);
    }

    public MutableLiveData<NetworkStatus> getMessageLiveData(){
        return profileRepository.getMessageLiveData();
    }
}
