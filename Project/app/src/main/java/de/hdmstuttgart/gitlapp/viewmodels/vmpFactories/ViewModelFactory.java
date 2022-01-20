package de.hdmstuttgart.gitlapp.viewmodels.vmpFactories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import de.hdmstuttgart.gitlapp.data.repositories.IssueRepository;
import de.hdmstuttgart.gitlapp.data.repositories.ProfileRepository;
import de.hdmstuttgart.gitlapp.data.repositories.ProjectRepository;
import de.hdmstuttgart.gitlapp.viewmodels.CreateIssueViewModel;
import de.hdmstuttgart.gitlapp.viewmodels.IssueDetailViewModel;
import de.hdmstuttgart.gitlapp.viewmodels.IssueOverviewViewModel;
import de.hdmstuttgart.gitlapp.viewmodels.LoginViewModel;
import de.hdmstuttgart.gitlapp.viewmodels.ProjectsViewModel;
import de.hdmstuttgart.gitlapp.viewmodels.SettingsViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    private final ProfileRepository profileRepository;

    public ViewModelFactory(IssueRepository issueRepository, ProjectRepository projectRepository, ProfileRepository profileRepository){
        this.issueRepository = issueRepository;
        this.projectRepository = projectRepository;
        this.profileRepository = profileRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(CreateIssueViewModel.class)){
            CreateIssueViewModel createIssueViewModel = new CreateIssueViewModel(issueRepository,projectRepository);
            return (T) createIssueViewModel;
        }else  if(modelClass.isAssignableFrom(IssueDetailViewModel.class)){
            IssueDetailViewModel issueDetailViewModel = new IssueDetailViewModel(issueRepository);
            return (T) issueDetailViewModel;
        } else if(modelClass.isAssignableFrom(IssueOverviewViewModel.class)){
            IssueOverviewViewModel issueOverviewViewModel = new IssueOverviewViewModel(issueRepository);
            return (T) issueOverviewViewModel;
        } else  if(modelClass.isAssignableFrom(LoginViewModel.class)){
            LoginViewModel loginViewModel = new LoginViewModel(profileRepository);
            return (T) loginViewModel;
        } else if(modelClass.isAssignableFrom(ProjectsViewModel.class)){
            ProjectsViewModel projectsViewModel = new ProjectsViewModel(projectRepository , profileRepository);
            return (T) projectsViewModel;
        } else if (modelClass.isAssignableFrom(SettingsViewModel.class)) {
            SettingsViewModel settingsViewModel = new SettingsViewModel(profileRepository, projectRepository);
            return (T) settingsViewModel;
        }
        throw new IllegalArgumentException("ViewModel class not found");
    }
}
