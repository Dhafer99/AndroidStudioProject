package com.example.mobile.ui.appointment;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.mobile.database.AppointmentEntity;
import com.example.mobile.database.relations.UserWithAppointments;
import com.example.mobile.database.repositories.AppointmentRepository;
import java.util.List;
public class AppointmentViewModel  extends AndroidViewModel {

    private final AppointmentRepository repository;
    private final LiveData<List<AppointmentEntity>> allAppointments;

    public AppointmentViewModel(@NonNull Application application) {
        super(application);
        repository = new AppointmentRepository(application);
        allAppointments = repository.getallAppointments();
    }

    public void insert(AppointmentEntity appointment, int userId) {repository.insert(appointment, userId);
    }

    public void update(AppointmentEntity appointment) {
        repository.update(appointment);
    }

    public void delete(AppointmentEntity appointment) {
        repository.delete(appointment);
    }

    public LiveData<List<AppointmentEntity>> getallAppointments() {
        return allAppointments;
    }

    public LiveData<UserWithAppointments> getUserWithAppointments(int userId) {
        return repository.getUserWithAppointments(userId);
    }

}
