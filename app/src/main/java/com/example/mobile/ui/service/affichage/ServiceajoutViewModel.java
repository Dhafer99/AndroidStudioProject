package com.example.mobile.ui.service.affichage;
import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.mobile.database.ServiceEntity;
import com.example.mobile.database.repositories.ServiceRepository;
import java.util.List;

public class ServiceajoutViewModel extends ViewModel {
    private final ServiceRepository serviceRepository;
    private final LiveData<List<ServiceEntity>> serviceList;

    public ServiceajoutViewModel(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
        this.serviceList = serviceRepository.getAllServices();
    }

    // Expose service list as LiveData to be observed by the UI
    public LiveData<List<ServiceEntity>> getServiceList() {
        return serviceList;
    }

    public void delete(ServiceEntity service) {
        serviceRepository.deleteService(service);
    }

    public void update(ServiceEntity service) {
        serviceRepository.updateService(service);
    }
}
