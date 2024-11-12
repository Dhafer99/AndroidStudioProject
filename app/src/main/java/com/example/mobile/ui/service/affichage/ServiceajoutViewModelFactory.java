package com.example.mobile.ui.service.affichage;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobile.database.repositories.ServiceRepository;

public class ServiceajoutViewModelFactory implements ViewModelProvider.Factory {
    private final ServiceRepository serviceRepository;

    public ServiceajoutViewModelFactory(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ServiceajoutViewModel.class)) {
            return (T) new ServiceajoutViewModel(serviceRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
