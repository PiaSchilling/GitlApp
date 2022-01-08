package de.hdmstuttgart.gitlapp.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import de.hdmstuttgart.gitlapp.data.repositories.IssueRepository;

public class IssueOverviewViewModelFactory implements ViewModelProvider.Factory {

    private final IssueRepository issueRepository;

    public IssueOverviewViewModelFactory(IssueRepository issueRepository){
        this.issueRepository = issueRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(IssueOverviewViewModel.class)){
            IssueOverviewViewModel issueOverviewViewModel = new IssueOverviewViewModel(issueRepository);
            return (T) issueOverviewViewModel;
        }
        throw new IllegalArgumentException("ViewModel class not found");
    }
}
