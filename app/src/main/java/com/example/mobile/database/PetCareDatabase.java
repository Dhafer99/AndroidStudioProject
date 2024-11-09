package com.example.mobile.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.mobile.DAO.PlanFoodDao;
import com.example.mobile.DAO.UserDao;
import com.example.mobile.database.relations.PlanFoodCrossRef;

@Database(entities = {UserEntity.class, ServiceEntity.class, FoodEntity.class, AnimalEntity.class, AppointmentEntity.class, PlanFoodEntity.class, PlanFoodCrossRef.class}, version = 3)
public abstract class PetCareDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract PlanFoodDao planFoodDao();
   // public abstract ServiceDao serviceDao();
   // public abstract FoodDao foodDao();
   // public abstract AnimalDao animalDao();
    //public abstract AppointmentDao appointmentDao();
}
