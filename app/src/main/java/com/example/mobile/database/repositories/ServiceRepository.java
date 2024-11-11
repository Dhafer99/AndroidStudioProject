package com.example.mobile.database.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.mobile.DAO.ServiceDao;
import com.example.mobile.database.DatabaseProvider;
import com.example.mobile.database.PetCareDatabase;
import com.example.mobile.database.ServiceEntity;
import com.example.mobile.database.UserEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceRepository {
    private final ExecutorService executorService;
    private LiveData<List<ServiceEntity>> allServices;
  private ServiceDao servicedao;
    public ServiceRepository(Context context) {
        PetCareDatabase db = DatabaseProvider.getDatabase(context);
        servicedao = db.serviceDao();
        executorService = Executors.newSingleThreadExecutor();
        allServices = servicedao.getAllServices();
    }



  public  void insertService(ServiceEntity service) {

      executorService.execute(() -> servicedao.insertService(service));
        }

    public LiveData<List<ServiceEntity>> getAllServices() {
        return allServices ;
    }


}
