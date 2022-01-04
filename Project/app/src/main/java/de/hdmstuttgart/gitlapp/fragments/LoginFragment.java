package de.hdmstuttgart.gitlapp.fragments;

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

import de.hdmstuttgart.gitlapp.AppContainer;
import de.hdmstuttgart.gitlapp.CustomApplication;
import de.hdmstuttgart.gitlapp.databinding.FragmentLogInBinding;
import de.hdmstuttgart.gitlapp.viewmodels.LoginViewModel;
import de.hdmstuttgart.gitlapp.viewmodels.LoginViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private FragmentLogInBinding binding;
    private LoginViewModel viewModel;

    private TextInputLayout baseUrlTextField;
    private TextInputLayout userIdTextField;
    private TextInputLayout accessTokenTextField;
    private Button loginButton;
    private ProgressBar spinner;

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
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();//todo remove
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // - - - - get the related view model - - - -
        AppContainer container = ((CustomApplication) getActivity().getApplication())
                .getContainer(getActivity().getApplicationContext());

        LoginViewModelFactory loginViewModelFactory = new LoginViewModelFactory(container.profileRepository);

        viewModel = new ViewModelProvider(this, loginViewModelFactory)
                .get(LoginViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLogInBinding.inflate(inflater,container,false);
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
                Toast.makeText(getActivity(),messageLiveData.getValue(),Toast.LENGTH_LONG).show();
                spinner.setVisibility(View.GONE);
            }
        });

        loginButton.setOnClickListener(view1 -> {

            try{
                String baseUrl = baseUrlTextField.getEditText().getText().toString();
                String userIdString = userIdTextField.getEditText().getText().toString();
                String accessToken = accessTokenTextField.getEditText().getText().toString();

                if(baseUrl.isEmpty() || userIdString.isEmpty() || accessToken.isEmpty()){
                    Toast.makeText(getActivity(),"Please fill in all fields",Toast.LENGTH_LONG).show();
                }else{
                    int userId = Integer.parseInt(userIdString);
                    viewModel.createUserProfile(baseUrl,userId,accessToken);
                    spinner.setVisibility(View.VISIBLE);
                }
            }catch (NullPointerException exception){
                Log.e("Api",exception.getMessage());
            }
            //todo fragment transaction
        });



    }
}