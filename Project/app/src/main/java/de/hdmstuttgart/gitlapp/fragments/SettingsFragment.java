package de.hdmstuttgart.gitlapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import de.hdmstuttgart.gitlapp.CustomApplication;
import de.hdmstuttgart.gitlapp.databinding.FragmentProfileSettingsBinding;
import de.hdmstuttgart.gitlapp.dependencies.AppContainer;
import de.hdmstuttgart.gitlapp.viewmodels.SettingsViewModel;
import de.hdmstuttgart.gitlapp.viewmodels.vmpFactories.ViewModelFactory;

public class SettingsFragment extends Fragment {

    private AppContainer appContainer;

    private SettingsViewModel settingsViewModel;
    private FragmentProfileSettingsBinding binding;

    private TextView hostUrlTextview;
    private TextView userIdTextView;
    private ImageView imageView;
    private TextView user;



    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get related view model
        appContainer = ((CustomApplication) getActivity().getApplication())
                .getAppContainer(getActivity().getApplicationContext());

        ViewModelFactory viewModelFactory = appContainer.viewModelFactory;

        settingsViewModel = new ViewModelProvider(this, viewModelFactory).get(SettingsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hostUrlTextview = binding.hostUrlLinkLabel;
        userIdTextView = binding.userIdNumberLabel;


        hostUrlTextview.setText(settingsViewModel.getGitlabUrl());
        userIdTextView.setText(settingsViewModel.getUserId());

        // user Card binding and setting of the values
        imageView = binding.userCard.projectAvatar;
        user = binding.userCard.userNameLabel;

        user.setText(settingsViewModel.getLoggedInUserName());

        Glide.with(getContext())
                .load(settingsViewModel.getLoggedInUserAvatar())
                .into(imageView);

    }
}
