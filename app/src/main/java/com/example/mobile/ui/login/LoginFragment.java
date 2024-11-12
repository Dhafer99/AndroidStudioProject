package com.example.mobile.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.mobile.AdminDashboardActivity;
import com.example.mobile.BlockedUserActivity;
import com.example.mobile.MainActivity; // Main activity with sidebar
import com.example.mobile.R;
import com.example.mobile.databinding.FragmentLoginBinding;
import com.example.mobile.database.repositories.UserRepository;
import com.example.mobile.ui.password.ForgotPasswordFragment;


import java.util.Objects;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private LoginViewModel loginViewModel;
    private ViewFlipper viewFlipper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        UserRepository userRepository = new UserRepository(requireContext());
        loginViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @Override
            @NonNull
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new LoginViewModel(requireActivity().getApplication(),userRepository);
            }
        }).get(LoginViewModel.class);


        viewFlipper = binding.viewFlipper;
        viewFlipper.setInAnimation(getContext(), R.anim.slide_in_right);
        viewFlipper.setOutAnimation(getContext(), R.anim.slide_out_left);

        binding.goToSignUp.setOnClickListener(v -> {
            viewFlipper.setInAnimation(getContext(), R.anim.slide_in_right);
            viewFlipper.setOutAnimation(getContext(), R.anim.slide_out_left);
            viewFlipper.showNext();
        });

        binding.goToSignIn.setOnClickListener(v -> {
            viewFlipper.setInAnimation(getContext(), R.anim.slide_in_left);
            viewFlipper.setOutAnimation(getContext(), R.anim.slide_out_right);
            viewFlipper.showPrevious();
        });

        binding.loginButton.setOnClickListener(v -> {
            String email = binding.emailEditText.getText().toString().trim();
            String password = binding.passwordEditText.getText().toString().trim();
            loginViewModel.login(email, password);
        });
        binding.forgotPasswordButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
        });

        loginViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                Toast.makeText(getContext(), "Login successful!", Toast.LENGTH_SHORT).show();

                Intent intent;



                if (Objects.equals(user.getRole(), "Admin")) {
                    intent = new Intent(getActivity(), AdminDashboardActivity.class); // Redirect to Admin Dashboard
                } else {
                    intent = new Intent(getActivity(), MainActivity.class); // Redirect to main app with sidebar
                }

                startActivity(intent);
                requireActivity().finish(); // Finish LoginSignupActivity
            }
        });
        loginViewModel.getBlockedUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                Intent intent = new Intent(getActivity(), BlockedUserActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });


        loginViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
        RadioGroup roleRadioGroup = viewFlipper.findViewById(R.id.roleRadioGroup);

        binding.signUpButton.setOnClickListener(v -> {

            int selectedRoleId = roleRadioGroup.getCheckedRadioButtonId();
            String role;
            if (selectedRoleId == R.id.radio_veterinarian) {
                role = "Veterinarian";
            } else if (selectedRoleId == R.id.radio_normal_user) {
                role = "User";
            } else {

                Toast.makeText(getContext(), "Please select a role", Toast.LENGTH_SHORT).show();
                return; // Exit if no role is selected
            }

            String name = binding.signUpName.getText().toString().trim();
            String email = binding.signUpEmail.getText().toString().trim();
            String phone = binding.signUpPhone.getText().toString().trim();
            String password = binding.signUpPassword.getText().toString().trim();
            loginViewModel.signUp(name, email, phone, password,role);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
