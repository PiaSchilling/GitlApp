package de.hdmstuttgart.gitlapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import de.hdmstuttgart.gitlapp.CustomApplication;
import de.hdmstuttgart.gitlapp.R;
import de.hdmstuttgart.gitlapp.databinding.FragmentProfileSettingsBinding;
import de.hdmstuttgart.gitlapp.dependencies.AppContainer;
import de.hdmstuttgart.gitlapp.viewmodels.SettingsViewModel;
import de.hdmstuttgart.gitlapp.viewmodels.vmpFactories.ViewModelFactory;

public class SettingsFragment extends Fragment {
    private Context context;

    private SettingsViewModel settingsViewModel;
    private FragmentProfileSettingsBinding binding;


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get related view model
        AppContainer appContainer = ((CustomApplication) context.getApplicationContext())
                .getAppContainer(context.getApplicationContext());

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

        TextView hostUrlTextview = binding.hostUrlLinkLabel;
        TextView userIdTextView = binding.userIdNumberLabel;


        hostUrlTextview.setText(settingsViewModel.getGitlabUrl());
        userIdTextView.setText(settingsViewModel.getUserId());

        // user Card binding and setting of the values
        ImageView imageView = binding.userCard.projectAvatar;
        TextView user = binding.userCard.userNameLabel;

        user.setText(settingsViewModel.getLoggedInUserName());

        Glide.with(context)
                .load(settingsViewModel.getLoggedInUserAvatar())
                .into(imageView);

        // trigger for logOut
        Button logOutButton = binding.logOutButton;

        logOutButton.setOnClickListener(v -> {
            clearSharedPreferences();
            settingsViewModel.clearDatabase();

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, LoginFragment.class, null)
                    .commit();
        });
    }


    private void clearSharedPreferences(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("profileInformation", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("baseUrl", "default");
        editor.apply();
    }
}
