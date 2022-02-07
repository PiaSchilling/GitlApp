package de.hdmstuttgart.gitlapp.fragments;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import de.hdmstuttgart.gitlapp.data.network.NetworkStatus;
import de.hdmstuttgart.gitlapp.dependencies.AppContainer;
import de.hdmstuttgart.gitlapp.CustomApplication;
import de.hdmstuttgart.gitlapp.R;
import de.hdmstuttgart.gitlapp.databinding.FragmentIssueOverviewBinding;
import de.hdmstuttgart.gitlapp.fragments.adapters.IssueListAdapter;
import de.hdmstuttgart.gitlapp.fragments.adapters.OnIssueClickListener;
import de.hdmstuttgart.gitlapp.models.Issue;
import de.hdmstuttgart.gitlapp.viewmodels.IssueOverviewViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class IssueOverviewFragment extends Fragment implements OnIssueClickListener {

    private Context context;

    //bundle data
    private static final String ARG_PARAM1 = "projectId";
    private static final String ARG_PARAM2 = "projectName";
    private int projectId;
    private String projectName;

    //bindings and viewModel
    private FragmentIssueOverviewBinding binding;
    private IssueOverviewViewModel viewModel;
    private IssueListAdapter adapter;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    //tab filtering, all per default/on fragment start
    private String tabFilter = "all";   //todo move to viewModel
    private int selectedTab = 0; //default selected tab is 0 (all)


    public IssueOverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
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
        AppContainer container = ((CustomApplication) context.getApplicationContext())
                .getAppContainer(context.getApplicationContext());

        viewModel = new ViewModelProvider(this, container.viewModelFactory)
                .get(IssueOverviewViewModel.class);

        // - - - - -  init and update data
        viewModel.initIssueLiveData(projectId);
    }

    @Override // Inflate the layout for this fragment
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentIssueOverviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override // set data and click listeners to the views
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //views
        recyclerView = binding.recyclerView;
        swipeRefreshLayout = binding.swipeRefresh;
        binding.toolbar.setTitle(getString(R.string.bar_title, projectName));

        //create and add adapter
        addListAdapter();
        setListPagination();


        // - - - - set listeners  - - - -
        viewModel.getMutableLiveData().observe(getViewLifecycleOwner(), changeList -> showFilteredIssueList());

        viewModel.getMessage().observe(getViewLifecycleOwner(), s -> {
            if (s == NetworkStatus.LOADING) {
                binding.progressSpinnerIssue.setVisibility(View.VISIBLE);
            } else if (s == NetworkStatus.SUCCESS) {
                binding.addIssueButton.setEnabled(true);
                binding.progressSpinnerIssue.setVisibility(View.GONE);
            } else {
                binding.addIssueButton.setEnabled(false);
                binding.progressSpinnerIssue.setVisibility(View.GONE); //cant add issues when offline
                Toast.makeText(getActivity(), s.message, Toast.LENGTH_SHORT).show();
            }
            swipeRefreshLayout.setRefreshing(false); //when receive message update failed/success -> stop refreshing
        });

        //refresh data on swipe
        swipeRefreshLayout.setOnRefreshListener(() -> viewModel.updateIssueLiveData(projectId));

        //store the currently selected tab
        TabLayout.Tab tab = binding.tabLayout.getTabAt(selectedTab);
        if (tab != null) {
            tab.select();
        }

        // define action when tabs are changed (filter list)
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {
                    String state = Objects.requireNonNull(tab.getText()).toString();
                    setFilter(state);
                    selectedTab = tab.getPosition();
                    showFilteredIssueList();
                } catch (NullPointerException e) {
                    Log.e("Api", e.getMessage());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //not needed for now
            }
        });

        binding.addIssueButton.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putInt("projectId", projectId);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, CreateIssueFragment.class, bundle)
                    .addToBackStack(null)
                    .commit();
        });

    }

    /**
     * creates the list adapter and sets it to the recyclerview
     */
    private void addListAdapter() {
        adapter = new IssueListAdapter(null, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.scheduleLayoutAnimation();
    }

    /**
     * @param filterString the string which represents the selected state (open,closed,all)
     */
    private void setFilter(String filterString) {
        tabFilter = filterString;
    }

    /**
     * applies the currently set filter by removing the issues which have the contrary state
     * (do noting if all is selected)
     */
    private void showFilteredIssueList() {
        switch (tabFilter) {
            case "open":
                adapter.submitList(viewModel.filterIssuesByState("opened"));
                break;
            case "closed":
                adapter.submitList(viewModel.filterIssuesByState("closed"));
                break;
            case "all": /*do nothing but needed otherwise default will be chosen*/
                adapter.submitList(viewModel.getMutableLiveData().getValue());
                break;
            default:
                Log.e("Api", "No valid tab filter");
        }
    }

    private void setListPagination() {
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d("Page", "Reached the end");
                    viewModel.incrementPageNumber();
                    viewModel.updateIssueLiveData(projectId);
                }
            }
        });
    }

    @Override
    public void onIssueClick(Issue issue) {
        Bundle bundle = new Bundle();
        bundle.putInt("issueId", issue.getId());

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, IssueDetailFragment.class, bundle)
                .addToBackStack(null)
                .commit();
    }

}