package com.example.mobile.ui.service.affichage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.database.ServiceEntity;
import com.example.mobile.database.repositories.ServiceRepository;
import com.example.mobile.ui.service.Ajout.serviceajoutViewModel;

public class ServiceaffichageFragment extends Fragment implements ServiceAdapter.OnServiceActionListener {

    private ServiceajoutViewModel serviceViewModel;
    private ServiceAdapter serviceAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_list, container, false);

        // Initialize ViewModel
        ServiceajoutViewModelFactory factory = new ServiceajoutViewModelFactory(new ServiceRepository(getContext()));
        serviceViewModel = new ViewModelProvider(this, factory).get(ServiceajoutViewModel.class);

        // Set up RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the adapter and pass the listener (this)
        serviceAdapter = new ServiceAdapter(getContext(), this);  // Pass the listener here

        recyclerView.setAdapter(serviceAdapter);

        // Observe the LiveData from the ViewModel and update the adapter
        serviceViewModel.getServiceList().observe(getViewLifecycleOwner(), serviceEntities -> {
            serviceAdapter.setServiceList(serviceEntities);
        });

        return view;
    }

    // Implement the onEdit method from the listener
    @Override
    public void onEdit(ServiceEntity service) {
        openEditDialog(service);  // Open the dialog to edit the service
    }

    @Override
    public void onDelete(ServiceEntity service) {
        // Show a confirmation dialog before deleting
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Service")
                .setMessage("Are you sure you want to delete this service?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Perform the delete action in ViewModel
                    serviceViewModel.delete(service);
                    dialog.dismiss();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void openEditDialog(ServiceEntity service) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_service, null);
        builder.setView(dialogView);

        // Initialize dialog views
        EditText editName = dialogView.findViewById(R.id.edit_name);
        EditText editDescription = dialogView.findViewById(R.id.edit_description);
        EditText editPhone = dialogView.findViewById(R.id.edit_phone);
        EditText editPlace = dialogView.findViewById(R.id.edit_place);
        EditText editStartDate = dialogView.findViewById(R.id.edit_start_date);
        EditText editEndDate = dialogView.findViewById(R.id.edit_end_date);
        EditText editPrice = dialogView.findViewById(R.id.edit_price);

        // Set existing values in the dialog
        editName.setText(service.getName());
        editDescription.setText(service.getDescription());
        editPhone.setText(service.getPhone());
        editPlace.setText(service.getPlace());
        editStartDate.setText(service.getStartDate());
        editEndDate.setText(service.getEndDate());
        editPrice.setText(service.getPrice());

        // Configure the dialog buttons
        builder.setPositiveButton("Save", (dialog, which) -> {
            // Update service entity with new values
            service.setName(editName.getText().toString());
            service.setDescription(editDescription.getText().toString());
            service.setPhone(editPhone.getText().toString());
            service.setPlace(editPlace.getText().toString());
            service.setStartDate(editStartDate.getText().toString());
            service.setEndDate(editEndDate.getText().toString());
            service.setPrice(editPrice.getText().toString());

            // Update service in ViewModel
            serviceViewModel.update(service);
            dialog.dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
