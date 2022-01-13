package de.hdmstuttgart.gitlapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdmstuttgart.gitlapp.dependencies.AppContainer;
import de.hdmstuttgart.gitlapp.CustomApplication;
import de.hdmstuttgart.gitlapp.databinding.FragmentProjectsBinding;
import de.hdmstuttgart.gitlapp.fragments.adapters.ProjectListAdapter;
import de.hdmstuttgart.gitlapp.models.Project;
import de.hdmstuttgart.gitlapp.models.User;
import de.hdmstuttgart.gitlapp.viewmodels.ProjectsViewModel;
import de.hdmstuttgart.gitlapp.viewmodels.vmpFactories.ProjectsViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String projectName;



    // bindings and viewmodel
    private FragmentProjectsBinding binding;
    private ProjectsViewModel projectsViewModel;
    private ProjectListAdapter adapter;

    // views
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ImageView imageView;
    private TextView toolbarTitle;
    private TextView user;
    private SwipeRefreshLayout swipeRefresh;


    //data
    private MutableLiveData<List<Project>> projectsLiveData;
    private MutableLiveData<String> networkCallMessage;
    List<Project> projectList = new ArrayList<>();


    public ProjectsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProjectsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectsFragment newInstance() {
        ProjectsFragment fragment = new ProjectsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        AppContainer container = ((CustomApplication) getActivity().getApplication())
                .getAppContainer(getActivity().getApplicationContext());

        // --- get related view model

        // wird noch ausgetauscht zu container.getFactory()
        ProjectsViewModelFactory factory = new ProjectsViewModelFactory(container.projectRepository, container.profileRepository);
        projectsViewModel = new ViewModelProvider(this, factory).get(ProjectsViewModel.class);

        // --- get data from view model
        projectsViewModel.initProjectsLiveData();
        projectsLiveData = projectsViewModel.getMutableLiveData();
        projectList.addAll(projectsLiveData.getValue());
        Log.e("ST-", "ProjectsLiveData in onCreate() " + projectsLiveData.getValue().toString());

        networkCallMessage = projectsViewModel.getMessage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProjectsBinding.inflate(inflater, container, false);
        return binding.getRoot();    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = binding.projectsRecyclerView;

        // create and add adapter
        addListAdapter();

        // user Card
        imageView = binding.userCard.projectAvatar;
        user = binding.userCard.userNameLabel;

        user.setText(projectsViewModel.getLoggedInUserName());

        Glide.with(getContext())
                .load(projectsViewModel.getLoggedInUserAvatar())
                .into(imageView);


        // swipe to refresh
        swipeRefresh = binding.swipeRefreshProject;
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                projectsViewModel.refreshProjects();
                Toast.makeText(getActivity(), "Projects loading", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
        });

        networkCallMessage.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                swipeRefresh.setRefreshing(false);
            }
        });


        // - - - - set listeners  - - - -
        //(bc getViewLifecycleOwner() it can be done only after onCreateView)
        Log.e("ST-", "ProjectLiveData in onViewCreated() " + projectsLiveData.getValue().toString());

        projectsLiveData.observe(getViewLifecycleOwner(), changeList -> {
            projectList.clear();
            projectList.addAll(changeList);
            adapter.notifyDataSetChanged();
            Log.e("ST-", "ProjectList in onViewCreated() " + projectList.toString());
        });
    }


    private void addListAdapter() {
        Log.e("ST-", "ProjectList in addListAdapter() " + projectList.toString());
        adapter = new ProjectListAdapter(projectList, getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


}