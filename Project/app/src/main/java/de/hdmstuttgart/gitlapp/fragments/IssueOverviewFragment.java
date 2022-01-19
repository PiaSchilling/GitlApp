package de.hdmstuttgart.gitlapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
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

import de.hdmstuttgart.gitlapp.dependencies.AppContainer;
import de.hdmstuttgart.gitlapp.CustomApplication;
import de.hdmstuttgart.gitlapp.R;
import de.hdmstuttgart.gitlapp.databinding.FragmentIssueOverviewBinding;
import de.hdmstuttgart.gitlapp.fragments.adapters.IssueListAdapter;
import de.hdmstuttgart.gitlapp.models.Issue;
import de.hdmstuttgart.gitlapp.viewmodels.IssueOverviewViewModel;

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
    private final List<Issue> issueList = new ArrayList<>();

    //tab filtering
    private String tabFilter = "all";


    public IssueOverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param projectId   the id of the project the overview is for to get the right issues
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
                .getAppContainer(getActivity().getApplicationContext());

        viewModel = new ViewModelProvider(this, container.viewModelFactory)
                .get(IssueOverviewViewModel.class);

        // - - - - -  init and update data
        viewModel.initIssueLiveData(projectId);
        this.issueListLiveData = viewModel.updateIssueLiveData(projectId);

        //  - - - - get data from view model - - - -
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
            applyFilter();
            issueList.sort(Comparator.comparingInt(Issue::getIid).reversed());
            adapter.notifyDataSetChanged(); //todo replace with more efficient
        });

        networkCallMessage.observe(getViewLifecycleOwner(), s -> {
            Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
            swipeRefreshLayout.setRefreshing(false); //when receive message update failed/success -> stop refreshing
        });

        //refresh data on swipe
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getActivity(), "try to update", Toast.LENGTH_SHORT).show();
                viewModel.updateIssueLiveData(projectId);
                adapter.notifyDataSetChanged();
            }
        });

        // define action when tabs are changed (sort list)
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {
                    String state = Objects.requireNonNull(tab.getText()).toString();

                    if (state.equals("open")) {
                        // issueList.removeAll(viewModel.filterIssuesByState("closed"));
                        //adapter.notifyDataSetChanged();
                        setFilter("open");
                    } else if (state.equals("closed")) {
                        //issueList.removeAll(viewModel.filterIssuesByState("opened"));
                        //adapter.notifyDataSetChanged();
                        setFilter("closed");
                    }else if(state.equals("all")){
                        setFilter("all");
                    }
                } catch (NullPointerException e) {
                    Log.e("Api", e.getMessage());
                }
                applyFilter();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                issueList.clear();
                issueList.addAll(issueListLiveData.getValue());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //not needed for now
            }
        });

        binding.addIssueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("projectId", projectId);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, CreateIssueFragment.class, bundle)
                        .addToBackStack(null)
                        .commit();
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

    private void setFilter(String filterString) {
        tabFilter = filterString;
        Log.d("Filter", "Filter set");
    }

    private void applyFilter() {
        switch (tabFilter) {
            case "open":
                issueList.removeAll(viewModel.filterIssuesByState("closed"));
                Log.d("Filter","open");
                break;
            case "closed":
                issueList.removeAll(viewModel.filterIssuesByState("opened"));
                Log.d("Filter","closed");
                break;
            case "all": /*do nothing*/
                Log.d("Filter","all");
                break;
            default:
                Log.e("Api", "No valid tab filter");
        }
        adapter.notifyDataSetChanged();
    }
}