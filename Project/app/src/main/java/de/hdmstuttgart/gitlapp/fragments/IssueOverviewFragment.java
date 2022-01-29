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

import java.util.Objects;

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
 * Use the {@link IssueOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IssueOverviewFragment extends Fragment implements OnIssueClickListener {

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
    private MutableLiveData<String> networkCallMessage; //inform the user of update fail/success

    //tab filtering, all per default/on fragment start
    private String tabFilter = "all";//todo move to viewModel


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
        super.onCreate(savedInstanceState); //todo this whole method should ideal only called once
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
        viewModel.getMutableLiveData().observe(getViewLifecycleOwner(), changeList -> {
            showFilteredIssueList();
        });

        viewModel.getMessage().observe(getViewLifecycleOwner(), s -> {
            if(s.equals("loading")){
                binding.progressSpinnerIssue.setVisibility(View.VISIBLE);
            }else if (Objects.equals(s,"Update successful")){
                binding.progressSpinnerIssue.setVisibility(View.GONE);
            }else{
                binding.progressSpinnerIssue.setVisibility(View.GONE);
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show(); //todo test if error toast is displayed
            }
            swipeRefreshLayout.setRefreshing(false); //when receive message update failed/success -> stop refreshing
        });

        //refresh data on swipe
        swipeRefreshLayout.setOnRefreshListener(() -> viewModel.updateIssueLiveData(projectId));

        // define action when tabs are changed (filter list)
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() { //todo add selected tab to viewModel (config change aware)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {
                    String state = Objects.requireNonNull(tab.getText()).toString();
                    setFilter(state);
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

        binding.addIssueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("projectId", projectId);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, CreateIssueFragment.class, bundle)
                        .addToBackStack(null)
                        .commit();
            }
        });

        setListPagination();
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

    private void setListPagination(){
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE){
                    Log.d("Page","Reached the end");
                    viewModel.incrementPageNumber();
                    viewModel.updateIssueLiveData(projectId);
                }
            }
        });
    }

    @Override
    public void onIssueClick(Issue issue) {
        Bundle bundle = new Bundle();
        bundle.putInt("issueId",issue.getId());

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, IssueDetailFragment.class, bundle)
                .addToBackStack(null)
                .commit();
    }
}