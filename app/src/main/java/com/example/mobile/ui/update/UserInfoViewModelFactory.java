package com.example.mobile.ui.update;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.mobile.database.repositories.UserRepository;

public class UserInfoViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final UserRepository userRepository;

    public UserInfoViewModelFactory(Application application, UserRepository userRepository) {
        this.application = application;
        this.userRepository = userRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserInfoViewModel.class)) {
            return (T) new UserInfoViewModel(application, userRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
