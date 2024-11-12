package com.example.mobile.ui.animal;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.mobile.R;
import com.example.mobile.database.NotificationReceiver;
import com.example.mobile.database.ReminderEntity;
import com.example.mobile.ui.animal.ReminderViewModel;

public class AnimalReminderFragment extends Fragment {

    private EditText editTextReminderTitle, editTextReminderDescription;
    private Button buttonSaveReminder;
    private ReminderViewModel reminderViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);

        // Initialize UI components
        editTextReminderTitle = view.findViewById(R.id.editTextReminderTitle);
        editTextReminderDescription = view.findViewById(R.id.editTextReminderDescription);
        buttonSaveReminder = view.findViewById(R.id.buttonSaveReminder);

        // Initialize ViewModel
        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);

        // Set click listener for the save button
        buttonSaveReminder.setOnClickListener(v -> saveReminder());

        return view;
    }

    private void saveReminder() {
        // Collect data from the user input
        String title = editTextReminderTitle.getText().toString().trim();
        String description = editTextReminderDescription.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // For simplicity, schedule a reminder 10 seconds from now (this can be modified later)
        long reminderTime = System.currentTimeMillis() + 10000;

        // Create ReminderEntity to save into the database
        ReminderEntity reminder = new ReminderEntity();
        reminder.setTitle(title);
        reminder.setDescription(description);
        reminder.setChannelId("MEDICATION_CHANNEL");
        reminder.setTimeInMillis(reminderTime);

        // Save reminder using ViewModel
        reminderViewModel.insert(reminder);

        // Schedule notification
        scheduleNotification(reminder);

        Toast.makeText(getContext(), "Reminder saved and notification scheduled!", Toast.LENGTH_SHORT).show();

        // Clear input fields after saving
        editTextReminderTitle.setText("");
        editTextReminderDescription.setText("");
    }

    private void scheduleNotification(ReminderEntity reminder) {
        Intent intent = new Intent(getContext(), NotificationReceiver.class);
        intent.putExtra("title", reminder.getTitle());
        intent.putExtra("description", reminder.getDescription());
        intent.putExtra("channelId", reminder.getChannelId());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getContext(),
                reminder.getId(), // Use reminder ID for uniqueness
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    reminder.getTimeInMillis(),
                    pendingIntent
            );
        }
    }
}
