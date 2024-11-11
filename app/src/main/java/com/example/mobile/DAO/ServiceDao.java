package com.example.mobile.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.mobile.database.FoodEntity;
import com.example.mobile.database.PlanFoodEntity;
import com.example.mobile.database.ServiceEntity;

import java.util.List;
@Dao
public interface ServiceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertService(ServiceEntity service);

    @Query("SELECT * FROM services")
    List<ServiceEntity> getAllEntretiens();
}