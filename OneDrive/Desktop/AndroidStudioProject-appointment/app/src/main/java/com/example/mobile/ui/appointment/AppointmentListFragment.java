package com.example.mobile.ui.appointment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.database.AppointmentEntity;
import java.util.ArrayList;
import java.util.List;

public class AppointmentListFragment extends Fragment implements AppointmentAdapter.OnAppointmentActionListener{

    private RecyclerView recyclerViewAppointments;
    private AppointmentAdapter appointmentAdapter;
    private AppointmentViewModel appointmentViewModel;
    private List<AppointmentEntity> appointmentList = new ArrayList<>();
    private List<AppointmentEntity> filteredAppointmentList = new ArrayList<>();
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
                filteredAppointmentList.clear();
                filteredAppointmentList.addAll(appointmentList);
                appointmentAdapter.notifyDataSetChanged();
            }
        });

        // Initialize the SearchView
        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterAppointments(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterAppointments(newText);
                return false;
            }
        });
        return view;
    }


    private void filterAppointments(String query) {

        filteredAppointmentList.clear();
        if (query.isEmpty()) {
            filteredAppointmentList.addAll(appointmentList);
        } else {
            for (AppointmentEntity appointment : appointmentList) {
                if (appointment.getDate().toLowerCase().contains(query.toLowerCase()) ||
                        appointment.getPlace().toLowerCase().contains(query.toLowerCase()) ||
                        String.valueOf(appointment.getDuration()).contains(query)) {
                    filteredAppointmentList.add(appointment);
                }
            }
        }
        appointmentAdapter.notifyDataSetChanged();
    }


    @Override
    public void onEdit(AppointmentEntity appointment) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("selectedAppointment", appointment);

        NavController navController = Navigation.findNavController(requireActivity(), R.id.appointmentEditFragment);
        navController.navigate(R.id.action_appointmentListFragment_to_appointmentEditFragment, bundle);
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
                            filterAppointments(""); // Reapply filter to ensure correct list state
                        }
                    });
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }


}

