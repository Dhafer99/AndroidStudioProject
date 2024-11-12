package com.example.mobile.ui.appointment;

import android.os.Bundle;
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

public class AppointmentHomeFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment_home, container, false);

        // Initialize UI components
        Button buttonAddAppointment = view.findViewById(R.id.buttonAddAppointment);
        Button buttonAppointmentList = view.findViewById(R.id.buttonAppointmentList);

        // Set button click listeners
        buttonAddAppointment.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_appointmentHomeFragment_to_appointmentFragment);
        });

        buttonAppointmentList.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_appointmentHomeFragment_to_appointmentListFragment);
        });

        return view;
    }

}
