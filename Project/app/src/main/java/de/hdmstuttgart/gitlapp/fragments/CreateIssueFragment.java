package de.hdmstuttgart.gitlapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.hdmstuttgart.gitlapp.CustomApplication;
import de.hdmstuttgart.gitlapp.databinding.FragmentCreateIssueBinding;
import de.hdmstuttgart.gitlapp.dependencies.AppContainer;
import de.hdmstuttgart.gitlapp.models.Label;
import de.hdmstuttgart.gitlapp.models.Milestone;
import de.hdmstuttgart.gitlapp.viewmodels.CreateIssueViewModel;
import de.hdmstuttgart.gitlapp.viewmodels.vmpFactories.CreateIssueViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateIssueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateIssueFragment extends Fragment {

    FragmentCreateIssueBinding binding;
    CreateIssueViewModel viewModel;

    MutableLiveData<String> networkCallMessage;

    public CreateIssueFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateIssueFragment.
     */
    public static CreateIssueFragment newInstance(String param1, String param2) {
        return new CreateIssueFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppContainer container = ((CustomApplication) getActivity().getApplication())
                .getAppContainer(getActivity().getApplicationContext());

        CreateIssueViewModelFactory factory = container.createIssueViewModelFactory;

        viewModel = new ViewModelProvider(this, factory)
                .get(CreateIssueViewModel.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateIssueBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // - - - - - fill the spinner widgets with content - - - - -
        Spinner milestoneSpinner = binding.milestoneSpinner;
        Spinner labelSpinner = binding.labelSpinner; //todo make it possible to select no milestone or label

        List<String> projectMilestones = viewModel.getProjectMilestoneNames(7124); //todo make not hardcoded
        ArrayAdapter<String> milestoneAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, projectMilestones);

        List<String> projectLabels = viewModel.getProjectLabelsNames(7124);
        ArrayAdapter<String> labelAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,projectLabels);

        milestoneSpinner.setAdapter(milestoneAdapter);
        labelSpinner.setAdapter(labelAdapter);

        // - - - - - action listeners - - - - -
        binding.createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = binding.inputIssueTitle.getEditText().getText().toString();
                String description = binding.inputIssueDescription.getEditText().getText().toString();
                String dueDate = binding.inputIssueDuedate.getEditText().getText().toString();
                int weight = Integer.parseInt(binding.inputIssueWeight.getEditText().getText().toString()); //todo number format exception on empty string

                if(!title.isEmpty()){
                    viewModel.postNewIssue(7124,title,description,null,2,0,null); //todo implement labels and milestones
                }else{
                    Toast.makeText(getActivity(),"Title can not be empty", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}