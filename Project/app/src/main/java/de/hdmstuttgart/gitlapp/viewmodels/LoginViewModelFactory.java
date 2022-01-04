package de.hdmstuttgart.gitlapp.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import de.hdmstuttgart.gitlapp.data.repositories.IssueRepository;
import de.hdmstuttgart.gitlapp.data.repositories.ProfileRepository;

public class LoginViewModelFactory implements ViewModelProvider.Factory {

    private final ProfileRepository profileRepository;

    public LoginViewModelFactory(ProfileRepository profileRepository){
        this.profileRepository = profileRepository;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(LoginViewModel.class)){
            LoginViewModel loginViewModel = new LoginViewModel(profileRepository);
            return (T) loginViewModel;
        }
        throw new IllegalArgumentException("ViewModel class not found");
    }
}
