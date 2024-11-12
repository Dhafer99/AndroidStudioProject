package com.example.mobile.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.lifecycle.LiveData;
import com.example.mobile.database.ServiceEntity;
import java.util.List;

@Dao
public interface ServiceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertService(ServiceEntity service);

    @Query("SELECT * FROM services")
    LiveData<List<ServiceEntity>> getAllServices();

    @Update
    void updateService(ServiceEntity service);

    @Delete
    void deleteService(ServiceEntity service);
}
