package com.example.mobile.ui.appointment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.database.AppointmentEntity;
import com.example.mobile.ui.animal.AnimalAdapter;
import java.util.ArrayList;
import java.util.List;

public class AppointmentListFragment extends Fragment implements AppointmentAdapter.OnAppointmentActionListener{

    private RecyclerView recyclerViewAppointments;
    private AppointmentAdapter appointmentAdapter;
    private AppointmentViewModel appointmentViewModel;
    private List<AppointmentEntity> appointmentList = new ArrayList<>();
    private int userId = 1; // Replace with the actual user ID logic


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment_list, container, false);
        // Initialize the RecyclerView
        recyclerViewAppointments = view.findViewById(R.id.recyclerViewAppointments);
        recyclerViewAppointments.setLayoutManager(new LinearLayoutManager(getContext()));
        // Set up the adapter with the action listener
        appointmentAdapter = new AppointmentAdapter(appointmentList, getContext(), this);
        recyclerViewAppointments.setAdapter(appointmentAdapter);
        // Initialize the ViewModel
        appointmentViewModel = new ViewModelProvider(this).get(AppointmentViewModel.class);
        // Observe the list of animals for the current user and update the RecyclerView
        appointmentViewModel.getUserWithAppointments(userId).observe(getViewLifecycleOwner(), userWithAppointments -> {
            if (userWithAppointments != null && userWithAppointments.appointments != null) {
                appointmentList.clear();
                appointmentList.addAll(userWithAppointments.appointments);
                appointmentAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    @Override
    public void onEdit(AppointmentEntity appointment) {
        // Handle edit functionality here (implementation can be added later)
        Toast.makeText(getContext(), "Edit feature not implemented yet.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDelete(AppointmentEntity appointment) {
        // Show confirmation dialog before deleting
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Appointment")
                .setMessage("Are you sure you want to delete this appointment?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    appointmentViewModel.delete(appointment);
                    Toast.makeText(getContext(), "Appointment deleted successfully.", Toast.LENGTH_SHORT).show();

                    // Refresh the list after deletion
                    appointmentViewModel.getUserWithAppointments(userId).observe(getViewLifecycleOwner(), userWithAppointments -> {
                        if (userWithAppointments != null && userWithAppointments.appointments != null) {
                            appointmentList.clear();
                            appointmentList.addAll(userWithAppointments.appointments);
                            appointmentAdapter.notifyDataSetChanged();
                        }
                    });
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }
}

