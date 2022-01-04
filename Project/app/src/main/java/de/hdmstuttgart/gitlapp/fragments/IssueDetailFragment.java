package de.hdmstuttgart.gitlapp.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.hdmstuttgart.gitlapp.AppContainer;
import de.hdmstuttgart.gitlapp.CustomApplication;
import de.hdmstuttgart.gitlapp.R;
import de.hdmstuttgart.gitlapp.databinding.FragmentIssueDetailBinding;
import de.hdmstuttgart.gitlapp.models.Issue;
import de.hdmstuttgart.gitlapp.viewmodels.IssueDetailViewModel;
import de.hdmstuttgart.gitlapp.viewmodels.IssueDetailViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IssueDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IssueDetailFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ISSUE_ID = "issueId";

    private int issueId;

    private MutableLiveData<Issue> issueLiveData = new MutableLiveData<>();
    private IssueDetailViewModel viewModel;
    public IssueDetailViewModelFactory issueDetailViewModelFactory;

    FragmentIssueDetailBinding binding;

    public IssueDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param issueId id of the issue the fragment represents
     * @return A new instance of fragment IssueDetailFragment.
     */
    public static IssueDetailFragment newInstance(int issueId) {
        IssueDetailFragment fragment = new IssueDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ISSUE_ID, issueId);
        Log.d("Api","args: " + args.get(ISSUE_ID));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            issueId = getArguments().getInt(ISSUE_ID);
        }

        //- - - get the view model - - -

        AppContainer appContainer = ((CustomApplication) getActivity().getApplication())
                .getContainer(getActivity().getApplicationContext());

        appContainer.issueRepository.initData(7124);//todo remove

        issueDetailViewModelFactory = new IssueDetailViewModelFactory(appContainer.issueRepository,issueId);

        viewModel = new ViewModelProvider(this, issueDetailViewModelFactory)
                .get(IssueDetailViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentIssueDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_issue_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViewData();
        binding.issueTitle.setText(issueLiveData.getValue().getTitle());
        binding.issueDescriptionContent.setText(issueLiveData.getValue().getDescription());
        binding.weightChip.setText(String.valueOf(issueLiveData.getValue().getWeight()));
        binding.dueDate.setText(getString(R.string.issue_due_date,issueLiveData.getValue().getDue_date()));
        binding.milestoneTitle.setText(issueLiveData.getValue().getMilestone().getTitle());
        binding.authorCard.authorName.setText(issueLiveData.getValue().getAuthor().getName());
        binding.authorCard.createDate.setText(getString(R.string.issue_create_date,issueLiveData.getValue().getCreated_at()));
        binding.thumbsUp.thumbsUpCount.setText(String.valueOf(issueLiveData.getValue().getThumbs_up()));
        binding.thumbsDown.thumbsDownCount.setText(String.valueOf(issueLiveData.getValue().getThumbs_down()));

        if(issueLiveData.getValue().getState().equals("opened")){
            binding.statusChip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(),R.color.green)));
        }
        binding.statusChip.setText(issueLiveData.getValue().getState()); //todo green when opened


    }

    private void initViewData() {
        viewModel.refreshData();//todo remove
        Log.d("Api","InitViewModel for issueId " + issueId);
        this.issueLiveData = viewModel.getIssueDetailLiveData(issueId);
        Log.d("Api","initViewModel, loaded Data " + issueLiveData.getValue().toString());
    }
}