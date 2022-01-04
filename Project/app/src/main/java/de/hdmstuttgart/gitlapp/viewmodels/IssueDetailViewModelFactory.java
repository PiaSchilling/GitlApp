package de.hdmstuttgart.gitlapp.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import de.hdmstuttgart.gitlapp.data.repositories.IssueRepository;

public class IssueDetailViewModelFactory implements ViewModelProvider.Factory { //todo maybe remove and only use viewModel provider

    private final IssueRepository issueRepository;

    public IssueDetailViewModelFactory(IssueRepository issueRepository){
        this.issueRepository = issueRepository;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(IssueDetailViewModel.class)){
             IssueDetailViewModel issueDetailViewModel = new IssueDetailViewModel(issueRepository);
             return (T) issueDetailViewModel;
        }
        throw new IllegalArgumentException("ViewModel class not found");
    }
}
