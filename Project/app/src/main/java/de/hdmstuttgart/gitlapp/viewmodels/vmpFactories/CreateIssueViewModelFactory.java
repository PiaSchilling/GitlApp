package de.hdmstuttgart.gitlapp.viewmodels.vmpFactories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import de.hdmstuttgart.gitlapp.data.repositories.IssueRepository;
import de.hdmstuttgart.gitlapp.data.repositories.ProjectRepository;
import de.hdmstuttgart.gitlapp.viewmodels.CreateIssueViewModel;
import de.hdmstuttgart.gitlapp.viewmodels.IssueDetailViewModel;

public class CreateIssueViewModelFactory implements ViewModelProvider.Factory {

    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;

    public CreateIssueViewModelFactory(IssueRepository issueRepository, ProjectRepository projectRepository){
        this.issueRepository = issueRepository;
        this.projectRepository = projectRepository;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(CreateIssueViewModel.class)){
            CreateIssueViewModel createIssueViewModel = new CreateIssueViewModel(issueRepository,projectRepository);
            return (T) createIssueViewModel;
        }
        throw new IllegalArgumentException("ViewModel class not found");
    }
}
