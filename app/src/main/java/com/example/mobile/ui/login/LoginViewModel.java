package com.example.mobile.ui.login;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Patterns;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.mobile.Session.SessionManager;
import com.example.mobile.database.UserEntity;
import com.example.mobile.database.repositories.UserRepository;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginViewModel extends AndroidViewModel {

    private final UserRepository userRepository;
    private final SessionManager sessionManager;
    private final MutableLiveData<UserEntity> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<UserEntity> blockedUserLiveData = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final FusedLocationProviderClient fusedLocationClient;

    public LoginViewModel(@NonNull Application application, UserRepository userRepository) {
        super(application);
        this.userRepository = userRepository;
        this.sessionManager = new SessionManager(application);
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(application);
    }

    public void login(String email, String password) {
        if (!isValidEmail(email)) {
            errorLiveData.postValue("Invalid email format");
            return;
        }
        if (!isValidPassword(password)) {
            errorLiveData.postValue("Password must be at least 6 characters long");
            return;
        }

        executor.execute(() -> {
            UserEntity user = userRepository.loginUser(email, password);

            if (user != null) {
                if (user.getIsBlocked()) {
                    blockedUserLiveData.postValue(user);
                } else {
                    sessionManager.createLoginSession(user.getUserId(), user.getRole());

                    userLiveData.postValue(user); // Admin or regular user
                }
            } else {
                errorLiveData.postValue("Invalid email or password");
            }
        });
    }

    public void signUp(String name, String email, String phone, String password, String role, String image) {
        if (!isValidEmail(email)) {
            errorLiveData.postValue("Invalid email format");
            return;
        }
        if (!isValidPhone(phone)) {
            errorLiveData.postValue("Phone number must contain 8 digits");
            return;
        }
        if (!isValidPassword(password)) {
            errorLiveData.postValue("Password must be at least 6 characters long");
            return;
        }

        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            errorLiveData.postValue("Location permissions are required for signup.");
            return;
        }

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(location -> {
                    executor.execute(() -> {
                        UserEntity existingUser = userRepository.getUserByEmail(email);
                        if (existingUser != null) {
                            errorLiveData.postValue("User already exists");
                        } else {
                            UserEntity newUser = new UserEntity();
                            newUser.setName(name);
                            newUser.setEmail(email);
                            newUser.setPhoneNumber(phone);
                            newUser.setPassword(password);
                            newUser.setType(role);
                            newUser.setIsBlocked(false);
                            newUser.setImage(image);
                            newUser.setRole("User");

                            if (location != null) {
                                newUser.setLatitude(location.getLatitude());
                                newUser.setLongitude(location.getLongitude());
                            } else {
                                newUser.setLatitude(0.0);
                                newUser.setLongitude(0.0);
                            }

                            userRepository.insertuser(newUser);
                            userLiveData.postValue(newUser);
                        }
                    });
                });
    }

    private boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d{8}");
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }


    public LiveData<UserEntity> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<UserEntity> getBlockedUserLiveData() {
        return blockedUserLiveData;
    }
}
