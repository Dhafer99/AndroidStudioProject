package com.example.mobile.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.mobile.DAO.AnimalDao;
import com.example.mobile.DAO.AppointmentDao;
import com.example.mobile.DAO.UserDao;

@Database(entities = {UserEntity.class, ServiceEntity.class, FoodEntity.class, AnimalEntity.class, AppointmentEntity.class}, version =9, exportSchema = false)
public abstract class PetCareDatabase extends RoomDatabase {
    public abstract UserDao userDao();
   // public abstract ServiceDao serviceDao();
   // public abstract FoodDao foodDao();
   public abstract AnimalDao animalDao();
    public abstract AppointmentDao appointmentDao();
}
