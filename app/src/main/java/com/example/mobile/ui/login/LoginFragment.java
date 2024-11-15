package com.example.mobile.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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


import com.example.mobile.ui.login.LoginViewModel;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private LoginViewModel loginViewModel;
    private ViewFlipper viewFlipper;

    private static final int IMAGE_PICK_CODE2 = 1000;

    private static final String PREFS_NAME = "UserPrefs";
    private static final String IMAGE_URI_KEY = "imageUri";

    private Uri selectedImageUri;

    private ImageView userImageView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        userImageView = root.findViewById(R.id.user_image);

        Button uploadImageButton = root.findViewById(R.id.upload_image_button);

        uploadImageButton.setOnClickListener(v -> openImagePicker());


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
                String image = selectedImageUri.toString() ;
            if (selectedImageUri != null) {
             image = selectedImageUri.toString(); // Store the image URI
            }

            String name = binding.signUpName.getText().toString().trim();
            String email = binding.signUpEmail.getText().toString().trim();
            String phone = binding.signUpPhone.getText().toString().trim();
            String password = binding.signUpPassword.getText().toString().trim();
            loginViewModel.signUp(name, email, phone, password,role,image);
        });

        return root;
    }
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_PICK_CODE2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE2 && data != null) {
            selectedImageUri = data.getData();
            userImageView.setImageURI(selectedImageUri);

            // Take persistable URI permission
            requireContext().getContentResolver().takePersistableUriPermission(
                    selectedImageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Save URI to shared preferences
            saveImageUri(selectedImageUri);
        }
    }
    private void saveImageUri(Uri uri) {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(IMAGE_URI_KEY, uri.toString()).apply();
    }
    private Uri getImageUri() {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String uriString = prefs.getString(IMAGE_URI_KEY, null);
        return uriString != null ? Uri.parse(uriString) : null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}




