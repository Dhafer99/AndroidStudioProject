package com.example.mobile.ui.appointment;

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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.mobile.R;
import com.example.mobile.database.AppointmentEntity;

public class AppointmentFragment extends Fragment {

    private EditText editTextDate, editTextPlace, editTextDuration;
    private Button buttonSave;
    private AppointmentViewModel appointmentViewModel;
    private int userId = 1; // Replace this with the actual user ID
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);
        // Initialize UI components
        editTextDate = view.findViewById(R.id.editTextDate);
        editTextPlace = view.findViewById(R.id.editTextPlace);
        editTextDuration = view.findViewById(R.id.editTextDuration);
        buttonSave = view.findViewById(R.id.buttonSave);
        // Initialize ViewModel
        appointmentViewModel = new ViewModelProvider(this).get(AppointmentViewModel.class);

        // Set button click listener
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAppointment(v);
            }
        });

        return view;
    }

    private void saveAppointment(View view) {
        String date = editTextDate.getText().toString().trim();
        String place = editTextPlace.getText().toString().trim();
        String durationStr = editTextDuration.getText().toString().trim();

        // Validate inputs
        if (date.isEmpty() || place.isEmpty() || durationStr.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int duration;
        try {
            duration = Integer.parseInt(durationStr);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Please enter a valid duration", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new AnimalEntity and insert it using the ViewModel
        AppointmentEntity newAppointment = new AppointmentEntity();
        newAppointment.setDate(date);
        newAppointment.setPlace(place);
        newAppointment.setDuration(duration);

        appointmentViewModel.insert(newAppointment, userId);
        Toast.makeText(requireContext(), "Appointment added successfully", Toast.LENGTH_SHORT).show();

        // Clear input fields after saving
        editTextDate.setText("");
        editTextPlace.setText("");
        editTextDuration.setText("");
        // Navigate to the display fragment to show the list of animals
        NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.action_appointmentFragment_to_appointmentListFragment);
    }
}





