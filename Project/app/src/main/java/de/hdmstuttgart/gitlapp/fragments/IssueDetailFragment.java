package de.hdmstuttgart.gitlapp.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
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

import de.hdmstuttgart.gitlapp.AppContainer;
import de.hdmstuttgart.gitlapp.CustomApplication;
import de.hdmstuttgart.gitlapp.R;
import de.hdmstuttgart.gitlapp.databinding.FragmentIssueDetailBinding;
import de.hdmstuttgart.gitlapp.models.Issue;
import de.hdmstuttgart.gitlapp.models.Label;
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
        AppContainer appContainer = ((CustomApplication) getActivity().getApplication())
                .getContainer(getActivity().getApplicationContext());

        appContainer.issueRepository.initProjectIssues(7124);//todo remove

        IssueDetailViewModelFactory issueDetailViewModelFactory = new IssueDetailViewModelFactory(appContainer.issueRepository);

        viewModel = new ViewModelProvider(this, issueDetailViewModelFactory)
                .get(IssueDetailViewModel.class);

        setLiveData();
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
    }

    /**
     * call method from viewModel to init the data related with this view/fragment
     */
    private void setLiveData() {
        viewModel.refreshData();//todo remove (will be made by issueOverviewDetail)
        this.issueLiveData = viewModel.getIssueDetailLiveData(issueId);
    }


    private void bindDataToViews(){

        //todo add show all screen

        try{

            // - - - - set simple views (text views) - - - -
            binding.issueTitle.setText(issueLiveData.getValue().getTitle());
            binding.issueDescriptionContent.setText(issueLiveData.getValue().getDescription());
            binding.weightChip.setText(String.valueOf(issueLiveData.getValue().getWeight()));
            binding.milestoneTitle.setText(issueLiveData.getValue().getMilestone().getTitle());
            binding.authorCard.authorName.setText(issueLiveData.getValue().getAuthor().getName());
            binding.authorCard.createDate.setText(getString(R.string.issue_create_date,issueLiveData.getValue().getCreated_at()));
            binding.thumbsUp.thumbsUpCount.setText(String.valueOf(issueLiveData.getValue().getThumbs_up()));
            binding.thumbsDown.thumbsDownCount.setText(String.valueOf(issueLiveData.getValue().getThumbs_down()));
            binding.issueIid.setText(getString(R.string.issue_iid,issueLiveData.getValue().getIid()));

            // - - - -  set due date if not null - - - -
            if(issueLiveData.getValue().getDue_date() == null){
                binding.dueDate.setText(getString(R.string.issue_due_date,"not set"));
            }else{
                binding.dueDate.setText(getString(R.string.issue_due_date,issueLiveData.getValue().getDue_date()));
            }

            // - - - - set state chip text and color- - - -
            if(issueLiveData.getValue().getState().equals("opened")){
                binding.statusChip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(),R.color.green)));
            }
            binding.statusChip.setText(issueLiveData.getValue().getState());


            // - - - - set label list - - - -
            List<Label> labels = issueLiveData.getValue().getLabels();

            for (Label label: labels){

                Chip labelChip = new Chip(getActivity());

                labelChip.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));

                labelChip.setText(label.getName());
                labelChip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(label.getColor())));
                binding.labelContainer.addView(labelChip);
            }

            // - - - - set author avatar - - - -
            Glide.with(this)
                    .load(issueLiveData.getValue().getAuthor().getAvatar_url())
                    .error(R.drawable.ic_gitlab_icon)
                    .into(binding.authorCard.avatar);


        }catch (NullPointerException e){
            Log.d("Api",e.getMessage());
            Toast.makeText(getActivity(),"Error loading data",Toast.LENGTH_SHORT).show();
        }
    }
}