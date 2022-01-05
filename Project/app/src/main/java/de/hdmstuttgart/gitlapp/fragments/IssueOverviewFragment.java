package de.hdmstuttgart.gitlapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdmstuttgart.gitlapp.AppContainer;
import de.hdmstuttgart.gitlapp.CustomApplication;
import de.hdmstuttgart.gitlapp.R;
import de.hdmstuttgart.gitlapp.databinding.FragmentIssueOverviewBinding;
import de.hdmstuttgart.gitlapp.fragments.adapters.IssueListAdapter;
import de.hdmstuttgart.gitlapp.models.Issue;
import de.hdmstuttgart.gitlapp.viewmodels.IssueOverviewViewModel;
import de.hdmstuttgart.gitlapp.viewmodels.IssueOverviewViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IssueOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IssueOverviewFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "projectId";
    private static final String ARG_PARAM2 = "projectName";

    private int projectId;
    private String projectName;

    private FragmentIssueOverviewBinding binding;
    private IssueOverviewViewModel viewModel;

    private MaterialToolbar toolbar;
    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private FloatingActionButton button;

    private MutableLiveData<List<Issue>> issueListLiveData;
    List<Issue> issueList = new ArrayList<>();
    IssueListAdapter adapter;


    public IssueOverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param projectId the id of the project the overview is for
     * @return A new instance of fragment IssueOverviewFragment.
     */
    public static IssueOverviewFragment newInstance(int projectId, String projectName) {
        IssueOverviewFragment fragment = new IssueOverviewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, projectId);
        args.putString(ARG_PARAM2, projectName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectId = getArguments().getInt(ARG_PARAM1);
            projectName = getArguments().getString(ARG_PARAM2);
            Log.d("Api", "ProjectId" + projectId);
        }

        // - - - -  get the related view model - - - -
        AppContainer container = ((CustomApplication) getActivity().getApplication())
                .getContainer(getActivity().getApplicationContext());

        IssueOverviewViewModelFactory factory = new IssueOverviewViewModelFactory(container.issueRepository, projectId);

        viewModel = new ViewModelProvider(this, factory)
                .get(IssueOverviewViewModel.class);

        //  - - - - get data from view model - - - -
        issueListLiveData = viewModel.getMutableLiveData();

    }

    @Override        // Inflate the layout for this fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentIssueOverviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = binding.toolbar;
        tabLayout = binding.tabLayout;
        recyclerView = binding.recyclerView;
        button = binding.addIssueButton;

        toolbar.setTitle(getString(R.string.bar_title, projectName));

        // - - - - create and add adapter - - - - -
        addListAdapter();

        // - - - - define action when data changes - - - -
        issueListLiveData.observe(getViewLifecycleOwner(), new Observer<List<Issue>>() {
            @Override
            public void onChanged(List<Issue> changeList) {
                issueList.clear();
                issueList.addAll(changeList);
                adapter.notifyDataSetChanged();
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("Api", tab.getText().toString());
                String state = tab.getText().toString();
                Log.d("WASS",state);

                if (state.equals("open")) {
                    viewModel.filterIssuesByState("opened");
                    Log.d("WAAS",issueListLiveData.getValue().toString());
                }else if(state.equals("closed")){
                    viewModel.filterIssuesByState("closed");
                }else{
                    viewModel.clearIssueFilter();
                    Log.d("WAAS",issueListLiveData.getValue().toString());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void addListAdapter() {
        adapter = new IssueListAdapter(issueList, getActivity());
        Log.d("Api", "apadter list " + issueListLiveData.getValue().hashCode());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}