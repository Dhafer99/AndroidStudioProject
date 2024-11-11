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
    private FragmentServiceajoutBinding  binding;
    private TextView txtDateTime;
    private Button btnSelectDateTime;
    private int year, month, day, hour, minute;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        serviceAddViewModel serviceAddViewModel = new ViewModelProvider(this).get(serviceAddViewModel.class);

        binding = FragmentServiceajoutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button ajoutService = binding.ajouterBtn;
        ajoutService.setOnClickListener(v -> {
            String name = binding.nameEditText.getText().toString().trim();
            String Description = binding.descriptionText.getText().toString().trim();
            String phone = binding.phoneText.getText().toString().trim();
            String place = binding.placeText.getText().toString().trim();
           // String startDate = binding.startDate.getText().toString().trim();
            String endDate = binding.endDateText.getText().toString().trim();
           // String price = binding.priceText.getText().toString().trim();
            serviceRepository = new ServiceRepository(getContext());
            ServiceEntity   serviceentity =  new ServiceEntity();
            serviceentity.setName("Name: " +name);
            serviceentity.setDescription("Description: " +Description);
            serviceentity.setPhone("Phone: " +phone);
            serviceentity.setPlace("Location: " +place);
            //serviceentity.setStartDate("Start Date: " +startDate);
            serviceentity.setEndDate("End Date: " +endDate);
            //serviceentity.setPrice("Price: " +price);

            serviceRepository.insertService(serviceentity);

            txtDateTime = root.findViewById(R.id.txt_date_time);
            btnSelectDateTime = root.findViewById(R.id.btn_select_date_time);

            // Get the current date and time
            final Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);

            btnSelectDateTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Show DatePicker
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                    year = selectedYear;
                                    month = selectedMonth;
                                    day = selectedDay;

                                    // After selecting date, show TimePicker
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                                                    hour = selectedHour;
                                                    minute = selectedMinute;

                                                    // Display the selected Date and Time
                                                    Calendar selectedDateTime = Calendar.getInstance();
                                                    selectedDateTime.set(year, month, day, hour, minute);

                                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                                    String dateTime = sdf.format(selectedDateTime.getTime());
                                                    txtDateTime.setText("Selected Date and Time: " + dateTime);
                                                }
                                            }, hour, minute, true);
                                    timePickerDialog.show();
                                }
                            }, year, month, day);
                    datePickerDialog.show();
                }
            });



            //serviceajoutViewModel.signUp(name, Description, phone, place,startDate,endDate,price);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}