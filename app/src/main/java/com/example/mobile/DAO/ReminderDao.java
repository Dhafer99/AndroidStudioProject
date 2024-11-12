package com.example.mobile.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mobile.database.ReminderEntity;

import java.util.List;

@Dao
public interface ReminderDao {
    @Insert
    void insert(ReminderEntity reminder);

    @Update
    void update(ReminderEntity reminder);

    @Delete
    void delete(ReminderEntity reminder);

    @Query("SELECT * FROM reminders WHERE id = :id")
    ReminderEntity getReminderById(int id);

    @Query("SELECT * FROM reminders")
    LiveData<List<ReminderEntity>> getAllReminders();
}
