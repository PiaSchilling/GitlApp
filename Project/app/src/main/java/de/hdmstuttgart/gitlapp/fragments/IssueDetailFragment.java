package de.hdmstuttgart.gitlapp.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;

import java.util.List;
import java.util.Objects;

import de.hdmstuttgart.gitlapp.dependencies.AppContainer;
import de.hdmstuttgart.gitlapp.CustomApplication;
import de.hdmstuttgart.gitlapp.R;
import de.hdmstuttgart.gitlapp.databinding.FragmentIssueDetailBinding;
import de.hdmstuttgart.gitlapp.models.Issue;
import de.hdmstuttgart.gitlapp.models.Label;
import de.hdmstuttgart.gitlapp.viewmodels.IssueDetailViewModel;

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


    private FragmentIssueDetailBinding binding;

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

        //- - - - get the view model and init data - - - -
        AppContainer container = ((CustomApplication) getActivity().getApplication())
                .getAppContainer(getActivity().getApplicationContext());

        viewModel = new ViewModelProvider(this, container.viewModelFactory)
                .get(IssueDetailViewModel.class);

        this.issueLiveData = viewModel.getIssueDetailLiveData(issueId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentIssueDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindDataToViews();

        issueLiveData.observe(getViewLifecycleOwner(), issue -> {
            Log.d("State","OnChanged triggered");
            setStateChip();
        });
    }


    //todo split up in multiple methods
    private void bindDataToViews() {

        try {

            // - - - - set views which can not be null - - - -
            binding.issueIid.setText(getString(R.string.issue_iid, issueLiveData.getValue().getIid()));
            binding.issueTitle.setText(issueLiveData.getValue().getTitle());
            binding.authorCard.createDate.setText(getString(R.string.issue_create_date, issueLiveData.getValue().getCreated_at()));
            binding.thumbsUp.thumbsUpCount.setText(String.valueOf(issueLiveData.getValue().getThumbs_up()));
            binding.thumbsDown.thumbsDownCount.setText(String.valueOf(issueLiveData.getValue().getThumbs_down()));
            binding.weightChip.setText(String.valueOf(issueLiveData.getValue().getWeight()));
            binding.authorCard.authorName.setText(issueLiveData.getValue().getAuthor().getName());

            // - - - - set view which can be null or empty (if null set default value) - - - -
            if (issueLiveData.getValue().getDescription() == null || issueLiveData.getValue().getDescription().isEmpty()) {
                binding.issueDescriptionContent.setText("no description");
            } else {
                binding.issueDescriptionContent.setText(issueLiveData.getValue().getDescription());
            }

            if (issueLiveData.getValue().getMilestone() == null) {
                binding.milestoneTitle.setText("milestone name not set");
            } else {
                binding.milestoneTitle.setText(issueLiveData.getValue().getMilestone().getTitle());
            }

            if (issueLiveData.getValue().getDue_date() == null) {
                binding.dueDate.setText(getString(R.string.issue_due_date, "not set"));
            } else {
                binding.dueDate.setText(getString(R.string.issue_due_date, issueLiveData.getValue().getDue_date()));
            }

            // - - - - set state chip text and color- - - -
            setStateChip();


            // - - - - set label list - - - -
            List<Label> labels = issueLiveData.getValue().getLabels();

            for (Label label : labels) {

                Chip labelChip = new Chip(getActivity());

                labelChip.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));

                labelChip.setText(label.getName());
                labelChip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(label.getColor())));
                labelChip.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.white)));
                binding.labelContainer.addView(labelChip);
            }

            // - - - - set author avatar - - - -
            Glide.with(this)
                    .load(issueLiveData.getValue().getAuthor().getAvatar_url())
                    .error(R.drawable.ic_gitlab_icon)
                    .into(binding.authorCard.avatar);


        } catch (NullPointerException e) {
            Log.e("Api", e.getMessage());
            Toast.makeText(getActivity(), "Error loading data", Toast.LENGTH_SHORT).show();
        }

        binding.closeIssueButton.setOnClickListener(view -> {
            try {
                int projectId = Objects.requireNonNull(issueLiveData.getValue()).getProject_id();
                int issueIid = issueLiveData.getValue().getIid();
                viewModel.closeIssue(projectId, issueIid);
            } catch (NullPointerException e) {
                Log.e("Api", e.getMessage());
            }
        });
    }

    private void setStateChip() {
        if (issueLiveData.getValue().getState().equals("opened")) {
            binding.statusChip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.green)));
        }else{
            binding.statusChip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.red)));
        }
        binding.statusChip.setText(issueLiveData.getValue().getState());
    }
}