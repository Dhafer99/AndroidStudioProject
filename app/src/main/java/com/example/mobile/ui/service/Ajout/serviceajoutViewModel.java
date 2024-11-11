package com.example.mobile.ui.service.Ajout;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mobile.database.ServiceEntity;
import com.example.mobile.database.repositories.ServiceRepository;
import com.example.mobile.databinding.FragmentServiceaddBinding;

import java.util.List;

public class serviceajoutViewModel extends AndroidViewModel {
    private final MutableLiveData<String> text;
    private ServiceRepository repository;
    private LiveData<List<ServiceEntity>> allServices;
    public serviceajoutViewModel(@NonNull Application application) {
        super(application);
        text = new MutableLiveData<>();
        text.setValue("ajoutService");

        repository = new ServiceRepository(application);
        allServices = repository.getAllServices();
    }

    public LiveData<List<ServiceEntity>> getAllServices() {
        return allServices;
    }
    public LiveData<String> getText() {
        return text;
    }
}
