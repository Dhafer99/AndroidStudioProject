package com.example.mobile;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.mobile.database.DatabaseProvider;
import com.example.mobile.database.PetCareDatabase;
import com.example.mobile.database.UserEntity;
import com.example.mobile.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PetCareDatabase db = DatabaseProvider.getDatabase(this);

        // Database operations in a background thread
        new Thread(() -> {
            // Example: Insert a user
            UserEntity user = new UserEntity();
            user.setName("John Doe");
            user.setEmail("john@example.com");
            user.setPhoneNumber("123456789");
            user.setPassword("123");
            db.userDao().insertUser(user);

            // Example: Fetch all users
            List<UserEntity> users = db.userDao().getAllUsers();
            for (UserEntity u : users) {
                System.out.println(u.getName());
            }
        }).start();

        // Set up view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up toolbar
        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show()
        );

        // Set up navigation drawer
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Add `R.id.nav_animal` to the list of top-level destinations
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_login, R.id.nav_animal)
                .setOpenableLayout(drawer)
                .build();

        // Set up NavController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
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
                    "MEDICATION_CHANNEL",
                    "Medication Reminders",
                    NotificationManager.IMPORTANCE_HIGH
            );
            medicationChannel.setDescription("This channel is for medication reminders");

            NotificationChannel appointmentChannel = new NotificationChannel(
                    "APPOINTMENT_CHANNEL",
                    "Appointment Alerts",
                    NotificationManager.IMPORTANCE_HIGH
            );
            appointmentChannel.setDescription("This channel is for appointment reminders");

            NotificationChannel feedingChannel = new NotificationChannel(
                    "FEEDING_CHANNEL",
                    "Feeding Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            feedingChannel.setDescription("This channel is for feeding reminders");

            NotificationChannel activityChannel = new NotificationChannel(
                    "ACTIVITY_CHANNEL",
                    "Activity Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
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
