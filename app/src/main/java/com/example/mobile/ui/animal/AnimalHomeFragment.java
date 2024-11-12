package com.example.mobile.ui.animal;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.mobile.R;

public class AnimalHomeFragment extends Fragment {

    private static final String TAG = "AnimalHomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_animal_home, container, false);

        // Initialize UI components
        Button buttonAddAnimal = view.findViewById(R.id.buttonAddAnimal);
        Button buttonAnimalList = view.findViewById(R.id.buttonAnimalList);
        Button buttonAddReminder = view.findViewById(R.id.buttonAddReminder);

        // Set button click listeners
        buttonAddAnimal.setOnClickListener(v -> navigateToFragment(v, R.id.action_animalHomeFragment_to_animalFragment));
        buttonAnimalList.setOnClickListener(v -> navigateToFragment(v, R.id.action_animalHomeFragment_to_animalListFragment));
        buttonAddReminder.setOnClickListener(v -> {
            Log.d(TAG, "Set Reminder Button Clicked");
            navigateToFragment(v, R.id.action_animalHomeFragment_to_animalReminderFragment);
        });

        // Initialize Notification Channel
        createNotificationChannel(
                "MEDICATION_CHANNEL",
                "Medication Reminder Channel",
                "Channel for animal medication and other reminders"
        );

        return view;
    }

    // Method to navigate to the specified fragment
    private void navigateToFragment(View view, int actionId) {
        try {
            NavController navController = Navigation.findNavController(view);
            int currentDestinationId = navController.getCurrentDestination().getId();
            Log.d(TAG, "Current Destination ID: " + currentDestinationId);

            // Check to ensure we are at the correct starting fragment
            if (currentDestinationId == R.id.nav_animal) {
                navController.navigate(actionId);
                Log.d(TAG, "Navigation successful to action: " + actionId);
            } else {
                Log.e(TAG, "Incorrect starting fragment for navigation. Current destination: " + currentDestinationId);
            }

        } catch (IllegalArgumentException e) {
            // Handle navigation error: print to log or show user message
            e.printStackTrace();
            Log.e(TAG, "Navigation action could not be found: " + e.getMessage());
        }
    }

    // Method to create the notification channel
    private void createNotificationChannel(String channelId, String channelName, String channelDescription) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                if (notificationManager.getNotificationChannel(channelId) == null) {
                    notificationManager.createNotificationChannel(channel);
                    Log.d(TAG, "Notification channel created: " + channelId);
                } else {
                    Log.d(TAG, "Notification channel already exists: " + channelId);
                }
            }
        }
    }
}
