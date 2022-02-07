package de.hdmstuttgart.gitlapp.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import de.hdmstuttgart.gitlapp.CustomApplication;
import de.hdmstuttgart.gitlapp.R;
import de.hdmstuttgart.gitlapp.data.network.NetworkStatus;
import de.hdmstuttgart.gitlapp.databinding.FragmentCreateIssueBinding;
import de.hdmstuttgart.gitlapp.dependencies.AppContainer;
import de.hdmstuttgart.gitlapp.models.Label;
import de.hdmstuttgart.gitlapp.models.Milestone;
import de.hdmstuttgart.gitlapp.viewmodels.CreateIssueViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateIssueFragment extends Fragment {

    private static final String ARG_PARAM1 = "projectId";
    private int projectId;

    private Context context;

    private FragmentCreateIssueBinding binding;
    private CreateIssueViewModel viewModel;

    private MutableLiveData<List<Label>> projectLabels;
    private MutableLiveData<List<Milestone>> projectMilestones;

    public CreateIssueFragment() {
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
        }

        AppContainer container = ((CustomApplication) context.getApplicationContext())
                .getAppContainer(context.getApplicationContext());

        viewModel = new ViewModelProvider(this, container.viewModelFactory)
                .get(CreateIssueViewModel.class);

        projectLabels = viewModel.getProjectLabels(projectId);
        projectMilestones = viewModel.getProjectMilestones(projectId);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateIssueBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) { //todo split up in multiple methods for better readability
        super.onViewCreated(view, savedInstanceState);

        // - - - - -  data observation - - - -
        viewModel.getMessage().observe(getViewLifecycleOwner(), s -> {
            if(s == NetworkStatus.LOADING){
                binding.progressSpinnerCreateIssue.setVisibility(View.VISIBLE);
            }else if(s != NetworkStatus.SUCCESS){
                Toast.makeText(getActivity(), s.message, Toast.LENGTH_SHORT).show();
            }

            if(s == NetworkStatus.ISSUE_POST_SUCCESS){
                getParentFragmentManager().popBackStackImmediate(); //end fragment if post was successful
            }
        });

        // - - - - - label and milestone selection - - - - -
        ChipGroup labelGroup = binding.labelChipGroup;
        ChipGroup milestoneGroup = binding.milestoneChipGroup;

        List<String> selectedLabels = new ArrayList<>();

        projectLabels.observe(getViewLifecycleOwner(), labels -> {
            labelGroup.removeAllViews();
            //fill chip groups
            //label group
            Log.d("Labels", "labels " + labels.toString());
            for (Label label : labels) {

                Chip labelChip = new Chip(context);
                labelChip.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));

                labelChip.setText(label.getName());
                labelChip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(label.getColor())));
                labelChip.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)));
                labelChip.setCheckable(true);
                labelChip.setCheckedIconVisible(true);

                labelChip.setOnCheckedChangeListener((compoundButton, b) -> {
                    if (b) {
                        selectedLabels.add(labelChip.getText().toString());
                    } else {
                        selectedLabels.remove(labelChip.getText().toString());
                    }
                });
                labelGroup.addView(labelChip);
            }
        });

        projectMilestones.observe(getViewLifecycleOwner(), milestones -> {
            milestoneGroup.removeAllViews();
            for (Milestone milestone : milestones) {
                Log.d("Labels", "stones " + milestones.toString());

                Chip milestoneChip = new Chip(context);
                milestoneChip.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));

                milestoneChip.setCheckable(true);
                milestoneChip.setCheckedIconVisible(true);
                milestoneChip.setText(milestone.getTitle());
                milestoneGroup.addView(milestoneChip);
            }
        });


        // - - - - - date  selection - - - - -
        DatePickerDialog.OnDateSetListener dateSetListener;
        final String[] dateString = new String[1]; //needs to be an array bc of lambda onDataSet

        //date picker for issue due date
        dateSetListener = (datePicker, year, month, day) -> {
            month += 1;
            Log.d("Date", year + "-" + month + "-" + day);
            dateString[0] = year + "-" + month + "-" + day;
            binding.dueDateButton.setText(dateString[0]);
        };


        binding.dueDateButton.setOnClickListener(view1 -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Material_Dialog, dateSetListener, year, month, day);
            dialog.setTitle("Choose issue due date");
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
            dialog.show();
        });


        // - - - - button listeners - - - -
        binding.createButton.setOnClickListener(view12 -> {
            try{
                Chip chip = milestoneGroup.findViewById(milestoneGroup.getCheckedChipId());
                String title = Objects.requireNonNull(binding.inputIssueEditText.getText()).toString();
                String description = Objects.requireNonNull(binding.inputIssueDescriptionEditText.getText()).toString();
                String dueDate = dateString[0];
                String weight = Objects.requireNonNull(binding.inputIssueWeightEditText.getText()).toString();
                String milestoneName = null;

                if (chip != null) {
                    milestoneName = chip.getText().toString();
                }

                //title may not be empty
                if (!title.isEmpty()) {
                    viewModel.postNewIssue(projectId, title, description, dueDate, weight, selectedLabels, milestoneName);
                } else {
                    Toast.makeText(getActivity(), "Title can not be empty", Toast.LENGTH_SHORT).show();
                }
            }catch(NullPointerException c){
                Log.d("Api", c.getMessage());
            }
        });
    }
}