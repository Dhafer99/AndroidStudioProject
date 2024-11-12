package com.example.mobile.DAO;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.mobile.database.AnimalEntity;
import com.example.mobile.database.AppointmentEntity;
import com.example.mobile.database.relations.UserWithAppointments;

import java.util.List;

@Dao
public interface AppointmentDao {


    @Insert
    void insertAppointment(AppointmentEntity appointment);

    @Update
    void updateAppointment(AppointmentEntity appointment);

    @Delete
    void deleteAnimal(AppointmentEntity appointment);

    @Query("SELECT * FROM appointments")
    LiveData<List<AppointmentEntity>> getAllAppointments();


    @Query("SELECT * FROM appointments WHERE appointmentId = :id")
    AppointmentEntity getAppointmentById(int id);

    @Query("SELECT * FROM appointments WHERE ownerId = :ownerId")
    List<AppointmentEntity> getAppointmentsByOwnerId(int ownerId);

    @Transaction
    @Query("SELECT * FROM users WHERE userId = :userId")
    UserWithAppointments getUserWithAppointments(int userId);


}
