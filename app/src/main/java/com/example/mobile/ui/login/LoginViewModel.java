package com.example.mobile.ui.login;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.location.Location;
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
        executor.execute(() -> {
            UserEntity user = userRepository.loginUser(email, password);

            if (user != null) {
                if (user.getIsBlocked()) {
                    blockedUserLiveData.postValue(user);
                } else {
                    sessionManager.createLoginSession(user.getUserId(),user.getRole());

                    if (Objects.equals(user.getRole(), "Admin")) {


                        userLiveData.postValue(user); // For admin
                    } else if (Objects.equals(user.getRole(), "User")) {
                        userLiveData.postValue(user); // For regular users
                    }
                }
            } else {
                errorLiveData.postValue("Invalid email or password");
            }
        });
    }

    public void signUp(String name, String email, String phone, String password,String role) {
        // Check if location permission is granted before calling this method
        // Check location permissions before proceeding
        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permissions are not granted; handle this scenario in your activity (e.g., request permissions)
            errorLiveData.postValue("Location permissions are required for signup.");
            return; // Exit the method if permissions are not granted
        }
        // Request current location with high accuracy
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(location -> {
                    executor.execute(() -> {
                        // Check if user already exists
                        UserEntity existingUser = userRepository.getUserByEmail(email);
                        if (existingUser != null) {
                            errorLiveData.postValue("User already exists");
                        } else {
                            // Create a new user and insert into the database
                            UserEntity newUser = new UserEntity();
                            newUser.setName(name);
                            newUser.setEmail(email);
                            newUser.setPhoneNumber(phone);
                            newUser.setPassword(password);
                            newUser.setType(role);
                            newUser.setIsBlocked(false);
                           newUser.setRole("User");

                            // Set latitude and longitude if location is available
                            if (location != null) {
                                newUser.setLatitude(location.getLatitude());
                                newUser.setLongitude(location.getLongitude());
                            } else {
                                newUser.setLatitude(0.0); // Default values if location is unavailable
                                newUser.setLongitude(0.0);
                            }

                            userRepository.insertuser(newUser);
                            userLiveData.postValue(newUser); // Successful signup
                        }
                    });
                });
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
