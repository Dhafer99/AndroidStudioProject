package com.example.mobile.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.mobile.database.UserEntity;
import com.example.mobile.database.repositories.UserRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginViewModel extends ViewModel {

    private final UserRepository userRepository;
    private final MutableLiveData<UserEntity> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public LoginViewModel(UserRepository repository) {
        this.userRepository = repository;
    }

    public void login(String email, String password) {
        executor.execute(() -> {
            UserEntity user = userRepository.loginUser(email, password);
            if (user != null) {
                userLiveData.postValue(user); // Successful login
            } else {
                errorLiveData.postValue("Invalid email or password"); // Failed login
            }
        });
    }

    public LiveData<UserEntity> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}
