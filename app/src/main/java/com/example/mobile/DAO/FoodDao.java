package com.example.mobile.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.mobile.database.FoodEntity;

import java.util.List;

@Dao
public interface FoodDao {
    @Query("SELECT * FROM foods")
    LiveData<List<FoodEntity>> getAllFoods();

    @Insert
    void insertFood(FoodEntity foodEntity);

    @Delete
    void deleteFood(FoodEntity foodEntity);

    @Query("DELETE FROM PlanFoodCrossRef WHERE foodId = :foodId")
    void deleteFoodRelations(long foodId);

    @Transaction
    default void deleteFoodWithRelations(FoodEntity foodEntity) {
        deleteFoodRelations(foodEntity.getFoodId());
        deleteFood(foodEntity);
    }

    @Query("SELECT planId FROM PlanFoodCrossRef WHERE foodId = :foodId")
    List<Long> getPlanIdsForFood(long foodId);

    @Query("DELETE FROM planfood WHERE planId = :planId")
    void deletePlan(long planId);

    @Query("SELECT COUNT(*) FROM PlanFoodCrossRef WHERE planId = :planId")
    int getPlanAssociationCount(long planId);

    @Update
    void updateFood(FoodEntity food);

}

