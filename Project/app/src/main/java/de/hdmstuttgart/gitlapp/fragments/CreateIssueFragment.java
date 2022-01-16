package de.hdmstuttgart.gitlapp.fragments;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Calendar;
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

    private static final String ARG_PARAM1 = "projectId";
    private int projectId;

    private FragmentCreateIssueBinding binding;
    private CreateIssueViewModel viewModel;

    private MutableLiveData<String> networkCallMessage;

    public CreateIssueFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param projectId the id of the project the issue should be created for
     * @return A new instance of fragment CreateIssueFragment.
     */
    public static CreateIssueFragment newInstance(int projectId) {
        CreateIssueFragment fragment = new CreateIssueFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, projectId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectId = getArguments().getInt(ARG_PARAM1);
        }

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

        // - - - - -  data observation - - - -
        networkCallMessage = viewModel.getMessage();
        networkCallMessage.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getActivity(), networkCallMessage.getValue(), Toast.LENGTH_SHORT).show();
                //todo if successful end fragment
            }
        });

        // - - - - - label and milestone selection - - - - -
        ChipGroup labelGroup = binding.labelChipGroup;
        ChipGroup milestoneGroup = binding.milestoneChipGroup;

        List<String> selectedLabels = new ArrayList<>();
        String selectedMilestone;

        //fill chip groups
        //label group
        List<Label> labels = viewModel.getProjectLabels(projectId);
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

        //milestone group
        List<Milestone> milestones = viewModel.getProjectMilestones(projectId);
        for (Milestone milestone : milestones){

            Chip milestoneChip = new Chip(getActivity());
            milestoneChip.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            milestoneChip.setCheckable(true);
            milestoneChip.setCheckedIconVisible(true);
            milestoneChip.setText(milestone.getTitle());
            milestoneGroup.addView(milestoneChip);
        }

        // - - - - - date  selection - - - - -
        DatePickerDialog.OnDateSetListener dateSetListener;
        final String[] dateString = new String[1]; //needs to be an array bc of lambda onDataSet

        //date picker for issue due date
        dateSetListener = (datePicker, year, month, day) -> {
            month += 1;
            Log.d("Date",year + "-" + month + "-" + day);
            dateString[0] = year + "-" + month + "-" + day;
        };


        binding.dueDateButton.setOnClickListener(view1 -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Material_Dialog,dateSetListener,year,month,day);
            dialog.setTitle("Choose issue due date");
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
            dialog.show();
        });


        // - - - - button listeners - - - -
        binding.createButton.setOnClickListener(view12 -> {

            Chip chip = milestoneGroup.findViewById(milestoneGroup.getCheckedChipId());
            String title = binding.inputIssueTitle.getEditText().getText().toString();
            String description = binding.inputIssueDescription.getEditText().getText().toString();
            String dueDate = dateString[0];
            String weight = binding.inputIssueWeight.getEditText().getText().toString();
            String milestoneName = null;

            if(chip != null){
                 milestoneName = chip.getText().toString();
            }

            //title may not be empty
            if(!title.isEmpty()){
                viewModel.postNewIssue(projectId,title,description,dueDate,weight,selectedLabels,milestoneName); //todo implement labels and milestones
            }else{
                Toast.makeText(getActivity(),"Title can not be empty", Toast.LENGTH_SHORT).show();
            }

        });
    }

}