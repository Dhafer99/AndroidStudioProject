package com.example.mobile.ui.update;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobile.R;
import com.example.mobile.database.UserEntity;
import com.example.mobile.database.repositories.UserRepository;

public class UserInfoModificationFragment extends Fragment {

    private EditText editTextName, editTextEmail, editTextPhone;
    private Button btnSave;
    private UserInfoViewModel userInfoViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info_modification, container, false);

        // Initialize UI components
        editTextName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        btnSave = view.findViewById(R.id.btnSave);

        // Initialize UserRepository and custom ViewModel factory
        UserRepository userRepository = new UserRepository(requireContext());
        UserInfoViewModelFactory factory = new UserInfoViewModelFactory(requireActivity().getApplication(), userRepository);
        userInfoViewModel = new ViewModelProvider(this, factory).get(UserInfoViewModel.class);

        // Load existing user info
        userInfoViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                editTextName.setText(user.getName());
                editTextEmail.setText(user.getEmail());
                editTextPhone.setText(user.getPhoneNumber());
            }
        });

        // Save button listener
        btnSave.setOnClickListener(v -> saveUserInfo());

        return view;
    }

    private void saveUserInfo() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty()) {
            userInfoViewModel.updateUserInfo(name, email, phone);
            Toast.makeText(getContext(), "User info updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
        }
    }
}
