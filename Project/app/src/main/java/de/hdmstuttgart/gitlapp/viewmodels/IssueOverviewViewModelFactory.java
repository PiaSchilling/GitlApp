package de.hdmstuttgart.gitlapp.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import de.hdmstuttgart.gitlapp.data.repositories.IssueRepository;

public class IssueOverviewViewModelFactory implements ViewModelProvider.Factory {

    private final IssueRepository issueRepository;
    private final int projectId;

    public IssueOverviewViewModelFactory(IssueRepository issueRepository, int projectId){
        this.issueRepository = issueRepository;
        this.projectId = projectId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(IssueOverviewViewModel.class)){
            IssueOverviewViewModel issueOverviewViewModel = new IssueOverviewViewModel(issueRepository,projectId);
            return (T) issueOverviewViewModel;
        }
        throw new IllegalArgumentException("ViewModel class not found");
    }
}
