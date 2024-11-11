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

public class AppointmentEditFragment extends Fragment {


    private EditText editTextDate, editTextPlace, editTextDuration;
    private Button buttonSaveChanges;
    private AppointmentViewModel appointmentViewModel;
    private AppointmentEntity appointmentToEdit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment_edit, container, false);

        // Initialize UI components
        editTextDate = view.findViewById(R.id.editTextDate);
        editTextPlace = view.findViewById(R.id.editTextPlace);
        editTextDuration = view.findViewById(R.id.editTextDuration);
        buttonSaveChanges = view.findViewById(R.id.buttonSaveChanges);

        // Initialize ViewModel
        appointmentViewModel = new ViewModelProvider(this).get(AppointmentViewModel.class);

        // Get the passed AnimalEntity from arguments (if any)
        if (getArguments() != null) {
            appointmentToEdit = (AppointmentEntity) getArguments().getSerializable("selectedAppointment");
            if (appointmentToEdit != null) {
                // Populate the fields with current appointment data
                editTextDate.setText(appointmentToEdit.getDate());
                editTextPlace.setText(appointmentToEdit.getPlace());
                editTextDuration.setText(String.valueOf(appointmentToEdit.getDuration()));
            }
        }

        // Set button click listener
        buttonSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAppointment(v);
            }
        });

        return view;
    }

    private void updateAppointment(View view) {
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
            Toast.makeText(requireContext(), "Please enter a valid age", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the appointment entity
        if (appointmentToEdit != null) {
            appointmentToEdit.setDate(date);
            appointmentToEdit.setPlace(place);
            appointmentToEdit.setDuration(duration);
            appointmentViewModel.update(appointmentToEdit);
            Toast.makeText(requireContext(), "Appointment updated successfully", Toast.LENGTH_SHORT).show();

            // Navigate back to the list after saving changes
            NavController navController = Navigation.findNavController(view);
            navController.popBackStack(); // Go back to the previous fragment
        }
    }
}
