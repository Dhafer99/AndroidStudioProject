package com.example.mobile.ui.signup;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.mobile.databinding.FragmentSignupBinding;
import com.example.mobile.database.repositories.UserRepository;

public class SignupFragment extends Fragment {

    private FragmentSignupBinding binding;
    private SignupViewModel signupViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        UserRepository userRepository = new UserRepository(requireContext());
        signupViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new SignupViewModel(userRepository);
            }
        }).get(SignupViewModel.class);

        setupObservers();
        setupListeners();

        return root;
    }

    private void setupObservers() {
        signupViewModel.getSignupResult().observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(getContext(), "Signup successful!", Toast.LENGTH_SHORT).show();
                // Navigate to the next screen or clear input fields
            } else {
                Toast.makeText(getContext(), "Email already exists", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListeners() {
        binding.signupButton.setOnClickListener(v -> {
            String name = binding.nameEditText.getText().toString().trim();
            String email = binding.emailEditText.getText().toString().trim();
            String phoneNumber = binding.phoneNumberEditText.getText().toString().trim();
            String password = binding.passwordEditText.getText().toString().trim();

            if (validateInput(name, email, phoneNumber, password)) {
                signupViewModel.signupUser(name, email, phoneNumber, password);
            } else {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInput(String name, String email, String phoneNumber, String password) {
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) &&
                !TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(password);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
