package com.example.mobile.ui.service.Ajout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mobile.R;
import com.example.mobile.database.ServiceEntity;
import com.example.mobile.database.repositories.ServiceRepository;

public class serviceajoutFragment extends Fragment {

    private EditText nameEditText, descriptionText, phoneText, placeText, priceTextView;
    private DatePicker startDatePicker, endDatePicker;
    private Button addServiceButton;
    private ServiceRepository serviceRepository;

    public serviceajoutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_serviceajout2, container, false);

        // Initialize the views
        nameEditText = rootView.findViewById(R.id.nameEditText);
        descriptionText = rootView.findViewById(R.id.descriptionText);
        phoneText = rootView.findViewById(R.id.phoneText);
        placeText = rootView.findViewById(R.id.placeText);
        priceTextView = rootView.findViewById(R.id.priceText);
        startDatePicker = rootView.findViewById(R.id.start_date_picker);
        endDatePicker = rootView.findViewById(R.id.end_date_picker);
        addServiceButton = rootView.findViewById(R.id.ajouterBtn);

        // Initialize the repository
        serviceRepository = new ServiceRepository(getContext());

        // Set up the button click listener
        addServiceButton.setOnClickListener(v -> addService());

        return rootView;
    }

    private void addService() {
        // Retrieve user input
        String name = nameEditText.getText().toString().trim();
        String description = descriptionText.getText().toString().trim();
        String phone = phoneText.getText().toString().trim();
        String place = placeText.getText().toString().trim();
        String price = priceTextView.getText().toString().trim();

        // Validate input
        if (name.isEmpty() || description.isEmpty() || phone.isEmpty() || place.isEmpty() || price.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the selected start and end date from the DatePicker
        int startDay = startDatePicker.getDayOfMonth();
        int startMonth = startDatePicker.getMonth() + 1;
        int startYear = startDatePicker.getYear();
        String startDate = startYear + "-" + startMonth + "-" + startDay;

        int endDay = endDatePicker.getDayOfMonth();
        int endMonth = endDatePicker.getMonth() + 1;
        int endYear = endDatePicker.getYear();
        String endDate = endYear + "-" + endMonth + "-" + endDay;

        // Create the ServiceEntity object
        ServiceEntity serviceEntity = new ServiceEntity();
        serviceEntity.setName(name);
        serviceEntity.setDescription(description);
        serviceEntity.setPhone(phone);
        serviceEntity.setPlace(place);
        serviceEntity.setPrice(price);
        serviceEntity.setStartDate(startDate);
        serviceEntity.setEndDate(endDate);

        // Insert the data into the database
        serviceRepository.insertService(serviceEntity);

        // Show a success message
        Toast.makeText(getContext(), "Service added successfully!", Toast.LENGTH_SHORT).show();

        // Clear fields after insertion
        nameEditText.setText("");
        descriptionText.setText("");
        phoneText.setText("");
        placeText.setText("");
        priceTextView.setText("");
        startDatePicker.updateDate(startYear, startMonth - 1, startDay);
        endDatePicker.updateDate(endYear, endMonth - 1, endDay);
    }
}
