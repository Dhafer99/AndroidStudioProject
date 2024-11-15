package com.example.mobile.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.mobile.DAO.FoodDao;
import com.example.mobile.DAO.PlanFoodCrossRefDao;
import com.example.mobile.DAO.PlanFoodDao;
import com.example.mobile.DAO.ServiceDao;
import com.example.mobile.DAO.AnimalDao;
import com.example.mobile.DAO.UserDao;
import com.example.mobile.database.relations.PlanFoodCrossRef;
@TypeConverters
@Database(entities = {UserEntity.class, ServiceEntity.class, FoodEntity.class, AnimalEntity.class, AppointmentEntity.class, PlanFoodEntity.class, PlanFoodCrossRef.class}, version = 3)
public abstract class PetCareDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    public abstract PlanFoodDao planFoodDao();

    public abstract PlanFoodCrossRefDao planFoodCrossRefDao();

    public abstract FoodDao foodDao();


    // public abstract ServiceDao serviceDao();
    // public abstract AnimalDao animalDao();
   public abstract ServiceDao serviceDao();
   // public abstract FoodDao foodDao();
   public abstract AnimalDao animalDao();
    //public abstract AppointmentDao appointmentDao();

}
