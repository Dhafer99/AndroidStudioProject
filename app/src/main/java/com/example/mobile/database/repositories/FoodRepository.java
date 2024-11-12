package com.example.mobile.database.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mobile.DAO.FoodDao;
import com.example.mobile.database.DatabaseProvider;
import com.example.mobile.database.FoodEntity;
import com.example.mobile.database.PetCareDatabase;

import java.util.List;

public class FoodRepository {
    private FoodDao foodDao;

    public FoodRepository(Application application) {
        PetCareDatabase db = DatabaseProvider.getDatabase(application);
        this.foodDao = db.foodDao();
    }

    public LiveData<List<FoodEntity>> getAllFoods() {
        return foodDao.getAllFoods();
    }

    public void insertFood(FoodEntity foodEntity) {
        DatabaseProvider.getDatabaseWriteExecutor().execute(() -> {
            foodDao.insertFood(foodEntity);  // Call insert method of FoodDao
        });
    }

    public void deleteFood(FoodEntity foodEntity) {
        new Thread(() -> {
            long foodId = foodEntity.getFoodId();

            // Step 1: Get list of plan IDs associated with this food
            List<Long> planIds = foodDao.getPlanIdsForFood(foodId);

            // Step 2: Delete the relations in PlanFoodCrossRef
            foodDao.deleteFoodRelations(foodId);

            // Step 3: Check and delete each plan if no other food references it
            for (Long planId : planIds) {
                int associationCount = foodDao.getPlanAssociationCount(planId);
                if (associationCount == 0) {
                    foodDao.deletePlan(planId);  // Only delete if no other food is related
                }
            }

            // Step 4: Delete the food itself
            foodDao.deleteFood(foodEntity);
        }).start();
    }

    public void updateFood(FoodEntity foodEntity) {
        new Thread(() -> {
            // Assuming you have a FoodDao instance (foodDao)
            foodDao.updateFood(foodEntity);  // Update the food in the database
        }).start();
    }

    /*public void deleteFood(FoodEntity foodEntity) {
        new Thread(() -> foodDao.deleteFoodWithRelations(foodEntity)).start();
    }*/


}
