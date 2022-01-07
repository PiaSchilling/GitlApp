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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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

    //bundle data
    private static final String ARG_PARAM1 = "projectId";
    private static final String ARG_PARAM2 = "projectName";
    private int projectId;
    private String projectName;

    //bindings and viewModel
    private FragmentIssueOverviewBinding binding;
    private IssueOverviewViewModel viewModel;
    private IssueListAdapter adapter;

    //views
    private MaterialToolbar toolbar;
    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private FloatingActionButton button;
    private SwipeRefreshLayout swipeRefreshLayout;

    //data
    private MutableLiveData<List<Issue>> issueListLiveData;
    private MutableLiveData<String> networkCallMessage; //inform the user of update fail/success
    List<Issue> issueList = new ArrayList<>();



    public IssueOverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param projectId the id of the project the overview is for to get the right issues
     * @param projectName the id of the projectName to show it in the app bar
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
        networkCallMessage = viewModel.getMessage();

    }

    @Override // Inflate the layout for this fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentIssueOverviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override // set data and click listeners to the views
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = binding.toolbar;
        tabLayout = binding.tabLayout;
        recyclerView = binding.recyclerView;
        button = binding.addIssueButton;
        swipeRefreshLayout = binding.swipeRefresh;

        toolbar.setTitle(getString(R.string.bar_title, projectName));

        //create and add adapter
        addListAdapter();

        // - - - - set listeners  - - - -
        //(bc getViewLifecycleOwner() it can be done only after onCreateView)
        issueListLiveData.observe(getViewLifecycleOwner(), changeList -> {
            issueList.clear();
            issueList.addAll(changeList);
            issueList.sort(Comparator.comparingInt(Issue::getIid).reversed());
            adapter.notifyDataSetChanged(); //todo replace with more efficient
        });

        networkCallMessage.observe(getViewLifecycleOwner(), s -> {
            Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
            swipeRefreshLayout.setRefreshing(false); //when receive message update failed/success -> stop refreshing
        });

        //refresh data on swipe
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getActivity(),"try to update",Toast.LENGTH_SHORT).show();
                viewModel.updateIssueLiveData();
                adapter.notifyDataSetChanged();
            }
        });

        // define action when tabs are changed (sort list)
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try{
                    String state = Objects.requireNonNull(tab.getText()).toString();

                    if (state.equals("open")) {
                        viewModel.filterIssuesByState("opened");
                    }else if(state.equals("closed")){
                        viewModel.filterIssuesByState("closed");
                    }else{
                        viewModel.clearIssueFilter();
                    }
                }catch (NullPointerException e){
                    Log.e("Api",e.getMessage());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //not needed for now
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //not needed for now
            }
        });
    }

    //created list adapter and sets it to the recyclerview
    private void addListAdapter() {
        adapter = new IssueListAdapter(issueList, getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}