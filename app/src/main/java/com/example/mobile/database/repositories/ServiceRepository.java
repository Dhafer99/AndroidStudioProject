package com.example.mobile.database.repositories;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.example.mobile.DAO.ServiceDao;
import com.example.mobile.database.DatabaseProvider;
import com.example.mobile.database.PetCareDatabase;
import com.example.mobile.database.ServiceEntity;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceRepository {
    private final ExecutorService executorService;
    private final ServiceDao serviceDao;
    private final LiveData<List<ServiceEntity>> allServices;

    public ServiceRepository(Context context) {
        PetCareDatabase db = DatabaseProvider.getDatabase(context);
        serviceDao = db.serviceDao();
        executorService = Executors.newSingleThreadExecutor();
        allServices = serviceDao.getAllServices();
    }

    public void insertService(ServiceEntity service) {
        executorService.execute(() -> serviceDao.insertService(service));
    }

    public LiveData<List<ServiceEntity>> getAllServices() {
        return allServices;
    }

    public void updateService(ServiceEntity service) {
        executorService.execute(() -> serviceDao.updateService(service));
    }

    public void deleteService(ServiceEntity service) {
        executorService.execute(() -> serviceDao.deleteService(service));
    }
}
