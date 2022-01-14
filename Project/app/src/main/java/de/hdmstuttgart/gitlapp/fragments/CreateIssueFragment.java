package de.hdmstuttgart.gitlapp.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

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

        // - - - - - label selection - - - - -
        ChipGroup labelGroup = binding.labelChipGroup;
        final List<String> selectedLabels = new ArrayList<>();

        // add labels to group
        List<Label> labels = viewModel.getProjectLabels(7124);
        for (Label label: labels){

            Chip labelChip = new Chip(getActivity());
            labelChip.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            labelChip.setText(label.getName());
            labelChip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(label.getColor())));
            labelChip.setCheckable(true);
            labelChip.setCheckedIconVisible(true);

            labelChip.setOnCheckedChangeListener((compoundButton, b) -> {
                if(b){
                    selectedLabels.add(labelChip.getText().toString());
                }else{
                    selectedLabels.remove(labelChip.getText().toString());
                }
            });
            labelGroup.addView(labelChip);
        }



        // - - - - - action listeners - - - - -
        binding.createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("Label", selectedLabels.toString());

                String title = binding.inputIssueTitle.getEditText().getText().toString();
                String description = binding.inputIssueDescription.getEditText().getText().toString();
                String dueDate = binding.inputIssueDuedate.getEditText().getText().toString();
                int weight = Integer.parseInt(binding.inputIssueWeight.getEditText().getText().toString()); //todo number format exception on empty string

                if(!title.isEmpty()){
                    viewModel.postNewIssue(7124,title,description,null,2,0,selectedLabels); //todo implement labels and milestones
                }else{
                    Toast.makeText(getActivity(),"Title can not be empty", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}