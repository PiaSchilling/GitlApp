package de.hdmstuttgart.gitlapp.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import de.hdmstuttgart.gitlapp.data.repositories.IssueRepository;

public class IssueDetailViewModelFactory implements ViewModelProvider.Factory {

    private final IssueRepository issueRepository;
    private final int issueId;

    public IssueDetailViewModelFactory(IssueRepository issueRepository, int issueId){
        this.issueRepository = issueRepository;
        this.issueId = issueId;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(IssueDetailViewModel.class)){
             IssueDetailViewModel issueDetailViewModel = new IssueDetailViewModel(issueRepository,issueId);
             return (T) issueDetailViewModel;
        }
        throw new IllegalArgumentException("ViewModel class not found");
    }
}
