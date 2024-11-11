package com.example.mobile.ui.signup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mobile.database.UserEntity;
import com.example.mobile.database.repositories.UserRepository;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignupViewModel extends ViewModel {

    private final UserRepository userRepository;
    private final MutableLiveData<Boolean> signupResult = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public SignupViewModel(UserRepository repository) {
        this.userRepository = repository;
    }

    public LiveData<Boolean> getSignupResult() {
        return signupResult;
    }

    public void signupUser(String name, String email, String phoneNumber, String password) {
        executor.execute(() -> {
            // Check if email already exists
            UserEntity existingUser = userRepository.getUserByEmail(email);
            if (existingUser != null) {
                signupResult.postValue(false); // Email already exists
            } else {
                // Create and insert new user
                UserEntity user = new UserEntity();
                user.setName(name);
                user.setEmail(email);
                user.setPhoneNumber(phoneNumber);
                user.setPassword(password);
                user.setRole("User");
                userRepository.insertuser(user);
                signupResult.postValue(true); // Signup successful
            }
        });
    }
}
