package de.hdmstuttgart.gitlapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdmstuttgart.gitlapp.R;
import de.hdmstuttgart.gitlapp.data.network.NetworkStatus;
import de.hdmstuttgart.gitlapp.dependencies.AppContainer;
import de.hdmstuttgart.gitlapp.CustomApplication;
import de.hdmstuttgart.gitlapp.databinding.FragmentProjectsBinding;
import de.hdmstuttgart.gitlapp.fragments.adapters.OnProjectClickListener;
import de.hdmstuttgart.gitlapp.fragments.adapters.ProjectListAdapter;
import de.hdmstuttgart.gitlapp.models.Project;
import de.hdmstuttgart.gitlapp.viewmodels.ProjectsViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectsFragment extends Fragment implements OnProjectClickListener {

    private Context context;


    // bindings and viewModel
    private FragmentProjectsBinding binding;
    private ProjectsViewModel projectsViewModel;
    private ProjectListAdapter adapter;

    // views
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;


    //data
    private MutableLiveData<List<Project>> projectsLiveData;
    private final List<Project> projectList = new ArrayList<>();


    public ProjectsFragment() {
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

        // - - - - get the related view model - - - - -
        AppContainer container = ((CustomApplication) context.getApplicationContext())
                .getAppContainer(context.getApplicationContext());

        projectsViewModel = new ViewModelProvider(this, container.viewModelFactory)
                .get(ProjectsViewModel.class);

        // - - - - - get data from view model - - - - -
        projectsViewModel.initProjectsLiveData();
        projectsLiveData = projectsViewModel.getMutableLiveData();
        projectList.addAll(Objects.requireNonNull(projectsLiveData.getValue()));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProjectsBinding.inflate(inflater, container, false);
        return binding.getRoot();    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = binding.projectsRecyclerView;

        // create and add adapter
        addListAdapter();

        // user Card
        ImageView imageView = binding.userCard.projectAvatar;
        TextView user = binding.userCard.userNameLabel;

        user.setText(projectsViewModel.getLoggedInUserName());
        Glide.with(context)
                .load(projectsViewModel.getLoggedInUserAvatar())
                .into(imageView);

        // get to the settings fragment
        ImageView settingsButton = binding.toolbarSettingsButton;
        settingsButton.setOnClickListener(v -> getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, SettingsFragment.class, null)
                .addToBackStack(null)
                .commit());


        // swipe to refresh
        swipeRefresh = binding.swipeRefreshProject;
        swipeRefresh.setOnRefreshListener(() -> {
            projectsViewModel.refreshProjects();
            adapter.notifyDataSetChanged();
        });

        projectsViewModel.getMessage().observe(getViewLifecycleOwner(), s -> {
            if(s != NetworkStatus.SUCCESS){ //needed to stop refresh layout but should not be displayed as a toast
                Toast.makeText(getActivity(), s.message, Toast.LENGTH_SHORT).show();
            }
            swipeRefresh.setRefreshing(false);
        });

        projectsLiveData.observe(getViewLifecycleOwner(), changeList -> {
            projectList.clear();
            projectList.addAll(changeList);
            adapter.notifyDataSetChanged();
            recyclerView.scheduleLayoutAnimation();
        });
    }


    private void addListAdapter() {
        adapter = new ProjectListAdapter(projectList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.scheduleLayoutAnimation();
    }


    @Override
    public void onProjectClick(Project project) {
        Bundle bundle = new Bundle();
        bundle.putString("projectName", project.getName());
        bundle.putInt("projectId", project.getId());

       getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, IssueOverviewFragment.class, bundle)
                .addToBackStack(null)
                .commit();
    }
}