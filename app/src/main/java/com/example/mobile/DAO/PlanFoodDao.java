package com.example.mobile.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.mobile.database.FoodEntity;
import com.example.mobile.database.PlanFoodEntity;
import com.example.mobile.database.relations.FoodWithPlans;
import com.example.mobile.database.relations.PlanFoodCrossRef;
import com.example.mobile.database.relations.PlanWithFoods;
import com.example.mobile.database.repositories.FoodRepository;

import java.util.List;

@Dao
public interface PlanFoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertFood(FoodEntity food);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPlan(PlanFoodEntity plan);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlanFoodCrossRef(PlanFoodCrossRef crossRef);

    @Transaction
    @Query("SELECT * FROM foods WHERE foodId = :foodId")
    List<FoodWithPlans> getPlansForFood(long foodId);

    @Query("SELECT * FROM planfood")
    LiveData<List<PlanFoodEntity>> getAllPlans();

    // 1. Retrieve all unique days in the plan (e.g., Monday, Tuesday)
    @Query("SELECT DISTINCT jour FROM planfood")
    List<String> getAllDays();

    // 2. Retrieve meal types (e.g., breakfast, dinner) for a given day
    @Query("SELECT DISTINCT type FROM planfood WHERE jour = :day")
    List<String> getMealTypesForDay(String day);

    // 3. Retrieve PlanFoodEntity for a specific day and meal type
    @Query("SELECT * FROM planfood WHERE jour = :day AND type = :mealType")
    List<PlanFoodEntity> getPlansForDayAndMealType(String day, String mealType);

    // 4. Retrieve FoodEntity items associated with a PlanFoodEntity by planId
    @Query("SELECT * FROM foods INNER JOIN PlanFoodCrossRef ON foods.foodId = PlanFoodCrossRef.foodId WHERE PlanFoodCrossRef.planId = :planId")
    List<FoodEntity> getFoodsForPlan(long planId);

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Remplace l'aliment existant
    void insertOrUpdate(FoodEntity food);

    @Query("SELECT * FROM foods WHERE nom = :foodName")
    FoodEntity getFoodByName(String foodName);

    @Delete
    void deletePlan(PlanFoodEntity plan);

    @Query("DELETE FROM planfood WHERE jour = :day")
    void deletePlansByDay(String day);

    @Delete
    void deletePlanFoodEntity(PlanFoodEntity planFoodEntity);

    @Query("SELECT * FROM planfood WHERE jour = :day")
    List<PlanFoodEntity> getPlansByDay(String day);



}

