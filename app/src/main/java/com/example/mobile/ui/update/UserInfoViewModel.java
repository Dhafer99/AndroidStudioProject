package com.example.mobile.ui.update;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mobile.Session.SessionManager;
import com.example.mobile.database.UserEntity;
import com.example.mobile.database.repositories.UserRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserInfoViewModel extends AndroidViewModel {

    private final UserRepository userRepository;
    private final MutableLiveData<UserEntity> userLiveData = new MutableLiveData<>();

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public UserInfoViewModel(Application application, UserRepository userRepository) {
        super(application);
        this.userRepository = userRepository;

        SessionManager sessionManager = new SessionManager(application);
        // Fetch user data asynchronously
        executorService.execute(() -> {
            UserEntity user = userRepository.getUserById(sessionManager.getUserId());
            userLiveData.postValue(user); // Post result to LiveData
        });


    }

    public LiveData<UserEntity> getUserLiveData() {
        return userLiveData;
    }

    public void updateUserInfo(String name, String email, String phone) {
        UserEntity user = userLiveData.getValue();
        if (user != null) {
            user.setName(name);
            user.setEmail(email);
            user.setPhoneNumber(phone);
            userRepository.updateUser(user);
            userLiveData.setValue(user); // Update LiveData to reflect changes
        }
    }
}
