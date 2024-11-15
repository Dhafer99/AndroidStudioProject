package com.example.mobile;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mobile.DAO.PlanFoodDao;
import com.example.mobile.Session.SessionManager;
import com.example.mobile.database.DatabaseProvider;
import com.example.mobile.database.PetCareDatabase;
import com.example.mobile.database.UserEntity;
import com.example.mobile.database.repositories.UserRepository;
import com.example.mobile.ui.login.LoginSignupActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.mobile.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private UserRepository userRepository;
    private final MutableLiveData<Boolean> logoutLiveData = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PetCareDatabase db = DatabaseProvider.getDatabase(this);
        PlanFoodDao planFoodDao = db.planFoodDao();

        // Set up view binding
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Set top level destinations
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_food, R.id.nav_login,
                R.id.nav_signup, R.id.nav_admin_item, R.id.nav_service_fares, R.id.nav_animal,
                R.id.nav_fragment, R.id.nav_vet)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Check user role and show/hide the admin item
        SessionManager sessionManager = new SessionManager(this);
        MenuItem adminItem = navigationView.getMenu().findItem(R.id.nav_admin_item);
        String userRole = sessionManager.getUserRole(); // Assuming this returns "Admin" or other roles
        adminItem.setVisible("Admin".equals(userRole));

        // Observe logoutLiveData for logout actions
        logoutLiveData.observe(this, shouldLogout -> {
            Log.d("MainActivity", "Logout triggered");
            if (shouldLogout) {
                Toast.makeText(MainActivity.this, "Logout successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LoginSignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Set logout listener
        Button LogoutButton = findViewById(R.id.logout);
        LogoutButton.setOnClickListener(v -> {
            sessionManager.logout();
            Intent intent = new Intent(MainActivity.this, LoginSignupActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void logout() {
        Log.d("MainActivity", "logout() called");
        logoutLiveData.setValue(true); // Trigger observer
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel medicationChannel = new NotificationChannel(
                    "MEDICATION_CHANNEL", "Medication Reminders", NotificationManager.IMPORTANCE_HIGH
            );
            medicationChannel.setDescription("This channel is for medication reminders");

            NotificationChannel appointmentChannel = new NotificationChannel(
                    "APPOINTMENT_CHANNEL", "Appointment Alerts", NotificationManager.IMPORTANCE_HIGH
            );
            appointmentChannel.setDescription("This channel is for appointment reminders");

            NotificationChannel feedingChannel = new NotificationChannel(
                    "FEEDING_CHANNEL", "Feeding Reminders", NotificationManager.IMPORTANCE_DEFAULT
            );
            feedingChannel.setDescription("This channel is for feeding reminders");

            NotificationChannel activityChannel = new NotificationChannel(
                    "ACTIVITY_CHANNEL", "Activity Reminders", NotificationManager.IMPORTANCE_DEFAULT
            );
            activityChannel.setDescription("This channel is for activity reminders");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(medicationChannel);
                manager.createNotificationChannel(appointmentChannel);
                manager.createNotificationChannel(feedingChannel);
                manager.createNotificationChannel(activityChannel);
            }
        }
    }
}
