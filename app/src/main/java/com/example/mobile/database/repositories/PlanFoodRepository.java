package com.example.mobile.database.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mobile.DAO.PlanFoodCrossRefDao;
import com.example.mobile.DAO.PlanFoodDao;
import com.example.mobile.database.DatabaseProvider;
import com.example.mobile.database.FoodEntity;
import com.example.mobile.database.PetCareDatabase;
import com.example.mobile.database.PlanFoodEntity;
import com.example.mobile.dto.DayMealPlan;
import com.example.mobile.dto.MealTypeFood;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PlanFoodRepository {
    private PlanFoodDao planDao;
    private LiveData<List<PlanFoodEntity>> allPlans;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private PlanFoodCrossRefDao planFoodCrossRefDao;

    public PlanFoodRepository(Application application) {
        PetCareDatabase db = DatabaseProvider.getDatabase(application);
        planDao = db.planFoodDao();
        planFoodCrossRefDao = db.planFoodCrossRefDao();  // Initialize this
        allPlans = planDao.getAllPlans();
    }

    public void deleteAllPlanFoodCrossRefs() {
        DatabaseProvider.getDatabaseWriteExecutor().execute(() -> planFoodCrossRefDao.deleteAll());
    }

    public LiveData<List<PlanFoodEntity>> getAllPlans() {
        return allPlans;
    }

    public long insert(PlanFoodEntity plan) {
        Future<Long> resultFuture = DatabaseProvider.getDatabaseWriteExecutor().submit(() -> {
            return planDao.insertPlan(plan); // Return the result from planDao
        });

        try {
            // Wait for the result and return it
            return resultFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return -1; // or some other error code
        }
    }

    // Updated method to return LiveData<List<DayMealPlan>>
    public LiveData<List<DayMealPlan>> getAllDayMealPlans() {
        MutableLiveData<List<DayMealPlan>> dayMealPlansLiveData = new MutableLiveData<>();

        executor.execute(() -> {
            List<DayMealPlan> dayMealPlans = new ArrayList<>();
            List<String> days = planDao.getAllDays();

            for (String day : days) {
                List<MealTypeFood> mealTypeFoods = new ArrayList<>();
                List<String> mealTypes = planDao.getMealTypesForDay(day);

                for (String mealType : mealTypes) {
                    List<PlanFoodEntity> plans = planDao.getPlansForDayAndMealType(day, mealType);
                    List<FoodEntity> foodItems = new ArrayList<>();

                    for (PlanFoodEntity plan : plans) {
                        foodItems.addAll(planDao.getFoodsForPlan(plan.getPlanId()));
                    }

                    MealTypeFood mealTypeFood = new MealTypeFood(mealType, foodItems);
                    mealTypeFoods.add(mealTypeFood);
                }

                DayMealPlan dayMealPlan = new DayMealPlan(day, mealTypeFoods);
                dayMealPlans.add(dayMealPlan);
            }

            dayMealPlansLiveData.postValue(dayMealPlans);
        });

        return dayMealPlansLiveData;
    }

    public LiveData<List<PlanFoodEntity>> getPlansByDay(String day) {
        MutableLiveData<List<PlanFoodEntity>> plansLiveData = new MutableLiveData<>();

        // Execute the query in a background thread
        executor.execute(() -> {
            List<PlanFoodEntity> plans = planDao.getPlansByDay(day);
            // Post the results to LiveData on the main thread
            plansLiveData.postValue(plans);
        });

        return plansLiveData;
    }



    public void deletePlanFoodEntity(PlanFoodEntity planFoodEntity) {
        executor.execute(() -> planDao.deletePlanFoodEntity(planFoodEntity));
    }

    public void deletePlansByDay(String day) {
        executor.execute(() -> planDao.deletePlansByDay(day));
    }
}
