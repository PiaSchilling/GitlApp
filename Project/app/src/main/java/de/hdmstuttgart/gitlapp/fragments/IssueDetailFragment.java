package de.hdmstuttgart.gitlapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import de.hdmstuttgart.gitlapp.AppContainer;
import de.hdmstuttgart.gitlapp.CustomApplication;
import de.hdmstuttgart.gitlapp.databinding.FragmentIssueDetailBinding;
import de.hdmstuttgart.gitlapp.models.Issue;
import de.hdmstuttgart.gitlapp.viewmodels.IssuesViewModel;

public class IssueDetailFragment extends Fragment {

    private FragmentIssueDetailBinding binding;
    IssuesViewModel issuesViewModel;

    MutableLiveData<Issue> issueMutableLiveData = new MutableLiveData<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentIssueDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        AppContainer appContainer = ((CustomApplication) getActivity().getApplication()).getContainer(getActivity().getApplicationContext());
        issuesViewModel = new IssuesViewModel(appContainer.issueRepository,7124);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.issueTitle.setText("NAME");

    }
}
