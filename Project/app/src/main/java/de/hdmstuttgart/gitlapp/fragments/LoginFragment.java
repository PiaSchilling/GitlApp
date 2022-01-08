package de.hdmstuttgart.gitlapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import de.hdmstuttgart.gitlapp.CustomApplication;
import de.hdmstuttgart.gitlapp.LoginContainer;
import de.hdmstuttgart.gitlapp.R;
import de.hdmstuttgart.gitlapp.databinding.FragmentLogInBinding;
import de.hdmstuttgart.gitlapp.viewmodels.LoginViewModel;
import de.hdmstuttgart.gitlapp.viewmodels.LoginViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private LoginContainer container;

    private FragmentLogInBinding binding;
    private LoginViewModel viewModel;

    private TextInputLayout baseUrlTextField;
    private TextInputLayout userIdTextField;
    private TextInputLayout accessTokenTextField;
    private Button loginButton;
    private ProgressBar spinner;

    private String baseUrl;

    private MutableLiveData<String> messageLiveData;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance() { //todo needed ?
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();//todo remove
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // - - - - get the related view model - - - -
        container = ((CustomApplication) getActivity().getApplication())
                .getLoginContainer(getActivity().getApplicationContext());

        LoginViewModelFactory loginViewModelFactory = container.loginViewModelFactory;

        viewModel = new ViewModelProvider(this, loginViewModelFactory)
                .get(LoginViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLogInBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        baseUrlTextField = binding.hostUrlTextField;
        userIdTextField = binding.userIdTextField;
        accessTokenTextField = binding.accessTokenTextField;
        loginButton = binding.loginButton;
        spinner = binding.progressSpinner;

        messageLiveData = viewModel.getMessageLiveData();
        messageLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                //inform the user if login failed etc
                Toast.makeText(getActivity(), messageLiveData.getValue(), Toast.LENGTH_LONG).show();
                spinner.setVisibility(View.GONE);

                //fragment transaction when profile was successfully created
                if (s.equals("Call successful, profile created")) {

                    //write profile info to shared preferences only if login was successful
                    String url = baseUrl + "/api/v4/";
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("profileInformation", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("baseUrl", url);
                    editor.apply();

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, IssueOverviewFragment.class, null) //todo replace to project overview fragment
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        loginButton.setOnClickListener(view1 -> {

            try {
                baseUrl = baseUrlTextField.getEditText().getText().toString();
                String userIdString = userIdTextField.getEditText().getText().toString();
                String accessToken = accessTokenTextField.getEditText().getText().toString();

                if (baseUrl.isEmpty() || userIdString.isEmpty() || accessToken.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_LONG).show();
                } else {

                    int userId = Integer.parseInt(userIdString);

                    viewModel.createUserProfile(baseUrl, userId, accessToken); //create user profile
                    spinner.setVisibility(View.VISIBLE);
                }
            } catch (NullPointerException exception) {
                Log.e("Api", exception.getMessage());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        container = null; //container will not be needed anymore after login is completed
    }
}