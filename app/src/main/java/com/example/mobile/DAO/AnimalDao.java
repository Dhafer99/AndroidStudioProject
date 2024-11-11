package com.example.mobile.DAO;

import androidx.core.text.util.LocalePreferences;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.*;
import com.example.mobile.database.AnimalEntity;
import com.example.mobile.database.UserEntity;
import com.example.mobile.database.relations.UserWithAnimals;

import java.util.List;

@Dao
public interface AnimalDao {
    @Insert
    void insertAnimal(AnimalEntity animal);

    @Update
    void updateAnimal(AnimalEntity animal);

    @Delete
    void deleteAnimal(AnimalEntity animal);

    @Query("SELECT * FROM animals")
    LiveData<List<AnimalEntity>> getAllAnimals();


    @Query("SELECT * FROM animals WHERE animalId = :id")
    AnimalEntity getAnimalById(int id);

    @Query("SELECT * FROM animals WHERE ownerId = :ownerId")
    List<AnimalEntity> getAnimalsByOwnerId(int ownerId);

    @Transaction
    @Query("SELECT * FROM users WHERE userId = :userId")
    UserWithAnimals getUserWithAnimals(int userId);

}
