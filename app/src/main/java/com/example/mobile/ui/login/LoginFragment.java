package com.example.mobile.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.mobile.databinding.FragmentLoginBinding;
import com.example.mobile.database.repositories.UserRepository;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private LoginViewModel loginViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize the LoginViewModel
        UserRepository userRepository = new UserRepository(requireContext());
        loginViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @Override
            @NonNull
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new LoginViewModel(userRepository);
            }
        }).get(LoginViewModel.class);

        // Set up button click listener for login
        binding.loginButton.setOnClickListener(v -> {
            String email = binding.emailEditText.getText().toString().trim();
            String password = binding.passwordEditText.getText().toString().trim();
            loginViewModel.login(email, password);
        });

        // Observe the ViewModel for login result
        loginViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                Toast.makeText(getContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                // Navigate to the next screen or update UI accordingly
            }
        });

        loginViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
