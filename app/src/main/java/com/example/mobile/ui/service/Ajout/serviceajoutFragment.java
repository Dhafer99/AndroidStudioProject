package com.example.mobile.ui.service.Ajout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobile.database.ServiceEntity;
import com.example.mobile.database.repositories.ServiceRepository;
import com.example.mobile.databinding.FragmentServiceaddBinding;
import com.example.mobile.databinding.FragmentServiceajoutBinding;
import com.example.mobile.ui.service.serviceAddViewModel;

public class serviceajoutFragment extends Fragment {
    private ServiceRepository serviceRepository;
    private FragmentServiceajoutBinding  binding;
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
            String startDate = binding.startDateText.getText().toString().trim();
            String endDate = binding.endDateText.getText().toString().trim();
            String price = binding.priceText.getText().toString().trim();
            serviceRepository = new ServiceRepository(getContext());
            ServiceEntity   serviceentity =  new ServiceEntity();
            serviceentity.setName(name);
            serviceentity.setDescription(Description);
            serviceentity.setPhone(phone);
            serviceentity.setPlace(place);
            serviceentity.setStartDate(startDate);
            serviceentity.setEndDate(endDate);
            serviceentity.setPrice(price);

            serviceRepository.insertService(serviceentity);





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