package com.example.mobile.database.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mobile.DAO.AppointmentDao;
import com.example.mobile.database.AppointmentEntity;
import com.example.mobile.database.DatabaseProvider;
import com.example.mobile.database.PetCareDatabase;
import com.example.mobile.database.relations.UserWithAppointments;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppointmentRepository {



    private final AppointmentDao appointmentDao;
    private final LiveData<List<AppointmentEntity>> allAppointmentsLiveData;
    private final ExecutorService executorService;

    public AppointmentRepository(Context context) {
        PetCareDatabase db = DatabaseProvider.getDatabase(context);
        appointmentDao = db.appointmentDao();
        allAppointmentsLiveData = appointmentDao.getAllAppointments();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(AppointmentEntity appointment, int ownerId) {
        appointment.setOwnerId(ownerId);
        executorService.execute(() -> appointmentDao.insertAppointment(appointment));
    }

    public void update(AppointmentEntity appointment) {
        executorService.execute(() -> appointmentDao.updateAppointment(appointment));
    }

    public void delete(AppointmentEntity appointment) {
        executorService.execute(() -> appointmentDao.deleteAnimal(appointment));
    }

    public LiveData<List<AppointmentEntity>> getallAppointments() {
        return allAppointmentsLiveData;

    }

    public LiveData<UserWithAppointments> getUserWithAppointments(int userId) {
        MutableLiveData<UserWithAppointments> liveData = new MutableLiveData<>();
        executorService.execute(() -> {
            UserWithAppointments userWithAppointments = appointmentDao.getUserWithAppointments(userId);
            liveData.postValue(userWithAppointments);
        });
        return liveData;
    }


}
