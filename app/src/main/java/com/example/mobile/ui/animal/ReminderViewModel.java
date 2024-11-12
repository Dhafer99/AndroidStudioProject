package com.example.mobile.ui.animal;


import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.mobile.database.ReminderEntity;
import com.example.mobile.database.repositories.ReminderRepository;

import java.util.List;

public class ReminderViewModel extends AndroidViewModel {
    private ReminderRepository repository;
    private LiveData<List<ReminderEntity>> allReminders;

    public ReminderViewModel(@NonNull Application application) {
        super(application);
        repository = new ReminderRepository(application);
        allReminders = repository.getAllReminders();
    }

    public void insert(ReminderEntity reminder) {
        repository.insert(reminder);
    }

    public void update(ReminderEntity reminder) {
        repository.update(reminder);
    }

    public void delete(ReminderEntity reminder) {
        repository.delete(reminder);
    }

    public LiveData<List<ReminderEntity>> getAllReminders() {
        return allReminders;
    }
}

