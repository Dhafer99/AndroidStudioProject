package com.example.mobile.database.repositories;


import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.mobile.DAO.ReminderDao;
import com.example.mobile.database.ReminderDatabase;
import com.example.mobile.database.ReminderEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReminderRepository {
    private ReminderDao reminderDao;
    private LiveData<List<ReminderEntity>> allReminders;
    private final ExecutorService executorService;

    public ReminderRepository(Application application) {
        ReminderDatabase database = ReminderDatabase.getInstance(application);
        reminderDao = database.reminderDao();
        allReminders = reminderDao.getAllReminders();
        executorService = Executors.newFixedThreadPool(2);
    }

    public void insert(ReminderEntity reminder) {
        executorService.execute(() -> reminderDao.insert(reminder));
    }

    public void update(ReminderEntity reminder) {
        executorService.execute(() -> reminderDao.update(reminder));
    }

    public void delete(ReminderEntity reminder) {
        executorService.execute(() -> reminderDao.delete(reminder));
    }

    public LiveData<List<ReminderEntity>> getAllReminders() {
        return allReminders;
    }
}

