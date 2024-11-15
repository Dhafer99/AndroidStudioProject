package com.example.mobile.ui.service.Ajout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobile.R;
import com.example.mobile.database.ServiceEntity;
import com.example.mobile.database.repositories.ServiceRepository;
import com.example.mobile.databinding.FragmentServiceajoutBinding;
import com.example.mobile.ui.service.serviceAddViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class serviceajoutFragment1 extends Fragment {
    private ServiceRepository serviceRepository;
    private FragmentServiceajoutBinding binding;
    private TextView txtDateTime;
    private Button btnSelectDateTime;
    private int year, month, day, hour, minute;
    private String selectedDateTime;
    private Calendar selectedDateTimeCal;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        serviceAddViewModel serviceAddViewModel = new ViewModelProvider(this).get(serviceAddViewModel.class);

        binding = FragmentServiceajoutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button ajoutService = binding.ajouterBtn;

        txtDateTime = root.findViewById(R.id.txt_date_time);
        btnSelectDateTime = root.findViewById(R.id.btn_select_date_time);

        // Get the current date and time
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        btnSelectDateTime.setOnClickListener(view -> {
            // Show DatePicker and set the minimum date to today
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        year = selectedYear;
                        month = selectedMonth;
                        day = selectedDay;

                        // After selecting date, show TimePicker
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                                (view2, selectedHour, selectedMinute) -> {
                                    hour = selectedHour;
                                    minute = selectedMinute;

                                    // Display the selected Date and Time
                                    selectedDateTimeCal = Calendar.getInstance();
                                    selectedDateTimeCal.set(year, month, day, hour, minute);

                                    // Validate that the selected date is in the future
                                    if (selectedDateTimeCal.before(Calendar.getInstance())) {
                                        Toast.makeText(getContext(), "Start date must be in the future", Toast.LENGTH_SHORT).show();
                                        selectedDateTime = null;
                                        txtDateTime.setText("");
                                    } else {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                        selectedDateTime = sdf.format(selectedDateTimeCal.getTime());
                                        txtDateTime.setText("Selected Date and Time: " + selectedDateTime);
                                    }
                                }, hour, minute, true);
                        timePickerDialog.show();
                    }, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        });

        ajoutService.setOnClickListener(v -> {
            String name = binding.nameEditText.getText().toString().trim();
            String description = binding.descriptionText.getText().toString().trim();
            String phone = binding.phoneText.getText().toString().trim();
            String place = binding.placeText.getText().toString().trim();
            String endDate = binding.endDateText.getText().toString().trim();

            // Validate phone number
            if (!isValidPhone(phone)) {
                Toast.makeText(getContext(), "Phone number must contain exactly 8 digits", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate selected date and time
            if (selectedDateTime == null || selectedDateTime.isEmpty()) {
                Toast.makeText(getContext(), "Please select a valid future start date", Toast.LENGTH_SHORT).show();
                return;
            }

            // Proceed with creating the service entity
            serviceRepository = new ServiceRepository(getContext());
            ServiceEntity serviceEntity = new ServiceEntity();
            serviceEntity.setName("Name: " + name);
            serviceEntity.setDescription("Description: " + description);
            serviceEntity.setPhone("Phone: " + phone);
            serviceEntity.setPlace("Location: " + place);
            serviceEntity.setEndDate("End Date: " + endDate);

            serviceRepository.insertService(serviceEntity);
            Toast.makeText(getContext(), "Service added successfully", Toast.LENGTH_SHORT).show();
        });

        return root;
    }

    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d{8}");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
