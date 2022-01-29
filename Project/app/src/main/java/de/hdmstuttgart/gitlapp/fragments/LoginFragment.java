package de.hdmstuttgart.gitlapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Objects;

import de.hdmstuttgart.gitlapp.CustomApplication;
import de.hdmstuttgart.gitlapp.dependencies.LoginContainer;
import de.hdmstuttgart.gitlapp.R;
import de.hdmstuttgart.gitlapp.databinding.FragmentLogInBinding;
import de.hdmstuttgart.gitlapp.viewmodels.LoginViewModel;
import de.hdmstuttgart.gitlapp.viewmodels.vmpFactories.LoginViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private Context context;

    private LoginContainer container;

    private FragmentLogInBinding binding;
    private LoginViewModel viewModel;

    private EditText baseUrlTextField;
    private EditText userIdTextField;
    private EditText accessTokenTextField;
    private ProgressBar spinner;

    private String baseUrl;

    private MutableLiveData<String> messageLiveData;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("Bug","LoginFragment onCreate");
        super.onCreate(savedInstanceState);

        // - - - - get the related view model - - - -
        container = ((CustomApplication) context.getApplicationContext())
                .getLoginContainer(context.getApplicationContext());

        LoginViewModelFactory loginViewModelFactory = container.loginViewModelFactory;

        viewModel = new ViewModelProvider(this, loginViewModelFactory)
                .get(LoginViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLogInBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("Bug","LoginFragment onViewCreated");

        super.onViewCreated(view, savedInstanceState);

        baseUrlTextField = binding.hostUrlEditText;
        userIdTextField = binding.userIdEditText;
        accessTokenTextField = binding.accessTokenEditText;
        Button loginButton = binding.loginButton;
        Button openUrlButton = binding.openUrlButton;
        spinner = binding.progressSpinner;

        messageLiveData = viewModel.getMessageLiveData();
        messageLiveData.observe(getViewLifecycleOwner(), s -> {

            //inform the user if login failed etc
            if(!Objects.equals(messageLiveData.getValue(), "default")){ //default value is set after profile creation
                Toast.makeText(getActivity(), messageLiveData.getValue(), Toast.LENGTH_SHORT).show();
                spinner.setVisibility(View.GONE);
            }

            //fragment transaction when profile was successfully created
            if (s.equals("Call successful, profile created")) {

                //write profile info to shared preferences only if login was successful
                String url = baseUrl + "/api/v4/";
                SharedPreferences sharedPreferences = context.getSharedPreferences("profileInformation", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("baseUrl", url);
                editor.apply();

                messageLiveData.setValue("default"); //otherwise this if clause will be always true afterwards -> app crash on logout (where login screen is called again)

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, ProjectsFragment.class, null)
                        .commit();
            }
        });

        //open gitlab url in browser to let the user easily create an access token
        openUrlButton.setOnClickListener(view12 -> {
           String baseUrl = baseUrlTextField.getText().toString();

           if(baseUrl.isEmpty()){
               Toast.makeText(getActivity(), "Please fill in the base url", Toast.LENGTH_SHORT).show();
           }else{
               String tokenUrl = baseUrl + "/-/profile/personal_access_tokens?name=GitLappToken&scopes=api";
               Uri uri = Uri.parse(tokenUrl);
               Intent intent = new Intent(Intent.ACTION_VIEW,uri);
               startActivity(intent);
           }
        });

        //try to create the user profile
        loginButton.setOnClickListener(view1 -> {
            try {
                baseUrl = baseUrlTextField.getText().toString();
                String userIdString = userIdTextField.getText().toString();
                String accessToken = accessTokenTextField.getText().toString();

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
        super.onDestroy(); //todo i guess this is only called when the activity is destroyed
        container = null; //container will not be needed anymore after login is completed
    }
}