package com.example.mobile.ui.appointment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobile.R;
import com.example.mobile.database.AppointmentEntity;

public class AppointmentDetailsFragment extends Fragment {


    private ImageView imageViewAppointmentDetails;
    private TextView textViewAppointmentDate, textViewAppointmentPlace, textViewAppointmentDuration;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment_details, container, false);

        // Initialize UI components
        imageViewAppointmentDetails = view.findViewById(R.id.imageViewAppointmentDetails);
        textViewAppointmentDate = view.findViewById(R.id.textViewAppointmentDate);
        textViewAppointmentPlace = view.findViewById(R.id.textViewAppointmentPlace);
        textViewAppointmentDuration = view.findViewById(R.id.textViewAppointmentDuration);

        // Get the passed AnimalEntity from arguments
        if (getArguments() != null) {
            AppointmentEntity selectedAppointment = getArguments().getParcelable("appointment");
            if (selectedAppointment != null) {
                // Populate the fields with the animal data
                textViewAppointmentDate.setText(selectedAppointment.getDate() != null ? selectedAppointment.getDate() : "Unknown");
                textViewAppointmentPlace.setText("Place: " + (selectedAppointment.getPlace() != null ? selectedAppointment.getPlace() : "Unknown"));
                textViewAppointmentDuration.setText("Duration: " + selectedAppointment.getDuration());


            }
        }

        return view;
    }
}
