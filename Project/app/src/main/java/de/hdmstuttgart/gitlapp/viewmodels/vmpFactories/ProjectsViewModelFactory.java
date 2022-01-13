package de.hdmstuttgart.gitlapp.viewmodels.vmpFactories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import de.hdmstuttgart.gitlapp.data.repositories.ProfileRepository;
import de.hdmstuttgart.gitlapp.data.repositories.ProjectRepository;
import de.hdmstuttgart.gitlapp.viewmodels.ProjectsViewModel;

public class ProjectsViewModelFactory implements ViewModelProvider.Factory {

    private final ProjectRepository projectRepository;
    private  final ProfileRepository profileRepository;

    public ProjectsViewModelFactory(ProjectRepository projectRepository, ProfileRepository profileRepository){
        this.projectRepository = projectRepository;
        this.profileRepository = profileRepository;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(ProjectsViewModel.class)){
            ProjectsViewModel projectsViewModel = new ProjectsViewModel(projectRepository , profileRepository);
            return (T) projectsViewModel;
        }
        throw new IllegalArgumentException("ViewModel class not found");
    }
}
